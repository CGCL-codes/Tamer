package net.sourceforge.bibliotheque.modele.dao.hibernate;

import net.sourceforge.bibliotheque.modele.Auteur;
import net.sourceforge.bibliotheque.modele.dao.DAOException;

public class TestDAO {

    public static void main(String[] args) {
        Auteur a = new Auteur();
        a.setNom("Nom10");
        a.setPrenom("Prénom10");
        AuteurDAOImpl dao = new AuteurDAOImpl();
        try {
            dao.addAuteur(a);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }
}
