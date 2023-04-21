package uk.gov.dti.og.fox.security;

import uk.gov.dti.og.fox.FoxResponse;

public class StandardAuthenticationDescriptor implements AuthenticationDescriptor {

    private final String mSessionId;

    private final String mCode;

    private final String mMessage;

    private final FoxResponse mFoxResponse;

    private final String mSessionLoginId;

    public StandardAuthenticationDescriptor(String pCode, String pMessage, String pSessionId, FoxResponse pFoxResponse, String pSessionLoginId) {
        super();
        mSessionId = pSessionId;
        mCode = pCode;
        mMessage = pMessage;
        mFoxResponse = pFoxResponse;
        mSessionLoginId = pSessionLoginId;
    }

    public StandardAuthenticationDescriptor(String pCode, String pMessage, String pSessionId, FoxResponse pFoxResponse) {
        this(pCode, pMessage, pSessionId, pFoxResponse, null);
    }

    public StandardAuthenticationDescriptor(String pCode, String pMessage, String pSessionId) {
        this(pCode, pMessage, pSessionId, null);
    }

    public StandardAuthenticationDescriptor(String pCode, String pMessage) {
        this(pCode, pMessage, null, null);
    }

    public StandardAuthenticationDescriptor(FoxResponse pFoxResponse) {
        this(null, null, null, pFoxResponse);
    }

    public String getSessionId() {
        return mSessionId;
    }

    public String getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public FoxResponse getFoxResponse() {
        return mFoxResponse;
    }

    public String getSessionLoginId() {
        return mSessionLoginId;
    }
}
