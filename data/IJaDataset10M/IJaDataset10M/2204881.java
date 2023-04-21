package com.kongur.network.erp.manager.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eyeieye.melody.util.StringUtil;
import com.kongur.network.erp.manager.system.PasswordManager;
import com.kongur.network.erp.util.digest.MessageDigest;

/**
 * @author gaojf
 * @version $Id: PasswordManagerImpl.java,v 0.1 2011-11-22 ����10:45:16 gaojf Exp $
 */
@Service("passwordManager")
public class PasswordManagerImpl implements PasswordManager {

    @Autowired
    private MessageDigest messageDigest;

    @Override
    public boolean isPasswordValid(String dbPassword, String password, String encoding) {
        if (StringUtil.isBlank(dbPassword) || StringUtil.isBlank(password)) return false;
        String passwordEncode = passwordEncode(password, encoding);
        if (dbPassword.equals(passwordEncode)) return true;
        return false;
    }

    @Override
    public String passwordEncode(String password, String encoding) {
        return messageDigest.digest(password, encoding);
    }
}
