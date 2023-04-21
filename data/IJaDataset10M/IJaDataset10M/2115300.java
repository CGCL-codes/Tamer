package net.sf.fc.io;

import static net.sf.fc.TestUtil.assertFiles;
import static net.sf.fc.TestUtil.createFile;
import static net.sf.fc.TestUtil.createFiles;
import static net.sf.fc.TestUtil.createLvlDir;
import static net.sf.fc.TestUtil.createSrcFile;
import static net.sf.fc.TestUtil.deleteTestFiles;
import static net.sf.fc.TestUtil.getDstDirectory;
import static net.sf.fc.TestUtil.getSrcDirectory;
import static net.sf.fc.TestUtil.getFileCopyEventListener;
import static net.sf.fc.TestUtil.assertRenamedFilesExist;
import static net.sf.fc.TestUtil.assertRenamedFiles;
import static net.sf.fc.TestUtil.assertDirectories;
import static net.sf.fc.TestUtil.assertFlattenedDirectories;
import static net.sf.fc.TestUtil.createFlatDirs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.fc.io.FileCopier.FileType;
import net.sf.fc.io.ch.FileCopyHelper;
import net.sf.fc.io.ch.OverwriteFileCopyHelper;
import net.sf.fc.io.ch.RenameAppendTimestampFileCopyHelper;
import net.sf.fc.io.ch.SafeRenameFileCopyHelper;
import net.sf.fc.io.ch.UpdateRenameFileCopyHelper;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileCopierTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCopyFileToDir() {
        System.out.println("testCopyFileToDir");
        File srcFile;
        try {
            srcFile = createSrcFile("srcFile1", 4096);
            File dstDir = getDstDirectory();
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFileToDirectory(srcFile, dstDir);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, new File(dstDir, "srcFile1"));

        } catch (IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToNonexistantDir() {
        System.out.println("testCopyFileToNonexistantDir");
        File srcFile;
        try {
            srcFile = createSrcFile("srcFile1", 4096);
            File dstDir = new File(getDstDirectory(), "lvl1DstDirLvl");
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFileToDirectory(srcFile, dstDir);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, new File(dstDir, "srcFile1"));

        } catch (IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToNonexistantFile() {
        System.out.println("testCopyFileToNonexistantFile");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFile);

        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToExistingFileNoCopy() {
        System.out.println("testCopyFileToExistingFileNoCopy");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            dstFile = createFile(dstFile, 2048, true);
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = false;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);

        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToExistingFileOverwrite() {
        System.out.println("testCopyFileToExistingFileOverwrite");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            dstFile = createFile(dstFile, 2048, true);
            FileCopier instance = new FileCopier(new Options.Builder().fileCopyHelper(new OverwriteFileCopyHelper()).replaceExisting(true).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFile);

        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToExistingFileTimestampRename() {
        System.out.println("testCopyFileToExistingFileTimestampRename");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            dstFile = createFile(dstFile, 2048, true);
            FileCopier instance = new FileCopier(new Options.Builder().fileCopyHelper(new RenameAppendTimestampFileCopyHelper()).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFile);

            assertRenamedFilesExist(srcFile, dstFile, "\\.\\d{8}_\\d{6}", ".", 1);

        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToExistingFileTimestampRenameUpdateOnly() {
        System.out.println("testCopyFileToExistingFileTimestampRenameUpdateOnly");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            dstFile = createFile(dstFile, 2048, true);
            FileCopyHelper updateRenameFileCopyHelper = new UpdateRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            FileCopier instance = new FileCopier(new Options.Builder().fileCopyHelper(updateRenameFileCopyHelper).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());

            // Set the srcFile's last modified time to 1 second before the dstFile's last modified time
            // so we know it will be earlier than the destFile's last modified time and the call to copy will fail.
            srcFile.setLastModified(dstFile.lastModified()-1000);

            boolean expResult = false;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);

            // Set the srcFile's last modified time to 1 second after the dstFile's last modified time
            // so we know it will be later than the destFile's last modified time and the call to copy will succeed.
            srcFile.setLastModified(dstFile.lastModified()+1000);
            expResult = true;
            result = instance.copyFile(srcFile, dstFile);

            assertFiles(true, srcFile, dstFile);

            assertRenamedFilesExist(srcFile, dstFile, "\\.\\d{8}_\\d{6}", ".", 1);

        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToExistingFileSafeTimestampRename() {
        System.out.println("testCopyFileToExistingFileSafeTimestampRename");
        try {
            File srcFile = createSrcFile("srcFile1", 4096);
            File dstFile = new File(getDstDirectory(), "dstFile1");
            dstFile = createFile(dstFile, 2048, true);
            FileCopyHelper fileCopyHelper = new SafeRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            FileCopier instance = new FileCopier(new Options.Builder().fileCopyHelper(fileCopyHelper).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            boolean expResult = true;
            boolean result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFile);

            assertRenamedFilesExist(srcFile, dstFile, "\\.\\d{8}_\\d{6}", ".", 1);

            result = instance.copyFile(srcFile, dstFile);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFile);

            assertRenamedFilesExist(srcFile, dstFile, "\\.\\d{8}_\\d{6}", ".", 2);
        } catch(IOException e) {
            assertTrue(false);
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectory() {
        System.out.println("testCopyDirectory");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);
        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryFlattenFirstLevel() {
        System.out.println("testCopyDirectoryFlattenFirstLevel");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopier instance = new FileCopier(new Options.Builder().maxDirFlattenLevel(1).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            assertFlattenedDirectories(srcDir, dstDir, true);
            srcDir = new File(srcDir, "lvl4Dir");
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryMergeFirstLevel() {
        System.out.println("testCopyDirectoryMergeFirstLevel");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopyHelper fileCopyHelper = new SafeRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            FileCopier instance = new FileCopier(new Options.Builder().maxDirMergeLevel(1).fileCopyHelper(fileCopyHelper).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);

            // Copy again. The first level directory should be merged.
            result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            dstDir = new File(dstDir, srcDir.getName());
            assertDirectories(srcDir, dstDir, true, 1, 1, Pattern.compile("lvl5Dir"),
                    true, "\\.\\d{8}_\\d{6}", ".", 1);
            srcDir = new File(srcDir, "lvl4Dir");
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);
        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryFlattenFilter() {
        System.out.println("testCopyDirectoryFlattenFilter");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopier instance = new FileCopier(new Options.Builder().dirFlattenFilter(new NameFileFilter("lvl3Dir")).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true, 0, Pattern.compile("lvl3Dir"), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryMergeFilter() {
        System.out.println("testCopyDirectoryMergeFilter");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopyHelper fileCopyHelper = new SafeRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            FileCopier instance = new FileCopier(new Options.Builder().dirMergeFilter(new NameFileFilter("lvl2Dir")).fileCopyHelper(fileCopyHelper).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);

            // Copy again. The lvl2Dir should be merged.
            result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            dstDir = new File(dstDir, srcDir.getName());
            assertDirectories(srcDir, dstDir, true, 3);

            srcDir = new File(new File(new File(srcDir, "lvl4Dir"), "lvl3Dir"), "lvl2Dir");
            dstDir = new File(new File(new File(dstDir, "lvl4Dir"), "lvl3Dir"), "lvl2Dir");
            assertDirectories(srcDir, dstDir, true, 1, 1, Pattern.compile("lvl2Dir"),
                    true, "\\.\\d{8}_\\d{6}", ".", 1);

            srcDir = new File(srcDir, "lvl1Dir");
            dstDir = new File(dstDir, "lvl1Dir");
            assertDirectories(srcDir, dstDir, true, 0);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryCopyFirstTwoLevels() {
        System.out.println("testCopyDirectoryCopyFirstTwoLevels");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 10;
            FileCopier instance = new FileCopier(new Options.Builder().maxDirCopyLevel(2).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            dstDir = new File(dstDir, srcDir.getName());
            assertDirectories(srcDir, dstDir, true, 1, 2);

            dstDir = new File(new File(dstDir, "lvl4Dir"), "lvl3Dir");
            assertTrue(dstDir.exists() == false);
        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryCopyFileDirFilter() {
        System.out.println("testCopyDirectoryCopyFileDirFilter");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 8;
            // Copy only the file1 and file2 in lvl2Dir through lvl5Dir.
            List<IOFileFilter> filters = new ArrayList<>();
            filters.add(DirectoryFileFilter.DIRECTORY);
            filters.add(new RegexFileFilter("^lvl[12345]\\.file[12]$"));
            IOFileFilter copyFileFilter = new OrFileFilter(filters);

            filters = new ArrayList<>();
            filters.add(FileFileFilter.FILE);
            filters.add(new RegexFileFilter("^lvl[5432]Dir$"));
            IOFileFilter copyDirFilter = new OrFileFilter(filters);

            IOFileFilter copyFilter = new AndFileFilter(copyFileFilter, copyDirFilter);

            FileCopier instance = new FileCopier(new Options.Builder().fileCopyFilter(copyFilter).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            dstDir = new File(dstDir, srcDir.getName());
            dstDir = new File(new File(new File(dstDir, "lvl4Dir"), "lvl3Dir"), "lvl2Dir");

            srcLvlDirs = srcLvlDirs.subList(1, srcLvlDirs.size());
            int dirLvl = 2;
            for(File sDir : srcLvlDirs) {
                for(i = 1; i <= 2; i++) {
                    String fileNm = "lvl" + dirLvl + ".file" + i;
                    assertFiles(true, new File(sDir, fileNm), new File(dstDir, fileNm));
                }
                dstDir = dstDir.getParentFile();
                dirLvl++;
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryCopyFileFilter() {
        System.out.println("testCopyDirectoryCopyFileFilter");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);
            File dstDir = getDstDirectory();

            int expResult = 5;
            // Copy only the files in the lvl1Dir.
            List<IOFileFilter> filters = new ArrayList<>();
            filters.add(DirectoryFileFilter.DIRECTORY);
            filters.add(new RegexFileFilter("^lvl1\\.file[12345]$"));
            IOFileFilter copyFilter = new OrFileFilter(filters);
            FileCopier instance = new FileCopier(new Options.Builder().fileCopyFilter(copyFilter).build());
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDir);
            assertEquals(expResult, result);
            dstDir = new File(dstDir, srcDir.getName());
            dstDir = new File(new File(new File(new File(dstDir, "lvl4Dir"), "lvl3Dir"), "lvl2Dir"), "lvl1Dir");
            assertDirectories(srcLvlDirs.get(0), dstDir, true, 1, 4);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryToDirectoryList() {
        System.out.println("testCopyDirectoryToDirectoryList");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, false);

            int expResult = 125;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstDirs);
            assertEquals(expResult, result);

            for(File dstDir : dstDirs) {
                assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryToDirectoryMap() {
        System.out.println("testCopyDirectoryToDirectoryMap");
        try {
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 5);
            // Put 5 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 5);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);

            Map<Options, List<File>> dstMap = new HashMap<>();
            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, false);
            Options opt = Options.createDefaultOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 6, 5, false);
            opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(Options.createOverwriteOptions(), dstDirs);

            int expResult = 250;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectory(srcDir, dstMap);
            assertEquals(expResult, result);

            for(Options o : dstMap.keySet()) {
                for(File dstDir : dstMap.get(o)) {
                    assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);
                }
            }

            // Copy it again. This time, the expected result should be half since the first list of destinations use
            // the SafeFileCopyHelper which does not copy if the file already exists.
            expResult = 125;
            result = instance.copyDirectory(srcDir, dstMap);
            assertEquals(expResult, result);

            for(Options o : dstMap.keySet()) {
                for(File dstDir : dstMap.get(o)) {
                    assertDirectories(srcDir, new File(dstDir, srcDir.getName()), true);
                }
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToFilesList() {
        System.out.println("testCopyFileToFilesList");
        try {
            File srcFile = new File(getSrcDirectory(), "srcFile1");
            createFile(srcFile, 4096);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, true);
            List<File> dstFiles = new ArrayList<>(dstDirs.size());
            int i = 1;
            for(File dstDir : dstDirs) {
                dstFiles.add(new File(dstDir, "dstFile" + i));
                i++;
            }

            int expResult = 5;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFileToFiles(srcFile, dstFiles);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFiles.toArray(new File[0]));

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToFilesMap() {
        System.out.println("testCopyFileToFilesMap");
        try {
            File srcFile = new File(getSrcDirectory(), "srcFile1");
            createFile(srcFile, 4096);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, true);

            List<File> dstFiles = new ArrayList<>(dstDirs.size());

            int i = 1;
            for(File dstDir : dstDirs) {
                dstFiles.add(new File(dstDir, "dstFile" + i));
                i++;
            }

            Map<Options, List<File>> dstFilesMap = new HashMap<>();
            dstFilesMap.put(null, dstFiles);

            dstFiles = new ArrayList<>(dstDirs.size());

            for(File dstDir : dstDirs) {
                dstFiles.add(new File(dstDir, "dstFile" + i));
                i++;
            }
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstFilesMap.put(opt, dstFiles);

            int expResult = 10;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFileToFiles(srcFile, dstFilesMap);
            assertEquals(expResult, result);

            for(Options o: dstFilesMap.keySet()) {
                assertFiles(true, srcFile, dstFilesMap.get(o).toArray(new File[0]));
            }

            // Copy again. This time the expected result is 5 since only 5 of the destination files use the OverwriteFileCopyHelper.
            expResult = 5;
            result = instance.copyFileToFiles(srcFile, dstFilesMap);
            assertEquals(expResult, result);

            for(Options o: dstFilesMap.keySet()) {
                assertFiles(true, srcFile, dstFilesMap.get(o).toArray(new File[0]));
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToDirectoryList() {
        System.out.println("testCopyFileToDirectoryList");
        try {
            File srcFile = new File(getSrcDirectory(), "srcFile1");
            createFile(srcFile, 4096);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, true);

            int expResult = 5;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFileToDirectories(srcFile, dstDirs);
            assertEquals(expResult, result);
            List<File> dstFiles = new ArrayList<>(dstDirs.size());
            for(File dstDir : dstDirs) {
                dstFiles.add(new File(dstDir, "srcFile1"));
            }
            assertFiles(true, srcFile, dstFiles.toArray(new File[0]));

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileToDirectoryMap() {
        System.out.println("testCopyFileToDirectoryMap");
        try {
            File srcFile = new File(getSrcDirectory(), "srcFile1");
            createFile(srcFile, 4096);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5, false);
            Map<Options, List<File>> dstMap = new HashMap<>();
            dstMap.put(null, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 6, 5, false);
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, dstDirs);

            int expResult = 10;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFileToDirectories(srcFile, dstMap);
            assertEquals(expResult, result);

            for(Options o : dstMap.keySet()) {
                List<File> dstFiles = new ArrayList<>(dstDirs.size());
                for(File dstDir : dstMap.get(o)) {
                    dstFiles.add(new File(dstDir, "srcFile1"));
                }
                assertFiles(true, srcFile, dstFiles.toArray(new File[0]));
            }

            expResult = 5;
            result = instance.copyFileToDirectories(srcFile, dstMap);
            assertEquals(expResult, result);

            for(Options o : dstMap.keySet()) {
                List<File> dstFiles = new ArrayList<>(dstDirs.size());
                for(File dstDir : dstMap.get(o)) {
                    dstFiles.add(new File(dstDir, "srcFile1"));
                }
                assertFiles(true, srcFile, dstFiles.toArray(new File[0]));
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileListToDirectory() {
        System.out.println("testCopyFileListToDirectory");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 10);
            File dstDir = getDstDirectory();

            int expResult = 10;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectory(srcFiles, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileMapToDirectory() {
        System.out.println("testCopyFileMapToDirectory");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 10);
            Map<Options, List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcFiles);

            srcFiles = createFiles(getSrcDirectory(), 11, 10);
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcFiles);

            File dstDir = getDstDirectory();

            int expResult = 20;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectory(srcMap, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

            expResult = 10;
            result = instance.copyFilesToDirectory(srcMap, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryList() {
        System.out.println("testCopyDirectoryList");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 5);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            File dstDir = getDstDirectory();

            int expResult = 25;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcDirs, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryMap() {
        System.out.println("testCopyDirectoryMap");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 5);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Map<Options, List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcDirs);

            srcDirs = createFlatDirs(getSrcDirectory(), 6, 5);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcDirs);

            File dstDir = getDstDirectory();

            int expResult = 50;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcMap, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

            expResult = 25;
            result = instance.copyDirectories(srcMap, dstDir);
            assertEquals(expResult, result);
            assertDirectories(getSrcDirectory(), getDstDirectory(), true);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryListList() {
        System.out.println("testCopyDirectoryListList");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            int expResult = 20;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcDirs, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryMapList() {
        System.out.println("testCopyDirectoryMapList");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Map<Options, List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcDirs);

            srcDirs = createFlatDirs(getSrcDirectory(), 3, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcDirs);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            int expResult = 40;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcMap, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

            expResult = 20;
            instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            result = instance.copyDirectories(srcMap, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryListMap() {
        System.out.println("testCopyDirectoryListMap");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);
            Map<Options, List<File>> dstMap = new HashMap<>();
            dstMap.put(null, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 3, 2);
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, dstDirs);

            int expResult = 40;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcDirs, dstMap);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

            expResult = 20;
            instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            result = instance.copyDirectories(srcDirs, dstMap);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyDirectoryMapMap() {
        System.out.println("testCopyDirectoryMapMap");
        try {
            List<File> srcDirs = createFlatDirs(getSrcDirectory(), 1, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Map<Options, List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcDirs);

            srcDirs = createFlatDirs(getSrcDirectory(), 3, 2);
            for(File srcDir : srcDirs) {
                createFiles(srcDir, 5);
            }
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcDirs);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);
            Map<Options, List<File>> dstMap = new HashMap<>();
            dstMap.put(null, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 3, 2);
            opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, dstDirs);

            int expResult = 80;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyDirectories(srcMap, dstMap);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

            expResult = 60;
            instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            result = instance.copyDirectories(srcMap, dstMap);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileListToDirectoryList() {
        System.out.println("testCopyFileListToDirectoryList");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 10);
            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5);

            int expResult = 50;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectories(srcFiles, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileMapToDirectoryList() {
        System.out.println("testCopyFileMapToDirectoryList");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 5);
            Map<Options,List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcFiles);

            srcFiles = createFiles(getSrcDirectory(), 6, 5);
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcFiles);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5);

            int expResult = 50;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectories(srcMap, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

            expResult = 25;
            result = instance.copyFilesToDirectories(srcMap, dstDirs);
            assertEquals(expResult, result);
            for(File dstDir : dstDirs) {
                assertDirectories(getSrcDirectory(), dstDir, true);
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileListToDirectoryMap() {
        System.out.println("testCopyFileListToDirectoryMap");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 5);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5);
            Map<Options,List<File>> dstMap = new HashMap<>();
            dstMap.put(null, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 6, 5);
            Options opt = Options.createOverwriteOptions();
            dstMap.put(opt, dstDirs);

            int expResult = 50;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectories(srcFiles, dstMap);
            assertEquals(expResult, result);
            for(Options o : dstMap.keySet()) {
                for(File dDir : dstMap.get(o)) {
                    assertDirectories(getSrcDirectory(), dDir, true);
                }
            }

            expResult = 25;
            result = instance.copyFilesToDirectories(srcFiles, dstMap);
            assertEquals(expResult, result);
            for(Options o : dstMap.keySet()) {
                for(File dDir : dstMap.get(o)) {
                    assertDirectories(getSrcDirectory(), dDir, true);
                }
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFileMapToDirectoryMap() {
        System.out.println("testCopyFileMapToDirectoryMap");
        try {
            List<File> srcFiles = createFiles(getSrcDirectory(), 5);
            Map<Options,List<File>> srcMap = new HashMap<>();
            srcMap.put(null, srcFiles);

            srcFiles = createFiles(getSrcDirectory(), 6, 5);
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            srcMap.put(opt, srcFiles);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 5);
            Map<Options,List<File>> dstMap = new HashMap<>();
            dstMap.put(null, dstDirs);

            dstDirs = createFlatDirs(getDstDirectory(), 6, 5);
            opt = new Options.Builder().fileCopyHelper(new RenameAppendTimestampFileCopyHelper()).build();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, dstDirs);

            int expResult = 100;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFilesToDirectories(srcMap, dstMap);
            assertEquals(expResult, result);
            for(Options o : dstMap.keySet()) {
                for(File dDir : dstMap.get(o)) {
                    assertDirectories(getSrcDirectory(), dDir, true);
                }
            }

            expResult = 75;
            result = instance.copyFilesToDirectories(srcMap, dstMap);
            assertEquals(expResult, result);
            for(Options o : dstMap.keySet()) {
                for(File dDir : dstMap.get(o)) {
                    assertDirectories(getSrcDirectory(), dDir, true);
                    if(dDir.getName().matches("flat([6789]|10)Dir")) {
                        File [] sFiles = getSrcDirectory().listFiles();
                        for(File sFile : sFiles) {
                            assertRenamedFiles(true, sFile, new File(dDir, sFile.getName()), "\\.\\d{8}_\\d{6}", ".", 1);
                        }
                    }
                }
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFile1() {
        System.out.println("testCopyFile1");
        try {
            File srcFile = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            File dstFile = new File(getDstDirectory(), "dstFile1");
            List<File> dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile2");
            dstFiles.add(dstFile);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            Map<FileCopier.FileType,List<File>> dstMap = new HashMap<>();
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            int expResult = 4;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFile1(srcFile, dstMap);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFiles.get(0), dstFiles.get(1),
                    new File(dstDirs.get(0), "srcFile1"), new File(dstDirs.get(1), "srcFile1"));

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFile2() {
        System.out.println("testCopyFile2");
        try {
            File srcFile = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            File dstFile = new File(getDstDirectory(), "dstFile1");
            List<File> dstFilesTmp = new ArrayList<>();
            dstFilesTmp.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile2");
            dstFilesTmp.add(dstFile);
            List<File> dstFiles = new ArrayList<>();
            dstFiles.addAll(dstFilesTmp);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            Map<FileCopier.FileType,List<File>> fileTypeMap = new HashMap<>();
            fileTypeMap.put(FileType.File, dstFilesTmp);
            fileTypeMap.put(FileType.Directory, dstDirs);
            Map<Options,Map<FileCopier.FileType,List<File>>> dstMap = new HashMap<>();
            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstMap.put(opt, fileTypeMap);

            dstFile = new File(getDstDirectory(), "dstFile3");
            dstFilesTmp = new ArrayList<>();
            dstFilesTmp.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile4");
            dstFilesTmp.add(dstFile);
            FileCopyHelper fileCopyHelper = new RenameAppendTimestampFileCopyHelper();
            fileCopyHelper.addFileCopyEventListener(getFileCopyEventListener());
            opt = new Options.Builder().fileCopyHelper(fileCopyHelper).build();
            fileTypeMap = new HashMap<>();
            fileTypeMap.put(FileType.File, dstFilesTmp);
            dstMap.put(opt, fileTypeMap);
            dstFiles.addAll(dstFilesTmp);

            int expResult = 6;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFile2(srcFile, dstMap);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFiles.get(0), dstFiles.get(1), dstFiles.get(2), dstFiles.get(3),
                    new File(dstDirs.get(0), "srcFile1"), new File(dstDirs.get(1), "srcFile1"));

            result = instance.copyFile2(srcFile, dstMap);
            assertEquals(expResult, result);
            assertFiles(true, srcFile, dstFiles.get(0), dstFiles.get(1), dstFiles.get(2), dstFiles.get(3),
                    new File(dstDirs.get(0), "srcFile1"), new File(dstDirs.get(1), "srcFile1"));

            assertRenamedFiles(true, srcFile, dstFiles.get(2), "\\.\\d{8}_\\d{6}", ".", 1);
            assertRenamedFiles(true, srcFile, dstFiles.get(3), "\\.\\d{8}_\\d{6}", ".", 1);

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFiles1() {
        System.out.println("testCopyFiles1");
        try {
            File srcFile1 = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            File dstFile = new File(getDstDirectory(), "dstFile1");
            List<File> dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile2");
            dstFiles.add(dstFile);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            Map<FileCopier.FileType,List<File>> dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            Map<File,Map<FileCopier.FileType,List<File>>> filesMap = new HashMap<>();
            filesMap.put(srcFile1, dstMap);

            File srcFile2 = createFile(new File(getSrcDirectory(), "srcFile2"), 512);

            dstFile = new File(getDstDirectory(), "dstFile3");
            dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile4");
            dstFiles.add(dstFile);

            dstDirs = createFlatDirs(getDstDirectory(), 3, 2);

            dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            filesMap.put(srcFile2, dstMap);

            int expResult = 8;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFiles1(filesMap);
            assertEquals(expResult, result);

            Map<FileCopier.FileType,List<File>> dstFilesMap = filesMap.get(srcFile1);
            List<File> dDirs = dstFilesMap.get(FileType.Directory);
            List<File> dFiles = dstFilesMap.get(FileType.File);

            assertFiles(true, srcFile1, dFiles.get(0), dFiles.get(1),
                    new File(dDirs.get(0), "srcFile1"), new File(dDirs.get(1), "srcFile1"));

            dstFilesMap = filesMap.get(srcFile2);
            dDirs = dstFilesMap.get(FileType.Directory);
            dFiles = dstFilesMap.get(FileType.File);

            assertFiles(true, srcFile2, dFiles.get(0), dFiles.get(1),
                    new File(dDirs.get(0), "srcFile2"), new File(dDirs.get(1), "srcFile2"));

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFiles2() {
        System.out.println("testCopyFiles2");
        try {
            File srcFile1 = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            File dstFile = new File(getDstDirectory(), "dstFile1");
            List<File> dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile2");
            dstFiles.add(dstFile);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            Map<FileCopier.FileType,List<File>> dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            Map<File,Map<FileCopier.FileType,List<File>>> filesMap = new HashMap<>();
            filesMap.put(srcFile1, dstMap);

            File srcFile2 = createFile(new File(getSrcDirectory(), "srcFile2"), 512);

            dstFile = new File(getDstDirectory(), "dstFile3");
            dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile4");
            dstFiles.add(dstFile);

            dstDirs = createFlatDirs(getDstDirectory(), 3, 2);

            dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            filesMap.put(srcFile2, dstMap);

            Map<Options,Map<File,Map<FileCopier.FileType,List<File>>>> optionsMap = new HashMap<>();
            optionsMap.put(null, filesMap);

            File srcFile3 = createFile(new File(getSrcDirectory(), "srcFile3"), 15360);

            dstFile = new File(getDstDirectory(), "dstFile5");
            dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile6");
            dstFiles.add(dstFile);
            dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            filesMap = new HashMap<>();
            filesMap.put(srcFile3, dstMap);

            File srcFile4 = createFile(new File(getSrcDirectory(), "srcFile4"), 12288);
            dstFile = new File(getDstDirectory(), "dstFile7");
            dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile8");
            dstFiles.add(dstFile);

            dstDirs = createFlatDirs(getDstDirectory(), 3, 2);

            dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            filesMap.put(srcFile4, dstMap);

            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            optionsMap.put(opt, filesMap);

            int expResult = 16;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFiles2(optionsMap);
            assertEquals(expResult, result);

            for(Options o : optionsMap.keySet()) {
                filesMap = optionsMap.get(o);
                if(o == null) {
                    Map<FileCopier.FileType,List<File>> dstFilesMap = filesMap.get(srcFile1);
                    List<File> dDirs = dstFilesMap.get(FileType.Directory);
                    List<File> dFiles = dstFilesMap.get(FileType.File);

                    assertFiles(true, srcFile1, dFiles.get(0), dFiles.get(1),
                            new File(dDirs.get(0), "srcFile1"), new File(dDirs.get(1), "srcFile1"));

                    dstFilesMap = filesMap.get(srcFile2);
                    dDirs = dstFilesMap.get(FileType.Directory);
                    dFiles = dstFilesMap.get(FileType.File);

                    assertFiles(true, srcFile2, dFiles.get(0), dFiles.get(1),
                            new File(dDirs.get(0), "srcFile2"), new File(dDirs.get(1), "srcFile2"));
                } else {
                    Map<FileCopier.FileType,List<File>> dstFilesMap = filesMap.get(srcFile3);
                    List<File> dDirs = dstFilesMap.get(FileType.Directory);
                    List<File> dFiles = dstFilesMap.get(FileType.File);

                    assertFiles(true, srcFile3, dFiles.get(0), dFiles.get(1),
                            new File(dDirs.get(0), "srcFile3"), new File(dDirs.get(1), "srcFile3"));

                    dstFilesMap = filesMap.get(srcFile4);
                    dDirs = dstFilesMap.get(FileType.Directory);
                    dFiles = dstFilesMap.get(FileType.File);

                    assertFiles(true, srcFile4, dFiles.get(0), dFiles.get(1),
                            new File(dDirs.get(0), "srcFile4"), new File(dDirs.get(1), "srcFile4"));
                }
            }


        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFiles3() {
        System.out.println("testCopyFiles3");
        try {
            File srcFile1 = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            File dstFile = new File(getDstDirectory(), "dstFile1");
            List<File> dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile2");
            dstFiles.add(dstFile);

            List<File> dstDirs = createFlatDirs(getDstDirectory(), 1, 2);

            Map<FileCopier.FileType,List<File>> dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);
            dstMap.put(FileType.Directory, dstDirs);

            Map<Options,Map<FileCopier.FileType,List<File>>> dstOptionsMap = new HashMap<>();
            dstOptionsMap.put(null, dstMap);

            dstFile = new File(getDstDirectory(), "dstFile3");
            dstFiles = new ArrayList<>();
            dstFiles.add(dstFile);
            dstFile = new File(getDstDirectory(), "dstFile4");
            dstFiles.add(dstFile);

            dstMap = new EnumMap<>(FileCopier.FileType.class);
            dstMap.put(FileType.File, dstFiles);

            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstOptionsMap.put(opt, dstMap);

            Map<File,Map<Options,Map<FileCopier.FileType,List<File>>>> filesMap = new HashMap<>();
            filesMap.put(srcFile1, dstOptionsMap);

            int expResult = 6;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFiles3(filesMap);
            assertEquals(expResult, result);

            for(File f : filesMap.keySet()) {
                dstOptionsMap = filesMap.get(f);
                for(Options o : dstOptionsMap.keySet()) {
                    dstMap = dstOptionsMap.get(o);
                    for(FileType fileType : dstMap.keySet()) {
                        List<File> files = new ArrayList<>();
                        if(fileType.equals(FileType.File)) {
                            files = dstMap.get(fileType);
                        } else {
                            List<File> dirs = dstMap.get(fileType);
                            for(File dir : dirs) {
                                files.add(new File(dir, srcFile1.getName()));
                            }
                        }
                        assertFiles(true, srcFile1, files.toArray(new File[0]));
                    }
                }
            }

            expResult = 2;
            instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            result = instance.copyFiles3(filesMap);
            assertEquals(expResult, result);

            for(File f : filesMap.keySet()) {
                dstOptionsMap = filesMap.get(f);
                for(Options o : dstOptionsMap.keySet()) {
                    dstMap = dstOptionsMap.get(o);
                    for(FileType fileType : dstMap.keySet()) {
                        List<File> files = new ArrayList<>();
                        if(fileType.equals(FileType.File)) {
                            files = dstMap.get(fileType);
                        } else {
                            List<File> dirs = dstMap.get(fileType);
                            for(File dir : dirs) {
                                files.add(new File(dir, srcFile1.getName()));
                            }
                        }
                        assertFiles(true, srcFile1, files.toArray(new File[0]));
                    }
                }
            }
        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }

    @Test
    public void testCopyFiles4() {
        System.out.println("testCopyFiles4");
        try {

            // The first source file is just a file.
            File srcFile1 = createFile(new File(getSrcDirectory(), "srcFile1"), 10240);

            Map<FileType,List<File>> dstMap = new EnumMap<>(FileType.class);
            List<File> dstFiles = new ArrayList<>();
            dstFiles.add(new File(getDstDirectory(), "dstRenameFile1"));
            dstFiles.add(new File(getDstDirectory(), "dstRenameFile2"));
            dstMap.put(FileType.File, dstFiles);

            Map<Options,Map<FileCopier.FileType,List<File>>> dstOptionsMap = new HashMap<>();
            dstOptionsMap.put(null, dstMap);

            dstMap = new EnumMap<>(FileType.class);
            dstFiles = new ArrayList<>();
            dstFiles.add(new File(getDstDirectory(), "dstOverwriteFile1"));
            dstFiles.add(new File(getDstDirectory(), "dstOverwriteFile2"));
            dstMap.put(FileType.File, dstFiles);

            Options opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            dstOptionsMap.put(opt, dstMap);

            Map<File,Map<Options,Map<FileCopier.FileType,List<File>>>> srcFilesMap = new HashMap<>();
            srcFilesMap.put(srcFile1, dstOptionsMap);

            Map<Options,Map<File,Map<Options,Map<FileCopier.FileType,List<File>>>>> filesMap = new HashMap<>();
            FileCopyHelper fileCopyHelper = new SafeRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            fileCopyHelper.addFileCopyEventListener(getFileCopyEventListener());
            opt = new Options.Builder().fileCopyHelper(fileCopyHelper).build();
            filesMap.put(opt, srcFilesMap);

            // The second source file is a hierarchical directory.
            List<File> srcLvlDirs = createLvlDir(getSrcDirectory(), 10);
            // Put 3 files in each level.
            int i = 1;
            for(File dir: srcLvlDirs) {
                createFiles(dir, "lvl" + (i++) + ".file", 3);
            }
            File srcDir = srcLvlDirs.get(srcLvlDirs.size()-1);

            dstMap = new EnumMap<>(FileType.class);
            dstFiles = new ArrayList<>();
            dstFiles.add(new File(getDstDirectory(), "dstOverwriteFlattenedDir"));
            dstMap.put(FileType.Directory, dstFiles);

            dstOptionsMap = new HashMap<>();
            opt = Options.createOverwriteOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            opt = opt.setMaxDirFlattenLevel(Options.FLATTEN_ALL_DIRS);
            dstOptionsMap.put(opt, dstMap);

            dstMap = new EnumMap<>(FileType.class);
            dstFiles = new ArrayList<>();
            dstFiles.add(new File(getDstDirectory(), "dstMergeDir"));
            dstMap.put(FileType.Directory, dstFiles);

            fileCopyHelper = new SafeRenameFileCopyHelper(new RenameAppendTimestampFileCopyHelper());
            fileCopyHelper.addFileCopyEventListener(getFileCopyEventListener());
            opt = new Options.Builder().fileCopyHelper(fileCopyHelper).maxDirMergeLevel(Options.MERGE_ALL_DIRS).build();
            dstOptionsMap.put(opt, dstMap);

            dstMap = new EnumMap<>(FileType.class);
            dstFiles = new ArrayList<>();
            dstFiles.add(new File(getDstDirectory(), "dstCopyAllDir"));
            dstMap.put(FileType.Directory, dstFiles);

            dstOptionsMap.put(null, dstMap);

            srcFilesMap = new HashMap<>();
            srcFilesMap.put(srcDir, dstOptionsMap);

            opt = Options.createDefaultOptions();
            opt.getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            filesMap.put(opt, srcFilesMap);

            int expResult = 94;
            FileCopier instance = new FileCopier();
            instance.getOptions().getFileCopyHelper().addFileCopyEventListener(getFileCopyEventListener());
            int result = instance.copyFiles4(filesMap);
            assertEquals(expResult, result);

            for(Options o : filesMap.keySet()) {
                srcFilesMap = filesMap.get(o);
                for(File s : srcFilesMap.keySet()) {
                    dstOptionsMap = srcFilesMap.get(s);
                    for(Options dstO : dstOptionsMap.keySet()) {
                        dstMap = dstOptionsMap.get(dstO);
                        for(FileType fileType : dstMap.keySet()) {
                            dstFiles = dstMap.get(fileType);
                            if(s.isFile()) {
                                assertFiles(true, s, dstFiles.toArray(new File[0]));
                            } else {
                                for(File dstDir : dstFiles) {
                                    if(dstDir.getName().equals("dstOverwriteFlattenedDir")) {
                                        assertFlattenedDirectories(s, dstDir, true);
                                    } else if(dstDir.getName().matches("dst(Merge|CopyAll)Dir")) {
                                        assertDirectories(s, new File(dstDir, s.getName()), true);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            expResult = 64;
            result = instance.copyFiles4(filesMap);
            assertEquals(expResult, result);

            for(Options o : filesMap.keySet()) {
                srcFilesMap = filesMap.get(o);
                for(File s : srcFilesMap.keySet()) {
                    dstOptionsMap = srcFilesMap.get(s);
                    for(Options dstO : dstOptionsMap.keySet()) {
                        dstMap = dstOptionsMap.get(dstO);
                        for(FileType fileType : dstMap.keySet()) {
                            dstFiles = dstMap.get(fileType);
                            if(s.isFile()) {
                                assertFiles(true, s, dstFiles.toArray(new File[0]));
                                for(File dFile : dstFiles) {
                                    if(dFile.getName().matches("dstRenameFile[12]")) {
                                        assertRenamedFiles(true, s, dFile, "\\.\\d{8}_\\d{6}", ".", 1);
                                    }
                                }
                            } else {
                                for(File dstDir : dstFiles) {
                                    if(dstDir.getName().equals("dstOverwriteFlattenedDir")) {
                                        assertFlattenedDirectories(s, dstDir, true);
                                    } else if(dstDir.getName().equals("dstCopyAllDir")) {
                                        assertDirectories(s, new File(dstDir, s.getName()), true);
                                    } else if(dstDir.getName().equals("dstMergeDir")) {
                                        assertDirectories(s, new File(dstDir, s.getName()), true, 0, 0, Pattern.compile(".*"), true,
                                                "\\.\\d{8}_\\d{6}", ".", 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch(IOException e) {
            assertTrue(false);
        } finally {
            deleteTestFiles();
        }
    }
}
