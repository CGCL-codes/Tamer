package net.sourceforge.olympos.oaw.extend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.TypedElement;

public class Prr {

    public static Parameter getParameter(TypedElement ruleVariable) {
        Parameter result = null;
        if (ruleVariable instanceof ActivityParameterNode) {
            result = ((ActivityParameterNode) ruleVariable).getParameter();
        }
        return result;
    }

    public static Collection<ActivityNode> getNode(NamedElement productionRule) {
        Collection<ActivityNode> result = new BasicEList<ActivityNode>();
        if (productionRule instanceof StructuredActivityNode) {
            result = ((StructuredActivityNode) productionRule).getNodes();
        }
        return result;
    }

    public static StructuralFeature structuralFeature(Action action) {
        StructuralFeature result = null;
        if (action instanceof AddStructuralFeatureValueAction) {
            result = ((AddStructuralFeatureValueAction) action).getStructuralFeature();
        }
        return result;
    }

    public static InputPin value(Action action) {
        InputPin result = null;
        if (action instanceof AddStructuralFeatureValueAction) {
            result = ((AddStructuralFeatureValueAction) action).getValue();
        }
        return result;
    }

    public static Collection<ActivityEdge> incoming(Action action) {
        Collection<ActivityEdge> result = new BasicEList<ActivityEdge>();
        if (action instanceof Action) {
            result = ((Action) action).getIncomings();
        }
        return result;
    }

    public static InputPin object(Action action) {
        InputPin result = null;
        if (action instanceof AddStructuralFeatureValueAction) {
            result = ((AddStructuralFeatureValueAction) action).getObject();
        }
        return result;
    }

    public static Object getAttribute(Element element, String className, String attributeName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Object result = null;
        Class<?>[] params = {};
        Object[] paramsObj = {};
        Class<?> clazz = Class.forName(className);
        if (clazz.isInstance(element)) {
            String methodName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method method = clazz.getMethod(methodName, params);
            if (method != null) {
                result = method.invoke(element, paramsObj);
            }
        }
        return result;
    }
}
