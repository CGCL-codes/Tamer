package com.l2jserver.gameserver.network.gameserverpackets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import com.l2jserver.util.network.BaseSendablePacket;

/**
 * @author -Wooden-
 *
 */
public class BlowFishKey extends BaseSendablePacket {

    private static Logger _log = Logger.getLogger(BlowFishKey.class.getName());

    /**
	 * @param blowfishKey
	 * @param publicKey
	 */
    public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey) {
        writeC(0x00);
        byte[] encrypted = null;
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = rsaCipher.doFinal(blowfishKey);
        } catch (GeneralSecurityException e) {
            _log.log(Level.SEVERE, "Error While encrypting blowfish key for transmision (Crypt error): " + e.getMessage(), e);
        }
        writeD(encrypted.length);
        writeB(encrypted);
    }

    @Override
    public byte[] getContent() throws IOException {
        return getBytes();
    }
}
