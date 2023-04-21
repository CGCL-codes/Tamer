package ch.sahits.codegen.java.model.util;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.sahits.codegen.util.ERunAsType;
import ch.sahits.codegen.util.RunAs;
import ch.sahits.model.ModelBuilderFactory;
import ch.sahits.model.db.IDatabaseTableField;
import ch.sahits.model.db.IDatabaseTableFieldBuilder;

@RunAs(ERunAsType.JUNIT)
public class ConvenientCodegenTest {

    private IDatabaseTableField intField;

    private IDatabaseTableField dateField;

    private IDatabaseTableField stringField;

    private IDatabaseTableField thisField;

    @Before
    public void setUp() throws Exception {
        IDatabaseTableFieldBuilder builder = (IDatabaseTableFieldBuilder) ModelBuilderFactory.newBuilder(IDatabaseTableField.class);
        builder.fieldName("some").fieldType(Integer.class).nullAllowed(false).isAutoGenKey(false);
        intField = builder.build();
        builder = (IDatabaseTableFieldBuilder) ModelBuilderFactory.newBuilder(IDatabaseTableField.class);
        builder.fieldName("some").fieldType(java.sql.Date.class).nullAllowed(false).isAutoGenKey(false);
        dateField = builder.build();
        builder = (IDatabaseTableFieldBuilder) ModelBuilderFactory.newBuilder(IDatabaseTableField.class);
        builder.fieldName("some").fieldType(String.class).nullAllowed(false).isAutoGenKey(false);
        stringField = builder.build();
        builder = (IDatabaseTableFieldBuilder) ModelBuilderFactory.newBuilder(IDatabaseTableField.class);
        builder.fieldName("some").fieldType(this.getClass()).nullAllowed(false).isAutoGenKey(false);
        thisField = builder.build();
    }

    @Test
    public void testToPackageDefinition() {
        String expected = "";
        Assert.assertEquals(expected, ConvenientCodegen.toPackageDefinition(null));
        Assert.assertEquals(expected, ConvenientCodegen.toPackageDefinition(""));
        expected = "package ch.sahits;\n\n";
        Assert.assertEquals(expected, ConvenientCodegen.toPackageDefinition("ch.sahits"));
    }

