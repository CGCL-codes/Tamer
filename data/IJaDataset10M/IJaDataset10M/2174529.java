package com.xinsdd.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.xinsdd.client.frame.update.UpdateFrame;

public class Download {

    private String filePath;

    private String localDir;

    private UpdateFrame parent;

    private String fileName;

    private int countConnection = 0;

    /**
	 * 
	 * @param filepath
	 *            远程文件路径
	 * @param savePath
	 *            文件保存目录
	 * @param parent
	 *            窗体
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public Download(String filepath, String savePath, UpdateFrame parent) throws NumberFormatException, MalformedURLException, FileNotFoundException, IOException {
        this.filePath = filepath;
        this.parent = parent;
        this.localDir = localSavePath(filepath, savePath);
        downloadFile();
        System.out.println("下载完成");
    }

    private void downloadFile() throws MalformedURLException, FileNotFoundException, IOException {
        if (isRemoteFileExist(filePath)) {
            final long filesize;
            RemoteFile rf = getRemoteFile(filePath);
            final long remoteSize = rf.size;
            final String realUrl = rf.realUrl;
            File f = new File(localDir);
            if (f.exists()) {
                filesize = f.length();
            } else {
                filesize = 0;
            }
            try {
                if (filesize < remoteSize) {
                    URL u = new URL(realUrl);
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestProperty("RANGE", "bytes=" + filesize + "-");
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.1.4322)");
                    InputStream input = connection.getInputStream();
                    RandomAccessFile SavedFile = new RandomAccessFile(localDir, "rw");
                    SavedFile.seek(filesize);
                    byte[] b = new byte[1024];
                    int nRead;
                    double dsize = Double.parseDouble(remoteSize + "");
                    long readed = filesize;
                    while ((nRead = input.read(b, 0, 1024)) > 0) {
                        readed += nRead;
                        double dreaded = Double.valueOf(readed + "");
                        int progressValue = (int) (dreaded / dsize * 100);
                        if (parent != null) {
                            parent.setProgressValue("正在下载\"" + fileName + "\"", progressValue);
                        }
                        SavedFile.write(b, 0, nRead);
                    }
                    connection.disconnect();
                    SavedFile.close();
                }
            } catch (NumberFormatException e) {
                throw e;
            } catch (MalformedURLException e) {
                throw e;
            } catch (FileNotFoundException e) {
                throw e;
            } catch (IOException e) {
                if (countConnection < 3) {
                    downloadFile();
                } else {
                    throw e;
                }
            }
        }
    }

    /**
	 * 保存路径
	 * 
	 * @param fileParh
	 * @param savePath
	 * @return
	 */
    private String localSavePath(String fileParh, String savePath) {
        this.fileName = fileParh.substring(fileParh.lastIndexOf("/") + 1, fileParh.lastIndexOf("?") > 0 ? fileParh.lastIndexOf("?") : fileParh.length());
        return savePath + "/" + fileName;
    }

    private static boolean isRemoteFileExist(String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            in = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * 远程文件大小
	 * 
	 * @param url
	 * @return
	 */
    private static RemoteFile getRemoteFile(String url) {
        long size = 0;
        String realUrl = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            size = conn.getContentLength();
            realUrl = conn.getURL().toString();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RemoteFile rf = new RemoteFile(size, realUrl);
        return rf;
    }
}

class RemoteFile {

    long size;

    String realUrl;

    RemoteFile(long size, String realUrl) {
        this.size = size;
        this.realUrl = realUrl;
    }
}
