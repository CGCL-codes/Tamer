package org.curjent.example.agent.ftpxfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPFile;
import org.curjent.example.agent.factory.Agents;
import org.curjent.example.agent.logger.Logger;

/**
 * FTP transfer test driver.
 * <p>
 * The test driver recursively traverses an FTP directory tree, delegating
 * concurrent file transfers to an FTPTransfer agent.
 */
public class FTPTransferTest {

    public static void main(String[] args) throws Exception {
        final int TASKS = 4;
        final String HOST = "ftp.osuosl.org";
        final String TMPDIR = new File(System.getProperties().getProperty("java.io.tmpdir"), "org.curjent.example.agent.ftpxfer").getCanonicalPath();
        FTPTransferClient ftp = new FTPTransferClient(HOST, TMPDIR);
        Logger logger = Logger.INSTANCE;
        logger.log(ftp.connect());
        FTPTransfer xfer = FTPTransferFactory.INSTANCE.newInstance(HOST, TMPDIR, TASKS);
        List<String> dirs = new ArrayList<String>();
        dirs.add("/pub/apache/commons/net");
        long start = System.currentTimeMillis();
        while (dirs.size() > 0) {
            String dir = dirs.remove(dirs.size() - 1);
            xfer.starting(dir);
            FTPFile[] files = ftp.list(dir);
            for (FTPFile file : files) {
                String path = dir + "/" + file.getName();
                if (file.isDirectory()) {
                    dirs.add(path);
                } else {
                    xfer.transfer(path);
                }
            }
            xfer.finished(dir);
        }
        ftp.disconnect();
        Agents.await(xfer);
        logger.log("Elapsed time (ms): " + (System.currentTimeMillis() - start));
        Agents.await(logger);
        System.exit(0);
    }
}
