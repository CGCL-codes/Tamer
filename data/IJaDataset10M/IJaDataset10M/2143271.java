package it.webscience.uima.ocStorageAbstract;

import java.io.Serializable;

/** Proprietà associata all'evento.
 * Oggetto serializzato ed inviato allo storage agent
 * @author dellanna
 */
public class OcStoragePropertyRequest implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = -3706220232150181820L;

    /** nome della proprietà. */
    protected String name;

    /** valore della proprietà. */
    protected String value;

    /** tipo della proprietà. */
    protected String type;

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param inName the name to set
     */
    public final void setName(final String inName) {
        this.name = inName;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param inValue the value to set
     */
    public final void setValue(final String inValue) {
        this.value = inValue;
    }

    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * @param inType the type to set
     */
    public final void setType(final String inType) {
        this.type = inType;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
