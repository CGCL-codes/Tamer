package com.common.security;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.common.exception.BusinessException;
import com.common.log.ILogger;
import com.common.log.LogFactory;

/**
 * Clase que representa el contexto del usuario
 * 
 * @author Fernando Abad
 * 
 */
public class User implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4080032510943718454L;

    private String name;

    private boolean logged;

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
        logged = false;
        name = "";
    }

    /**
	 * Funci�n que permite encriptar el password con MD5
	 * 
	 * @param aPassword
	 * @return
	 * @throws BusinessException
	 */
    public static String encriptarPassword(String aPassword) throws BusinessException {
        ILogger logger = LogFactory.getLogger(User.class);
        String methodId = "encriptarPassword";
        try {
            MessageDigest currentAlgorithm = MessageDigest.getInstance("MD5");
            currentAlgorithm.reset();
            currentAlgorithm.update(aPassword.getBytes());
            byte[] hash = currentAlgorithm.digest();
            String d = "";
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) d += "0";
                d += Integer.toString(v, 16).toUpperCase();
            }
            return d.toLowerCase();
        } catch (NoSuchAlgorithmException nsae) {
            logger.logError(methodId + "error al encriptar password:" + nsae, nsae);
            throw new BusinessException("Error al encriptar password");
        }
    }
}
