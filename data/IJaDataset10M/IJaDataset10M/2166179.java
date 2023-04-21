package ognl;

import java.util.List;
import java.util.Map;

/**
 * Implementation of PropertyAccessor that uses reflection on the target object's class to find a
 * field or a pair of set/get methods with the given property name.
 * 
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public class ObjectMethodAccessor implements MethodAccessor {

    public Object callStaticMethod(Map context, Class targetClass, String methodName, Object[] args) throws MethodFailedException {
        List methods = OgnlRuntime.getMethods(targetClass, methodName, true);
        return OgnlRuntime.callAppropriateMethod((OgnlContext) context, targetClass, null, methodName, null, methods, args);
    }

    public Object callMethod(Map context, Object target, String methodName, Object[] args) throws MethodFailedException {
        Class targetClass = (target == null) ? null : target.getClass();
        List methods = OgnlRuntime.getMethods(targetClass, methodName, false);
        return OgnlRuntime.callAppropriateMethod((OgnlContext) context, target, target, methodName, null, methods, args);
    }
}
