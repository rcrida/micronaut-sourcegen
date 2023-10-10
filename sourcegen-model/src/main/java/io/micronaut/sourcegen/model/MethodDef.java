/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.sourcegen.model;

import io.micronaut.core.annotation.Experimental;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * The method definition.
 *
 * @author Denis Stepanov
 * @since 1.0
 */
@Experimental
public final class MethodDef extends AbstractElement {

    private final TypeDef returnType;
    private final List<ParameterDef> parameters;
    private final List<StatementDef> statements;

    MethodDef(String name,
              EnumSet<Modifier> modifiers,
              TypeDef returnType,
              List<ParameterDef> parameters,
              List<StatementDef> statements) {
        super(name, modifiers);
        this.returnType = returnType;
        this.parameters = Collections.unmodifiableList(parameters);
        this.statements = statements;
    }

    public TypeDef getReturnType() {
        return returnType;
    }

    public List<ParameterDef> getParameters() {
        return parameters;
    }

    public List<StatementDef> getStatements() {
        return statements;
    }

    @Nullable
    public ParameterDef findParameter(String name) {
        for (ParameterDef parameter : parameters) {
            if (parameter.getName().equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    @NonNull
    public ParameterDef getParameter(String name) {
        ParameterDef parameter = findParameter(name);
        if (parameter == null) {
            throw new IllegalStateException("Method: " + name + " doesn't have parameter: " + name);
        }
        return parameter;
    }

    public static MethodDefBuilder builder(String name) {
        return new MethodDefBuilder(name);
    }

    /**
     * The method builder definition.
     *
     * @author Denis Stepanov
     * @since 1.0
     */
    @Experimental
    public static final class MethodDefBuilder extends AbstractElementBuilder<MethodDefBuilder> {

        private final List<ParameterDef> parameters = new ArrayList<>();
        private TypeDef returnType;
        private final List<StatementDef> statements = new ArrayList<>();

        private MethodDefBuilder(String name) {
            super(name);
        }

        public MethodDefBuilder returns(TypeDef type) {
            this.returnType = type;
            return this;
        }

        public MethodDefBuilder addParameter(String name, TypeDef type) {
            parameters.add(new ParameterDef(name, type));
            return this;
        }

        public MethodDefBuilder addStatement(StatementDef statement) {
            statements.add(statement);
            return this;
        }

        public MethodDefBuilder addStatements(Collection<StatementDef> newStatements) {
            statements.addAll(newStatements);
            return this;
        }

        public MethodDef build() {
            if (returnType == null) {
                throw new IllegalStateException("Return type of method: " + name + " not specified!");
            }
            return new MethodDef(name, modifiers, returnType, parameters, statements);
        }

    }
}
