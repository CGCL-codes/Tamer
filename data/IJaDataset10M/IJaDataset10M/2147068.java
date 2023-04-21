package metier.modele;

import java.awt.Color;
import java.io.Serializable;

/**
 * @author Quentin
 *
 */
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 425425960839478518L;

    private String nom;

    private String project = null;

    private int id = -1;

    private Color color = Color.BLACK;

    private int status = INIT;

    public static int AJOUT = 0;

    public static int INIT = 1;

    public static int SUPPRESSION = 2;

    /**
	 * Constructeur 1
	 * @param nom
	 */
    public Utilisateur(String nom) {
        this.nom = nom;
    }

    /**
	 * Constructeur 2
	 * @param nom
	 * @param id
	 */
    public Utilisateur(String nom, int id) {
        this.nom = nom;
        this.id = id;
    }

    /**
	 * Constructeur 3
	 * @param nom
	 * @param id
	 * @param status
	 */
    public Utilisateur(String nom, int id, int status) {
        this.nom = nom;
        this.id = id;
        this.status = status;
    }

    /**
	 * Constructeur 4
	 * @param nom
	 * @param id
	 * @param status
	 * @param color
	 */
    public Utilisateur(String nom, int id, int status, Color color) {
        this.nom = nom;
        this.id = id;
        this.status = status;
        this.color = color;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString() {
        return this.nom + " (ID: " + this.id + ", Status: " + this.status + ")";
    }
}
