package net.suberic.pooka.resource;

import net.suberic.util.*;
import net.suberic.pooka.ssl.*;
import net.suberic.pooka.*;
import javax.activation.*;
import java.net.*;
import java.io.*;

/**
 * A ResourceManager which uses files.
 */
public class FileResourceManager extends ResourceManager {

    /**
   * Creates a VariableBundle to be used.
   */
    public VariableBundle createVariableBundle(String fileName, VariableBundle defaults) {
        try {
            java.io.File f = new java.io.File(fileName);
            if (!f.exists()) f.createNewFile();
            return new net.suberic.util.VariableBundle(f, defaults);
        } catch (java.io.IOException ioe) {
            return defaults;
        }
    }

    /**
   * Creates a MailcapCommandMap to be used.
   */
    public MailcapCommandMap createMailcap(String fileName) throws java.io.IOException {
        return new FullMailcapCommandMap(fileName);
    }

    /**
   * Creates a PookaTrustManager.
   */
    public PookaTrustManager createPookaTrustManager(javax.net.ssl.TrustManager[] pTrustManagers, String fileName) {
        return new PookaTrustManager(pTrustManagers, fileName);
    }

    public java.io.InputStream getInputStream(String pFileName) throws java.io.IOException {
        try {
            URL url = new URL(pFileName);
            return url.openStream();
        } catch (MalformedURLException mue) {
            return new FileInputStream(new File(pFileName));
        }
    }

    public java.io.OutputStream getOutputStream(String pFileName) throws java.io.IOException {
        return new FileOutputStream(new File(pFileName));
    }
}
