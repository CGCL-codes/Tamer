package com.hp.hpl.jena.xmloutput.impl;

/**
 * @author <a href="mailto:Jeremy.Carroll@hp.com">Jeremy Carroll</a>
 *
*/
public interface SimpleLogger {

    public void warn(String s);

    public void warn(String s, Exception e);
}
