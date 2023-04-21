package com.apc.websiteschema.res.fms;

import java.util.Map;
import websiteschema.common.wrapper.BeanWrapper;

/**
 *
 * @author ray
 */
public class FmsSource {

    String id;

    String name;

    String bigKind;

    String smallKind;

    String core;

    String url;

    String regionId;

    String sourceExpertise;

    String sourceOriginality;

    String sourceInfluence;

    String sourceKindName;

    public String getSourceKindName() {
        return sourceKindName;
    }

    public void setSourceKindName(String sourceKindName) {
        this.sourceKindName = sourceKindName;
    }

    public String getBigKind() {
        return bigKind;
    }

    public void setBigKind(String bigKind) {
        this.bigKind = bigKind;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSmallKind() {
        return smallKind;
    }

    public void setSmallKind(String smallKind) {
        this.smallKind = smallKind;
    }

    public String getSourceExpertise() {
        return sourceExpertise;
    }

    public void setSourceExpertise(String sourceExpertise) {
        this.sourceExpertise = sourceExpertise;
    }

    public String getSourceInfluence() {
        return sourceInfluence;
    }

    public void setSourceInfluence(String sourceInfluence) {
        this.sourceInfluence = sourceInfluence;
    }

    public String getSourceOriginality() {
        return sourceOriginality;
    }

    public void setSourceOriginality(String sourceOriginality) {
        this.sourceOriginality = sourceOriginality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static FmsSource apply(Map<String, String> map) {
        FmsSource obj = BeanWrapper.getBean(map, FmsSource.class, false);
        return obj;
    }
}
