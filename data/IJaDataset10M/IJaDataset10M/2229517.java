package org.egavas.xml;

import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;
import java.io.*;

/**
 *	Provides simple class to extend to use when you need to access the 
 *	xml properties.
 **/
public abstract class XMLHandler extends HandlerBase {

    /**
	 *	The object to which all values from the xml file will be loaded.
	 **/
    protected XMLProperties properties;

    /**
	 *	No argument constructor.
	 **/
    public XMLHandler() {
        properties = new XMLProperties();
    }

    /**
	 *	Return all of the properties from the xml file.
	 **/
    public XMLProperties getProperties() {
        return properties;
    }
}
