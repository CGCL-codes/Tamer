package net.laubenberger.bogatyr.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import net.laubenberger.bogatyr.misc.Constants;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsEmpty;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsNull;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionMustBeGreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a helper class for compress operations.
 *
 * @author Stefan Laubenberger
 * @version 0.9.4 (20101119)
 * @since 0.3.0
 */
public abstract class HelperCompress {

    private static final Logger log = LoggerFactory.getLogger(HelperCompress.class);

    /**
	 * Writes a ZIP {@link File} containing a list of {@link File}.
	 *
	 * @param file  for writing
	 * @param files for the ZIP file
	 * @throws IOException
	 * @see File
	 * @since 0.3.0
	 */
    public static void writeZip(final File file, final File... files) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, files));
        writeZip(file, files, Constants.DEFAULT_FILE_BUFFER_SIZE);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    /**
	 * Writes a ZIP {@link File} containing a list of {@link File}.
	 *
	 * @param file  for writing
	 * @param files for the ZIP file
	 * @param bufferSize in bytes
	 * @throws IOException
	 * @see File
	 * @since 0.8.0
	 */
    public static void writeZip(final File file, final File[] files, final int bufferSize) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, files, bufferSize));
        if (null == file) {
            throw new RuntimeExceptionIsNull("file");
        }
        if (null == files) {
            throw new RuntimeExceptionIsNull("files");
        }
        if (!HelperArray.isValid(files)) {
            throw new RuntimeExceptionIsEmpty("files");
        }
        if (1 > bufferSize) {
            throw new RuntimeExceptionMustBeGreater("bufferSize", bufferSize, 1);
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(file));
            for (final File entry : files) {
                addEntry(zos, entry, bufferSize);
            }
        } finally {
            if (null != zos) {
                zos.close();
            }
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    /**
	 * Extracts a ZIP {@link File} to a destination directory.
	 *
	 * @param file					  to extract
	 * @param destinationDirectory for the ZIP file
	 * @throws IOException
	 * @see File
	 * @since 0.3.0
	 */
    public static void extractZip(final File file, final File destinationDirectory) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, destinationDirectory));
        extractZip(file, destinationDirectory, Constants.DEFAULT_FILE_BUFFER_SIZE);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    /**
	 * Extracts a ZIP {@link File} to a destination directory.
	 *
	 * @param file					  to extract
	 * @param destinationDirectory for the ZIP file
	 * @param bufferSize			  in bytes
	 * @throws IOException 
	 * @see File
	 * @since 0.8.0
	 */
    public static void extractZip(final File file, final File destinationDirectory, final int bufferSize) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(file, destinationDirectory, bufferSize));
        if (null == file) {
            throw new RuntimeExceptionIsNull("file");
        }
        if (null == destinationDirectory) {
            throw new RuntimeExceptionIsNull("destinationDirectory");
        }
        if (1 > bufferSize) {
            throw new RuntimeExceptionMustBeGreater("bufferSize", bufferSize, 1);
        }
        final ZipFile zf = new ZipFile(file);
        final Enumeration<? extends ZipEntry> zipEntryEnum = zf.entries();
        try {
            while (zipEntryEnum.hasMoreElements()) {
                final ZipEntry zipEntry = zipEntryEnum.nextElement();
                extractEntry(zf, zipEntry, destinationDirectory, bufferSize);
            }
        } finally {
            zf.close();
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    private static void addEntry(final ZipOutputStream zos, final File file, final int bufferSize) throws IOException {
        if (log.isTraceEnabled()) log.trace(HelperLog.methodStart(zos, file, bufferSize));
        BufferedInputStream bis = null;
        final byte[] buffer = new byte[bufferSize];
        try {
            final ZipEntry entry = new ZipEntry(file.getPath() + (file.isDirectory() ? "/" : HelperString.EMPTY_STRING));
            zos.putNextEntry(entry);
            if (!file.isDirectory()) {
                bis = new BufferedInputStream(new FileInputStream(file));
                int offset;
                while (-1 != (offset = bis.read(buffer))) {
                    zos.write(buffer, 0, offset);
                }
            }
        } finally {
            if (null != bis) {
                bis.close();
            }
        }
        if (log.isTraceEnabled()) log.trace(HelperLog.methodExit());
    }

    private static void extractEntry(final ZipFile zipFile, final ZipEntry entry, final File destDir, final int bufferSize) throws IOException {
        if (log.isTraceEnabled()) log.trace(HelperLog.methodStart(zipFile, entry, destDir, bufferSize));
        final File file = new File(destDir, entry.getName());
        if (entry.isDirectory()) {
            file.mkdirs();
        } else {
            new File(file.getParent()).mkdirs();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            final byte[] buffer = new byte[bufferSize];
            try {
                bis = new BufferedInputStream(zipFile.getInputStream(entry));
                bos = new BufferedOutputStream(new FileOutputStream(file));
                int offset;
                while (-1 != (offset = bis.read(buffer))) {
                    bos.write(buffer, 0, offset);
                }
            } finally {
                if (null != bos) {
                    bos.close();
                }
                if (null != bis) {
                    bis.close();
                }
            }
        }
        if (log.isTraceEnabled()) log.trace(HelperLog.methodExit());
    }
}
