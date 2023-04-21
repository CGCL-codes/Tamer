package org.mobicents.slee.resource.diameter.s6a.events;

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.*;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link AuthenticationInformationRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AuthenticationInformationRequestImpl extends DiameterMessageImpl implements AuthenticationInformationRequest {

    /**
   * @param message
   */
    public AuthenticationInformationRequestImpl(Message message) {
        super(message);
    }

    @Override
    public String getLongName() {
        return "Authentication-Information-Request";
    }

    @Override
    public String getShortName() {
        return "AIR";
    }

    public AuthSessionStateType getAuthSessionState() {
        return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
    }

    public SupportedFeaturesAvp[] getSupportedFeatureses() {
        return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
    }

    public boolean hasAuthSessionState() {
        return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
    }

    public void setAuthSessionState(AuthSessionStateType authSessionState) {
        addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
    }

    public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
        addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
    }

    public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
        for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
            setSupportedFeatures(supportedFeatures);
        }
    }

    public boolean hasRequestedEUTRANAuthenticationInfo() {
        return hasAvp(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID);
    }

    public RequestedEUTRANAuthenticationInfoAvp getRequestedEUTRANAuthenticationInfo() {
        return (RequestedEUTRANAuthenticationInfoAvp) getAvpAsCustom(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, RequestedEUTRANAuthenticationInfoAvpImpl.class);
    }

    public void setRequestedEUTRANAuthenticationInfo(RequestedEUTRANAuthenticationInfoAvp requestedEUTRANAuthenticationInfo) {
        addAvp(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, requestedEUTRANAuthenticationInfo.byteArrayValue());
    }

    public boolean hasRequestedUTRANGERANAuthenticationInfo() {
        return hasAvp(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID);
    }

    public RequestedUTRANGERANAuthenticationInfoAvp getRequestedUTRANGERANAuthenticationInfo() {
        return (RequestedUTRANGERANAuthenticationInfoAvp) getAvpAsCustom(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, RequestedUTRANGERANAuthenticationInfoAvpImpl.class);
    }

    public void setRequestedUTRANGERANAuthenticationInfo(RequestedUTRANGERANAuthenticationInfoAvp requestedUTRANGERANAuthenticationInfoAvp) {
        addAvp(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, requestedUTRANGERANAuthenticationInfoAvp.byteArrayValue());
    }

    public boolean hasVisitedPLMNId() {
        return hasAvp(VISITED_PLMN_ID, S6A_VENDOR_ID);
    }

    public byte[] getVisitedPLMNId() {
        return getAvpAsOctetString(VISITED_PLMN_ID, S6A_VENDOR_ID);
    }

    public void setVisitedPLMNId(byte[] visitedPLMNId) {
        addAvp(VISITED_PLMN_ID, S6A_VENDOR_ID, visitedPLMNId);
    }
}
