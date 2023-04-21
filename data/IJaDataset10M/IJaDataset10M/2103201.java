package net.sf.kerner.commons.io.lazy;

import static org.junit.Assert.assertEquals;
import java.io.FileOutputStream;
import java.io.IOException;
import net.sf.kerner.commons.TUtils;
import net.sf.kerner.commons.io.lazy.LazyStringWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Alexander Kerner
 * 
 */
public class TestLazyStringWriter {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
	 * 
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#LazyStringWriter(Object)}.
	 */
    @Test
    public final void testLazyStringWriterString() {
        new LazyStringWriter("test");
    }

    /**
	 * 
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#LazyStringWriter(Object)}.
	 */
    @Test(expected = NullPointerException.class)
    public final void testLazyStringWriterString01() {
        new LazyStringWriter(null);
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#write(java.io.Writer)}.
	 * 
	 * @throws IOException
	 */
    @Test
    public final void testWriteWriter() throws IOException {
        final java.io.StringWriter wr = new java.io.StringWriter();
        new LazyStringWriter("test").write(wr);
        assertEquals("test", wr.toString());
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#write(java.io.File)}.
	 * 
	 * @throws IOException
	 */
    @Test(expected = IOException.class)
    public final void testWriteFile() throws IOException {
        new LazyStringWriter("test").write(TUtils.INACCESSIBLE_FILE);
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#write(java.io.OutputStream)}
	 * .
	 * 
	 * @throws IOException
	 */
    @Test(expected = IOException.class)
    public final void testWriteOutputStream() throws IOException {
        new LazyStringWriter("test").write(new FileOutputStream(TUtils.INACCESSIBLE_FILE));
    }

    /**
	 * Test method for
	 * {@link net.sf.kerner.commons.io.lazy.LazyStringWriter#write(java.io.File)}.
	 * 
	 * @throws IOException
	 */
    @Test
    public final void example() throws IOException {
        final java.io.StringWriter wr = new java.io.StringWriter();
        new LazyStringWriter("Hallo Welt!").write(wr);
        assertEquals("Hallo Welt!", wr.toString());
    }
}
