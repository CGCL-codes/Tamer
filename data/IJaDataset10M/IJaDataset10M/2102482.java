package miage;

import java.io.File;

public class Atome {

    public static final int DOUBLON = 1;

    public static final int FRAPPE = 2;

    private int mode;

    private int ID;

    private int idArtiste;

    private int idAlbum;

    private int idFichier;

    private int idDossier;

    private String nomArtiste;

    private String nomAlbum;

    private String nomFichier;

    private String nomDossier;

    private int duree;

    private String titre;

    public Atome(int id, String nom, String table) {
        this.ID = id;
        if (table.equals("artiste")) {
            this.idArtiste = id;
            this.nomArtiste = nom;
        } else if (table.equals("album")) {
            this.idAlbum = id;
            this.nomAlbum = nom;
        }
        mode = FRAPPE;
    }

    public Atome(int id_fichier, String nom_fichier, String titre, int duree, int id_artiste, String nom_artiste, int id_album, String nom_album, int id_dossier, String nom_dossier) {
        this.ID = id_fichier;
        this.idFichier = id_fichier;
        this.nomFichier = nom_fichier;
        this.duree = duree;
        this.titre = titre;
        this.idArtiste = id_artiste;
        this.nomArtiste = nom_artiste;
        this.idAlbum = id_album;
        this.nomAlbum = nom_album;
        this.idDossier = id_dossier;
        this.nomDossier = nom_dossier;
        mode = DOUBLON;
    }

    public int getID() {
        return ID;
    }

    public int getIdArtiste() {
        return idArtiste;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public int getIdDossier() {
        return idDossier;
    }

    public int getIdFichier() {
        return idFichier;
    }

    public String getNomArtiste() {
        return nomArtiste;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public String getNomDossier() {
        return nomDossier;
    }

    public String getDossierFichier() {
        return nomDossier + File.separator + nomFichier;
    }

    public String getTitre() {
        return titre;
    }

    public int getDuree() {
        return duree;
    }

    public String toString() {
        if (mode == FRAPPE) return nomArtiste; else if (mode == DOUBLON) return nomArtiste + " - " + nomAlbum + " - " + titre + " (" + duree / 60 + "min)"; else return "";
    }
}
