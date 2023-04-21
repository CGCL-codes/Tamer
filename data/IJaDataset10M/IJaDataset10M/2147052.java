package jcfs.core;

import java.io.File;
import jcfs.core.fs.RFile;
import jcfs.core.fs.RFileOutputStream;
import java.util.List;
import jcfs.core.serverside.JCFSFileServer;
import jcfs.core.client.JCFS;
import jcfs.core.fs.RSearchAnswer;
import jcfs.core.fs.RServerInfo;
import jcfs.core.fs.WriteMode;
import jcfs.core.serverside.DirectoryConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * test async distribution of files
 * @author enrico
 */
public class DistributionTest extends StorageDirBased {

    @Test
    public void testDoubleReplica() throws Exception {
        JCFSFileServer server1 = new JCFSFileServer(defaultTcpPort, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir, 0, 1);
        server1.defineDirectoryDefault(new DirectoryConfiguration("mydir", WriteMode.BESTEFFORT, 1, false, true, 2));
        JCFSFileServer server2 = new JCFSFileServer(defaultTcpPort - 1, defaultTcpAddress, defaultUdpPort, defaultUdpAddress, dir2, 0, 0);
        try {
            server1.start();
            server2.start();
            server1.forcePeerDiscovery();
            server2.forcePeerDiscovery();
            assertEquals("server1 did not see other peer", 1, server1.getPeers().size());
            assertEquals("server2 did not see other peer", 1, server2.getPeers().size());
            JCFS.configureDiscovery(defaultUdpAddress, defaultUdpPort);
            JCFS.getInstance().setDiscoverServers(false);
            JCFS.getInstance().addServer(server1.getLocalServerInfo());
            RFile file = new RFile("mydir/testdist.txt");
            RFileOutputStream out = new RFileOutputStream(file, WriteMode.BESTEFFORT, false, 1);
            out.write("test".getBytes());
            out.close();
            assertTrue("file does not really exists", new File(new File(dir, "mydir"), "testdist.txt").isFile());
            Thread.sleep(5000);
            JCFS.getInstance().setDiscoverServers(true);
            List<RServerInfo> servers = JCFS.getInstance().discoverServers();
            assertEquals("some server not anwered to broadcast discovery", 2, servers.size());
            List<RSearchAnswer> answers = JCFS.getInstance().searchFile(file);
            assertEquals("some server not anwered to search query", 2, answers.size());
            for (RSearchAnswer a : answers) {
                assertTrue("on server " + a.getServer() + " file was not found", a.isFileFound());
            }
        } finally {
            dumpStackTrace();
            server1.stop();
            server2.stop();
        }
    }
}
