package org.personalsmartspace.onm.stub;

import java.util.ArrayList;
import java.util.HashMap;
import org.personalsmartspace.onm.api.platform.IAdvertisementMgr;
import org.personalsmartspace.onm.api.platform.MessageEndpoint;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.onm.api.pss3p.PssServiceAdvertisement;

/**
 * @author David McKitterick
 *
 */
public class StubAdvertisementMgr implements IAdvertisementMgr {

    @Override
    public HashMap<String, PSSAdvertisement> getCachedPSSAdvertisements() throws ONMException {
        HashMap<String, PSSAdvertisement> map = new HashMap<String, PSSAdvertisement>();
        PSSAdvertisement pssAd = new PSSAdvertisement();
        pssAd.setPssName("test PSS");
        pssAd.setDpi("DPI?");
        pssAd.setPeerGroupDesc("This is a PSS");
        pssAd.setPeerGroupID("testPGID");
        map.put(pssAd.getPssName(), pssAd);
        return map;
    }

    @Override
    public void advertisePublicServices(ArrayList<PssServiceAdvertisement> services) throws ONMException {
    }

    @Override
    public void advertiseSharedServices(ArrayList<String> targetPSSIDs, ArrayList<PssServiceAdvertisement> services) throws ONMException {
    }

    @Override
    public MessageEndpoint getPSSMessageEndpoint(String PSSID) throws ONMException {
        return new MessageEndpoint("</endpoint>");
    }
}
