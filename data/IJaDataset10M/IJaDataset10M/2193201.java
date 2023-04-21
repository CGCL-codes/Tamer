package org.fudaa.fudaa.lido.ihmhelper.gestion;

import java.util.Comparator;
import org.fudaa.dodico.corba.lido.SParametresPerteLigneCLM;

/**
 * @version      $Revision: 1.9 $ $Date: 2006-09-19 15:05:06 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public class PerteComparator implements Comparator {

    PerteComparator() {
    }

    public int compare(final Object o1, final Object o2) {
        return ((SParametresPerteLigneCLM) o1).xPerte < ((SParametresPerteLigneCLM) o2).xPerte ? -1 : ((SParametresPerteLigneCLM) o1).xPerte == ((SParametresPerteLigneCLM) o2).xPerte ? 0 : 1;
    }

    public boolean equals(final Object obj) {
        return false;
    }
}
