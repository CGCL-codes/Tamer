package org.infoeng.aki.test.ldap;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.naming.ldap.*;
import java.security.*;
import javax.naming.Context;
import javax.naming.directory.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import org.xml.sax.Attributes;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import org.bouncycastle.util.encoders.*;
import org.bouncycastle.asn1.x509.*;

public class LDAPPopulateExample extends DefaultHandler {

    public LDAPPopulateExample() {
        super();
    }

    private static X509Name baseDN = new X509Name("ou=Archives,dc=eforge,dc=localdomain");

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        Properties configProps = new Properties();
        configProps.load(new FileInputStream(args[0]));
        for (int m = 4000; m < 5000; m++) {
            File rfcDir = new File(args[2]);
            File rfcFile = new File(rfcDir.getCanonicalPath() + "/" + "rfc" + m + ".txt");
            if (rfcFile == null || !(rfcFile.exists())) continue;
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            DigestInputStream dis = new DigestInputStream(new FileInputStream(rfcFile), sha256);
            while (dis.read() != -1) continue;
            String fileHex = new String(Hex.encode(sha256.digest()));
            X509Name resourceName = new X509Name("sha256=" + fileHex + "," + baseDN.toString());
            System.out.println("resource name: " + resourceName.toString());
        }
    }
}
