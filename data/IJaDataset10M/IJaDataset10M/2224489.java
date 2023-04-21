package jadacommon.operation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

/**
 * @author Cris ,Andrea1979
 */
public class Crypting {

    /**
         *  used to encript
         */
    Cipher ecipher;

    /**
         * used to decritp
         */
    Cipher dcipher;

    /**
         * 8 - byte salt
         */
    byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    /**
         * iteration count
         */
    int iterationCount = 19;

    /**
         * Constructor for a new class of Crypting , by a specifed password used to decript and encript
         * @param passPhrase 
         */
    public Crypting(String passPhrase) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    /**
         * Encript an inputStream and save in an specifed File
         * @param input the inpuntStream to Encript
         * @param fileName the File path and name ( with extension)
         * @return a pointer to the file created and Encripted or null if there was a problem
         */
    public File encrypt(InputStream input, String fileName) {
        try {
            byte[] bRead = new byte[input.available()];
            input.read(bRead);
            byte[] enc = ecipher.doFinal(bRead);
            return writeFile(enc, fileName);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    /**
         * Decript a File 
         * @param f the file to Decript
         * @return an inputStream Decripted
         */
    public InputStream decrypt(File f) {
        try {
            byte[] todec = readFile(f);
            byte[] dec = dcipher.doFinal(todec);
            return new ByteArrayInputStream(dec);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    /**
         * Encript a String 
         * @param String
         * 
         * @return the string encrypted
         */
    public String encrypt(String stri) {
        try {
            byte[] b = stri.getBytes("UTF8");
            byte[] result = ecipher.doFinal(b);
            return new sun.misc.BASE64Encoder().encode(result);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Crypting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Crypting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Crypting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
         * Decript a String
         * @param str string to be decrypted
         * @return the string decrypted
         */
    public String decrypt(String str) {
        try {
            byte[] toDec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = dcipher.doFinal(toDec);
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    private byte[] readFile(File f) throws IOException {
        try {
            FileInputStream pel = new FileInputStream(f);
            DataInputStream read = new DataInputStream(pel);
            byte[] result = new byte[pel.available()];
            read.readFully(result);
            read.close();
            return result;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private File writeFile(byte[] b, String name) {
        try {
            File temp = new File(name);
            DataOutputStream write = new DataOutputStream(new FileOutputStream(temp));
            write.write(b, 0, b.length);
            write.close();
            return temp;
        } catch (IOException ex) {
            Logger.getLogger(Crypting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
