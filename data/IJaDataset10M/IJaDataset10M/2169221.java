package org.openexi.fujitsu.scomp;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.openexi.fujitsu.schema.EXISchema;
import org.openexi.fujitsu.schema.SchemaValidatorException;
import org.openexi.fujitsu.schema.SimpleTypeValidator;

public class LongValidatorTest extends TestCase {

    public LongValidatorTest(String name) {
        super(name);
    }

    /**
   * Test long value space.
   */
    public void testLongValueSpace() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/decimalRange.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int longType = corpus.getTypeOfNamespace(xsdns, "long");
        validator.validate("9223372036854775807", longType);
        validator.validate("-9223372036854775808", longType);
        try {
            caught = false;
            validator.validate("123.0", longType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_INTEGER, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("9223372036854775808", longType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MAX_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("-9223372036854775809", longType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MIN_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
   * Test int value space.
   */
    public void testIntValueSpace() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/decimalRange.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int intType = corpus.getTypeOfNamespace(xsdns, "int");
        validator.validate("2147483647", intType);
        validator.validate("-2147483648", intType);
        try {
            caught = false;
            validator.validate("123.0", intType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_INTEGER, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("2147483648", intType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MAX_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("-2147483649", intType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MIN_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
   * Test short value space.
   */
    public void testShortValueSpace() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/decimalRange.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int shortType = corpus.getTypeOfNamespace(xsdns, "short");
        validator.validate("32767", shortType);
        validator.validate("-32768", shortType);
        try {
            caught = false;
            validator.validate("123.0", shortType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_INTEGER, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("32768", shortType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MAX_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("-32769", shortType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MIN_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
   * Test byte value space.
   */
    public void testByteValueSpace() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/decimalRange.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int byteType = corpus.getTypeOfNamespace(xsdns, "byte");
        validator.validate("127", byteType);
        validator.validate("-128", byteType);
        try {
            caught = false;
            validator.validate("12.0", byteType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_INTEGER, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("128", byteType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MAX_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("-129", byteType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.MIN_INCLUSIVE_INVALID, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }
}
