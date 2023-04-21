package org.identifylife.key.engine.core.model.descriptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.identifylife.key.engine.core.model.Model;
import org.identifylife.key.engine.core.oxm.descriptions.jaxb.FeatureAdapter;
import org.identifylife.key.engine.core.oxm.descriptions.jaxb.StateAdapter;
import org.identifylife.key.engine.core.oxm.descriptions.jaxb.TaxonAdapter;

/**
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Descriptlet extends Model {

    @XmlJavaTypeAdapter(TaxonAdapter.class)
    private String taxon;

    @XmlJavaTypeAdapter(FeatureAdapter.class)
    private String feature;

    @XmlJavaTypeAdapter(StateAdapter.class)
    private String state;

    private Value value = Value.UNCERTAIN;

    public Descriptlet() {
    }

    public Descriptlet(String uuid) {
        setUuid(uuid);
    }

    public Descriptlet(String uuid, String taxon, String character) {
        this(uuid);
        this.taxon = taxon;
        this.feature = character;
    }

    public Descriptlet(String uuid, String taxon, String character, String state) {
        this(uuid);
        this.taxon = taxon;
        this.feature = character;
        this.state = state;
    }

    public String getTaxon() {
        return taxon;
    }

    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String character) {
        this.feature = character;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Descriptlet) {
            Descriptlet other = (Descriptlet) object;
            if (getUuid() != null) {
                return getUuid().equals(other.getUuid());
            } else if (this == object) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("uuid", getUuid()).append("taxon", getTaxon()).append("feature", getFeature()).append("state", getState()).append("value", getValue()).toString();
    }
}
