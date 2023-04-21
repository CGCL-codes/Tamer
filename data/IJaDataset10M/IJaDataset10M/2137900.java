package de.mpiwg.vspace.search.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.emf.ecore.EStructuralFeature;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;

public class WebImageSearchHelper extends AEObjectSearchHelper {

    public void fillFeatureMap() {
        featureMap = new HashMap<EStructuralFeature, List<EStructuralFeature>>();
    }

    public List<EStructuralFeature> getSearchableFeatures() {
        List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
        features.add(ExhibitionPackage.Literals.MEDIA__TITLE);
        features.add(ExhibitionPackage.Literals.MEDIA__DESCRIPTION);
        return features;
    }

    public List<EStructuralFeature> getSubelements() {
        List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
        return features;
    }
}
