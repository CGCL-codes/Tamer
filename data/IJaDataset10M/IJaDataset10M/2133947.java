package org.identifylife.key.editor.gwt.shared.context;

import java.util.ArrayList;
import java.util.List;
import org.identifylife.key.editor.gwt.shared.model.Feature;
import org.identifylife.key.editor.gwt.shared.model.TaxonSet;

/**
 * @author dbarnier
 *
 */
public class EditorContext {

    private String keyId;

    private List<Feature> features;

    private List<TaxonSet> taxa;

    public EditorContext() {
    }

    public EditorContext(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<TaxonSet> getTaxa() {
        return taxa;
    }

    public void setTaxa(List<TaxonSet> taxa) {
        this.taxa = taxa;
    }

    public void addFeature(Feature feature) {
        if (features == null) {
            features = new ArrayList<Feature>();
        }
        features.add(feature);
    }

    public void addTaxonSet(TaxonSet taxonSet) {
        if (taxa == null) {
            taxa = new ArrayList<TaxonSet>();
        }
        taxa.add(taxonSet);
    }
}
