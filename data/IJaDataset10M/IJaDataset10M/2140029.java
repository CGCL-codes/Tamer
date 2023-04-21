package clump.language.ast.specification.expression.impl;

import clump.language.analysis.IEntitySolver;
import clump.language.analysis.exception.EntityCannotBeSolvedException;
import clump.language.ast.IEntity;
import clump.language.ast.definition.entity.IClassDefinition;
import clump.language.ast.definition.entity.IObjectDefinition;
import clump.language.ast.definition.entity.impl.SubstitutedClassDefinition;
import clump.language.ast.definition.entity.impl.SubstitutedObjectDefinition;
import clump.language.ast.specification.entity.IClassSpecification;
import clump.language.ast.specification.entity.IVariantSpecification;
import clump.language.ast.specification.entity.impl.SubstitutedClassSpecification;
import clump.language.ast.specification.entity.impl.SubstitutedVariantSpecification;
import clump.language.ast.specification.expression.IEntityInstance;
import clump.language.compilation.typechecker.impl.TypeSymbolSolver;
import clump.language.compilation.typechecker.impl.Types;
import opala.utils.Predicate;
import java.util.HashMap;
import java.util.Map;

public class ClosedEntityInstance implements IEntityInstance {

    private static final long serialVersionUID = 2337150854775297629L;

    private IEntityInstance thisType;

    private IEntityInstance entityInstance;

    public ClosedEntityInstance(IEntityInstance thisType, IEntityInstance entityInstance) {
        this.thisType = thisType;
        this.entityInstance = entityInstance;
    }

    public boolean isSelfClosure() {
        return thisType.equals(entityInstance);
    }

    public IEntityInstance[] getParameters() {
        return this.entityInstance.getParameters();
    }

    public IEntity solve(IEntitySolver loader) throws EntityCannotBeSolvedException {
        final IEntity entity = entityInstance.solve(loader);
        switch(entity.getDefinitionKind()) {
            case Class:
                {
                    final IClassDefinition definition = (IClassDefinition) entity;
                    final Map<VariableEntityInstance, IEntityInstance> substitutions = new HashMap<VariableEntityInstance, IEntityInstance>();
                    substitutions.put(VariableEntityInstance.SELFTYPE, thisType);
                    substitutions.put(VariableEntityInstance.THISTYPE, Types.doClosure(definition.getObjectType()));
                    final SubstitutedClassDefinition classDefinition = new SubstitutedClassDefinition((IClassDefinition) entity, substitutions);
                    return classDefinition;
                }
            case Interface:
                {
                    final Map<VariableEntityInstance, IEntityInstance> substitutions = new HashMap<VariableEntityInstance, IEntityInstance>();
                    substitutions.put(VariableEntityInstance.SELFTYPE, thisType);
                    return new SubstitutedClassSpecification((IClassSpecification) entity, substitutions);
                }
            case Object:
                {
                    final Map<VariableEntityInstance, IEntityInstance> substitutions = new HashMap<VariableEntityInstance, IEntityInstance>();
                    substitutions.put(VariableEntityInstance.THISTYPE, thisType);
                    return new SubstitutedObjectDefinition((IObjectDefinition) entity, substitutions);
                }
            case Type:
                {
                    final Map<VariableEntityInstance, IEntityInstance> substitutions = new HashMap<VariableEntityInstance, IEntityInstance>();
                    substitutions.put(VariableEntityInstance.SELFTYPE, thisType);
                    substitutions.put(VariableEntityInstance.THISTYPE, thisType);
                    return new SubstitutedVariantSpecification((IVariantSpecification) entity, substitutions);
                }
            default:
                return entity;
        }
    }

    public String[] getPath() {
        return entityInstance.getPath();
    }

    public String getName() {
        return entityInstance.getName();
    }

    public boolean isATypeVariable() {
        return false;
    }

    public IEntityInstance getVirtualType() {
        return thisType;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (String s : getPath()) {
            result.append(s);
            result.append('.');
        }
        result.append(this.getName());
        final IEntityInstance[] parameters = getParameters();
        if (parameters.length > 0) {
            result.append("<");
            String sep = "";
            for (IEntityInstance parameter : parameters) {
                result.append(sep);
                result.append(parameter);
                sep = ",";
            }
            result.append(">");
        }
        return result.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof IEntityInstance) {
            IEntityInstance that = (IEntityInstance) object;
            return this.isATypeVariable() == false && Predicate.isNullOfEquals(getName(), that.getName()) && Predicate.isNullOfEquals(getPath(), that.getPath()) && Predicate.isNullOfEquals(getParameters(), that.getParameters());
        } else {
            return false;
        }
    }
}
