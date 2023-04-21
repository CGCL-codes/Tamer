package CV;

/**
 * 
 *
 * @hibernate.class
 *     table="TITOLO_SCOLASTICO"
 *
 */
public class TitoloScolastico {

    private String nomeTitoloScolastico;

    /**
  *   @hibernate.property
  */
    public String getNomeTitoloScolastico() {
        return nomeTitoloScolastico;
    }

    public void setNomeTitoloScolastico(String nomeTitoloScolastico) {
        this.nomeTitoloScolastico = nomeTitoloScolastico;
    }

    private String livelloRiconoscibilta;

    /**
  *   @hibernate.property
  */
    public String getLivelloRiconoscibilta() {
        return livelloRiconoscibilta;
    }

    public void setLivelloRiconoscibilta(String livelloRiconoscibilta) {
        this.livelloRiconoscibilta = livelloRiconoscibilta;
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
