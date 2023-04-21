package net.unto.twitter.methods;

import java.util.List;
import net.unto.twitter.JsonUtil;
import net.unto.twitter.TwitterProtos.Status;

public final class PublicTimelineRequest extends AbstractRequest {

    public static Builder builder() {
        return new Builder();
    }

    PublicTimelineRequest(Builder builder) {
        super(builder);
    }

    public static final class Builder extends AbstractRequest.Builder<Builder> {

        Builder() {
            path("/statuses/public_timeline.json");
            authorizationRequired(false);
        }

        public PublicTimelineRequest build() {
            return new PublicTimelineRequest(this);
        }

        public Builder sinceId(long sinceId) {
            return parameter("since_id", Long.toString(sinceId));
        }
    }

    public List<Status> get() {
        return JsonUtil.newStatusList(getJson());
    }
}
