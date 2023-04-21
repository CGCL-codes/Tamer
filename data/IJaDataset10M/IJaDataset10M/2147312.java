package com.lonedev.androftpsync;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import android.util.Log;

public class FTPUtils {

    private String ftpUsername, ftpHostname, ftpPassword;

    private int ftpPort;

    private FTPClient ftpClient;

    public static final String TAG = "FTPUtils";

    public static final int DEFAULT_FTP_PORT = 21;

    public FTPUtils(String ftpHostname, int ftpPort, String ftpUsername, String ftpPassword) throws SocketException, IOException {
        this.ftpUsername = ftpUsername;
        this.ftpHostname = ftpHostname;
        this.ftpPort = ftpPort;
        this.ftpPassword = ftpPassword;
    }

    public FTPUtils(String ftpHostname, String ftpUsername, String ftpPassword) throws SocketException, IOException {
        this(ftpHostname, DEFAULT_FTP_PORT, ftpUsername, ftpPassword);
    }

    public void connect() throws SocketException, IOException {
        Log.i(TAG, "Test attempt login to " + ftpHostname + " as " + ftpUsername);
        ftpClient = new FTPClient();
        ftpClient.connect(this.ftpHostname, this.ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            String error = "Login failure (" + reply + ") : " + ftpClient.getReplyString();
            Log.e(TAG, error);
            throw new IOException(error);
        }
    }

    public void syncToFTPDirectory(File localFolder, File remoteFolder) {
        List<File> syncLocalFiles = new ArrayList<File>();
        getFiles(localFolder, syncLocalFiles);
        Log.d(TAG, "Retrieved " + syncLocalFiles + " local files for syncing");
    }

    private void getFiles(File folder, List<File> syncFiles) {
        File[] folderFiles = folder.listFiles();
        if (!folder.exists()) {
            Log.e(TAG, "Folder " + folder.getName() + " does not exist locally.");
            return;
        }
        for (File file : folderFiles) {
            if (file.isDirectory()) {
                Log.v(TAG, "File " + file.getName() + " is a directory. Getting it's folders.");
                getFiles(file, syncFiles);
            } else if (file.isFile()) {
                Log.v(TAG, file.getName() + " added to list.");
                syncFiles.add(file);
            }
        }
    }

    public void disconnect() throws IOException {
        if (ftpClient != null) {
            Log.i(TAG, "Disconnecting from " + ftpHostname);
            ftpClient.disconnect();
        }
    }
}
