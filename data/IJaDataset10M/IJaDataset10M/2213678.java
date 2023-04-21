package com.nyandu.weboffice.mail.business;

import javax.mail.*;
import java.util.Properties;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: alvaro
 * Date: 18/12/2004
 * Time: 03:25:56 PM
 */
public class MailUserData {

    private URLName urlName;

    private Session session;

    private Store store = null;

    private Folder folder = null;

    private String host;

    private String user;

    private String password;

    private MailTransferObject mto;

    public MailUserData(MailTransferObject mto) throws Exception {
        this.mto = mto;
    }

    public void reconnect() throws MessagingException {
        if (folder != null) {
            if (folder.isOpen()) {
                folder.close(true);
            }
        }
        if (store != null) {
            store.close();
        }
        this.host = mto.getPop3Host();
        this.user = mto.getUser();
        this.password = mto.getPassword();
        String protocol = "pop3";
        String mbox = "INBOX";
        this.urlName = new URLName(protocol, host, -1, mbox, user, password);
        Properties props = new Properties();
        props.put("mail.MailTransport.protocol", "smtp");
        props.put("mail.smtp.user", mto.getUser());
        props.put("mail.smtp.host", mto.getSmtpHost());
        props.put("mail.smtp.port", Integer.toString(mto.getSmtpPort()));
        if (mto.getSmtpAuth()) {
            props.put("mail.smtp.auth", "true");
        }
        if (mto.getSmtpSSL()) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", Integer.toString(mto.getSmtpPort()));
        }
        props.put("mail.pop3.user", mto.getUser());
        props.put("mail.pop3.host", mto.getPop3Host());
        props.put("mail.pop3.port", Integer.toString(mto.getPop3Port()));
        if (mto.getPop3SSL()) {
            props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3.socketFactory.fallback", "false");
            props.put("mail.pop3.socketFactory.port", Integer.toString(mto.getPop3Port()));
        }
        session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        try {
            store = session.getStore(urlName);
            store.connect();
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] msgs = folder.getMessages();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(msgs, fp);
        } catch (NoSuchProviderException nspe) {
            throw nspe;
        } catch (MessagingException me) {
            throw me;
        }
    }

    public void close() throws MessagingException {
        if (folder != null) {
            if (folder.isOpen()) {
                folder.close(true);
            }
        }
        if (store != null) {
            store.close();
        }
    }

    public URLName getURLName() {
        return urlName;
    }

    public Session getSession() {
        return session;
    }

    public Store getStore() {
        return store;
    }

    public Folder getFolder() {
        return folder;
    }

    public MailTransferObject getMto() {
        return mto;
    }

    public void setMto(MailTransferObject mto) throws Exception {
        this.mto = mto;
        this.reconnect();
    }
}
