package com.liferay.mail.model;

/**
 * <a href="Account.java.html"><b><i>View Source</i></b></a>
 *
 * @author Scott Lee
 *
 */
public interface Account extends AccountModel {

    public java.lang.String getMessageAddress() throws com.liferay.mail.MailboxException;

    public java.lang.String getPasswordDecrypted();

    public void setPasswordDecrypted(java.lang.String password);
}
