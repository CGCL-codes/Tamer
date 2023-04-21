package net.sourceforge.qvtrel2op.compiler;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.qvtparser.model.emof.Operation;
import net.sourceforge.qvtparser.model.essentialocl.OclExpression;
import net.sourceforge.qvtparser.model.essentialocl.OperationCallExp;
import net.sourceforge.qvtparser.model.essentialocl.Variable;
import net.sourceforge.qvtparser.model.essentialocl.VariableExp;
import net.sourceforge.qvtparser.model.essentialocl.IterateExp;
import net.sourceforge.qvtparser.model.imperativeocl.BlockExp;
import net.sourceforge.qvtparser.model.imperativeocl.ForExp;
import net.sourceforge.qvtparser.model.imperativeocl.ImperativeIterateExp;
import net.sourceforge.qvtparser.model.imperativeocl.ImperativeLoopExp;
import net.sourceforge.qvtparser.model.qvtoperational.ObjectExp;
import net.sourceforge.qvtparser.model.qvtoperational.ResolveExp;
import net.sourceforge.qvtparser.model.qvtoperational.ModelParameter;
import net.sourceforge.qvtparser.model.qvtoperational.Helper;
import net.sourceforge.qvtparser.model.qvtoperational.MappingOperation;
import net.sourceforge.qvtparser.model.qvtoperational.MappingParameter;
import net.sourceforge.qvtparser.model.qvtoperational.OperationalTransformation;
import net.sourceforge.qvtparser.model.qvtrelation.Relation;
import net.sourceforge.qvtparser.model.qvtrelation.RelationDomain;
import net.sourceforge.qvtparser.model.qvtrelation.RelationalTransformation;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This abstract class handles the operation context of a
 * relation-to-mapping translation.
 * 
 * @author Pascal Muellender (p_muellender@users.sourceforge.net)
 */
@SuppressWarnings("unchecked")
public abstract class MappingContext {

    /**
	 * The current relational transformation
	 */
    protected static RelationalTransformation currentRelTransformation;

    /**
	 * The current operational transformation.
	 */
    protected static OperationalTransformation currentOpTransformation;

    /**
	 * The current mapping operation.
	 */
    protected MappingOperation mapping;

    /**
	 * The current relation to translate.
	 */
    protected Relation relation;

    /**
	 * The QVT Factory instance to create QVT objects with.
	 */
    protected static QvtFactory qvtFactory;

    /**
	 * The index of context domain in the relation
	 */
    protected int contextDomainIndex;

    /**
	 * The index of the target domain in the relation
	 */
    protected int targetDomainIndex;

    /**
	 * The mapping parameters of the current mapping
	 */
    private Hashtable<String, MappingParameter> mappingParameters;

    /**
	 * Contains the variable bindings of all free variables of the current relation
	 */
    private Hashtable<Variable, ContextVariableData> freeRelationVariables;

    /**
	 * Contains a list of reserved names, that can't be assigned to new variables.
	 */
    private List<String> reservedNames;

    /**
	 * Contains all variables that have to be unequal to zero.
	 */
    protected List<Variable> notNullVariables;

    /**
	 * Constructor
	 */
    protected MappingContext() {
        mappingParameters = new Hashtable<String, MappingParameter>();
        freeRelationVariables = new Hashtable<Variable, ContextVariableData>();
        notNullVariables = new ArrayList<Variable>();
    }

    /**
	 * Returns the value of a variable contained in the mapping context
	 * @param var The variable
	 * @param assignCollection Specifies whether the assigned value can be a collection
	 * @return The expression defining the variable value
	 */
    protected OclExpression getVariableAssignment(Variable var, boolean assignCollection) {
        return getVariableValue(var, assignCollection, null);
    }

