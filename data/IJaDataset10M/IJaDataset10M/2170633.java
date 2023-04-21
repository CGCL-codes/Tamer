package cz.possoft.explorer.test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.management.ObjectName;
import javax.swing.JLabel;
import junit.framework.TestCase;
import cz.possoft.explorer.ServiceFactory;
import cz.possoft.explorer.context.FieldModifierEnum;
import cz.possoft.explorer.context.ObjectContext;
import cz.possoft.explorer.context.ObjectContextBuilder;
import cz.possoft.explorer.converter.Converter;
import cz.possoft.explorer.converter.ConverterException;
import cz.possoft.explorer.meta.ClassMetadata;
import cz.possoft.explorer.meta.DomainMetadata;
import cz.possoft.explorer.meta.MethodMetadata;
import cz.possoft.explorer.resolver.FieldResolverImpl;
import cz.possoft.explorer.resolver.GetterNamesResolverImpl;
import cz.possoft.explorer.resolver.Resolver;
import cz.possoft.explorer.resolver.method.MethodKey;
import cz.possoft.explorer.resolver.method.MethodResolver;
import cz.possoft.explorer.resolver.method.MethodResolverImpl;

/**
 * @author <a href="mailto:mposolda@gmail.com">Marek Posolda</a>
 * @version $Revision$
 */
public class WebExplorerTest extends TestCase {

    private static final String classPrefix = "GetternamesResolverTest";

    protected void setUp() throws Exception {
        System.out.println(classPrefix + ".setUp");
    }

    protected void tearDown() throws Exception {
        System.out.println(classPrefix + ".tearDown");
    }

    public void testGetterNamesResolver() {
        System.out.println(classPrefix + ".testGetterNamesResolver");
        Resolver<MockClass, String> resolver = new GetterNamesResolverImpl<MockClass>();
        Map<String, Class<?>> fields = resolver.getFields(MockClass.class, null);
        assertEquals(fields.size(), 2);
        assertTrue(fields.containsKey("fieldA"));
        assertFalse(fields.containsKey("fieldB"));
        assertTrue(fields.containsKey("fieldC"));
        assertFalse(fields.containsKey("class"));
        MockClass mock = new MockClass();
        mock.setFieldA("Str");
        assertEquals(resolver.getFieldValue(mock, "fieldA"), "Str");
        assertNull(resolver.getFieldValue(mock, "fieldC"));
    }

    public void testFieldsResolver() {
        System.out.println(classPrefix + ".testFieldsResolver");
        Resolver<MockClass, String> resolver = new FieldResolverImpl<MockClass>();
        Map<String, Class<?>> fields = resolver.getFields(MockClass.class, null);
        assertEquals(fields.size(), 4);
        assertTrue(fields.containsKey("fieldA"));
        assertTrue(fields.containsKey("fieldB"));
        assertTrue(fields.containsKey("fieldC"));
        assertTrue(fields.containsKey("fieldD"));
        assertFalse(fields.containsKey("class"));
        MockClass mock = new MockClass();
        mock.setFieldA("Str");
        assertEquals(resolver.getFieldValue(mock, "fieldA"), "Str");
        assertNull(resolver.getFieldValue(mock, "fieldC"));
        assertEquals(resolver.getFieldValue(mock, "fieldD"), 10);
        assertEquals(resolver.getFieldModifier(mock, "fieldA").toString(), "private");
        assertEquals(resolver.getFieldModifier(mock, "fieldB").toString(), "private");
        assertEquals(resolver.getFieldModifier(mock, "fieldC").toString(), "default");
        assertEquals(resolver.getFieldModifier(mock, "fieldD").toString(), "protected");
    }

