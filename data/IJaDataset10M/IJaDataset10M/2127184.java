package gate.creole.pennbio;

import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import java.net.URL;

@CreoleResource(name = "Penn BioTagger: Variation", icon = "bio.png", helpURL = "http://gate.ac.uk/userguide/sec:domain-creole:biomed:pennbio")
public class VariationTagger extends AbstractTagger {

    public VariationTagger() {
        type = "var";
    }

    @CreoleParameter(defaultValue = "resources/variationModel.crf.gz")
    public void setModelURL(URL modelURL) {
        this.modelURL = modelURL;
    }
}
