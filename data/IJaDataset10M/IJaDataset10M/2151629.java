package shellkk.qiq.jdm.featureextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.datamining.MiningFunction;
import shellkk.qiq.jdm.MiningFunctionEx;
import shellkk.qiq.jdm.base.ModelImpl;

public class FeatureExtractionModelImpl extends ModelImpl implements FeatureExtractionModel {

    @Override
    protected FeatureExtractionModelImpl create() {
        return new FeatureExtractionModelImpl();
    }

    public FeatureExtractionModelImpl getCopy() {
        FeatureExtractionModelImpl copy = (FeatureExtractionModelImpl) super.getCopy();
        return copy;
    }

    public Collection<Feature> getFeatures(int topNCoefficient) {
        FeatureExtractionModelDetail md = (FeatureExtractionModelDetail) modelDetail;
        ArrayList<Feature> all = new ArrayList();
        all.addAll(md.getFeatures());
        Comparator<Feature> c = new Comparator<Feature>() {

            public int compare(Feature o1, Feature o2) {
                return o1.getIdentifier() - o2.getIdentifier();
            }
        };
        Collections.sort(all, c);
        int size = Math.min(topNCoefficient, all.size());
        return all.subList(0, size);
    }

    public Collection<Feature> getFeatures() {
        FeatureExtractionModelDetail md = (FeatureExtractionModelDetail) modelDetail;
        return getFeatures(md.getFeatures().size());
    }

    public MiningFunction getMiningFunction() {
        return MiningFunctionEx.featureExtraction;
    }
}
