package clump.language.compilation.backend.impl;

import clump.language.analysis.exception.EntityCannotBeSolvedException;
import clump.language.ast.IEntity;
import clump.language.ast.common.IVariableSpecification;
import clump.language.ast.definition.method.IMethodImplementation;
import clump.language.ast.definition.method.impl.ConstructorImplementation;
import clump.language.ast.specification.expression.IEntityInstance;
import clump.language.ast.specification.expression.impl.TopEntity;
import clump.language.ast.specification.expression.impl.VariableEntityInstance;
import clump.language.belang.behavior.IBehavior;
import clump.language.belang.behavior.impl.Constructor;
import clump.language.belang.behavior.impl.MethodImplementation;
import clump.language.belang.statement.IStatement;
import clump.language.belang.type.IType;
import clump.language.compilation.backend.exception.GenerationException;
import opala.utils.Pair;
import java.util.ArrayList;
import java.util.List;

public class MethodImplementationGenerator extends Generator<IMethodImplementation, IBehavior> {

    private final IEntity entity;

    public MethodImplementationGenerator(IEntity entity) {
        this.entity = entity;
    }

    public IBehavior generationUnit(clump.language.ast.definition.method.impl.MethodImplementation definition) throws GenerationException, EntityCannotBeSolvedException {
        final TypeGenerator typeGenerator = new TypeGenerator(entity);
        final IType iType = typeGenerator.getType(definition.getObjectType());
        final String iName = definition.getInternalName();
        final VariableEntityInstance[] generics = definition.getSpecification().getGenerics();
        final String[] iVars = new String[generics.length];
        for (int i = 0; i < iVars.length; i++) {
            iVars[i] = generics[i].getName();
        }
        final List<String> variables = new ArrayList<String>();
        final IVariableSpecification[] parameters = definition.getParameters();
        final Pair<IType, String>[] iParameters = new Pair[parameters.length];
        for (int i = 0; i < iParameters.length; i++) {
            final IVariableSpecification variable = parameters[i];
            final IEntityInstance instance = variable.getType();
            final IType type;
            if (instance == null) {
                type = typeGenerator.getType(TopEntity.SINGLETON);
            } else {
                type = typeGenerator.getType(instance);
            }
            variables.add(variable.getIdentifier().getName());
            iParameters[i] = new Pair<IType, String>(type, variable.getIdentifier().getName());
        }
        final IStatement iBehavior = new StatementGenerator(variables, entity).generate(definition.getBody());
        return new MethodImplementation(definition.getBody().getLocation(), iType, iName, iVars, iParameters, iBehavior);
    }

    public IBehavior generationUnit(ConstructorImplementation definition) throws GenerationException, EntityCannotBeSolvedException {
        final TypeGenerator typeGenerator = new TypeGenerator(entity);
        final String iName = definition.getInternalName();
        final clump.language.belang.expression.IExpression[] superConstructors = new clump.language.belang.expression.IExpression[definition.getCreators().length];
        final Pair<IType, String>[] iParameters = new Pair[definition.getParameters().length];
        final List<String> variables = new ArrayList<String>();
        for (int i = 0; i < iParameters.length; i++) {
            final IVariableSpecification variable = definition.getParameters()[i];
            variables.add(variable.getIdentifier().getName());
            iParameters[i] = new Pair<IType, String>(typeGenerator.getType(variable.getType()), variable.getIdentifier().getName());
        }
        for (int i = 0; i < superConstructors.length; i++) {
            superConstructors[i] = new ExpressionGenerator(variables, entity).generate(definition.getCreators()[i]);
        }
        IStatement iBehavior = new StatementGenerator(variables, entity).generate(definition.getBody());
        return new Constructor(definition.getLocation(), iName, iParameters, iBehavior, superConstructors);
    }
}
