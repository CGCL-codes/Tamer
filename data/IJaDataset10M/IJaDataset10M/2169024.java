package com.earnware.xml;

public class XmlSerializerException extends Exception {

    public XmlSerializerException(String message) {
        super(message);
    }

    public XmlSerializerException(Exception e) {
        super(e);
    }
}
