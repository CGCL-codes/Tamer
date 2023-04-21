package jpa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Persistence.Etudiant;
import form.EtudiantBean;
import form.RetourRechercherEtudiantBean;

public class ListeEtudiantJPA {

    /**
	* Retourne la liste des �tudiants stock�s dans la base.
	* @return La liste des �tudiants/erreurs.
	*/
    public RetourRechercherEtudiantBean rechercherEtudiants() {
        final RetourRechercherEtudiantBean lRetourRechercherEtudiant = new RetourRechercherEtudiantBean();
        final List<EtudiantBean> lListeEtudiant = new LinkedList<EtudiantBean>();
        final EtudiantBean etudiant1 = new EtudiantBean();
        final String prenom = "Monica";
        final String nom = "Cl�ment";
        final String email = "monica.clement@gmail.com";
        final String cursus = "Master2 GLRE";
        etudiant1.setPrenom(prenom);
        etudiant1.setNom(nom);
        etudiant1.setEmail(email);
        etudiant1.setCursus(cursus);
        lListeEtudiant.add(etudiant1);
        lRetourRechercherEtudiant.setListeEtudiant(lListeEtudiant);
        return lRetourRechercherEtudiant;
    }

    /**
	* Cr�e un nouvel �l�ment dans la liste des �tudiants
	* @param pPrenom Prenom du nouvel �tudiant
	* @param pNom Nom du nouvel �tudiant
	* @return Erreur s'il y a lieu
	*/
    public Etudiant creerEtudiant(final String pPrenom, final String pNom, final String pEmail, String pCursus) {
        return null;
    }
}
