package dbaccess.iono.General;

import java.io.*;

public class FileRW extends RandomAccessFile {

    public String filePathName;

    public String fileName;

    public String filePath;

    public FileRW(String inPathName, String mode) throws IOException {
        super(inPathName, mode);
        int i;
        filePathName = new String(inPathName);
        i = filePathName.lastIndexOf(File.separatorChar);
        fileName = filePathName.substring(i + 1);
        if (i > 0) filePath = filePathName.substring(0, i + 1);
    }

    public static String getExt(String fileName) {
        int i;
        i = fileName.lastIndexOf('.');
        return new String(fileName.substring(i + 1)).toUpperCase();
    }

    public static String getName(String fileName) {
        int i;
        i = fileName.lastIndexOf('.');
        if (i > 0) return new String(fileName.substring(0, i)).toUpperCase();
        return fileName.toUpperCase();
    }

    public boolean ready() throws IOException {
        return (length() - getFilePointer()) > 0;
    }

    public void newLine() throws IOException {
        writeBytes("\r\n");
    }
}
