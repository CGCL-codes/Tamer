package dto;

import java.io.Serializable;
import entity.Adresse;

public class AdresseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String rue;

    private String codePostal;

    private String ville;

    private String pays;

    public String toString() {
        return "[" + rue + ", " + codePostal + ", " + ville + ", " + pays + "]";
    }

    public String getLibelleCourt() {
        return ville + " (" + pays + ")";
    }

    public AdresseVO() {
        super();
    }

    public AdresseVO(String codePostal, String pays, String rue, String ville) {
        super();
        this.codePostal = codePostal;
        this.pays = pays;
        this.rue = rue;
        this.ville = ville;
    }

    public AdresseVO(Adresse a) {
        super();
        this.codePostal = a.getCodePostal();
        this.pays = a.getPays();
        this.rue = a.getRue();
        this.ville = a.getVille();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }
}
