package de.bioutils.plaintext;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * description // TODO
 * </p>
 * 
 * <p>
 * example // TODO
 * </p>
 * 
 * @author Alexander Kerner
 * @lastVisit 2010-07-29
 * 
 */
@SuppressWarnings("serial")
public class PlainTextDataTableFileReaderTest {

    private static final File input = new File("resources/plainTextDataTableFileImplTest.txt");

    private static final File input02 = new File("resources/plainTextDataTableFileImplTest02.txt");

    private static final File input03 = new File("resources/plainTextDataTableFileImplTest03.txt");

    private static final File input04 = new File("resources/plainTextDataTableFileImplTest.csv");

    private static final File input05 = new File("resources/plainTextDataTableFileImplTest02.csv");

    private static final Set<String> ids = new LinkedHashSet<String>() {

        {
            add("1");
            add("2");
            add("3");
        }
    };

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertArrayEquals(ids.toArray(), file.getColumnIdentifiers().toArray());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader01() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertEquals("1-1", file.getRow(0).getAtIndex("1"));
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader02() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertEquals("1-2", file.getRow(1).getAtIndex("1"));
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader03() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertEquals("2-2", file.getRow(1).getAtIndex("2"));
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader04() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertTrue(file.isColumnConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader05() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input);
        assertTrue(file.isRowConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader06() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input02);
        assertTrue(file.isRowConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader07() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input02);
        assertFalse(file.isColumnConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader08() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input03);
        assertTrue(file.isColumnConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader09() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input03);
        assertTrue(file.isRowConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader10() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input04);
        assertTrue(file.isRowConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader11() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input04);
        assertTrue(file.isColumnConsistent());
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader12() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input02);
        assertArrayEquals(file.getRow(0).asList().toArray(), new String[] { "1-1", "2-1", "3-1", " " });
    }

    /**
	 * Test method for {@link de.bioutils.plaintext.PlainTextDataTableFileReader#read(java.io.Reader)}.
	 * @throws IOException 
	 */
    @Test
    public final void testReadReader13() throws IOException {
        final PlainTextDataTableFileReader reader = new PlainTextDataTableFileReader(true, "\t");
        final PlainTextDataTableFile file = reader.read(input05);
        file.setColumnIdentifiers(ColumnIdentifier.asStringSet());
        assertTrue(file.getColumnIdentifiers().contains(ColumnIdentifier.CLONE_SEQUENCE));
    }
}
