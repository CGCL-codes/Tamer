package org.apache.commons.io.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * @author <A HREF="mailto:smufu@naver.com">kris jeong</A> smufu@naver.com
 *
 * commons file utils test class
 */
public class FileUtilsTest {

    /**
	 * @param args
	 * @throws IOException
	 */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        Logger log = Logger.getLogger("test");
        String filePath = System.getProperty("user.dir") + "\\testFile";
        File file1 = new File(filePath, "file1.txt");
        String filename = file1.getAbsolutePath();
        FileUtils.writeStringToFile(file1, filename, "UTF-8");
        String file1contents = FileUtils.readFileToString(file1, "UTF-8");
        log.log(Level.FINE, "file1contents : " + file1contents);
        log.log(Level.FINE, "list dir : " + FileUtils.listFiles(new File(filePath), new String[] { "txt" }, false));
        File file2 = new File(filePath, "file2.txt");
        String filename2 = file2.getAbsolutePath();
        FileUtils.writeStringToFile(file2, filename2, "UTF-8");
        String file2contents = FileUtils.readFileToString(file2, "UTF-8");
        log.log(Level.FINE, "file2contents : " + file2contents);
        File testFile = new File(filePath, "subdir");
        FileUtils.forceMkdir(testFile);
        log.log(Level.FINE, "list dir : " + FileUtils.listFiles(testFile, new String[] { "txt" }, false));
        File testFile2 = new File(filePath, "testFile1Copy.txt");
        File testFile1 = new File(filePath, "testFile1.txt");
        FileUtils.copyFile(testFile1, testFile2);
        FileUtils.forceDelete(testFile2);
        log.log(Level.FINE, "list dir : " + FileUtils.listFiles(new File(filePath), new String[] { "txt" }, false));
        File directory = new File(filePath, "subdir");
        FileUtils.copyFileToDirectory(testFile1, directory);
        String readFile = FileUtils.readFileToString(file1, "UTF-8");
        log.log(Level.FINE, "readFile : " + readFile);
        List<String> readList = FileUtils.readLines(file1, "UTF-8");
        for (int j = 0, sl = readList.size(); j < sl; j++) {
            log.log(Level.FINE, "readList : " + readList.get(j));
        }
        long size = FileUtils.sizeOfDirectory(new File(filePath));
        log.log(Level.FINE, size + " bytes");
        log.log(Level.FINE, FileUtils.byteCountToDisplaySize(size));
    }
}
