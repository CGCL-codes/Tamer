package fr.miage.client.metier.entite;

import java.util.HashSet;
import java.util.Set;

/**
 * Client generated by hbm2java
 */
public class Client implements java.io.Serializable {

    /**
	 * Serialisation identifier
	 */
    private static final long serialVersionUID = 1L;

    private int idClient;

    private Ville ville;

    private String nom;

    private String prenom;

    private String adresse;

    private String telephone;

    private String email;

    private Set<Commande> commandes = new HashSet<Commande>(0);

    /** default constructor */
    public Client() {
    }

    public Client(int idClient) {
        this.idClient = idClient;
    }

    /** minimal constructor */
    public Client(int idClient, Ville ville, String nom, String prenom, String adresse, String telephone, String email) {
        this.idClient = idClient;
        this.ville = ville;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    /** full constructor */
    public Client(int idClient, Ville ville, String nom, String prenom, String adresse, String telephone, String email, Set<Commande> commandes) {
        this.idClient = idClient;
        this.ville = ville;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.commandes = commandes;
    }

    public int getIdClient() {
        return this.idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Ville getVille() {
        return this.ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
}