package jcfs.core;

import jcfs.core.client.JCFS;
import jcfs.core.fs.RFile;
import jcfs.core.fs.RFileInputStream;
import jcfs.core.fs.RFileOutputStream;
import jcfs.core.serverside.JCFSFileServer;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectoriesTest extends StorageDirBased {

    @Test
    public void testParseAbsolutePathTriple() throws Exception {
        RFile file = new RFile("a/b/c");
        assertNotNull(file.getParentFile());
        assertNotNull(file.getParentFile().getParentFile());
        assertEquals("a/b", file.getParentFile().getAbsolutePath());
        assertEquals("a", file.getParentFile().getParentFile().getAbsolutePath());
    }

    @Test
    public void testParseAbsolutePathDouble() throws Exception {
        RFile file = new RFile("a/b");
        assertNotNull(file.getParentFile());
        assertEquals("a", file.getParentFile().getAbsolutePath());
    }

    @Test
    public void testParseAbsolutePathSingle() throws Exception {
        RFile file = new RFile("a");
        assertNull(file.getParentFile());
        assertEquals("a", file.getAbsolutePath());
    }

    @Test
    public void testParseAbsolutePathTripleWithParent() throws Exception {
        RFile file = new RFile(new RFile("a"), "b/c");
        assertNotNull(file.getParentFile());
        assertNotNull(file.getParentFile().getParentFile());
        assertEquals("a/b", file.getParentFile().getAbsolutePath());
        assertEquals("a", file.getParentFile().getParentFile().getAbsolutePath());
    }

    @Test
    public void testWriteAndReadFirstLevel() throws Exception {
        JCFSFileServer server = new JCFSFileServer(defaultTcpPort, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir, 0, 0);
        JCFS.configureDiscovery(defaultUdpAddress, defaultUdpPort);
        try {
            server.start();
            RFile directory1 = new RFile("directory1");
            RFile file = new RFile(directory1, "testreadwrite1st.txt");
            RFileOutputStream out = new RFileOutputStream(file);
            out.write("test".getBytes("utf-8"));
            out.close();
            RFileInputStream in = new RFileInputStream(file);
            byte[] buffer = new byte[4];
            int readCount = in.read(buffer);
            in.close();
            assertEquals(4, readCount);
            String resultRead = new String(buffer, "utf-8");
            assertEquals("test", resultRead);
        } finally {
            server.stop();
        }
    }

    @Test
    public void testAbsolutePath() throws Exception {
        RFile directory1 = new RFile("a");
        RFile directory2 = new RFile(directory1, "b");
        RFile file = new RFile(directory2, "c");
        assertEquals("a/b/c", file.getAbsolutePath());
    }

    @Test
    public void testWriteAndReadSecondLevel() throws Exception {
        JCFSFileServer server = new JCFSFileServer(defaultTcpPort, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir, 0, 0);
        JCFS.configureDiscovery(defaultUdpAddress, defaultUdpPort);
        try {
            server.start();
            RFile directory1 = new RFile("directory1");
            RFile directory2 = new RFile(directory1, "directory2");
            RFile file = new RFile(directory2, "testreadwrite2nd.txt");
            RFileOutputStream out = new RFileOutputStream(file);
            out.write("test".getBytes("utf-8"));
            out.close();
            RFileInputStream in = new RFileInputStream(file);
            byte[] buffer = new byte[4];
            int readCount = in.read(buffer);
            in.close();
            assertEquals(4, readCount);
            String resultRead = new String(buffer, "utf-8");
            assertEquals("test", resultRead);
        } finally {
            server.stop();
        }
    }
}
