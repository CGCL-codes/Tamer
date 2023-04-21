package ru.itbrains.jicard.wstrust;

public class RequestSecurityTokenResponse {

    private String context;

    private TokenType tokenType;

    private RequestedSecurityToken rst;

    private RequestedAttachedReference rar;

    private RequestedUnattachedReference rur;

    public RequestSecurityTokenResponse() {
    }

    public RequestSecurityTokenResponse(String context) {
        this.context = context;
    }

    public RequestedAttachedReference getRar() {
        return rar;
    }

    public void setRar(RequestedAttachedReference rar) {
        this.rar = rar;
    }

    public RequestedUnattachedReference getRur() {
        return rur;
    }

    public void setRur(RequestedUnattachedReference rur) {
        this.rur = rur;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public RequestedSecurityToken getRst() {
        return rst;
    }

    public void setRst(RequestedSecurityToken rst) {
        this.rst = rst;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String toString() {
        return "RequestSecurityTokenResponse{" + "context='" + context + '\'' + ", tokenType=" + tokenType + ", rst=" + rst + '}';
    }
}
