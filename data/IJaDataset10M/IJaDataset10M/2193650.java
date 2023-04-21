package org.dbreplicator.repconsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.*;
import org.dbreplicator.replication.PathHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.*;

public class EncryptDecryptPassword {

    private DesEncrypter encrypter;

    private String publisherpassword = "", pubYes = "";

    private String subscriberpassword = "", subyes = "";

    private SecretKey key;

    public EncryptDecryptPassword() {
        getPasswordFromEndUser();
        initialiseDesEncryptor();
        encryptPassword();
    }

    private void initialiseDesEncryptor() {
        try {
            key = getSecretKey();
            String key1 = new String(key.getEncoded());
            encrypter = new DesEncrypter(key);
        } catch (Exception ex) {
        }
    }

    private void getPasswordFromEndUser() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Press Y to encrypt password for publication datasource else N :");
            pubYes = (input.readLine());
            pubYes = pubYes.trim();
            if (pubYes.equalsIgnoreCase("y")) {
                System.out.print("Enter Publisher DataSource Password  : ");
                publisherpassword = (input.readLine());
                publisherpassword = publisherpassword.trim();
                if (publisherpassword.equalsIgnoreCase("\n") || publisherpassword.equalsIgnoreCase("")) {
                    System.out.println("         Invalid Password     ");
                    getPasswordFromEndUser();
                }
            } else if (pubYes.equalsIgnoreCase("n")) {
                System.out.print("Enter Subscriber DataSource Password : ");
                subscriberpassword = (input.readLine());
                subscriberpassword = subscriberpassword.trim();
                if (subscriberpassword.equalsIgnoreCase("\n") || subscriberpassword.equalsIgnoreCase("")) {
                    System.out.println("        Invalid Password     ");
                    getPasswordFromEndUser();
                }
            } else {
                System.out.println("       Invalid Option     ");
                System.out.print("       Continue(Y/N)  :");
                String tes = (input.readLine());
                if (tes.equalsIgnoreCase("y")) getPasswordFromEndUser(); else System.exit(00);
            }
        } catch (Exception e) {
        }
    }

    private void encryptPassword() {
        if (pubYes.equalsIgnoreCase("y")) {
            publisherpassword = encrypter.encrypt(publisherpassword);
            writePublisherPassword("PASSWORD", publisherpassword);
        } else if (pubYes.equalsIgnoreCase("n")) {
            subscriberpassword = encrypter.encrypt(subscriberpassword);
            writeSubscriberPassword("PASSWORD", subscriberpassword);
        }
    }

    private void writePublisherPassword(String key, String valueTowrite) {
        try {
            File f = new File("." + File.separator + "pubconfig.ini");
            FileOutputStream fos = new FileOutputStream(f, true);
            OutputStreamWriter os = new OutputStreamWriter(fos);
            os.write("\r\n");
            os.write(key + "=" + valueTowrite);
            os.write("\r\n");
            os.flush();
        } catch (Exception ex) {
        }
    }

    private void writeSubscriberPassword(String key, String valueTowrite) {
        try {
            File f = new File("." + File.separator + "Subconfig.ini");
            FileOutputStream fos = new FileOutputStream(f, true);
            OutputStreamWriter os = new OutputStreamWriter(fos);
            os.write("\r\n");
            os.write(key + "=" + valueTowrite);
            os.write("\r\n");
            os.flush();
        } catch (Exception ex) {
        }
    }

    private static void EcodePassword(String password) {
        byte[] b = password.getBytes();
        byte five = (byte) 5;
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (b[i]);
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (b[i] / five * five);
        }
    }

    private SecretKey getSecretKey() {
        try {
            String path = "/org.dbreplicator/repconsole/secretKey.obj";
            java.net.URL url1 = getClass().getResource(path);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(url1.openStream()));
            SecretKey sk = (SecretKey) ois.readObject();
            return sk;
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return null;
    }

    public static void main(String[] args) {
        EncryptDecryptPassword en = new EncryptDecryptPassword();
    }
}
