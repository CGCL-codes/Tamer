package net.sf.dsig;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface Strategy {

    FormContentHandler getFormContentHandler();

    void sign(PrivateKey privateKey, X509Certificate[] certificateChain) throws Exception;

    String signPlaintext(String plaintext, PrivateKey privateKey, X509Certificate[] certificateChain) throws Exception;
}
