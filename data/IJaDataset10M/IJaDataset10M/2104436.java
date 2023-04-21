package updater.model.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Utility class with static methods providing access to zip archive files.
 * 
 * @author Dominik Schaufelberger <dominik.schaufelberger@web.de>
 */
public class ZipArchiveExtractor {

    private ZipFile zipFile;

    public ZipArchiveExtractor(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Unzips the opened ZIP file to the destination directory {@code destDir}.
     * 
     * @param destDir
     *          destination directory
     * @return
     *          {@code File} Array containing the unzipped files in the archive.
     * @throws ZipException
     *          Given file {@code srcFile} is no valid zip archive.
     * @throws FileNotFoundException
     *          File to write in, at location {@code destDir}, doesn't exist or is invalid.
     * @throws IOException
     *          Common I/O error while streaming from archive to unzipped file.
     */
    public File[] unzip(String destDir) throws ZipException, FileNotFoundException, IOException {
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        File[] containedFiles = new File[zipFile.size()];
        int counter = 0;
        while (zipEntries.hasMoreElements()) {
            ZipEntry zipEntry = zipEntries.nextElement();
            containedFiles[counter++] = extractZipEntry(zipEntry.getName(), destDir);
        }
        return containedFiles;
    }

    /**
     * Extracts a single entry named by {@code entryname} from the ZIP archive
     * to the target location {@code destDir}.
     * 
     * @param entryName
     *      name of the entry
     * @param destDir
     *      target location
     * @return
     *      unzipped file
     * @throws IOException
     *      if I/O error occurs while accessing the ZIP archive
     */
    public File extractZipEntry(String entryName, String destDir) throws IOException {
        File unzippedFile = null;
        ZipEntry entry = null;
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements() && entry == null) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.getName().contains(entryName)) {
                entry = zipEntry;
            }
        }
        if (entry != null) {
            unzippedFile = new File(destDir, entry.getName());
            if (entry.isDirectory()) {
                unzippedFile.mkdirs();
            } else {
                new File(unzippedFile.getParent()).mkdirs();
                byte[] buffer = new byte[32768];
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = zipFile.getInputStream(entry);
                    out = new FileOutputStream(unzippedFile);
                    for (int len; (len = in.read(buffer)) != -1; ) {
                        out.write(buffer, 0, len);
                    }
                } finally {
                    if (in != null) in.close();
                    if (out != null) out.close();
                }
            }
        }
        return unzippedFile;
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

    public void setZipFile(ZipFile zipFile) {
        this.zipFile = zipFile;
    }
}
