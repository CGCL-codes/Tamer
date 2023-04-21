package CV;

/**
 * 
 *
 * @hibernate.class
 *     table="IMPIEGO"
 *
 */
public class Impiego {

    private String nomeImpiego;

    /**
  *   @hibernate.property
  */
    public String getNomeImpiego() {
        return nomeImpiego;
    }

    public void setNomeImpiego(String nomeImpiego) {
        this.nomeImpiego = nomeImpiego;
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
}
