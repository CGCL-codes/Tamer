package delphorm.service.personne;

import java.util.Date;
import java.util.List;
import delphorm.entite.personne.Utilisateur;
import delphorm.web.exception.MauvaiseManipulationException;

public class ImplPersonne implements IPersonne {

    private delphorm.dao.personne.IUtilisateur daoPersonne;

    public Utilisateur enregistrerUtilisateur(Utilisateur u) {
        u.setDateEnregistrement(new Date());
        if (u.getGroupeUtilisateur().getNom() == null) {
            u.getGroupeUtilisateur().setNom(u.getLogin());
        } else if (!u.getGroupeUtilisateur().getNom().equals(u.getLogin())) {
            throw new MauvaiseManipulationException(this.getClass(), "Le groupe utilisateur devrait avoir le nom de login de l'utilisateur");
        }
        u = daoPersonne.ajouterUtilisateur(u);
        u.calculerDroits();
        return u;
    }

    public Utilisateur editerUtilisateur(Utilisateur u) {
        u = daoPersonne.editerUtilisateur(u);
        return u;
    }

    public delphorm.dao.personne.IUtilisateur getDaoPersonne() {
        return daoPersonne;
    }

    public void setDaoPersonne(delphorm.dao.personne.IUtilisateur daoPersonne) {
        this.daoPersonne = daoPersonne;
    }

    public Utilisateur identifierUtilisateur(Utilisateur u) {
        u = daoPersonne.identifierUtilisateur(u);
        if (u != null) u.calculerDroits();
        return u;
    }

    public List listerUtilisateurs(String type) {
        if (type.equals("nom")) {
            return daoPersonne.getAllOrderByNom();
        }
        return null;
    }

    public Utilisateur chercherUtilisateurParId(Long id) {
        Utilisateur u = daoPersonne.getUtilisateurParId(id);
        if (u != null) u.calculerDroits();
        return u;
    }

    public void supprimerUtilisateurParId(Long id) {
        daoPersonne.supprimerUtilisateurParId(id);
    }

    public boolean existeParLogin(String login) {
        if (daoPersonne.getUtilisateurParLogin(login) == null) return false; else return true;
    }

    public Utilisateur chercherUtilisateurParLogin(String login) {
        Utilisateur u = daoPersonne.getUtilisateurParLogin(login);
        if (u != null) u.calculerDroits();
        return u;
    }
}
