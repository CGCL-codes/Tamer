package com.g0dkar.leet.util.reflection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.exception.MirrorException;
import net.vidageek.mirror.reflect.dsl.MethodReflector;
import net.vidageek.mirror.reflect.dsl.ReflectionHandler;
import com.g0dkar.leet.exception.MissingMemberException;
import com.g0dkar.leet.log.Log;
import com.g0dkar.leet.util.common.StringUtils;
import com.g0dkar.leet.util.file.FileUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Classe com vários métodos que auxiliam na execução de "Reflexão por Convenção". Esse conceito tenta expressar, por
 * exemplo, a implementação de uma interface sem implicitamente implementar (sem
 * <code>public class Classe <strong>implements InterfaceQualquer</strong></code> ).
 * 
 * @author Rafael M. Lins
 * 
 */
public final class ReflectionUtils {

    private ReflectionUtils() throws InstantiationException {
        throw new InstantiationException("You cannot create an instance of this class.");
    }

    public static final Map<Class<?>, Class<?>> AUTOBOXING_TYPES;

    public static final Set<Class<?>> JHEAT_PRIMITIVE_TYPES;

    public static final Mirror MIRROR = new Mirror();

    static {
        Map<Class<?>, Class<?>> autoboxingTypesMap = new HashMap<Class<?>, Class<?>>();
        autoboxingTypesMap.put(void.class, Void.class);
        autoboxingTypesMap.put(int.class, Integer.class);
        autoboxingTypesMap.put(long.class, Long.class);
        autoboxingTypesMap.put(byte.class, Byte.class);
        autoboxingTypesMap.put(char.class, Character.class);
        autoboxingTypesMap.put(short.class, Short.class);
        autoboxingTypesMap.put(boolean.class, Boolean.class);
        autoboxingTypesMap.put(float.class, Float.class);
        autoboxingTypesMap.put(double.class, Double.class);
        autoboxingTypesMap.put(Void.class, void.class);
        autoboxingTypesMap.put(Integer.class, int.class);
        autoboxingTypesMap.put(Long.class, long.class);
        autoboxingTypesMap.put(Byte.class, byte.class);
        autoboxingTypesMap.put(Character.class, char.class);
        autoboxingTypesMap.put(Short.class, short.class);
        autoboxingTypesMap.put(Boolean.class, boolean.class);
        autoboxingTypesMap.put(Float.class, float.class);
        autoboxingTypesMap.put(Double.class, double.class);
        AUTOBOXING_TYPES = Collections.unmodifiableMap(autoboxingTypesMap);
        Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(AUTOBOXING_TYPES.keySet().size() * 2 + 4);
        primitiveTypes.addAll(AUTOBOXING_TYPES.keySet());
        primitiveTypes.addAll(AUTOBOXING_TYPES.values());
        primitiveTypes.remove(void.class);
        primitiveTypes.remove(Void.class);
        primitiveTypes.add(char[].class);
        primitiveTypes.add(String.class);
        primitiveTypes.add(Date.class);
        primitiveTypes.add(Calendar.class);
        primitiveTypes.add(BigDecimal.class);
        primitiveTypes.add(Enum.class);
        JHEAT_PRIMITIVE_TYPES = Collections.unmodifiableSet(primitiveTypes);
    }

    /**
	 * Executa o algoritmo de Autoboxing do JHeat sob um determinado tipo de dado
	 * 
	 * @param klass
	 *            O tipo de dado
	 * @return O tipo de dado após o algoritmo de Autoboxing.
	 */
    public static Class<?> autobox(Class<?> klass) {
        if (AUTOBOXING_TYPES.containsKey(klass)) {
            return AUTOBOXING_TYPES.get(klass);
        }
        return klass;
    }

