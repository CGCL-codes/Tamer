package org.lc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * ��Ҫ�Ƕ��ļ��Ļ����
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: file�����
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * @version 1.0
 */
public class FileTool {

    public FileTool() {
    }

    /**
	 * ���ļ�
	 * 
	 * @param fileName:�ļ�����Դ�·��
	 * @return
	 */
    public static String read(String fileName) {
        File file = new File(fileName);
        FileInputStream in = null;
        String fileContent = "";
        try {
            int size = (int) file.length();
            in = new FileInputStream(file);
            byte[] b = new byte[size];
            in.read(b);
            fileContent = new String(new String(b, "GB2312").getBytes("GB2312"));
        } catch (Exception e) {
            fileContent = "";
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        return fileContent;
    }

    /**
	 * д�ļ�
	 * 
	 * @param fileName:�ļ��������Դ�·��
	 * @param value:�ļ�����
	 */
    public static void writeFile(String fileName, String value) {
        FileOutputStream outfile = null;
        OutputStreamWriter out = null;
        try {
            outfile = new FileOutputStream(fileName);
            out = new OutputStreamWriter(outfile);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex1) {
            }
            if (outfile != null) {
                try {
                    outfile.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static boolean MoveFile(String SourcePath, String targetPath) {
        boolean MoveFile = false;
        String sPath, tPath;
        sPath = SourcePath.trim();
        tPath = targetPath.trim();
        java.io.File f = new java.io.File(sPath);
        try {
            if (f.exists()) {
                java.io.File file = new java.io.File(tPath);
                MoveFile = f.renameTo(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MoveFile;
    }

    public static boolean delDir(String dirPath) {
        File f = new File(dirPath);
        if (f.exists()) {
            if (f.isDirectory()) {
                String[] dirs = f.list();
                if (dirs != null) {
                    for (int i = 0; i < dirs.length; i++) {
                        File f1 = new File(dirPath + "/" + dirs[i]);
                        if (f1.isDirectory()) delDir(f1.getAbsolutePath()); else {
                            f1.delete();
                        }
                    }
                }
            }
            f.delete();
            return true;
        } else return false;
    }
}