    /**
	 * Returns the value of a variable contained in the mapping context.
	 * @param var The variable
	 * @param assignCollection Specifies whether the assigned value can be a collection
	 * @param colVarDestinations Contains collection-variable <-> iterator-variable
	 * relations for the current expression context
	 * @return The expression defining the variable value
	 */
    private OclExpression getVariableValue(Variable var, boolean assignCollection, Hashtable colVarDestinations) {
        OclExpression value = null;
        if (!((var instanceof MappingParameter) && (var.getName().equals("result")))) value = freeRelationVariables.get(var).value;
        if (value != null) {
            List<OclExpression> colRelations = freeRelationVariables.get(var).getRelationalCollectionConstraints();
            value = resolveOclExpressionFromContext(copyExpression(value), true);
            if ((colRelations != null) && (colRelations.size() > 0)) {
                boolean added;
                IterateExp existsExp = null;
                IterateExp relationalIterateExp = null;
                Variable colVar;
                OclExpression relationalCondition;
                ImperativeIterateExp xselect = getXSelectExpression(var, value);
                Variable itVar = (Variable) xselect.getIterator().get(0);
                if (colVarDestinations == null) colVarDestinations = new Hashtable();
                colVarDestinations.put(var, itVar);
                for (Iterator<OclExpression> it = colRelations.iterator(); it.hasNext(); ) {
                    relationalCondition = it.next();
                    relationalCondition = copyExpression(relationalCondition);
                    added = false;
                    for (Iterator<Variable> varIterator = getCollectionVariables(relationalCondition).iterator(); varIterator.hasNext(); ) {
                        colVar = varIterator.next();
                        if ((colVar != var) && !(colVarDestinations.containsKey(colVar))) {
                            relationalIterateExp = qvtFactory.createIterateExp("exists", getVariableValue(colVar, true, colVarDestinations), getUniqueName(colVar.getName().substring(0, 1)));
                            itVar = (Variable) relationalIterateExp.getIterator().get(0);
                            if (existsExp != null) relationalIterateExp.setSource(existsExp);
                            existsExp = relationalIterateExp;
                            addFurtherCondition(relationalIterateExp, resolveRelationsOclExpression(relationalCondition, false));
                            colVarDestinations.put(colVar, itVar);
                            added = true;
                        }
                    }
                    replaceCollectionVars(relationalCondition, colVarDestinations);
                    if (!added) {
                        if (relationalIterateExp != null) addFurtherCondition(relationalIterateExp, relationalCondition); else addFurtherCondition(xselect, relationalCondition);
                    }
                }
                if (relationalIterateExp != null) {
                    replaceCollectionVars(relationalIterateExp, colVarDestinations);
                    addFurtherCondition(xselect, relationalIterateExp);
                }
                value = xselect;
            }
            if (!assignCollection && isCollectionVariable(var)) {
                if (value instanceof ResolveExp) {
                    ResolveExp resolve = (ResolveExp) value;
                    resolve.setOne(Boolean.TRUE);
                } else if (value instanceof ImperativeIterateExp) {
                    ImperativeIterateExp iterateExp = (ImperativeIterateExp) value;
                    if ((iterateExp.getName().equals("xselect")) || (iterateExp.getName().equals("xcollect")) || (iterateExp.getName().equals("collectselect"))) value = qvtFactory.createFirstOperation(value);
                }
            }
        }
        return value;
    }

    /**
	 * Specifies whether an expression is a variable expression bound
	 * to a collection.
	 * @param object The expression
	 * @return <code>true</code> if the expression represents a variable bound to
	 * a collection, <code>false</code> otherwise
	 */
    private boolean isCollectionVariableExp(java.lang.Object object) {
        if ((object instanceof VariableExp) && (isCollectionVariable(((VariableExp) object).getReferredVariable()))) return true;
        return false;
    }

    /**
	 * Specifies, whether a variable is bound to a collection.
	 * @param var The variable
	 * @return <code>true</code> if the expression represents a variable bound to
	 * a collection, <code>false</code> otherwise
	 */
    protected boolean isCollectionVariable(Variable var) {
        ContextVariableData vardata = freeRelationVariables.get(var);
        if (vardata != null) return vardata.isCollection; else return false;
    }

