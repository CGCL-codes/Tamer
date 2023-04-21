package java.lang.reflect;

import org.jikesrvm.classloader.Atom;
import gnu.java.lang.ClassHelper;
import gnu.java.lang.reflect.MethodSignatureParser;
import gnu.java.lang.reflect.FieldSignatureParser;

/**
 * Class library dependent helper methods used to implement
 * class library independent code in java.lang.reflect
 */
class JikesRVMHelpers {

    static String getUserName(Class<?> c) {
        return ClassHelper.getUserName(c);
    }

    static <T> TypeVariable<Constructor<T>>[] getTypeParameters(Constructor<T> constructor, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(constructor, sig.toString());
        return p.getTypeParameters();
    }

    static Type[] getGenericExceptionTypes(Constructor<?> constructor, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(constructor, sig.toString());
        return p.getGenericExceptionTypes();
    }

    static Type[] getGenericParameterTypes(Constructor<?> constructor, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(constructor, sig.toString());
        return p.getGenericParameterTypes();
    }

    static TypeVariable<Method>[] getTypeParameters(Method method, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(method, sig.toString());
        return p.getTypeParameters();
    }

    static Type[] getGenericExceptionTypes(Method method, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(method, sig.toString());
        return p.getGenericExceptionTypes();
    }

    static Type[] getGenericParameterTypes(Method method, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(method, sig.toString());
        return p.getGenericParameterTypes();
    }

    static Type getGenericReturnType(Method method, Atom sig) {
        MethodSignatureParser p = new MethodSignatureParser(method, sig.toString());
        return p.getGenericReturnType();
    }

    static Type getFieldType(Field field, Atom sig) {
        FieldSignatureParser p = new FieldSignatureParser(field.getDeclaringClass(), sig.toString());
        return p.getFieldType();
    }
}
