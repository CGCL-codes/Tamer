package org.fudaa.dodico.navigation;

import java.util.Comparator;
import org.fudaa.dodico.corba.navigation.IGeneration;
import org.fudaa.dodico.corba.navigation.IGenerationDeterministe;
import org.fudaa.dodico.corba.navigation.IGenerationDeterministeHelper;
import org.fudaa.dodico.corba.navigation.IGenerationHelper;
import org.fudaa.dodico.corba.navigation.IGenerationJournaliere;
import org.fudaa.dodico.corba.navigation.IGenerationJournaliereHelper;

/**
 * ComparateurGeneration:compare 2 generations regulieres.
 *
 * @version      $Revision: 1.5 $ $Date: 2004-04-30 07:18:34 $ by $Author: deniger $
 * @author       Fred Deniger 
 */
public class ComparateurGeneration implements Comparator {

    /**
   * La m�thode compare les longervalles des generations deterministes et
   * journalieres. Les criteres: <br>
   * <ul>
   * <li><code>dateDebut()</code> et <code>dateFin()</code></li>
   * <li><code>heureDebut()</code> puis <code>heurFin()</code></li>
   * <li>renvoie 0 si egalite parfaite.</li>
   * </ul>
   *
   * @param _entree1
   * @param _entree2
   * @return                        1 si _entree1 pr�c�de _entree2,-1
   * inversement et 0 si �gaux
   * @exception ClassCastException
   */
    public int compare(java.lang.Object _entree1, java.lang.Object _entree2) throws ClassCastException {
        long dateDebut1 = 0;
        long dateFin1 = 0;
        long dateDebut2 = 0;
        long dateFin2 = 0;
        long heureDebut1 = 0;
        long heureFin1 = 0;
        long heureDebut2 = 0;
        long heureFin2 = 0;
        IGeneration temp;
        IGenerationDeterministe tempD;
        IGenerationJournaliere tempJ;
        if (_entree1 instanceof IGeneration) {
            temp = IGenerationHelper.narrow((org.omg.CORBA.Object) _entree1);
            dateDebut1 = temp.dateDebut();
            dateFin1 = temp.dateFin();
            if (_entree1 instanceof IGenerationDeterministe) {
                tempD = IGenerationDeterministeHelper.narrow((org.omg.CORBA.Object) _entree1);
                heureDebut1 = tempD.premierInstant() - dateDebut1;
                heureFin1 = tempD.dernierInstant() - dateFin1;
            } else if (_entree1 instanceof IGenerationJournaliere) {
                tempJ = IGenerationJournaliereHelper.narrow((org.omg.CORBA.Object) _entree1);
                heureDebut1 = tempJ.heureDebut();
                heureFin1 = tempJ.heureFin();
            }
        } else {
            throw new ClassCastException("L'entree 1 n'est pas correcte");
        }
        if (_entree2 instanceof IGeneration) {
            temp = IGenerationHelper.narrow((org.omg.CORBA.Object) _entree2);
            dateDebut2 = temp.dateDebut();
            dateFin2 = temp.dateFin();
            if (_entree2 instanceof IGenerationDeterministe) {
                tempD = IGenerationDeterministeHelper.narrow((org.omg.CORBA.Object) _entree2);
                heureDebut2 = tempD.premierInstant() - dateDebut2;
                heureFin2 = tempD.dernierInstant() - dateFin2;
            } else if (_entree2 instanceof IGenerationJournaliere) {
                tempJ = IGenerationJournaliereHelper.narrow((org.omg.CORBA.Object) _entree2);
                heureDebut2 = tempJ.heureDebut();
                heureFin2 = tempJ.heureFin();
            }
        } else {
            throw new ClassCastException("L'entree 2 n'est pas correcte");
        }
        temp = null;
        tempD = null;
        tempJ = null;
        if (dateDebut1 > dateDebut2) {
            return 1;
        } else if (dateDebut1 < dateDebut2) {
            return -1;
        } else if (dateFin1 > dateFin2) {
            return 1;
        } else if (dateFin1 < dateFin2) {
            return -1;
        } else if (heureDebut1 > heureDebut2) {
            return 1;
        } else if (heureDebut1 < heureDebut2) {
            return -1;
        } else if (heureFin1 > heureFin2) {
            return 1;
        } else if (heureFin1 < heureFin2) {
            return -1;
        } else {
            return 0;
        }
    }
}
