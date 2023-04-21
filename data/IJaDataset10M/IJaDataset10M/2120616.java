package ejp.profiler.parser;

import java.io.File;
import java.text.ParseException;
import junit.framework.TestCase;
import ejp.presenter.parser.EjpFile;

public class EjpFileTest extends TestCase {

    public EjpFileTest(String arg0_) {
        super(arg0_);
    }

    public void testIncorrectSuffix() {
        boolean failed = false;
        try {
            new EjpFile(new DummyFile("00000000_Classloaderejp", 0));
        } catch (ParseException pe_) {
            failed = true;
        }
        assertTrue("Failed", failed);
    }

    public void testMissingSeparator() {
        boolean failed = false;
        try {
            new EjpFile(new DummyFile("00000000Classloader.ejp", 0));
        } catch (ParseException pe_) {
            failed = true;
        }
        assertTrue("Failed", failed);
    }

    public void testMissingTimestamp() {
        boolean failed = false;
        try {
            new EjpFile(new DummyFile("_00000000Classloader.ejp", 0));
        } catch (ParseException pe_) {
            failed = true;
        }
        assertTrue("Failed", failed);
    }

    public void testMissingName() {
        boolean failed = false;
        try {
            new EjpFile(new DummyFile("00000000Classloader_.ejp", 0));
        } catch (ParseException pe_) {
            failed = true;
        }
        assertTrue("Failed", failed);
    }

    public void testClassloaderFile() throws ParseException {
        EjpFile ef = new EjpFile(new DummyFile("00000000_Classloader.ejp", 1272701L));
        assertEquals("Parsed timestamp", "00000000", ef.getTimestamp());
        assertTrue("Is classloader", ef.isClassloader());
        assertEquals("Parsed name", "Classloader", ef.getName());
    }

    public void testThread1File() throws ParseException {
        EjpFile ef = new EjpFile(new DummyFile("00000000_Thread0003-main.ejp", 202388920L));
        assertEquals("Parsed timestamp", "00000000", ef.getTimestamp());
        assertFalse("Is not classloader", ef.isClassloader());
        assertEquals("Parsed name", "Thread0003-main", ef.getName());
    }

    private class DummyFile extends File {

        private final long m_length;

        public DummyFile(String pathname_, long length_) {
            super(pathname_);
            m_length = length_;
        }

        public long length() {
            return m_length;
        }
    }
}
