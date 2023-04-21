package clump.language.ast.common.impl;

import clump.language.ast.common.IEntitySpecification;
import clump.language.ast.specification.expression.IEntityInstance;
import clump.language.ast.specification.expression.impl.TopEntity;
import clump.language.ast.specification.expression.impl.VariableEntityInstance;
import clump.message.MessageProvider;
import opala.lexing.ILocation;
import java.util.ArrayList;
import java.util.List;

public class EntitySpecification implements IEntitySpecification {

    private static final long serialVersionUID = -3211412614497056453L;

    private String name;

    private VariableEntityInstance[] variables;

    private IEntityInstance[] bounds;

    private ILocation location;

    public EntitySpecification(ILocation location, String name) {
        this(location, name, new ArrayList<VariableEntityInstance>(), new ArrayList<IEntityInstance>());
    }

    public EntitySpecification(ILocation location, String name, List<VariableEntityInstance> variables, List<IEntityInstance> bounds) {
        this.location = location;
        this.name = name;
        this.variables = variables.toArray(new VariableEntityInstance[variables.size()]);
        this.bounds = bounds.toArray(new IEntityInstance[bounds.size()]);
        assert this.variables.length == this.bounds.length;
    }

    public String getName() {
        return name;
    }

    public VariableEntityInstance[] getGenerics() {
        return variables;
    }

    public IEntityInstance getGenericBound(VariableEntityInstance variable) {
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].equals(variable)) {
                return this.bounds[i];
            }
        }
        throw new IllegalArgumentException(MessageProvider.getMessage("generic.variable.not.defined", new Object[] { variable, this.name }));
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.getName());
        if (variables.length > 0) {
            result.append("<");
            boolean firstOne = true;
            for (VariableEntityInstance variable : variables) {
                if (firstOne) {
                    firstOne = false;
                } else {
                    result.append(",");
                }
                result.append(variable);
                final IEntityInstance bound = this.getGenericBound(variable);
                if (bound.equals(TopEntity.SINGLETON) == false) {
                    result.append(" extends ");
                    result.append(bound.toString());
                }
            }
            result.append(">");
        }
        return result.toString();
    }

    public ILocation getLocation() {
        return this.location;
    }

    public boolean defineTypeVariable(VariableEntityInstance variableEntityInstance) {
        for (VariableEntityInstance variable : variables) {
            if (variable.equals(variableEntityInstance)) {
                return true;
            }
        }
        return false;
    }
}
