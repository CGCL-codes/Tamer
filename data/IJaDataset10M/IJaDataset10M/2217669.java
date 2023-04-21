package org.fudaa.dodico.vag;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.fudaa.dodico.corba.objet.IConnexion;
import org.fudaa.dodico.corba.objet.IPersonne;
import org.fudaa.dodico.corba.vag.ICalculVag;
import org.fudaa.dodico.corba.vag.ICalculVagHelper;
import org.fudaa.dodico.corba.vag.IParametresVag;
import org.fudaa.dodico.corba.vag.IParametresVagHelper;
import org.fudaa.dodico.corba.vag.IResultatsVag;
import org.fudaa.dodico.corba.vag.IResultatsVagHelper;
import org.fudaa.dodico.corba.vag.SResultats05;
import org.fudaa.dodico.objet.CDodico;
import org.fudaa.dodico.objet.ServeurPersonne;
import org.fudaa.dodico.vag.DParametresVag;

/**
 * Une classe client de VagServeur.
 * 
 * @version $Revision: 1.2 $ $Date: 2006-10-19 14:12:28 $ by $Author: deniger $
 * @author Axel von Arnim
 */
public final class VagClient {

    private VagClient() {
    }

    /**
   * @param _args non utilise
   */
    public static void main(final String[] _args) {
        ICalculVag vag = null;
        System.out.println("VagClient");
        vag = ICalculVagHelper.narrow(CDodico.findServerByName("un-serveur-vag"));
        System.out.println("Connexion au serveur VAG : " + vag);
        System.out.println("Creation de la connexion");
        final IPersonne sp = ServeurPersonne.createPersonne("test-personne-vag", "test-organisme-vag");
        System.out.println("Connexion au serveur Vag : " + vag);
        final IConnexion c = vag.connexion(sp);
        final IParametresVag params = IParametresVagHelper.narrow(vag.parametres(c));
        try {
            params.parametres01(DParametresVag.litParametres01(new File("vag4051.01")));
            params.parametres02(DParametresVag.litParametres02(new File("vag4051.02")));
            params.parametres03(DParametresVag.litParametres03(new File("vag4051.03")));
            params.parametres04(DParametresVag.litParametres04(new File("vag4051.04")));
        } catch (final IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        vag.calcul(c);
        final IResultatsVag results = IResultatsVagHelper.narrow(vag.resultats(c));
        final SResultats05 resultats = results.resultats05();
        try {
            final FileWriter asc05 = new FileWriter("vag1000.05.asc");
            String string = "\n";
            for (int i = 0; i < resultats.orthogonales.length; i++) {
                asc05.write("\nOrthogonale " + resultats.orthogonales[i].numeroOrth + string);
                for (int j = 0; j < resultats.orthogonales[i].pas.length; j++) {
                    asc05.write("  numeroPas       : " + resultats.orthogonales[i].pas[j].numeroPas + string);
                    asc05.write("  numeroSegDepart : " + resultats.orthogonales[i].pas[j].numeroSegDepart + string);
                    asc05.write("  pointCourantX   : " + resultats.orthogonales[i].pas[j].pointCourantX + string);
                    asc05.write("  pointCourantY   : " + resultats.orthogonales[i].pas[j].pointCourantY + string);
                    asc05.write("  angleHoriz      : " + resultats.orthogonales[i].pas[j].angleHoriz + string);
                    asc05.write("  longueurOnde    : " + resultats.orthogonales[i].pas[j].longueurOnde + string);
                    asc05.write("  hauteurEau      : " + resultats.orthogonales[i].pas[j].hauteurEau + string);
                    asc05.write(string);
                }
            }
            asc05.close();
        } catch (final IOException e) {
            System.out.println(e);
        }
    }
}
