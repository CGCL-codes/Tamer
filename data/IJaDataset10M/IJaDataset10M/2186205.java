package com.gever.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 
 * 
 * ���ڶ�ȡ�����ļ�
 * 
 */
public class Environment {

    private static String FILE_NAME = "/gever_config.properties";

    private static Properties properties;

    static {
        properties = getProperties(FILE_NAME);
    }

    private Environment() {
    }

    /**
	 * 
	 * @param fileName
	 * @return
	 */
    public static Properties getProperties(String fileName) {
        Properties result = new Properties();
        try {
            result.load(Environment.class.getResourceAsStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * ��ȡProperties
	 * @return
	 */
    public static Properties getProperties() {
        return properties;
    }

    /**
	 * ��ȡ�ַ����͵�����
	 * @param name ������
	 * @return
	 */
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    /**
	 * ��ȡ��������
	 * @param name ������
	 * @param defaultValue Ĭ��ֵ
	 * @return
	 */
    public static int getIntProperty(String name, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(name));
        } catch (NumberFormatException e) {
            System.err.println(name + "��ʽ����");
            return defaultValue;
        } catch (NullPointerException e) {
            System.err.println("δ�ҵ�����" + name);
            return defaultValue;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return defaultValue;
        }
    }

    /**
	 * ��ȡ����������
	 * @param name ������
	 * @param defaultValue Ĭ��ֵ
	 * @return
	 */
    public static long getLongProperty(String name, long defaultValue) {
        try {
            return Long.parseLong(getProperty(name));
        } catch (NumberFormatException e) {
            System.err.println(name + "��ʽ����");
            return defaultValue;
        } catch (NullPointerException e) {
            System.err.println("δ�ҵ�����" + name);
            return defaultValue;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return defaultValue;
        }
    }
}
