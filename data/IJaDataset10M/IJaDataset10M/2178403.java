package org.sepp.security;

import iaik.utils.Base64Exception;
import iaik.utils.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sepp.exceptions.SecurityServiceException;
import org.sepp.utils.XMLTags;
import org.sepp.utils.XMLUtils;
import org.w3c.dom.Document;

public class AuthenticationData {

    private Log log;

    private String identity;

    private byte[] randomNumber1;

    private byte[] randomNumber2;

    private byte[] sessionKey;

    public AuthenticationData(byte[] data) throws SecurityServiceException {
        log = LogFactory.getLog(this.getClass());
        parseAuthenticationData(Util.toASCIIString(data));
    }

    public AuthenticationData(String identity, byte[] randomNumber) throws SecurityServiceException {
        log = LogFactory.getLog(this.getClass());
        this.identity = identity;
        randomNumber1 = randomNumber;
    }

    public AuthenticationData(String identity, byte[] randomNumber1, byte[] randomNumber2) throws SecurityServiceException {
        log = LogFactory.getLog(this.getClass());
        this.identity = identity;
        this.randomNumber1 = randomNumber1;
        this.randomNumber2 = randomNumber2;
    }

    public AuthenticationData(byte[] sessionKey, byte[] randomNumber1, byte[] randomNumber2) throws SecurityServiceException {
        log = LogFactory.getLog(this.getClass());
        this.sessionKey = sessionKey;
        this.randomNumber1 = randomNumber1;
        this.randomNumber2 = randomNumber2;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public byte[] getRandomNumber1() {
        return randomNumber1;
    }

    public void setRandomNumber1(byte[] randomNumber1) {
        this.randomNumber1 = randomNumber1;
    }

    public byte[] getRandomNumber2() {
        return randomNumber2;
    }

    public void setRandomNumber2(byte[] randomNumber2) {
        this.randomNumber2 = randomNumber2;
    }

    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = sessionKey;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getBytes() {
        return Util.toASCIIBytes(toString());
    }

    @Override
    public String toString() {
        StringBuffer message = new StringBuffer();
        message.append("<" + XMLTags.AUTHENTICATION_DATA + ">\n");
        if (identity instanceof String) message.append("\t<" + XMLTags.PEER_ID + ">" + identity + "</" + XMLTags.PEER_ID + ">\n");
        if (randomNumber1 instanceof byte[]) message.append("\t<" + XMLTags.RANDOM_NUMBER1 + ">" + Util.toBase64String(randomNumber1) + "</" + XMLTags.RANDOM_NUMBER1 + ">\n");
        if (randomNumber2 instanceof byte[]) message.append("\t<" + XMLTags.RANDOM_NUMBER2 + ">" + Util.toBase64String(randomNumber2) + "</" + XMLTags.RANDOM_NUMBER2 + ">\n");
        if (sessionKey instanceof byte[]) message.append("\t<" + XMLTags.SESSION_KEY + ">" + Util.toBase64String(sessionKey) + "</" + XMLTags.SESSION_KEY + ">\n");
        message.append("</" + XMLTags.AUTHENTICATION_DATA + ">");
        return message.toString();
    }

    private void parseAuthenticationData(String message) throws SecurityServiceException {
        Document messageDocument = XMLUtils.xmlUtils.getDocument(message);
        identity = XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.AUTHENTICATION_DATA + "/" + XMLTags.PEER_ID, messageDocument);
        try {
            randomNumber1 = Util.Base64Decode(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.AUTHENTICATION_DATA + "/" + XMLTags.RANDOM_NUMBER1, messageDocument).getBytes());
            randomNumber2 = Util.Base64Decode(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.AUTHENTICATION_DATA + "/" + XMLTags.RANDOM_NUMBER2, messageDocument).getBytes());
            sessionKey = Util.Base64Decode(XMLUtils.xmlUtils.getStringFromXPath("/" + XMLTags.AUTHENTICATION_DATA + "/" + XMLTags.SESSION_KEY, messageDocument).getBytes());
        } catch (Base64Exception e) {
            log.fatal("Couldn't decode the random number from base64.");
            throw new SecurityServiceException("Couldn't obtain random number from authentication data.");
        }
        log.debug("AuthenticationData successfully parsed!");
    }
}
