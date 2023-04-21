package com.brekeke.report.engine.entity;

import java.io.Serializable;

/**
 * @author madawei
 * @version 1.0
 */
public class CTIServerAcd implements Serializable {

    private static final long serialVersionUID = -8185277058010596968L;

    private CTIServerAcdId id;

    private String inboundRuleId;

    private Integer agents;

    private Integer acdLevel;

    private String description;

    private String inboundParam1;

    private String inboundParam2;

    private String inboundParam3;

    private String inboundParam4;

    private String inboundParam5;

    private String inboundParam6;

    private String inboundParam7;

    private String inboundParam8;

    private String inboundParam9;

    private String inboundParam10;

    private String acdName;

    private String url;

    private String queueFunction;

    private String paging;

    private String urlAble;

    private String parameters;

    public CTIServerAcd() {
    }

    public CTIServerAcd(String acdId, Integer tenantId) {
        this.id = new CTIServerAcdId(acdId, tenantId);
    }

    public CTIServerAcdId getId() {
        return id;
    }

    public void setId(CTIServerAcdId id) {
        this.id = id;
    }

    public String getInboundRuleId() {
        return inboundRuleId;
    }

    public void setInboundRuleId(String inboundRuleId) {
        this.inboundRuleId = inboundRuleId;
    }

    public Integer getAgents() {
        return agents;
    }

    public void setAgents(Integer agents) {
        this.agents = agents;
    }

    public Integer getAcdLevel() {
        return acdLevel;
    }

    public void setAcdLevel(Integer acdLevel) {
        this.acdLevel = acdLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInboundParam1() {
        return inboundParam1;
    }

    public void setInboundParam1(String inboundParam1) {
        this.inboundParam1 = inboundParam1;
    }

    public String getInboundParam2() {
        return inboundParam2;
    }

    public void setInboundParam2(String inboundParam2) {
        this.inboundParam2 = inboundParam2;
    }

    public String getInboundParam3() {
        return inboundParam3;
    }

    public void setInboundParam3(String inboundParam3) {
        this.inboundParam3 = inboundParam3;
    }

    public String getInboundParam4() {
        return inboundParam4;
    }

    public void setInboundParam4(String inboundParam4) {
        this.inboundParam4 = inboundParam4;
    }

    public String getInboundParam5() {
        return inboundParam5;
    }

    public void setInboundParam5(String inboundParam5) {
        this.inboundParam5 = inboundParam5;
    }

    public String getInboundParam6() {
        return inboundParam6;
    }

    public void setInboundParam6(String inboundParam6) {
        this.inboundParam6 = inboundParam6;
    }

    public String getInboundParam7() {
        return inboundParam7;
    }

    public void setInboundParam7(String inboundParam7) {
        this.inboundParam7 = inboundParam7;
    }

    public String getInboundParam8() {
        return inboundParam8;
    }

    public void setInboundParam8(String inboundParam8) {
        this.inboundParam8 = inboundParam8;
    }

    public String getInboundParam9() {
        return inboundParam9;
    }

    public void setInboundParam9(String inboundParam9) {
        this.inboundParam9 = inboundParam9;
    }

    public String getInboundParam10() {
        return inboundParam10;
    }

    public void setInboundParam10(String inboundParam10) {
        this.inboundParam10 = inboundParam10;
    }

    public String getAcdName() {
        return acdName;
    }

    public void setAcdName(String acdName) {
        this.acdName = acdName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQueueFunction() {
        return queueFunction;
    }

    public void setQueueFunction(String queueFunction) {
        this.queueFunction = queueFunction;
    }

    public String getPaging() {
        return paging;
    }

    public void setPaging(String paging) {
        this.paging = paging;
    }

    public String getUrlAble() {
        return urlAble;
    }

    public void setUrlAble(String urlAble) {
        this.urlAble = urlAble;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
