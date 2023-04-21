package com.controller;

import com.session.Session;
import metier.livre.LivreEjbLocal;
import ejb.entity.Livre;
import java.util.List;
import com.formulaire.Article;
import com.session.Panier;
import ejb.entity.Commande;
import ejb.entity.CommandePK;
import ejb.entity.Journal;
import ejb.entity.Libraire;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import metier.commande.CommandeEjbLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wikola
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/panier/*")
public class PanierController {

    private LivreEjbLocal LivreEjbLocal;

    private CommandeEjbLocal commandeEjbLocal;

    @Autowired
    Session session;

    @RequestMapping(value = "ajoutLivre.htm", method = RequestMethod.POST)
    public ModelAndView chargementLivreAjax(@RequestParam(value = "id", required = true) int idLivre) {
        Livre livre = LivreEjbLocal.selectionnerLivre(idLivre);
        session.getPanier().ajouterLivre(livre);
        ModelAndView mv = new ModelAndView("panier/livreAjoutAjax");
        mv.addObject("panier", session.getPanier().getAffichagePanier());
        return mv;
    }

    @RequestMapping(value = "panier.htm", method = RequestMethod.GET)
    public ModelAndView afficherPanier() {
        ModelAndView mv = new ModelAndView("panier/panier");
        List<Article> panier = session.getPanier().getPanier();
        mv.addObject("panier", panier);
        mv.addObject("total", calculPrixTotal(panier));
        return mv;
    }

    private ModelAndView afficherArticles(String messageCarte) {
        ModelAndView mv = new ModelAndView("panier/articles");
        List<Article> panier = session.getPanier().getPanier();
        mv.addObject("panier", panier);
        mv.addObject("messageCarte", messageCarte);
        mv.addObject("total", calculPrixTotal(panier));
        return mv;
    }

    @RequestMapping(value = "plus.htm", method = RequestMethod.POST)
    public ModelAndView plusQttArticle(@RequestParam(value = "id", required = true) int idLivre) {
        Livre livre = LivreEjbLocal.selectionnerLivre(idLivre);
        session.getPanier().ajouterLivre(livre);
        return afficherArticles("");
    }

    @RequestMapping(value = "moins.htm", method = RequestMethod.POST)
    public ModelAndView moinsQttArticle(@RequestParam(value = "id", required = true) int idLivre) {
        Livre livre = LivreEjbLocal.selectionnerLivre(idLivre);
        session.getPanier().moinsUnQttLivre(livre);
        return afficherArticles("");
    }

    @RequestMapping(value = "retirer.htm", method = RequestMethod.POST)
    public ModelAndView retitrerArticle(@RequestParam(value = "id", required = true) int idLivre) {
        Livre livre = LivreEjbLocal.selectionnerLivre(idLivre);
        session.getPanier().retirerLivre(livre);
        return afficherArticles("");
    }

    private float calculPrixTotal(List<Article> articles) {
        float total = 0;
        for (Article article : articles) {
            total = total + article.getLivre().getLivreprix() * article.getQtt();
        }
        total = total * 100;
        total = Math.round(total);
        total = total / 100;
        return total;
    }

    @RequestMapping(value = "terminerCommande.htm", method = RequestMethod.POST)
    public ModelAndView terminerCommande(@RequestParam(value = "carte", required = true) String carte) {
        if (session.getClient() == null) {
            return afficherArticles("vous devez vous inscrire");
        }
        if (session.getPanier().getPanier().isEmpty()) {
            return afficherArticles("Vous devez avoir au moins 1 livre dans le panier");
        }
        if (carte.length() < 10) {
            return afficherArticles("Vous n'avez pas saisie 10 chiffres");
        }
        int id = commandeEjbLocal.newCommandeId();
        Date date = new Date();
        for (Article article : session.getPanier().getPanier()) {
            Journal journal = new Journal(-1);
            journal.setJournaldate(date);
            journal.setJournaldescription("Création de la commande");
            journal.setLibraire(new Libraire(1));
            journal = commandeEjbLocal.addJournal(journal);
            CommandePK commandePK = new CommandePK();
            commandePK.setClientid(session.getClient().getClientid());
            commandePK.setJournalId(journal.getJournalId());
            commandePK.setLivreid(article.getLivre().getLivreid());
            Commande commande = new Commande(commandePK);
            commande.setClient(session.getClient());
            commande.setCommandedate(date);
            commande.setCommandedatelivraison(date);
            commande.setCommandeetat("validée");
            commande.setJournal(journal);
            commande.setLivre(article.getLivre());
            commande.setCommandeid(id);
            commande.setCommandequantite(article.getQtt());
            commandeEjbLocal.commander(commande);
        }
        System.out.print("############### numéro de carte:" + carte + "###############");
        ModelAndView mv = new ModelAndView("commande/detailCommande");
        mv.addObject("commandes", commandeEjbLocal.listCommande(id));
        session.setPanier(new Panier());
        return mv;
    }

    private ModelAndView refuser() {
        ModelAndView mv = new ModelAndView("admin/refuser");
        return mv;
    }

    @RequestMapping(value = "listeCommande.htm", method = RequestMethod.GET)
    public ModelAndView afficherListeCommande(@RequestParam(value = "etat", required = false) String etat) {
        if (!session.getAdmin()) {
            return refuser();
        }
        Calendar Today = Calendar.getInstance();
        Date dateJ = Today.getTime();
        ModelAndView mv = new ModelAndView("admin/commande/commandeListe");
        if (etat == null || etat == "Du Jour") {
            mv.addObject("commandes", commandeEjbLocal.listCommandeGroupBy("", dateJ));
        } else {
            mv.addObject("commandes", commandeEjbLocal.listCommandeGroupBy(etat, null));
            mv.addObject("message", "Commande à l'état: ");
            mv.addObject("etat", etat);
        }
        return mv;
    }

    @RequestMapping(value = "listeCommandeRecherche.htm", method = RequestMethod.POST)
    public ModelAndView afficherListeCommandeRechercheId(@RequestParam(value = "id", required = false) int id) {
        if (!session.getAdmin()) {
            return refuser();
        }
        ModelAndView mv = new ModelAndView("admin/commande/commandeListe");
        List<Commande> commandes = new ArrayList<Commande>(0);
        commandes.add(commandeEjbLocal.listCommande(id).get(0));
        if (commandes.isEmpty()) {
            mv.addObject("message", "Aucune Commande ne porte cette Id");
        }
        mv.addObject("commandes", commandes);
        return mv;
    }

    @RequestMapping(value = "detailCommande.htm", method = RequestMethod.POST)
    public ModelAndView afficherDetailCommande(@RequestParam(value = "id", required = true) int id) {
        if (!session.getAdmin()) {
            return refuser();
        }
        ModelAndView mv = new ModelAndView("admin/commande/detailCommande");
        mv.addObject("commandes", commandeEjbLocal.listCommande(id));
        return mv;
    }

    private ModelAndView rafraichirCommandeListe(String etat) {
        if (!session.getAdmin()) {
            return refuser();
        }
        ModelAndView mv = new ModelAndView("admin/commande/commande");
        mv.addObject("commandes", commandeEjbLocal.listCommandeGroupBy(etat, null));
        return mv;
    }

    private void journalDeLog(Commande commande) {
        System.out.print("################### Journal de log pour la commande:" + commande.getCommandeid() + " ################### " + " Changement d'état:" + commande.getCommandeetat() + " ################### " + " Description du changment: " + commande.getJournal().getJournaldescription() + " ################### " + " le " + commande.getJournal().getJournaldate() + " ################### " + " par " + commande.getJournal().getLibraire().getLibrairenom());
    }

    @RequestMapping(value = "valider.htm", method = RequestMethod.POST)
    public ModelAndView validerCommande(@RequestParam(value = "id", required = true) int id) {
        if (!session.getAdmin()) {
            return refuser();
        }
        List<Commande> commandes = commandeEjbLocal.listCommande(id);
        String etat = commandes.get(0).getCommandeetat();
        for (Commande commande : commandes) {
            commande.setCommandeetat("Validée");
            commandeEjbLocal.updateCommande(commande);
        }
        journalDeLog(commandes.get(0));
        return rafraichirCommandeListe(etat);
    }

    @RequestMapping(value = "preparer.htm", method = RequestMethod.POST)
    public ModelAndView preparerCommande(@RequestParam(value = "id", required = true) int id) {
        if (!session.getAdmin()) {
            return refuser();
        }
        List<Commande> commandes = commandeEjbLocal.listCommande(id);
        String etat = commandes.get(0).getCommandeetat();
        for (Commande commande : commandes) {
            commande.setCommandeetat("en Préparation");
            commandeEjbLocal.updateCommande(commande);
        }
        journalDeLog(commandes.get(0));
        return rafraichirCommandeListe(etat);
    }

    @RequestMapping(value = "annuler.htm", method = RequestMethod.POST)
    public ModelAndView annulerCommande(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "raison", required = true) String raison) {
        if (!session.getAdmin()) {
            return refuser();
        }
        List<Commande> commandes = commandeEjbLocal.listCommande(id);
        String etat = commandes.get(0).getCommandeetat();
        Calendar Today = Calendar.getInstance();
        Date dateJ = Today.getTime();
        Libraire libraire = new Libraire(session.getClient().getClientid());
        libraire.setLibrairenom(session.getClient().getClientnom());
        libraire.setLibrairemdp(session.getClient().getClientmdp());
        for (Commande commande : commandes) {
            commande.getJournal().setJournaldate(dateJ);
            commande.getJournal().setJournaldescription(raison);
            commande.getJournal().setLibraire(libraire);
            commande.setCommandeetat("Annulée");
            commandeEjbLocal.decommander(commande);
        }
        journalDeLog(commandes.get(0));
        return rafraichirCommandeListe(etat);
    }

    @RequestMapping(value = "envoyer.htm", method = RequestMethod.POST)
    public ModelAndView preparerCommande(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "envoi", required = true) String envoi) {
        if (!session.getAdmin()) {
            return refuser();
        }
        List<Commande> commandes = commandeEjbLocal.listCommande(id);
        String etat = commandes.get(0).getCommandeetat();
        Date date = new Date(envoi);
        for (Commande commande : commandes) {
            commande.setCommandeetat("Envoyée");
            commande.setCommandedatelivraison(date);
            commandeEjbLocal.updateCommande(commande);
        }
        journalDeLog(commandes.get(0));
        return rafraichirCommandeListe(etat);
    }

    public void setLivreEjbLocal(LivreEjbLocal LivreEjbLocal) {
        this.LivreEjbLocal = LivreEjbLocal;
    }

    public void setCommandeEjbLocal(CommandeEjbLocal commandeEjbLocal) {
        this.commandeEjbLocal = commandeEjbLocal;
    }
}