    /**
	 * Verifica se um determinado tipo é um "tipo primitivo" (algo natural para seres humanos). Tecnicamente um tipo
	 * primitivo desses pode ser considerado praticamente qualquer tipo que possa naturalmente ser representado como uma
	 * String como {@link Number Números}, {@link Date Datas}, etc.
	 * 
	 * @param klass
	 *            O tipo de dado
	 * @return <code>true</code> se ele faz parte dos tipos primitivos do JHeat
	 */
    public static boolean isJHeatPrimitive(Class<?> klass) {
        for (Class<?> primitive : JHEAT_PRIMITIVE_TYPES) {
            if (isAssignable(primitive, klass)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Verifica se um determinado tipo é um tipo primitivo Java.
	 * 
	 * @param klass
	 *            O tipo de dado
	 * @return <code>true</code> se ele for primitivo (primitivo real ou auto-boxing)
	 */
    public static boolean isPrimitive(Class<?> klass) {
        if (!klass.isPrimitive()) {
            return autobox(klass).isPrimitive();
        }
        return true;
    }

    /**
	 * Same as
	 * <code>{@link #newInstance(Class, Object...) newInstance}({@link Class#forName(String) Class.forName(className)}, params)</code>
	 * 
	 * @param className
	 *            The Class Name
	 * @param params
	 *            The Constructor Parameters
	 * @return The new instance, or <code>null</code> if it cannot be instantiated
	 * @see #newInstance(Class, Object...)
	 */
    public static Object newInstance(String className, Object... params) {
        try {
            return newInstance(Class.forName(className), params);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
	 * Retorna uma nova instância de uma determinada Class.
	 * 
	 * @param <T>
	 *            O tipo do novo objeto
	 * @param klass
	 *            A classe do objeto
	 * @param params
	 *            Parâmetros que serão passados para o construtor
	 * @return O novo objeto ou <code>null</code> se não puder criá-lo
	 */
    public static <T> T newInstance(Class<T> klass, Object... params) {
        if (params != null && params.length > 0) {
            return MIRROR.on(klass).invoke().constructor().withArgs(params);
        }
        return MIRROR.on(klass).invoke().constructor().withoutArgs();
    }

    /**
	 * <p>
	 * Returns if a given {@link Class} can be instantiated. It checks if:
	 * </p>
	 * <ol>
	 * <li>It has at least 1 <code>public</code> {@link Constructor};</li>
	 * <li>The class itself is <code>public</code>;</li>
	 * <li>The class itself is <strong>NOT</strong> <code>abstract</code>;</li>
	 * <li>The class itself is <strong>NOT</strong> an <code>interface</code>;</li>
	 * </ol>
	 * 
	 * @param klass
	 *            The Class to be checked
	 * @return <code>true</code> if this Class follows the above contitions
	 * 
	 * @see Class#getModifiers()
	 * @see Constructor
	 * @see Constructor#getModifiers()
	 * @see Modifier
	 * @see Modifier#isPublic(int)
	 * @see Modifier#isAbstract(int)
	 * @see Modifier#isInterface(int)
	 */
    public static boolean isConcrete(Class<?> klass) {
        int modifiers = klass.getModifiers();
        boolean hasPublicConstructor = klass.getConstructors() == null || klass.getConstructors().length > 0;
        if (hasPublicConstructor) {
            hasPublicConstructor = false;
            for (Constructor<?> constructor : klass.getConstructors()) {
                if (Modifier.isPublic(constructor.getModifiers())) {
                    hasPublicConstructor = true;
                    break;
                }
            }
        }
        return hasPublicConstructor && Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers);
    }

    /**
	 * Verifica se um tipo é compatível com outro <em>levando em consideração Autoboxing</em>.
	 * 
	 * @param fromClass
	 *            Classe original
	 * @param toClass
	 *            Classe destino
	 * @return <code>true</code> se eles são compatíveis.
	 * @see Class#isAssignableFrom(Class)
	 */
    public static boolean isAssignable(Class<?> fromClass, Class<?> toClass) {
        Class<?> fromType = fromClass.isArray() ? fromClass.getComponentType() : fromClass;
        Class<?> toType = toClass.isArray() ? toClass.getComponentType() : toClass;
        if (fromType.isAssignableFrom(toType)) {
            return true;
        } else {
            int fromDimensions = fromClass.isArray() ? fromClass.getName().lastIndexOf('[') + 1 : 0;
            int toDimensions = toClass.isArray() ? toClass.getName().lastIndexOf('[') + 1 : 0;
            if (AUTOBOXING_TYPES.containsKey(fromType)) {
                fromType = AUTOBOXING_TYPES.get(fromType);
            }
            if (AUTOBOXING_TYPES.containsKey(toType)) {
                toType = AUTOBOXING_TYPES.get(toType);
            }
            return fromDimensions == toDimensions && fromType.isAssignableFrom(toType);
        }
    }

    /**
	 * <p>
	 * Busca um determinado método em uma classe. Este método leva em consideração auto-boxing, herança e outras
	 * relações. Por exemplo, buscar por <code>findMethod("soma", MeuObjeto.class, int.class)</code> pode encontrar
	 * <code>soma(Integer)</code> assim como <code>soma(int)</code>. Em caso de conflitos, as regras de precedência de
	 * tipagem da Sun JVM serão obedecidas.
	 * </p>
	 * 
	 * @param name
	 *            Nome do método
	 * @param objectClass
	 *            {@link Class} do objeto onde o método será buscado
	 * @param parameterTypes
	 *            Tipos dos parâmetros do método. (será aplicado autoboxing, herança, etc. na busca)
	 * @return O primeiro {@link Method} encontrado ou <code>null</code> caso contrário.
	 */
    public static Method findMethod(String name, Class<?> objectClass, Class<?>... parameterTypes) {
        MethodReflector reflector = MIRROR.on(objectClass).reflect().method(name);
        if (parameterTypes == null || parameterTypes.length == 0) {
            return reflector.withoutArgs();
        } else {
            try {
                return reflector.withArgs(parameterTypes);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
	 * Encapsula a chamada de um método reflexivamente. Quando qualquer exceção ocorrer <code>null</code> é retornado.
	 * 
	 * @param method
	 *            O objeto referente ao método
	 * @param object
	 *            O objeto no qual o método será refletido
	 * @param params
	 *            Os parâmetros enviados ao método
	 * @return O resultado da execução do método ou <code>null</code>
	 * @throws Throwable The invoked method raised a exception.
	 */
    public static Object invoke(Method method, Object object, Object... params) {
        try {
            return invokeException(method, object, params);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
	 * Encapsula a chamada de um método reflexivamente. Quando qualquer exceção ocorrer <code>null</code> é retornado.
	 * 
	 * @param method
	 *            O objeto referente ao método
	 * @param object
	 *            O objeto no qual o método será refletido
	 * @param params
	 *            Os parâmetros enviados ao método
	 * @return O resultado da execução do método ou <code>null</code>
	 * @throws Throwable The invoked method raised a exception.
	 */
    public static Object invokeException(Method method, Object object, Object... params) throws Throwable {
        return method.invoke(object, params);
    }

    /**
	 * Verifica se um método pode ser encontrado ou não. Igual a chamar {@link #findMethod(String, Class, Class...)} com
	 * os mesmo parâmetros e verificar se algo foi retornado.
	 * 
	 * @param name
	 *            Nome do método
	 * @param objectClass
	 *            Classe onde executar a busca
	 * @param parameterTypes
	 *            Parâmetros do método
	 * @return <code>true</code> se o método existe
	 */
    public static boolean hasMethod(String name, Class<?> objectClass, Class<?>... parameterTypes) {
        return findMethod(name, objectClass, parameterTypes) != null;
    }

    /**
	 * <p>
	 * Compares if two {@link Method Methods} are compatible with each other.
	 * </p>
	 * 
	 * <p>
	 * Two {@link Method Methods} will be considered compatible if:
	 * </p>
	 * <ol>
	 * <li>Both {@link Method#getName() names} are equal;</li>
	 * <li>They have the same {@link Method#getParameterTypes() number of parameters};</li>
	 * <li>Theirs' parameters are {@link #isAssignable(Class, Class) compatible} and are in the same order.<br />
	 * I.E.: if <b>m1</b> have a 2nd parameter of the {@link Integer} type and <b>m2</b> have a 2nd parameter of the
	 * <code>int</code> type they'll be considered compatible.</li>
	 * </ol>
	 * 
	 * @param m1
	 *            The first Method
	 * @param m2
	 *            The second Method
	 * @return <code>true</code> if it matches all of the items above, <code>false</code> otherwise.
	 */
    public static boolean isMethodsCompatible(Method m1, Method m2) {
        if (m1.equals(m2) && m1.getParameterTypes().length == m2.getParameterTypes().length) {
            List<Class<?>> m1Params = Arrays.asList(m1.getParameterTypes());
            List<Class<?>> m2Params = Arrays.asList(m2.getParameterTypes());
            for (int i = 0, len = m1Params.size(); i < len; i++) {
                if (!isAssignable(m1Params.get(i), m2Params.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
	 * Retorna todos os métodos da {@link Class classe} e de sua hierarquia.
	 * 
	 * @param klass
	 *            {@link Class Classe}
	 * @return Todos os métodos (publicos, privados, protegidos, etc.)
	 */
    public static Collection<Method> getAllMethods(Class<?> klass) {
        return MIRROR.on(klass).reflectAll().methods();
    }

    /**
	 * Return all methods of a class, on the same declared order.
	 * 
	 * @param klass
	 * @return
	 */
    public static List<Method> getAllMethodsOrdered(Class<?> klass) {
        List<String> methodNames = getOrderedMethods(klass);
        List<Method> methods = new LinkedList<Method>();
        for (String method : methodNames) {
            String[] aux = method.split("-");
            String[] types;
            if (aux.length > 1) {
                types = aux[1].split(",");
            } else {
                types = new String[0];
            }
            String name = aux[0];
            if (types.length == 0) {
                methods.add(findMethod(name, klass));
            } else {
                Class<?>[] classes = new Class<?>[types.length];
                for (int i = 0; i < types.length; i++) {
                    String string = types[i].trim();
                    try {
                        classes[i] = Class.forName(string);
                    } catch (ClassNotFoundException e) {
                        Log.error("The class {} could not be found.", string);
                    }
                }
                methods.add(findMethod(name, klass, classes));
            }
        }
        return methods;
    }

    /**
	 * Retorna todos os métodos da {@link Class classe} e de sua hierarquia.
	 * 
	 * @param klass
	 *            {@link Class Classe}
	 * @return Todos os métodos (publicos, privados, protegidos, etc.)
	 */
    public static Collection<java.lang.reflect.Field> getAllFields(Class<?> klass) {
        return MIRROR.on(klass).reflectAll().fields();
    }

    /**
	 * Atribui um valor de um atributo chamando o método set correspondente. Aceita a notação
	 * "elemento.outroElemento.maisOutroElemento" no estilo EL.
	 * 
	 * @param field
	 *            Campo a ser atribuído
	 * @param value
	 *            Valor para o campo
	 * @param object
	 *            Objeto onde a operação será executada
	 */
    public static void set(String field, Object value, Object object) {
        Object ownerField = object;
        String trueField = field;
        if (field.indexOf('.') > 0) {
            String getField = trueField.substring(0, trueField.lastIndexOf('.'));
            ownerField = get(getField, ownerField);
            trueField = trueField.substring(trueField.lastIndexOf('.') + 1);
        }
        if (value != null) {
            String setterMethod = StringUtils.asSetMethod(trueField);
            try {
                MIRROR.on(ownerField).invoke().method(setterMethod).withArgs(value);
            } catch (MirrorException e) {
                throw new MissingMemberException(object.getClass(), setterMethod);
            }
        }
    }

    /**
	 * Captura o valor de um atributo chamando o método get correspondente. Aceita a notação
	 * "elemento.outroElemento.maisOutroElemento" no estilo EL.
	 * 
	 * @param field
	 *            Campo a ser capturado
	 * @param object
	 *            Objeto onde a operação será executada
	 * @throws MissingMemberException
	 *             when field doesn't exist
	 */
    public static Object get(String field, Object object) {
        String[] fieldNames = field.split("\\.");
        Object returnedObject = object;
        Method method;
        for (int i = 0; i < fieldNames.length; i++) {
            method = null;
            String getterMethod = StringUtils.asGetMethod(fieldNames[i]);
            ReflectionHandler<?> handler = MIRROR.on(returnedObject.getClass()).reflect();
            method = handler.method(getterMethod).withoutArgs();
            if (method == null) {
                String getterMethodBoolean = StringUtils.asIsMethod(fieldNames[i]);
                method = handler.method(getterMethodBoolean).withoutArgs();
            }
            if (method != null) {
                returnedObject = MIRROR.on(returnedObject).invoke().method(method).withoutArgs();
            } else {
                throw new MissingMemberException(returnedObject.getClass(), field);
            }
        }
        return returnedObject;
    }

    public static List<Field> getFields(Class<?> klass) {
        List<Field> fields = new ArrayList<Field>();
        Collection<Method> allMethods = getAllMethods(klass);
        Method setMethod;
        String attribName;
        for (Method method : allMethods) {
            attribName = null;
            if (method.getName().startsWith("get") && method.getName().length() > 3) {
                attribName = StringUtils.asMethod(method.getName().substring(3));
            } else if (method.getName().startsWith("is") && method.getName().length() > 2) {
                attribName = StringUtils.asMethod(method.getName().substring(2));
            }
            if (attribName != null && !attribName.equals("class")) {
                setMethod = findMethod(StringUtils.asSetMethod(attribName), klass, method.getReturnType());
                if (setMethod != null) {
                    fields.add(new Field(method, setMethod, findField(attribName, klass), method.getReturnType()));
                }
            }
        }
        final List<String> orderedFields = getOrderedFields(klass);
        Collections.sort(fields, new Comparator<Field>() {

            public int compare(Field arg0, Field arg1) {
                return orderedFields.indexOf(arg0) - orderedFields.indexOf(arg1);
            }
        });
        return fields;
    }

    /**
	 * Retorna todos os campos de uma classe. Campo = Conjunto get + set.
	 * 
	 * @param klass
	 *            Classe para efetuar a busca
	 * @return Coleção de objetos {@link Field}
	 */
    public static Field getField(String fieldName, Class<?> klass) {
        Collection<Method> allMethods = getAllMethods(klass);
        Method setMethod;
        String getterName, getterBooleanName;
        for (Method method : allMethods) {
            getterName = StringUtils.asGetMethod(fieldName);
            getterBooleanName = StringUtils.asIsMethod(fieldName);
            if (method.getName().equals(getterName) || method.getName().equals(getterBooleanName)) {
                String setterName = StringUtils.asSetMethod(fieldName);
                setMethod = findMethod(setterName, klass, method.getReturnType());
                if (setMethod != null) {
                    return new Field(method, setMethod, findField(fieldName, klass), method.getReturnType());
                }
            }
        }
        return null;
    }

    /**
	 * 
	 * @param fieldName
	 * @param klass
	 * @return o java.lang.reflect.Field presente na classe com aquele nome ou <code>null</code> se o campo não existir.
	 */
    public static java.lang.reflect.Field findField(String fieldName, Class<?> klass) {
        return MIRROR.on(klass).reflect().field(fieldName);
    }

    /**
	 * 
	 * @param m1
	 *            Method de uma classe
	 * @param m2
	 *            Method de outra classe
	 * @return Verdadeiro se <code>m1</code> for um método "compatível" com <code>m2</code>. Um método é considerado
	 *         compatível se seus tipos de retorno são compatíveis ({@link Number} e {@link Integer}, por exemplo), seus
	 *         nomes são iguais e seus parâmetros também são iguais (tipos "compatíveis" em parâmetros não são
	 *         considerados).
	 */
    public static boolean equalsMethod(Method m1, Method m2) {
        boolean isCompatibleReturnType = m2.getReturnType().isAssignableFrom(m1.getReturnType());
        List<Class<?>> paramTypes1 = Arrays.asList(m1.getParameterTypes());
        List<Class<?>> paramTypes2 = Arrays.asList(m2.getParameterTypes());
        boolean isCompatibleParamTypes = paramTypes1.equals(paramTypes2);
        return isCompatibleReturnType && m1.getName().equals(m2.getName()) && isCompatibleParamTypes;
    }

    /**
	 * <p>
	 * Verifica se uma classe implementa direta ou indiretamente outra classe.
	 * </p>
	 * 
	 * <p>
	 * Considera-se uma implementação direta o caso onde {@link Class#isAssignableFrom(Class)} retorna <tt>true</tt>.
	 * </p>
	 * 
	 * <p>
	 * Já a implementação indireta se dá quando <code>baseClass</code> contém todos os métodos que
	 * <code>interfaceClass</code>.
	 * </p>
	 * 
	 * @param baseClass
	 *            Classe-base para verificação
	 * @param interfaceClass
	 *            Classe de referência
	 * @return <code>true</code> se <code>baseClass</code> implementa <code>interfaceClass</code> segundo as regras
	 *         descritas anteriormente.
	 */
    public static boolean isImplements(Class<?> baseClass, Class<?> interfaceClass) {
        if (baseClass.isAssignableFrom(interfaceClass)) {
            return true;
        } else {
            List<Method> methods = Arrays.asList(interfaceClass.getMethods());
            Method m = null;
            for (Method method : methods) {
                try {
                    m = baseClass.getMethod(method.getName(), method.getParameterTypes());
                    if (!method.getReturnType().isAssignableFrom(m.getReturnType())) {
                        return false;
                    }
                } catch (SecurityException e) {
                    return false;
                } catch (NoSuchMethodException e) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Collection<Class<?>> getAllClasses(String packageName, String className, Class<? extends Annotation>... annotations) {
        return ClassFinder.find(packageName, className, annotations);
    }

    /**
	 * Retorna o método get do atributo identificador da classe informada
	 * 
	 * @param klass
	 *            classe em que será procurado o método get par ao identificador
	 * @return o método get do identificador
	 */
    public static Method getterID(Class<?> klass) {
        Method result = null;
        ReflectionHandler<?> handler = MIRROR.on(klass).reflect();
        result = handler.method("getId").withoutArgs();
        if (result == null) {
            result = handler.method("getId" + klass.getSimpleName()).withoutArgs();
        }
        if (result == null) {
            throw new MissingMemberException(klass, "default id getter");
        }
        return result;
    }

    /**
	 * Retorna um tipo genérico da propriedade de uma classe que representa uma coleção. Ex.: se a propriedade for
	 * "ArrayList&lt;Animal&gt; animais" então o tipo genérico será "Animal".
	 * 
	 * @param propertyName
	 *            nome do atributo que representa uma coleção na classe informada.
	 * @param klass
	 *            classe que contém o atributo informado.
	 * @return o nome completo da classe que representa o tipo genérico na coleção.
	 */
    public static String getGenericTypeFromProperty(String propertyName, Class<?> klass) {
        String className = null;
        Method method = ReflectionUtils.findMethod(StringUtils.asGetMethod(propertyName), klass);
        if (method != null && Collection.class.isAssignableFrom(method.getReturnType())) {
            String returnTypeStr = method.getGenericReturnType().toString();
            className = returnTypeStr.substring(returnTypeStr.indexOf('<') + 1, returnTypeStr.indexOf('>'));
        }
        return className;
    }

    /**
	 * Retorna um tipo genérico da propriedade de uma classe que representa uma coleção. Ex.: se a propriedade for "
	 * <code>ArrayList&lt;Animal&gt; animais</code> " então o tipo genérico será "Animal".
	 * 
	 * @param propertyName
	 *            nome do atributo que representa uma coleção na classe informada.
	 * @param klass
	 *            classe que contém o atributo informado.
	 * @return o nome completo da classe que representa o tipo genérico na coleção.
	 */
    public static Class<?> getGenericTypeFromField(Field field, Class<?> klass) {
        String className = null;
        if (field.get != null) {
            String returnTypeStr = field.get.getGenericReturnType().toString();
            className = returnTypeStr.substring(returnTypeStr.indexOf('<') + 1, returnTypeStr.indexOf('>'));
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
	 * @param type
	 *            - class to be read. null-safe
	 * @return a list of fields names of given class and its superclasses, in the same order from the bytecode.
	 */
    private static List<String> getOrderedFields(Class<?> type) {
        LinkedList<String> fields = new LinkedList<String>();
        readOrderedFields(type, fields);
        return fields;
    }

    /**
	 * @param type
	 *            - class to be read. null-safe.
	 * @param fieldsNames
	 *            - a list to receive read field names. <b>Must NOT be null.</b>
	 * @return a list of fields names of given class and its superclasses, in the same order from the bytecode.
	 */
    private static void readOrderedFields(Class<?> type, List<String> fields) {
        if (type != null) {
            readOrderedFields(type.getSuperclass(), fields);
            String resourcePath = type.getName().replace(".", "/") + ".class";
            InputStream stream = FileUtils.getResourceAsStream(resourcePath);
            if (stream == null) {
                Log.error("Resource " + resourcePath + " not found.");
            } else {
                try {
                    FieldsNamesCollector visitor = new FieldsNamesCollector();
                    fields.addAll(visitor.getFieldsNames());
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.error("Stream " + stream + " could not be closed.");
                    }
                }
            }
        }
    }

    private static List<String> getOrderedMethods(Class<?> type) {
        LinkedList<String> methods = new LinkedList<String>();
        readOrderedMethods(type, methods);
        return methods;
    }

    private static void readOrderedMethods(Class<?> type, List<String> methods) {
        if (type != null) {
            readOrderedMethods(type.getSuperclass(), methods);
            String resourcePath = type.getName().replace(".", "/") + ".class";
            InputStream stream = FileUtils.getResourceAsStream(resourcePath);
            if (stream == null) {
                Log.error("Resource " + resourcePath + " not found.");
            } else {
                try {
                    MethodNamesCollector visitor = new MethodNamesCollector();
                    methods.addAll(visitor.getMethodNames());
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.error("Stream " + stream + " could not be closed.");
                    }
                }
            }
        }
    }

    /**
	 * <p>
	 * <em>Code based on the {@link TypeToken} class of Google's Gson Library. The original code was made by Bob Lee and Sven Mawson.</em>
	 * </p>
	 * 
	 * <p>
	 * Internal class needed for runtime generics type discovery. It works like this:
	 * </p>
	 * 
	 * <p>
	 * <code>Class&lt;?&gt; genericClassForCollectionOfStrings = new GenericTypeDiscovery&lt;Collection&lt;String&gt;&gt;(){}.getGenericType();</code>
	 * </p>
	 * 
	 * @author Bob Lee
	 * @author Sven Mawson
	 * @author Rafael g0dkar Lins
	 * 
	 * @param <T>
	 *            The Generic Type from which you need the {@link Class} object
	 */
    public abstract static class GenericTypeDiscovery<T> {

        /**
		 * Returns the {@link Class Generic Class Object} for <code>T</code>.
		 * 
		 * @return The {@link Class Generic Class Object} for <code>T</code>
		 */
        public Type getGenericType() {
            Type genericSuperClass = getClass().getGenericSuperclass();
            if (genericSuperClass instanceof Class<?>) {
                return null;
            }
            return ((ParameterizedType) genericSuperClass).getActualTypeArguments()[0];
        }
    }

    /**
	 * Convert signature to simple name.
	 */
    public static String toSimpleName(String signature) {
        int index = signature.lastIndexOf('[');
        String typeName = null;
        if (index != -1) {
            if (signature.lastIndexOf(';') != -1) {
                typeName = signature.substring(index + 1, signature.length() - 1);
            } else {
                typeName = signature.substring(index + 1, signature.length());
            }
        } else {
            if (signature.lastIndexOf(';') != -1) {
                typeName = signature.substring(0, signature.length() - 1);
            } else {
                typeName = signature;
            }
        }
        char ch = typeName.charAt(0);
        switch(ch) {
            case 'L':
                typeName = typeName.substring(1);
                typeName = typeName.replace('/', '.');
                break;
            case 'I':
                typeName = "java.lang.Integer";
                break;
            case 'J':
                typeName = "java.lang.Long";
                break;
            case 'C':
                typeName = "java.lang.Char";
                break;
            case 'Z':
                typeName = "java.lang.Boolean";
                break;
            case 'B':
                typeName = "java.lang.Byte";
                break;
            case 'F':
                typeName = "java.lang.Float";
                break;
            case 'D':
                typeName = "java.lang.Double";
                break;
            case 'V':
                typeName = "void";
                break;
            default:
                System.out.println("error: " + typeName);
                break;
        }
        if (index != -1) {
            StringBuffer sb = new StringBuffer(typeName);
            for (int i = 0; i <= index; i++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return typeName;
    }

    /**
	 * Classe interna para representar um campo de uma classe. Campo = Conjunto de get/set.
	 */
    public static class Field {

        public final Method get;

        public final Method set;

        public final String name;

        public final java.lang.reflect.Field field;

        public final Class<?> type;

        public Field(Method get, Method set, java.lang.reflect.Field field, Class<?> type) {
            this.get = get;
            this.set = set;
            this.name = StringUtils.asField(get.getName());
            this.field = field;
            this.type = type;
        }

        public String toString() {
            return "[field: " + name + ", get: " + get.getName() + "(), set: " + set.getName() + "()]";
        }

        public Class<?> fieldType() {
            if (type == null) {
                if (field == null) {
                    return get.getReturnType();
                }
                return field.getType();
            }
            return type;
        }

        public Type genericType() {
            if (field == null) {
                return get.getGenericReturnType();
            }
            return field.getGenericType();
        }

        /**
		 * @return true if the field or getter are annotated with {@link Ignored}, and thus this field must be ignored
		 *         by jheat.
		 */
        public boolean isIgnored() {
            return isIgnored(field) || isIgnored(get);
        }

        private boolean isIgnored(AccessibleObject member) {
            return false;
        }

        /**
		 * @param annotationClass
		 * @return the annotation if present in field or getter.
		 */
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            if (field != null && field.isAnnotationPresent(annotationClass)) {
                if (get.isAnnotationPresent(annotationClass)) {
                    Log.error("Duplicated @Property on getter: {}", get.toString());
                }
                return field.getAnnotation(annotationClass);
            }
            return get.getAnnotation(annotationClass);
        }
    }
}