    /**
	 * Adds a binding which defines a relation between two or more collection variables.
	 * @param var The variable to bind
	 * @param relationalCondition The relational condition expression
	 */
    protected void addCollectionRelation(Variable var, OclExpression relationalCondition) {
        freeRelationVariables.get(var).getRelationalCollectionConstraints().add(relationalCondition);
    }

    /**
	 * Returns the xSelect expression of an expression if there exists one, otherwise
	 * it is created and embedded in the given expression. 
	 * @param var The variable to extract an iterator name of
	 * @param exp The expression
	 * @return The xSelect expression
	 */
    private ImperativeIterateExp getXSelectExpression(Variable var, OclExpression exp) {
        if ((!(exp instanceof ImperativeIterateExp)) || (!exp.getName().equals("xselect"))) return qvtFactory.createImperativeIterateExp("xselect", exp, getUniqueName("v")); else return (ImperativeIterateExp) exp;
    }

    /**
	 * Binds a variable contained in the mapping context to an expression value.
	 * @param var The variable
	 * @param value The expression to bind the variable to
	 * @param bindsCollection Specifies, whether the expression returns a collection
	 */
    protected void bindVariable(Variable var, OclExpression value, boolean bindsCollection) {
        if (isBound(var)) {
            ContextVariableData varData = freeRelationVariables.get(var);
            OclExpression oldBinding = varData.value;
            if (bindsCollection) {
                varData.isCollection = bindsCollection;
                ImperativeIterateExp xselect = getXSelectExpression(var, oldBinding);
                addFurtherCondition(xselect, resolveRelationsOclExpression(value, false));
                replaceCollectionVar(xselect, var, (Variable) xselect.getIterator().get(0));
                varData.value = xselect;
                System.out.println("Added xselect condition to variable '" + var.getName() + "'");
            } else {
                if ((varData.isCollection) && (oldBinding instanceof ResolveExp) && (varData.getRelationalCollectionConstraints().size() == 1)) {
                    Variable counterpartVar = varData.relCallVarCounterpart;
                    ContextVariableData counterpartVarData = freeRelationVariables.get(counterpartVar);
                    if ((counterpartVarData.value instanceof ResolveExp) && (counterpartVarData.getRelationalCollectionConstraints().size() == 1)) {
                        List<OclExpression> relationalConditions = varData.getRelationalCollectionConstraints();
                        varData.isCollection = false;
                        varData.value = value;
                        relationalConditions.remove(0);
                        addNotNullVariable(counterpartVarData.furtherNotNullVariable);
                        ResolveExp counterpartResolve = (ResolveExp) counterpartVarData.value;
                        counterpartResolve.setSource(copyExpression(value));
                        counterpartResolve.setOne(Boolean.TRUE);
                        counterpartVarData.getRelationalCollectionConstraints().remove(0);
                        counterpartVarData.isCollection = false;
                    }
                }
                System.out.println("Overwriting variable '" + var.getName() + "'");
            }
        } else {
            freeRelationVariables.put(var, new ContextVariableData(bindsCollection, value));
            if (bindsCollection) {
                System.out.println("Bound collection variable '" + var.getName() + "'");
            } else {
                System.out.println("Bound variable '" + var.getName() + "'");
            }
        }
    }

    /**
	 * Overwrites the binding of a variable.
	 * @param var The variable
	 * @param newValue The value expression
	 * @param isCollection Specifies, whether the variable binds a collection
	 */
    protected void overwriteVariable(Variable var, OclExpression newValue, boolean isCollection) {
        freeRelationVariables.put(var, new ContextVariableData(isCollection, newValue));
    }

    /**
	 * Adds a variable to a list of all variables which have to be unequal to zero
	 * because of a template constraint.
	 * @param var The variable to add
	 */
    protected void addNotNullVariable(Variable var) {
        if (var == null) return;
        if (!notNullVariables.contains(var)) {
            notNullVariables.add(var);
            Variable furtherNotNullVariable = freeRelationVariables.get(var).furtherNotNullVariable;
            if ((furtherNotNullVariable != null) && (!notNullVariables.contains(furtherNotNullVariable))) notNullVariables.add(furtherNotNullVariable);
        }
    }

