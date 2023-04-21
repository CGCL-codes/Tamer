package org.fudaa.fudaa.refonde;

import java.io.IOException;

/**
 * Une exception lanc�e en cas de probl�me lors de la lecture/ecriture des
 * donn�es. Elle permet de faire la diff�rence entre les IOException lanc�es
 * de facon non control�es par les classes java et les IOException control�es
 * (� la suite d'un controle de coh�rence de la lecture).
 *
 * @version      $Id: RefondeIOException.java,v 1.4 2006-09-08 16:04:28 opasteur Exp $
 * @author       Bertrand Marchand
 */
class RefondeIOException extends IOException {

    /**
   * Cr�ation d'une exception sans message.
   */
    public RefondeIOException() {
        super();
    }

    /**
   * Cr�ation d'une exception avec un message.
   * @param s Message.
   */
    public RefondeIOException(String s) {
        super(s);
    }
}
