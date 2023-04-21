package org.identifylife.descriptlet.store.oxm.jaxb;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.identifylife.descriptlet.store.model.Feature;

/**
 * @author dbarnier
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FeatureMapEntry {

    @XmlAttribute
    private String key;

    @XmlElement
    private Set<Feature> features;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("key", getKey()).append("features", getFeatures()).toString();
    }
}