    /**
	 * Replaces a variable bound to a collection by another variable from an expression.
	 * @param exp The expression
	 * @param replaceVar The variable to replace
	 * @param destVar The new variable
	 */
    protected void replaceCollectionVar(OclExpression exp, Variable replaceVar, Variable destVar) {
        java.lang.Object object;
        for (TreeIterator it = exp.eAllContents(); it.hasNext(); ) {
            object = it.next();
            if ((isCollectionVariableExp(object)) && (((VariableExp) object).getReferredVariable() == replaceVar)) {
                ((VariableExp) object).setReferredVariable(destVar);
            }
        }
    }

    /**
	 * Adds a condition to an ImperativeIterateExp.
	 * @param iterateExp The iteration expression
	 * @param furtherCondition The condition to add
	 */
    protected void addFurtherCondition(ImperativeIterateExp iterateExp, OclExpression furtherCondition) {
        OclExpression oldCondition = iterateExp.getCondition();
        if (oldCondition == null) iterateExp.setCondition(furtherCondition); else iterateExp.setCondition(qvtFactory.createBooleanOperation("and", oldCondition, furtherCondition));
    }

    /**
	 * Adds a condition to an IterateExp.
	 * @param iterateExp The iteration expression
	 * @param furtherCondition The condition to add
	 */
    private void addFurtherCondition(IterateExp iterateExp, OclExpression furtherCondition) {
        OclExpression oldCondition = iterateExp.getBody();
        if (oldCondition == null) iterateExp.setBody(furtherCondition); else iterateExp.setBody(qvtFactory.createBooleanOperation("and", oldCondition, furtherCondition));
    }

    /**
	 * Copies an OCL expression.
	 * @param src The source expression
	 * @return The copied expression
	 */
    protected OclExpression copyExpression(OclExpression src) {
        return (OclExpression) EcoreUtil.copy(src);
    }

    /**
	 * Copies and resolves an expression from the current context.
	 * @param exp The expression to copy and resolve
	 * @param resolveCollectionVars Specifies, whether collection variables should be resolved
	 * @return The resolved expression
	 */
    protected OclExpression resolveRelationsOclExpression(OclExpression exp, boolean resolveCollectionVars) {
        return resolveOclExpressionFromContext(copyExpression(exp), resolveCollectionVars);
    }

    /**
	 * Resolves all variables from context contained in an OCL expression.
	 * @param exp The OCL expression
	 * @param resolveCollectionVars Specifies, whether collection variables should be resolved
	 * @return The resolved OCL expression
	 */
    protected OclExpression resolveOclExpressionFromContext(OclExpression exp, boolean resolveCollectionVars) {
        if (exp == null) return null;
        Object object;
        OclExpression result = processOclExpressionFromContext(exp, resolveCollectionVars);
        for (Iterator it = result.eContents().iterator(); it.hasNext(); ) {
            object = it.next();
            if (object instanceof OclExpression) {
                resolveOclExpressionFromContext((OclExpression) object, resolveCollectionVars);
            }
        }
        return result;
    }

