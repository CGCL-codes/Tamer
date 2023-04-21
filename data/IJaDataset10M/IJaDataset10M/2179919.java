package com.googlecode.mycontainer.mail;

import javax.mail.Session;
import com.googlecode.mycontainer.kernel.deploy.NamingDeployer;

public class MailDeployer extends NamingDeployer {

    private static final long serialVersionUID = -1747391411302364401L;

    private MailInfoBuilder info = new MailInfoBuilder();

    public MailInfoBuilder getInfo() {
        return info;
    }

    @Override
    protected Object getResource() {
        return Session.getDefaultInstance(info.getProperties(), info.getAuthenticator());
    }
}
