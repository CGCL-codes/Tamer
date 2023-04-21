package org.jcvi.io.fileServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.finj.FTPClient;
import org.finj.FTPException;
import org.finj.RemoteFile;

public class FTPFileServer implements ReadWriteFileServer {

    private static final Pattern DIR_PATTERN = Pattern.compile("(.+)/(.+?)$");

    private final FTPClient client;

    /**
     * 
     * @param server
     * @param username
     * @param password
     * @throws IOException
     */
    public FTPFileServer(String server, String username, char[] password) throws IOException {
        this(new FTPClient(), server, username, password);
    }

    FTPFileServer(FTPClient ftpClient, String server, String username, char[] password) throws IOException {
        client = ftpClient;
        try {
            client.isVerbose(false);
            client.open(server);
            client.login(username, password);
        } catch (FTPException e) {
            throw new IOException("error logging into server " + server, e);
        }
    }

    /**
     * FTP does not support returning Files. Will always
     * throw {@link UnsupportedOperationException}.
     * @throws UnsupportedOperationException every time this method is called.
     * @see #supportsGettingFileObjects()
     */
    @Override
    public File getFile(String fileId) throws IOException {
        throw new UnsupportedOperationException("can not create local files");
    }

    @Override
    public InputStream getFileAsStream(String filePath) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        client.getFile(output, filePath);
        return new ByteArrayInputStream(output.toByteArray());
    }

    /**
     * 
     */
    @Override
    public void putFile(String destinationPath, File fileToPut) throws IOException {
        InputStream inputStream = new FileInputStream(fileToPut);
        putStream(destinationPath, inputStream);
    }

    @Override
    public void putStream(String destinationPath, InputStream inputStream) throws IOException {
        client.putFile(inputStream, destinationPath, true);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public boolean supportsGettingFileObjects() {
        return false;
    }

    @Override
    public boolean contains(String filePath) throws IOException {
        Matcher matcher = DIR_PATTERN.matcher(filePath);
        String file;
        String dir;
        if (matcher.find()) {
            dir = matcher.group(1);
            file = matcher.group(2);
        } else {
            dir = "";
            file = filePath;
        }
        try {
            for (RemoteFile remoteFile : client.getFileDescriptors(dir)) {
                if (file.equals(remoteFile.getName())) {
                    return true;
                }
            }
            return false;
        } catch (FTPException e) {
            throw new IOException("error getting data from FTP", e);
        }
    }

    @Override
    public File createNewDirIfNeeded(String dirPath) throws IOException {
        throw new UnsupportedOperationException("creating new directories not supported on ftp");
    }

    @Override
    public File createNewFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("creating new files not supported on ftp");
    }

    @Override
    public File createNewDir(String dirPath) throws IOException {
        throw new UnsupportedOperationException("creating new directories not supported on ftp");
    }

    @Override
    public void createNewSymLink(String pathtoFileToLink, String symbolicPath) throws IOException {
        throw new UnsupportedOperationException("sym links not supported");
    }

    @Override
    public boolean supportsSymlinks() {
        return false;
    }
}
