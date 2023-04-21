package cn.mmbook.platform.service.system.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * author ：陈书华; create time：May 26, 2010 10:30:26 AM; modify time：May 26, 2010
 * 10:30:26 AM; remark ：
 */
public class SPManagerImpl {

    public static String PATH = null;

    private Properties props = null;

    private InputStream in = null;

    private OutputStream out = null;

    public void writeProperties(String key, String value, String displayName) {
        props = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(PATH));
            props.load(in);
            out = new FileOutputStream(PATH);
            if (null == value) {
                value = "";
            }
            props.setProperty(key, value);
            props.store(out, null);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                props.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> readProperties() {
        Properties props = new Properties();
        Map<String, String> map = new Hashtable<String, String>();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(PATH));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                map.put(key, value);
            }
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
