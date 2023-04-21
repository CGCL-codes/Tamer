package org.fudaa.fudaa.dimduc;

import org.fudaa.fudaa.commun.impl.Fudaa;

/**
 * L'application cliente Dimduc : pour connexion au serveur.
 *
 * @version      $Revision: 1.10 $ $Date: 2007-01-19 13:14:38 $ by $Author: deniger $
 * @author       Christian Barou
 */
public class Dimduc {

    public static void main(final String[] args) {
        final Fudaa f = new Fudaa();
        f.launch(args, DimducImplementation.informationsSoftware(), true);
        f.startApp(new DimducImplementation());
    }
}
