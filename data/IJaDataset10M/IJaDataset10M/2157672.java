package org.mobicents.protocols.ss7.cap.api.EsiBcsm;

import java.util.ArrayList;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LocationInformation;

/**
*
tChangeOfPositionSpecificInfo [51] SEQUENCE {
locationInformation [50] LocationInformation OPTIONAL,
...,
metDPCriteriaList [51] MetDPCriteriaList {bound} OPTIONAL
},
dpSpecificInfoAlt [52] DpSpecificInfoAlt {bound}
}

* 
* @author sergey vetyutnev
* 
*/
public interface TChangeOfPositionSpecificInfo {

    public LocationInformation getLocationInformation();

    public ArrayList<MetDPCriterion> getMetDPCriteriaList();

    public DpSpecificInfoAlt getDpSpecificInfoAlt();
}
