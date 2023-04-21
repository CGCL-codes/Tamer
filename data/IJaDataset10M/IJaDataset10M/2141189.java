package net.tourbook.srtm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class FileZip {

    public static final String gunzip(final String gzipName) throws Exception {
        String outFileName = null;
        String gzipEntryName = null;
        try {
            final GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(gzipName));
            gzipEntryName = gzipName;
            if (gzipEntryName.indexOf(File.separator) != -1) gzipEntryName = gzipEntryName.substring(gzipEntryName.lastIndexOf(File.separator) + 1);
            if (gzipEntryName.indexOf('.') != -1) gzipEntryName = gzipEntryName.substring(0, gzipEntryName.lastIndexOf('.'));
            outFileName = gzipName.substring(0, gzipName.lastIndexOf(File.separator)) + File.separator + gzipEntryName;
            System.out.println("outFileName " + outFileName);
            final OutputStream fileOutputStream = new FileOutputStream(outFileName);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }
            fileOutputStream.close();
            gzipInputStream.close();
            return gzipEntryName;
        } catch (final IOException e) {
            System.out.println("gunzip: Error: " + e.getMessage());
            throw (e);
        }
    }

    public static void main(final String[] args) throws Exception {
    }

    public static final String unzip(final String zipName) throws Exception {
        String outFileName = null;
        String zipEntryName = null;
        try {
            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipName));
            final ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                throw new Exception("zipEntry is null");
            }
            zipEntryName = zipEntry.getName();
            System.out.println("zipEntryName " + zipEntryName);
            if (zipEntryName.indexOf(File.separator) != -1) zipEntryName = zipEntryName.substring(zipEntryName.lastIndexOf(File.separator) + 1);
            outFileName = zipName.substring(0, zipName.lastIndexOf(File.separator)) + File.separator + zipEntryName;
            System.out.println("outFileName " + outFileName);
            final OutputStream fileOutputStream = new FileOutputStream(outFileName);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = zipInputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }
            fileOutputStream.close();
            zipInputStream.close();
            return zipEntryName;
        } catch (final IOException e) {
            System.out.println("unzip: Error: " + e.getMessage());
            throw (e);
        }
    }

    public static final void zip(String fileName, final String zipName) throws Exception {
        try {
            final File file = new File(fileName);
            final FileInputStream fileInputStream = new FileInputStream(file);
            final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipName));
            if (fileName.indexOf(File.separator) != -1) fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            final byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
            zipOutputStream.close();
        } catch (final IOException e) {
            System.out.println("zip: Error: " + e.getMessage());
            throw (e);
        }
    }
}
