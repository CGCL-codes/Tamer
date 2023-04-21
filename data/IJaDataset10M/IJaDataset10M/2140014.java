package fedora.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip and GZip utilities.
 * 
 * @author Edwin Shin
 *
 */
public class Zip {

    private static final int BUFFER = 2048;

    /**
	 * Create a zip file.
	 * 
	 * @param destination The zip file to create.
	 * @param source The file or directory to be zipped.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void zip(File destination, File source) throws FileNotFoundException, IOException {
        zip(destination, new File[] { source });
    }

    /**
	 * Create a zip file.
	 * @param destination The zip file to create.
	 * @param source The File array to be zipped.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void zip(File destination, File[] source) throws FileNotFoundException, IOException {
        FileOutputStream dest = new FileOutputStream(destination);
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(dest));
        for (int i = 0; i < source.length; i++) {
            zip(null, source[i], zout);
        }
        zout.close();
    }

    /**
	 * Extracts the file given by entryName to destination.
	 * 
	 * @param zipFile
	 * @param entryName
	 * @param destination The extracted destination File.
	 * @throws IOException
	 */
    public static void extractFile(File zipFile, String entryName, File destination) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        try {
            ZipEntry entry = zip.getEntry(entryName);
            if (entry != null) {
                InputStream entryStream = zip.getInputStream(entry);
                try {
                    File parent = destination.getParentFile();
                    if (parent != null) {
                        parent.mkdirs();
                    }
                    FileOutputStream file = new FileOutputStream(destination);
                    try {
                        byte[] data = new byte[BUFFER];
                        int bytesRead;
                        while ((bytesRead = entryStream.read(data)) != -1) {
                            file.write(data, 0, bytesRead);
                        }
                    } finally {
                        file.close();
                    }
                } finally {
                    entryStream.close();
                }
            } else {
                throw new IOException(zipFile.getName() + " does not contain: " + entryName);
            }
        } finally {
            zip.close();
        }
    }

    /**
	 * Unzips the InputStream to the given destination directory.
	 * @param is
	 * @param destDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static void unzip(InputStream is, File destDir) throws FileNotFoundException, IOException {
        BufferedOutputStream dest = null;
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                (new File(destDir, entry.getName())).mkdirs();
            } else {
                File f = new File(destDir, entry.getName());
                f.getParentFile().mkdirs();
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(f);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
        }
        zis.close();
    }

    public static void gzip() {
    }

    public static void gunzip() {
    }

    public static void zip(String destination, String source) throws FileNotFoundException, IOException {
        zip(new File(destination), new File(source));
    }

    public static void unzip(InputStream is, String destDir) throws FileNotFoundException, IOException {
        unzip(is, new File(destDir));
    }

    public static void main(String[] args) {
    }

    private static void zip(String baseDir, File source, ZipOutputStream zout) throws IOException {
        ZipEntry entry = null;
        if (baseDir == null || baseDir.equals(".") || baseDir.equals("./")) {
            baseDir = "";
        }
        if (source.isDirectory()) {
            entry = new ZipEntry(baseDir + source.getName() + "/");
        } else {
            entry = new ZipEntry(baseDir + source.getName());
        }
        zout.putNextEntry(entry);
        if (!source.isDirectory()) {
            byte data[] = new byte[BUFFER];
            FileInputStream fis = new FileInputStream(source);
            BufferedInputStream origin = new BufferedInputStream(fis, BUFFER);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                zout.write(data, 0, count);
            }
            fis.close();
            origin.close();
        } else {
            File files[] = source.listFiles();
            for (int i = 0; i < files.length; i++) {
                zip(entry.getName(), files[i], zout);
            }
        }
    }
}
