package org.apache.harmony.luni.tests.java.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import tests.support.Support_StringReader;
import tests.support.Support_StringWriter;

public class PrintWriterTest extends junit.framework.TestCase {

    static class Bogus {

        public String toString() {
            return "Bogus";
        }
    }

    /**
     * @since 1.6
     */
    static class MockPrintWriter extends PrintWriter {

        public MockPrintWriter(OutputStream out, boolean autoflush) {
            super(out, autoflush);
        }

        @Override
        public void clearError() {
            super.clearError();
        }
    }

    PrintWriter pw;

    ByteArrayOutputStream bao;

    ByteArrayInputStream bai;

    BufferedReader br;

    /**
	 * @tests java.io.PrintWriter#PrintWriter(java.io.OutputStream)
	 */
    public void test_ConstructorLjava_io_OutputStream() {
        String s;
        pw.println("Random Chars");
        pw.write("Hello World");
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            assertTrue("Incorrect string written/read: " + s, s.equals("Random Chars"));
            s = br.readLine();
            assertTrue("Incorrect string written/read: " + s, s.equals("Hello World"));
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
    }

    /**
	 * @tests java.io.PrintWriter#PrintWriter(java.io.OutputStream, boolean)
	 */
    public void test_ConstructorLjava_io_OutputStreamZ() {
        String s;
        pw = new PrintWriter(bao, true);
        pw.println("Random Chars");
        pw.write("Hello World");
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            assertTrue("Incorrect string written/read: " + s, s.equals("Random Chars"));
            pw.flush();
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            assertTrue("Incorrect string written/read: " + s, s.equals("Random Chars"));
            s = br.readLine();
            assertTrue("Incorrect string written/read: " + s, s.equals("Hello World"));
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
    }

    /**
	 * @tests java.io.PrintWriter#PrintWriter(java.io.Writer)
	 */
    public void test_ConstructorLjava_io_Writer() {
        Support_StringWriter sw;
        pw = new PrintWriter(sw = new Support_StringWriter());
        pw.print("Hello");
        pw.flush();
        assertEquals("Failed to construct proper writer", "Hello", sw.toString());
    }

    /**
	 * @tests java.io.PrintWriter#PrintWriter(java.io.Writer, boolean)
	 */
    public void test_ConstructorLjava_io_WriterZ() {
        Support_StringWriter sw;
        pw = new PrintWriter(sw = new Support_StringWriter(), true);
        pw.print("Hello");
        assertEquals("Failed to construct proper writer", "Hello", sw.toString());
    }

