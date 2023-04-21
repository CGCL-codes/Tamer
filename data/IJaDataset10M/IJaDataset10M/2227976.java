package net.community.chest.apache.ant.helpers;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.windows.Attrib;
import org.apache.tools.ant.types.FileSet;

/**
 * Copyright 2007 as per GPLv2
 * 
 * <P>Performs some kind of manipulation on a file by reading it <U>entirely</U>
 * into the memory and then calling the (abstract) method {@link #replaceProperties(File, String)}
 * to executed the manipulation. The result is then compared (case
 * <U>sensitive</I>) with the target file contents. If the target file does
 * not exist, or the result is not the same as the target file, the {@link #setOverwrite(boolean)}
 * attribute is TRUE, then the target file is created/updated. If this is
 * required and the target file is marked as read-only, then it will be
 * overwritten only if {@link #setOverwriteReadOnly(boolean)} has been called
 * with TRUE (otherwise a {@link BuildException} is thrown).</P>
 * 
 * <P><U>Note:</U> that this base class has been designed to match as much as
 * possible the core <I>copy</I> task so that existing <I>copy</I> tasks can
 * be easily replaced with other tasks derived from this one.
 * 
 * @author Lyor G.
 * @since Jun 20, 2007 3:58:51 PM
 */
public abstract class InMemoryFileContentsReplacer extends AbstractFilesInputTask<InMemoryProcessingResult> {

    protected InMemoryFileContentsReplacer() {
        super();
    }

    /**
     * If TRUE then output file is re-generated even if nothing replaced
     */
    private boolean _overwrite;

    public boolean isOverwrite() {
        return _overwrite;
    }

    public void setOverwrite(final boolean overwrite) {
        _overwrite = overwrite;
    }

    /**
     * Expected charset in the file(s) - default="US-ASCII"
     */
    private String _encoding = "US-ASCII";

    public String getEncoding() {
        return _encoding;
    }

    public void setEncoding(final String charset) throws BuildException {
        if ((null == (_encoding = charset)) || (_encoding.length() <= 0)) throw new BuildException("Charset may NOT be null/empty", getLocation());
    }

