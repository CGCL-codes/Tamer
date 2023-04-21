package com.shine.DataBaseSource;

/**
 * 
 * @author viruscodecn@gmail.com
 * 
 */
public class DBManager {

    private static DBManager manager;

    public static DBManager getManager() {
        if (manager == null) manager = new DBManager();
        return manager;
    }

    /**
	 * 初始化
	 * 
	 * @param xmlfile
	 */
    public void initDBConfig(String xmlfile) {
    }
}
