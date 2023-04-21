package ru.ipo.dces.utils;

import java.io.*;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 13:17:35
 */
public class ZipUtils {

    private static final int BUFFER = 4096;

    private ZipUtils() {
    }

    public static byte[] zip(File file) throws IOException {
        if (!file.exists()) throw new FileNotFoundException("file or folder not found: " + file.getCanonicalPath());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOS = new ZipOutputStream(out);
        if (file.isFile()) doZipFile(zipOS, "", file); else archiveCatalog(file, zipOS, "");
        zipOS.close();
        return out.toByteArray();
    }

    public static void unzip(byte[] zip, File folder) throws IOException {
        int BUFFER = 4096;
        BufferedOutputStream dest;
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            int count;
            byte data[] = new byte[BUFFER];
            if (entry.isDirectory()) {
                (new File(entry.getName())).mkdirs();
                continue;
            }
            File fout = new File(folder.getCanonicalPath() + '/' + entry.getName());
            FileSystemUtils.ensureFileHasPath(fout);
            FileOutputStream fos = new FileOutputStream(fout);
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zis.close();
    }

    private static void archiveCatalog(File dir, ZipOutputStream zipOS, String zipPath) throws IOException {
        String[] files = dir.list();
        if (files == null) return;
        for (String fileName : files) {
            File file = new File(dir.getAbsolutePath() + "/" + fileName);
            if (file.isDirectory()) {
                archiveCatalog(file, zipOS, zipPath + fileName + "/");
            } else {
                doZipFile(zipOS, zipPath, file);
            }
        }
    }

    private static void doZipFile(ZipOutputStream zipOS, String zipPath, File file) throws IOException {
        String fileName = file.getName();
        byte[] data = new byte[BUFFER];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        zipOS.putNextEntry(new ZipEntry(zipPath + fileName));
        int count;
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zipOS.write(data, 0, count);
        }
        zipOS.closeEntry();
        bis.close();
    }
}
