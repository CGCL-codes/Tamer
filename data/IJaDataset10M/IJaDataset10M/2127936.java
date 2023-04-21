package org.comptahome.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class Chequier implements Serializable {

    /**
	 * version objet
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * id : Identificateur du chequier en base
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
	 * compte : compte banquaire de rattachement du chequier.
	 */
    @ManyToOne
    @JoinColumn(name = "idCompte")
    private Compte compte;

    /**
	 * cheques : liste des cheques composant le chequier
	 */
    @OneToMany(mappedBy = "chequier", fetch = FetchType.LAZY)
    private List<Cheque> cheques;

    /**
	 * nombre : nombre de cheques composant le chequier (25,50, etc)
	 */
    private int nombre;

    /**
	 * premier : numero du premier cheque du chequier.
	 */
    private long premier;

    public Chequier() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Cheque> getCheques() {
        return cheques;
    }

    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public long getPremier() {
        return premier;
    }

    public void setPremier(long premier) {
        this.premier = premier;
    }
}
