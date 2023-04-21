package com.laoer.bbscs.web.action;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.laoer.bbscs.exception.SysConfigException;
import com.laoer.bbscs.service.config.SysConfig;
import com.laoer.bbscs.web.ui.RadioInt;

public class AdminEmailSet extends BaseAction {

    /**
	 * Logger for this class
	 */
    private static final Log logger = LogFactory.getLog(AdminEmailSet.class);

    /**
	 *
	 */
    private static final long serialVersionUID = -7618901001233150189L;

    public AdminEmailSet() {
        this.setRadioYesNoListValues();
    }

    private SysConfig sysConfig;

    private String senderEmail;

    private int smtpAuth;

    private String smtpPasswd;

    private int smtpPort;

    private String smtpServer;

    private String smtpUser;

    private int useEmail;

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public int getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(int smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpPasswd() {
        return smtpPasswd;
    }

    public void setSmtpPasswd(String smtpPasswd) {
        this.smtpPasswd = smtpPasswd;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public SysConfig getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(SysConfig sysConfig) {
        this.sysConfig = sysConfig;
    }

    public int getUseEmail() {
        return useEmail;
    }

    public void setUseEmail(int useEmail) {
        this.useEmail = useEmail;
    }

    List<RadioInt> radioYesNoList = new ArrayList<RadioInt>();

    private void setRadioYesNoListValues() {
        radioYesNoList.add(new RadioInt(0, this.getText("bbscs.no")));
        radioYesNoList.add(new RadioInt(1, this.getText("bbscs.yes")));
    }

    public List<RadioInt> getRadioYesNoList() {
        return radioYesNoList;
    }

    public void setRadioYesNoList(List<RadioInt> radioYesNoList) {
        this.radioYesNoList = radioYesNoList;
    }

    public String execute() {
        try {
            return this.executeMethod(this.getAction());
        } catch (Exception e) {
            logger.error(e);
            return INPUT;
        }
    }

    public String index() {
        this.setAction("save");
        this.setSenderEmail(this.getSysConfig().getSenderEmail());
        this.setSmtpAuth(this.getSysConfig().getSmtpAuth());
        this.setSmtpPasswd(this.getSysConfig().getSmtpPasswd());
        this.setSmtpPort(this.getSysConfig().getSmtpPort());
        this.setSmtpServer(this.getSysConfig().getSmtpServer());
        this.setSmtpUser(this.getSysConfig().getSmtpUser());
        this.setUseEmail(this.getSysConfig().getUseEmail());
        return INPUT;
    }

    public String save() {
        this.getSysConfig().setSenderEmail(this.getSenderEmail());
        this.getSysConfig().setSmtpAuth(this.getSmtpAuth());
        this.getSysConfig().setSmtpPasswd(this.getSmtpPasswd());
        this.getSysConfig().setSmtpPort(this.getSmtpPort());
        this.getSysConfig().setSmtpServer(this.getSmtpServer());
        this.getSysConfig().setSmtpUser(this.getSmtpUser());
        this.getSysConfig().setUseEmail(this.getUseEmail());
        try {
            this.getSysConfig().saveConfigs();
            this.addActionMessage(this.getText("bbscs.dataupdate.succeed"));
            return INPUT;
        } catch (SysConfigException e) {
            logger.error("save()", e);
            this.addActionError(this.getText("error.dataupdate.failed"));
            return INPUT;
        }
    }
}
