package org.wcb.gui.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.security.GeneralSecurityException;

/**
 * <small>
 * <p>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Sep 26, 2006 11:44:35 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 *         Authentication
 */
public class AuthenticationUtility {

    private String userName;

    private String pword;

    private boolean rememberName;

    private Logger log = Logger.getLogger(AuthenticationUtility.class.getName());

    /**
     * Name of the algorithm used for encryption\decryption.
     */
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    /**
     * Password for Key.
     */
    private static final char[] KEY_PASSWORD = "pilotlogbook".toCharArray();

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    private static final String REMEMBER_NAME = "username.rembmer";

    /**
     * Create new instance of authentication utility object.
     */
    public AuthenticationUtility() {
        load();
    }

    /**
     * Loads stored username and password values from the application preferences.
     */
    private void load() {
        userName = ApplicationPreferences.getInstance().getString(USERNAME);
        pword = ApplicationPreferences.getInstance().getString(PASSWORD);
        rememberName = ApplicationPreferences.getInstance().getBoolean(REMEMBER_NAME);
    }

    /**
     * Should only be used to clear user name and password
     * in cases of unit testing.
     */
    public void clearAuthentication() {
        ApplicationPreferences.getInstance().removeKey(USERNAME);
        ApplicationPreferences.getInstance().removeKey(PASSWORD);
    }

    /**
     * Create a new user with password.
     * @param user User name.
     * @param passwrd Password to store.
     */
    public void createNewUser(String user, String passwrd) {
        this.setUsername(user);
        this.setPassword(encrypt(passwrd));
        ApplicationPreferences.getInstance().putString(USERNAME, userName);
        ApplicationPreferences.getInstance().putString(PASSWORD, encrypt(passwrd));
    }

    /**
     * Validates if the user name and password match.
     * If they match then true is returned.
     * @param user User's name.
     * @param passwrd user password.
     * @return True indicates password succeeded.
     */
    public boolean authenticateUser(String user, String passwrd) {
        String unencryptPassword = decrypt(pword);
        return user.equalsIgnoreCase(userName) && passwrd.equalsIgnoreCase(unencryptPassword);
    }

    /**
     * Calls update password using old password to authenticate.
     * The old password is authenticated then the new password is stored.
     * @param oldpasswrd Old password for verification.
     * @param passwrd New password.
     * @return True indicates password storing was successful.
     */
    public boolean updatePassword(String oldpasswrd, String passwrd) {
        String unencryptPassword = decrypt(pword);
        if (unencryptPassword.equalsIgnoreCase(oldpasswrd) || userName.equals("")) {
            this.setPassword(encrypt(passwrd));
            ApplicationPreferences.getInstance().putString(USERNAME, userName);
            ApplicationPreferences.getInstance().putString(PASSWORD, encrypt(passwrd));
            return true;
        }
        return false;
    }

    /**
     * Encrypts a string with basic cipher encryption algorithims.
     * @param pass String to encrypt
     * @return Encrypted string.
     */
    public String encrypt(String pass) {
        Cipher cipher;
        SecretKey secretKey;
        PBEParameterSpec pbeParmSpec;
        byte[] encData = null;
        try {
            secretKey = createSecretKey();
            pbeParmSpec = createParameterSpec();
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParmSpec);
            encData = cipher.doFinal(pass.getBytes());
        } catch (GeneralSecurityException gse) {
            log.log(Level.WARNING, gse.getMessage());
        }
        return new String(encData);
    }

    /**
     * Does a decryption of a string that has been secured.
     * @param newPassword Ciphered text string.
     * @return Human readable string or password.
     */
    public String decrypt(String newPassword) {
        Cipher cipher;
        SecretKey secretKey;
        PBEParameterSpec pbeParmSpec;
        byte[] decData = null;
        try {
            secretKey = createSecretKey();
            pbeParmSpec = createParameterSpec();
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParmSpec);
            decData = cipher.doFinal(newPassword.getBytes());
        } catch (GeneralSecurityException gse) {
            log.log(Level.WARNING, gse.getMessage(), gse);
        }
        return new String(decData);
    }

    /**
     * Gets the user name.
     * @return user name string.
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Allows setting of user name.
     * @param uname user name string.
     */
    public void setUsername(String uname) {
        this.userName = uname;
    }

    /**
     * Gets the password from this object.
     * @return user password.
     */
    public String getPassword() {
        return pword;
    }

    /**
     * Sets the user password.
     * @param password user's password.
     */
    public void setPassword(String password) {
        this.pword = password;
    }

    /**
     * Set to true to remeber the user name on the login form.
     * @param val Set to true to remember name on user form
     */
    public void setRememberUsername(boolean val) {
        this.rememberName = val;
        ApplicationPreferences.getInstance().setBoolean(REMEMBER_NAME, val);
    }

    /**
     * True indicates user wants to have the login dialog remember their username.
     * @return  true remember username.
     */
    public boolean isRememberName() {
        return this.rememberName;
    }

    /**
     * Returns the SecretKey used for encryption and decryption.
     *
     * @return SecretKey - the SecretKey
     * @throws java.security.GeneralSecurityException - on error;
     */
    private SecretKey createSecretKey() throws GeneralSecurityException {
        PBEKeySpec pbeKeySpec;
        SecretKeyFactory skFactory;
        pbeKeySpec = new PBEKeySpec(KEY_PASSWORD);
        skFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return skFactory.generateSecret(pbeKeySpec);
    }

    /**
     * Returns the PBEParameterSpec used for initializing
     * a Cipher object.
     *
     * @return PBEParameterSpec - the PBEParameterSpec.
     */
    private PBEParameterSpec createParameterSpec() {
        return new PBEParameterSpec(("LMBSALT!".getBytes()), 50);
    }
}
