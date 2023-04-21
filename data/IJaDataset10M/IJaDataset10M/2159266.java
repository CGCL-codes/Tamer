package org.dasein.cloud.platform;

import java.io.Serializable;
import org.dasein.cloud.DataFormat;

public class Subscription implements Serializable {

    private static final long serialVersionUID = 2417835759973538821L;

    private DataFormat dataFormat;

    private String description;

    private String endpoint;

    private EndpointType endpointType;

    private String providerOwnerId;

    private String providerRegionId;

    private String providerSubscriptionId;

    private String providerTopicId;

    private String name;

    public Subscription() {
    }

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public String getProviderSubscriptionId() {
        return providerSubscriptionId;
    }

    public void setProviderSubscriptionId(String providerSubscriptionId) {
        this.providerSubscriptionId = providerSubscriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " [" + providerSubscriptionId + "]";
    }

    public void setProviderTopicId(String providerTopicId) {
        this.providerTopicId = providerTopicId;
    }

    public String getProviderTopicId() {
        return providerTopicId;
    }

    public void setProviderRegionId(String providerRegionId) {
        this.providerRegionId = providerRegionId;
    }

    public String getProviderRegionId() {
        return providerRegionId;
    }

    public void setProviderOwnerId(String providerOwnerId) {
        this.providerOwnerId = providerOwnerId;
    }

    public String getProviderOwnerId() {
        return providerOwnerId;
    }
}
