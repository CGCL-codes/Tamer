package cn.edu.wuse.musicxml.demo;

import java.io.*;
import java.util.zip.*;
import java.util.*;

public class OpenZipFile {

    static String copyDir = null;

    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        copyDir = "E:\\";
        ZipFile zf = new ZipFile("E:\\作品集.mxl");
        Enumeration en = zf.entries();
        while (en.hasMoreElements()) {
            ZipEntry fi = (ZipEntry) en.nextElement();
            System.out.println(fi.getName());
            InAndOut(zf, fi);
        }
        zf.close();
        long end = System.currentTimeMillis();
        System.out.println('\n' + " " + '\n' + "耗用时间(秒): " + (end - start) / 1000 + " " + '\n' + "    -----恭喜您!  解压成功!!!");
    }

    public static void InAndOut(ZipFile zf, ZipEntry ze) throws Exception {
        if (ze.isDirectory()) {
            File f = new File(copyDir + ze.getName());
            f.mkdirs();
        } else {
            System.out.println("====" + ze.getName());
            InputStream in = zf.getInputStream(ze);
            FileOutputStream out = new FileOutputStream(copyDir + ze.getName());
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
    }
}
