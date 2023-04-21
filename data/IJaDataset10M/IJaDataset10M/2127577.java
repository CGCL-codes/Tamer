package org.apache.batik.script;

import java.net.URL;

/**
 * An hight level interface that represents a factory allowing
 * to create instances of a particular <code>Interpreter</code> interface
 * implementation.
 * @author <a href="mailto:cjolif@ilog.fr">Christophe Jolif</a>
 * @version $Id: InterpreterFactory.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public interface InterpreterFactory {

    /**
     * Returns the mime-type to register this interpereter with.
     */
    public String getMimeType();

    /**
     * This method should create an instance of <code>Interpreter</code>
     * interface implementation.
     * 
     * @param documentURL the url for the document which will be scripted
     */
    public Interpreter createInterpreter(URL documentURL);
}
