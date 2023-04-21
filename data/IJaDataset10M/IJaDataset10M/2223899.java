package com.prefabware.jmodel;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import com.prefabware.commons.JavaUtil;
import com.prefabware.commons.StringUtil;
import com.prefabware.commons.reflection.ReflectionUtil;
import com.prefabware.jmodel.annotation.JAnnotationClass;
import com.prefabware.jmodel.annotation.JAnnotationClass.JAnnotationMethod;
import com.prefabware.jmodel.sample.WithExplicitConstructor;
import com.prefabware.jmodel.sample.WithMethodeOverrideAndDifferentReturnType;
import com.prefabware.jmodel.sample.WithToString;
import com.prefabware.jmodel.sample.WithoutExplicitConstructor;
import com.prefabware.jmodel.typefactory.TypeFactory;

public class JavaUtilTest {

    private TypeFactory typeFactory;

    @Before
    public void setUp() throws Exception {
        typeFactory = new TypeFactory();
    }

    @Test
    public void testWhereDeclared() {
        Class<?>[] parameterTypes = new Class<?>[0];
        Method toString = ReflectionUtil.findDeclaredMethod(String.class, "toString", parameterTypes);
        assertNotNull(toString);
        Class<?> firstDeclared = ReflectionUtil.firstDeclaredWhere(String.class, toString.getName(), parameterTypes);
        assertEquals(Object.class, firstDeclared);
    }

    @Test
    public void testgetSimpleName() throws Exception {
        String qname = "java.util.Currency";
        String regex = String.format("(%s)([a-zA-Z_0-9]*)", Pattern.quote("."), Pattern.quote("."));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qname);
        System.out.println(String.format("groupCount:%d", matcher.groupCount()));
        while (matcher.find()) {
            System.out.println(String.format("%s start:%d, end:%d", matcher.group(), matcher.start(), matcher.end()));
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(String.format("group(%d):%s", i, matcher.group(i)));
            }
            String result = matcher.group(2);
            System.out.println(String.format("result:%s", result));
        }
    }

    @Test
    public void testgetPackageName() throws Exception {
        String qname = "java.util.Currency";
        String regex = String.format("(.*)(%s)", Pattern.quote("."), Pattern.quote("."));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qname);
        System.out.println(String.format("groupCount:%d", matcher.groupCount()));
        while (matcher.find()) {
            System.out.println(String.format("%s start:%d, end:%d", matcher.group(), matcher.start(), matcher.end()));
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(String.format("group(%d):%s", i, matcher.group(i)));
            }
            String result = matcher.group(1);
            System.out.println(String.format("result:%s", result));
        }
    }

    @Test
    public void testGetterName() throws Exception {
        assertEquals("getCountry", JavaUtil.getGetterName("country", null));
    }

    @Test
    public void testGetGetterFieldName() throws Exception {
        assertEquals("country", JavaUtil.getGetterFieldName("getCountry", false));
        assertEquals("bookable", JavaUtil.getGetterFieldName("isBookable", true));
    }

    @Test
    public void testGetSetterFieldName() throws Exception {
        assertEquals("country", JavaUtil.getSetterFieldName("setCountry"));
    }

    @Test
    public void testEnumCode() throws Exception {
        TestEnum one = TestEnum.ONE;
        String asCode = one.getClass().getName() + "." + one.name();
        assertEquals("com.prefabware.jmodel.JavaUtilTest$TestEnum.ONE", asCode);
    }

    public enum TestEnum {

        ONE, TWO, THREE
    }

    @Test
    public void testEnumArrayCode() throws Exception {
        TestEnum[] array = TestEnum.values();
        List<String> asCodeList = new ArrayList<String>();
        for (TestEnum one : array) {
            String asCode = one.getClass().getName() + "." + one.name();
            asCodeList.add(asCode);
        }
        String arrayAsCode = "{" + StringUtil.getListAsString(asCodeList, ",") + "}";
        assertEquals("{com.prefabware.jmodel.JavaUtilTest$TestEnum.ONE,com.prefabware.jmodel.JavaUtilTest$TestEnum.TWO,com.prefabware.jmodel.JavaUtilTest$TestEnum.THREE}", arrayAsCode);
    }

    /**
	 * to test the names of primitive classes
	 * 
	 * @throws Exception
	 */
    @Test
    public void testPrimitives() throws Exception {
        assertEquals("int", int.class.getName());
        assertEquals("void", void.class.getName());
    }

    @Test
    public void testWithToString() throws Exception {
        Class<WithToString> cls = WithToString.class;
        Method[] declaredMethods = cls.getDeclaredMethods();
        assertEquals(1, declaredMethods.length);
    }

    @Test
    public void testWithMethodeOverrideAndDifferentReturnType() throws Exception {
        Class<WithMethodeOverrideAndDifferentReturnType> cls = WithMethodeOverrideAndDifferentReturnType.class;
        Method[] declaredMethods = cls.getDeclaredMethods();
        assertEquals(2, declaredMethods.length);
        Method method1 = declaredMethods[0];
        Method method2 = declaredMethods[1];
        assertEquals(method1.getName(), method2.getName());
        assertArrayEquals(method1.getParameterTypes(), method2.getParameterTypes());
        Method myMethod = getMethodWithMostSpecificReturnType(method1, Arrays.asList(declaredMethods));
        assertEquals(String.class, myMethod.getReturnType());
        myMethod = getMethodWithMostSpecificReturnType(method2, Arrays.asList(declaredMethods));
        assertEquals(String.class, myMethod.getReturnType());
        List<Method> uniqueMethods = ReflectionUtil.getDeclaredMethodsUnique(cls);
        assertEquals(1, uniqueMethods.size());
        assertEquals(String.class, uniqueMethods.get(0).getReturnType());
    }

    private Method getMethodWithMostSpecificReturnType(Method method1, List<Method> asList) {
        Method candidate = method1;
        for (Method method : asList) {
            if (method.getName().equals(candidate.getName()) && Arrays.equals(method.getParameterTypes(), candidate.getParameterTypes())) {
                if (candidate.getReturnType().isAssignableFrom(method.getReturnType())) {
                    candidate = method;
                }
            }
        }
        return candidate;
    }

    @Test
    public void testWithoutExplicitConstructor() throws Exception {
        Class<WithoutExplicitConstructor> cls = WithoutExplicitConstructor.class;
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        assertEquals(1, declaredConstructors.length);
    }

    /**
	 * to test,that for cunstroctors it does not matter, whether a constructor is
	 * inherited or overwritten, reflection will only return that one constructor once
	 * in contrast to methods. If overloading happens with different returntypes, a method might be returned twice 
	 * @throws Exception
	 */
    @Test
    public void testWithExplicitConstructor() throws Exception {
        Class<WithExplicitConstructor> cls = WithExplicitConstructor.class;
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        assertEquals(1, declaredConstructors.length);
    }

    @Test
    public void testLinkedHashMap() throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("5", "5");
        map.put("3", "3");
        map.put("4", "4");
        map.put("2", "2");
        map.put("1", "1");
        System.out.println(map.values());
        List<String> list = new ArrayList<String>();
        list.addAll(map.values());
        System.out.println(list);
        System.out.println(Collections.unmodifiableList(list));
    }

    @Test
    public void testJAnnotationDefault() throws Exception {
        Class<Test> cls = org.junit.Test.class;
        JAnnotationClass jc = typeFactory.getJType(cls);
        JAnnotationMethod am = jc.getAnnotationMethod("expected");
        assertNotNull(am);
        assertEquals(None.class, am.getDefaultValue());
    }
}
