package com.sun.identity.console.user.model;

import com.iplanet.am.sdk.AMHashMap;
import com.iplanet.sso.SSOException;
import com.sun.identity.console.base.model.AMAdminConstants;
import com.sun.identity.console.base.model.AMConsoleException;
import com.sun.identity.console.base.model.AMModelBase;
import com.sun.identity.idm.AMIdentity;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.idm.IdUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;

public class UMChangeUserPasswordModelImpl extends AMModelBase implements UMChangeUserPasswordModel {

    public static final String TOLVEN_CREDENTIAL_FORMAT_PKCS12 = "pkcs12";

    public static final String USERPKCS12_BINARY_ATTRNAME = "userPKCS12";

    public UMChangeUserPasswordModelImpl(HttpServletRequest req, Map map) {
        super(req, map);
    }

    /**
     * Returns user name.
     *
     * @param userId Universal ID of user.
     * @return user name.
     */
    public String getUserName(String userId) {
        String userName = "";
        try {
            AMIdentity amid = IdUtils.getIdentity(getUserSSOToken(), userId);
            userName = amid.getName();
        } catch (IdRepoException e) {
            debug.warning("UMChangeUserPasswordModelImpl.getUserName", e);
        }
        return userName;
    }

    /** 
     * Returns user password.
     *
     * @param userId Universal ID of user.
     * @return user password.
     * @throws AMConsoleException if password cannot be obtained.
     */
    public String getPassword(String userId) throws AMConsoleException {
        String password = "";
        String[] params = { userId, AMAdminConstants.ATTR_USER_PASSWORD };
        try {
            logEvent("ATTEMPT_READ_IDENTITY_ATTRIBUTE_VALUE", params);
            AMIdentity amid = IdUtils.getIdentity(getUserSSOToken(), userId);
            Set set = amid.getAttribute(AMAdminConstants.ATTR_USER_PASSWORD);
            if ((set != null) && !set.isEmpty()) {
                password = (String) set.iterator().next();
            }
            logEvent("SUCCEED_READ_IDENTITY_ATTRIBUTE_VALUE", params);
        } catch (SSOException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_PASSWORD, strError };
            logEvent("SSO_EXCEPTION_READ_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        } catch (IdRepoException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_PASSWORD, strError };
            logEvent("IDM_EXCEPTION_READ_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        }
        return password;
    }

    /**
     * Modifies user password.
     *
     * @param userId Universal ID of user.
     * @param password New password.
     * @throws AMConsoleException if password cannot be modified.
     */
    public void changePassword(String userId, String password) throws AMConsoleException {
        String[] params = { userId, AMAdminConstants.ATTR_USER_PASSWORD };
        try {
            logEvent("ATTEMPT_MODIFY_IDENTITY_ATTRIBUTE_VALUE", params);
            AMIdentity amid = IdUtils.getIdentity(getUserSSOToken(), userId);
            Map map = new HashMap(2);
            Set set = new HashSet(2);
            set.add(password);
            map.put(AMAdminConstants.ATTR_USER_PASSWORD, set);
            amid.setAttributes(map);
            amid.store();
            logEvent("SUCCEED_MODIFY_IDENTITY_ATTRIBUTE_VALUE", params);
        } catch (SSOException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_PASSWORD, strError };
            logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        } catch (IdRepoException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_PASSWORD, strError };
            logEvent("IDM_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        }
    }

    public void changePwd(String userId, String oldPassword, String newPassword) throws AMConsoleException {
        String[] params = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD };
        try {
            logEvent("ATTEMPT_MODIFY_IDENTITY_ATTRIBUTE_VALUE", params);
            AMIdentity amIdentity = IdUtils.getIdentity(getUserSSOToken(), userId);
            boolean verified = verifyOldPassword(amIdentity, userId, oldPassword.toCharArray());
            if (!verified) {
                String strError = "Authorized for password changed denied";
                String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
                logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
                throw new AMConsoleException(strError);
            }
            Map passwordMap = new AMHashMap(2);
            Set set = new HashSet(2);
            set.add(newPassword);
            passwordMap.put(AMAdminConstants.ATTR_USER_PASSWORD, set);
            Set<String> attributeNames = new HashSet<String>();
            attributeNames.add(USERPKCS12_BINARY_ATTRNAME);
            Map<String, byte[][]> userPKCS12Map = amIdentity.getBinaryAttributes(attributeNames);
            if (userPKCS12Map != null && userPKCS12Map.get(USERPKCS12_BINARY_ATTRNAME) != null) {
                KeyStore keyStore = null;
                try {
                    keyStore = KeyStore.getInstance(TOLVEN_CREDENTIAL_FORMAT_PKCS12);
                } catch (Exception e) {
                    String strError = "Could not get an instance of KeyStore to create user KeyStore: " + getErrorString(e);
                    String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
                    logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
                    throw new AMConsoleException(strError);
                }
                byte[][] oldUserPKCS12ByteArrs = (byte[][]) userPKCS12Map.get(USERPKCS12_BINARY_ATTRNAME);
                byte[] oldUserPKCS12 = oldUserPKCS12ByteArrs[0];
                ByteArrayInputStream bais = new ByteArrayInputStream(oldUserPKCS12);
                try {
                    keyStore.load(bais, oldPassword.toCharArray());
                } catch (Exception e) {
                    String strError = "Could not get user KeyStore using old password: " + getErrorString(e);
                    String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
                    logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
                    throw new AMConsoleException(strError);
                }
                ByteArrayOutputStream baos = null;
                try {
                    String alias = keyStore.aliases().nextElement();
                    PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, oldPassword.toCharArray());
                    keyStore.setKeyEntry(alias, privateKey, newPassword.toCharArray(), keyStore.getCertificateChain(alias));
                    baos = new ByteArrayOutputStream();
                } catch (Exception e) {
                    String strError = "Could not get Key from user KeyStore: " + getErrorString(e);
                    String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
                    logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
                    throw new AMConsoleException(strError);
                }
                try {
                    keyStore.store(baos, newPassword.toCharArray());
                } catch (Exception e) {
                    String strError = "Could not save user KeyStore: " + getErrorString(e);
                    String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
                    logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
                    throw new AMConsoleException(strError);
                }
                byte[] newUserPKCS12 = baos.toByteArray();
                byte[][] newUserPKCS12ByteArrs = new byte[1][];
                newUserPKCS12ByteArrs[0] = newUserPKCS12;
                Map<String, byte[][]> newUserPKCS12Map = newUserPKCS12Map = new HashMap<String, byte[][]>();
                newUserPKCS12Map.put(USERPKCS12_BINARY_ATTRNAME, newUserPKCS12ByteArrs);
                amIdentity.setBinaryAttributes(newUserPKCS12Map);
                amIdentity.setAttributes(passwordMap);
                amIdentity.store();
            } else {
                amIdentity.setAttributes(passwordMap);
                amIdentity.store();
            }
            logEvent("SUCCEED_MODIFY_IDENTITY_ATTRIBUTE_VALUE", params);
        } catch (SSOException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
            logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        } catch (IdRepoException e) {
            String strError = getErrorString(e);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
            logEvent("IDM_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        }
    }

    private boolean verifyOldPassword(AMIdentity amIdentity, String userId, char[] oldPassword) throws AMConsoleException {
        Set<String> userPasswordSet = null;
        try {
            userPasswordSet = amIdentity.getAttribute("userPassword");
        } catch (Exception ex) {
            String strError = "Authorized for password changed denied: " + getErrorString(ex);
            String[] paramsEx = { userId, AMAdminConstants.ATTR_USER_OLD_PASSWORD, strError };
            logEvent("SSO_EXCEPTION_MODIFY_IDENTITY_ATTRIBUTE_VALUE", paramsEx);
            throw new AMConsoleException(strError);
        }
        if (userPasswordSet == null || userPasswordSet.isEmpty()) {
        }
        String sshaPasswordString = (String) userPasswordSet.iterator().next();
        return checkPassword(oldPassword, sshaPasswordString);
    }

    private boolean checkPassword(char[] password, String sshaPasswordString) {
        byte[] passwordBytes = getBytes(password);
        return checkPassword(passwordBytes, sshaPasswordString);
    }

    private byte[] getBytes(char[] charArr) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = null;
            try {
                outputStreamWriter = new OutputStreamWriter(baos);
                outputStreamWriter.write(charArr, 0, charArr.length);
            } finally {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Could not convert char[] to byte[]", ex);
        }
    }

    /**
     * Check the supplied plaintext password against the supplied hashed SSHA password.
     * @param password
     * @param sshaPasswordString
     * @return true if the password compares true
     */
    private boolean checkPassword(byte[] password, String sshaPasswordString) {
        if (!sshaPasswordString.startsWith("{SSHA}")) return false;
        byte[] digestPlusSalt = Base64.decodeBase64(sshaPasswordString.substring(6).getBytes());
        byte[] salt = new byte[8];
        byte[] digestBytes = new byte[digestPlusSalt.length - 8];
        System.arraycopy(digestPlusSalt, 0, digestBytes, 0, digestBytes.length);
        System.arraycopy(digestPlusSalt, digestBytes.length, salt, 0, salt.length);
        byte[] passwordPlusSalt = new byte[password.length + salt.length];
        System.arraycopy(password, 0, passwordPlusSalt, 0, password.length);
        System.arraycopy(salt, 0, passwordPlusSalt, password.length, salt.length);
        MessageDigest sha1Digest = null;
        try {
            sha1Digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Could not get an SHA-1 MessageDigest instance to encode a password", ex);
        }
        byte[] passwordPlusSaltHash = sha1Digest.digest(passwordPlusSalt);
        return Arrays.equals(digestBytes, passwordPlusSaltHash);
    }
}
