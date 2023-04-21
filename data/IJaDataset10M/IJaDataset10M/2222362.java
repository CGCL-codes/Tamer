package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.jdyna.value.MultiValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Rappresenta una singola proprieta' dell'oggetto.
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({ @NamedQuery(name = "Property.findPropertyByWidget", query = "from Property where typo.rendering = ?"), @NamedQuery(name = "Property.countValueByPropertiesDefinition", query = "select count(*) from Property where typo.id = ?") })
public abstract class Property<TP extends PropertiesDefinition> extends IdentifiableObject implements Comparable<Property<TP>> {

    @Transient
    private Log log = LogFactory.getLog(Property.class);

    /** chiave d'accesso primario */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPERTY_SEQ")
    @SequenceGenerator(name = "PROPERTY_SEQ", sequenceName = "PROPERTY_SEQ")
    private Integer id;

    /** Posizione */
    private int position;

    @OneToOne
    @JoinColumn(unique = true)
    @Cascade(value = { CascadeType.ALL })
    private AValue value;

    /**
	 * the visibility flags of the field
	 */
    private Integer visibility;

    /**
     * Getter method.
     *
     * 
     * @return the visibility flags of the field
     */
    public Integer getVisibility() {
        if (this.visibility == null) {
            return 0;
        }
        return visibility;
    }

    /**
     * Setter method. 
     * 
     * @param visibility
     *            the visibility flags of the field
     */
    public void setVisibility(Integer visibility) {
        this.visibility = visibility == null ? new Integer(0) : visibility;
    }

    /**
	 * Restituisce la posizione della proprieta' all'interno della lista di proprieta' della medesima tipologia.
	 * La prima proprieta' ha posizione 0.
	 * 
	 * @see AnagraficaSupport#getProprietaDellaTipologia(PropertiesDefinition)
	 * @return la posizione della proprieta'
	 */
    public int getPosition() {
        return position;
    }

    /**
	 * Imposta la posizione della proprieta' all'interno della lista di proprieta' della medesima tipologia<br>
	 * <b>Attenzione</b> l'utilizzo di questo metodo potrebbe introdurre disallineamenti nella lista sopra citata.
	 * Utilizzare solamente per implementare i metodi dell'interfaccia <code>AnagraficaSupport</code>
	 * 
	 * @see AnagraficaSupport, AnagraficaSupport#getProprietaDellaTipologia(PropertiesDefinition)
	 * @param posizione, la nuova posizione della proprieta'
	 */
    public void setPosition(int posizione) {
        this.position = posizione;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * Restituisce la proprieta' contenente la proprieta' corrente o <code>null</code> se la proprieta' e' di primo livello
	 * 
	 * @return l'eventuale proprieta' nel cui valore (MultiValue) e' contenuta la proprieta' corrente
	 * @see MultiValue
	 */
    @Transient
    public abstract Property<TP> getPropertyParent();

    /**
	 * Setter per la proprieta' contenitore.<br>
	 * <b>Attenzione</b> la proprieta' contenitore deve essere della medesima classe della proprieta' corrente, altrimenti
	 * deve essere sollevata una <code>ClassCastException</code>
	 * 
	 * @param proprieta' nel cui valore (MultiValue) e' contenuta la proprieta' corrente, <code>null</code> se la proprieta' e' di primo livello
	 */
    public abstract void setParentProperty(Property<TP> property);

    /**
	 * Restituisce la tipologia di proprieta' associata.
	 * 
	 * @see PropertiesDefinition 
	 * @return la tipologia di proprieta' associata.
	 */
    @Transient
    public abstract TP getTypo();

    public abstract void setTypo(TP propertyDefinition);

    /**
	 * Definisce il link all'indietro verso l'oggetto proprietario dell'anagrafica 
	 * @param parent l'oggetto proprietario dell'anagrafica in cui compare la proprieta'
	 */
    public abstract void setParent(AnagraficaSupport<? extends Property<TP>, TP> parent);

    /**
	 * Restituisce l'oggetto proprietario dell'anagrafica
	 * @return l'oggetto proprietario dell'anagrafica
     */
    @Transient
    public abstract AnagraficaSupport<? extends Property<TP>, TP> getParent();

    /**
	 * Permette di restituire per qualsiasi valore della proprieta il generico
	 * oggetto associato; ad esempio per una Stringa il valore della stringa,
	 * per un puntatore ad un contatto, l'oggetto contatto con id passato nella
	 * variabile valore.
	 */
    @Transient
    public Object getObject() {
        return getValue().getObject();
    }

    /** 
	 *  Restituisce la descrizione esatta del valore associato alla tipologia
	 * 	che viene renderizzato tramite widget; e' il widget che sa come visualizzare
	 *  sulla view il valore della proprieta'. 
	 **/
    @Override
    public String toString() {
        if (getValue() != null && getValue().getObject() != null) {
            return getTypo().getRendering().toString(getValue().getObject());
        }
        log.warn("to string di Proprieta' con valore null");
        return super.toString();
    }

    public String toHTML() {
        return getTypo().getRendering().toHTML(getValue().getObject());
    }

    public String getHtml() {
        return toHTML();
    }

    /**
	 * Confronta due proprieta' prima in base alla tipologia e poi, nel caso queste coincidano, in base
	 * alle rispettive posizioni. Se anche la posizione coincide vuol dire che si stanno confrontando proprieta'
	 * di oggetti differenti, a questo punto si guarda l'attributo sortValue del valore.
	 * 
	 * @see PropertiesDefinition#compareTo
	 * @see #getPosition()
	 * @param o la proprieta' da comparare
	 * @return come disporre in modo ordinato le due proprieta'
	 */
    public int compareTo(Property<TP> o) {
        if (this.getTypo().compareTo(o.getTypo()) == 0) {
            if (this.getPosition() > o.getPosition()) return 1;
            if (this.getPosition() == o.getPosition()) {
                if ((value == null || value.getSortValue() == null) && (o.value == null || o.value.getSortValue() == null)) return 0;
                if (value == null || value.getSortValue() == null) return 1;
                if (o.value == null || o.value.getSortValue() == null) return -1;
                return value.getSortValue().compareTo(o.getValue().getSortValue());
            }
            if (this.getPosition() < o.getPosition()) return -1;
        }
        return getTypo().compareTo(o.getTypo());
    }

    @Override
    public boolean equals(Object object) {
        Property<TP> propConfronto;
        if (object != null && object instanceof Property) {
            try {
                propConfronto = (Property<TP>) object;
            } catch (ClassCastException e) {
                log.warn("Attenzione, l'oggetto che hai passato al metodo equals non e' una proprieta", e);
                return false;
            }
        } else return false;
        if ((getTypo().getShortName().equals(propConfronto.getTypo().getShortName())) && (position == propConfronto.getPosition())) {
            if (value != null && value.equals(propConfronto.value)) return true; else if (value == null && propConfronto.value == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getTypo() != null ? getTypo().hashCode() : 0) + (getValue() != null && getValue().getObject() != null ? getValue().getObject().hashCode() : 0);
    }

    public AValue getValue() {
        return value;
    }

    public void setValue(AValue valore) {
        this.value = valore;
    }

    /**
	 * Return true if property has no value or "empty" value.
	 * @see AWidget#isNull(Object)
	 * @return
	 */
    public boolean hasNullValue() {
        if (value == null) {
            return true;
        } else {
            return getTypo().getRendering().isNull(value.getObject());
        }
    }
}
