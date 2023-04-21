package equilibrium.commons.report.databuilder;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.odysseus.el.util.SimpleContext;
import equilibrium.commons.LayeredStack;
import equilibrium.commons.report.ResourceBundleAdapter;

public class JUELContextManager {

    private static final Log log = LogFactory.getLog(JUELContextManager.class);

    private ExpressionFactory expressionFactory;

    private SimpleContext context;

    private Locale locale;

    private boolean parserToLowerCase;

    private LayeredStack variableStack = new LayeredStack();

    public JUELContextManager(ExpressionFactory factory, ResourceBundleAdapter bundle, Locale locale, boolean parserToLowerCase) {
        expressionFactory = factory;
        this.locale = locale;
        this.parserToLowerCase = parserToLowerCase;
        Method method = null;
        ELResolver resolver = new MethodResolver();
        try {
            method = ResourceBundleAdapter.class.getMethod("getText", String.class);
            ((MethodResolver) resolver).addMethod(method);
            method = String.class.getMethod("endsWith", String.class);
            ((MethodResolver) resolver).addMethod(method);
            method = String.class.getMethod("trim");
            ((MethodResolver) resolver).addMethod(method);
        } catch (SecurityException e) {
            log.fatal("SecurityException thrown on ContextBuilder.builContext() " + "invocation. This is fatal and should NOT happen.");
        } catch (NoSuchMethodException e) {
            log.fatal("NoSuchMethodException thrown on ContextBuilder.builContext() " + "invocation. This is fatal and should NOT happen.");
        }
        log.debug("Creating JUEL context for parser");
        context = new SimpleContext(resolver);
        log.debug("Adding bundle object to JUEL context");
        ValueExpression value = expressionFactory.createValueExpression(bundle, ResourceBundleAdapter.class);
        context.setVariable("bundle", value);
    }

    public ELContext getContext() {
        return context;
    }

    public void pushAllToContext(List<Map<String, Object>> contextMaps) {
        for (Map<String, Object> contextMap : contextMaps) {
            pushToContext(contextMap);
        }
    }

    public void pushToContext(Map<String, Object> map) {
        variableStack.push(map);
        for (Entry<String, Object> variable : map.entrySet()) {
            addVariableToJUELContext(variable);
        }
    }

    public Map<String, Object> popFromContext() {
        Map<String, Object> stackLayer = variableStack.pop();
        Map<String, Object> allVariables = variableStack.retrieveAllVariables();
        for (Entry<String, Object> removedVariable : stackLayer.entrySet()) {
            String key = removedVariable.getKey();
            if (allVariables.containsKey(key)) {
                Object previousVariableValue = allVariables.get(key);
                ValueExpression valueExpression = expressionFactory.createValueExpression(previousVariableValue, Object.class);
                context.setVariable(key, valueExpression);
                if (parserToLowerCase) {
                    String lowerCaseKey = key.toLowerCase(locale);
                    context.setVariable(lowerCaseKey, valueExpression);
                }
            } else {
                context.setVariable(key, null);
            }
        }
        return stackLayer;
    }

    private void addVariableToJUELContext(Entry<String, Object> newVariable) {
        ValueExpression valueExpression = expressionFactory.createValueExpression(newVariable.getValue(), Object.class);
        String key = newVariable.getKey();
        context.setVariable(key, valueExpression);
        if (parserToLowerCase) {
            String lowerCaseKey = key.toLowerCase(locale);
            context.setVariable(lowerCaseKey, valueExpression);
        }
    }

    public Map<String, Object> peekFromContext() {
        return variableStack.peek();
    }

    public boolean hasRowsInParserContext() {
        return !variableStack.isEmpty();
    }

    class MethodResolver extends ELResolver {

        private List<Method> methodList = new ArrayList<Method>();

        @Override
        public Method getValue(ELContext context, Object base, Object prop) {
            for (Method method : methodList) {
                if (method.getDeclaringClass().isInstance(base) && method.getName().equals(prop.toString())) {
                    context.setPropertyResolved(true);
                    return method;
                }
            }
            return null;
        }

        public void addMethod(Method method) {
            methodList.add(method);
        }

        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            throw new PropertyNotWritableException();
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            return Object.class;
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            return true;
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) {
            return null;
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return null;
        }
    }
}
