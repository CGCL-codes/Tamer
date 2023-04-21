package marcin.downloader.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import marcin.downloader.model.exceptions.BadLinkException;
import marcin.downloader.model.exceptions.FileLinkReadException;
import marcin.downloader.model.exceptions.LinkReadException;
import marcin.downloader.model.exceptions.LinkReaderException;
import marcin.downloader.model.utils.Link;
import org.jmock.Expectations;

public class TestFileLinkReader extends Test {

    FileLinkReader lr;

    List<Link> lTest;

    static final String LINKREADER_TESTFILES = "fileLinkReader";

    static final String GOOD = "links.lnk";

    static final String BAD = "2linksBad.lnk";

    static final String NOFILE = "fileNotFound.lnk";

    static final String EMPTYLINES = "linksEmptyLn.lnk";

    @Override
    public void setUp() {
        lr = new FileLinkReader();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @org.junit.Test
    public void testReadAllLinks() throws Exception {
        lTest = readTestData(GOOD);
        try {
            assertTrue(checkEquality(read(GOOD), lTest));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
	 * Bad links must be silently skipped
	 * @throws Exception
	 */
    @org.junit.Test
    public void testBadLinkException() throws Exception {
        lTest = readTestData(BAD);
        Link l;
        int badCounter = 0;
        int goodCounter = 0;
        lr.init(new FileReader(getTestFile(LINKREADER_TESTFILES, BAD)));
        List<Link> ls = new ArrayList<Link>();
        while (true) {
            try {
                l = lr.nextLink();
                if (l == null) break;
                ls.add(l);
                goodCounter++;
            } catch (FileLinkReadException e) {
                badCounter++;
                continue;
            }
        }
        assertEquals(6, goodCounter);
        assertEquals(1, badCounter);
        assertTrue(checkEquality(ls, lTest));
    }

    @org.junit.Test
    public void testIOException() throws Exception {
        try {
            read(NOFILE);
            assertFalse(true);
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @org.junit.Test
    public void testEmptyLines() throws Exception {
        lTest = readTestData(EMPTYLINES);
        try {
            assertTrue(checkEquality(read(EMPTYLINES), lTest));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    private List<Link> readTestData(String testFile) throws IOException {
        FileReader fr = new FileReader(getTestFile(LINKREADER_TESTFILES, testFile));
        BufferedReader br = new BufferedReader(fr);
        String line;
        Link link;
        List<Link> links = new ArrayList<Link>();
        while (true) {
            line = br.readLine();
            if (line == null) break;
            if ("".equals(line)) continue;
            try {
                link = new Link(line);
                links.add(link);
            } catch (BadLinkException e) {
                continue;
            }
        }
        return links;
    }

    private List<Link> read() throws LinkReadException, LinkReaderException {
        List<Link> list = new LinkedList<Link>();
        Link l;
        while (true) {
            l = lr.nextLink();
            if (l == null) break;
            list.add(l);
        }
        return list;
    }

    private List<Link> read(String file) throws LinkReadException, IOException, LinkReaderException {
        lr.init(getTestFile(LINKREADER_TESTFILES, file));
        return read();
    }

    private boolean checkEquality(List<Link> l1, List<Link> l2) {
        return l1.equals(l2);
    }
}
