package org.atricore.idbus.capabilities.sso.support.core;

import oasis.names.tc.saml._2_0.protocol.StatusResponseType;
import org.atricore.idbus.kernel.main.mediation.IdentityMediationFault;

public class SSOResponseException extends IdentityMediationFault {

    private StatusResponseType response;

    private StatusCode topLevelStatusCode;

    private StatusCode secondLevelStatusCode;

    private StatusDetails statusDtails;

    public SSOResponseException(StatusResponseType response, StatusCode topLevelStatusCode, StatusCode secondLevelStatusCode, StatusDetails statusDtails) {
        super(topLevelStatusCode.getValue(), (secondLevelStatusCode != null ? secondLevelStatusCode.getValue() : ""), (statusDtails != null ? statusDtails.getValue() : ""), null, null);
        this.response = response;
        this.topLevelStatusCode = topLevelStatusCode;
        this.secondLevelStatusCode = secondLevelStatusCode;
        this.statusDtails = statusDtails;
    }

    public SSOResponseException(StatusResponseType response, StatusCode topLevelStatusCode, StatusCode secondLevelStatusCode, StatusDetails statusDtails, String errorDetails) {
        super(topLevelStatusCode.getValue(), (secondLevelStatusCode != null ? secondLevelStatusCode.getValue() : null), (statusDtails != null ? statusDtails.getValue() : null), errorDetails, null);
        this.response = response;
        this.topLevelStatusCode = topLevelStatusCode;
        this.secondLevelStatusCode = secondLevelStatusCode;
        this.statusDtails = statusDtails;
    }

    public SSOResponseException(StatusResponseType response, StatusCode topLevelStatusCode, StatusCode secondLevelStatusCode, StatusDetails statusDtails, Throwable cause) {
        super(topLevelStatusCode.getValue(), (secondLevelStatusCode != null ? secondLevelStatusCode.getValue() : null), (statusDtails != null ? statusDtails.getValue() : null), null, cause);
        this.response = response;
        this.statusDtails = statusDtails;
        this.topLevelStatusCode = topLevelStatusCode;
        this.secondLevelStatusCode = secondLevelStatusCode;
    }

    public SSOResponseException(StatusResponseType response, StatusCode topLevelStatusCode, StatusCode secondLevelStatusCode, StatusDetails statusDtails, String errorDetails, Throwable cause) {
        super(topLevelStatusCode.getValue(), (secondLevelStatusCode != null ? secondLevelStatusCode.getValue() : null), (statusDtails != null ? statusDtails.getValue() : null), errorDetails, cause);
        this.response = response;
        this.topLevelStatusCode = topLevelStatusCode;
        this.secondLevelStatusCode = secondLevelStatusCode;
        this.statusDtails = statusDtails;
    }

    public StatusResponseType getResponse() {
        return response;
    }

    public StatusCode getTopLevelStatusCode() {
        return topLevelStatusCode;
    }

    public StatusCode getSecondLevelStatusCode() {
        return secondLevelStatusCode;
    }

    public StatusDetails getSamlStatusDetails() {
        return statusDtails;
    }
}
