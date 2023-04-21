package org.curjent.example.agent.ftpxfer;

import org.curjent.example.agent.factory.Agents;

/**
 * FTP transfer factory.
 * <p>
 * The agent's factory accepts three arguments. The first is the remote FTP
 * host. The test driver uses ftp.osuosl.org. The second factory argument is a
 * local file system directory. Files are downloaded from the host and saved to
 * the local directory. The test driver uses the Java runtime's temp directory.
 * The last argument is the maximum number of current tasks to simultaneously
 * download files.
 */
public interface FTPTransferFactory {

    FTPTransfer newInstance(String remote, String local, int tasks);

    static final FTPTransferFactory INSTANCE = new FTPTransferFactory() {

        @Override
        public FTPTransfer newInstance(String remote, String local, int tasks) {
            return Agents.newInstance(FTPTransfer.class, new FTPTransferTasks(remote, local, tasks));
        }
    };
}
