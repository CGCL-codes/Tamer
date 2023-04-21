package at.voctrainee.bo;

/**
 * Die Klasse <code>WortBO</code> stellt ein Wort dar. 
 *
 * @version $Revision: 105 $
 * @author  $Author: xian666 $
 * @since   0.1.0
 */
public final class WortBO {

    /** Id des Wortes. */
    private Long id;

    /** Das Wort selbst. */
    private String wort;

    /**
     * 
     */
    protected WortBO() {
    }

    /**
     * 
     * @param wort
     */
    public WortBO(String wort) {
        setWort(wort);
    }

    /**
     * 
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     */
    public String getWort() {
        return wort;
    }

    /**
     * 
     * @param wort
     */
    public void setWort(String wort) {
        this.wort = wort;
    }

    /**
     * �berpr�ft ob zwei <code>WortBO</code> Objekte gleich sind. Zum
     * Vergleich wird der Constraint WOERTER_UK herangezogen.
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof WortBO)) {
            return false;
        }
        final String anotherWort = ((WortBO) object).wort;
        return wort == null ? anotherWort == null : wort.equals(anotherWort);
    }

    /**
     * Berechnet einen eindeutigen Hashcode f�r ein <code>SpracheBO</code>
     * Objekt.
     */
    @Override
    public int hashCode() {
        return wort != null ? wort.hashCode() : 17;
    }
}
