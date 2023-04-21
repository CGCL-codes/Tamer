package goldengate.common.file.filesystembased;

import goldengate.common.command.exception.CommandAbstractException;
import goldengate.common.command.exception.Reply550Exception;
import goldengate.common.command.exception.Reply553Exception;
import goldengate.common.digest.FilesystemBasedDigest;
import goldengate.common.file.AbstractDir;
import goldengate.common.file.FileInterface;
import goldengate.common.file.OptsMLSxInterface;
import goldengate.common.file.SessionInterface;
import goldengate.common.file.filesystembased.specific.FilesystemBasedCommonsIo;
import goldengate.common.file.filesystembased.specific.FilesystemBasedDirJdkAbstract;
import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * Directory implementation for Filesystem Based
 *
 * @author Frederic Bregier
 *
 */
public abstract class FilesystemBasedDirImpl extends AbstractDir {

    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory.getLogger(FilesystemBasedDirImpl.class);

    /**
     * Class that handles specifity of one Jdk or another
     */
    protected static FilesystemBasedDirJdkAbstract filesystemBasedFtpDirJdk = null;

    /**
     * Init the dependant object according to internals of JDK
     *
     * @param filesystemBasedFtpDirJdkChoice
     */
    public static void initJdkDependent(FilesystemBasedDirJdkAbstract filesystemBasedFtpDirJdkChoice) {
        filesystemBasedFtpDirJdk = filesystemBasedFtpDirJdkChoice;
        initWindowsSupport();
    }

    /**
     * @param session
     * @param optsMLSx
     */
    public FilesystemBasedDirImpl(SessionInterface session, OptsMLSxInterface optsMLSx) {
        this.session = session;
        this.optsMLSx = optsMLSx;
        this.optsMLSx.setOptsModify((byte) 1);
        this.optsMLSx.setOptsPerm((byte) 1);
        this.optsMLSx.setOptsSize((byte) 1);
        this.optsMLSx.setOptsType((byte) 1);
    }

    /**
     * Finds all files matching a wildcard expression (based on '?', '~' or
     * '*').
     *
     * @param pathWithWildcard
     *            The wildcard expression with a business path.
     * @return List of String as relative paths matching the wildcard
     *         expression. Those files are tested as valid from business point
     *         of view. If Wildcard support is not active, if the path contains
     *         any wildcards, it will throw an error.
     * @throws CommandAbstractException
     */
    protected List<String> wildcardFiles(String pathWithWildcard) throws CommandAbstractException {
        List<String> resultPaths = new ArrayList<String>();
        if (!(pathWithWildcard.contains("*") || pathWithWildcard.contains("?") || pathWithWildcard.contains("~"))) {
            if (getSession().getAuth().isBusinessPathValid(pathWithWildcard)) {
                resultPaths.add(pathWithWildcard);
            }
            return resultPaths;
        }
        if (!FilesystemBasedDirJdkAbstract.ueApacheCommonsIo) {
            throw new Reply553Exception("Wildcards in pathname is not allowed");
        }
        File wildcardFile;
        File rootFile;
        if (isAbsoluteWindows(pathWithWildcard)) {
            wildcardFile = new File(pathWithWildcard);
            rootFile = getCorrespondingRoot(wildcardFile);
        } else {
            rootFile = new File(((FilesystemBasedAuthImpl) getSession().getAuth()).getBaseDirectory());
            wildcardFile = new File(rootFile, pathWithWildcard);
        }
        List<String> subdirs = new ArrayList<String>();
        while (wildcardFile != null) {
            File parent = wildcardFile.getParentFile();
            if (parent == null) {
                subdirs.add(0, wildcardFile.getPath());
                break;
            }
            subdirs.add(0, wildcardFile.getName());
            if (parent.equals(rootFile)) {
                subdirs.add(0, parent.getPath());
                break;
            }
            wildcardFile = parent;
        }
        List<File> basedPaths = new ArrayList<File>();
        basedPaths.add(new File(subdirs.get(0)));
        int i = 1;
        while (i < subdirs.size()) {
            FileFilter fileFilter = FilesystemBasedCommonsIo.getWildcardFileFilter(subdirs.get(i));
            List<File> newBasedPaths = new ArrayList<File>();
            for (File dir : basedPaths) {
                if (dir.isDirectory()) {
                    for (File match : dir.listFiles(fileFilter)) {
                        newBasedPaths.add(match);
                    }
                }
            }
            basedPaths = newBasedPaths;
            i++;
        }
        for (File file : basedPaths) {
            String relativePath = ((FilesystemBasedAuthImpl) getSession().getAuth()).getRelativePath(normalizePath(file.getAbsolutePath()));
            String newpath = this.validatePath(relativePath);
            resultPaths.add(newpath);
        }
        return resultPaths;
    }

