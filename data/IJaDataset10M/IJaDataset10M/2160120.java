package game.controler.gestionnaires.elementjeu;

import game.impl.objets.decors.*;
import game.model.objets.ElementDecors;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gab'z
 */
public class GestionnaireDecors {

    private static GestionnaireDecors Gdecors = null;

    private HashMap<String, ElementDecors> listeDecors = new HashMap<String, ElementDecors>();

    private HashMap<String, ArrayList<String>> listesIdPlanDecors = new HashMap<String, ArrayList<String>>();

    public GestionnaireDecors() {
        System.out.println("Initialisation des éléments du décors");
        initialiserDecors();
    }

    public static GestionnaireDecors getGdecors() {
        if (Gdecors == null) {
            Gdecors = new GestionnaireDecors();
        }
        return Gdecors;
    }

    public ElementDecors getElementDecors(String ID) {
        return listeDecors.get(ID);
    }

    private void initialiserDecors() {
        ajouterDecors(new p0_porte_chambre());
        ajouterDecors(new p0_calepin_medecin());
        ajouterDecors(new p1_porte_p0());
        ajouterDecors(new p1_porte_placard());
        ajouterDecors(new p1_porte_sas());
        ajouterDecors(new p1_porte_bureau_medecin());
        ajouterDecors(new p1_porte_patio());
        ajouterDecors(new p3_porte());
        ajouterDecors(new p3_garde_ko());
        ajouterDecors(new p4_commande_gateau());
        ajouterDecors(new p4_photo());
        ajouterDecors(new p4_crochet());
        ajouterDecors(new p5_porte_blanche());
    }

    private void ajouterDecors(ElementDecors decors) {
        listeDecors.put(decors.getId(), decors);
        ArrayList<String> listeStringDecors = listesIdPlanDecors.get(decors.getIdPlan());
        if (listeStringDecors == null) {
            listeStringDecors = new ArrayList<String>(0);
        }
        listeStringDecors.add(decors.getId());
        listesIdPlanDecors.put(decors.getIdPlan(), listeStringDecors);
    }

    public ArrayList<String> getListeDecorsPlan(String idPlanCourant) {
        System.out.println("getListeDecorsPlan id = " + idPlanCourant);
        ArrayList<String> listeDecorsPlan = listesIdPlanDecors.get(idPlanCourant);
        if (listeDecorsPlan == null) {
            return new ArrayList<String>(0);
        }
        return listeDecorsPlan;
    }
}
