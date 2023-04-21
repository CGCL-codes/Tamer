package org.paradise.dms.dao.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class DataTable {

    private static Hashtable<String, String> HASH_DATATABLE = new Hashtable<String, String>();

    private String path;

    /**
	 * Description:通过字符串获得对应的数据表名
	 * @param id 数据表对应的字符串
	 * @return
	 */
    public static String getDataTableName(String id) {
        return HASH_DATATABLE.get(id);
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * Description；动态加载数据表对应的properties文本
	 */
    public void init() {
        Properties prop = new Properties();
        path.replace('\\', '/');
        InputStream fis;
        try {
            fis = this.getClass().getResourceAsStream(path);
            prop.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Enumeration<Object> e = prop.keys(); e.hasMoreElements(); ) {
            String id = e.nextElement().toString();
            String name = prop.getProperty(id);
            HASH_DATATABLE.put(id, name);
        }
    }
}
