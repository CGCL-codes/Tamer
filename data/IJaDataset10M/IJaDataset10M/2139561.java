package com.spring.rssReader.util;

import org.xmlpull.v1.XmlPullParserException;
import com.roha.xmlparsing.GeneralPullHandler;
import com.roha.xmlparsing.XMLHandler;

/**
 * User: ronald
 * Date: May 13, 2004
 * Time: 11:58:36 AM
 */
public class TestXmlPullParsing extends TestXmlParsing {

    public XMLHandler getXmlHandler() {
        try {
            return new GeneralPullHandler();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
