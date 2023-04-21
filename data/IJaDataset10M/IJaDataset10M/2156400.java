package org.xmlprocess.lircServer.dto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;
import org.xmlprocess.lircServer.common.CommonStatics;

public class ConfigDTO {

    private static final Logger log = Logger.getLogger(ConfigDTO.class.getName());

    public Properties props;

    public String lircDir;

    public String irSend;

    public String irSendPath;

    public String xmlDir;

    public String xmlFile;

    public String xmlPath;

    public String sleep;

    public Hashtable<String, ArrayList<String>> errsHT = new Hashtable<String, ArrayList<String>>();

    public boolean[] testIRpath;

    public boolean[] testXMLpath;

    public boolean[] testXMLDir;

    public Properties getProps() {
        if (props == null) {
            props = new Properties();
        }
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public String getLircDir() {
        this.lircDir = winSafeURL(getProp(CommonStatics.LIRC_DIR, ""));
        props.setProperty(CommonStatics.LIRC_DIR, this.lircDir);
        return lircDir;
    }

    public void setLircDir(String lircDir) {
        this.lircDir = winSafeURL(lircDir);
        props.setProperty(CommonStatics.LIRC_DIR, this.lircDir);
        getIrSendPath();
    }

    public String getIrSend() {
        irSend = getProp(CommonStatics.IRSEND, CommonStatics.IRSEND);
        return irSend;
    }

    public void setIrSend(String irSend) {
        this.irSend = irSend;
        props.setProperty(CommonStatics.IRSEND, this.irSend);
        getIrSendPath();
    }

    public String getIrSendPath() {
        if (getLircDir().length() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(lircDir);
            sb.append("/");
            sb.append(getIrSend());
            setIrSendPath(sb.toString());
        } else setIrSendPath("");
        return irSendPath;
    }

    public void setIrSendPath(String irSendPath) {
        this.irSendPath = winSafeURL(irSendPath);
        props.setProperty(CommonStatics.IRSEND_PATH, this.irSendPath);
    }

    public String getXmlDir() {
        xmlDir = winSafeURL(getProp(CommonStatics.XML_DIR, ""));
        props.setProperty(CommonStatics.XML_DIR, this.xmlDir);
        return xmlDir;
    }

    public void setXmlDir(String xmlDir) {
        this.xmlDir = winSafeURL(xmlDir);
        props.setProperty(CommonStatics.XML_DIR, this.xmlDir);
        getXmlPath();
    }

    public String getXmlFile() {
        xmlFile = getProp(CommonStatics.XML_FILE, "");
        if (xmlFile == null || xmlFile.length() == 0) {
            setXmlFile(CommonStatics.DEF_CONFIG_XML);
        }
        return xmlFile;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
        props.setProperty(CommonStatics.XML_FILE, this.xmlFile);
        getXmlPath();
    }

    public String getXmlPath() {
        if (getXmlDir().length() > 0 && getXmlFile().length() > 0) {
            String xmlp = "/" + getXmlFile();
            setXmlPath(xmlDir + xmlp);
        } else setXmlPath("");
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = winSafeURL(xmlPath);
        props.setProperty(CommonStatics.XML_PATH, this.xmlPath);
    }

    public Hashtable<String, ArrayList<String>> getErrsHT() {
        return errsHT;
    }

    public void setErrsHT(Hashtable<String, ArrayList<String>> errsHT) {
        this.errsHT = errsHT;
    }

    public boolean[] getTestIRpath() {
        return testIRpath;
    }

    public void setTestIRpath(boolean[] testIRpath) {
        this.testIRpath = testIRpath;
    }

    public boolean[] getTestXMLpath() {
        return testXMLpath;
    }

    public void setTestXMLpath(boolean[] testXMLpath) {
        this.testXMLpath = testXMLpath;
    }

    public boolean[] getTestXMLDir() {
        return testXMLDir;
    }

    public void setTestXMLDir(boolean[] testXMLDir) {
        this.testXMLDir = testXMLDir;
    }

    public static String winSafeURL(String filepath) {
        String WinSafePath = "";
        if (filepath != null) {
            WinSafePath = filepath.replace('\\', '/');
        }
        return WinSafePath;
    }

    public void debugvals() {
        log.info("lircDir = " + lircDir);
        log.info("irSend = " + irSend);
        log.info("irSendPath = " + irSendPath);
        log.info("xmlDir = " + xmlDir);
        log.info("xmlFile = " + xmlFile);
        log.info("xmlPath = " + xmlPath);
    }

    public void writeProps() throws Exception {
        String fn = getProps().getProperty(CommonStatics.CONFIG_PROPS);
        getProps().store(new FileOutputStream(fn), null);
    }

    public String getProp(String key, String defval) {
        if (getProps().getProperty(key) == null || getProps().getProperty(key).length() == 0) {
            getProps().setProperty(key, defval);
        }
        return getProps().getProperty(key);
    }

    public void addReqProps(String propsFN) throws Exception {
        File propsFile = new File(propsFN);
        String propsAbsFN = propsFile.getAbsolutePath();
        getProps().setProperty(CommonStatics.CONFIG_PROPS, propsAbsFN);
        getIrSendPath();
        getXmlPath();
        writeProps();
    }

    public String getSleep() {
        sleep = getProp(CommonStatics.SLEEP, "500");
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }
}