    /**
	 * Processes all variables from context contained in an OCL expression
	 * and operation calls referring helper operations.
	 * @param exp The OCL expression
	 * @param resolveCollectionVars Specifies, whether collection variables should be resolved
	 * @return The resolved OCL expression
	 */
    private OclExpression processOclExpressionFromContext(OclExpression exp, boolean resolveCollectionVars) {
        if (exp instanceof VariableExp) {
            VariableExp v = (VariableExp) exp;
            if (resolveCollectionVars || !isCollectionVariableExp(v)) {
                if (v.getReferredVariable().eContainer() instanceof Relation) {
                    System.out.println("   Replacing variable '" + v.getReferredVariable().getName() + "'");
                    OclExpression res;
                    if (resolveCollectionVars) res = getVariableAssignment(v.getReferredVariable(), resolveCollectionVars); else res = resolveOclExpressionFromContext(copyExpression(freeRelationVariables.get(v.getReferredVariable()).value), resolveCollectionVars);
                    EcoreUtil.replace(v, res);
                    return res;
                }
            }
        } else if (exp instanceof OperationCallExp) {
            OperationCallExp oc = (OperationCallExp) exp;
            if (oc.getReferredOperation().getClass_() == currentRelTransformation) oc.setReferredOperation(findHelperOperation(oc.getReferredOperation().getName()));
        }
        return exp;
    }

    /**
	 * Finds a helper operation in the current operational transformation
	 * @param operationName the helper operation name
	 * @return the helper operation
	 */
    private Helper findHelperOperation(String operationName) {
        Operation result;
        for (Iterator it = currentOpTransformation.getOwnedOperation().iterator(); it.hasNext(); ) {
            result = (Operation) it.next();
            if (result.getName().equals(operationName)) return (Helper) result;
        }
        return null;
    }

    /**
	 * Maps the root variable of a domain to a mapping parameter.
	 * @param domain The relation domain
	 * @param mp The mapping parameter corresponding to the domain
	 */
    protected void addMappingParameter(RelationDomain domain, MappingParameter mp) {
        if (!mappingParameters.containsKey(domain.getRootVariable().getName())) mappingParameters.put(domain.getRootVariable().getName(), mp);
    }

    /**
	 * Returns the corresponding mapping parameter.
	 * @param domain The relation domain to get the corresponding relation domain from
	 * @return The mapping parameter
	 */
    protected MappingParameter getMappingParameter(RelationDomain domain) {
        return (MappingParameter) mappingParameters.get(domain.getRootVariable().getName());
    }

    /**
	 * Returns the corresponding mapping parameter.
	 * @param var The root variable of a relation domain
	 * @return The mapping parameter
	 */
    protected MappingParameter getMappingParameter(Variable var) {
        return (MappingParameter) mappingParameters.get(var.getName());
    }

    /**
	 * Returns a unique name for a variable.
	 * @param name The suggested name
	 * @return A unique variable name
	 */
    protected String getUniqueName(String name) {
        if (reservedNames == null) {
            reservedNames = new ArrayList<String>();
            reservedNames.add("this");
            for (Iterator<ModelParameter> it = currentOpTransformation.getModelParameter().iterator(); it.hasNext(); ) reservedNames.add(it.next().getName());
            for (Iterator<MappingParameter> it = mapping.getOwnedParameter().iterator(); it.hasNext(); ) reservedNames.add(it.next().getName());
            for (Iterator<MappingParameter> it = mapping.getResult().iterator(); it.hasNext(); ) reservedNames.add(it.next().getName());
        }
        if (reservedNames.contains(name)) {
            if ((name.charAt(name.length() - 1) >= '0') && (name.charAt(name.length() - 1) <= '9')) return getUniqueName(name.substring(0, name.length() - 1) + Integer.toString((Integer.parseInt(name.substring(name.length() - 1)) + 1))); else return getUniqueName(name + "2");
        } else reservedNames.add(name);
        return name;
    }

    /**
	 * Specifies, whether a variable is bound in the current context.
	 * @param var The variable
	 * @return <code>true</code> if the variable is bound, <code>false</code> otherwise
	 */
    protected boolean isBound(Variable var) {
        return freeRelationVariables.containsKey(var);
    }

