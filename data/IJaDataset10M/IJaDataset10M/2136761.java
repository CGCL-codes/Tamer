package tefkat.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xml.type.AnyType;
import tefkat.model.TRule;
import tefkat.model.Term;
import tefkat.model.Var;
import tefkat.model.internal.ModelUtils;

final class Context {

    private final RuleEvaluator ruleEval;

    private final Evaluator exprEval;

    final Tree tree;

    final Node node;

    Context(RuleEvaluator ruleEval, Evaluator exprEval, Tree tree, Node node) {
        this.ruleEval = ruleEval;
        this.exprEval = exprEval;
        this.tree = tree;
        this.node = node;
    }

    void createBranch() {
        List newGoal = newGoal();
        tree.createBranch(node, null, newGoal);
    }

    void createBranch(Term term) {
        createBranch(term, null);
    }

    void createBranch(Binding unifier) {
        List newGoal = newGoal();
        tree.createBranch(node, unifier, newGoal);
    }

    void createBranch(Term term, Binding unifier) {
        List newGoal = newGoal();
        newGoal.add(term);
        tree.createBranch(node, unifier, newGoal);
    }

    void createBranch(Collection terms) {
        List newGoal = newGoal();
        newGoal.addAll(terms);
        tree.createBranch(node, null, newGoal);
    }

    void delay(String message) throws NotGroundException {
        throw new NotGroundException(node, message);
    }

    void error(String message) throws ResolutionException {
        throw new ResolutionException(node, message);
    }

    void error(String message, Exception e) throws ResolutionException {
        throw new ResolutionException(node, message, e);
    }

    private List newGoal() {
        List newGoal = new ArrayList(node.goal());
        newGoal.remove(node.selectedLiteral());
        return newGoal;
    }

    void fail() {
        node.setIsFailure(true);
    }

    /**
     * Find a binding for the variable in the current context.
     * 
     * @param var   The var to lookup in this context
     * @return      The value that var is bound to in the context of this node or null
     */
    Object lookup(Var var) {
        return node.lookup(var);
    }

    Tree createTree(Collection goal, Binding unifier, boolean isNegation, boolean subTree) {
        return createTree(new Node(goal, unifier), isNegation, subTree);
    }

    Tree createTree(Node newRoot, boolean isNegation, boolean subTree) {
        Tree result = new Tree(this, newRoot, tree.getContext(), tree.getTrackingExtent(), isNegation);
        if (subTree) {
            result.setLevel(tree.getLevel() - 1);
        } else {
            result.setLevel(tree.getLevel());
        }
        ruleEval.addUnresolvedTree(result);
        return result;
    }

    Binding getBindings() {
        return node.getBindings();
    }

    List expand(WrappedVar var) throws NotGroundException {
        return exprEval.expand(this, var);
    }

    Function getFunction(String name) {
        return (Function) exprEval.funcMap.get(name);
    }

    EObject lookup(List keys, TRule rule) {
        return ruleEval.injections.lookup(tree.getTrackingExtent(), keys, rule);
    }

    void warn(String string) {
        ruleEval.fireWarning(string);
    }

    Map getNameMap() {
        return ruleEval.nameMap;
    }

    Object fetchFeature(String featureName, Object obj) throws ResolutionException {
        Object valuesObject = null;
        if (obj instanceof DynamicObject) {
            throw new ResolutionException(node, "Illegal attempt to retrieve feature value from target object instance: " + obj);
        }
        if (obj instanceof EObject) {
            EObject instance = (EObject) obj;
            try {
                EStructuralFeature eFeature = AbstractResolver.getFeature(this, instance.eClass(), featureName);
                valuesObject = instance.eGet(eFeature);
                if (valuesObject != null || instance.eIsSet(eFeature) || !eFeature.isRequired()) {
                    ExtentUtil.highlightEdge(instance, valuesObject, ExtentUtil.FEATURE_LOOKUP);
                } else {
                    warn(ModelUtils.getFullyQualifiedName(eFeature) + " is not set and no default value");
                }
                return valuesObject;
            } catch (ResolutionException e) {
            }
        }
        if (obj instanceof FeatureMap.Entry) {
            FeatureMap.Entry entry = (FeatureMap.Entry) obj;
            EStructuralFeature eFeature = entry.getEStructuralFeature();
            if (eFeature.getName().equals(featureName)) {
                valuesObject = entry.getValue();
                return valuesObject;
            }
        }
        String methName = "get" + featureName.substring(0, 1).toUpperCase() + featureName.substring(1, featureName.length());
        try {
            try {
                valuesObject = obj.getClass().getMethod(methName, null).invoke(obj, null);
            } catch (NoSuchMethodException e) {
                if (null == valuesObject) {
                    if (obj instanceof AnyType) {
                        AnyType anyObj = (AnyType) obj;
                        FeatureMap fm = anyObj.getAnyAttribute();
                        for (FeatureMap.Entry entry : fm) {
                            EStructuralFeature val = entry.getEStructuralFeature();
                            if (val.getName().equals(featureName)) {
                                valuesObject = anyObj.getAnyAttribute().get(val, true);
                            }
                            XMIResource res = (XMIResource) anyObj.eResource();
                            valuesObject = res.getEObject((String) valuesObject);
                        }
                    } else {
                        valuesObject = obj.getClass().getField(featureName).get(obj);
                    }
                }
            }
        } catch (Exception e) {
            warn("Could not find a source of values for '" + featureName + "' in '" + obj + "' " + e.getMessage());
        }
        return valuesObject;
    }

    void addPartialOrder(Object inst, Object feat, Object lesser, Object greater) {
        ruleEval.addPartialOrder(inst, feat, lesser, greater);
    }

    void fireInfo(String mesg) {
        ruleEval.fireInfo(mesg);
    }

    Tree getResultTree(final Term term, final Binding unifier) {
        final Map cache = ruleEval.getPatternCache(term);
        final Binding parameterContext;
        if (null == unifier) {
            parameterContext = tree.getContext();
        } else {
            parameterContext = unifier;
            parameterContext.composeRight(tree.getContext());
        }
        Tree resultTree = (Tree) cache.get(parameterContext);
        if (null == resultTree) {
            final Collection goal = new ArrayList();
            goal.add(term);
            Node patternNode = new Node(goal, parameterContext);
            resultTree = createTree(patternNode, false, false);
            cache.put(parameterContext, resultTree);
        }
        if (!resultTree.isCompleted()) {
            resultTree.addTreeListener(new TreeListener() {

                public void solution(Binding answer) {
                }

                public void completed(Tree theTree) {
                    theTree.removeTreeListener(this);
                }

                public void floundered(Tree theTree) {
                    cache.remove(parameterContext);
                }
            });
        }
        return resultTree;
    }
}
