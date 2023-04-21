package com.pk.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * <p>
 * 描述:文件工具类
 * </p>
 * @author 3hphw
 * @version 1.0
 */
public class FileUtil {

    private String filePath;

    public FileUtil(String filePath) {
        this.filePath = filePath;
    }

    public void clean() {
        this.write("");
    }

    public void write(String content) {
        this.write(content, false, false);
    }

    public void append(String content) {
        this.write(content, true, false);
    }

    public void appendLine(String content) {
        this.write(content, true, true);
    }

    private void write(String content, boolean isAppend, boolean isln) {
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            fw = new FileWriter(file, isAppend);
            if (isln) {
                content += "\r\n";
            }
            fw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>
     * 说明:输出文件
     * </p>
     * @param in
     * @param path
     */
    public static void fileOutput(InputStream in, String uploadPath, String fileName) throws IOException {
        File upload = new File(uploadPath);
        File file = new File(uploadPath + fileName);
        makeDirFile(upload, file);
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int index = -1;
        while ((index = in.read(buffer)) != -1) {
            out.write(buffer, 0, index);
        }
        out.close();
    }

    /**
     * <p>
     * 说明:删除文件
     * </p>
     * @param path
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * <p>
     * 说明:文件上传
     * </p>
     * @param uploadFile
     * @param path 形式: dailyWork/office
     * @param filename 形式: hello.doc
     * @param inProject 是否在工程上
     * @throws Exception
     */
    public static void upload(InputStream uploadFile, String path, String filename) throws Exception {
        WebContext webContext = WebContextFactory.get();
        HttpServletRequest request = webContext.getHttpServletRequest();
        StringBuffer uploadPath = new StringBuffer();
        uploadPath.append(request.getSession().getServletContext().getRealPath(path));
        File fileDir = new File(uploadPath.toString());
        File file = new File(uploadPath.toString() + "/" + filename);
        makeDirFile(fileDir, file);
        int available = uploadFile.available();
        byte[] b = new byte[available];
        FileOutputStream foutput = new FileOutputStream(file);
        uploadFile.read(b);
        foutput.write(b);
        foutput.flush();
        foutput.close();
        uploadFile.close();
    }

    /**
     * <p>
     * 说明:文件上传到其他盘
     * </p>
     */
    public static void uploadToCustomDisk(InputStream uploadFile, String diskPath, String filename) throws Exception {
        File fileDir = new File(diskPath);
        File file = new File(diskPath + "/" + filename);
        makeDirFile(fileDir, file);
        int available = uploadFile.available();
        byte[] b = new byte[available];
        FileOutputStream foutput = new FileOutputStream(file);
        uploadFile.read(b);
        foutput.write(b);
        foutput.flush();
        foutput.close();
        uploadFile.close();
    }

    /**
     * <p>
     * 说明:文件批量上传
     * </p>
     * @param file
     * @param name
     * @param path
     * @throws IOException
     */
    public static void batchUpload(File file, String name, String path) throws IOException {
        File dir = new File(path);
        File newFile = new File(path + name);
        makeDirFile(dir, newFile);
        FileOutputStream out = new FileOutputStream(newFile);
        InputStream in = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            out.write(buffer);
        }
        out.close();
        in.close();
    }

    /**
     * <p>
     * 说明:创建路径
     * </p>
     * @param dir
     * @param file
     */
    public static void makeDir(File dir) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * <p>
     * 说明:创建路径与文件
     * </p>
     * @param dir
     * @param file
     */
    public static void makeDirFile(File dir, File file) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
