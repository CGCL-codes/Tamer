package com.dfamaj.textproc.xwm.exceptions;

import com.dfamaj.textproc.xwm.Location;
import java.io.Serializable;

/**
 * exception levée lorsqu'on rencontre un token interdit dans un paramètre.
 *
 * @author <a href="mailto:david.andriana@free.fr">David Andriana</a>
 * @version 2.0 -- 2007-02-01 -- $Revision$
 * @since 2.0
 */
public class IllegalTokenInParamException extends LocatedIOException implements Serializable {

    /**
	 * la version de la classe, pour les sérialisations.
	 */
    private static final long serialVersionUID = 20070125L;

    /**
	 * constructeur.
	 *
	 * @nullable location
	 * @nullable token
	 */
    public IllegalTokenInParamException(final Location location, final String token) {
        super(location, "Illegal token in macro argument: [" + token + "]");
    }
}
