package net.sf.eBus.messages.type;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;
import junit.framework.TestCase;

/**
 * JUnit test for text-based serialization.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class ESerializeTest extends TestCase {

    public ESerializeTest(String name) {
        super(name);
    }

    public void testSerializeBooleanNull() {
        DataType type = null;
        Boolean before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Boolean.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("null type", type);
        assertEquals("incorrect position", 0, buffer.position());
        return;
    }

    public void testDeserializeBooleanEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Boolean after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Boolean.class);
            buffer.clear();
            buffer.flip();
            after = (Boolean) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeBooleanFalse() {
        DataType type = null;
        Boolean before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Boolean after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Boolean.class);
            before = Boolean.FALSE;
            type.serialize(before, buffer);
            buffer.flip();
            after = (Boolean) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeBooleanTrue() {
        DataType type = null;
        Boolean before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Boolean after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Boolean.class);
            before = Boolean.TRUE;
            type.serialize(before, buffer);
            buffer.flip();
            after = (Boolean) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeByteNull() {
        DataType type = null;
        Byte before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Byte.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeByteEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Byte after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Byte.class);
            buffer.clear();
            buffer.flip();
            after = (Byte) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeByte() {
        DataType type = null;
        Byte before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Byte after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Byte.class);
            before = new Byte((byte) 127);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Byte) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeCharNull() {
        DataType type = null;
        Character before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Character.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeCharEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Character after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Character.class);
            buffer.clear();
            buffer.flip();
            after = (Character) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeChar() {
        DataType type = null;
        Character before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Character after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Character.class);
            before = new Character('m');
            type.serialize(before, buffer);
            buffer.flip();
            after = (Character) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeDateNull() {
        DataType type = null;
        Date before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Date.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeDateEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Date after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Date.class);
            buffer.clear();
            buffer.flip();
            after = (Date) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeDate() {
        Calendar calendar = Calendar.getInstance();
        DataType type = null;
        Date before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Date after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Date.class);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            calendar.set(Calendar.YEAR, 2005);
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 28);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            before = calendar.getTime();
            type.serialize(before, buffer);
            buffer.flip();
            after = (Date) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeDoubleNull() {
        DataType type = null;
        Double before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Double.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeDoubleInvalid() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Double after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Double.class);
            buffer.clear();
            buffer.flip();
            after = (Double) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeDouble() {
        DataType type = null;
        Double before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Double after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Double.class);
            before = new Double(3.14159);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Double) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeFloatNull() {
        DataType type = null;
        Float before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Float.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeFloatEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Float after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Float.class);
            buffer.clear();
            buffer.flip();
            after = (Float) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeFloat() {
        DataType type = null;
        Float before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Float after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Float.class);
            before = new Float((float) 3.14159);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Float) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeIntegerNull() {
        DataType type = null;
        Integer before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Integer.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeIntegerEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Integer after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Integer.class);
            buffer.clear();
            buffer.flip();
            after = (Integer) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeInteger() {
        DataType type = null;
        Integer before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Integer after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Integer.class);
            before = new Integer(123456);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Integer) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeLongNull() {
        DataType type = null;
        Long before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Long.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeLongEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Long after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Long.class);
            buffer.clear();
            buffer.flip();
            after = (Long) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeLong() {
        DataType type = null;
        Long before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Long after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Long.class);
            before = new Long((long) 0x7fffffff);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Long) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeShortNull() {
        DataType type = null;
        Short before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(Short.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeShortEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Short after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Short.class);
            buffer.clear();
            buffer.flip();
            after = (Short) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeShort() {
        DataType type = null;
        Short before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Short after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(Short.class);
            before = new Short((short) 0x7fff);
            type.serialize(before, buffer);
            buffer.flip();
            after = (Short) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeStringNull() {
        DataType type = null;
        String before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(String.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && buffer.position() == 0);
        return;
    }

    public void testDeserializeStringEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        String after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(String.class);
            buffer.clear();
            buffer.flip();
            after = (String) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeEmptyString() {
        DataType type = null;
        String before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        String after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(String.class);
            before = "";
            type.serialize(before, buffer);
            buffer.flip();
            after = (String) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeString() {
        DataType type = null;
        String before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        String after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(String.class);
            before = "\\ab,c,\\de\nf\n";
            type.serialize(before, buffer);
            buffer.flip();
            after = (String) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeEnumNull() {
        DataType type = null;
        DayOfWeek before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(DayOfWeek.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && type instanceof EnumType && buffer.position() == 0);
        return;
    }

    public void testDeserializeEnumEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        DayOfWeek after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(DayOfWeek.class);
            buffer.clear();
            buffer.flip();
            after = (DayOfWeek) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && after == null);
        return;
    }

    public void testSerializeEnum() {
        DataType type = null;
        DayOfWeek before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        DayOfWeek after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(DayOfWeek.class);
            before = DayOfWeek.TUESDAY;
            type.serialize(before, buffer);
            buffer.flip();
            after = (DayOfWeek) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("null type", type);
        assertNotNull("null after", after);
        assertEquals("wrong enum", before, after);
        return;
    }

    public void testSerializeGenericNull() {
        DataType type = null;
        TestClass before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Exception caughtex = null;
        try {
            type = DataType.findType(TestClass.class);
            type.serialize(before, buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && type instanceof GenericType && buffer.position() == 0);
        return;
    }

    public void testDeserializeGenericEmptyBuffer() {
        DataType type = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        TestClass after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(TestClass.class);
            buffer.clear();
            buffer.flip();
            after = (TestClass) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex != null && caughtex instanceof BufferUnderflowException && type != null && type instanceof GenericType && after == null);
        return;
    }

    public void testSerializeGeneric() {
        DataType type = null;
        TestClass before = null;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        TestClass after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(TestClass.class);
            before = new TestClass(12345);
            type.serialize(before, buffer);
            buffer.flip();
            after = (TestClass) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertTrue(caughtex == null && type != null && type instanceof GenericType && after != null && before.equals(after) == true);
        return;
    }

    public void testSerializeArray() {
        DataType type = null;
        int[] before = { 0, 1, 1, 2, 3, 5 };
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int[] after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(before.getClass());
            type.serialize(before, buffer);
            buffer.flip();
            after = (int[]) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("type null", type);
        assertNotNull("null after", after);
        assertTrue("before != after", Arrays.equals(before, after));
        return;
    }

    public void testSerializeGenericArray() {
        DataType type = null;
        TestClass[] before = new TestClass[] { new TestClass(1), new TestClass(2), new TestClass(3) };
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        TestClass[] after = null;
        Exception caughtex = null;
        try {
            type = DataType.findType(before.getClass());
            type.serialize(before, buffer);
            buffer.flip();
            after = (TestClass[]) type.deserialize(buffer);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("type null", type);
        assertNotNull("null after", after);
        assertTrue("before != after", Arrays.equals(before, after));
        return;
    }

    private enum DayOfWeek {

        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    private static final int BUFFER_SIZE = 512;
}
