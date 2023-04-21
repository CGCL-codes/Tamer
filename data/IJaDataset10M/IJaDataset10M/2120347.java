package supersync.sync.prefs;

import MyCommon.Utilities;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import org.jdesktop.application.ResourceMap;
import supersync.sync.Logger;
import supersync.ui.GetPasswordForm;

/** This class represents how a particular folder is setup.
 *
 * @author Brandon Drake
 */
public class SystemSetup {

    private static final ResourceMap resMap = org.jdesktop.application.Application.getInstance(supersync.SynchronizerApp.class).getContext().getResourceMap(SystemSetup.class);

    protected boolean compress = false;

    protected boolean encrypt = false;

    protected String name = "System Setup";

    protected byte[] encryptedPassword = null;

    protected String password = null;

    protected String passwordHash = null;

    protected String relativePathBase = null;

    protected boolean storeFileHistory = false;

    protected String fileHistoryBaseLocation = "./";

    /** Gets the system setup from an xml element.
     */
    public static SystemSetup fromXML(org.jdom.Element l_element, String l_relativePathBase) {
        SystemSetup result = new SystemSetup();
        result.relativePathBase = l_relativePathBase;
        result.passwordHash = l_element.getAttributeValue("PasswordHash");
        result.compress = l_element.getAttributeValue("Compress", "N").equals("Y");
        result.encrypt = l_element.getAttributeValue("Encrypt", "N").equals("Y");
        result.storeFileHistory = l_element.getAttributeValue("StoreFileHistory", "N").equals("Y");
        result.fileHistoryBaseLocation = l_element.getAttributeValue("FileHistoryBaseLocation", "./");
        String passwordString = l_element.getAttributeValue("ep");
        if (null != passwordString) {
            result.encryptedPassword = Utilities.convertFromHex(passwordString);
        }
        return result;
    }

    /** Gets whether or not compression should be used.
     */
    public synchronized boolean getCompress() {
        return this.compress;
    }

    /** Gets whether or not encryption should be used.
     */
    public synchronized boolean getEncrypt() {
        return this.encrypt;
    }

    /** Gets the encrypted version of the password for the system.  This may be null if the password is not saved.
     *
     * Note if this is null, the password hash is not necessarily null.
     */
    public synchronized byte[] getEncryptedPassword() {
        return this.encryptedPassword;
    }

    /** Gets the location of the file history base when file history is enabled.
     */
    public synchronized String getFileHistoryBaseLocation() {
        return this.fileHistoryBaseLocation;
    }

    /** Gets the name of the system setup.
     */
    public synchronized String getName() {
        return this.name;
    }

    /** Gets the stored password.  If the password has not been set, it returns null.
     */
    public synchronized String getPassword() {
        return this.password;
    }

    /** Gets the stored password.  If the password has not been set, this will try to use the specified password manager to decrypt the encrypted password if it is set.  At last it will return null if the previous attempts fail.
     *
     * If the password manager is null, this will simply try to return the stored password.
     */
    public synchronized String getPassword(PasswordManager l_passwordManager) {
        if (null != this.password) {
            return this.password;
        }
        if (null == l_passwordManager || null == this.encryptedPassword) {
            return null;
        }
        try {
            this.password = String.valueOf(l_passwordManager.decryptPassword(this.encryptedPassword));
        } catch (FileNotFoundException ex) {
            Logger.defaultLogger.Log(resMap.getString("message.keyFileNotFound.text"), Logger.LogLevel.WARNING);
            return null;
        }
        if (false == this.passwordHash.equals(hashPassword(this.password))) {
            this.password = null;
        }
        return this.password;
    }

    /** Gets the password from the user.  Stores the password.
     */
    public synchronized String getPasswordFromUser(String l_message) {
        this.password = GetPasswordForm.getPasswordFromUser(l_message);
        if (null == this.password) {
            return null;
        }
        if (false == this.passwordHash.equals(hashPassword(this.password))) {
            return this.getPasswordFromUser(resMap.getString("message.invalidPassword.text"));
        }
        this.encryptedPassword = null;
        return this.password;
    }

    /** Gets the hash of the user's password.
     */
    public synchronized String getPasswordHash() {
        return this.passwordHash;
    }

    /** Gets the base for the relative base file location path.  This is not stored but needs to be set when this class is initialized.
     */
    public synchronized String getRelativePathBase() {
        return this.relativePathBase;
    }

    /** Gets whether or not file history should be stored for this file system.
     */
    public synchronized boolean getStoreFileHistory() {
        return this.storeFileHistory;
    }

    /** Hashes the password.  This is useful for checking to see if the user's password is correct without having to store their password.
     */
    public static String hashPassword(String l_password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((l_password + "ndi93HUF83ydnhYjt7$%^57hf").getBytes());
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception ex) {
            throw new RuntimeException("Could not get the MD5 hash algorithm.");
        }
    }

    /** Sets whether or not compression should be used.
     */
    public synchronized void setCompress(boolean l_value) {
        this.compress = l_value;
    }

    /** Sets whether or not encryption should be used.
     */
    public synchronized void setEncrypt(boolean l_value) {
        this.encrypt = l_value;
    }

    /** Sets the encrypted version of the password for the system.  This may be null if the password is not saved.
     *
     * Note if this is null, the password hash is not necessarily null.
     */
    public synchronized void setEncryptedPassword(byte[] l_value) {
        this.encryptedPassword = l_value;
    }

    /** Sets the location of the file history base when file history is enabled.
     */
    public synchronized void setFileHistoryBaseLocation(String l_value) {
        this.fileHistoryBaseLocation = l_value;
    }

    /** Gets the name of the system setup.
     */
    public synchronized void setName(String l_value) {
        this.name = l_value;
    }

    /** Sets the stored password.  If the password has not been set, it returns null.
     *
     * This will also update the password hash, but does not update the encrypted password.
     */
    public synchronized void setPassword(String l_value) {
        this.password = l_value;
        this.setPasswordHashFromPassword(password);
    }

    /** Sets the hash of the user's password.
     */
    public synchronized void setPasswordHash(String l_value) {
        this.passwordHash = l_value;
    }

    /** Sets the password hash from the specified password.
     */
    public synchronized void setPasswordHashFromPassword(String l_password) {
        this.passwordHash = hashPassword(l_password);
    }

    /** Sets whether or not file history should be stored for this file system.
     */
    public synchronized void setStoreFileHistory(boolean l_value) {
        this.storeFileHistory = l_value;
    }

    /** Sets the base for the relative base file location path.
     */
    public synchronized void setRelativePathBase(String l_value) {
        this.relativePathBase = l_value;
    }

    @Override
    public synchronized String toString() {
        return this.name;
    }

    /** Converts the system setup to an xml element.
     */
    public synchronized org.jdom.Element toXML() {
        org.jdom.Element result = new org.jdom.Element("SystemSetup");
        result.setAttribute("Compress", this.compress ? "Y" : "N");
        result.setAttribute("Encrypt", this.encrypt ? "Y" : "N");
        result.setAttribute("StoreFileHistory", this.storeFileHistory ? "Y" : "N");
        if (null != this.passwordHash) {
            result.setAttribute("PasswordHash", this.passwordHash);
        }
        if (null != this.fileHistoryBaseLocation) {
            result.setAttribute("FileHistoryBaseLocation", this.fileHistoryBaseLocation);
        }
        if (null != this.encryptedPassword) {
            result.setAttribute("ep", Utilities.convertToHex(encryptedPassword));
        }
        return result;
    }
}
