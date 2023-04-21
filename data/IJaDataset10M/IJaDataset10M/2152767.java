package edu.ucdavis.genomics.metabolomics.binbase.cluster.jmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

public class TwitterNotifierJMX implements TwitterNotifierJMXMBean {

    private Logger logger = Logger.getLogger(getClass());

    private String userName;

    private String password;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        this.store();
    }

    @Override
    public void setUserName(String username) {
        this.userName = username;
        this.store();
    }

    @Override
    public void postDeregister() {
    }

    @Override
    public void postRegister(Boolean arg0) {
        try {
            File file = new File(getClass().getName() + ".properties");
            if (!file.exists()) {
                return;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            this.userName = in.readUTF();
            this.password = in.readUTF();
            in.close();
        } catch (Exception e) {
            logger.debug("postRegister(Boolean)", e);
        }
    }

    @Override
    public void preDeregister() throws Exception {
        this.store();
    }

    @Override
    public ObjectName preRegister(MBeanServer arg0, ObjectName arg1) throws Exception {
        return null;
    }

    private void store() {
        try {
            File file = new File(getClass().getName() + ".properties");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeUTF(this.userName);
            out.writeUTF(this.password);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
