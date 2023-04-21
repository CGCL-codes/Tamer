package metier.livre;

import ejb.entity.Livre;
import ejb.transition.LivreJpaController;
import ejb.transition.exceptions.IllegalOrphanException;
import ejb.transition.exceptions.NonexistentEntityException;
import ejb.transition.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Wikola
 */
@Stateless(mappedName = "LivreEjb")
public class LivreEjb implements LivreEjbRemote, LivreEjbLocal {

    LivreJpaController jpaLivre = new LivreJpaController();

    public List<Livre> selectionnerLivre(int min, int max) {
        System.out.print("###################### selectionnerLivre #################");
        List<Livre> livres = new ArrayList<Livre>(0);
        livres.addAll(jpaLivre.findLivreEntities());
        System.out.print("###################### nb Livre: " + livres.size() + " #################");
        return livres;
    }

    public List<Livre> topDix(int nbr) {
        return jpaLivre.topDix(nbr);
    }

    public List<Livre> selectionnerLivre(String recherche, Integer initLigne, Integer maxLigne) {
        return jpaLivre.findLivre(recherche, initLigne, maxLigne);
    }

    public Livre selectionnerLivre(int id) {
        return jpaLivre.findLivre(id);
    }

    public void updateLivre(Livre Livre) {
        try {
            jpaLivre.edit(Livre);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(LivreEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LivreEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LivreEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Livre addLivre(Livre Livre) {
        try {
            jpaLivre.create(Livre);
            return Livre;
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(LivreEjb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(LivreEjb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Livre> stockAlert() {
        return jpaLivre.stockAlert();
    }

    public String about() {
        return "tu es bon ";
    }
}
