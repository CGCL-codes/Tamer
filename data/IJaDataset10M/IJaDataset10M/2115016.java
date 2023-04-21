package org.nextframework.report.sumary.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.nextframework.report.sumary.Sumary;
import org.nextframework.report.sumary.annotations.CalculationType;
import org.nextframework.report.sumary.annotations.Scope;
import org.nextframework.report.sumary.annotations.Variable;

public class SumaryVariableDefinition implements SumaryItemDefinition {

    String name;

    Variable variable;

    Class<?> type;

    Method method;

    public SumaryVariableDefinition(final Variable variable, String name, Class<?> type, Method method) {
        if (!variable.scopeGroup().equals("") && variable.scope() != Scope.GROUP) {
            this.variable = new Variable() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Variable.class;
                }

                @Override
                public String scopeGroup() {
                    return variable.scopeGroup().equals("report") ? "" : variable.scopeGroup();
                }

                @Override
                public Scope scope() {
                    return variable.scopeGroup().equals("report") ? Scope.REPORT : Scope.GROUP;
                }

                @Override
                public CalculationType calculation() {
                    return variable.calculation();
                }

                @Override
                public String incrementOnGroupChange() {
                    return variable.incrementOnGroupChange();
                }
            };
        } else {
            this.variable = variable;
        }
        this.name = name;
        this.type = type;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n@").append(name).append(" -> ");
        if (variable.calculation() != CalculationType.NONE) {
            builder.append(variable.calculation()).append(", ");
        }
        builder.append(variable.scope());
        return builder.toString();
    }

    @Override
    public Object getValue(Sumary<?> sumary) {
        if (sumary == null) {
            throw new NullPointerException("sumary is null");
        }
        if (!method.getDeclaringClass().isAssignableFrom(sumary.getClass())) {
            throw new IllegalArgumentException("This sumary item is not of the sumary class " + sumary.getClass());
        }
        try {
            return method.invoke(sumary);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Scope getScope() {
        return variable.scope();
    }

    @Override
    public String getScopeGroup() {
        return variable.scopeGroup();
    }
}
