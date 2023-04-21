package org.elephantt.javabook.client;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.NameValuePair;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A PostMethod that supports signing the request according to FB signature rules.
 * <p/>
 * Once the sign(String) method is called, the parameters cannot be mutated.
 *
 * Modified by Progiweb (Java 1.4 compatibility)
 */
public class FacebookPostMethod extends PostMethod {

    private boolean signed = false;

    public boolean removeParameter(String s, String s1) throws IllegalArgumentException {
        verifyState();
        return super.removeParameter(s, s1);
    }

    public void addParameter(String s, String s1) throws IllegalArgumentException {
        verifyState();
        super.addParameter(s, s1);
    }

    public void addParameter(NameValuePair nameValuePair) throws IllegalArgumentException {
        verifyState();
        super.addParameter(nameValuePair);
    }

    public void addParameters(NameValuePair[] nameValuePairs) {
        verifyState();
        super.addParameters(nameValuePairs);
    }

    public boolean removeParameter(String s) throws IllegalArgumentException {
        verifyState();
        return super.removeParameter(s);
    }

    /**
   * Signs the POST according to FB sig rules
   */
    public void sign(String secretKey) {
        NameValuePair[] params = getParameters();
        List sigParams = new ArrayList(params.length);
        for (int i = 0; i < params.length; i++) {
            NameValuePair nvp = params[i];
            sigParams.add(nvp.getName() + "=" + nvp.getValue());
        }
        addParameter("sig", generateSignature(sigParams, secretKey));
        signed = true;
    }

    /**
   * Generates a sig over the params according to FB sig rules
   *
   * @param params
   * @return
   */
    private String generateSignature(List params, String secretKey) {
        StringBuffer buffer = new StringBuffer();
        Collections.sort(params);
        for (Iterator it = params.iterator(); it.hasNext(); ) {
            buffer.append(it.next());
        }
        buffer.append(secretKey);
        StringBuffer result = new StringBuffer();
        byte[] digest = createDigester().digest(buffer.toString().getBytes());
        for (int i = 0; i < digest.length; i++) {
            byte b = digest[i];
            result.append(Integer.toHexString((b & 0xf0) >>> 4));
            result.append(Integer.toHexString(b & 0x0f));
        }
        return result.toString();
    }

    private MessageDigest createDigester() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 MessageDigest algo missing");
        }
    }

    private void verifyState() {
        if (signed) {
            throw new IllegalStateException("cannot modify parameters after parameter set is signed");
        }
    }
}
