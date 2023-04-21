package org.cyberaide.authentication.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Enumeration;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserAccountFile extends UserAccountMem {

    private String userhome = System.getProperty("user.home");

    private String fs = System.getProperty("file.separator");

    private String accountFile = userhome + fs + "users.cyberaide";

    private URL furl = ClassLoader.getSystemResource("users.cyberaide");

    public UserAccountFile() {
        super();
        System.out.println(furl);
        users = new Properties();
        updateFromFile();
    }

    public synchronized boolean addUser(String username, String password) {
        boolean ret = super.addUser(username, password);
        if (ret) {
            storeToFile();
        }
        return ret;
    }

    public synchronized boolean delUser(String username) {
        boolean ret = super.delUser(username);
        if (ret) {
            storeToFile();
        }
        return ret;
    }

    public synchronized boolean changePasswd(String username, String newPasswd) {
        boolean ret = super.changePasswd(username, newPasswd);
        if (ret) {
            storeToFile();
        }
        return ret;
    }

    public synchronized String resetPasswd(String username) {
        String ret = super.resetPasswd(username);
        if (ret != null) {
            storeToFile();
        }
        return ret;
    }

    private void updateFromFile() {
        InputStream is = null;
        try {
            System.out.println(furl);
            is = ClassLoader.getSystemResourceAsStream("users.cyberaide");
            users.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void storeToFile() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(furl.toURI()));
            users.store(fos, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
