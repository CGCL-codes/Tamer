package activejava.vm;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 
 * AcitveJava
 *
 * Java���乤�߼�
 *
 * @author �����
 * @email 17bity@gmail.com
 * @since 2006-9-19
 * @version 0.1a
 */
public class ReflectionUtils {

    /**
	 * �õ�ĳ������Ĺ�������
	 * 
	 * @param owner,
	 *            fieldname
	 * @return �����Զ���
	 * @throws Exception
	 * 
	 */
    public static Object getProperty(Object owner, String fieldName) throws Exception {
        Class ownerClass = owner.getClass();
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(owner);
        return property;
    }

    /**
	 * �õ�ĳ��ľ�̬��������
	 * 
	 * @param class_name
	 *            ����
	 * @param field_name
	 *            ������
	 * @return �����Զ���
	 * @throws Exception
	 */
    public static Object getStaticProperty(String className, String fieldName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class ownerClass = getClass(className);
        Field field = ownerClass.getField(fieldName);
        Object property = field.get(ownerClass);
        return property;
    }

    /**
	 * �õ�ĳ�����Class
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
    public static Class getClass(Object obj) throws Exception {
        return obj.getClass();
    }

    /**
	 * ִ��ĳ���󷽷�
	 * 
	 * @param owner
	 *            ����
	 * @param method_name
	 *            ������
	 * @param args
	 *            ����
	 * @return ��������ֵ
	 * @throws Exception
	 */
    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        String className = ownerClass.getName();
        Method[] methods = ownerClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                Class[] paramClasses = methods[i].getParameterTypes();
                if (paramClasses.length == args.length) {
                    for (int j = 0; j < paramClasses.length; j++) {
                        if (!isAssignableFrom(args[j].getClass().getName(), paramClasses[j].getName())) {
                            break;
                        }
                        if (j == args.length - 1) {
                            return methods[i].invoke(owner, args);
                        }
                    }
                }
            }
        }
        throw new NoSuchMethodException("Class " + className + " has no " + methodName + " method ");
    }

    /**
	 * ִ��ĳ��ľ�̬����
	 * 
	 * @param className
	 *            ����
	 * @param methodName
	 *            ������
	 * @param args
	 *            ��������
	 * @return ִ�з������صĽ��
	 * @throws Exception
	 */
    public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);
        Method[] methods = ownerClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                Class[] paramClasses = methods[i].getParameterTypes();
                if (paramClasses.length == args.length) {
                    for (int j = 0; j < paramClasses.length; j++) {
                        if (!isAssignableFrom(args[j].getClass().getName(), paramClasses[j].getName())) {
                            break;
                        }
                        if (j == args.length - 1) {
                            return methods[i].invoke(null, args);
                        }
                    }
                }
            }
        }
        throw new NoSuchMethodException("Class " + className + " has no " + methodName + " method ");
    }

    /**
	 * �½�ʵ��
	 * 
	 * @param className
	 *            ����
	 * @param args
	 *            ���캯��Ĳ���
	 * @return �½���ʵ��
	 * @throws Exception
	 */
    public static Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor cons = newoneClass.getConstructor(argsClass);
        return cons.newInstance(args);
    }

    /**
	 * �ǲ���ĳ�����ʵ��
	 * @param obj ʵ��
	 * @param cls ��
	 * @return ��� obj �Ǵ����ʵ���򷵻� true
	 */
    public static boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    /**
	 * �õ������е�ĳ��Ԫ��
	 * @param array ����
	 * @param index ����
	 * @return ����ָ��������������������ֵ
	 */
    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }

    /**
	 * ���������е�Ԫ��
	 * @param array ����
	 * @param index ���
	 * @param value ����ֵ
	 * @throws IllegalArgumentException
	 * @throws ArrayIndexOutOfBoundsException
	 */
    public static void setValueIntoArray(Object array, int index, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Array.set(array, index, value);
    }

    /**
	 *  �ж��� Class �������ʾ�����ӿ���ָ���� Class �������ʾ�����ӿ��Ƿ���ͬ�����Ƿ����䳬��򳬽ӿڡ�
	 * @param superName ������
	 * @param inheritName
	 * @return
	 * @throws ClassNotFoundException
	 */
    public static boolean isAssignableFrom(String superName, String inheritName) throws ClassNotFoundException {
        System.out.println("isAssignableFrom : left:" + superName + "  right:" + inheritName);
        if (superName.equals("Dynamic") || inheritName.equals("Dynamic")) return true;
        if (superName.equals("int") && inheritName.equals("java.lang.Integer")) return true;
        if (superName.equals("java.lang.Integer") && inheritName.equals("int")) return true;
        if (superName.equals("boolean") && inheritName.equals("java.lang.Boolean")) return true;
        if (superName.equals("java.lang.Boolean") && inheritName.equals("boolan")) return true;
        if (superName.equals("char") && inheritName.equals("java.lang.Character")) return true;
        if (superName.equals("java.lang.Character") && inheritName.equals("char")) return true;
        Class superClass = getClass(superName);
        Class inheritClass = getClass(inheritName);
        return superClass.isAssignableFrom(inheritClass);
    }

    /**
	 * �½�����ʵ��
	 * @param className ������
	 * @param length ���鳤��
	 * @return ����
	 * @throws ClassNotFoundException
	 */
    public static Object newArray(String className, int length) throws ClassNotFoundException {
        Class classBase = getClass(className);
        return Array.newInstance(classBase, length);
    }

    /**
	 * �õ�ĳ�����Ե�����
	 * @param className ����
	 * @param fieldName ������
	 * @return ��������
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
    public static String getFieldTypeName(String className, String fieldName) throws ClassNotFoundException, NoSuchFieldException {
        System.out.println("in getFieldTypeName :" + className + "  " + fieldName);
        Class classBase = getClass(className);
        Field field = classBase.getField(fieldName);
        System.out.println("return :" + field.getType().getName());
        return field.getType().getName();
    }

    /**
	 * �õ����ĳ�����ķ���ֵ������
	 * @param className ����
	 * @param methodName ������
	 * @param args ����������������飬���û�в��������null
	 * @return ����ֵ������
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
    public static String getMethodReturnTypeName(String className, String methodName, String[] args) throws ClassNotFoundException, NoSuchMethodException {
        Class classBase = Class.forName(className);
        Method method;
        if (args != null) {
            Class[] argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = getClass(args[i]);
            }
            method = classBase.getMethod(methodName, argsClass);
        } else {
            method = classBase.getMethod(methodName, new Class[0]);
        }
        return method.getReturnType().getName();
    }

    /**
	 * �������õ��֧࣬�ֻ���
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
    public static Class getClass(String name) throws ClassNotFoundException {
        if (name.endsWith("char")) {
            return char.class;
        } else if (name.endsWith("int")) {
            return int.class;
        } else if (name.endsWith("float")) {
            return float.class;
        } else if (name.endsWith("long")) {
            return long.class;
        } else if (name.endsWith("boolean")) {
            return boolean.class;
        } else if (name.endsWith("double")) {
            return double.class;
        } else if (name.endsWith("byte")) {
            return byte.class;
        } else if (name.endsWith("short")) {
            return short.class;
        } else if (name.endsWith("void")) {
            return void.class;
        } else {
            return Class.forName(name);
        }
    }

    /**
	 * ���þ�̬����
	 * @param className ����
	 * @param propertyName ������
	 * @param value ֵ
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
    public static void setStaticProperty(String className, String propertyName, Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class owner = Class.forName(className);
        Field field = owner.getField(propertyName);
        Class type = field.getType();
        if (type.equals(int.class)) {
            field.setInt(null, ((Integer) value).intValue());
        } else if (type.equals(float.class)) {
            field.setFloat(null, ((Float) value).floatValue());
        } else if (type.equals(long.class)) {
            field.setLong(null, ((Long) value).longValue());
        } else if (type.equals(boolean.class)) {
            field.setBoolean(null, ((Boolean) value).booleanValue());
        } else if (type.equals(double.class)) {
            field.setDouble(null, ((Double) value).doubleValue());
        } else if (type.equals(byte.class)) {
            field.setByte(null, ((Byte) value).byteValue());
        } else if (type.equals(short.class)) {
            field.setShort(null, ((Short) value).shortValue());
        } else {
            field.set(null, value);
        }
    }

    /**
	 * ��������
	 * @param ownerObject ���õĶ���
	 * @param propertyName ����
	 * @param value ֵ
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
    public static void setProperty(Object ownerObject, String propertyName, Object value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class owner = ownerObject.getClass();
        Field field = owner.getField(propertyName);
        Class type = field.getType();
        if (type.equals(int.class)) {
            field.setInt(ownerObject, ((Integer) value).intValue());
        } else if (type.equals(float.class)) {
            field.setFloat(ownerObject, ((Float) value).floatValue());
        } else if (type.equals(long.class)) {
            field.setLong(ownerObject, ((Long) value).longValue());
        } else if (type.equals(boolean.class)) {
            field.setBoolean(ownerObject, ((Boolean) value).booleanValue());
        } else if (type.equals(double.class)) {
            field.setDouble(ownerObject, ((Double) value).doubleValue());
        } else if (type.equals(byte.class)) {
            field.setByte(ownerObject, ((Byte) value).byteValue());
        } else if (type.equals(short.class)) {
            field.setShort(ownerObject, ((Short) value).shortValue());
        } else {
            field.set(ownerObject, value);
        }
    }
}
