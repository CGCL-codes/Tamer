package com.persistent.appfabric.acs;

import com.persistent.appfabric.acs.Credentials.TOKEN_TYPE;
import com.persistent.appfabric.common.AppFabricException;

/**
 * Class used to call the appropriate TokenProvider to get ACS token
 * */
public class ACSTokenProvider {

    private TOKEN_TYPE type;

    private Credentials credentials;

    private String httpProxy = null;

    private int httpPort;

    /**
	 * Constructor to be used when proxy is not needed
	 * @param credentials Required to get the token type and credentails for retrieving the token
	 * */
    public ACSTokenProvider(Credentials credentials) {
        this.type = credentials.getCredentialtype();
        this.credentials = credentials;
    }

    /**
	 * Constructor to be used when proxy is not needed
	 * @param httpProxy Proxy server
	 * @param httpPort Proxy port
	 * @param credentials Required to get the token type and credentails for retrieving the token
	 * */
    public ACSTokenProvider(String httpProxy, int httpPort, Credentials credentials) {
        this.type = credentials.getCredentialtype();
        this.credentials = credentials;
        this.httpPort = httpPort;
        this.httpProxy = httpProxy;
    }

    /**
	 * Function to retrieve ACS token. The method calls different token providers depending on the token type
	 * @param requestUriStr Target string where the ACS request is to be sent 
	 * @param appliesTo Applies to address
	 * */
    public String getACSToken(String requestUriStr, String appliesTo) throws AppFabricException {
        String token = "";
        switch(type) {
            case SamlToken:
                SamlTokenProvider samlt;
                try {
                    if (httpProxy != null) {
                        samlt = new SamlTokenProvider(this.httpProxy, this.httpPort, this.credentials.getSamlToken());
                    } else {
                        samlt = new SamlTokenProvider(this.credentials.getSamlToken());
                    }
                    samlt.SetCustomInputClaims(this.credentials.getCustomCalimsStr());
                    token = samlt.GetToken(requestUriStr, appliesTo);
                    return token;
                } catch (AppFabricException e) {
                    throw e;
                } catch (Exception e) {
                    throw new AppFabricException(e.getMessage());
                }
            case SharedSecretToken:
                SharedSecretTokenProvider sst;
                String issuerName = credentials.getIssuerName();
                String issuerSecret = credentials.getIssuerSecret();
                try {
                    if (httpProxy != null) {
                        sst = new SharedSecretTokenProvider(this.httpProxy, this.httpPort, issuerName, issuerSecret);
                    } else sst = new SharedSecretTokenProvider(issuerName, issuerSecret);
                    sst.SetCustomInputClaims(this.credentials.getCustomCalimsStr());
                    token = sst.GetToken(requestUriStr, appliesTo);
                    return token;
                } catch (AppFabricException e) {
                    throw e;
                } catch (Exception e) {
                    throw new AppFabricException(e.getMessage());
                }
            case SimpleWebToken:
                SimpleWebTokenProvider swt;
                try {
                    if (this.httpProxy != null) {
                        swt = new SimpleWebTokenProvider(this.httpProxy, this.httpPort, this.credentials.getSimpleWebToken());
                    } else {
                        swt = new SimpleWebTokenProvider(this.credentials.getSimpleWebToken());
                    }
                    swt.SetCustomInputClaims(this.credentials.getCustomCalimsStr());
                    token = swt.GetToken(requestUriStr, appliesTo);
                    return token;
                } catch (AppFabricException e) {
                    throw e;
                } catch (Exception e) {
                    throw new AppFabricException(e.getMessage());
                }
            case SimpleApiAuthToken:
                SimpleApiAuthTokenProvider simpleApit;
                try {
                    if (this.httpProxy != null) {
                        simpleApit = new SimpleApiAuthTokenProvider(this.httpProxy, this.httpPort, this.credentials.getWrapName(), this.credentials.getWrapPassword());
                    } else {
                        simpleApit = new SimpleApiAuthTokenProvider(this.credentials.getWrapName(), this.credentials.getWrapPassword());
                    }
                    simpleApit.SetCustomInputClaims(this.credentials.getCustomCalimsStr());
                    token = simpleApit.GetToken(requestUriStr, appliesTo);
                    return token;
                } catch (AppFabricException e) {
                    throw e;
                } catch (Exception e) {
                    throw new AppFabricException(e.getMessage());
                }
        }
        throw new AppFabricException("ACSTokenProvider (getACSToken) Incorrect token type");
    }
}