    public void testDomainMetadata() {
        System.out.println(classPrefix + ".testDomainMetadata");
        DomainMetadata domainMetadata = ServiceFactory.getInstance().getDomainMetadata();
        assertEquals(domainMetadata.getAllConfiguredClasses().size(), 7);
        assertTrue(domainMetadata.getAllConfiguredClasses().contains(MockClass.class));
        assertTrue(domainMetadata.getAllConfiguredClasses().contains(MockInterface.class));
        assertTrue(domainMetadata.getAllConfiguredClasses().contains(Object.class));
        ClassMetadata<MockClass, String> mockClassMetadata = domainMetadata.getClassMetadata(MockClass.class);
        ClassMetadata<MockInterface, String> mockInterfaceMetadata = domainMetadata.getClassMetadata(MockInterface.class);
        assertNotNull(mockClassMetadata.getFields(null).get("fieldA"));
        assertNull(mockClassMetadata.getFields(null).get("fieldB"));
        assertNotNull(mockClassMetadata.getFields(null).get("fieldC"));
        assertNull(mockClassMetadata.getFields(null).get("class"));
        assertNull(mockInterfaceMetadata.getFields(null).get("fieldA"));
        assertNull(mockInterfaceMetadata.getFields(null).get("fieldB"));
        assertNull(mockInterfaceMetadata.getFields(null).get("fieldC"));
    }

    public void testObjectContextBuilder() {
        System.out.println(classPrefix + ".testObjectContextBuilder");
        ObjectContextBuilder ocBuilder = ServiceFactory.getInstance().getObjectContextBuilder();
        MockClass mock = new MockClass();
        mock.setFieldA("Str");
        List<ObjectContext<? super MockClass, String>> result = ocBuilder.build(mock);
        assertEquals(result.size(), 4);
        ObjectContext<?, String> oc1 = result.get(0);
        ObjectContext<?, String> oc2 = result.get(1);
        ObjectContext<?, String> oc3 = result.get(2);
        ObjectContext<?, String> oc4 = result.get(3);
        assertEquals(oc1.getClazz(), MockClass.class);
        assertEquals(oc1.getValue(), mock);
        assertEquals(oc1.getFieldContextList().size(), 3);
        assertEquals(oc1.getFieldContextList().get(1).getFieldName(), "fieldC");
        assertEquals(oc1.getFieldContextList().get(1).getValue(), null);
        assertEquals(oc1.getFieldContextList().get(1).isInspectable(), false);
        assertEquals(oc1.getFieldContextList().get(1).getFieldModifierEnum(), FieldModifierEnum.DEFAULT);
        assertEquals(oc2.getClazz(), MockInterface.class);
        assertEquals(oc2.getValue(), mock);
        assertEquals(oc2.getFieldContextList().size(), 0);
        assertEquals(oc3.getClazz(), MockSuperClass.class);
        assertEquals(oc3.getFieldContextList().get(0).getFieldName(), "fieldB");
        assertEquals(oc3.getFieldContextList().get(0).getValue(), null);
        assertEquals(oc3.getFieldContextList().get(0).isInspectable(), false);
        assertEquals(oc3.getFieldContextList().get(1).getFieldName(), "fieldE");
        assertEquals(oc3.getFieldContextList().get(1).getValue(), "kokos");
        assertEquals(oc3.getFieldContextList().get(1).isInspectable(), false);
        assertEquals(oc3.getFieldContextList().get(1).getFieldModifierEnum(), FieldModifierEnum.PRIVATE);
        assertEquals(oc4.getClazz(), Object.class);
        assertEquals(oc4.getValue(), mock);
        assertEquals(oc4.getFieldContextList().size(), 0);
    }

    public void testConverter() {
        System.out.println(classPrefix + ".testConverter");
        Converter stringConverter = ServiceFactory.getInstance().getConverter();
        try {
            assertEquals(Integer.valueOf(25), stringConverter.convert("25", Integer.class));
            assertEquals("350", stringConverter.convert("350", String.class));
            assertEquals("25", stringConverter.convert(25, String.class));
            assertEquals("kkolo", stringConverter.convert("kkolo", String.class));
        } catch (ConverterException ce) {
            throw new AssertionError(ce);
        }
        try {
            Float l = stringConverter.convert("25", Float.class);
            fail("ConverterException was not thrown when trying to convert object of class " + l.getClass() + ". Converted object is " + l);
        } catch (ConverterException ignore) {
        }
    }

