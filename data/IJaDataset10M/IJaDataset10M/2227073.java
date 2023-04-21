package com.cbsgmbh.xi.af.edifact.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import com.sap.aii.utilxi.misc.encrypt.Base64Encryptor;

/**
 * @author jutta.boehme
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments /
 */
public class Transformer {

    /**
	 * This method encodes base64.
	 * @param inByteArray					byte array to be encoded
	 * @return byte[]				
	 */
    public static byte[] encodeBase64(byte[] inByteArray) throws GeneralSecurityException {
        Base64Encryptor encryptor = new Base64Encryptor();
        return encryptor.encryptBytes2Bytes(inByteArray);
    }

    /**
	 * This method decodes base64.
	 * @param inByteArray					byte array to be decoded
	 * @return byte[]				
	 */
    public static byte[] decodeBase64(byte[] inByteArray) throws GeneralSecurityException {
        Base64Encryptor encryptor = new Base64Encryptor();
        return encryptor.decryptBytes2Bytes(inByteArray);
    }

    /**
	 * This method converts an InputStream to a byte[]
	 * @param inStream		input stream
	 * @return byte[]				
	 */
    public static byte[] convertInputStreamToByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream baOutputStream = null;
        if (inStream != null) {
            baOutputStream = new ByteArrayOutputStream();
            int i = inStream.read();
            while (i >= 0) {
                baOutputStream.write((char) i);
                i = inStream.read();
            }
            baOutputStream.close();
            return baOutputStream.toByteArray();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            InputStream inStream = new FileInputStream("C:\\TEMP\\EdifactTest\\Test03.txt");
            System.out.println(new String(convertInputStreamToByteArray(inStream)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
