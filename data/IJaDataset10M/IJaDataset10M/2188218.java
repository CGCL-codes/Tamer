package org.tscribble.bitleech.core.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DES implements Cryptography {

    private static String UTF_8 = "UTF-8";

    private static String SIMETRIC_ALGORITHM = "DES";

    private Cipher ecipher;

    private Cipher dcipher;

    byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    int iterationCount = 19;

    private static PrintStream err = System.err;

    private static int EXIT = 0;

    private void initialize(SecretKey key) {
        try {
            ecipher = Cipher.getInstance(DES.SIMETRIC_ALGORITHM);
            dcipher = Cipher.getInstance(DES.SIMETRIC_ALGORITHM);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    /**
	 * use a pass phrase 
	 * (a string password of multiple words) for encryption. 
	 */
    public DES(String passPhrase) {
        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public DES(SecretKey key) {
        this.initialize(key);
    }

    public DES(File filePath) {
        SecretKey key = null;
        try {
            key = getKey(filePath.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (key == null) {
            err.println("Key inv?lida");
            System.exit(EXIT);
        }
        this.initialize(key);
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes(DES.UTF_8);
            byte[] enc = ecipher.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        try {
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    private static SecretKey getKey(String filePath) {
        File file = new File(filePath);
        SecretKey key = null;
        boolean exist = file.exists();
        if (!exist) {
            PrintWriter printFile = null;
            try {
                key = KeyGenerator.getInstance("DES").generateKey();
                printFile = new PrintWriter(new FileWriter(file));
                printFile.print(new sun.misc.BASE64Encoder().encode(key.getEncoded()));
            } catch (NoSuchAlgorithmException e) {
                err.println("Algoritmo criptogr?fico inv?lido.");
            } catch (IOException e) {
                err.println("Erro de I/O (Gravar em arquivo).");
            } finally {
                if (printFile != null) {
                    printFile.close();
                }
            }
        } else {
            if (file.length() <= 1) {
                file.delete();
                return getKey(filePath);
            }
            String skey = getKeyInFile(filePath);
            if (skey == null) {
                throw new NullPointerException("O arquivo n?o possui nenhuma \"key\"");
            }
            try {
                byte[] decodedKey = new sun.misc.BASE64Decoder().decodeBuffer(skey);
                DESKeySpec desKeySpec = new DESKeySpec(decodedKey);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                key = keyFactory.generateSecret(desKeySpec);
            } catch (IOException e) {
            } catch (InvalidKeyException e) {
                err.println("Key recuperada do arquivo, key inv?lida.");
            } catch (NoSuchAlgorithmException e) {
                err.println("Algoritmo criptogr?fico inv?lido.");
            } catch (InvalidKeySpecException e) {
                err.println("Especificacao inv?lida da key recuperada.");
            }
        }
        return key;
    }

    private static String getKeyInFile(String filePath) {
        BufferedReader fileReader = null;
        StringBuffer key = new StringBuffer();
        try {
            File file = new File(filePath);
            fileReader = new BufferedReader(new FileReader(file));
            String line = fileReader.readLine();
            while (line != null) {
                key.append(line);
                line = fileReader.readLine();
            }
        } catch (FileNotFoundException e) {
            err.println("Arquivo n?o encontrado.");
        } catch (IOException e) {
            err.println("Erro de I/O (Leitura do arquivo).");
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    err.println("Erro de I/O (Fechar arquivo).");
                }
            }
        }
        return key.toString();
    }
}
