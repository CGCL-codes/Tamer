package org.mobicents.slee.resource.diameter.cxdx.events;

import static net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes.*;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoAnswer;
import net.java.slee.resource.diameter.cxdx.events.avp.ServerCapabilities;
import net.java.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.ServerCapabilitiesImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvpImpl;

/**
 *
 * LocationInfoAnswerImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LocationInfoAnswerImpl extends DiameterMessageImpl implements LocationInfoAnswer {

    public LocationInfoAnswerImpl(Message message) {
        super(message);
    }

    @Override
    public String getLongName() {
        return "Location-Info-Answer";
    }

    @Override
    public String getShortName() {
        return "LIA";
    }

    public AuthSessionStateType getAuthSessionState() {
        return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
    }

    public ExperimentalResultAvp getExperimentalResult() {
        return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
    }

    public ServerCapabilities getServerCapabilities() {
        return (ServerCapabilities) getAvpAsCustom(SERVER_CAPABILITIES, CXDX_VENDOR_ID, ServerCapabilitiesImpl.class);
    }

    public String getServerName() {
        return getAvpAsUTF8String(SERVER_NAME, CXDX_VENDOR_ID);
    }

    public SupportedFeaturesAvp[] getSupportedFeatureses() {
        return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, CXDX_VENDOR_ID, SupportedFeaturesAvpImpl.class);
    }

    public String getWildcardedIMPU() {
        return getAvpAsUTF8String(WILDCARDED_IMPU, CXDX_VENDOR_ID);
    }

    public String getWildcardedPSI() {
        return getAvpAsUTF8String(WILDCARDED_PSI, CXDX_VENDOR_ID);
    }

    public boolean hasAuthSessionState() {
        return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
    }

    public boolean hasExperimentalResult() {
        return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
    }

    public boolean hasServerCapabilities() {
        return hasAvp(SERVER_CAPABILITIES, CXDX_VENDOR_ID);
    }

    public boolean hasServerName() {
        return hasAvp(SERVER_NAME, CXDX_VENDOR_ID);
    }

    public boolean hasWildcardedIMPU() {
        return hasAvp(WILDCARDED_IMPU, CXDX_VENDOR_ID);
    }

    public boolean hasWildcardedPSI() {
        return hasAvp(WILDCARDED_PSI, CXDX_VENDOR_ID);
    }

    public void setAuthSessionState(AuthSessionStateType authSessionState) {
        addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
    }

    public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
        addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
    }

    public void setServerCapabilities(ServerCapabilities serverCapabilities) {
        addAvp(SERVER_CAPABILITIES, CXDX_VENDOR_ID, serverCapabilities.byteArrayValue());
    }

    public void setServerName(String serverName) {
        addAvp(SERVER_NAME, CXDX_VENDOR_ID, serverName);
    }

    public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
        addAvp(SUPPORTED_FEATURES, CXDX_VENDOR_ID, supportedFeatures.byteArrayValue());
    }

    public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
        for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
            setSupportedFeatures(supportedFeatures);
        }
    }

    public void setWildcardedIMPU(String wildcardedIMPU) {
        addAvp(WILDCARDED_IMPU, CXDX_VENDOR_ID, wildcardedIMPU);
    }

    public void setWildcardedPSI(String wildcardedPSI) {
        addAvp(WILDCARDED_PSI, CXDX_VENDOR_ID, wildcardedPSI);
    }
}
