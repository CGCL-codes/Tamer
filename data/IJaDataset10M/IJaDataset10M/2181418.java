package com.bones.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {

    /**
	 * 向txt文本中写入内容
	 * 
	 * @param file
	 *            要写入的文件 
	 *            
	 * @param content
	 *            写入的具体内容
	 * @return
	 */
    public boolean WriteTxtToFile(File file, String content) {
        if (file.exists()) {
            file.delete();
        }
        byte[] b = content.getBytes();
        try {
            OutputStream writer = new FileOutputStream(file);
            writer.write(b);
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * 向某个文件中追加指定的内容，如果没有改文件，则创建它
	 * @param file 文件
	 * @param content 输入文本内容
	 * @param flag 是否追加，true 追加，false不追加
	 * @return 是否写入成功
	 */
    public boolean WriteTxtToFile(File file, String content, boolean flag) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter filewriter2 = null;
        try {
            filewriter2 = new FileWriter(file, flag);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            filewriter2.write(content);
            filewriter2.flush();
            filewriter2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * 读取指定文件中到内容到String中
	 * 
	 * @param file
	 *            要读取内容的文件
	 * @return
	 * @throws IOException
	 */
    public String readFormFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader ipsr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(ipsr);
        String str = null;
        StringBuffer sb = new StringBuffer();
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
            ipsr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
	 * 将指定路径的文件移动到另一个目录
	 * @param sourcefilepath 要移动的文件路径
	 * @param destdicpath 移动到的目标目录
	 * @return
	 */
    public boolean move(String sourcefilepath, String destdicpath) {
        File sourcefile = new File(sourcefilepath);
        if (sourcefile.exists()) {
            return sourcefile.renameTo(new File(destdicpath + sourcefile.getName()));
        }
        return false;
    }
}
