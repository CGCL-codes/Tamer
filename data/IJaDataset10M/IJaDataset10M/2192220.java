package net.sf.maple.data.file.testing;

import java.io.File;
import java.io.IOException;
import net.sf.maple.data.file.Saveable;
import junit.framework.TestCase;

public class Test_SimpleRecord extends TestCase {

    @Override
    public void setUp() throws IOException {
        File f = File.createTempFile("record", ".props");
        f.deleteOnExit();
        r1 = new SimpleRecord();
        r1.setAutoSave(false);
        r1.setFiles(f);
        Saveable.load(r1);
        r2 = new SimpleRecord();
        r2.setAutoSave(false);
        r2.setFiles(f);
    }

    private SimpleRecord r1;

    private SimpleRecord r2;

    public void test1() {
        assertEquals(10, r1.n.intValue());
        assertEquals(10, r2.n.intValue());
    }

    public void test2() throws IOException {
        r1.n = 15;
        r1.save();
        r2.load();
        assertEquals(r1.n, r2.n);
    }

    public void test3() throws IOException {
        r1.n = null;
        r1.save();
        r2.load();
        assertNull(r2.n);
    }

    public static void main(String[] args) throws IOException {
        Test_SimpleRecord ts = new Test_SimpleRecord();
        ts.setUp();
        ts.test2();
    }
}
