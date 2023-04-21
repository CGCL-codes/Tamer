package org.kalypso.nofdpidss.core.common.utils.various;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static String convertFileName(final File packDir, final File file) {
        final String strPackDir = packDir.getAbsolutePath();
        final String strFileDir = file.getAbsolutePath();
        if (strFileDir.contains(strPackDir)) {
            String string = strFileDir.substring(strPackDir.length() + 1);
            string = string.replaceAll("\\\\", "/");
            return string;
        }
        return file.getName();
    }

    public static void pack(final File archiveTarget, final File packDir) throws ZipException, IOException {
        if (!packDir.isDirectory()) return;
        final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(archiveTarget));
        final ZipOutputStream out = new ZipOutputStream(bos);
        out.setMethod(ZipOutputStream.DEFLATED);
        final File[] files = packDir.listFiles();
        for (final File file : files) ZipUtils.processFiles(packDir, file, out);
        out.close();
        bos.flush();
        bos.close();
    }

    private static void processFiles(final File packDir, final File file, final ZipOutputStream out) throws IOException {
        if (file.isDirectory()) {
            final ZipEntry e = new ZipEntry(ZipUtils.convertFileName(packDir, file) + "/");
            out.putNextEntry(e);
            out.closeEntry();
            final File[] files = file.listFiles();
            for (final File f : files) ZipUtils.processFiles(packDir, f, out);
        } else {
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            final ZipEntry e = new ZipEntry(ZipUtils.convertFileName(packDir, file));
            final CRC32 crc = new CRC32();
            out.putNextEntry(e);
            final byte[] buf = new byte[4096];
            int len = 0;
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
                crc.update(buf, 0, len);
            }
            e.setCrc(crc.getValue());
            out.closeEntry();
            bis.close();
        }
    }

    public static void rmDir(final File dir) {
        if (!dir.isDirectory()) return;
        final File[] files = dir.listFiles();
        for (final File file : files) if (file.isDirectory()) ZipUtils.rmDir(file); else file.delete();
        dir.delete();
    }

    private static void saveEntry(final ZipFile zf, final File targetDir, final ZipEntry target) throws ZipException, IOException {
        final File file = new File(targetDir.getAbsolutePath() + "/" + target.getName());
        if (target.isDirectory()) file.mkdirs(); else {
            final InputStream is = zf.getInputStream(target);
            final BufferedInputStream bis = new BufferedInputStream(is);
            new File(file.getParent()).mkdirs();
            final FileOutputStream fos = new FileOutputStream(file);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            final int EOF = -1;
            for (int c; (c = bis.read()) != EOF; ) bos.write((byte) c);
            bos.close();
            fos.close();
        }
    }

    public static void unpack(final ZipFile zf, final File targetDir) throws ZipException, IOException {
        targetDir.mkdir();
        for (final Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements(); ) {
            final ZipEntry target = e.nextElement();
            System.out.print(target.getName() + " .");
            ZipUtils.saveEntry(zf, targetDir, target);
            System.out.println(". unpacked");
        }
    }
}
