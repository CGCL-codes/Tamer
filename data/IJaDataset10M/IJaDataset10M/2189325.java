package com.tad.tools.xml;

import org.w3c.dom.*;

public class GenericXMLParser extends XMLParser {

    private Configuration config;

    private XMLToObject xObject;

    public GenericXMLParser(Configuration config) {
        this.config = config;
        this.xObject = new XMLToObject(this.config);
    }

    private <T> T onElement(Class<?> clazz, Node el) {
        try {
            return (T) xObject.toObject(el, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T parse(Class<T> clazz, Node doc) {
        return onElement(clazz, doc);
    }
}
