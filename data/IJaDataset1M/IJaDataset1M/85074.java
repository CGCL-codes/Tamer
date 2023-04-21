package org.rockaa.search.amazon;

public class AmazonUS extends AbstractAmazonSearchEngine {

    @Override
    public String getDescriptionForGUI() {
        return "Amazon US";
    }

    @Override
    protected String getEndPoint() {
        return "ecs.amazonaws.com";
    }
}
