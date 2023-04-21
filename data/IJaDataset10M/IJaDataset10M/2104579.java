package net.sf.implessbean;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class Materializer {

    ClassLoader classLoader = Materializer.class.getClassLoader();

    ClassPool pool = new ClassPool(ClassPool.getDefault());

    public void insertClassPath(Class<?> clazz) {
        pool.insertClassPath(new ClassClassPath(clazz));
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected Materializer() {
    }

    public static Materializer newMaterializer() {
        return new Materializer();
    }

    /**
	 * @param args
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException 
	 */
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println(escapeString("abc\\def \"a\nd\\\" you"));
        Materializer m = Materializer.newMaterializer();
        Class cls = m.materialize(Materializer.class.getClassLoader(), "net.sf.implessbean.generated.Customer", Object.class, ICustomer.class);
        ICustomer customer = (ICustomer) cls.newInstance();
        customer.setBirthday(new Date());
        customer.setId(10);
        customer.setName("Olivia");
        System.out.println(customer.getName());
        System.out.println(customer.getId());
        System.out.println(customer.getBirthday());
    }

    public Class materialize(String className, Class<?> superClass, Class<?>... interfaces) throws ClassNotFoundException, NotFoundException, CannotCompileException {
        return materialize(classLoader, className, superClass, interfaces);
    }

    public Class materialize(ClassLoader classLoader, String className, Class<?> superClass, Class<?>... interfaces) throws ClassNotFoundException, NotFoundException, CannotCompileException {
        CtClass[] ifClasses = new CtClass[interfaces.length];
        for (int i = 0; i < ifClasses.length; i++) {
            ifClasses[i] = pool.get(interfaces[i].getName());
        }
        CtClass newClass = pool.makeClass(className, pool.get(superClass.getName()));
        try {
            newClass.setInterfaces(ifClasses);
            CtConstructor ctor = new CtConstructor(new CtClass[0], newClass);
            ctor.setBody("{super();}");
            newClass.addConstructor(ctor);
            Map<String, PropertyInfo> props = pickUpAbstractAccessors(newClass);
            for (PropertyInfo propInfo : props.values()) {
                String propName = "_ib_" + propInfo.name;
                CtField field = new CtField(propInfo.type, propName, newClass);
                newClass.addField(field);
                for (MethodInfo methodInfo : propInfo.methods) {
                    Object[] annotations = methodInfo.method.getAnnotations();
                    CtMethod method = new CtMethod(methodInfo.method, newClass, null);
                    int modifiers = methodInfo.method.getModifiers();
                    method.setModifiers(modifiers & ~Modifier.ABSTRACT);
                    if (methodInfo.operation == AccessorOperation.GET) {
                        makeGetter(method, propName, annotations);
                    } else if (methodInfo.operation == AccessorOperation.SET) {
                        makeSetter(method, propName, annotations);
                    } else {
                        assert false;
                    }
                    newClass.addMethod(method);
                }
            }
            Class cls = newClass.toClass(classLoader);
            return cls;
        } finally {
            newClass.detach();
            newClass = null;
        }
    }

    static void makeGetter(CtMethod method, String propName, Object[] annotations) throws CannotCompileException, NotFoundException {
        StringBuilder sb = new StringBuilder("{");
        for (Object annotation : annotations) {
            if (annotation instanceof RejectNull) {
                RejectNull rejectNull = (RejectNull) annotation;
                if (method.getReturnType().isPrimitive()) {
                    throw new IllegalArgumentException("primitive type property shouldn't annotated as " + RejectNull.class.getName() + ": " + method);
                }
                sb.append("if ($0." + propName + " == null) throw new java.lang.IllegalStateException(\"");
                sb.append(escapeString(rejectNull.errorMessage()));
                sb.append("\");");
            } else {
            }
        }
        sb.append("return $0." + propName + ";}");
        method.setBody(sb.toString());
    }

    static String escapeString(String str) {
        assert str == null;
        return str.replaceAll("([\"\\\\\n])", "\\\\$1");
    }

    static void makeSetter(CtMethod method, String propName, Object[] annotations) throws CannotCompileException, NotFoundException {
        StringBuilder sb = new StringBuilder("{");
        for (Object annotation : annotations) {
            if (annotation instanceof RejectNull) {
                RejectNull rejectNull = (RejectNull) annotation;
                if (method.getParameterTypes()[0].isPrimitive()) {
                    throw new IllegalArgumentException("primitive type property shouldn't annotated as " + RejectNull.class.getName() + ": " + method);
                }
                sb.append("if ($1 == null)" + " throw new java.lang.IllegalArgumentException(\"");
                sb.append(escapeString(rejectNull.errorMessage()));
                sb.append("\");");
            } else {
            }
        }
        sb.append("$0." + propName + " = $1; return;}");
        method.setBody(sb.toString());
    }

    static Map<String, PropertyInfo> pickUpAbstractAccessors(CtClass newClass) throws NotFoundException, CannotCompileException {
        CtMethod[] methods = newClass.getMethods();
        int modMask = Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL;
        int modValue = Modifier.ABSTRACT;
        Map<String, PropertyInfo> props = new HashMap<String, PropertyInfo>();
        for (CtMethod method : methods) {
            if ((method.getModifiers() & modMask) == modValue) {
                processMethod(props, method);
            }
        }
        return props;
    }

    static class MethodInfo {

        CtMethod method;

        AccessorOperation operation;

        MethodInfo(CtMethod method, AccessorOperation operation) {
            this.method = method;
            this.operation = operation;
        }
    }

    static class PropertyInfo {

        String name;

        CtClass type;

        List<MethodInfo> methods = new ArrayList<MethodInfo>();

        PropertyInfo(String name, CtClass type) {
            this.name = name;
            this.type = type;
        }
    }

    static void processMethod(Map<String, PropertyInfo> propertyInfo, CtMethod method) throws NotFoundException, CannotCompileException {
        AccessorOperation operation = AccessorOperation.OTHER;
        String name = method.getName();
        CtClass returnType = method.getReturnType();
        String propName = null;
        try {
            int propStarts = -1;
            char a = name.charAt(0);
            char b = name.charAt(1);
            char c = name.charAt(2);
            if (b == 'e' && c == 't') {
                if (a == 'g') {
                    operation = AccessorOperation.GET;
                    propStarts = 3;
                } else if (a == 's') {
                    operation = AccessorOperation.SET;
                    propStarts = 3;
                }
            } else if (a == 'i' && b == 's' && Boolean.TYPE.equals(returnType.getClass())) {
                operation = AccessorOperation.GET;
                propStarts = 2;
            }
            char firstChar = name.charAt(propStarts);
            if (Character.isLowerCase(firstChar)) {
                return;
            }
            propName = Introspector.decapitalize(name.substring(propStarts));
            CtClass propType = null;
            CtClass[] paramTypes = method.getParameterTypes();
            if (operation == AccessorOperation.SET && paramTypes.length == 1) {
                propType = paramTypes[0];
            }
            if (operation == AccessorOperation.GET && paramTypes.length == 0) {
                propType = returnType;
            }
            if (propType == null) {
                System.err.println(method);
                return;
            }
            PropertyInfo propInfo = propertyInfo.get(propName);
            if (propInfo == null) {
                propInfo = new PropertyInfo(propName, propType);
                propertyInfo.put(propName, propInfo);
            }
            if (!propInfo.type.equals(propType)) {
                System.err.println("type conflict: " + propName);
            }
            propInfo.methods.add(new MethodInfo(method, operation));
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }
}

class XClassLoader extends ClassLoader {
}
