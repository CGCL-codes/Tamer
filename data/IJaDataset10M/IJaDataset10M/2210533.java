/*
 * 
 */
package com.built1.apps.eis.common.setting;

import java.io.*;
import java.util.*;

//import org.apache.log4j.Logger;

/** 
 * FileSettings 를 사용시에 파일을 수정했을 때, Settings.load() 를 호출든가,
 * WAS 를 재시작해야 한다. ReloadableFileSettings는 파일 수정 시간을 비교하여
 * 수정되었으면 다시 설정 파일을 로딩한다. 
 * 
 * @author Donghyeok Kang (wolfkang@netville.co.kr)
 * @version 
 */
public class ReloadableFileSettings implements SettingSource {

    protected Properties prop;
    protected File propFile;
    protected long lastModified;

    public ReloadableFileSettings() {
        this.prop = new Properties();
        this.lastModified = 0;
        this.propFile = null;
    }

    public Enumeration getPropertyNames() {
        return prop.propertyNames();
    }

    public synchronized void load(String param) throws IOException {
        String fileName = Settings.getInstance().translateDirectory(param);
        propFile = new File(fileName);
        load();
    }

    protected void load() throws IOException {
        FileInputStream in = new FileInputStream(propFile);
        prop.load(in);
        in.close();
    }

    public synchronized void save() throws IOException {
        if ( propFile != null ) {
            FileOutputStream out = new FileOutputStream(propFile);
            prop.store(out, "");
            out.close();
        }
    }

    public int size() {
        return prop.size();
    }

    public String getSetting(String key) {
        long modified = propFile.lastModified();
        if (modified != lastModified) {
            lastModified = modified;
            try {
                load();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        return prop.getProperty(key);
    }

    public void setSetting(String key, String value) {
        prop.setProperty(key, value);
    }
}
