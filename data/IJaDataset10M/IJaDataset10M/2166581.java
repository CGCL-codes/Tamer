package org.openconcerto.erp.element.objet;

public class Association {

    private int id;

    private final transient int idCompte;

    private int idRep;

    private boolean creation;

    private boolean modification;

    private boolean suppression;

    public Association(final int id, final int id_compte, final int id_rep) {
        this.id = id;
        this.idCompte = id_compte;
        this.idRep = id_rep;
        this.creation = false;
        this.modification = false;
        this.suppression = false;
    }

    public Association(final int id, final int id_compte, final int id_rep, final boolean cree) {
        this.id = id;
        this.idCompte = id_compte;
        this.idRep = id_rep;
        this.creation = cree;
        this.modification = false;
        this.suppression = false;
    }

    public int getId() {
        return this.id;
    }

    public int getIdCompte() {
        return this.idCompte;
    }

    public int getIdRep() {
        return this.idRep;
    }

    public boolean getModification() {
        return this.modification;
    }

    public boolean getCreation() {
        return this.creation;
    }

    public boolean getSuppression() {
        return this.suppression;
    }

    public void setId(final int id) {
        this.modification = true;
        this.id = id;
    }

    public void setIdRep(final int id) {
        this.modification = true;
        this.idRep = id;
    }

    public void setModification(final boolean b) {
        this.modification = b;
    }

    public void setCreation(final boolean b) {
        this.creation = b;
    }

    public void setSuppression(final boolean b) {
        this.suppression = b;
    }

    @Override
    public String toString() {
        return "Association --> " + this.id + " Compte : " + this.idCompte + " Repartition : " + this.idRep;
    }
}
