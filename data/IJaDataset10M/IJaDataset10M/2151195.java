package com.csii.util;

/**
 * @author cuiyi
 *
 */
public class PathManager {

    /**
	 * �õ�xml�ļ��ľ��·��
	 * @return
	 */
    public String getPath() {
        String classpath = this.getClass().getClassLoader().getResource("").getPath();
        String path = classpath.substring(1, classpath.length() - 8);
        String configpath = path + "config.xml";
        return configpath;
    }
}