    /**
     * Get the FileInterface from this path, checking first its validity
     *
     * @param path
     * @return the FileInterface
     * @throws CommandAbstractException
     */
    protected File getFileFromPath(String path) throws CommandAbstractException {
        String newdir = validatePath(path);
        if (isAbsoluteWindows(newdir)) {
            return new File(newdir);
        }
        String truedir = ((FilesystemBasedAuthImpl) getSession().getAuth()).getAbsolutePath(newdir);
        return new File(truedir);
    }

    /**
     * Get the true file from the path
     *
     * @param path
     * @return the true File from the path
     * @throws CommandAbstractException
     */
    protected File getTrueFile(String path) throws CommandAbstractException {
        checkIdentify();
        String newpath = consolidatePath(path);
        List<String> paths = wildcardFiles(normalizePath(newpath));
        if (paths.size() != 1) {
            throw new Reply550Exception("File not found: " + paths.size() + " founds");
        }
        String extDir = paths.get(0);
        extDir = this.validatePath(extDir);
        File file = getFileFromPath(extDir);
        if (!file.isFile()) {
            throw new Reply550Exception("Path is not a file: " + path);
        }
        return file;
    }

    /**
     * Get the relative path (without mount point)
     *
     * @param file
     * @return the relative path
     */
    protected String getRelativePath(File file) {
        return ((FilesystemBasedAuthImpl) getSession().getAuth()).getRelativePath(normalizePath(file.getAbsolutePath()));
    }

    public boolean changeDirectory(String path) throws CommandAbstractException {
        checkIdentify();
        String newpath = consolidatePath(path);
        List<String> paths = wildcardFiles(newpath);
        if (paths.size() != 1) {
            logger.warn("CD error: {}", newpath);
            throw new Reply550Exception("Directory not found: " + paths.size() + " founds");
        }
        String extDir = paths.get(0);
        extDir = this.validatePath(extDir);
        if (isDirectory(extDir)) {
            currentDir = extDir;
            return true;
        }
        throw new Reply550Exception("Directory not found: " + extDir);
    }

    public String mkdir(String directory) throws CommandAbstractException {
        checkIdentify();
        String newdirectory = consolidatePath(directory);
        File dir = new File(newdirectory);
        String parent = dir.getParentFile().getPath();
        List<String> paths = wildcardFiles(normalizePath(parent));
        if (paths.size() != 1) {
            throw new Reply550Exception("Base Directory not found: " + paths.size() + " founds");
        }
        String newDir = paths.get(0) + SEPARATOR + dir.getName();
        newDir = this.validatePath(newDir);
        File newdir = getFileFromPath(newDir);
        if (newdir.mkdir()) {
            return newDir;
        }
        throw new Reply550Exception("Cannot create directory " + newDir);
    }

    public String rmdir(String directory) throws CommandAbstractException {
        checkIdentify();
        String newdirectory = consolidatePath(directory);
        List<String> paths = wildcardFiles(normalizePath(newdirectory));
        if (paths.size() != 1) {
            throw new Reply550Exception("Directory not found: " + paths.size() + " founds");
        }
        String extDir = paths.get(0);
        extDir = this.validatePath(extDir);
        File dir = getFileFromPath(extDir);
        if (dir.delete()) {
            return extDir;
        }
        throw new Reply550Exception("Cannot delete directory " + extDir);
    }

    public boolean isDirectory(String path) throws CommandAbstractException {
        checkIdentify();
        File dir = getFileFromPath(path);
        return dir.isDirectory();
    }

    public boolean isFile(String path) throws CommandAbstractException {
        checkIdentify();
        return getFileFromPath(path).isFile();
    }

    public String getModificationTime(String path) throws CommandAbstractException {
        checkIdentify();
        File file = getFileFromPath(path);
        if (file.exists()) {
            return getModificationTime(file);
        }
        throw new Reply550Exception("\"" + path + "\" does not exist");
    }

    /**
     * Return the Modification time for the File
     *
     * @param file
     * @return the Modification time as a String YYYYMMDDHHMMSS.sss
     */
    protected String getModificationTime(File file) {
        long mstime = file.lastModified();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mstime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int ms = calendar.get(Calendar.MILLISECOND);
        StringBuilder sb = new StringBuilder(18);
        sb.append(year);
        if (month < 10) {
            sb.append(0);
        }
        sb.append(month);
        if (day < 10) {
            sb.append(0);
        }
        sb.append(day);
        if (hour < 10) {
            sb.append(0);
        }
        sb.append(hour);
        if (minute < 10) {
            sb.append(0);
        }
        sb.append(minute);
        if (second < 10) {
            sb.append(0);
        }
        sb.append(second);
        sb.append('.');
        if (ms < 10) {
            sb.append(0);
        }
        if (ms < 100) {
            sb.append(0);
        }
        sb.append(ms);
        return sb.toString();
    }

