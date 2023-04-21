package org.personalsmartspace.onm.api;

import java.io.Serializable;
import java.util.ArrayList;
import org.personalsmartspace.pss_sm_common.impl.PssService;

/**
 * @author David McKitterick
 *
 */
public class PSSAdvertisement implements Serializable {

    private static final long serialVersionUID = 6395444892328657454L;

    private String pssName;

    private String peerGroupID;

    private String peerGroupDesc;

    private String dpi;

    private ArrayList<PssService> services = new ArrayList<PssService>();

    /**
     * @return the peerGroupID
     */
    public String getPeerGroupID() {
        return peerGroupID;
    }

    /**
     * @param peerGroupID the peerGroupID to set
     */
    public void setPeerGroupID(String peerGroupID) {
        this.peerGroupID = peerGroupID;
    }

    /**
     * @return the peerGroupDesc
     */
    public String getPeerGroupDesc() {
        return peerGroupDesc;
    }

    /**
     * @param peerGroupDesc the peerGroupDesc to set
     */
    public void setPeerGroupDesc(String peerGroupDesc) {
        this.peerGroupDesc = peerGroupDesc;
    }

    /**
     * @return the services
     */
    public ArrayList<PssService> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(ArrayList<PssService> services) {
        this.services = services;
    }

    /**
     * @return the pssName
     */
    public String getPssName() {
        return pssName;
    }

    /**
     * @param pssName the pssName to set
     */
    public void setPssName(String pssName) {
        this.pssName = pssName;
    }

    /**
     * @return the dpi
     */
    public String getDpi() {
        return dpi;
    }

    /**
     * @param dpi the dpi to set
     */
    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
}
