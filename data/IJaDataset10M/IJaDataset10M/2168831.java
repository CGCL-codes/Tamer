package junit.extensions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * This class is used to access a method or field of an object no matter what the access modifier of the method or field. The syntax
 * for accessing fields and methods is out of the ordinary because this class uses reflection to peel away protection.
 * <p>
 * a.k.a. The "ObjectMolester"
 * <p>
 * Here is an example of using this to access a private member: <br>
 * Given the following class <code>MyClass</code>: <br>
 * 
 * <pre>
 * public class MyClass {
 *    private String name; // private attribute
 * 
 *    // private constructor
 *    private MyClass() {
 *       super();
 *    }
 * 
 *    // private method
 *    private void setName(String newName) {
 *       this.name = newName;
 *    }
 * }
 * </pre>
 * 
 * We now want to access the class: <br>
 * 
 * <pre>
 * MyClass myObj = StrictPA.instantiate(MyClass.class);
 * StrictPA.invokeMethod(myObj, &quot;setName(java.lang.String)&quot;, &quot;myNewName&quot;);
 * String name = PA.getValue(myObj, &quot;name&quot;);
 * </pre>
 * 
 * Internally PA uses the class PrivilegedAccessor which does not support varargs but is still available for backward compatibility.
 * PrivilegedAccessor is deprecated and will not be available in releases > 1.2.
 * 
 * @author Sebastian Dietrich (sebastian.dietrich@e-movimento.com)
 */
public final class StrictPA {

    /**
    * Private constructor to make it impossible to instantiate this class.
    */
    private StrictPA() {
        assert false : "You mustn't instantiate StrictPA, use its methods statically";
    }

    /**
    * Returns a string representation of the given object.
    * 
    * @param instanceOrClass the object or class to get a string representation of
    * @return a string representation of the given object
    */
    @SuppressWarnings("deprecation")
    public static String toString(final Object instanceOrClass) {
        return PrivilegedAccessor.toString(instanceOrClass);
    }

    /**
    * Gets the name of all fields (public, private, protected, default) of the given instance or class. This includes as well all
    * fields (public, private, protected, default) of all its super classes.
    * 
    * @param instanceOrClass the instance or class to get the fields of
    * @return the collection of field names of the given instance or class
    */
    @SuppressWarnings("deprecation")
    public static Collection<String> getFieldNames(final Object instanceOrClass) {
        return PrivilegedAccessor.getFieldNames(instanceOrClass);
    }

    /**
    * Gets the signatures of all methods (public, private, protected, default) of the given instance or class. This includes as well
    * all methods (public, private, protected, default) of all its super classes. This does not include constructors.
    * 
    * @param instanceOrClass the instance or class to get the method signatures of
    * @return the collection of method signatures of the given instance or class
    */
    @SuppressWarnings("deprecation")
    public static Collection<String> getMethodSignatures(final Object instanceOrClass) {
        return PrivilegedAccessor.getMethodSignatures(instanceOrClass);
    }

    /**
    * Gets the value of the named field and returns it as an object. If instanceOrClass is a class then a static field is returned.
    * 
    * @param instanceOrClass the instance or class to get the field from
    * @param fieldName the name of the field
    * @return an object representing the value of the field
    * @throws NoSuchFieldException if the field does not exist
    */
    @SuppressWarnings("deprecation")
    public static Object getValue(final Object instanceOrClass, final String fieldName) throws NoSuchFieldException {
        return PrivilegedAccessor.getValue(instanceOrClass, fieldName);
    }

