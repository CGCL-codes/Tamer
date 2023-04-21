package com.google.code.gtkjfilechooser;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Locale;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.code.gtkjfilechooser.GettextResource;
import sun.security.action.GetPropertyAction;

public class GettextResourceTest {

    @BeforeClass
    public static void beforeClass() {
        Locale.setDefault(Locale.ITALIAN);
    }

    @AfterClass
    public static void resetLocale() {
        String language = AccessController.doPrivileged(new GetPropertyAction("user.language"));
        String country = AccessController.doPrivileged(new GetPropertyAction("user.country"));
        Locale.setDefault(new Locale(language, country));
    }

    GettextResource r0;

    GettextResource r1;

    public GettextResourceTest() throws IOException {
        r0 = new GettextResource(new File("misc/mo_file/gtk+.gtk-2-10.it.mo"));
        r1 = new GettextResource(new File("misc/mo_file/gtk+.gtk-2-10.it.mo"));
    }

    @Test
    public void testMoReaderGetKeys() throws Exception {
        GettextResource r = new GettextResource(new File("misc/mo_file/default.mo"));
        Enumeration<String> en = r.getKeys();
        while (en.hasMoreElements()) {
            String msgid = en.nextElement();
            String msgstr = r._(msgid);
            System.out.println("msgid \"" + msgid + "\"");
            System.out.println("msgstr \"" + msgstr + "\"");
            System.out.println();
        }
    }

    @Test
    public void testMoReaderDefault() throws Exception {
        GettextResource r = new GettextResource(new File("misc/mo_file/default.mo"));
        assertEquals("Yes, the look-up worked.  Congratulations!!", r._("hello"));
        assertEquals("This is just here to provide\nsome other strings so that the lookup\nis not totally trivial.", r._("misc_1"));
        assertEquals("Another misc.", r._("misc_2"));
        assertEquals("Bye!", r._("misc_3"));
    }

    @Test
    public void testMoReaderGtk20() throws Exception {
        GettextResource r = new GettextResource(Locale.ITALIAN, "gtk20");
        assertEquals("Impossibile recuperare informazioni sul file", r._("Could not retrieve information about the file"));
        assertEquals("Usati di recente", r._("Recently Used"));
    }

    @Test
    public void testMoReaderGtk0() throws Exception {
        assertEquals("Nome del file non valido: %s", r1._("Invalid filename: %s"));
        assertEquals("Invio dati", r1._("print operation status|Sending data"));
    }

    /**
	 * The the performance gain with the cache. The excution of this test method should be shorter than {@link #testMoReaderGtk0()}
	 * @throws Exception
	 */
    @Test
    public void testMoReaderGtk1() throws Exception {
        assertEquals("Nome del file non valido: %s", r1._("Invalid filename: %s"));
        assertEquals("Invio dati", r1._("print operation status|Sending data"));
    }

    @Test
    public void testMoReaderLocale() throws Exception {
        GettextResource r = new GettextResource(Locale.ITALIAN, "nautilus");
        assertEquals("ieri", r._("yesterday"));
    }

    @Test
    public void testMoReaderGtk20Properties() throws Exception {
        GettextResource r = new GettextResource(Locale.ITALIAN, "gtk20-properties");
        assertEquals("La direzione verso cui punta la freccia", r._("The direction the arrow should point"));
    }

    @Test
    public void testSwap() throws Exception {
        int i = swap(881);
        System.out.println(i);
        assertEquals(1896022016, i);
    }

    @Test
    public void testSwapb() throws Exception {
        int i = swapb(0x00, 0x00, 0x00, 0x89);
        System.out.println("swapb: " + i);
    }

    /**
	 * Byte swap a single int value.
	 * 
	 * @param value
	 *            Value to byte swap.
	 * @return Byte swapped representation.
	 */
    int swap(int value) {
        int b1 = (value >> 0) & 0xff;
        int b2 = (value >> 8) & 0xff;
        int b3 = (value >> 16) & 0xff;
        int b4 = (value >> 24) & 0xff;
        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    int swapb(int b1, int b2, int b3, int b4) {
        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    @Test
    public void byteArrayToInt0() {
        int v = 136;
        byte[] b = toByteArray(v);
        for (byte c : b) {
            System.out.print(Integer.toHexString(c & 0xff) + " ");
        }
        System.out.println();
    }

    @Test
    public void byteArrayToInt1() {
        int v = 28;
        byte[] b = toByteArray(v);
        for (byte c : b) {
            System.out.print(Integer.toHexString(c & 0xff) + " ");
        }
        System.out.println();
    }

    private byte[] toByteArray(int v) {
        byte b[] = new byte[4];
        int i, shift;
        for (i = 0, shift = 24; i < 4; i++, shift -= 8) b[i] = (byte) (0xFF & (v >> shift));
        return b;
    }
}
