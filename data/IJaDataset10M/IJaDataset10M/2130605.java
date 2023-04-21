package fr.miage.dao.traitements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import fr.miage.client.metier.entite.Categorie;
import fr.miage.client.metier.entite.Client;
import fr.miage.client.metier.entite.Commande;
import fr.miage.client.metier.entite.Produit;
import fr.miage.client.metier.entite.Ville;

public class Utilitaire {

    public static void convertCategorie(Categorie c) {
        Set<Produit> prod = c.getProduits();
        Set<Categorie> cat = c.getCategories();
        Set<Categorie> ca = categorieListToArray(cat);
        c.setCategories(ca);
        Set<Produit> p = produitListToArray(prod);
        c.setProduits(p);
    }

    public static void convertClient(Client client) {
        Set<Commande> commandes = client.getCommandes();
        Iterator<Commande> it = commandes.iterator();
        while (it.hasNext()) {
            Commande commande = it.next();
            Set<Commande> com = commandeListToArray(commande.getProduit());
            commande.getProduit().setCommandes(com);
        }
    }

    public static void convertProduit(Produit produit) {
        Set<Commande> commandes = commandeListToArray(produit);
        produit.setCommandes(commandes);
        Categorie categorie = produit.getCategorie();
        Set<Produit> produits = produitListToArray(categorie.getProduits());
        categorie.setProduits(produits);
    }

    public static void convertCommande(Commande c) {
        Client client = c.getClient();
        Produit produit = c.getProduit();
        client.setCommandes(commandeClientToArray(client));
        Ville ville = client.getVille();
        ville.setClients(clientVilleToArray(ville));
        client.setVille(ville);
        produit.setCommandes(commandeListToArray(produit));
        Categorie categorie = produit.getCategorie();
        categorie.setProduits(produitListToArray(categorie.getProduits()));
        produit.setCategorie(categorie);
        c.setClient(client);
        c.setProduit(produit);
    }

    public static Set<Categorie> categorieListToArray(Set<Categorie> cl) {
        Set<Categorie> aList = new HashSet<Categorie>();
        Iterator<Categorie> it = cl.iterator();
        while (it.hasNext()) {
            aList.add((Categorie) it.next());
        }
        return aList;
    }

    public static Set<Produit> produitListToArray(Set<Produit> pl) {
        Set<Produit> aList = new HashSet<Produit>();
        Iterator<Produit> it = pl.iterator();
        while (it.hasNext()) {
            Produit produit = it.next();
            produit.setCommandes(commandeListToArray(produit));
            aList.add(produit);
        }
        return aList;
    }

    public static Set<Commande> commandeListToArray(Produit p) {
        Set<Commande> lc = new HashSet<Commande>();
        Iterator<Commande> it = p.getCommandes().iterator();
        while (it.hasNext()) {
            Commande commande = it.next();
            Client client = commande.getClient();
            Set<Commande> lcommande = commandeClientToArray(client);
            client.setCommandes(lcommande);
            Ville ville = client.getVille();
            Set<Client> clients = clientVilleToArray(ville);
            ville.setClients(clients);
            lc.add(commande);
        }
        return lc;
    }

    public static Set<Commande> commandeClientToArray(Client client) {
        Set<Commande> comandes = new HashSet<Commande>();
        Iterator<Commande> it = client.getCommandes().iterator();
        while (it.hasNext()) {
            comandes.add(it.next());
        }
        return comandes;
    }

    public static Set<Client> clientVilleToArray(Ville ville) {
        Set<Client> clients = new HashSet<Client>();
        Iterator<Client> it = ville.getClients().iterator();
        while (it.hasNext()) {
            Client c = it.next();
            clients.add(c);
        }
        return clients;
    }
}
