package de.mpicbg.buchholz.phenofam.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.user.client.rpc.IsSerializable;

public class InputDataEntity implements Serializable, IsSerializable {

    private String xref;

    private Double value;

    private XrefAnnotations xrefAnnotations;

    protected InputDataEntity() {
    }

    public InputDataEntity(String xref, Double value, Set<String> sequenceIds, String geneDescription, List<ProteinFeature> features) {
        this(xref, value, new XrefAnnotations(geneDescription, sequenceIds, features));
    }

    public InputDataEntity(String xref, Double value, XrefAnnotations xrefAnnotations) {
        this.xref = xref;
        this.value = value;
        this.xrefAnnotations = xrefAnnotations;
    }

    public XrefAnnotations getXref() {
        return xrefAnnotations;
    }

    public void setXref(XrefAnnotations xref) {
        this.xrefAnnotations = xref;
    }

    public String getId() {
        return xref;
    }

    public void setId(String xref) {
        this.xref = xref;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Set<String> getUniprotIds() {
        return xrefAnnotations.getUniprotIds();
    }

    public String getGeneDescription() {
        return xrefAnnotations.getGeneDescription();
    }

    public List<? extends ProteinFeature> getFeatures() {
        return xrefAnnotations.getFeatures();
    }

    public List<? extends ProteinFeature> getFeatures(FeatureDatabase featureDb) {
        ArrayList<ProteinFeature> arr = new ArrayList<ProteinFeature>(xrefAnnotations.getFeatures().size());
        for (ProteinFeature f : xrefAnnotations.getFeatures()) {
            if (f.getFeatureDatabase().equals(featureDb)) arr.add(f);
        }
        arr.trimToSize();
        return arr;
    }

    public List<String> getFeatureInterPros() {
        List<? extends ProteinFeature> features = getFeatures();
        Set<String> featureLabels = new HashSet<String>(features.size());
        for (ProteinFeature feature : features) {
            List<String> ips = feature.getInterpros();
            featureLabels.addAll(ips);
        }
        return new ArrayList<String>(featureLabels);
    }
}
