package cn.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 * 
 * @author 孙树林
 * 
 */
public class CompressionUtil {

    /**
	 * 压缩文件zip格式
	 * 
	 * @param zipFilePath
	 * @param filePaths
	 * @throws IOException
	 */
    public static void compress(String zipFilePath, List<String> filePaths) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
        for (String filePath : filePaths) {
            compressFile(zos, new File(filePath), "");
        }
        zos.close();
        fos.close();
    }

    /**
	 * 压缩文件
	 * 
	 * @param zos
	 * @param file
	 * @param dir
	 * @throws IOException
	 */
    public static void compressFile(ZipOutputStream zos, File file, String dir) throws IOException {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                if (!dir.equals("")) {
                    compressFile(zos, f, dir + "\\" + file.getName());
                } else {
                    compressFile(zos, f, file.getName());
                }
            }
        } else {
            String entryName = null;
            if (!dir.equals("")) {
                entryName = dir + "\\" + file.getName();
            } else {
                entryName = file.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            InputStream is = new FileInputStream(file);
            int len = 0;
            while ((len = is.read()) != -1) {
                zos.write(len);
            }
            is.close();
        }
    }

    public static void compressFile(ZipOutputStream zos, InputStream is, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        int len = 0;
        while ((len = is.read()) != -1) {
            zos.write(len);
        }
        is.close();
    }

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        String zipFilePath = "E:\\CompressionUtil.zip";
        List<String> filePaths = new ArrayList<String>();
        filePaths.add("E:\\只能给我");
        CompressionUtil.compress(zipFilePath, filePaths);
    }
}
