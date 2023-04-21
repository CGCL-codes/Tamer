package com.nhncorp.cubridqa.console.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.nhncorp.cubridqa.replication.config.SystemHandle;

/**
 * 
 * @ClassName: LogUtil
 * @Description: utility to read,write ,copy or delete file .Compare the
 *               difference between two files . create directory and get the dir
 *               name .
 * 
 * @date 2009-9-4
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class FileUtil {

    /**
	 * read the content of file .
	 * 
	 * @param file
	 * @return
	 */
    public static String readFile(String file) {
        if (file == null) {
            return null;
        }
        String ret = null;
        byte[] data = null;
        List<byte[]> list = new ArrayList<byte[]>();
        byte[] temp = new byte[1024];
        int totalSize = 0;
        int index = 0;
        FileInputStream input = null;
        try {
            File f = new File(file);
            if (!f.exists()) {
                return null;
            }
            input = new FileInputStream(f);
            int count = input.read(temp);
            while (count != -1) {
                byte[] bytes = new byte[count];
                System.arraycopy(temp, 0, bytes, 0, count);
                list.add(bytes);
                totalSize = totalSize + count;
                count = input.read(temp);
            }
            data = new byte[totalSize];
            for (int i = 0; i < list.size(); i++) {
                temp = (byte[]) list.get(i);
                System.arraycopy(temp, 0, data, index, temp.length);
                index = index + temp.length;
            }
            ret = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return ret;
    }

    public static void writeToFile(String file, String data) {
        writeToFile(file, data, false);
    }

    /**
	 * write data to file .
	 * 
	 * @param file
	 * @param data
	 * @param append
	 *            if append or rewrite .
	 */
    public static void writeToFile(String file, String data, boolean append) {
        if (file == null || data == null) {
            return;
        }
        BufferedWriter writer = null;
        try {
            File f = new File(file);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            f.setWritable(true);
            f.setExecutable(true);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, append), "UTF-8"));
            writer.write(data);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * create directory.
	 * 
	 * @param dirPath
	 */
    public static void createDir(String dirPath) {
        if (dirPath == null) {
            return;
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            StringBuilder path = new StringBuilder("");
            boolean flag = dirPath.startsWith("/");
            if (flag) {
                path.append("/");
            }
            StringTokenizer token = new StringTokenizer(dirPath, "/");
            while (token.hasMoreTokens()) {
                String dirName = token.nextToken();
                path.append(dirName);
                try {
                    File currentDir = new File(path.toString());
                    if (!currentDir.exists()) {
                        currentDir.mkdir();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path.append("/");
            }
        }
    }

    /**
	 * get the directory name by the file name .
	 * 
	 * @param file
	 * @return
	 */
    public static String getDirPath(String file) {
        if (file == null) {
            return null;
        }
        String ret = null;
        int position = file.lastIndexOf("/");
        if (position != -1) {
            ret = file.substring(0, position);
        }
        return ret;
    }

    /**
	 * copy file from source directory to target directory.
	 * 
	 * @param srcFile
	 * @param targetFile
	 */
    public static void copyFile(String srcFile, String targetFile) {
        File f = new File(srcFile);
        if (!f.exists()) {
            return;
        }
        String targetDir = FileUtil.getDirPath(targetFile);
        FileUtil.createDir(targetDir);
        CommandUtil.execute("cp " + srcFile + " " + targetFile, null);
    }

    /**
	 * get the difference between two files.
	 * 
	 * @param srcFile
	 * @param targetFile
	 * @return
	 */
    public static String compare(String srcFile, String targetFile) {
        return CommandUtil.execute("diff " + srcFile + " " + targetFile, null);
    }

    /**
	 * remove the file or directory.
	 * 
	 * @param file
	 * @return
	 */
    public static String deleteFile(String file) {
        return CommandUtil.execute("rm -rf " + file, null);
    }

    /**
	 * download file from remote machine .
	 * 
	 * @param host
	 * @param user
	 * @param passwd
	 * @param srcFile
	 * @param retFile
	 */
    public static void downloadFile(String host, String user, String passwd, String srcFile, String retFile) {
        if (host == null || user == null || passwd == null || srcFile == null || retFile == null) {
            return;
        }
        String cmd = "expect";
        String shPath = RepositoryPathUtil.getScriptConfPath();
        if (com.nhncorp.cubridqa.utils.FileUtil.isLinux()) {
            String command = cmd + " " + shPath + "/download.sh" + " -user " + user + " -host " + host + " -password " + passwd + " -src " + srcFile + " -ret " + retFile;
            CommandUtil.execute(command, null);
        } else {
            String command = shPath + "/download.sh" + " -user " + user + " -host " + host + " -password " + passwd + " -src " + srcFile + " -ret " + retFile;
            SystemHandle.executeWCmdShell(command);
        }
    }

    /**
	 * get the directory by the file name .
	 * 
	 * @param file
	 * @return
	 */
    public static String getDir(String file) {
        if (file == null || file.trim().equals("")) {
            return null;
        }
        String ret = file;
        int position = file.lastIndexOf("/");
        if (position != -1) {
            ret = file.substring(0, position);
        }
        return ret;
    }

    /**
	 * get the file name by the path .
	 * 
	 * @param path
	 * @return
	 */
    public static String getFileName(String path) {
        String ret = path;
        int position = path.lastIndexOf("/");
        if (position > 0) {
            ret = path.substring(position + 1);
            int position2 = ret.indexOf(".");
            if (position2 != -1) {
                ret = ret.substring(0, position2);
            }
        }
        return ret;
    }

    /**
	 * determin if file exist.
	 * 
	 * @param filePath
	 * @return
	 */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
