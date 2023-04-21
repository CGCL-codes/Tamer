package org.fudaa.dodico.all;

import org.fudaa.dodico.corba.sisyphe.SParametresSisypheCOND;
import org.fudaa.dodico.sisyphe.DParametresSisyphe;

/**
 * @version      $Revision: 1.2 $ $Date: 2006-10-19 14:12:28 $ by $Author: deniger $
 * @author       Mickael Rubens 
 */
public final class MainTestCond {

    private MainTestCond() {
    }

    /**
   * @param args
   */
    public static void main(final String[] args) {
        System.out.println("1)Test de lecture/ecriture du fichier .cond....");
        SParametresSisypheCOND params = new SParametresSisypheCOND();
        params = DParametresSisyphe.litParametresCOND("/home/users/rubens/sisyphe_fic/cas/ca1_3");
        System.out.println("2)ecriture du fichier .cond....");
        DParametresSisyphe.ecritParametresCOND("test.ecritCOND", params);
        params = DParametresSisyphe.litParametresCOND("test.ecritCOND");
        DParametresSisyphe.ecritParametresCOND("ca1_3", params);
        System.out.println("3)Fin du test de lecture/Ecriture du fichier .cond...");
    }
}
