package pl.wcislo.sbql4j.java.model.compiletime.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.wcislo.sbql4j.exception.NotSupportedException;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ConstructorSignature;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;

@Deprecated
public class JavaSignatureReflectFactory extends JavaSignatureFactory {

    public static final ValueSignature VOID_VAL_SIG = null;

    public static final ClassSignature VOID_CLASS_SIG = null;

    private Map<Class, ValueSignature> cacheValueSig = new HashMap<Class, ValueSignature>();

    private Map<Class, ClassSignature> cacheClassSig = new HashMap<Class, ClassSignature>();

    private Map<Method, MethodSignature> cacheMethods = new HashMap<Method, MethodSignature>();

    private Map<Constructor, ConstructorSignature> cacheConstructors = new HashMap<Constructor, ConstructorSignature>();

    private JavaObjectReflectFactory fac = JavaObjectReflectFactory.getInstance();

    private JavaSignatureReflectFactory() {
        super();
    }

    private static JavaSignatureFactory instance;

    public static JavaSignatureFactory getInstance() {
        if (instance == null) {
            instance = new JavaSignatureReflectFactory();
        }
        return instance;
    }

    public ValueSignature createValueSignature(Type javaCompilerType, boolean staticMembersOnly) throws NotSupportedException {
        java.lang.reflect.Type javaType = compilerTypeToClass(javaCompilerType);
        if (javaType == null) return null;
        if (javaType == Void.TYPE) return VOID_VAL_SIG;
        Class javaClass = null;
        ParameterizedType paramType = null;
        if (javaType instanceof Class) {
            javaClass = (Class) javaType;
        } else if (javaType instanceof ParameterizedType) {
            paramType = (ParameterizedType) javaType;
            javaClass = (Class) paramType.getRawType();
        } else if (javaType instanceof GenericArrayType) {
            GenericArrayType gt = (GenericArrayType) javaType;
            java.lang.reflect.Type t = Object.class;
            ValueSignature nSig = createValueSignature(javaCompilerType, staticMembersOnly);
            nSig.sColType = SCollectionType.SEQUENCE;
            return nSig;
        }
        javaClass = checkPrimitiveClass(javaClass);
        SCollectionType cType = SCollectionType.NO_COLLECTION;
        if (List.class.isAssignableFrom(javaClass)) {
            cType = SCollectionType.SEQUENCE;
        } else if (Collection.class.isAssignableFrom(javaClass)) {
            cType = SCollectionType.BAG;
        }
        if (javaClass.isArray()) {
            if (cType != SCollectionType.NO_COLLECTION) {
            }
            cType = SCollectionType.SEQUENCE;
        }
        if (cType != SCollectionType.NO_COLLECTION) {
            Class newJavaType = Object.class;
            if (!javaClass.isArray()) {
                if (paramType != null) {
                    java.lang.reflect.Type t0 = paramType.getActualTypeArguments()[0];
                    ValueSignature vs = null;
                    if (vs.isCollectionResult()) {
                    }
                    vs.sColType = cType;
                    return vs;
                } else if (javaClass.getTypeParameters().length > 0) {
                    TypeVariable t = javaClass.getTypeParameters()[0];
                }
            } else {
                Class arrayClass = javaClass.getComponentType();
                ValueSignature vs = null;
                if (vs.isCollectionResult()) {
                }
                vs.sColType = cType;
                return vs;
            }
            javaType = newJavaType;
            javaClass = newJavaType;
        }
        ValueSignature res = cacheValueSig.get(javaType);
        if (res == null) {
            Map<String, Field> fields = fac.getValFields(javaClass, staticMembersOnly);
            Map<String, List<Method>> methods = fac.getValMethods(javaClass, staticMembersOnly);
        }
        return res;
    }

    public ClassSignature createClassSignature(Type.ClassType javaType) {
        Class javaClass = compilerTypeToClass(javaType);
        if (javaClass == null) return null;
        if (javaClass == Void.TYPE) return VOID_CLASS_SIG;
        javaClass = checkPrimitiveClass(javaClass);
        ClassSignature res = cacheClassSig.get(javaClass);
        if (res == null) {
            Map<String, Field> fields = fac.getValFields(javaClass, javaType, true);
            Map<String, List<Method>> methods = fac.getValMethods(javaClass, true);
            List<Constructor> constr = fac.getConstructors(javaClass);
            cacheClassSig.put(javaClass, res);
        }
        return res;
    }

    @Deprecated
    public MethodSignature createJavaMethodSignature(Symbol.MethodSymbol method) {
        Method m = null;
        if (m == null) return null;
        MethodSignature res = cacheMethods.get(m);
        if (res == null) {
            java.lang.reflect.Type rTypeGen = m.getGenericReturnType();
            Class rType = m.getReturnType();
            StructSignature paramsSig = new StructSignature();
            if (m.getParameterTypes() != null) {
                TypeVariable<Method>[] params = m.getTypeParameters();
                for (Class c : m.getParameterTypes()) {
                }
            }
            cacheMethods.put(m, res);
        }
        return res;
    }

    @Deprecated
    public ConstructorSignature createJavaConstructorSignature(Symbol.MethodSymbol constr2) {
        Constructor constr = null;
        if (constr == null) return null;
        ConstructorSignature res = cacheConstructors.get(constr);
        if (res == null) {
            java.lang.reflect.Type rTypeGen = constr.getDeclaringClass();
            StructSignature paramsSig = new StructSignature();
            if (constr.getParameterTypes() != null) {
                for (Class c : constr.getParameterTypes()) {
                }
            }
            cacheConstructors.put(constr, res);
        }
        return res;
    }

    @Deprecated
    public ValueSignature createJavaSignature(Type javaType) throws NotSupportedException {
        return null;
    }

    public static Class checkPrimitiveClass(Class c) {
        if (c == null) return c;
        if (c.isPrimitive()) {
            if (c == Boolean.TYPE) {
                return Boolean.class;
            } else if (c == Character.TYPE) {
                return Character.class;
            } else if (c == Byte.TYPE) {
                return Byte.class;
            } else if (c == Short.TYPE) {
                return Short.class;
            } else if (c == Integer.TYPE) {
                return Integer.class;
            } else if (c == Long.TYPE) {
                return Long.class;
            } else if (c == Float.TYPE) {
                return Float.class;
            } else if (c == Double.TYPE) {
                return Double.class;
            }
            return null;
        } else {
            return c;
        }
    }
}
