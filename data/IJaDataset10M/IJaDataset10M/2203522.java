package com.objectwave.xjr;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.apache.log4j.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * This class is used to....
 *
 * @author  trever
 * @version  $Id: XJRTest.java,v 1.1.1.1 2002/05/15 16:50:02 trever Exp $
 * @author:  Trever Shick
 */
public class XJRTest {

    static Category log = Category.getInstance(XJRTest.class);

    /**
	 * Starts the application.
	 *
	 * @param  args an array of command-line arguments
	 */
    public static void main(String[] args) {
        HashMap subMap = new HashMap();
        XJRFactory factory = new XJRFactory(subMap);
        factory.setDefaultPackage(args[1]);
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setValidating(true);
            SAXParser parser = parserFactory.newSAXParser();
            long start = System.currentTimeMillis();
            File f = new File(args[0]);
            InputSource is = new InputSource(f.toURL().toString());
            Object obj = factory.build(parser, is);
            long end = System.currentTimeMillis();
            log.debug(factory.errors());
            log.debug(obj);
            log.debug("Took :" + (end - start) + " ms");
            start = System.currentTimeMillis();
            f = new File(args[0]);
            is = new InputSource(f.toURL().toString());
            obj = factory.build(parser, is);
            end = System.currentTimeMillis();
            log.debug(factory.errors());
            log.debug(obj);
            log.debug("Took :" + (end - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
