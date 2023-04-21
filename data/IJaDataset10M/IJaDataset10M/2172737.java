package org.wangfy.hy.hylet.util;

import java.io.File;

/**
 * 
 * FileTest.java
 * 
 * @author fuyewang E-mail: willfcareer@sohu.com
 * @version 1.0 Creation Date：2009-9-3 上午10:11:38
 * @since 1.0
 * 
 */
public class FileTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        File file = new File("Testfile");
        File _file = new File(file, "/test1/test2/test3");
        String path = _file.getAbsolutePath();
        System.out.println(path);
    }
}
