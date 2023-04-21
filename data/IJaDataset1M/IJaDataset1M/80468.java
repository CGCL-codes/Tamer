package de.sendorian.app.forumArchive;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.sendorian.util.io.RecursiveFileListIterator;

/**
 * Creates ZIPs for each forum with all files of that forum contained. The ZIPs
 * are potentially splitted to be smaller than a defined size.
 * 
 * @author sendorian
 * 
 */
@Component
public class ZipCreator implements Runnable {

    private static final Logger LOG = Logger.getLogger(ZipCreator.class);

    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024;

    @Value("#{config.downloadDir}")
    private String downloadDir;

    @Autowired
    Config config;

    @Autowired
    private Cleaner cleaner;

    /**
	 * Filter that excludes the generated files ("downloaded files",
	 * "external links" etc.).
	 */
    private static FileFilter generatedFilesExclusionFilter = new FileFilter() {

        String[] excludes = new String[] { "downloaded files", "external links", "readme.txt" };

        @Override
        public boolean accept(File pathname) {
            for (String exclude : excludes) {
                if (pathname.getName().startsWith(exclude)) {
                    return false;
                }
            }
            return true;
        }
    };

    /**
	 * Cleans the download dir and creates ZIPs per forum with all files
	 * contained.
	 */
    @Override
    public void run() {
        cleaner.run();
        File fDownloadDir = new File(downloadDir);
        for (File dir : fDownloadDir.listFiles()) {
            if (dir.isDirectory()) {
                zipForumDir(dir);
            }
        }
        LOG.info("Finished creating ZIPs.");
    }

    /**
	 * Creates ZIPs for one forum with all files contained. The ZIPs are
	 * potentially splitted to be smaller than MAX_FILE_SIZE bytes.
	 */
    private void zipForumDir(File forumDir) {
        Iterator<File> it = new RecursiveFileListIterator(forumDir, generatedFilesExclusionFilter);
        List<File> files = new ArrayList<File>();
        files.addAll(getStandardFiles(forumDir));
        long size = getStandardFilesSize(forumDir);
        int zipPartNr = 1;
        while (it.hasNext()) {
            File file = it.next();
            if (size + file.length() > MAX_FILE_SIZE) {
                LOG.debug("ZIP part " + zipPartNr + " reached " + size + " bytes. Creating ZIP and proceeding with file collection for next ZIP.");
                zipFiles(files, forumDir, zipPartNr);
                zipPartNr++;
                files = new ArrayList<File>();
                files.addAll(getStandardFiles(forumDir));
                size = getStandardFilesSize(forumDir);
            }
            files.add(file);
            size += file.length();
        }
        if (zipPartNr == 1) zipPartNr = 0;
        zipFiles(files, forumDir, zipPartNr);
    }

    /**
	 * Returns the default files that should be contained in every zip.
	 */
    private List<File> getStandardFiles(File forumDir) {
        List<File> files = new ArrayList<File>();
        for (File file : forumDir.listFiles()) {
            if (!generatedFilesExclusionFilter.accept(file)) {
                files.add(file);
            }
        }
        return files;
    }

    /**
	 * Returns the size of the default files that should be contained in every
	 * zip.
	 */
    private long getStandardFilesSize(File forumDir) {
        List<File> files = getStandardFiles(forumDir);
        long size = 0;
        for (File file : files) {
            size += file.length();
        }
        return size;
    }

    /**
	 * Creates a zip for all the given files.
	 */
    private void zipFiles(List<File> files, File forumDir, int zipPartNr) {
        String zipPart = zipPartNr == 0 ? "" : "_" + zipPartNr;
        String zipName = config.getZipNameForForum(forumDir.getName()) + zipPart;
        String outFilename = forumDir.getParent() + File.separator + zipName + ".zip";
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (File file : files) {
                try {
                    in = new FileInputStream(file);
                    String relativeFileName = getRelativeFileName(file, forumDir);
                    LOG.debug("Adding " + relativeFileName + " to " + zipName + ".zip");
                    out.putNextEntry(new ZipEntry(zipName + "/" + relativeFileName));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                } catch (IOException e) {
                    LOG.error("Error adding " + file + " to " + outFilename, e);
                } finally {
                    try {
                        out.closeEntry();
                        in.close();
                    } catch (IOException e) {
                        LOG.error("Error closing FileInputStream for " + file, e);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Error creating " + outFilename, e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOG.error("Error closing Filestream for " + outFilename, e);
            }
        }
    }

    /**
	 * Returns the relative file name as which this file schould be stored in
	 * the zip.
	 * 
	 * Files in the root of the forum dir will be returned as files in the
	 * topmost folder in the zip. Files in thread dirs will be returned as files
	 * in folders named with these thread names.
	 */
    private String getRelativeFileName(File file, File forumDir) {
        if (forumDir.equals(file.getParentFile())) {
            return file.getName();
        } else {
            return file.getParentFile().getName() + "/" + file.getName();
        }
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
}