    /**
	 * Resolves an expression containing variables bound to a collection by
	 * creating a <code>forAll</code> or <code>xcollect</code> expression
	 * and replacing the variables by iterators.
	 * @param exp The expression
	 * @param isForAll if <code>true</code>, a <code>forAll</code> expression is returned,
	 * otherwise a <code>xcollect</code> expression
	 * @param loopExp An existing loop expression, <code>null</code> otherwise
	 * @param objectExp The object expression belonging to an <code>xcollect</code> resolvement,
	 * <code>null</code> otherwise
	 * @param colVarDestinations A mapping collection-bound-variable<->iterator-variable 
	 * @return The resolved expression
	 */
    protected ImperativeLoopExp resolveCollectionExpression(OclExpression exp, boolean isForAll, ImperativeLoopExp loopExp, ObjectExp objectExp, Hashtable<Variable, Variable> colVarDestinations) {
        java.lang.Object object = exp;
        TreeIterator it = exp.eAllContents();
        do {
            if (isCollectionVariableExp(object)) {
                VariableExp colVarExp = (VariableExp) object;
                Variable colVar = colVarExp.getReferredVariable();
                if (colVarDestinations.containsKey(colVar)) {
                    colVarExp.setReferredVariable((Variable) colVarDestinations.get(colVar));
                } else if (isTopCollectionVariable(colVarExp)) {
                    if (!isForAll) {
                        ImperativeIterateExp oldXCollect = (ImperativeIterateExp) loopExp;
                        ImperativeIterateExp xcollect = qvtFactory.createImperativeIterateExp("xcollect");
                        if (oldXCollect != null) xcollect.setBody(oldXCollect); else if (objectExp != null) xcollect.setBody(objectExp); else xcollect.setBody(exp);
                        xcollect.setSource(getVariableAssignment(colVar, true));
                        xcollect.getIterator().add(qvtFactory.createVariable(getUniqueName(colVar.getName().substring(0, 1))));
                        colVarDestinations.put(colVar, (Variable) xcollect.getIterator().get(0));
                        loopExp = xcollect;
                    } else {
                        ForExp forall = (ForExp) loopExp;
                        if (forall == null) forall = addForEachExpression(exp, colVar.getName()); else forall = addForEachExpression(forall, colVar.getName());
                        forall.setSource(getVariableAssignment(colVar, true));
                        colVarDestinations.put(colVar, (Variable) forall.getIterator().get(0));
                        loopExp = forall;
                    }
                    replaceCollectionVars(exp, colVarDestinations);
                    return resolveCollectionExpression(exp, isForAll, loopExp, objectExp, colVarDestinations);
                }
            }
            if (it.hasNext()) object = it.next(); else object = null;
        } while (object != null);
        return loopExp;
    }

    /**
	 * Resolves an expression by replacing all occuring collection-bound variables
	 * by the corresponding iterator variables.
	 * @param exp The expression to resolve
	 * @param colVarDestinations The mapping collection-bound-variable<->iterator-variable 
	 * @return The resolved expression
	 */
    private OclExpression replaceCollectionVars(OclExpression exp, Hashtable colVarDestinations) {
        java.lang.Object object;
        for (TreeIterator it = exp.eAllContents(); it.hasNext(); ) {
            object = it.next();
            if ((isCollectionVariableExp(object)) && (colVarDestinations.containsKey(((VariableExp) object).getReferredVariable()))) {
                ((VariableExp) object).setReferredVariable((Variable) colVarDestinations.get(((VariableExp) object).getReferredVariable()));
            }
        }
        return exp;
    }

    /**
	 * Specifies, whether a collection-bound variable referred by an expression is the root
	 * of all other occuring collection variables.
	 * @param collectionVar The variable expression
	 * @return <code>true</code>, if the variable is the first occuring in the model tree,
	 * <code>false</code> otherwise
	 */
    private boolean isTopCollectionVariable(VariableExp collectionVar) {
        EObject container = collectionVar.eContainer();
        while (container != null) {
            if (isCollectionVariableExp(container)) return false;
            container = container.eContainer();
        }
        return true;
    }

    /**
	 * Specifies, whether an expression contains collection-bound variables.
	 * @param exp The expression
	 * @return <code>true</code>, if it contains collection variables,
	 * <code>false</code> otherwise
	 */
    protected boolean hasCollectionVariables(OclExpression exp) {
        return getCollectionVariables(exp).size() > 0;
    }

