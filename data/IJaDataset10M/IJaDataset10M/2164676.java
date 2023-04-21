package social.hub.api.google.buzz.util;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import social.hub.api.google.buzz.BuzzActivity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuzzAtivityRequest {

    private BuzzActivity object;

    public void setObject(BuzzActivity object) {
        this.object = object;
    }

    public BuzzActivity getObject() {
        return object;
    }
}
