package com.mymail.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.mymail.dao.MailboxDAO;

public class MailBox {

    public static final char SSL_TRUE = 'Y';

    public static final char SSL_FALSE = 'N';

    private static final String HOST = "mail.smtp.host";

    private static final String AUTH = "mail.smtp.auth";

    private static final String TRUE = "true";

    private Integer id;

    private String name;

    private Integer user;

    private String password;

    private String server_name;

    private String server_smtp;

    private String server_pop3;

    private boolean server_ssl;

    private Date lasttime;

    private static MailboxDAO dao = new MailboxDAO();

    public void sendMail(Mail mail) throws AuthenticationFailedException {
        Properties prop = new Properties();
        prop.put(HOST, server_smtp);
        prop.put(AUTH, TRUE);
        Session session = Session.getInstance(prop, new MyAuth(name, password));
        MimeMessage message = mail.getMimeMessage(session);
        try {
            Transport.send(message);
            mail.setSort(Mail.YIFA);
            mail.toDataBase();
        } catch (MessagingException e) {
            if (e instanceof AuthenticationFailedException) {
                throw (AuthenticationFailedException) e;
            } else e.printStackTrace();
        }
    }

    public static MailboxDAO getDAO() {
        return dao;
    }

    public static MailBox getById(int id) {
        MailBox mailbox = new MailBox();
        if (dao.getByID(new Integer(id), mailbox)) {
            return mailbox;
        }
        return null;
    }

    public ArrayList<Mail> getMails(int start, int end) {
        return null;
    }

    /**
	 * @return the lasttime
	 */
    public Date getLasttime() {
        return lasttime;
    }

    /**
	 * @param lasttime
	 *            the lasttime to set
	 */
    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the user
	 */
    public Integer getUser() {
        return user;
    }

    /**
	 * @param user
	 *            the user to set
	 */
    public void setUser(Integer user) {
        this.user = user;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the server_name
	 */
    public String getServer_name() {
        return server_name;
    }

    /**
	 * @return the server_smtp
	 */
    public String getServer_smtp() {
        return server_smtp;
    }

    /**
	 * @return the server_pop3
	 */
    public String getServer_pop3() {
        return server_pop3;
    }

    /**
	 * @return the server_ssl
	 */
    public boolean isServer_ssl() {
        return server_ssl;
    }

    /**
	 * @param serverName
	 *            the server_name to set
	 */
    public void setServer_name(String serverName) {
        server_name = serverName;
    }

    /**
	 * @param serverSmtp
	 *            the server_smtp to set
	 */
    public void setServer_smtp(String serverSmtp) {
        server_smtp = serverSmtp;
    }

    /**
	 * @param serverPop3
	 *            the server_pop3 to set
	 */
    public void setServer_pop3(String serverPop3) {
        server_pop3 = serverPop3;
    }

    /**
	 * @param serverSsl
	 *            the server_ssl to set
	 */
    public void setServer_ssl(boolean serverSsl) {
        server_ssl = serverSsl;
    }
}

class MyAuth extends Authenticator {

    private String username;

    private String password;

    public MyAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
