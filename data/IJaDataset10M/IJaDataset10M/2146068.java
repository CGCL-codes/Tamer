package cn.tworen.demou;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * ConfigManger �������õ���Ϣ ��ȡ˳�����ȶ�ȡconfig�����ļ�,�ٵ�ϵͳSettingĬ��ֵ 
 * Title : 
 * Description :
 * Copyright : Copyright (c) 2004
 * 
 * @author : LuJinYi
 * @version : 1.0 builder 2004040910
 * @Date : 2004/09/10
 */
public class Config extends Properties {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(Config.class);

    /** Ĭ�������ļ������ */
    private static String configFileName = "demou.config";

    private static java.net.URL url = null;

    private static Config config = null;

    private String configFilePath = "";

    private static String delim = ",";

    public static String separator = System.getProperty("file.separator");

    public static String userDir = System.getProperty("user.dir").replaceAll("bin", "") + separator;

    public Config() {
        this.config = this;
    }

    public Config(String fullFileName) {
        this.setConfigFile(fullFileName);
    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config(configFileName);
        }
        return config;
    }

    public Properties getProperties() {
        return this;
    }

    /**
	 * ���������ļ����
	 * 
	 * @param fullFileNem
	 *            ����������ļ���
	 */
    public void setConfigFile(String fullFileName) {
        this.configFileName = fullFileName;
        loadProperties();
    }

    /**
	 * ��ȡ�����ļ���Ϣ
	 * 
	 * @param key
	 *            ���Ե�����keyֵ,String����.
	 * @return ������������ֵ,û���ҵ�,�򷵻�ϵͳ���õ�errorֵ.
	 */
    public String read(String key) {
        return this.getProperty(key);
    }

    public int readInt(String key) {
        String value = this.read(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public float readFloat(String key) {
        String value = this.read(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public boolean readBoolean(String key) {
        String value = this.read(key);
        if (value != null) {
            try {
                return Boolean.parseBoolean(value);
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
	 * ��ȡ�����ļ���Ϣ
	 * 
	 * @param key
	 *            ���Ե�����keyֵ,String����.
	 * @param defaultInfo
	 *            �ڵ�һ������û���ҵ�ֵ�������,����default��ֵ������, ������ʱ����Ȼû���ҵ�,�ⷵ�����key����.
	 * @return ������������ֵ,û���ҵ�,�򷵻�ϵͳ���õ�errorֵ.
	 */
    public String read(String key, String defaultInfo) {
        return this.getProperty(key, defaultInfo);
    }

    public void save(String key, String value) {
        this.setProperty(key, value);
        try {
            URL url = Config.class.getResource(Config.configFileName);
            String urlString = URLDecoder.decode(url.getFile(), "UTF-8");
            if (url != null) {
                OutputStream os = new FileOutputStream(urlString);
                this.store(os, "workflow Config File");
                os.close();
            } else {
            }
        } catch (Exception ex) {
            logger.debug("save() - ### exception in save config " + key + "=" + value);
            logger.error("save()", ex);
        }
    }

    private void loadProperties() {
        if (configFileName == null) return;
        try {
            File file = null;
            try {
                url = this.getClass().getClassLoader().getResource(Config.configFileName);
                InputStream inputStream = (InputStream) url.getContent();
                this.load(inputStream);
            } catch (Exception ex) {
                ex.printStackTrace();
                file = new File(configFilePath + configFileName);
                if (!file.exists()) {
                    if (file.createNewFile()) {
                        try {
                            url = Config.class.getClassLoader().getResource(configFilePath + configFileName);
                        } catch (Exception e) {
                            logger.error("can not create New File in loadProperties" + e.toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("path=" + configFilePath + configFileName);
            logger.error("properties loadProperties error, url=" + url);
        }
    }
}
