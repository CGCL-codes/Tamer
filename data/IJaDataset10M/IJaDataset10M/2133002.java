package javax.print.attribute.standard;

import javax.print.attribute.EnumSyntax;
import junit.framework.TestCase;

public class MediaSizeNameTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MediaSizeNameTest.class);
    }

    static {
        System.out.println("MediaSizeName testing...");
    }

    public final void testMediaName() {
        MediaSizeName msn = new mediaSizeName(0);
        assertEquals(0, msn.getValue());
        assertEquals("iso-a0", msn.toString());
        msn = mediaSizeName.A;
        assertEquals("a", msn.toString());
        msn = mediaSizeName.FOLIO;
        assertEquals("folio", msn.toString());
    }

    public void testGetStringTable() {
        mediaSizeName msn = new mediaSizeName(1);
        String[] str = msn.getStringTableEx();
        EnumSyntax[] table = msn.getEnumValueTableEx();
        assertEquals(str.length, table.length);
        assertEquals(73, str.length);
        msn = new mediaSizeName(1);
        str = msn.getStringTableEx();
        str[1] = "MediaSizeName1";
        assertFalse(msn.getStringTableEx()[1].equals("MediaSizeName1"));
    }

    public class mediaSizeName extends MediaSizeName {

        public mediaSizeName(int value) {
            super(value);
        }

        public String[] getStringTableEx() {
            return getStringTable();
        }

        public EnumSyntax[] getEnumValueTableEx() {
            return getEnumValueTable();
        }
    }
}