    /**
     * @tests java.io.PrintWriter#PrintWriter(java.io.File)
     */
    public void test_ConstructorLjava_io_File() throws Exception {
        File file = File.createTempFile(getClass().getName(), null);
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        } finally {
            file.delete();
        }
    }

    /**
     * @tests java.io.PrintWriter#PrintWriter(java.io.File, java.lang.String)
     */
    public void test_ConstructorLjava_io_File_Ljava_lang_String() throws Exception {
        File file = File.createTempFile(getClass().getName(), null);
        try {
            PrintWriter writer = new PrintWriter(file, Charset.defaultCharset().name());
            writer.close();
        } finally {
            file.delete();
        }
    }

    /**
     * @tests java.io.PrintWriter#PrintWriter(java.lang.String)
     */
    public void test_ConstructorLjava_lang_String() throws Exception {
        File file = File.createTempFile(getClass().getName(), null);
        try {
            PrintWriter writer = new PrintWriter(file.getPath());
            writer.close();
        } finally {
            file.delete();
        }
    }

    /**
     * @tests java.io.PrintWriter#PrintWriter(java.lang.String, java.lang.String)
     */
    public void test_ConstructorLjava_lang_String_Ljava_lang_String() throws Exception {
        File file = File.createTempFile(getClass().getName(), null);
        try {
            PrintWriter writer = new PrintWriter(file.getPath(), Charset.defaultCharset().name());
            writer.close();
        } finally {
            file.delete();
        }
    }

    /**
	 * @tests java.io.PrintWriter#checkError()
	 */
    public void test_checkError() {
        pw.close();
        pw.print(490000000000.08765);
        assertTrue("Failed to return error", pw.checkError());
    }

    /**
     * @tests java.io.PrintWriter#clearError()
     * @since 1.6
     */
    public void test_clearError() {
        MockPrintWriter mpw = new MockPrintWriter(new ByteArrayOutputStream(), false);
        mpw.close();
        mpw.print(490000000000.08765);
        assertTrue("Failed to return error", mpw.checkError());
        mpw.clearError();
        assertFalse("Internal error state has not be cleared", mpw.checkError());
    }

    /**
	 * @tests java.io.PrintWriter#close()
	 */
    public void test_close() {
        pw.close();
        pw.println("l");
        assertTrue("Write on closed stream failed to generate error", pw.checkError());
    }

    /**
	 * @tests java.io.PrintWriter#flush()
	 */
    public void test_flush() {
        final double dub = 490000000000.08765;
        pw.print(dub);
        pw.flush();
        assertTrue("Failed to flush", new String(bao.toByteArray()).equals(String.valueOf(dub)));
    }

    /**
	 * @tests java.io.PrintWriter#print(char[])
	 */
    public void test_print$C() {
        String s = null;
        char[] schars = new char[11];
        "Hello World".getChars(0, 11, schars, 0);
        pw.print(schars);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("Hello World"));
        int r = 0;
        try {
            pw.print((char[]) null);
        } catch (NullPointerException e) {
            r = 1;
        }
        assertEquals("null pointer exception for printing null char[] is not caught", 1, r);
    }

    /**
	 * @tests java.io.PrintWriter#print(char)
	 */
    public void test_printC() {
        pw.print('c');
        pw.flush();
        assertEquals("Wrote incorrect char string", "c", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#print(double)
	 */
    public void test_printD() {
        final double dub = 490000000000.08765;
        pw.print(dub);
        pw.flush();
        assertTrue("Wrote incorrect double string", new String(bao.toByteArray()).equals(String.valueOf(dub)));
    }

    /**
	 * @tests java.io.PrintWriter#print(float)
	 */
    public void test_printF() {
        final float flo = 49.08765f;
        pw.print(flo);
        pw.flush();
        assertTrue("Wrote incorrect float string", new String(bao.toByteArray()).equals(String.valueOf(flo)));
    }

    /**
	 * @tests java.io.PrintWriter#print(int)
	 */
    public void test_printI() {
        pw.print(4908765);
        pw.flush();
        assertEquals("Wrote incorrect int string", "4908765", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#print(long)
	 */
    public void test_printJ() {
        pw.print(49087650000L);
        pw.flush();
        assertEquals("Wrote incorrect long string", "49087650000", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#print(java.lang.Object)
	 */
    public void test_printLjava_lang_Object() {
        pw.print((Object) null);
        pw.flush();
        assertEquals("Did not write null", "null", new String(bao.toByteArray()));
        bao.reset();
        pw.print(new Bogus());
        pw.flush();
        assertEquals("Wrote in incorrect Object string", "Bogus", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#print(java.lang.String)
	 */
    public void test_printLjava_lang_String() {
        pw.print((String) null);
        pw.flush();
        assertEquals("did not write null", "null", new String(bao.toByteArray()));
        bao.reset();
        pw.print("Hello World");
        pw.flush();
        assertEquals("Wrote incorrect  string", "Hello World", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#print(boolean)
	 */
    public void test_printZ() {
        pw.print(true);
        pw.flush();
        assertEquals("Wrote in incorrect boolean string", "true", new String(bao.toByteArray()));
    }

    /**
	 * @tests java.io.PrintWriter#println()
	 */
    public void test_println() {
        String s;
        pw.println("Blarg");
        pw.println();
        pw.println("Bleep");
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            assertTrue("Wrote incorrect line: " + s, s.equals("Blarg"));
            s = br.readLine();
            assertTrue("Wrote incorrect line: " + s, s.equals(""));
            s = br.readLine();
            assertTrue("Wrote incorrect line: " + s, s.equals("Bleep"));
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
    }

    /**
	 * @tests java.io.PrintWriter#println(char[])
	 */
    public void test_println$C() {
        String s = null;
        char[] schars = new char[11];
        "Hello World".getChars(0, 11, schars, 0);
        pw.println("Random Chars");
        pw.println(schars);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("Hello World"));
    }

    /**
	 * @tests java.io.PrintWriter#println(char)
	 */
    public void test_printlnC() {
        String s = null;
        pw.println("Random Chars");
        pw.println('c');
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            s = br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char string: " + s, s.equals("c"));
    }

    /**
	 * @tests java.io.PrintWriter#println(double)
	 */
    public void test_printlnD() {
        String s = null;
        final double dub = 4000000000000000.657483;
        pw.println("Random Chars");
        pw.println(dub);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect double string: " + s, s.equals(String.valueOf(dub)));
    }

    /**
	 * @tests java.io.PrintWriter#println(float)
	 */
    public void test_printlnF() {
        String s;
        final float flo = 40.4646464f;
        pw.println("Random Chars");
        pw.println(flo);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
            assertTrue("Wrote incorrect float string: " + s + " wanted: " + String.valueOf(flo), s.equals(String.valueOf(flo)));
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
    }

    /**
	 * @tests java.io.PrintWriter#println(int)
	 */
    public void test_printlnI() {
        String s = null;
        pw.println("Random Chars");
        pw.println(400000);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect int string: " + s, s.equals("400000"));
    }

    /**
	 * @tests java.io.PrintWriter#println(long)
	 */
    public void test_printlnJ() {
        String s = null;
        pw.println("Random Chars");
        pw.println(4000000000000L);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect long string: " + s, s.equals("4000000000000"));
    }

    /**
	 * @tests java.io.PrintWriter#println(java.lang.Object)
	 */
    public void test_printlnLjava_lang_Object() {
        String s = null;
        pw.println("Random Chars");
        pw.println(new Bogus());
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect Object string: " + s, s.equals("Bogus"));
    }

    /**
	 * @tests java.io.PrintWriter#println(java.lang.String)
	 */
    public void test_printlnLjava_lang_String() {
        String s = null;
        pw.println("Random Chars");
        pw.println("Hello World");
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect string: " + s, s.equals("Hello World"));
    }

    /**
	 * @tests java.io.PrintWriter#println(boolean)
	 */
    public void test_printlnZ() {
        String s = null;
        pw.println("Random Chars");
        pw.println(false);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect boolean string: " + s, s.equals("false"));
    }

    /**
	 * @tests java.io.PrintWriter#write(char[])
	 */
    public void test_write$C() {
        String s = null;
        char[] schars = new char[11];
        "Hello World".getChars(0, 11, schars, 0);
        pw.println("Random Chars");
        pw.write(schars);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test: " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("Hello World"));
    }

    /**
	 * @tests java.io.PrintWriter#write(char[], int, int)
	 */
    public void test_write$CII() {
        String s = null;
        char[] schars = new char[11];
        "Hello World".getChars(0, 11, schars, 0);
        pw.println("Random Chars");
        pw.write(schars, 6, 5);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("World"));
    }

    /**
	 * @tests java.io.PrintWriter#write(int)
	 */
    public void test_writeI() throws IOException {
        char[] cab = new char[3];
        pw.write('a');
        pw.write('b');
        pw.write('c');
        pw.flush();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(bao.toByteArray()));
        cab[0] = (char) isr.read();
        cab[1] = (char) isr.read();
        cab[2] = (char) isr.read();
        assertTrue("Wrote incorrect ints", cab[0] == 'a' && cab[1] == 'b' && cab[2] == 'c');
    }

    /**
	 * @tests java.io.PrintWriter#write(java.lang.String)
	 */
    public void test_writeLjava_lang_String() {
        String s = null;
        pw.println("Random Chars");
        pw.write("Hello World");
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("Hello World"));
    }

    /**
	 * @tests java.io.PrintWriter#write(java.lang.String, int, int)
	 */
    public void test_writeLjava_lang_StringII() {
        String s = null;
        pw.println("Random Chars");
        pw.write("Hello World", 6, 5);
        pw.flush();
        try {
            br = new BufferedReader(new Support_StringReader(bao.toString()));
            br.readLine();
            s = br.readLine();
        } catch (IOException e) {
            fail("IOException during test : " + e.getMessage());
        }
        assertTrue("Wrote incorrect char[] string: " + s, s.equals("World"));
    }

    /**
	 * @tests java.io.PrintWriter#append(char)
	 */
    public void test_appendChar() {
        char testChar = ' ';
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(out);
        printWriter.append(testChar);
        printWriter.flush();
        assertEquals(String.valueOf(testChar), out.toString());
        printWriter.close();
    }

    /**
	 * @tests java.io.PrintWriter#append(CharSequence)
	 */
    public void test_appendCharSequence() {
        String testString = "My Test String";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(out);
        printWriter.append(testString);
        printWriter.flush();
        assertEquals(testString, out.toString());
        printWriter.close();
    }

    /**
	 *  @tests java.io.PrintWriter#append(CharSequence, int, int)
	 */
    public void test_appendCharSequenceIntInt() {
        String testString = "My Test String";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(out);
        printWriter.append(testString, 1, 3);
        printWriter.flush();
        assertEquals(testString.substring(1, 3), out.toString());
        printWriter.close();
    }

    /**
     * @tests java.io.PrintWriter#format(java.lang.String, java.lang.Object...)
     */
    public void test_formatLjava_lang_String$Ljava_lang_Object() {
        pw.format("%s %s", "Hello", "World");
        pw.flush();
        assertEquals("Wrote incorrect string", "Hello World", new String(bao.toByteArray()));
    }

    /**
     * @tests java.io.PrintWriter#format(java.util.Locale, java.lang.String, java.lang.Object...)
     */
    public void test_formatLjava_util_Locale_Ljava_lang_String_$Ljava_lang_Object() {
        pw.format(Locale.US, "%s %s", "Hello", "World");
        pw.flush();
        assertEquals("Wrote incorrect string", "Hello World", new String(bao.toByteArray()));
    }

    /**
     * @tests java.io.PrintWriter#printf(java.lang.String, java.lang.Object...)
     */
    public void test_printfLjava_lang_String$Ljava_lang_Object() {
        pw.printf("%s %s", "Hello", "World");
        pw.flush();
        assertEquals("Wrote incorrect string", "Hello World", new String(bao.toByteArray()));
    }

    /**
     * @tests java.io.PrintWriter#printf(java.util.Locale, java.lang.String, java.lang.Object...)
     */
    public void test_printfLjava_util_Locale_Ljava_lang_String_$Ljava_lang_Object() {
        pw.printf(Locale.US, "%s %s", "Hello", "World");
        pw.flush();
        assertEquals("Wrote incorrect string", "Hello World", new String(bao.toByteArray()));
    }

    /**
	 * Sets up the fixture, for example, open a network connection. This method
	 * is called before a test is executed.
	 */
    protected void setUp() {
        bao = new ByteArrayOutputStream();
        pw = new PrintWriter(bao, false);
    }

    /**
	 * Tears down the fixture, for example, close a network connection. This
	 * method is called after a test is executed.
	 */
    protected void tearDown() {
        try {
            pw.close();
        } catch (Exception e) {
        }
    }
}
