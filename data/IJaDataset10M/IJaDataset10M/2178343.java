package uk.co.q3c.deplan.util.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class Q3cFileUtil {

    protected static final transient Logger logger = Logger.getLogger(Q3cFileUtil.class.getName());

    private static final String defaultFormat = "yyyyMMddHHmm";

    private static final String secondsIncluded = "yyyyMMddHHmmss";

    /**
	 * Returns a backup filename with ""_<datetime>" appended to the filename.
	 * The extension remains the same. The path is dropped, so just the filename
	 * and extension is returned
	 * 
	 * @param sourceDbFileName
	 * @return
	 */
    public static String backupFileName(String sourceDbFileName, boolean includeSeconds) {
        String cleanFileName = FilenameUtils.normalizeNoEndSeparator(sourceDbFileName);
        String fileName = FilenameUtils.removeExtension(cleanFileName);
        File sourceDbFile = new File(sourceDbFileName);
        Date d = new Date();
        d.setTime(sourceDbFile.lastModified());
        SimpleDateFormat sdf;
        if (includeSeconds) {
            sdf = new SimpleDateFormat(secondsIncluded);
        } else {
            sdf = new SimpleDateFormat(defaultFormat);
        }
        String sourceFileDate = sdf.format(d);
        String backupFN = FilenameUtils.getBaseName(fileName) + "_" + sourceFileDate + "." + FilenameUtils.getExtension(cleanFileName);
        return backupFN;
    }

    public static String backupFileName(String sourceDbFileName) {
        return backupFileName(sourceDbFileName, false);
    }

    /**
	 * Makes a backup copy of the specified <code>sourceFileName</code> to the
	 * <code>targetDirectory</code> using the backup file naming convention
	 * provided by {@link Q3cFileUtil#backupFileName(String)}
	 * 
	 * @param sourceFileName
	 * @param targetDirectory
	 * @throws IOException
	 */
    public static void makeBackupCopy(String sourceFileName, String targetDirectory) throws IOException {
        String td = FilenameUtils.normalizeNoEndSeparator(targetDirectory);
        String targetFileName = Q3cFileUtil.backupFileName(sourceFileName);
        File targetPath = new File(td + File.separator + targetFileName);
        if (logger.isInfoEnabled()) {
            logger.info("backup file is " + targetFileName);
        }
        FileUtils.copyFile(new File(sourceFileName), targetPath);
    }

    /**
	 * Makes a backup copy of the specified <code>sourceFileName</code> to the
	 * same directory using the backup file naming convention provided by
	 * {@link Q3cFileUtil#backupFileName(String)}
	 * 
	 * @param sourceFileName
	 * @throws IOException
	 */
    public static void makeBackupCopy(String sourceFileName) throws IOException {
        String targetDir = FilenameUtils.getFullPathNoEndSeparator(sourceFileName);
        if (logger.isInfoEnabled()) {
            logger.info("Making backup copy from " + sourceFileName + " to directory" + targetDir);
        }
        makeBackupCopy(sourceFileName, targetDir);
    }

    public static String currentDirectory() {
        File f = new File(".");
        try {
            return f.getCanonicalPath();
        } catch (IOException e) {
            return null;
        }
    }

    public static String fileCopyName(String filename) {
        return FilenameUtils.getFullPathNoEndSeparator(filename) + File.separator + "copy of " + FilenameUtils.getBaseName(filename) + FilenameUtils.EXTENSION_SEPARATOR_STR + FilenameUtils.getExtension(filename);
    }

    public static void makeFileCopy(String filename) throws IOException {
        FileUtils.copyFile(new File(filename), new File(fileCopyName(filename)));
    }

    public static String changeExtension(String fileName, String newExtension) {
        String path = FilenameUtils.getFullPath(fileName);
        String baseName = FilenameUtils.getBaseName(fileName);
        String newfilename = path + baseName + FilenameUtils.EXTENSION_SEPARATOR + newExtension;
        return newfilename;
    }
}
