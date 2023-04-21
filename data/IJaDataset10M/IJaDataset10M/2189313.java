package com.tg.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.tg.filehandler.FileHandler;
import com.tg.filehandler.TgCommonFile;
import com.tg.filehandler.TgFile;

public class TestFileHandler {

    private FileHandler fh;

    public TestFileHandler() {
        this.fh = new FileHandler();
    }

    @Test
    public void testGetShortName() {
        String shortName = this.fh.getShortName("Helloworld.txt", "test");
        assertEquals(shortName, "Helloworld");
        shortName = this.fh.getShortName("Helloworld.txt.java", "test");
        assertEquals(shortName, "Helloworld.txt");
        shortName = this.fh.getShortName(".txt.java", "test");
        assertEquals(shortName, ".txt");
        shortName = this.fh.getShortName(".java", "test");
        assertEquals(shortName, "test");
        shortName = this.fh.getShortName("", "test");
        assertEquals(shortName, "test");
    }

    @Test
    public void testGetExtName() {
        String extName = this.fh.getExtension("Helloworld.txt", "txt");
        assertEquals(extName, "txt");
        extName = this.fh.getExtension("Helloworld.txt.java", "txt");
        assertEquals(extName, "java");
        extName = this.fh.getExtension("Helloworld.", "txt");
        assertEquals(extName, "txt");
    }

    @Test
    public void testSaveFile() {
        String path = "temp.jpg";
        List<File> files = this.fh.read(path);
        printFiles(files);
        String outputFolder = "./testresult";
        this.fh.save(getTgFiles(files), outputFolder);
    }

    private List<TgFile> getTgFiles(List<File> files) {
        List<TgFile> tgFiles = new ArrayList<TgFile>();
        for (File file : files) {
            TgFile tf = new TgCommonFile(file.getName());
            tf.setData(file.toString().getBytes());
            tf.setAbsolutePath(file.getAbsolutePath());
            tgFiles.add(tf);
        }
        return tgFiles;
    }

    @Test
    public void testSingleFile() {
        String path = "./bin/com/tg/testcase/TestFileHandler.class";
        List<File> files = this.fh.read(path);
        printFiles(files);
        assertEquals(files.size(), 1);
    }

    @Test
    public void testMultiFiles() {
        String path = ".";
        List<File> files = this.fh.read(path);
        printFiles(files);
        assertTrue(files.size() > 1);
    }

    public void printFiles(List<File> files) {
        System.out.println("Total number is:" + files.size());
        for (File file : files) {
            System.out.println(file.getName() + "---" + file.getAbsolutePath());
        }
    }
}
