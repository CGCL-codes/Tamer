package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;

/**
*

CAP V4:
InitialDPArgExtension {PARAMETERS-BOUND : bound} ::= SEQUENCE { 
 gmscAddress       [0] ISDN-AddressString      OPTIONAL, 
 forwardingDestinationNumber   [1] CalledPartyNumber {bound}    OPTIONAL, 
 ms-Classmark2      [2] MS-Classmark2       OPTIONAL, 
 iMEI        [3] IMEI         OPTIONAL, 
 supportedCamelPhases    [4] SupportedCamelPhases     OPTIONAL, 
 offeredCamel4Functionalities   [5] OfferedCamel4Functionalities   OPTIONAL, 
 bearerCapability2     [6] BearerCapability {bound}    OPTIONAL, 
 ext-basicServiceCode2    [7] Ext-BasicServiceCode     OPTIONAL, 
 highLayerCompatibility2    [8] HighLayerCompatibility     OPTIONAL, 
 lowLayerCompatibility    [9] LowLayerCompatibility {bound}   OPTIONAL, 
 lowLayerCompatibility2    [10] LowLayerCompatibility {bound}   OPTIONAL, 
 ..., 
 enhancedDialledServicesAllowed  [11] NULL         OPTIONAL, 
 uu-Data        [12] UU-Data        OPTIONAL
}

CAP V2:
InitialDPArgExtension ::= SEQUENCE {
naCarrierInformation [0] NACarrierInformation OPTIONAL,
gmscAddress [1] ISDN-AddressString OPTIONAL,
...
}

-- If iPSSPCapabilities is not present then this denotes that a colocated gsmSRF is not  
-- supported by the gsmSSF. If present, then the gsmSSF supports a colocated gsmSRF capable  
-- of playing announcements via elementaryMessageIDs and variableMessages, the playing of 
-- tones and the collection of DTMF digits. Other supported capabilities are explicitly  
-- detailed in the IPSSPCapabilities parameter itself. 
-- Carrier is included at the discretion of the gsmSSF operator.  
 
* 
* @author sergey vetyutnev
* 
*/
public interface InitialDPArgExtension {

    public ISDNAddressString getGmscAddress();

    public CalledPartyNumberCap getForwardingDestinationNumber();

    public MSClassmark2 getMSClassmark2();

    public IMEI getIMEI();

    public SupportedCamelPhases getSupportedCamelPhases();

    public OfferedCamel4Functionalities getOfferedCamel4Functionalities();

    public BearerCapability getBearerCapability2();

    public ExtBasicServiceCode getExtBasicServiceCode2();

    public HighLayerCompatibilityInap getHighLayerCompatibility2();

    public LowLayerCompatibility getLowLayerCompatibility();

    public LowLayerCompatibility getLowLayerCompatibility2();

    public boolean getEnhancedDialledServicesAllowed();

    public UUData getUUData();
}
