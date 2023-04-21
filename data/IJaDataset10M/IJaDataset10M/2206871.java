package jk.spider.api.event.engine;

import jk.spider.api.event.EventVisitor;

public class SpideringStartedEvent extends EngineRelatedEvent {

    protected String baseURL;

    public SpideringStartedEvent(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getComment() {
        return "Spidering started at '" + baseURL + "'";
    }

    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    public String getBaseURL() {
        return baseURL;
    }
}
