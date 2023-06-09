package reconcile.featureVector.individualFeature;

import java.util.Map;
import reconcile.data.Annotation;
import reconcile.data.Document;
import reconcile.featureVector.Feature;
import reconcile.featureVector.NominalFeature;
import reconcile.features.properties.GramRole;

public class Subject2 extends NominalFeature {

    public Subject2() {
        name = this.getClass().getSimpleName();
    }

    @Override
    public String[] getValues() {
        return YN;
    }

    @Override
    public String produceValue(Annotation np1, Annotation np2, Document doc, Map<Feature, String> featVector) {
        boolean sub = GramRole.getValue(np2, doc).equals("SUBJECT");
        return sub ? "Y" : "N";
    }
}
