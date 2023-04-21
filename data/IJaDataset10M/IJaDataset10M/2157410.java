package ao.dd.shell.def;

import java.io.*;
import java.util.List;

/**
 * User: aostrovsky
 * Date: 22-Jun-2009
 * Time: 12:40:16 PM
 */
public interface TransferAgent extends Closeable {

    void throttle(long maxBytesPerSecond);

    void unThrottle();

    ShellFile file(String remoteFilePath);

    List<ShellFile> files(String inRemoteFilePath);

    boolean makeDir(String remoteDirectoryPath);

    boolean makeDirs(String remoteDirectoryPath);

    boolean upload(String localFileName, String remoteFileName);

    boolean upload(File localFile, String remoteFile);

    /**
     * Does not automatically close the given source stream.
     * 
     * Works only on the immediate host to which this agent logged into.
     * If you would like to transfer data that are one or more network
     *  hops away (i.e. recursive ssh calls), then you will need to
     *  upload to the immediate host, then scp to the destination host,
     *  and then delete the immediate copy (to clean up).
     *
     * @param source data to be uploaded
     * @param remoteFileName destination file path/name
     * @return true if the entire uploaded was successful
     */
    boolean upload(InputStream source, String remoteFileName);

    boolean download(String remoteFileName, String localFileName);

    boolean download(String remoteFileName, File localFile);

    /**
     * Does not automatically close the given source stream.
     *
     * For downloading data from multiple network hops away,
     *  use a similar method as described for uploading.
     * @see #upload(InputStream, String)
     * 
     * @param remoteFileName file path/name to download
     * @param destination sink for remote file data
     * @return true if the entire download was successful
     */
    boolean download(String remoteFileName, OutputStream destination);

    boolean open();

    void openChecked() throws IOException;

    @Override
    void close();
}
