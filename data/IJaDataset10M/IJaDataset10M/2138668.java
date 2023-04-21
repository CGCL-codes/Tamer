package org.expasy.jpl.tools.qm.params;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.expasy.jpl.commons.collection.ExtraIterable;

/**
 * Get from
 * http://massapi.com/class/org/apache/commons/compress/compressors/gzip
 * /GzipCompressorInputStream.java.html
 * 
 * @version 1.0.0
 * 
 */
public class ZipExtractor implements ExtraIterable<File> {

    private static String TMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    private String archPath;

    /** the directory to extract files */
    private File targetDir;

    private InputStream is;

    private ArchiveInputStream ais;

    private ArchiveEntry waitingArchEntry;

    private ZipExtractor(String archPath, File targetDir) throws IOException {
        this.targetDir = targetDir;
        setArchPath(archPath);
    }

    public static ZipExtractor newInstance(String archPath) throws IOException {
        return new ZipExtractor(archPath, new File(TMP_DIR));
    }

    public static ZipExtractor newInstance(String archPath, File targetDir) throws IOException {
        return new ZipExtractor(archPath, targetDir);
    }

    /**
	 * @return the archPath
	 */
    public String getArchPath() {
        return archPath;
    }

    /**
	 * @param archPath the archPath to set
	 * @throws IOException
	 */
    public void setArchPath(String archPath) throws IOException {
        this.archPath = archPath;
        this.is = new FileInputStream(archPath);
        String lowerPath = archPath.toLowerCase();
        if (lowerPath.endsWith(".zip")) {
            this.ais = new ZipArchiveInputStream(is);
        } else if (lowerPath.endsWith(".tar.gz")) {
            this.ais = new TarArchiveInputStream(new GzipCompressorInputStream(is));
        } else {
            throw new IllegalArgumentException("no extractor for archive " + archPath);
        }
    }

    public static boolean isSupported(String filename) {
        filename = filename.toLowerCase();
        if (filename.endsWith(".zip") || filename.endsWith(".tar.gz")) {
            return true;
        }
        return false;
    }

    /**
	 * @return the targetDir
	 */
    public File getTargetDir() {
        return targetDir;
    }

    /**
	 * @param targetDir the targetDir to set
	 */
    public void setTargetDir(File targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public AbstractExtraIterator<File> iterator() {
        return new Iterator();
    }

    public final class Iterator extends AbstractExtraIterator<File> {

        public File next() {
            File file = null;
            if (hasNext()) {
                file = nextEntry();
                waitingArchEntry = null;
            }
            return file;
        }

        public File nextEntry() {
            try {
                while (hasNext()) {
                    String name = waitingArchEntry.getName();
                    name = name.substring(name.indexOf("/") + 1);
                    File file = new File(targetDir.getAbsolutePath() + "/" + name);
                    if (waitingArchEntry.isDirectory()) {
                        file.mkdirs();
                        waitingArchEntry = ais.getNextEntry();
                    } else {
                        OutputStream os = new FileOutputStream(file);
                        try {
                            IOUtils.copy(ais, os);
                        } finally {
                            IOUtils.closeQuietly(os);
                        }
                        return file;
                    }
                }
            } catch (IOException e) {
                return null;
            }
            return null;
        }

        @Override
        public final boolean hasNext() {
            if (waitingArchEntry == null) {
                try {
                    waitingArchEntry = ais.getNextEntry();
                } catch (IOException e) {
                    System.err.println("problem getting next entry: " + e.getMessage());
                }
            }
            return waitingArchEntry != null;
        }
    }
}
