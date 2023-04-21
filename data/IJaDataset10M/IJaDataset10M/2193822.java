package metier.commande;

import ejb.entity.Commande;
import ejb.entity.CommandePK;
import ejb.entity.Journal;
import ejb.entity.Livre;
import ejb.transition.CommandeJpaController;
import ejb.transition.JournalJpaController;
import ejb.transition.LivreJpaController;
import ejb.transition.exceptions.IllegalOrphanException;
import ejb.transition.exceptions.NonexistentEntityException;
import ejb.transition.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author Wikola
 */
@Stateless(mappedName = "CommandeEjb")
public class CommandeEjb implements CommandeEjbRemote, CommandeEjbLocal {

    CommandeJpaController jpaCommande = new CommandeJpaController();

    LivreJpaController jpaLivre = new LivreJpaController();

    JournalJpaController jpaJournal = new JournalJpaController();

    public Commande selectionnerCommande(CommandePK commandePK) {
        return jpaCommande.findCommande(commandePK);
    }

    public Integer lastCommandeId() {
        return jpaCommande.lastCommandeId();
    }

    public Integer newCommandeId() {
        return (lastCommandeId() + 1);
    }

    public int verifCommande(Commande commande) {
        int stockApres = commande.getLivre().getLivrestock() - commande.getCommandequantite();
        if (stockApres < 0) {
            commande.setCommandequantite(commande.getLivre().getLivrestock());
        }
        if (commande.getLivre().getLivrestock() <= 0) {
            commande = null;
        }
        return stockApres;
    }

    public void commander(Commande commande) {
        try {
            Livre livre = commande.getLivre();
            livre.setLivrestock(livre.getLivrestock() - commande.getCommandequantite());
            livre.setLivrenbvente(livre.getLivrenbvente() + commande.getCommandequantite());
            if (livre.getLivrestock() <= 0) {
                livre.setLivreetat("en Réapprovisionnement");
            }
            jpaCommande.create(commande);
            jpaLivre.edit(livre);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void decommander(Commande commande) {
        try {
            Livre livre = commande.getLivre();
            livre.setLivrestock(livre.getLivrestock() + commande.getCommandequantite());
            livre.setLivrenbvente(livre.getLivrenbvente() - commande.getCommandequantite());
            if (livre.getLivreetat().equals("en Réapprovisionnement") && livre.getLivrestock() >= 0) {
                livre.setLivreetat("en Stock");
            }
            jpaCommande.edit(commande);
            jpaLivre.edit(livre);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCommande(Commande commande) {
        try {
            jpaCommande.edit(commande);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Commande> listCommande(int commandeId) {
        return jpaCommande.findCommande(commandeId);
    }

    public List<Commande> listCommandeGroupBy(String etat, Date date) {
        return jpaCommande.GroupByCommandeId(etat, date);
    }

    public void updateJournal(Journal journal) {
        try {
            jpaJournal.edit(journal);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Journal addJournal(Journal journal) {
        try {
            jpaJournal.create(journal);
            return journal;
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(CommandeEjb.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String about() {
        return "tu es bon ";
    }
}
