package org.jredway.model;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.List;

/**
 * Classe permettant de gèrer la classe Utilisateur
 * pour la persistence des données
 * 
 * @author killian.b
 * @version 1.0.0
 */
public class BaseUtilisateur {

    private Utilisateur membre;

    private UserService userService = UserServiceFactory.getUserService();

    private User user = userService.getCurrentUser();

    /**
     * constructeur sans paramètres
     * 
     * @since 1.0.0
     */
    public BaseUtilisateur() {
    }

    /**
     * Méthode qui ajoute un utilisateur dans la bdd
     * utilise la persistence
     * 
     * @see PMF
     * @param compte
     * @param nom
     * @param prenom
     * @param index
     * @since 1.0.0
     */
    public void ajouter(User compte, String nom, String prenom, int index) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        this.membre = new Utilisateur(compte, nom, prenom, index);
        try {
            pm.makePersistent(membre);
        } finally {
            pm.close();
        }
    }

    /**
     * Méthode qui supprime un utilisateur
     * dans la bdd
     * 
     * @see PMF
     * @param compte
     * @since 1.0.0
     */
    public void supprimer(User compte) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Utilisateur.class, ":p.contains(compte)");
        query.deletePersistentAll(compte);
    }

    /**
     * Créer un objet détachable pour la modification 
     * car sinon impossible
     * 
     * @see PMF
     * @return detached, retourne le currentUser pour modification
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public Utilisateur getUtilisateur() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Utilisateur uti, detached = null;
        Query query = pm.newQuery(Utilisateur.class, ":p.contains(compte)");
        List<Utilisateur> results = (List<Utilisateur>) query.execute(this.user);
        try {
            uti = pm.getObjectById(Utilisateur.class, results.get(0).getKey().getId());
            detached = pm.detachCopy(uti);
        } finally {
            pm.close();
        }
        return detached;
    }

    /**
     * Mise à jour des informations de
     * l'utilisateur envoyer en paramètre
     * 
     * @see PMF
     * @param e
     * @since 1.0.0
     */
    public void modifier(Utilisateur e) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(e);
        } finally {
            pm.close();
        }
    }

    /**
     * Teste l'existence de l'utilisateur
     * courant
     * 
     * @see PMF
     * @return res, true=existe false=existe!
     * @since 1.0.0
     */
    public boolean existe() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        boolean res = false;
        try {
            Query query = pm.newQuery(Utilisateur.class, ":p.contains(compte)");
            if (query.execute(this.user).toString() != "[]") {
                res = true;
            }
        } finally {
            pm.close();
        }
        return res;
    }
}