    public void testConverterOutputTypesFromString() {
        System.out.println(classPrefix + ".testConverterOutputTypesFromString");
        Converter stringConverter = ServiceFactory.getInstance().getConverter();
        Collection<Class<?>> outputStringConverterClasses = stringConverter.getStringConverterTypes();
        assertTrue(outputStringConverterClasses.contains(String.class));
        assertTrue(outputStringConverterClasses.contains(Integer.class));
        assertTrue(outputStringConverterClasses.contains(int.class));
        assertTrue(outputStringConverterClasses.contains(long.class));
        assertTrue(outputStringConverterClasses.contains(Double.class));
        assertTrue(outputStringConverterClasses.contains(ObjectName.class));
        assertFalse(outputStringConverterClasses.contains(Float.class));
    }

    public void testReplacingConverterProvider() {
        System.out.println(classPrefix + ".testReplacingConverterProvider");
        Converter stringConverter = ServiceFactory.getInstance().getConverter();
        assertFalse(stringConverter.getStringConverterTypes().contains(JLabel.class));
        ServiceFactory.getInstance().setConverterProvider(new MockConverterProviderImpl());
        assertTrue(stringConverter.getStringConverterTypes().contains(JLabel.class));
    }

    public void testArrayResolver() {
        System.out.println(classPrefix + ".testArrayResolver");
        String[] stringArray = { "aaa", "bbb", "ccc" };
        ClassMetadata classMetadata = ServiceFactory.getInstance().getDomainMetadata().getClassMetadata(stringArray.getClass());
        Resolver arrayResolver = classMetadata.getResolver();
        Map<?, Class<?>> metadataMap = classMetadata.getFields(stringArray);
        assertTrue(metadataMap.containsKey(0));
        assertTrue(metadataMap.containsKey(1));
        assertTrue(metadataMap.containsKey(2));
        assertFalse(metadataMap.containsKey(3));
        assertEquals(arrayResolver.getFieldValue(stringArray, 0), "aaa");
        assertEquals(arrayResolver.getFieldValue(stringArray, 1), "bbb");
        assertEquals(arrayResolver.getFieldValue(stringArray, 2), "ccc");
        assertEquals(arrayResolver.getFieldValue(stringArray, 0), "aaa");
    }

    public void testMethodResolverAndMetadata() {
        System.out.println(classPrefix + ".testMethodResolverAndMetadata");
        DomainMetadata domainMetadata = ServiceFactory.getInstance().getDomainMetadata();
        ClassMetadata<MockClass, String> mockClassMetadata = domainMetadata.getClassMetadata(MockClass.class);
        ClassMetadata<MockInterface, String> mockInterfaceMetadata = domainMetadata.getClassMetadata(MockInterface.class);
        MethodResolver<MockClass, MethodKey> mockResolver = mockClassMetadata.getMethodResolver();
        MethodResolver<MockInterface, MethodKey> mockIntResolver = mockInterfaceMetadata.getMethodResolver();
        mockResolver.getClass().equals(MethodResolverImpl.class);
        mockIntResolver.getClass().equals(MethodResolverImpl.class);
        Map<MethodKey, MethodMetadata> methods = mockClassMetadata.getMethods(null);
        assertEquals(methods.keySet().size(), 8);
        MethodKey key1 = new MethodKey("someMethod", new Class<?>[] { Integer.class });
        MethodKey key2 = new MethodKey("someMethod", new Class<?>[] { Integer.class, Integer.class });
        MethodKey key3 = new MethodKey("getFieldC", new Class<?>[] { Integer.class });
        MethodKey key4 = new MethodKey("getFieldC", new Class<?>[] {});
        assertNotNull(methods.get(key1));
        assertNotNull(methods.get(key2));
        assertNotNull(methods.get(key3));
        assertNotNull(methods.get(key4));
        Map<MethodKey, MethodMetadata> intMethods = mockIntResolver.getMethods(MockInterface.class, null);
        assertEquals(intMethods.keySet().size(), 3);
        assertNotNull(intMethods.get(key1));
        assertNotNull(intMethods.get(key2));
        assertNull(intMethods.get(key3));
        assertNull(intMethods.get(key4));
        MockClass mock = new MockClass();
        try {
            assertEquals(mockResolver.invokeMethod(mock, key1, 10), 10);
            assertEquals(mockResolver.invokeMethod(mock, key2, 10, 5), 15);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown " + e.getMessage());
        }
    }
}
