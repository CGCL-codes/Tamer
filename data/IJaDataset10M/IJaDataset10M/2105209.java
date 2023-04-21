package com.google.gdt.eclipse.designer.gxt.model;

import org.eclipse.wb.core.eval.AstEvaluationEngine;
import org.eclipse.wb.core.eval.EvaluationContext;
import org.eclipse.wb.core.eval.InvocationEvaluatorInterceptor;
import org.eclipse.wb.internal.core.model.JavaInfoUtils;
import org.eclipse.wb.internal.core.model.util.ScriptUtils;
import org.eclipse.wb.internal.core.utils.ast.AstNodeUtils;
import org.eclipse.wb.internal.core.utils.jdt.core.CodeUtils;
import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;
import org.eclipse.wb.internal.core.utils.state.EditorState;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * {@link InvocationEvaluatorInterceptor} for GXT.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.model
 */
public final class GxtInvocationEvaluatorInterceptor extends InvocationEvaluatorInterceptor {

    @Override
    public Object evaluate(EvaluationContext context, ClassInstanceCreation expression, ITypeBinding typeBinding, Class<?> clazz, Constructor<?> actualConstructor, Object[] arguments) throws Exception {
        useNotNullArgument(expression, arguments);
        return AstEvaluationEngine.UNKNOWN;
    }

    private static void useNotNullArgument(ClassInstanceCreation expression, Object[] arguments) throws Exception {
        IMethodBinding binding = AstNodeUtils.getCreationBinding(expression);
        ITypeBinding[] parameterTypes = binding.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ITypeBinding parameterType = parameterTypes[i];
            if (arguments[i] == null) {
                ClassLoader classLoader = JavaInfoUtils.getClassLoader(EditorState.getActiveJavaInfo());
                if (AstNodeUtils.isSuccessorOf(parameterType, "com.extjs.gxt.ui.client.widget.grid.ColumnModel")) {
                    arguments[i] = ScriptUtils.evaluate(classLoader, CodeUtils.getSource("new com.extjs.gxt.ui.client.widget.grid.ColumnModel(", "  new java.util.ArrayList()", ")"));
                }
                if (AstNodeUtils.isSuccessorOf(parameterType, "com.extjs.gxt.ui.client.store.ListStore")) {
                    arguments[i] = ScriptUtils.evaluate(classLoader, "new com.extjs.gxt.ui.client.store.GroupingStore()");
                }
                if (AstNodeUtils.isSuccessorOf(parameterType, "com.extjs.gxt.ui.client.store.TreeStore")) {
                    arguments[i] = ScriptUtils.evaluate(classLoader, "new com.extjs.gxt.ui.client.store.TreeStore()");
                }
            }
        }
    }

    @Override
    public Object evaluate(EvaluationContext context, MethodInvocation invocation, IMethodBinding methodBinding, Class<?> clazz, Method method, Object[] arguments) {
        if (ReflectionUtils.isSuccessorOf(clazz, "com.extjs.gxt.ui.client.widget.form.ComboBox")) {
            String signature = ReflectionUtils.getMethodSignature(method);
            if (signature.equals("setStore(com.extjs.gxt.ui.client.store.ListStore)") && arguments[0] == null) {
                arguments[0] = ScriptUtils.evaluate(clazz.getClassLoader(), "new com.extjs.gxt.ui.client.store.ListStore()");
            }
        }
        return AstEvaluationEngine.UNKNOWN;
    }
}
