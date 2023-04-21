package org.parallelj.designer.expressions;

import java.util.Collections;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.EcoreFactory;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.ecore.OCL.Helper;
import org.eclipse.ocl.options.ParsingOptions;
import org.parallelj.designer.part.ParallelJDiagramEditorPlugin;

/**
 * @generated
 */
public class ParallelJOCLFactory {

    /**
	 * @generated
	 */
    private final ParallelJAbstractExpression[] expressions;

    /**
	 * @generated
	 */
    protected ParallelJOCLFactory() {
        this.expressions = new ParallelJAbstractExpression[3];
    }

    /**
	 * @generated
	 */
    public static ParallelJAbstractExpression getExpression(int index, EClassifier context, Map<String, EClassifier> environment) {
        ParallelJOCLFactory cached = ParallelJDiagramEditorPlugin.getInstance().getParallelJOCLFactory();
        if (cached == null) {
            ParallelJDiagramEditorPlugin.getInstance().setParallelJOCLFactory(cached = new ParallelJOCLFactory());
        }
        if (index < 0 || index >= cached.expressions.length) {
            throw new IllegalArgumentException();
        }
        if (cached.expressions[index] == null) {
            final String[] exprBodies = new String[] { "not self.oclIsTypeOf(ForEachLoop) and not self.oclIsTypeOf(WhileLoop) and not self.oclIsTypeOf(Handler) and not self.oclIsTypeOf(Pipeline)", "not oclIsKindOf(OutputCondition) and not eContainer().oclIsKindOf(Pipeline) and not oclIsKindOf(Data) and not oclIsKindOf(Predicate)", "not oclIsKindOf(InputCondition) and not oclIsKindOf(Handler) and if oclIsKindOf(Condition) then not oppositeEnd.oclIsKindOf(Condition) else true endif and oppositeEnd.outputLinks -> select (p | p.destination = self ) -> size() <> 1 and not eContainer().oclIsKindOf(Pipeline) and not oclIsKindOf(Data) and not oclIsKindOf(Predicate)" };
            cached.expressions[index] = getExpression(exprBodies[index], context, environment == null ? Collections.<String, EClassifier>emptyMap() : environment);
        }
        return cached.expressions[index];
    }

    /**
	 * This is factory method, callers are responsible to keep reference to the return value if they want to reuse parsed expression
	 * @generated
	 */
    public static ParallelJAbstractExpression getExpression(String body, EClassifier context, Map<String, EClassifier> environment) {
        return new Expression(body, context, environment);
    }

    /**
	 * This method will become private in the next release
	 * @generated
	 */
    public static ParallelJAbstractExpression getExpression(String body, EClassifier context) {
        return getExpression(body, context, Collections.<String, EClassifier>emptyMap());
    }

    /**
	 * @generated
	 */
    private static class Expression extends ParallelJAbstractExpression {

        /**
		 * @generated
		 */
        private final org.eclipse.ocl.ecore.OCL oclInstance;

        /**
		 * @generated
		 */
        private OCLExpression oclExpression;

        /**
		 * @generated
		 */
        public Expression(String body, EClassifier context, Map<String, EClassifier> environment) {
            super(body, context);
            oclInstance = org.eclipse.ocl.ecore.OCL.newInstance();
            initCustomEnv(oclInstance.getEnvironment(), environment);
            Helper oclHelper = oclInstance.createOCLHelper();
            oclHelper.setContext(context());
            try {
                oclExpression = oclHelper.createQuery(body());
                setStatus(IStatus.OK, null, null);
            } catch (ParserException e) {
                setStatus(IStatus.ERROR, e.getMessage(), e);
            }
        }

        /**
		 * @generated
		 */
        @SuppressWarnings("rawtypes")
        protected Object doEvaluate(Object context, Map env) {
            if (oclExpression == null) {
                return null;
            }
            EvaluationEnvironment<?, ?, ?, ?, ?> evalEnv = oclInstance.getEvaluationEnvironment();
            for (Object nextKey : env.keySet()) {
                evalEnv.replace((String) nextKey, env.get(nextKey));
            }
            try {
                Object result = oclInstance.evaluate(context, oclExpression);
                return oclInstance.isInvalid(result) ? null : result;
            } finally {
                evalEnv.clear();
                oclInstance.setExtentMap(null);
            }
        }

        /**
		 * @generated
		 */
        private static void initCustomEnv(Environment<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, ?, ?, ?> ecoreEnv, Map<String, EClassifier> environment) {
            ParsingOptions.setOption(ecoreEnv, ParsingOptions.implicitRootClass(ecoreEnv), EcorePackage.eINSTANCE.getEObject());
            for (String varName : environment.keySet()) {
                EClassifier varType = environment.get(varName);
                ecoreEnv.addElement(varName, createVar(ecoreEnv, varName, varType), false);
            }
        }

        /**
		 * @generated
		 */
        private static Variable createVar(Environment<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> ecoreEnv, String name, EClassifier type) {
            Variable var = EcoreFactory.eINSTANCE.createVariable();
            var.setName(name);
            var.setType(ecoreEnv.getUMLReflection().getOCLType(type));
            return var;
        }
    }
}
