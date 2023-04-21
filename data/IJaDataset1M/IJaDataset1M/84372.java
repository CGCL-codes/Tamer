package org.tolven.gatekeeper.entity;

import java.util.Date;

/**
 * 
 * @author Joseph Isaac
 * 
 */
public interface PasswordBackup {

    public Date getCreation();

    public byte[] getEncryptedPassword();

    public int getIterationCount();

    public String getPasswordPurpose();

    public String getRealm();

    public byte[] getSalt();

    public String getSecurityQuestion();

    public String getUserId();

    public void setCreation(Date creation);

    public void setEncryptedPassword(byte[] encryptedPassword);

    public void setIterationCount(int iterationCount);

    public void setPasswordPurpose(String passwordPurpose);

    public void setRealm(String realm);

    public void setSalt(byte[] salt);

    public void setSecurityQuestion(String securityQuestion);

    public void setUserId(String userId);
}
