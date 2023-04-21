package com.xerox.amazonws.sns;

/**
 * An instance of this class represents a topic subscription.
 */
public class SubscriptionInfo {

    private String topicArn;

    private String protocol;

    private String subscriptionArn;

    private String owner;

    private String endpoint;

    public SubscriptionInfo(String topicArn, String protocol, String subscriptionArn, String owner, String endpoint) {
        this.topicArn = topicArn;
        this.protocol = protocol;
        this.subscriptionArn = subscriptionArn;
        this.owner = owner;
        this.endpoint = endpoint;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSubscriptionArn() {
        return subscriptionArn;
    }

    public String getOwner() {
        return owner;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String toString() {
        return "SubscriptionInfo[topicArn=" + this.topicArn + ", protocol=" + this.protocol + ", subscriptionArn=" + this.subscriptionArn + ", owner=" + this.owner + ", endpoint=" + this.endpoint + "]";
    }
}
