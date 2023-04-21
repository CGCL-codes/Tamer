package org.openexi.fujitsu.scomp;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.openexi.fujitsu.schema.EXISchema;
import org.openexi.fujitsu.schema.SchemaValidatorException;
import org.openexi.fujitsu.schema.SimpleTypeValidator;

public class DateTimeValidatorTest extends TestCase {

    public DateTimeValidatorTest(String name) {
        super(name);
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new RuntimeException(dce);
        } finally {
            m_datatypeFactory = datatypeFactory;
        }
    }

    private final DatatypeFactory m_datatypeFactory;

    /**
  * Test dateTime syntax parser.
  */
    public void testDateTimeSyntax() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/simpleType.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int dateTime = corpus.getTypeOfNamespace(xsdns, "dateTime");
        Assert.assertEquals("2003-04-25T11:41:30.45+09:00", validator.validate("   2003-04-25T11:41:30.45+09:00  ", dateTime));
        validator.validate("2003-04-25T11:41:30.45+14:00", dateTime);
        validator.validate("1997-07-16T19:20:30.45-12:00", dateTime);
        validator.validate("-0601-07-16T19:20:30.45-05:00", dateTime);
        validator.validate("1997-07-16T19:20:30.45Z", dateTime);
        validator.validate("1999-12-31T24:00:00", dateTime);
        try {
            caught = false;
            validator.validate("", dateTime);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATETIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("-601-07-16T19:20:30.45-05:00", dateTime);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATETIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("1997-07-16T19:20:30.45J", dateTime);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATETIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("1999-09-16T24:01:00", dateTime);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATETIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
  * Test dateTime minInclusive facet.
  */
    public void testDateTimeMinInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minInclusive = corpus.getTypeOfNamespace(foons, "dateTimeDerived");
        validator.validate("2003-04-25T11:41:30.45+09:00", minInclusive);
        validator.validate("2003-03-19T13:20:00-05:00", minInclusive);
        validator.validate("1997-07-16T19:20:30.45Z", minInclusive);
        validator.validate("2003-03-19T13:20:00", minInclusive);
    }

    /**
  * Test dateTime maxInclusive facet.
  */
    public void testDateTimeMaxInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxInclusive = corpus.getTypeOfNamespace(foons, "dateTimeDerived");
        validator.validate("1995-04-25T11:41:30.45+09:00", maxInclusive);
        validator.validate("2003-03-19T13:20:00-05:00", maxInclusive);
        validator.validate("2003-07-16T19:20:30.45Z", maxInclusive);
        validator.validate("2003-03-19T13:20:00", maxInclusive);
    }

    /**
  * Test dateTime minExclusive facet.
  */
    public void testDateTimeMinExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minExclusive = corpus.getTypeOfNamespace(foons, "dateTimeDerived");
        validator.validate("2003-04-25T11:41:30.45+09:00", minExclusive);
        validator.validate("2003-03-19T13:20:00-05:00", minExclusive);
        validator.validate("2003-03-19T13:20:00", minExclusive);
    }

    /**
  * Test dateTime maxExclusive facet.
  */
    public void testDateTimeMaxExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxExclusive = corpus.getTypeOfNamespace(foons, "dateTimeDerived");
        validator.validate("2001-04-25T11:41:30.45+09:00", maxExclusive);
        validator.validate("2003-03-19T13:20:00-05:00", maxExclusive);
        validator.validate("2003-03-19T13:20:00", maxExclusive);
    }

    /**
  * Test dateTime maxInclusive facet with value.
  */
    public void testDateTimeMaxInclusiveWithValue() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxInclusive = corpus.getTypeOfNamespace(foons, "dateTimeDerived");
        XMLGregorianCalendar dateTimeValue;
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("1995-04-25T11:41:30.45+09:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, maxInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-03-19T13:20:00-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, maxInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-07-16T19:20:30.45Z");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, maxInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-03-19T13:20:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, maxInclusive);
    }

    /**
  * Test date syntax parser.
  */
    public void testDateSyntax() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/simpleType.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int dateType = corpus.getTypeOfNamespace(xsdns, "date");
        Assert.assertEquals("2003-04-25+09:00", validator.validate("   2003-04-25+09:00  ", dateType));
        validator.validate("-0601-07-16-05:00", dateType);
        validator.validate("1997-07-16", dateType);
        validator.validate("1997-07-16Z", dateType);
        try {
            caught = false;
            validator.validate("", dateType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATE, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("1997-07T19:20:30", dateType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_DATE, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
  * Test date minInclusive facet.
  */
    public void testDateMinInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minInclusive = corpus.getTypeOfNamespace(foons, "dateDerived");
        validator.validate("2003-04-25", minInclusive);
        validator.validate("2003-04-25+09:00", minInclusive);
        validator.validate("2003-03-19-05:00", minInclusive);
        validator.validate("2003-03-18-05:00", minInclusive);
        validator.validate("2003-03-19", minInclusive);
    }

    /**
  * Test date maxInclusive facet.
  */
    public void testDateMaxInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxInclusive = corpus.getTypeOfNamespace(foons, "dateDerived");
        validator.validate("2003-03-15", maxInclusive);
        validator.validate("2003-03-15+09:00", maxInclusive);
        validator.validate("2003-03-19-05:00", maxInclusive);
        validator.validate("2003-03-20-05:00", maxInclusive);
        validator.validate("2003-03-19", maxInclusive);
    }

    /**
  * Test date minExclusive facet.
  */
    public void testDateMinExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minExclusive = corpus.getTypeOfNamespace(foons, "dateDerived");
        validator.validate("2003-03-21", minExclusive);
        validator.validate("2003-03-21+09:00", minExclusive);
        validator.validate("2003-03-19-05:00", minExclusive);
        validator.validate("2003-03-19", minExclusive);
    }

    /**
  * Test date maxExclusive facet.
  */
    public void testDateMaxExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxExclusive = corpus.getTypeOfNamespace(foons, "dateDerived");
        validator.validate("2003-03-17", maxExclusive);
        validator.validate("2003-03-17+09:00", maxExclusive);
        validator.validate("2003-03-19-05:00", maxExclusive);
        validator.validate("2003-03-19", maxExclusive);
    }

    /**
  * Test date minInclusive facet with value.
  */
    public void testDateMinInclusiveWithValue() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minInclusive = corpus.getTypeOfNamespace(foons, "dateDerived");
        XMLGregorianCalendar dateTimeValue;
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-04-25");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-04-25+09:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-03-19-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-03-18-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("2003-03-19");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
    }

    /**
  * Test time syntax parser.
  */
    public void testTimeSyntax() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/simpleType.xsd", getClass());
        int xsdns = corpus.getNamespaceOfSchema("http://www.w3.org/2001/XMLSchema");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        boolean caught = false;
        int timeType = corpus.getTypeOfNamespace(xsdns, "time");
        Assert.assertEquals("13:20:00+09:00", validator.validate("   13:20:00+09:00  ", timeType));
        validator.validate("13:20:00", timeType);
        validator.validate("13:20:00Z", timeType);
        try {
            caught = false;
            validator.validate("", timeType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_TIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
        try {
            caught = false;
            validator.validate("19:", timeType);
        } catch (SchemaValidatorException sve) {
            caught = true;
            Assert.assertEquals(SchemaValidatorException.INVALID_TIME, sve.getCode());
        } finally {
            if (!caught) Assert.fail("The operation should have resulted in an exception.");
        }
    }

    /**
  * Test time minInclusive facet.
  */
    public void testTimeMinInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minInclusive = corpus.getTypeOfNamespace(foons, "timeDerived");
        validator.validate("13:20:01-05:00", minInclusive);
        validator.validate("13:20:00-05:00", minInclusive);
        validator.validate("13:19:00-05:00", minInclusive);
    }

    /**
  * Test time maxInclusive facet.
  */
    public void testTimeMaxInclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxInclusive = corpus.getTypeOfNamespace(foons, "timeDerived");
        validator.validate("13:19:00-05:00", maxInclusive);
        validator.validate("13:20:00-05:00", maxInclusive);
        validator.validate("13:21:00-05:00", maxInclusive);
    }

    /**
  * Test time minExclusive facet.
  */
    public void testTimeMinExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minExclusive = corpus.getTypeOfNamespace(foons, "timeDerived");
        validator.validate("13:20:01-05:00", minExclusive);
        validator.validate("13:20:00-05:00", minExclusive);
    }

    /**
  * Test time maxExclusive facet.
  */
    public void testTimeMaxExclusive() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/maxExclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int maxExclusive = corpus.getTypeOfNamespace(foons, "timeDerived");
        validator.validate("13:19:01-05:00", maxExclusive);
        validator.validate("13:20:00-05:00", maxExclusive);
    }

    /**
  * Test time minInclusive facet with value.
  */
    public void testTimeMinInclusiveWithValue() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/minInclusive.xsd", getClass());
        int foons = corpus.getNamespaceOfSchema("urn:foo");
        SimpleTypeValidator validator = new SimpleTypeValidator(corpus);
        int minInclusive = corpus.getTypeOfNamespace(foons, "timeDerived");
        XMLGregorianCalendar dateTimeValue;
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("13:20:01-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("13:20:00-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
        dateTimeValue = m_datatypeFactory.newXMLGregorianCalendar("13:19:00-05:00");
        validator.validateAtomicValue(dateTimeValue.toString(), dateTimeValue, minInclusive);
    }
}
