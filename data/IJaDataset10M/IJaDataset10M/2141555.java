package com.incendiaryblue.cmslite.static_pub;

import com.incendiaryblue.appcomponents.FTPAccount;
import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.config.XMLConfigurable;
import com.incendiaryblue.config.XMLConfigurationException;
import com.incendiaryblue.config.XMLContext;
import java.io.*;
import org.w3c.dom.*;

/**
 * Represents the details of additional content that needs to be included in a 
 * static version of a site.
 */
public class RemoteDestination implements XMLConfigurable {

    private FTPAccount ftpAccount;

    private String remoteDir;

    public FTPAccount getFTPAccount() {
        return ftpAccount;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public Object configure(Element element, XMLContext context) throws XMLConfigurationException {
        AppConfig config = (AppConfig) context.getRoot();
        String ftpAccountName = element.getAttribute("ftpAccount");
        ftpAccount = (FTPAccount) config.getAppComponent(FTPAccount.class, ftpAccountName);
        if (ftpAccount == null) throw new IllegalArgumentException("FTPAccount not found: " + ftpAccountName);
        remoteDir = element.getAttribute("remoteDir");
        return this;
    }

    public void registerChild(Object child) {
    }
}
