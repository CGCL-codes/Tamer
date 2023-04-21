package com.hp.hpl.jena.util;

import java.io.InputStream;

/**
 *  Interface to things that open streams by some string reference
 * 
 * @author Andy Seaborne
 * @version $Id: Locator.java,v 1.3 2006/03/22 13:52:50 andy_seaborne Exp $
 */
public interface Locator {

    public InputStream open(String filenameOrURI);

    public String getName();
}
