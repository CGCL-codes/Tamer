package com.ysh.dbpool;

import java.io.File;
import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import java.sql.SQLException;
import java.util.List;

/**
 * A class to wrap DBPool
 * @author yangshuo
 */
public class DBPoolWrapper {

    /**
	 * DB driver
	 */
    private String dbDriver;

    /**
	 * DB url
	 */
    private String dbUrl;

    /**
	 * DB username
	 */
    private String userName;

    /**
	 * d
	 */
    private String password;

    /**
	 * max connections in dbpool
	 */
    private int maxSize;

    /**
	 * max connections in memory
	 */
    private int maxMemorySize;

    /**
	 * initial connections
	 */
    private int initialSize;

    /**
	 * increase step
	 */
    private int stepSize;

    /**
	 * Config File
	 */
    private String configFile;

    /**
	 * Instance of wrapper
	 */
    private static DBPoolWrapper wrapper;

    /**
	 * Create a wrapper
	 * @param configFile:Config file to read
	 */
    private DBPoolWrapper(String configFile) {
        this.dbDriver = "";
        this.dbUrl = "";
        this.userName = "";
        this.password = "";
        this.maxSize = 0;
        this.maxMemorySize = 0;
        this.initialSize = 0;
        this.stepSize = 0;
        this.configFile = configFile;
        this.readConfig();
    }

    public static DBPool getDBPool(String configFile) throws ClassNotFoundException, SQLException {
        if (wrapper == null) {
            synchronized (DBPoolWrapper.class) {
                if (wrapper == null) {
                    wrapper = new DBPoolWrapper(configFile);
                    wrapper.readConfig();
                }
            }
        }
        System.out.println("=========��ʼ����ݿ����ӳ�,��ȴ�..........==========\n");
        return DBPool.getInstance(wrapper.initialSize, wrapper.stepSize, wrapper.maxSize, wrapper.maxMemorySize, wrapper.dbUrl, wrapper.dbDriver, wrapper.userName, wrapper.password);
    }

    /**
	 * Read config file
	 */
    @SuppressWarnings("unchecked")
    private void readConfig() {
        try {
            System.out.println("==========Reading Config File:dbTestConfig.xml===========");
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(new File(this.configFile));
            List<Element> list = doc.selectNodes("/dbPool/dbDriver");
            Element e = list.get(0);
            this.printElement(e);
            DBPoolWrapper.this.dbDriver = e.getStringValue();
            list = doc.selectNodes("/dbPool/dbUrl");
            e = list.get(0);
            this.printElement(e);
            this.dbUrl = e.getStringValue();
            list = doc.selectNodes("/dbPool/userName");
            e = list.get(0);
            this.printElement(e);
            this.userName = e.getStringValue();
            list = doc.selectNodes("/dbPool/password");
            e = list.get(0);
            this.printElement(e);
            this.password = e.getStringValue();
            list = doc.selectNodes("/dbPool/maxCount");
            e = list.get(0);
            this.printElement(e);
            this.maxSize = Integer.parseInt(e.getStringValue());
            list = doc.selectNodes("/dbPool/maxInMemory");
            e = list.get(0);
            this.printElement(e);
            this.maxMemorySize = Integer.parseInt(e.getStringValue());
            list = doc.selectNodes("/dbPool/initalCount");
            e = list.get(0);
            this.printElement(e);
            this.initialSize = Integer.parseInt(e.getStringValue());
            list = doc.selectNodes("/dbPool/stepSize");
            e = list.get(0);
            this.printElement(e);
            this.stepSize = Integer.parseInt(e.getStringValue());
            System.out.println("=============�����ȡ���:============\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void printElement(Element e) {
        System.out.println(e.getName() + ":\t" + e.getStringValue());
    }

    public void test() {
        this.readConfig();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    }
}
