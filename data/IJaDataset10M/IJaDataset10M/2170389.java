package fr.aston.authentification.metiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * La classe Employe fournie l'ensemble des informations sur l'employ�.
 * 
 * @author Wolf Anthony
 */
public class Employe extends Utilisateur {

    /** The id manager. */
    protected int idManager;

    /** The login. */
    protected String login;

    /** The password. */
    protected String password;

    /** The nom. */
    protected String nom;

    /** The prenom. */
    protected String prenom;

    /** The date naissance. */
    protected Date dateNaissance;

    /** The email. */
    protected String email;

    /** The fonction. */
    protected String fonction;

    /**
	 * Constructeur par defaut.
	 */
    public Employe() {
        super();
    }

    /**
	 * Instantiates a new employe.
	 * 
	 * @param nom the nom
	 * @param prenom the prenom
	 * @param dateNaissance the date naissance
	 * @param login the login
	 * @param password the password
	 * @param email the email
	 * @param idUtilisateur the id utilisateur
	 */
    public Employe(int idManager, String login, String password, String nom, String prenom, Date dateNaissance, String email, String fonction, int idUtilisateur) {
        super();
        this.idManager = idManager;
        this.login = login;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.fonction = fonction;
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdManager() {
        return idManager;
    }

    public void setIdManager(int idManager) {
        this.idManager = idManager;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    /**
	 * M�thode d'affichage des donn�es de l'employ�.
	 * 
	 * @return La chaine de cract�res � afficher
	 */
    public String toString() {
        String affichageAttributsEmploye;
        affichageAttributsEmploye = super.toString();
        affichageAttributsEmploye += "\nLe nom : " + this.nom + "\n" + "Le prenom : " + this.prenom + "\n" + "La dateNaissance : " + this.dateNaissance + "\n" + "Le login : " + this.login + "\n" + "Le password : " + this.password + "\n" + "L'email : " + this.email;
        return affichageAttributsEmploye;
    }
}
