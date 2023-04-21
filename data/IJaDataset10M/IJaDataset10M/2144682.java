package com.myjavalab.core;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyTest {

    public static void main(String[] args) {
        FileInputStream in = null;
        try {
            Properties defaltProperties = new Properties();
            in = new FileInputStream("defaultProps");
            defaltProperties.load(in);
            Enumeration<Object> en = defaltProperties.elements();
            while (en.hasMoreElements()) {
                System.out.println(en.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
