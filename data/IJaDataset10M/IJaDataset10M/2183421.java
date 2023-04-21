package org.mobicents.slee.resource.diameter.cxdx.events;

import static net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes.*;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.UserAuthorizationType;
import net.java.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvpImpl;

/**
 *
 * UserAuthorizationRequestImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserAuthorizationRequestImpl extends DiameterMessageImpl implements UserAuthorizationRequest {

    /**
   * @param message
   */
    public UserAuthorizationRequestImpl(Message message) {
        super(message);
    }

    @Override
    public String getLongName() {
        return "User-Authorization-Request";
    }

    @Override
    public String getShortName() {
        return "UAR";
    }

    public AuthSessionStateType getAuthSessionState() {
        return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
    }

    public String getPublicIdentity() {
        return getAvpAsUTF8String(PUBLIC_IDENTITY, CXDX_VENDOR_ID);
    }

    public SupportedFeaturesAvp[] getSupportedFeatureses() {
        return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, CXDX_VENDOR_ID, SupportedFeaturesAvpImpl.class);
    }

    public long getUARFlags() {
        return getAvpAsUnsigned32(UAR_FLAGS, CXDX_VENDOR_ID);
    }

    public UserAuthorizationType getUserAuthorizationType() {
        return (UserAuthorizationType) getAvpAsEnumerated(USER_AUTHORIZATION_TYPE, UserAuthorizationType.class);
    }

    public byte[] getVisitedNetworkIdentifier() {
        return getAvpAsOctetString(VISITED_NETWORK_IDENTIFIER, CXDX_VENDOR_ID);
    }

    public boolean hasAuthSessionState() {
        return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
    }

    public boolean hasPublicIdentity() {
        return hasAvp(PUBLIC_IDENTITY, CXDX_VENDOR_ID);
    }

    public boolean hasUARFlags() {
        return hasAvp(UAR_FLAGS, CXDX_VENDOR_ID);
    }

    public boolean hasUserAuthorizationType() {
        return hasAvp(USER_AUTHORIZATION_TYPE, CXDX_VENDOR_ID);
    }

    public boolean hasVisitedNetworkIdentifier() {
        return hasAvp(VISITED_NETWORK_IDENTIFIER, CXDX_VENDOR_ID);
    }

    public void setAuthSessionState(AuthSessionStateType authSessionState) {
        addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
    }

    public void setPublicIdentity(String publicIdentity) {
        addAvp(PUBLIC_IDENTITY, CXDX_VENDOR_ID, publicIdentity);
    }

    public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
        addAvp(SUPPORTED_FEATURES, CXDX_VENDOR_ID, supportedFeatures.byteArrayValue());
    }

    public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
        for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
            setSupportedFeatures(supportedFeatures);
        }
    }

    public void setUARFlags(long uarFlags) {
        addAvp(UAR_FLAGS, CXDX_VENDOR_ID, uarFlags);
    }

    public void setUserAuthorizationType(UserAuthorizationType userAuthorizationType) {
        addAvp(USER_AUTHORIZATION_TYPE, CXDX_VENDOR_ID, userAuthorizationType.getValue());
    }

    public void setVisitedNetworkIdentifier(byte[] visitedNetworkIdentifier) {
        addAvp(VISITED_NETWORK_IDENTIFIER, CXDX_VENDOR_ID, visitedNetworkIdentifier);
    }
}
