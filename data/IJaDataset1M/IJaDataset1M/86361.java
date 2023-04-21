package CV;

/**
 * 
 *
 * @hibernate.class
 *     table="OCCUPAZIONE"
 *
 */
public class Occupazione {

    private String nomeOccupazione;

    /**
  *   @hibernate.property
  */
    public String getNomeOccupazione() {
        return nomeOccupazione;
    }

    public void setNomeOccupazione(String nomeOccupazione) {
        this.nomeOccupazione = nomeOccupazione;
    }

    private long id;

    /**
  *   @hibernate.id
  *     generator-class="increment"
  */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private CV.SettoreLavorativo settoreLavorativo;

    /**
    * @hibernate.many-to-one
    *     column="SETTORELAVORATIVO_FK"
    *     class="CV.SettoreLavorativo"
    *     not-null="true"
    *     outer-join="auto"
    */
    public CV.SettoreLavorativo getSettoreLavorativo() {
        return this.settoreLavorativo;
    }

    public void setSettoreLavorativo(CV.SettoreLavorativo settoreLavorativo) {
        this.settoreLavorativo = settoreLavorativo;
    }
}
