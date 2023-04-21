package net.java.slee.resource.diameter.s6a.events;

import java.util.Date;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.IMSVoiceOverPSSessionsSupported;
import net.java.slee.resource.diameter.s6a.events.avp.RATType;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

/**
 * Defines an interface representing the Insert-Subscriber-Data-Request message.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.2.10 Insert-Subscriber-Data-Answer (IDA) Command
 * The Insert-Subscriber-Data-Answer (IDA) command, indicated by the Command-Code field set to 319
 * and the 'R' bit cleared in the Command Flags field, is sent from MME or SGSN to HSS.
 * 
 * Message Format
 * < Insert-Subscriber-Data-Answer> ::=    < Diameter Header: 319, PXY, 16777251 >
 *                                         < Session-Id >
 *                                         [ Vendor-Specific-Application-Id ]
 *                                        *[ Supported-Features ]
 *                                         [ Result-Code ]
 *                                         [ Experimental-Result ] 
 *                                         { Auth-Session-State }
 *                                         { Origin-Host }
 *                                         { Origin-Realm }
 *                                         [ IMS-Voice-Over-PS-Sessions-Supported ]
 *                                         [ Last-UE-Activity-Time ]
 *                                         [ RAT-Type ]
 *                                         [ IDA-Flags ] 
 *                                         [ EPS-User-State ]
 *                                         [ EPS-Location-Information ]
 *                                        *[ AVP ]
 *                                        *[ Failed-AVP ]
 *                                        *[ Proxy-Info ]
 *                                        *[ Route-Record ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface InsertSubscriberDataAnswer extends DiameterMessage {

    public static final int COMMAND_CODE = 319;

    /**
   * Returns true if the Vendor-Specific-Application-Id AVP is present in the message.
   * 
   * @return true if the Vendor-Specific-Application-Id AVP is present in the message, false otherwise
   */
    public boolean hasVendorSpecificApplicationId();

    /**
   * Returns the value of the Vendor-Specific-Application-Id AVP, of type Grouped.
   * 
   * @return the value of the Vendor-Specific-Application-Id AVP or null if it has not been set on this message
   */
    public VendorSpecificApplicationIdAvp getVendorSpecificApplicationId();

    /**
   * Sets the value of the Vendor-Specific-Application-Id AVP, of type Grouped.
   * 
   * @param vendorSpecificApplicationId the new value for the Vendor-Specific-Application-Id AVP
   */
    public void setVendorSpecificApplicationId(VendorSpecificApplicationIdAvp vendorSpecificApplicationId);

    /**
   * Set a single instance value of the Supported-Features AVP, of type Grouped.
   * 
   * @param supportedFeatures
   */
    public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures);

    /**
   * Set multiple instance value of the Supported-Features AVP, of type Grouped.
   * 
   * @param supportedFeatureses
   */
    public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses);

    /**
   * Returns the value of the Supported-Features AVP, of type Grouped.
   * 
   * @return
   */
    public SupportedFeaturesAvp[] getSupportedFeatureses();

    /**
   * Returns true if the Result-Code AVP is present in the message.
   * 
   * @return
   */
    boolean hasResultCode();

    /**
   * Returns the value of the Result-Code AVP, of type Unsigned32.
   * Use {@link #hasResultCode()} to check the existence of this AVP.
   *   
   * @return the value of the Result-Code AVP
   */
    long getResultCode();

    /**
   * Sets the value of the Result-Code AVP, of type Unsigned32.
   * 
   * @param resultCode
   */
    void setResultCode(long resultCode);

    /**
   * Returns true if the Experimental-Result AVP is present in the message.
   * 
   * @return
   */
    public boolean hasExperimentalResult();

    /**
   * Returns the value of the Experimental-Result AVP, of type Grouped.
   * Use {@link #hasExperimentalResult()} to check the existence of this AVP.
   *   
   * @return the value of the Experimental-Result AVP
   */
    public ExperimentalResultAvp getExperimentalResult();

    /**
   * Sets the value of the Experimental-Result AVP, of type Grouped.
   * 
   * @param experimentalResult
   */
    public void setExperimentalResult(ExperimentalResultAvp experimentalResult);

    /**
   * Returns true if the Auth-Session-State AVP is present in the message.
   * 
   * @return true if the Auth-Session-State AVP is present in the message, false otherwise
   */
    public boolean hasAuthSessionState();

    /**
   * Returns the value of the Auth-Session-State AVP, of type Enumerated.
   * 
   * @return the value of the Auth-Session-State AVP, of type Enumerated
   */
    public AuthSessionStateType getAuthSessionState();

    /**
   * Sets the value of the Auth-Session-State AVP, of type Enumerated.
   * 
   * @param authSessionState
   */
    public void setAuthSessionState(AuthSessionStateType authSessionState);

    /**
   * Returns true if the IMS-Voice-Over-PS-Sessions-Supported AVP is present in the message.
   * 
   * @return true if the IMS-Voice-Over-PS-Sessions-Supported AVP is present in the message, false otherwise
   */
    public boolean hasIMSVoiceOverPSSessionsSupported();

    /**
   * Returns the value of the IMS-Voice-Over-PS-Sessions-Supported AVP, of type Enumerated.
   * 
   * @return the value of the IMS-Voice-Over-PS-Sessions-Supported AVP, of type Enumerated
   */
    public IMSVoiceOverPSSessionsSupported getIMSVoiceOverPSSessionsSupported();

    /**
   * Sets the value of the IMS-Voice-Over-PS-Sessions-Supported AVP, of type Enumerated.
   * 
   * @param imsVoiceOverPSSessionsSupported
   */
    public void setIMSVoiceOverPSSessionsSupported(IMSVoiceOverPSSessionsSupported imsVoiceOverPSSessionsSupported);

    /**
   * Returns true if the Last-UE-Activity-Time AVP is present in the message.
   * 
   * @return true if the Last-UE-Activity-Time AVP is present in the message, false otherwise
   */
    public boolean hasLastUEActivityTime();

    /**
   * Returns the value of the Last-UE-Activity-Time AVP, of type Time.
   * 
   * @return the value of the Last-UE-Activity-Time AVP or null if it has not been
   *         set on this message
   */
    public Date getLastUEActivityTime();

    /**
   * Sets the value of the Last-UE-Activity-Time AVP, of type Time.
   * 
   * @param lastUEActivityTime
   */
    public void setLastUEActivityTime(Date lastUEActivityTime);

    /**
   * Returns true if the RAT-Type AVP is present in the message.
   * 
   * @return true if the RAT-Type AVP is present in the message, false otherwise
   */
    public boolean hasRATType();

    /**
   * Returns the value of the RAT-Type AVP, of type Grouped.
   * 
   * @return the value of the RAT-Type AVP or null if it has not been set on this message
   */
    public RATType getRATType();

    /**
   * Sets the value of the RAT-Type AVP, of type Grouped.
   * 
   * @param ratType
   */
    public void setRATType(RATType ratType);

    /**
   * Returns true if the IDA-Flags AVP is present in the message.
   * 
   * @return
   */
    public boolean hasIDAFlags();

    /**
   * Returns the value of the IDA-Flags AVP, of type Unsigned32.
   * 
   * @return
   */
    public long getIDAFlags();

    /**
   * Sets the value of the IDA-Flags AVP, of type Unsigned32.
   * 
   * @param idaFlags
   */
    public void setIDAFlags(long idaFlags);

    /**
   * Returns true if the EPS-User-State AVP is present in the message.
   * 
   * @return
   */
    public boolean hasEPSUserState();

    /**
   * Returns the value of the EPS-User-State AVP, of type Grouped.
   * 
   * @return
   */
    public EPSUserStateAvp getEPSUserState();

    /**
   * Sets the value of the EPS-User-State AVP, of type Grouped.
   * 
   * @param epsUserState
   */
    public void setEPSUserState(EPSUserStateAvp epsUserState);

    /**
   * Returns true if the EPS-Location-Information AVP is present in the message.
   * 
   * @return
   */
    public boolean hasEPSLocationInformation();

    /**
   * Returns the value of the EPS-Location-Information AVP, of type Grouped.
   * 
   * @return
   */
    public EPSLocationInformationAvp getEPSLocationInformation();

    /**
   * Sets the value of the EPS-Location-Information AVP, of type Grouped.
   * 
   * @param epsLocationInformation
   */
    public void setEPSLocationInformation(EPSLocationInformationAvp epsLocationInformation);

    /**
   * Returns the set of Failed-AVP AVPs. The returned array contains the AVPs in the order they
   * appear in the message.
   * A return value of null implies that no Failed-AVP AVPs have been set.
   * The elements in the given array are FailedAvp objects.
   */
    public FailedAvp[] getFailedAvps();

    /**
   * Sets a single Failed-AVP AVP in the message, of type Grouped.
   * 
   * @param failedAvp
   */
    public void setFailedAvp(FailedAvp failedAvp);

    /**
   * Sets the set of Failed-AVP AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getFailedAvps()
   * is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   * 
   * @param failedAvps
   */
    public void setFailedAvps(FailedAvp[] failedAvps);

    /**
   * Returns the set of Proxy-Info AVPs. The returned array contains the AVPs in the order they
   * appear in the message.
   * A return value of null implies that no Proxy-Info AVPs have been set.
   * The elements in the given array are ProxyInfo objects.
   * 
   * @return
   */
    public ProxyInfoAvp[] getProxyInfos();

    /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * 
   * @param proxyInfo
   */
    public void setProxyInfo(ProxyInfoAvp proxyInfo);

    /**
   * Sets the set of Proxy-Info AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getProxyInfos() is
   * not guaranteed to return the same array instance, e.g. an "==" check would fail.
   *
   * @param proxyInfos
   */
    public void setProxyInfos(ProxyInfoAvp[] proxyInfos);

    /**
   * Returns the set of Route-Record AVPs. The returned array contains the AVPs in the order they appear in the message.
   * A return value of null implies that no Route-Record AVPs have been set.
   * The elements in the given array are DiameterIdentity objects.
   * 
   * @return
   */
    public DiameterIdentity[] getRouteRecords();

    /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   * 
   * @param routeRecord
   */
    public void setRouteRecord(DiameterIdentity routeRecord);

    /**
   * Sets the set of Route-Record AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getRouteRecords() is
   * not guaranteed to return the same array instance, e.g. an "==" check would fail.
   *
   * @param routeRecords
   */
    public void setRouteRecords(DiameterIdentity[] routeRecords);
}
