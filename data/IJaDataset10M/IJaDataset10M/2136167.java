package com.nexustar.extract;

import junit.framework.TestCase;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 *
 * @author gaoyang
 * @version $Id: $
 * @date 11-5-30 上午12:11
 */
public class FileUtilsTestCase extends TestCase {

    public void testRead() throws Exception {
        String content = FileUtils.readFileByChars("D://test/book.html");
        System.out.println("content = " + content);
    }

    public void testCombine() {
        String bookPath = "D:\\book\\mybook.txt";
        String tempPath = "D:\\book\\118623_118627";
        Comparator<File> fileComparator = new Comparator<File>() {

            public int compare(File o1, File o2) {
                int index1 = o1.getName().lastIndexOf(".");
                int index2 = o2.getName().lastIndexOf(".");
                String f1 = o1.getName().substring(0, index1);
                String f2 = o2.getName().substring(0, index2);
                return Integer.parseInt(f1) - Integer.parseInt(f2);
            }
        };
        try {
            FileUtils.combine(new File(bookPath), new File(tempPath), fileComparator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
