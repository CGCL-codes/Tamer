package de.svenjacobs.invertm3u;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Inverts a M3U playlist.
 */
public class M3UInverter {

    public static final String VERSION = "1.0";

    private static final Logger logger = Logger.getLogger(M3UInverter.class);

    private FilenameFilter filenameFilter;

    private boolean absolutePath;

    public M3UInverter() {
        setFilenameFilter(new SuffixFilenameFilter(new String[] { "mp3", "ogg" }));
        setAbsolutePath(false);
    }

    public FilenameFilter getFilenameFilter() {
        return this.filenameFilter;
    }

    public void setFilenameFilter(final FilenameFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
    }

    public boolean isAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(boolean absolutePath) {
        this.absolutePath = absolutePath;
    }

    /***
   * Inverts the specified M3U playlist.
   * 
   * @param m3u M3U playlist
   * @param workingDir Working directory with media files
   * @param writer Target writer for inverted M3U
   * @throws java.io.IOException
   */
    public void invert(final M3U m3u, final String workingDir, final Writer writer) throws IOException {
        String normalizedWorkingDir = FilenameUtils.separatorsToSystem(workingDir);
        if (normalizedWorkingDir.endsWith(File.separator)) {
            normalizedWorkingDir = normalizedWorkingDir.substring(0, normalizedWorkingDir.length() - 1);
        }
        logger.debug(String.format("Working directory is \"%s\"", normalizedWorkingDir));
        File dir = new File(normalizedWorkingDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Working directory does not exist");
        }
        BufferedWriter buffWriter = new BufferedWriter(writer);
        try {
            logger.info("Scanning working directory for MP3 files");
            ArrayList<File> files = new ArrayList<File>();
            getRecurseFiles(files, dir);
            logger.info("Building inverted M3U file");
            for (File f : files) {
                String normalizedFilename = normalizeFilename(f.getPath(), normalizedWorkingDir);
                if (!m3u.contains(normalizedFilename)) {
                    logger.info(String.format("Adding file \"%s\" to inverted M3U", f.getPath()));
                    if (isAbsolutePath()) {
                        buffWriter.write(FilenameUtils.normalize(f.getAbsolutePath()));
                    } else {
                        buffWriter.write(normalizedFilename);
                    }
                    buffWriter.newLine();
                } else {
                    logger.debug(String.format("NOT adding file \"%s\" to inverted M3U", f.getPath()));
                }
            }
        } finally {
            buffWriter.flush();
            buffWriter.close();
        }
    }

    /***
   * Inverts the specified M3U playlist.
   * 
   * @param m3u M3U playlist
   * @param workingDir Working directory with media files
   * @param outFile Filename of inverted M3U
   * @throws java.io.IOException
   */
    public void invert(final M3U m3u, final String workingDir, final String outFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outFile);
        try {
            invert(m3u, workingDir, fileWriter);
        } catch (IOException ex) {
            throw ex;
        } finally {
            fileWriter.close();
        }
    }

    /***
   * Gets all files from a directory recursively.
   * 
   * @param files Target collection of File instances
   * @param dir Source directory
   */
    private void getRecurseFiles(final ArrayList<File> files, final File dir) {
        File[] currFiles = dir.listFiles(getFilenameFilter());
        for (File f : currFiles) {
            if (f.isDirectory()) {
                getRecurseFiles(files, f);
            } else {
                logger.info(String.format("Found file \"%s\"", f.getPath()));
                files.add(f);
            }
        }
    }

    /***
   * Removes working directory from filename.
   * 
   * @param filename Source filename
   * @param workingDir Working directory
   * @return Filename without working directory
   */
    private String normalizeFilename(final String filename, final String workingDir) {
        String normalized = filename;
        if (filename.startsWith(workingDir + File.separator)) {
            normalized = filename.substring(workingDir.length() + 1);
        }
        return normalized;
    }
}
