package net.sf.jpkgmk.prototype;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import net.sf.jpkgmk.PackageException;
import net.sf.jpkgmk.PkgInfo;
import net.sf.jpkgmk.prototype.AbstractPrototypeEntry;
import net.sf.jpkgmk.prototype.PrototypeBuilder;
import net.sf.jpkgmk.prototype.PrototypeEntry;
import net.sf.jpkgmk.prototype.PrototypeEntryType;
import net.sf.jpkgmk.util.FileUtil;
import net.sf.jpkgmk.util.FsTestCase;

public class PrototypeBuilderTest extends FsTestCase {

    private File basedir;

    private File pkgDir;

    private File sourceDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        basedir = super.getDirectory();
        pkgDir = new File(basedir, "packagedir");
        super.createDir(pkgDir);
        sourceDir = new File(basedir, "sourcedir");
        super.createDir(sourceDir);
    }

    public void testAddInstallationFile_HappyPath() throws Exception {
        File sourceFile = new File(sourceDir, "file1");
        FileUtil.writeFile(sourceFile, "my sourcefile 1 content");
        PrototypeBuilder builder = new PrototypeBuilder(pkgDir, "myUser", "myGroup");
        builder.addFile(sourceFile, PrototypeEntryType.I);
        List<PrototypeEntry> prototypeEntryList = builder.getPrototype().getPrototypeEntryList();
        assertEquals(1, prototypeEntryList.size());
        AbstractPrototypeEntry entry = (AbstractPrototypeEntry) prototypeEntryList.get(0);
        assertEquals(sourceFile.getName(), entry.getEntryPath());
        assertEquals(sourceFile.getAbsolutePath(), entry.getEntryPathSource());
        assertEquals(0, FileUtil.countFiles(pkgDir));
        builder.create();
        File prototypeFile = builder.getFile();
        assertTrue(prototypeFile.exists());
        String prototypeContent = FileUtil.readFile(prototypeFile);
        String expected = "i file1=" + sourceFile.getAbsolutePath() + PkgInfo.LINE_SEPARATOR;
        assertEquals(expected, prototypeContent);
    }

    public void testAddFile_HappyPath() throws Exception {
        File sourceFile = new File(sourceDir, "file1");
        FileUtil.writeFile(sourceFile, "my sourcefile 1 content");
        PrototypeBuilder builder = new PrototypeBuilder(pkgDir, "myUser", "myGroup");
        builder.addFile(sourceFile, PrototypeEntryType.F);
        List<PrototypeEntry> prototypeEntryList = builder.getPrototype().getPrototypeEntryList();
        assertEquals(1, prototypeEntryList.size());
        AbstractPrototypeEntry entry = (AbstractPrototypeEntry) prototypeEntryList.get(0);
        assertEquals(sourceFile.getName(), entry.getEntryPath());
        assertEquals(sourceFile.getAbsolutePath(), entry.getEntryPathSource());
        assertEquals(0, FileUtil.countFiles(pkgDir));
        builder.create();
        File prototypeFile = builder.getFile();
        assertTrue(prototypeFile.exists());
        String prototypeContent = FileUtil.readFile(prototypeFile);
        String expected = "f none file1=" + sourceFile.getAbsolutePath() + " 0440 myUser myGroup" + PkgInfo.LINE_SEPARATOR;
        assertEquals(expected, prototypeContent);
    }

    public void testAddFile_EntryPathAlreadyExists() throws Exception {
        File sourceSubDir = new File(sourceDir, "sourceSubDir");
        super.createDir(sourceSubDir);
        File sourceFile = new File(sourceDir, "file1");
        FileUtil.writeFile(sourceFile, "my sourcefile 1 content");
        File sourceSubDirFile = new File(sourceSubDir, "file1");
        FileUtil.writeFile(sourceSubDirFile, "my sourcefile subdir 1 content");
        PrototypeBuilder builder = new PrototypeBuilder(pkgDir, "myUser", "myGroup");
        builder.addFile(sourceFile, PrototypeEntryType.F);
        List<PrototypeEntry> prototypeEntryList = builder.getPrototype().getPrototypeEntryList();
        assertEquals(1, prototypeEntryList.size());
        try {
            builder.addFile(sourceSubDirFile, PrototypeEntryType.F);
            fail("Should not be able to add a prototype entry with a path that already exists.");
        } catch (PackageException expected) {
            String expectedText = "The prototype already contains an entry that equals the given one";
            assertTrue(expected.getMessage().startsWith(expectedText));
        }
    }

    public void testAddDirectory_HappyPath() throws Exception {
        File sourceSubDir = new File(sourceDir, "sourceSubDir");
        super.createDir(sourceSubDir);
        File sourceFile = new File(sourceDir, "file1");
        FileUtil.writeFile(sourceFile, "my sourcefile 1 content");
        File sourceSubDirFile = new File(sourceSubDir, "fileInSubdir1");
        FileUtil.writeFile(sourceSubDirFile, "my sourcefile subdir 1 content");
        PrototypeBuilder builder = new PrototypeBuilder(pkgDir, "myUser", "myGroup");
        builder.addDirectory(this.sourceDir);
        List<PrototypeEntry> prototypeEntryList = builder.getPrototype().getPrototypeEntryList();
        assertEquals(3, prototypeEntryList.size());
        builder.create();
        File prototypeFile = builder.getFile();
        assertTrue(prototypeFile.exists());
        String prototypeContent = FileUtil.readFile(prototypeFile);
        String expected = "d none " + sourceSubDir.getName() + " 0550 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "f none " + sourceSubDir.getName() + "/fileInSubdir1=" + sourceSubDirFile.getAbsolutePath() + " 0440 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "f none file1=" + sourceFile.getAbsolutePath() + " 0440 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "";
        assertEquals(expected, prototypeContent);
    }

    public void testAddDirectoryWithDifferentTargetPath_HappyPath() throws Exception {
        File sourceFile = new File(sourceDir, "file1");
        FileUtil.writeFile(sourceFile, "my sourcefile 1 content");
        File sourceSubDir = new File(sourceDir, "sourceSubDir");
        super.createDir(sourceSubDir);
        File sourceSubDirFile = new File(sourceSubDir, "fileInSubdir1");
        FileUtil.writeFile(sourceSubDirFile, "my sourcefile subdir 1 content");
        PrototypeBuilder builder = new PrototypeBuilder(pkgDir, "myUser", "myGroup");
        String targetPathDir = "differentTargetPathDir";
        builder.addDirectory(this.sourceDir, targetPathDir);
        List<PrototypeEntry> prototypeEntryList = builder.getPrototype().getPrototypeEntryList();
        assertEquals(4, prototypeEntryList.size());
        StringWriter writer = new StringWriter();
        builder.getPrototype().writeContent(writer);
        String result = writer.toString();
        String expected = "d none " + targetPathDir + " 0550 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "d none " + targetPathDir + "/" + sourceSubDir.getName() + " 0550 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "f none " + targetPathDir + "/" + sourceSubDir.getName() + "/fileInSubdir1=" + sourceSubDirFile.getAbsolutePath() + " 0440 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "f none " + targetPathDir + "/file1=" + sourceFile.getAbsolutePath() + " 0440 myUser myGroup" + PkgInfo.LINE_SEPARATOR + "";
        assertEquals(expected, result);
    }
}
