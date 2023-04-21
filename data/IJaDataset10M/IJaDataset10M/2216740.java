package org.personalsmartspace.sre.slm.impl;

import java.io.*;
import java.util.zip.*;
import org.personalsmartspace.log.impl.PSSLog;

/**
 * File compression utilities.
 *
 * @author Mitja Vardjan
 */
public class Compression {

    private PSSLog log = new PSSLog(this);

    private final int BUFFER = 2048;

    /**
	 * File compression and extraction.
	 */
    public Compression() {
    }

    /**
     * Unzip a file in archive from memory to memory, without usage of any real
     * files.
     * 
     * @param archive The archive to extract a file from.
     * @param fileName Name of the file to unzip.
     * @return The file in the archive.
     */
    public byte[] unzip(byte[] archive, String fileName) {
        ByteArrayOutputStream decompressedFile;
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(archive));
        ZipEntry entry;
        int count;
        byte[] data = new byte[BUFFER];
        try {
            while ((entry = zis.getNextEntry()) != null) {
                log.debug("Found: " + entry);
                data = new byte[BUFFER];
                if (!entry.isDirectory() && entry.getName().equals(fileName)) {
                    decompressedFile = new ByteArrayOutputStream();
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        decompressedFile.write(data, 0, count);
                    }
                    decompressedFile.flush();
                    decompressedFile.close();
                    zis.close();
                    return decompressedFile.toByteArray();
                }
            }
        } catch (IOException ex) {
            log.error(ex.toString(), ex);
            return null;
        }
        log.warn("File \"" + fileName + "\" not found in zip archive of length " + archive.length);
        return null;
    }
}
