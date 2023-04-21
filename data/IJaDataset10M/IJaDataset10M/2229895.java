package org.apache.batik.transcoder;

/**
 * A default <tt>ErrorHandler</tt> that throws a
 * <tt>TranscoderException</tt> when a fatal error occured and display
 * a message when a warning or an error occured.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id: DefaultErrorHandler.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class DefaultErrorHandler implements ErrorHandler {

    /**
     * Invoked when an error occured while transcoding.
     * @param ex the error informations encapsulated in a TranscoderException
     * @exception TranscoderException if the method want to forward
     * the exception
     */
    public void error(TranscoderException ex) throws TranscoderException {
        System.err.println("ERROR: " + ex.getMessage());
    }

    /**
     * Invoked when an fatal error occured while transcoding.
     * @param ex the fatal error informations encapsulated in a
     * TranscoderException
     * @exception TranscoderException if the method want to forward
     * the exception
     */
    public void fatalError(TranscoderException ex) throws TranscoderException {
        throw ex;
    }

    /**
     * Invoked when a warning occured while transcoding.
     * @param ex the warning informations encapsulated in a TranscoderException
     * @exception TranscoderException if the method want to forward
     * the exception
     */
    public void warning(TranscoderException ex) throws TranscoderException {
        System.err.println("WARNING: " + ex.getMessage());
    }
}
