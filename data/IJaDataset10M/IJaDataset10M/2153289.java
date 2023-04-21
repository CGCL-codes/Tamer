package org.proteomecommons.MSExpedite.SignalProcessing;

import java.net.URL;

/**
 *
 * @author takis
 */
public class ClassSignatureAttr extends SignatureAttr {

    Class algorithmClass;

    URL url = null;

    /** Creates a new instance of ClassSignatureAttr */
    public ClassSignatureAttr() {
        super();
        try {
            url = new URL("http://localhost");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setAlgorithmClass(Class c) {
        this.algorithmClass = c;
    }

    public Class getAlgorithmClass() {
        return algorithmClass;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
