package ca.gc.drdc_rddc.atlantic.dmso.cmd;

import ca.gc.drdc_rddc.atlantic.hla.HLAModel;
import hla.rti13.java1.FederateAmbassador;

public class timeConstrainedEnabledCmd extends BaseQueuedCommand {

    public timeConstrainedEnabledCmd() {
    }

    byte[] fedTime;

    String federationTime;

    public String getFederationTime() {
        return federationTime;
    }

    public void setFederationTime(String federationTime) {
        this.federationTime = federationTime;
    }

    public timeConstrainedEnabledCmd(byte[] fedTime) {
        this.fedTime = fedTime;
    }

    public void doMe(FederateAmbassador fedAmb) throws Exception {
        fedAmb.timeConstrainedEnabled(fedTime);
    }

    public void resolveHandles(HLAModel model, HandleNameMapper rti) throws Exception {
        fedTime = encodeTime(model.getTimeManager().getTimeFactory(), federationTime);
    }

    public void resolveNames(HLAModel model, HandleNameMapper rti) throws Exception {
        federationTime = decodeTime(model.getTimeManager().getTimeFactory(), fedTime);
    }
}
