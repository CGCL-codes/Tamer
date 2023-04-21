package com.tonicsystems.jarjar.util;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.JarMarker;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipOutputStream;

public abstract class AntJarProcessor extends Jar {

    private EntryStruct struct = new EntryStruct();

    private JarProcessor proc;

    private byte[] buf = new byte[0x2000];

    private Set<String> dirs = new HashSet<String>();

    private boolean filesOnly;

    protected boolean verbose;

    private static final ZipExtraField[] JAR_MARKER = new ZipExtraField[] { JarMarker.getInstance() };

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public abstract void execute() throws BuildException;

    public void execute(JarProcessor proc) throws BuildException {
        this.proc = proc;
        super.execute();
    }

    public void setFilesonly(boolean f) {
        super.setFilesonly(f);
        filesOnly = f;
    }

    protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode) throws IOException {
    }

    protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtil.pipe(is, baos, buf);
        struct.data = baos.toByteArray();
        struct.name = vPath;
        struct.time = lastModified;
        if (proc.process(struct)) {
            if (mode == 0) mode = ZipFileSet.DEFAULT_FILE_MODE;
            if (!filesOnly) {
                addParentDirs(struct.name, zOut);
            }
            super.zipFile(new ByteArrayInputStream(struct.data), zOut, struct.name, struct.time, fromArchive, mode);
        }
    }

    private void addParentDirs(String file, ZipOutputStream zOut) throws IOException {
        int slash = file.lastIndexOf('/');
        if (slash >= 0) {
            String dir = file.substring(0, slash);
            if (dirs.add(dir)) {
                addParentDirs(dir, zOut);
                super.zipDir((File) null, zOut, dir + "/", ZipFileSet.DEFAULT_DIR_MODE, JAR_MARKER);
            }
        }
    }

    public void reset() {
        super.reset();
        cleanHelper();
    }

    protected void cleanUp() {
        super.cleanUp();
        cleanHelper();
    }

    protected void cleanHelper() {
        verbose = false;
        filesOnly = false;
        dirs.clear();
    }
}
