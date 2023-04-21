package org.eweb4j.config.bean;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigMVC {

    private String open = "true";

    private LogsConfigBean logs;

    private ScanActionPackage scanActionPackage;

    private ActionXmlFile actionXmlFiles;

    private InterXmlFile interXmlFiles;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public LogsConfigBean getLogs() {
        return logs;
    }

    public void setLogs(LogsConfigBean logs) {
        this.logs = logs;
    }

    public ActionXmlFile getActionXmlFiles() {
        return actionXmlFiles;
    }

    public void setActionXmlFiles(ActionXmlFile actionXmlFiles) {
        this.actionXmlFiles = actionXmlFiles;
    }

    public InterXmlFile getInterXmlFiles() {
        return interXmlFiles;
    }

    public void setInterXmlFiles(InterXmlFile interXmlFiles) {
        this.interXmlFiles = interXmlFiles;
    }

    public ScanActionPackage getScanActionPackage() {
        return scanActionPackage;
    }

    public void setScanActionPackage(ScanActionPackage scanActionPackage) {
        this.scanActionPackage = scanActionPackage;
    }

    @Override
    public String toString() {
        return "ConfigMVC [open=" + open + ", logs=" + logs + ", scanActionPackage=" + scanActionPackage + ", actionXmlFiles=" + actionXmlFiles + ", interXmlFiles=" + interXmlFiles + "]";
    }
}