    /**
	 * Returns a list of all collection-bound variables occuring in an expression. 
	 * @param exp The expression
	 * @return The list of collection variables
	 */
    protected List<Variable> getCollectionVariables(OclExpression exp) {
        Variable var;
        Object nestedExp;
        List<Variable> colVars = new ArrayList<Variable>();
        if (isCollectionVariableExp(exp)) {
            var = ((VariableExp) exp).getReferredVariable();
            if (!colVars.contains(var)) colVars.add(var);
        }
        for (TreeIterator it = exp.eAllContents(); it.hasNext(); ) {
            nestedExp = it.next();
            if (isCollectionVariableExp(nestedExp)) {
                var = ((VariableExp) nestedExp).getReferredVariable();
                if (!colVars.contains(var)) colVars.add(var);
            }
        }
        return colVars;
    }

    /**
	 * Returns the boolean value.
	 * @param b The Boolean instance
	 * @return <code>true</code>, if the instance is not null and its
	 * boolean value is true, <code>false</code> otherwise
	 */
    protected boolean isTrue(Boolean b) {
        if (b != null && b.booleanValue()) return true; else return false;
    }

    /**
	 * Adds a <code>forEach</code> expression.
	 * @param bodyExp The body expression
	 * @param varName The suggested name for the created iterator variable
	 * @return The for expression
	 */
    protected ForExp addForEachExpression(OclExpression bodyExp, String varName) {
        ForExp res = qvtFactory.createForExp("forEach");
        res.getIterator().add(qvtFactory.createVariable(getUniqueName(varName.substring(0, 1))));
        res.setBody(qvtFactory.createBlockExp());
        if (bodyExp != null) ((BlockExp) res.getBody()).getBody().add(bodyExp);
        return res;
    }

    /**
	 * Sets a relation between context and result variable of a relation call expression.
	 * @param targetVar The variable to add the relation
	 * @param counterpartVar The counterpart variable of the relation call
	 * @param resolveExp The corresponding resolve expression
	 * @param furtherNotNullVariable The result variable, if <code>targetVar</code> is the context
	 * variable of the relation call, <code>null</code> otherwise
	 */
    protected void setRelationCallRelation(Variable targetVar, Variable counterpartVar, ResolveExp resolveExp, Variable furtherNotNullVariable) {
        ContextVariableData varData = freeRelationVariables.get(targetVar);
        varData.relCallVarCounterpart = counterpartVar;
        varData.furtherNotNullVariable = furtherNotNullVariable;
    }

    /**
	 * This class stores context information of a variable.
	 * 
	 * @author Pascal Muellender (p_muellender@users.sourceforge.net)
	 */
    private class ContextVariableData {

        /**
		 * Specifies, whether the variable is bound to a collection of objects or values
		 */
        private boolean isCollection;

        /**
		 * The current binding of the variable
		 */
        private OclExpression value;

        /**
		 * The relations to other collection-bound variables
		 */
        private List<OclExpression> collectionRelations;

        /**
		 * The counterpart variable, if the variable is bound by a relation call
		 */
        private Variable relCallVarCounterpart;

        /**
		 * The result variable of the relation call, if this variable is context
		 * variable
		 */
        private Variable furtherNotNullVariable;

        /**
		 * Constructor.
		 * @param isCollection Specifies, whether the variable is bound to a collection of objects or values
		 * @param value The binding of the variable
		 */
        public ContextVariableData(boolean isCollection, OclExpression value) {
            this.isCollection = isCollection;
            this.value = value;
            collectionRelations = null;
            relCallVarCounterpart = null;
            furtherNotNullVariable = null;
        }

        /**
		 * Returns a list of relations to other collection-bound variables.
		 * @return The list
		 */
        public List<OclExpression> getRelationalCollectionConstraints() {
            if (collectionRelations == null) collectionRelations = new ArrayList<OclExpression>();
            return collectionRelations;
        }
    }
}
