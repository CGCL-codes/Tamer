package org.fulworx.core.util;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fulworx.core.util.filesystem.FileSystemAccessor;
import org.fulworx.core.rest.SomeAddrVO;
import org.fulworx.core.rest.SomeVO;
import org.fulworx.core.util.DataAccessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * @author teastlack
 * @date Jan 25, 2008: $
 */
public class TestFileSystem extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestFileSystem.class);

    public void testFileCreate() throws Exception {
        String baseline = "./target/fstest/dir1";
        DataAccessor fs = new FileSystemAccessor(baseline);
        SomeVO someVO = new SomeVO();
        someVO.setFirstname("first");
        someVO.setLastname("last");
        SomeAddrVO someAddrVO = new SomeAddrVO();
        someAddrVO.setStreet1("street1");
        someAddrVO.setStreet2("street2");
        someVO.setAddress(someAddrVO);
        fs.writeObject("/first/last", "entity.dat", someVO);
        File file = new File(baseline + "/first/last/entity.dat");
        assertTrue("File " + file.getAbsolutePath() + " doesn't exist", file.exists());
        assertTrue("File " + file.getAbsolutePath() + " isn't a file", file.isFile());
        Object vo = fs.readObject("/first/last", "entity.dat");
        SomeVO readVo = (SomeVO) vo;
        assertEquals("first", readVo.getFirstname());
        SomeAddrVO readAddr = readVo.getAddress();
        assertEquals("street1", readAddr.getStreet1());
        Object delVO = fs.deleteObject("/first/last", "entity.dat");
        SomeVO delRead = (SomeVO) delVO;
        assertEquals("first", delRead.getFirstname());
        try {
            fs.readObject("/first/last", "entity.dat");
        } catch (Exception e) {
            assertTrue("Not FileNotFound Exception", e instanceof FileNotFoundException);
        }
    }

    public void testFileList() throws Exception {
        String baseline = "./target/fstest/dir2";
        DataAccessor fs = new FileSystemAccessor(baseline);
        SomeVO someVO = new SomeVO();
        someVO.setFirstname("first");
        someVO.setLastname("last");
        SomeAddrVO someAddrVO = new SomeAddrVO();
        someAddrVO.setStreet1("street1");
        someAddrVO.setStreet2("street2");
        someVO.setAddress(someAddrVO);
        fs.writeObject("/first/last", "entity.dat", someVO);
        File file = new File(baseline + "/first/last/entity.dat");
        assertTrue("File " + file.getAbsolutePath() + " doesn't exist", file.exists());
        assertTrue("File " + file.getAbsolutePath() + " isn't a file", file.isFile());
        someVO = new SomeVO();
        someVO.setFirstname("first");
        someVO.setLastname("anotherlast");
        someAddrVO = new SomeAddrVO();
        someAddrVO.setStreet1("street1");
        someAddrVO.setStreet2("street2");
        someVO.setAddress(someAddrVO);
        fs.writeObject("/first/anotherlast", "entity.dat", someVO);
        file = new File(baseline + "/first/anotherlast/entity.dat");
        assertTrue("File " + file.getAbsolutePath() + " doesn't exist", file.exists());
        assertTrue("File " + file.getAbsolutePath() + " isn't a file", file.isFile());
        someVO = new SomeVO();
        someVO.setFirstname("first");
        someVO.setLastname("nolast");
        someAddrVO = new SomeAddrVO();
        someAddrVO.setStreet1("street1");
        someAddrVO.setStreet2("street2");
        someVO.setAddress(someAddrVO);
        fs.writeObject("/first", "entity.dat", someVO);
        file = new File(baseline + "/first/entity.dat");
        assertTrue("File " + file.getAbsolutePath() + " doesn't exist", file.exists());
        assertTrue("File " + file.getAbsolutePath() + " isn't a file", file.isFile());
        String first = "/first/entity.dat";
        Collection<String> uris = fs.listObjects();
        assertEquals(3, uris.size());
        assertTrue("URI " + uris.toArray()[0] + " doesn't equal " + first, first.equals(uris.toArray()[0]));
        for (String uri : uris) {
            LOG.info("URI:" + uri);
        }
    }
}
