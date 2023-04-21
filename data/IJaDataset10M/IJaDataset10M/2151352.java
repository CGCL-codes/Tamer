package org.identifylife.key.engine.core.oxm.features;

import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.identifylife.key.engine.core.model.features.Feature;
import org.identifylife.key.engine.core.model.features.MultistateFeature;
import org.identifylife.key.engine.core.model.features.NumericFeature;
import org.identifylife.key.engine.core.model.features.TextFeature;
import org.identifylife.key.engine.utils.CollectionUtils;

/**
 * 
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Features {

    @XmlElements({ @XmlElement(name = "multistateFeature", type = MultistateFeature.class), @XmlElement(name = "numericFeature", type = NumericFeature.class), @XmlElement(name = "textFeature", type = TextFeature.class) })
    private List<Feature> features;

    public Features() {
    }

    public Features(Collection<Feature> features) {
        if (features != null && features.size() > 0) {
            this.features = CollectionUtils.newArrayList();
            this.features.addAll(features);
        }
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("features", getFeatures()).toString();
    }
}
