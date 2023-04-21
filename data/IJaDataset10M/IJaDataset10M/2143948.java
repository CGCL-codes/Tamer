package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Adresse")
public class Adresse {

    private long id;

    private String rue;

    private String codePostal;

    private String ville;

    private String pays;

    public String toString() {
        return "[" + rue + ", " + codePostal + ", " + ville + ", " + pays + "]";
    }

    public Adresse() {
        super();
    }

    public Adresse(String codePostal, String pays, String rue, String ville) {
        super();
        this.codePostal = codePostal;
        this.pays = pays;
        this.rue = rue;
        this.ville = ville;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
