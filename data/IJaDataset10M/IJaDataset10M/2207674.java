package org.feeddreamwork;

public class FeedResultRepository extends RepositoryBase {

    public FeedResult getFeedResult(String feedId) {
        Utils.throwIfNullOrEmpty(feedId);
        return (FeedResult) this.tryGetObjectById(FeedResult.class, feedId);
    }
}
