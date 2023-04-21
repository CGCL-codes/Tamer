package sisi.articoli;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the correlati database table.
 * 
 */
@Entity
@Table(name = "correlati")
public class Correlati implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CORRELATI_ID_GENERATOR", sequenceName = "CORRELATISEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CORRELATI_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 15)
    private String codcorrelato;

    @Column(length = 15)
    private String codprincipale;

    public Correlati() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodcorrelato() {
        return this.codcorrelato;
    }

    public void setCodcorrelato(String codcorrelato) {
        this.codcorrelato = codcorrelato;
    }

    public String getCodprincipale() {
        return this.codprincipale;
    }

    public void setCodprincipale(String codprincipale) {
        this.codprincipale = codprincipale;
    }
}