    @Test
    public void testGetSimpleClassNameClass() {
        String expected = "String";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName(java.lang.String.class));
        expected = "Double";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName(Double.class));
        expected = "int";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName(int.class));
        expected = "Map.Entry";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName(java.util.Map.Entry.class));
    }

    @Test
    public void testGetSimpleClassNameString() {
        String expected = "String";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName("java.lang.String"));
        expected = "int";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName("int"));
        expected = "Entry";
        Assert.assertEquals(expected, ConvenientCodegen.getSimpleClassName("java.util.Map.Entry"));
    }

    @Test
    public void testToName() {
        String expected = "sahitsCodeGen";
        String actual = ConvenientCodegen.toName("SAHITS_CODE_GEN", "_");
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, ConvenientCodegen.toName("Sahits_code_geN", "_"));
        Assert.assertEquals(expected, ConvenientCodegen.toName("SAHITS CODE GEN", " "));
    }

    @Test
    public void testIsPrimitiveClassString() {
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("void"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.String"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Integer"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Short"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Long"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Character"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Boolean"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Float"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Double"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Byte"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Class"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Enum"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Object"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.Thread"));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass("java.lang.System"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass("java.lang.annotation.ElementType"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass("java.lang.instrument.ClassDefinition"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass("java.lang.management.MemoryUsage"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass("java.lang.ref.Reference"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass("java.lang.reflect.Method"));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(getClass().getName()));
    }

    @Test
    public void testIsPrimitiveClassClass() {
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(void.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(int.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(char.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(float.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(double.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(short.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(long.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(boolean.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(byte.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.String.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Integer.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Short.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Long.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Character.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Boolean.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Float.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Double.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Byte.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Class.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Enum.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Object.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.Thread.class));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveClass(java.lang.System.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(java.lang.annotation.ElementType.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(java.lang.instrument.ClassDefinition.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(java.lang.management.MemoryUsage.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(java.lang.ref.Reference.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(java.lang.reflect.Method.class));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveClass(getClass()));
    }

    @Test
    public void testCamelCase() {
        String expected = "SahitsCodeGen";
        Assert.assertEquals(expected, ConvenientCodegen.camelCase("SAHITS_CODE_GEN", "_"));
        Assert.assertEquals(expected, ConvenientCodegen.camelCase("Sahits_code_geN", "_"));
        Assert.assertEquals(expected, ConvenientCodegen.camelCase("SAHITS CODE GEN", " "));
    }

    @Test
    public void testCamelCaseLowerCaseFirst() {
        String expected = "allUpperCase";
        Assert.assertEquals(expected, ConvenientCodegen.camelCaseLowerCaseFirst("ALL_UPPER_CASE", "_"));
    }

    @Test
    public void testToLowerCaseFirst() {
        String expected = "aLL_UPPER_CASE";
        Assert.assertEquals(expected, ConvenientCodegen.toLowerCaseFirst("ALL_UPPER_CASE"));
    }

    @Test
    public void testToUpperCaseFirst() {
        String expected = "ALL_UPPER_CASE";
        Assert.assertEquals(expected, ConvenientCodegen.toUpperCaseFirst("aLL_UPPER_CASE"));
        expected = "All_lower_case";
        Assert.assertEquals(expected, ConvenientCodegen.toUpperCaseFirst("all_lower_case"));
    }

    @Test
    public void testGetClassShortName() {
        String expected = "Integer";
        Assert.assertEquals(expected, ConvenientCodegen.getClassShortName(intField));
        expected = "String";
        Assert.assertEquals(expected, ConvenientCodegen.getClassShortName(stringField));
        expected = "Date";
        Assert.assertEquals(expected, ConvenientCodegen.getClassShortName(dateField));
        expected = "ConvenientCodegenTest";
        Assert.assertEquals(expected, ConvenientCodegen.getClassShortName(thisField));
    }

    @Test
    public void testIsPrimitiveType() {
        Assert.assertTrue(ConvenientCodegen.isPrimitiveType(intField));
        Assert.assertTrue(ConvenientCodegen.isPrimitiveType(stringField));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveType(dateField));
        Assert.assertFalse(ConvenientCodegen.isPrimitiveType(thisField));
    }

    @Test
    public void testIsBoxingType() {
        Assert.assertFalse(ConvenientCodegen.isBoxingType(stringField.getFieldType()));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(dateField.getFieldType()));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(thisField.getFieldType()));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(int.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(char.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(float.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(double.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(short.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(long.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(boolean.class));
        Assert.assertTrue(ConvenientCodegen.isBoxingType(byte.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Integer.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Short.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Long.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Character.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Boolean.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Float.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Double.class));
        Assert.assertFalse(ConvenientCodegen.isBoxingType(java.lang.Byte.class));
    }

    @Test
    public void testGetBoxingClassShortName() {
        Assert.assertNull(ConvenientCodegen.getBoxingClassShortName(void.class));
        Assert.assertNull(ConvenientCodegen.getBoxingClassShortName(String.class));
        String expected = "Integer";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(int.class));
        expected = "Short";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(short.class));
        expected = "Long";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(long.class));
        expected = "Byte";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(byte.class));
        expected = "Boolean";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(boolean.class));
        expected = "Character";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(char.class));
        expected = "Float";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(float.class));
        expected = "Double";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassShortName(double.class));
    }

    @Test
    public void testGetBoxingClassParsingMethodName() {
        Assert.assertNull(ConvenientCodegen.getBoxingClassParsingMethodName(void.class));
        Assert.assertNull(ConvenientCodegen.getBoxingClassParsingMethodName(String.class));
        String expected = "parseInt";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(int.class));
        expected = "parseShort";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(short.class));
        expected = "parseLong";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(long.class));
        expected = "parseByte";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(byte.class));
        expected = "getBoolean";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(boolean.class));
        expected = "Character";
        Assert.assertNull(ConvenientCodegen.getBoxingClassParsingMethodName(char.class));
        expected = "parseFloat";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(float.class));
        expected = "parseDouble";
        Assert.assertEquals(expected, ConvenientCodegen.getBoxingClassParsingMethodName(double.class));
    }
}
