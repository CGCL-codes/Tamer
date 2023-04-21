package ognl;

import java.util.Map;

/**
 * This interface defines methods for callinig methods in a target object.
 * Methods are broken up into static and instance methods for convenience.
 * indexes into the target object, which must be an array.
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 */
public interface MethodAccessor {

    /**
       * Calls the static method named with the arguments given on the class given.
       * @param context     expression context in which the method should be called
       * @param targetClass the object in which the method exists
       * @param methodName  the name of the method
       * @param args        the arguments to the method
       * @result            result of calling the method
       * @exception MethodFailedException if there is an error calling the method
       */
    Object callStaticMethod(Map context, Class targetClass, String methodName, Object[] args) throws MethodFailedException;

    /**
       * Calls the method named with the arguments given.
       * @param context     expression context in which the method should be called
       * @param target      the object in which the method exists
       * @param methodName  the name of the method
       * @param args        the arguments to the method
       * @result            result of calling the method
       * @exception MethodFailedException if there is an error calling the method
       */
    Object callMethod(Map context, Object target, String methodName, Object[] args) throws MethodFailedException;
}
