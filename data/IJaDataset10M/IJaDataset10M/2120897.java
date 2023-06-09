package org.emftext.language.OCL.resource.OCL.mopp;

/**
 * A basic implementation of the ITokenResolveResult interface.
 */
public class OCLTokenResolveResult implements org.emftext.language.OCL.resource.OCL.IOCLTokenResolveResult {

    private String errorMessage;

    private Object resolvedToken;

    public OCLTokenResolveResult() {
        super();
        clear();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getResolvedToken() {
        return resolvedToken;
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void setResolvedToken(Object resolvedToken) {
        this.resolvedToken = resolvedToken;
    }

    public void clear() {
        errorMessage = "Can't resolve token.";
        resolvedToken = null;
    }
}
