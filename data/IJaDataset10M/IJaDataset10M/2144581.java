package com.vangent.hieos.services.xca.gateway.transactions;

import com.vangent.hieos.xutil.xconfig.XConfigActor;

/**
 *
 * @author Bernie Thuman
 */
public class XCAGatewayConfig {

    private XConfigActor config;

    private String patientId;

    /**
     * 
     * @param config
     */
    public XCAGatewayConfig(XConfigActor config) {
        this.config = config;
    }

    /**
     * 
     * @return
     */
    public XConfigActor getConfig() {
        return this.config;
    }

    /**
     *
     * @return
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * 
     * @return
     */
    public String getHomeCommunityId() {
        return config.getUniqueId();
    }
}
