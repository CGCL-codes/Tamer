package org.jpos.core;

/**
 * Object is Configurable
 * @author <a href="mailto:apr@cs.com.uy">Alejandro P. Revilla</a>
 * @version $Revision: 2854 $ $Date: 2010-01-02 05:34:31 -0500 (Sat, 02 Jan 2010) $
 * @since jPOS 1.2
 */
public interface Configurable {

    /**
    * @param cfg Configuration object
    * @throws ConfigurationException
    */
    public void setConfiguration(Configuration cfg) throws ConfigurationException;
}
