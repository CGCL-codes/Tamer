package org.fudaa.dodico.simboat;

import java.util.Date;
import org.fudaa.dodico.objet.CDodico;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Classe Serveur de Simboat.
 * 
 * @version $Revision: 1.11 $ $Date: 2006-09-19 14:45:58 $ by $Author: deniger $
 * @author Nicolas Maillot
 */
public final class ServeurSimboat {

    private ServeurSimboat() {
    }

    public static void main(final String[] _args) {
        UsineLib.setAllLocal(false);
        final String nom = (_args.length > 0 ? _args[0] : CDodico.generateName("::simboat::ISimulateurSimboat"));
        CDodico.rebind(nom, UsineLib.createService(DSimulateurSimboat.class));
        System.out.println("Simboat server running... ");
        System.out.println("Name: " + nom);
        System.out.println("Date: " + new Date());
    }
}
