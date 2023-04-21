package bdd;

import java.util.Iterator;
import java.util.Vector;

/**
 * Classe qui représente un étudiant
 * @author Alexander Remen et Tonya Vo Thanh
 *
 */
public class Etudiant extends Personne {

    private Vector<Groupe> groupes;

    private Promotion promo;

    /**
	 * Constructeur qui crée un étudiant 
	 * @param num
	 * @param username
	 * @param password
	 * @param nom
	 * @param prenom
	 * @param adresse
	 * @param email
	 * @param tel
	 */
    public Etudiant(String num, String username, String password, String nom, String prenom, String adresse, String email, int tel) {
        super(num, username, password, nom, prenom, adresse, email, tel);
        groupes = new Vector<Groupe>();
    }

    /**
	 * @return Returns the promo.
	 */
    public Promotion getPromo() {
        return promo;
    }

    /**
	 * @param promo The promo to set.
	 */
    public void setPromo(Promotion promo) {
        this.promo = promo;
    }

    /**
	 * Méthode qui ajoute un groupe a l'étudiant
	 * @param g
	 */
    public void ajouteGroupe(Groupe g) {
        groupes.add(g);
    }

    /**
	 * @return la liste des groupes
	 */
    public Vector<Groupe> getGroupes() {
        return groupes;
    }

    /**
	 * @param groupes the groupes to set
	 */
    public void setGroupes(Vector<Groupe> groupes) {
        this.groupes = groupes;
    }

    /**
	 * Méthode qui dit si un étudiant fait parti d'un groupe donné en paramètre
	 * @param g le groupe
	 * @return true si oui, false sinon
	 */
    public boolean estDuGroupe(Groupe g) {
        return groupes.contains(g);
    }

    private String AfficheGroupes() {
        String txt = "";
        Iterator i = groupes.iterator();
        while (i.hasNext()) {
            txt = txt + ((Groupe) i.next()).toString() + " ";
        }
        return txt;
    }
}
