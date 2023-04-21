package sisi.users;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the diritti database table.
 * 
 */
@Entity
@Table(name = "diritti")
public class Diritti implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, length = 6)
    private String id;

    @Column(length = 40)
    private String descrizione;

    @Column(length = 40)
    private String gruppo;

    @Column(length = 40)
    private String sottogruppo;

    public Diritti() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getGruppo() {
        return this.gruppo;
    }

    public void setGruppo(String gruppo) {
        this.gruppo = gruppo;
    }

    public String getSottogruppo() {
        return this.sottogruppo;
    }

    public void setSottogruppo(String sottogruppo) {
        this.sottogruppo = sottogruppo;
    }
}
