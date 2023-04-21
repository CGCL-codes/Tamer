package com.volantis.mcs.policies.impl;

import com.volantis.mcs.policies.BaseURLPolicy;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyVisitor;
import com.volantis.mcs.policies.variants.content.BaseLocation;

public class BaseURLPolicyImpl extends AbstractPolicy implements BaseURLPolicy {

    private final String baseURL;

    private final BaseLocation baseLocation;

    public BaseURLPolicyImpl(BaseURLPolicyBuilder builder) {
        super(builder);
        baseURL = builder.getBaseURL();
        baseLocation = builder.getBaseLocation();
    }

    public PolicyBuilder getPolicyBuilder() {
        return getBaseURLPolicyBuilder();
    }

    public BaseURLPolicyBuilder getBaseURLPolicyBuilder() {
        return new BaseURLPolicyBuilderImpl(this);
    }

    public void accept(PolicyVisitor visitor) {
        visitor.visit(this);
    }

    public String getBaseURL() {
        return baseURL;
    }

    public PolicyType getPolicyType() {
        return PolicyType.BASE_URL;
    }

    public BaseLocation getBaseLocation() {
        return baseLocation;
    }
}
