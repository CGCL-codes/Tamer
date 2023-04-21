package org.openconcerto.erp.element.objet;

public class Repartition {

    private int id, idAxe;

    private String nom;

    private boolean modif;

    private boolean suppresion;

    private boolean creation;

    private boolean deleted;

    public Repartition(final int id, final String nom, final int idAxe) {
        this.id = id;
        this.nom = nom;
        this.idAxe = idAxe;
        this.modif = false;
        this.suppresion = false;
        this.creation = false;
        this.deleted = false;
    }

    public Repartition(final int id, final String nom, final int idAxe, final boolean creation) {
        this.id = id;
        this.nom = nom;
        this.idAxe = idAxe;
        this.modif = false;
        this.suppresion = false;
        this.deleted = false;
        this.creation = creation;
    }

    @Override
    public String toString() {
        return "ID : " + this.id + ", NOM : " + this.nom + ", ID_AXE : " + this.idAxe;
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public int getIdAxe() {
        return this.idAxe;
    }

    public boolean getModif() {
        return this.modif;
    }

    public boolean getSuppression() {
        return this.suppresion;
    }

    public boolean getCreation() {
        return this.creation;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setId(final int id) {
        this.modif = true;
        this.id = id;
    }

    public void setNom(final String nom) {
        if (nom.trim().length() == 0) {
            this.nom = "Répartition";
        } else {
            this.nom = nom;
        }
        this.modif = true;
    }

    public void setIdAxe(final int idAxe) {
        this.modif = true;
        this.idAxe = idAxe;
    }

    public void setSuppression(final boolean b) {
        this.suppresion = b;
    }

    public void setCreation(final boolean b) {
        this.creation = b;
    }

    public void setModif(final boolean b) {
        this.modif = b;
    }

    public void setDeleted(final boolean b) {
        this.deleted = b;
    }
}
