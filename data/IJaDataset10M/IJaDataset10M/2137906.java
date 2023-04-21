package org.personalsmartspace.pm.PCM.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.platform.PreferenceDetails;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IAction;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

/**
 * @author Elizabeth
 *
 */
public class MonitoredInfo {

    private ArrayList<PreferenceDetails> list;

    private final PSSLog logging = new PSSLog(this);

    public MonitoredInfo() {
        this.list = new ArrayList<PreferenceDetails>();
    }

    public void addInfo(String serviceType, IServiceIdentifier serviceID, String prefName) {
        PreferenceDetails details = new PreferenceDetails(serviceType, serviceID, prefName);
        if (null == lookup(serviceType, serviceID, prefName)) {
            this.list.add(details);
        }
    }

    public PreferenceDetails lookup(String serviceType, IServiceIdentifier serviceID, String prefName) {
        PreferenceDetails details = new PreferenceDetails(serviceType, serviceID, prefName);
        Iterator<PreferenceDetails> i = this.list.iterator();
        while (i.hasNext()) {
            PreferenceDetails prefDetail = i.next();
            if (prefDetail.equals(details)) {
                return prefDetail;
            }
        }
        return null;
    }

    public List<PreferenceDetails> getList() {
        return this.list;
    }

    public void deleteInfo(String serviceType, IServiceIdentifier serviceID) {
        Iterator<PreferenceDetails> i = this.list.iterator();
        while (i.hasNext()) {
            PreferenceDetails pd = i.next();
            if (pd.equalsServiceOnlyDetails(serviceType, serviceID)) {
                this.list.remove(pd);
            }
        }
    }

    public String toString() {
        String s = "";
        for (PreferenceDetails d : this.list) {
            s = s.concat(d.toString());
            s = s.concat("\n");
        }
        return s;
    }
}