    /**
    * Instantiates an object of the given class with the given arguments and the given argument types. If you want to instantiate a
    * member class, you must provide the object it is a member of as first argument.
    * 
    * @param fromClass the class to instantiate an object from
    * @param args the arguments to pass to the constructor
    * @param argumentTypes the types of the arguments of the constructor
    * @return an object of the given type
    * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an unwrapping conversion for primitive
    *            arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal
    *            parameter type by a method invocation conversion.
    * @throws IllegalAccessException if this Constructor object enforces Java language access control and the underlying constructor is
    *            inaccessible.
    * @throws InvocationTargetException if the underlying constructor throws an exception.
    * @throws NoSuchMethodException if the constructor could not be found
    * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
    * 
    * @see StrictPA#invokeMethod(Object,String,Object)
    */
    @SuppressWarnings("deprecation")
    public static <T> T instantiate(final Class<? extends T> fromClass, final Class<?>[] argumentTypes, final Object... args) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
    }

    /**
    * Instantiates an object of the given class with the given arguments. If you want to instantiate a member class, you must provide
    * the object it is a member of as first argument.
    * 
    * @param fromClass the class to instantiate an object from
    * @param args the arguments to pass to the constructor
    * @return an object of the given type
    * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an unwrapping conversion for primitive
    *            arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to the corresponding formal
    *            parameter type by a method invocation conversion.
    * @throws IllegalAccessException if this Constructor object enforces Java language access control and the underlying constructor is
    *            inaccessible.
    * @throws InvocationTargetException if the underlying constructor throws an exception.
    * @throws NoSuchMethodException if the constructor could not be found
    * @throws InstantiationException if the class that declares the underlying constructor represents an abstract class.
    * 
    * @see StrictPA#invokeMethod(Object,String,Object)
    */
    @SuppressWarnings("deprecation")
    public static <T> T instantiate(final Class<? extends T> fromClass, final Object... args) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, args);
    }

    /**
    * Calls a method on the given object instance with the given arguments. Arguments can be fully qualified object types or
    * representations for primitives.
    * 
    * @param instanceOrClass the instance or class to invoke the method on
    * @param methodSignature the name of the method and the parameters <br>
    *           (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
    * @param arguments an array of objects to pass as arguments
    * @return the return value of this method or null if void
    * @throws IllegalAccessException if the method is inaccessible
    * @throws InvocationTargetException if the underlying method throws an exception.
    * @throws NoSuchMethodException if no method with the given <code>methodSignature</code> could be found
    * @throws IllegalArgumentException if an argument couldn't be converted to match the expected type
    */
    @SuppressWarnings("deprecation")
    public static Object invokeMethod(final Object instanceOrClass, final String methodSignature, final Object... arguments) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, correctVarargs(arguments));
    }

    /**
    * Corrects varargs to their initial form. If you call a method with an object-array as last argument the Java varargs mechanism
    * converts this array in single arguments. This method returns an object array if the arguments are all of the same type.
    * 
    * @param arguments the possibly converted arguments of a vararg method
    * @return arguments possibly converted
    */
    private static Object[] correctVarargs(final Object... arguments) {
        if (arguments == null || changedByVararg(arguments)) {
            return new Object[] { arguments };
        }
        return arguments;
    }

    /**
    * Tests if the arguments were changed by vararg. Arguments are changed by vararg if they are of a non primitive array type. E.g.
    * arguments[] = Object[String[]] is converted to String[] while e.g. arguments[] = Object[int[]] is not converted and stays
    * Object[int[]]
    * 
    * Unfortunately we can't detect the difference for arg = Object[primitive] since arguments[] = Object[Object[primitive]] which is
    * converted to Object[primitive] and arguments[] = Object[primitive] which stays Object[primitive]
    * 
    * and we can't detect the difference for arg = Object[non primitive] since arguments[] = Object[Object[non primitive]] is converted
    * to Object[non primitive] and arguments[] = Object[non primitive] stays Object[non primitive]
    * 
    * @param parameters the parameters
    * @return true if parameters were changes by varargs, false otherwise
    */
    private static boolean changedByVararg(final Object[] parameters) {
        if (parameters.length == 0 || parameters[0] == null) {
            return false;
        }
        if (parameters.getClass() == Object[].class) {
            return false;
        }
        return true;
    }

    /**
    * Sets the value of the named field. If fieldName denotes a static field, provide a class, otherwise provide an instance. If the
    * fieldName denotes a final field, this method could fail with an IllegalAccessException, since setting the value of final fields
    * at other times than instantiation can have unpredictable effects.<br/>
    * <br/>
    * Example:<br/>
    * <br/>
    * <code>
    * String myString = "Test"; <br/>
    * <br/>
    * //setting the private field value<br/>
    * StrictPA.setValue(myString, "value", new char[] {'T', 'e', 's', 't'});<br/>
    * <br/>
    * //setting the static final field serialVersionUID - MIGHT FAIL<br/>
    * StrictPA.setValue(myString.getClass(), "serialVersionUID", 1);<br/>
    * <br/>
    * </code>
    * 
    * @param instanceOrClass the instance or class to set the field
    * @param fieldName the name of the field
    * @param value the new value of the field
    * @throws NoSuchFieldException if no field with the given <code>fieldName</code> can be found
    * @throws IllegalAccessException possibly if the field was final
    */
    @SuppressWarnings("deprecation")
    public static void setValue(final Object instanceOrClass, final String fieldName, final Object value) throws NoSuchFieldException, IllegalAccessException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }
}