    /**
     * @param inFile input file to read from
     * @return file contents as a string using the charset specified by {@link #getCharset()}
     * @throws IOException if unable to read the file or convert it to the specified charset
     */
    protected String readFileData(final File inFile) throws IOException {
        final long iSize = inFile.length();
        if ((iSize < 0L) || (iSize >= Integer.MAX_VALUE)) throw new FileNotFoundException("Bad/Illegal file size (" + iSize + ") for file=" + inFile.getAbsolutePath());
        if (0L == iSize) return "";
        final byte[] iData = new byte[(int) iSize];
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(inFile);
            final int iLen = fin.read(iData, 0, (int) iSize);
            if (iLen != iSize) throw new EOFException("Mismatched (" + iLen + " out of " + iSize + " bytes) read data size for file=" + inFile.getAbsolutePath());
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } finally {
                    fin = null;
                }
            }
        }
        return new String(iData, 0, (int) iSize, getEncoding());
    }

    /**
     * @param inFile input file to which this data belongs
     * @param inData content of a <U>whole</U> file
     * @return replaced properties - same as input if nothing done
     * @throws IOException if unable to "read/write" to/from string (never...)
     * @throws BuildException if failed to parse the file
     */
    protected abstract String replaceProperties(final File inFile, final String inData) throws IOException, BuildException;

    /**
     * TRUE if OK to overwrite read-only files if contents need to be changed
     */
    private boolean _overwriteReadOnly;

    public void setOverwriteReadOnly(final boolean overwriteReadOnly) {
        _overwriteReadOnly = overwriteReadOnly;
    }

    public boolean isOverwriteReadOnly() {
        return _overwriteReadOnly;
    }

    /**
     * @param inFile input file path to be processed
     * @param outFile output file location - may be <U>same</U> as input
     * @return true if the output file has been created/updated
     * @throws IOException unable to access the file(s)
     * @throws BuildException internal build state error(s)
     */
    protected boolean processFile(final File inFile, final File outFile) throws IOException, BuildException {
        final long pStart = System.currentTimeMillis();
        if (isVerboseMode()) log("Processing " + inFile.getAbsolutePath(), getVerbosity());
        final String inString = readFileData(inFile), outString = replaceProperties(inFile, inString);
        boolean createOutputFile = (!outFile.exists()) || isOverwrite();
        if (!createOutputFile) {
            final String curOut = readFileData(outFile);
            if (!outString.equals(curOut)) createOutputFile = true;
        }
        if (createOutputFile) {
            {
                final File outDir = outFile.getParentFile();
                if ((!outDir.exists()) && (!outDir.mkdirs())) throw new BuildException("Cannot create output folder for file=" + outFile.getAbsolutePath(), getLocation());
            }
            if (outFile.exists() && (!outFile.canWrite())) {
                if (!isOverwriteReadOnly()) throw new BuildException("Not allowed to overwrite read-only file=" + outFile.getAbsolutePath(), getLocation());
                final Attrib at = new Attrib();
                at.setFile(outFile);
                at.setReadonly(false);
                at.setProject(getProject());
                at.execute();
                if (isVerboseMode()) log("Enabled write to read-only file=" + outFile.getAbsolutePath(), getVerbosity());
            }
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(outFile);
                fout.write(outString.getBytes(getEncoding()));
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } finally {
                        fout = null;
                    }
                }
            }
            if (isVerboseMode()) {
                if (inFile != outFile) log("Translated " + inFile.getAbsolutePath() + " => " + outFile.getAbsolutePath(), getVerbosity()); else log("Created/Generated " + outFile.getAbsolutePath(), getVerbosity());
            }
        }
        final long pEnd = System.currentTimeMillis(), pDiff = (pEnd - pStart);
        if (isVerboseMode()) log("Processed (in " + pDiff + " msec.) " + inFile.getAbsolutePath(), getVerbosity());
        return createOutputFile;
    }

    /**
     * If non-null, then the target where the file(s) are to be created - relatively
     */
    private File _toDir;

    public File getToDir() {
        return _toDir;
    }

    public void setToDir(final File targetDir) {
        _toDir = targetDir;
    }

    /**
     * If non-null then the target file - may be used ONLY with 'srcfile' set 
     */
    private File _toFile;

    public File getToFile() {
        return _toFile;
    }

    public void setToFile(final File targetFile) throws BuildException {
        if (getToDir() != null) throw new BuildException("Cannot specify 'targetfile' and 'targetdir' in same task", getLocation());
        final Vector<FileSet> filesets = getFilesets();
        final int numFilesets = (null == filesets) ? 0 : filesets.size();
        if (numFilesets > 0) throw new BuildException("Cannot specify 'targetfile' and 'fileset' in same task", getLocation());
        if (null == getFile()) throw new BuildException("Must specify 'srcfile' before 'targetfile'", getLocation());
        _toFile = targetFile;
    }

    @Override
    protected InMemoryProcessingResult processFileSets(final Collection<? extends FileSet> filesets) throws BuildException {
        final int numFilesets = (null == filesets) ? 0 : filesets.size();
        if (numFilesets <= 0) throw new BuildException("no filesets specified", getLocation());
        int totalProcessed = 0, totalChanged = 0;
        final File targetDir = getToDir();
        for (final FileSet fs : filesets) {
            if (null == fs) continue;
            final FileScanner scanner = fs.getDirectoryScanner(getProject());
            final String[] files = (null == scanner) ? null : scanner.getIncludedFiles();
            final int numFiles = (null == files) ? 0 : files.length;
            if (numFiles <= 0) continue;
            final File baseDir = scanner.getBasedir();
            for (final String fileName : files) {
                if ((null == fileName) || (fileName.length() <= 0)) continue;
                final File inFile = new File(baseDir, fileName), outFile = new File((targetDir != null) ? targetDir : baseDir, fileName);
                try {
                    if (processFile(inFile, outFile)) totalChanged++;
                    totalProcessed++;
                } catch (IOException ioe) {
                    throw new BuildException(ioe.getClass().getName() + " while processing file=" + inFile.getAbsolutePath() + ": " + ioe.getMessage(), getLocation());
                }
            }
        }
        return new InMemoryProcessingResult(totalProcessed, totalChanged);
    }

    @Override
    protected InMemoryProcessingResult processSingleFile(final File inFile) throws BuildException {
        File outFile = getToFile();
        if (null == outFile) {
            final File targetDir = getToDir();
            if (targetDir != null) outFile = new File(targetDir, inFile.getName()); else outFile = inFile;
        }
        try {
            final boolean changed = processFile(inFile, outFile);
            return new InMemoryProcessingResult(1, changed ? 1 : 0);
        } catch (IOException ioe) {
            throw new BuildException(ioe.getClass().getName() + " while processing file=" + inFile.getAbsolutePath() + ": " + ioe.getMessage(), getLocation());
        }
    }

    @Override
    protected InMemoryProcessingResult executeProcessing() throws BuildException {
        final InMemoryProcessingResult res = super.executeProcessing();
        if ((null == res) || (res.getNumProcessed() <= 0)) throw new BuildException("No files processed", getLocation());
        log("Updated/created " + res.getNumChanged() + " out of " + res.getNumProcessed() + " processed files", Project.MSG_INFO);
        return res;
    }
}
