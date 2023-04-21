package tests.support;

import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.Format;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;

public class Support_Format extends TestCase {

    protected String text;

    public Support_Format(String p1) {
        super(p1);
    }

    protected void t_FormatWithField(int count, Format format, Object object, String text, Format.Field field, int begin, int end) {
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(field);
        format.format(object, buffer, pos);
        if (text == null) {
            assertEquals("Test " + count + ": incorrect formatted text", this.text, buffer.toString());
        } else {
            assertEquals("Test " + count + ": incorrect formatted text", text, buffer.toString());
        }
        assertEquals("Test " + count + ": incorrect begin index for field " + field, begin, pos.getBeginIndex());
        assertEquals("Test " + count + ": incorrect end index for field" + field, end, pos.getEndIndex());
    }

    protected void t_Format(int count, Object object, Format format, Vector<FieldContainer> expectedResults) {
        Vector<FieldContainer> results = findFields(format.formatToCharacterIterator(object));
        assertTrue("Test " + count + ": Format returned incorrect CharacterIterator for " + format.format(object), compare(results, expectedResults));
    }

    /**
     * compares two vectors regardless of the order of their elements
     */
    protected static boolean compare(Vector<FieldContainer> vector1, Vector<FieldContainer> vector2) {
        return vector1.size() == vector2.size() && vector1.containsAll(vector2);
    }

    /**
     * finds attributes with regards to char index in this
     * AttributedCharacterIterator, and puts them in a vector
     * 
     * @param iterator
     * @return a vector, each entry in this vector are of type FieldContainer ,
     *         which stores start and end indexes and an attribute this range
     *         has
     */
    protected static Vector<FieldContainer> findFields(AttributedCharacterIterator iterator) {
        Vector<FieldContainer> result = new Vector<FieldContainer>();
        while (iterator.getIndex() != iterator.getEndIndex()) {
            int start = iterator.getRunStart();
            int end = iterator.getRunLimit();
            Iterator<Attribute> it = iterator.getAttributes().keySet().iterator();
            while (it.hasNext()) {
                AttributedCharacterIterator.Attribute attribute = it.next();
                Object value = iterator.getAttribute(attribute);
                result.add(new FieldContainer(start, end, attribute, value));
            }
            iterator.setIndex(end);
        }
        return result;
    }

    protected static class FieldContainer {

        int start, end;

        AttributedCharacterIterator.Attribute attribute;

        Object value;

        public FieldContainer(int start, int end, AttributedCharacterIterator.Attribute attribute) {
            this(start, end, attribute, attribute);
        }

        public FieldContainer(int start, int end, Attribute attribute, int value) {
            this(start, end, attribute, new Integer(value));
        }

        public FieldContainer(int start, int end, Attribute attribute, Object value) {
            this.start = start;
            this.end = end;
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FieldContainer)) {
                return false;
            }
            FieldContainer fc = (FieldContainer) obj;
            return (start == fc.start && end == fc.end && attribute == fc.attribute && value.equals(fc.value));
        }
    }
}