    public List<String> list(String path) throws CommandAbstractException {
        checkIdentify();
        String newpath = path;
        if (newpath == null || newpath.length() == 0) {
            newpath = currentDir;
        }
        if (newpath.startsWith("-a") || newpath.startsWith("-A")) {
            String[] args = newpath.split(" ");
            if (args.length > 1) {
                newpath = args[1];
            } else {
                newpath = currentDir;
            }
        }
        newpath = consolidatePath(newpath);
        List<String> paths = wildcardFiles(newpath);
        if (paths.isEmpty()) {
            throw new Reply550Exception("No files found");
        }
        List<String> newPaths = new ArrayList<String>();
        for (String file : paths) {
            File dir = getFileFromPath(file);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    String[] files = dir.list();
                    for (String finalFile : files) {
                        String relativePath = ((FilesystemBasedAuthImpl) getSession().getAuth()).getRelativePath(finalFile);
                        newPaths.add(relativePath);
                    }
                } else {
                    newPaths.add(file);
                }
            }
        }
        return newPaths;
    }

    public List<String> listFull(String path, boolean lsFormat) throws CommandAbstractException {
        checkIdentify();
        boolean listAllFiles = false;
        String newpath = path;
        if (newpath == null || newpath.length() == 0) {
            newpath = currentDir;
        }
        if (newpath.startsWith("-a") || newpath.startsWith("-A")) {
            String[] args = newpath.split(" ");
            if (args.length > 1) {
                newpath = args[1];
            } else {
                newpath = currentDir;
            }
            listAllFiles = true;
        }
        newpath = consolidatePath(newpath);
        List<String> paths = wildcardFiles(newpath);
        if (paths.isEmpty()) {
            throw new Reply550Exception("No files found");
        }
        List<String> newPaths = new ArrayList<String>();
        for (String file : paths) {
            File dir = getFileFromPath(file);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    for (File finalFile : files) {
                        if (lsFormat) {
                            newPaths.add(lsInfo(finalFile));
                        } else {
                            newPaths.add(mlsxInfo(finalFile));
                        }
                    }
                } else {
                    if (lsFormat) {
                        newPaths.add(lsInfo(dir));
                    } else {
                        newPaths.add(mlsxInfo(dir));
                    }
                }
            }
        }
        if (listAllFiles) {
            File dir = new File(getFileFromPath(newpath), SEPARATOR + "..");
            if (lsFormat) {
                newPaths.add(lsInfo(dir));
            } else {
                newPaths.add(mlsxInfo(dir));
            }
        }
        return newPaths;
    }

    public String fileFull(String path, boolean lsFormat) throws CommandAbstractException {
        checkIdentify();
        String newpath = consolidatePath(path);
        List<String> paths = wildcardFiles(normalizePath(newpath));
        if (paths.size() != 1) {
            throw new Reply550Exception("No files found " + paths.size() + " founds");
        }
        File file = getFileFromPath(paths.get(0));
        if (file.exists()) {
            if (lsFormat) {
                return "Listing of \"" + paths.get(0) + "\"\n" + lsInfo(file) + "\nEnd of listing";
            }
            return "Listing of \"" + paths.get(0) + "\"\n" + mlsxInfo(file) + "\nEnd of listing";
        }
        return "No file with name \"" + path + "\"";
    }

    /**
     * Decide if Full time or partial time as in 'ls' command
     *
     * @return True if Full Time, False is Default (as in 'ls' command)
     */
    protected boolean isFullTime() {
        return false;
    }

    /**
     *
     * @param file
     * @return the ls format information
     */
    protected String lsInfo(File file) {
        StringBuilder builder = new StringBuilder();
        builder.append((file.isDirectory() ? 'd' : '-'));
        builder.append((file.canRead() ? 'r' : '-'));
        builder.append((file.canWrite() ? 'w' : '-'));
        if (filesystemBasedFtpDirJdk != null) builder.append(filesystemBasedFtpDirJdk.canExecute(file) ? 'x' : '-'); else builder.append('-');
        builder.append("---");
        builder.append("---");
        builder.append(' ');
        builder.append("1 ");
        builder.append("anybody\t");
        builder.append("anygroup\t");
        builder.append(file.length());
        builder.append('\t');
        long lastmod = file.lastModified();
        String fmt = null;
        long currentTime = System.currentTimeMillis();
        if (currentTime > lastmod + 6L * 30L * 24L * 60L * 60L * 1000L || currentTime < lastmod - 60L * 60L * 1000L) {
            fmt = "MMM dd  yyyy";
        } else {
            fmt = "MMM dd HH:mm";
        }
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.ENGLISH);
        dateFormat.applyPattern(fmt);
        builder.append(dateFormat.format(new Date(lastmod)));
        builder.append('\t');
        builder.append(file.getName());
        return builder.toString();
    }

    /**
     *
     * @param file
     * @return the MLSx information: ' Fact=facts;...; filename'
     */
    protected String mlsxInfo(File file) {
        StringBuilder builder = new StringBuilder(" ");
        if (getOptsMLSx().getOptsSize() == 1) {
            builder.append("Size=");
            builder.append(file.length());
            builder.append(';');
        }
        if (getOptsMLSx().getOptsModify() == 1) {
            builder.append("Modify=");
            builder.append(this.getModificationTime(file));
            builder.append(';');
        }
        if (getOptsMLSx().getOptsType() == 1) {
            builder.append("Type=");
            try {
                if (getFileFromPath(currentDir).equals(file)) {
                    builder.append("cdir");
                } else {
                    if (file.isDirectory()) {
                        builder.append("dir");
                    } else {
                        builder.append("file");
                    }
                }
            } catch (CommandAbstractException e) {
                if (file.isDirectory()) {
                    builder.append("dir");
                } else {
                    builder.append("file");
                }
            }
            builder.append(';');
        }
        if (getOptsMLSx().getOptsPerm() == 1) {
            builder.append("Perm=");
            if (file.isFile()) {
                if (file.canWrite()) {
                    builder.append('a');
                    builder.append('d');
                    builder.append('f');
                    builder.append('w');
                }
                if (file.canRead()) {
                    builder.append('r');
                }
            } else {
                if (file.canWrite()) {
                    builder.append('c');
                    try {
                        if (this.validatePath(file) != null) {
                            builder.append('d');
                            builder.append('m');
                            builder.append('p');
                        }
                    } catch (CommandAbstractException e) {
                    }
                }
                if (file.canRead()) {
                    builder.append('l');
                    builder.append('e');
                }
            }
            builder.append(';');
        }
        builder.append(' ');
        builder.append(file.getName());
        return builder.toString();
    }

    public long getFreeSpace() throws CommandAbstractException {
        checkIdentify();
        File directory = getFileFromPath(currentDir);
        if (filesystemBasedFtpDirJdk != null) return filesystemBasedFtpDirJdk.getFreeSpace(directory); else return Integer.MAX_VALUE;
    }

    public FileInterface setUniqueFile() throws CommandAbstractException {
        checkIdentify();
        File file = null;
        try {
            file = File.createTempFile(getSession().getAuth().getUser(), this.session.getUniqueExtension(), getFileFromPath(currentDir));
        } catch (IOException e) {
            throw new Reply550Exception("Cannot create unique file");
        }
        String currentFile = getRelativePath(file);
        return newFile(normalizePath(currentFile), false);
    }

    public boolean canRead() throws CommandAbstractException {
        checkIdentify();
        return getFileFromPath(currentDir).canRead();
    }

    public boolean canWrite() throws CommandAbstractException {
        checkIdentify();
        File file = getFileFromPath(currentDir);
        return file.canWrite();
    }

    public boolean exists() throws CommandAbstractException {
        checkIdentify();
        return getFileFromPath(currentDir).exists();
    }

    public long getCRC(String path) throws CommandAbstractException {
        File file = getTrueFile(path);
        try {
            CheckedInputStream cis = null;
            try {
                cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
            } catch (FileNotFoundException e) {
                throw new Reply550Exception("File not found: " + path);
            }
            byte[] buf = new byte[session.getBlockSize()];
            while (cis.read(buf) >= 0) {
            }
            return cis.getChecksum().getValue();
        } catch (IOException e) {
            throw new Reply550Exception("Error while reading file: " + path);
        }
    }

    public byte[] getMD5(String path) throws CommandAbstractException {
        File file = getTrueFile(path);
        try {
            if (FilesystemBasedFileParameterImpl.useNio) {
                return FilesystemBasedDigest.getHashMd5Nio(file);
            }
            return FilesystemBasedDigest.getHashMd5(file);
        } catch (IOException e1) {
            throw new Reply550Exception("Error while reading file: " + path);
        }
    }

    public byte[] getSHA1(String path) throws CommandAbstractException {
        File file = getTrueFile(path);
        try {
            if (FilesystemBasedFileParameterImpl.useNio) {
                return FilesystemBasedDigest.getHashSha1Nio(file);
            }
            return FilesystemBasedDigest.getHashSha1(file);
        } catch (IOException e1) {
            throw new Reply550Exception("Error while reading file: " + path);
        }
    }
}
