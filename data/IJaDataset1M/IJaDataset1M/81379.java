package org.fudaa.fudaa.sinavi3;

import org.fudaa.dodico.corba.sinavi3.SParametresResultatBassinee;
import org.fudaa.dodico.corba.sinavi3.SParametresResultatsBassinees;
import org.fudaa.dodico.corba.sinavi3.SParametresResultatsCompletSimulation;

/**
 * @version $Version$
 * @author Mederic FARGEIX
 */
public class Sinavi3AlgorithmeBassinees {

    public static void calculApresSimu(final Sinavi3DataSimulation _donnees) {
        final SParametresResultatsCompletSimulation resultats = _donnees.params_.ResultatsCompletsSimulation;
        final int nombreEcluses = _donnees.listeEcluse_.listeEcluses_.size();
        int element;
        resultats.Bassinees = new SParametresResultatsBassinees[nombreEcluses];
        int[] nombreEnregistrementsTraites = new int[nombreEcluses];
        int[] nombreEnregistrementsTotal = new int[nombreEcluses];
        for (int i = 0; i < _donnees.listeResultatsBassineesSimu_.listeBassinees.length; i++) {
            nombreEnregistrementsTotal[_donnees.listeResultatsBassineesSimu_.listeBassinees[i].numero]++;
        }
        for (int j = 0; j < nombreEcluses; j++) {
            resultats.Bassinees[j] = new SParametresResultatsBassinees();
            resultats.Bassinees[j].indiceElement = j;
            resultats.Bassinees[j].resultatsBassineesUnitaires = new SParametresResultatBassinee[nombreEnregistrementsTotal[j]];
            for (int k = 0; k < nombreEnregistrementsTotal[j]; k++) {
                resultats.Bassinees[j].resultatsBassineesUnitaires[k] = new SParametresResultatBassinee();
            }
        }
        for (int i = 0; i < _donnees.listeResultatsBassineesSimu_.listeBassinees.length; i++) {
            element = _donnees.listeResultatsBassineesSimu_.listeBassinees[i].numero;
            resultats.Bassinees[element].resultatsBassineesUnitaires[nombreEnregistrementsTraites[element]].heure = _donnees.listeResultatsBassineesSimu_.listeBassinees[i].heure;
            resultats.Bassinees[element].resultatsBassineesUnitaires[nombreEnregistrementsTraites[element]].sens = _donnees.listeResultatsBassineesSimu_.listeBassinees[i].sens;
            resultats.Bassinees[element].resultatsBassineesUnitaires[nombreEnregistrementsTraites[element]].fausseBassinee = _donnees.listeResultatsBassineesSimu_.listeBassinees[i].fausseBassinee;
            nombreEnregistrementsTraites[element]++;
        }
    }
}
