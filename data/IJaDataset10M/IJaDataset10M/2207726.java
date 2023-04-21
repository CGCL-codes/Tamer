package org.jl7.test;

import junit.framework.TestCase;
import org.jl7.hl7.HL7Field;
import org.jl7.hl7.HL7Message;
import org.jl7.hl7.HL7Segment;

/**
 * @author henribenoit
 * 
 */
public class TestHL7Segment extends TestCase {

    private HL7Segment segment;

    protected void setUp() throws Exception {
        super.setUp();
        segment = new HL7Segment();
        segment.setDelimiters("|^~\\&");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#getDelimiters()}.
	 */
    public void testGetDelimiters() {
        String delimiters = segment.getDelimiters();
        assertEquals("|^~\\&", delimiters);
    }

    /**
	 * Test method for
	 * {@link org.jl7.hl7.HL7Segment#setDelimiters(java.lang.String)}.
	 */
    public void testSetDelimiters() {
        assertEquals(segment.escapeCharacter, '\\');
        assertEquals(segment.subcomponentSeparator, '&');
        assertEquals(segment.componentSeparator, '^');
        assertEquals(segment.repetitionSeparator, '~');
    }

    /**
	 * Test method for
	 * {@link org.jl7.hl7.HL7Segment#setFields(java.lang.String[], java.lang.String, boolean)}
	 * .
	 */
    public void testSetFieldsStringArrayStringBoolean() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for
	 * {@link org.jl7.hl7.HL7Segment#setFields(org.jl7.hl7.HL7Field[])}.
	 */
    public void testSetFieldsHL7FieldArray() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#getFields()}.
	 */
    public void testGetFields() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for
	 * {@link org.jl7.hl7.HL7Segment#setFields(java.util.ArrayList)}.
	 */
    public void testSetFieldsArrayListOfHL7Field() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#get(int)}.
	 */
    public void testGet() {
        String[] fields = { "abc" + HL7Message.segmentTerminator, "\\|~^&def" };
        segment.setFields(fields, "|^~\\&", false);
        HL7Field field = segment.get(0);
        assertEquals("abc" + HL7Message.segmentTerminator, field.getValue());
        field = segment.get(1);
        assertEquals("\\|~^&def", field.getValue());
        field = segment.get(2);
        assertNull(field);
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#getCount()}.
	 */
    public void testGetCount() {
        assertEquals(0, segment.getCount());
        String[] fields = { "abc\\" + HL7Message.segmentTerminator, "\\E\\\\F\\\\R\\\\S\\\\T\\def" };
        segment.setFields(fields, "|^~\\&", true);
        assertEquals(2, segment.getCount());
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#getSegmentType()}.
	 */
    public void testGetSegmentType() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#getEnumerator()}.
	 */
    public void testGetEnumerator() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link org.jl7.hl7.HL7Segment#toString()}.
	 */
    public void testToString() {
        String[] fields = { "abc\\" + HL7Message.segmentTerminator, "\\E\\\\F\\\\R\\\\S\\\\T\\def" };
        segment.setFields(fields, "|^~\\&", true);
        String value = segment.toString();
        assertEquals("abc\\" + HL7Message.segmentTerminator + "|\\E\\\\F\\\\R\\\\S\\\\T\\def", value);
    }
}
