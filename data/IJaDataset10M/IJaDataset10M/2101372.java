package com.jsu.test.md5;

import com.jsu.util.MD5;

/**
 * @author Administrator
 *
 */
public class MD5Test {

    /**
	 * @param strings
	 */
    public static void main(String... strings) {
        String str;
        str = "root";
        System.out.println(MD5.getMD5(str));
    }
}
