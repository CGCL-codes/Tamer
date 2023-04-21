package org.geogrid.aist.credential.portlets;

import java.util.List;
import java.util.ArrayList;
import org.glite.security.voms.VOMSAttribute;

public class CredentialInfo {

    private String subject;

    private int timeleft;

    private List vomsACInfo;

    public CredentialInfo() {
        this.subject = null;
        this.timeleft = 0;
        this.vomsACInfo = new ArrayList();
    }

    public String getSubject() {
        return this.subject;
    }

    public int getTimeleft() {
        return this.timeleft;
    }

    public List getVomsACInfo() {
        return this.vomsACInfo;
    }

    public String getVo() {
        VOMSAttribute attr = (VOMSAttribute) this.vomsACInfo.get(0);
        return attr.getVO();
    }

    public List getFullyQualifiedAttributes() {
        VOMSAttribute attr = (VOMSAttribute) this.vomsACInfo.get(0);
        return attr.getFullyQualifiedAttributes();
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimeleft(int timeleft) {
        this.timeleft = timeleft;
    }

    public void setVomsACInfo(List acInfo) {
        if (acInfo == null) {
            this.vomsACInfo = new ArrayList();
        } else {
            this.vomsACInfo = acInfo;
        }
    }

    public boolean hasVomsExtension() {
        return !this.vomsACInfo.isEmpty();
    }

    public boolean isInvalid() {
        return ((this.subject == null) || (this.subject.length() == 0));
    }
}
