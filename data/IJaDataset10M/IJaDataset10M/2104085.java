package genomancer.ivy.das.client.modelimpl;

import genomancer.ivy.das.model.Das1FeaturesResponseI;
import genomancer.ivy.das.model.Das1FeatureI;
import java.util.List;

public class Das1FeaturesResponse extends Das1Response implements Das1FeaturesResponseI {

    List<Das1FeatureI> features;

    public Das1FeaturesResponse(String request_url, String version, List<Das1FeatureI> features) {
        super(request_url, version);
        this.features = features;
    }

    public List<Das1FeatureI> getFeatures() {
        return features;
    }
}
