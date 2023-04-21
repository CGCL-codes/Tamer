package net.orangemile.security;

/**
 * @author Orange Mile, Inc
 */
public class SecureJoinPoint {

    private Object target;

    private Object thisObject;

    private String kind;

    private Object[] args;

    private String signatureName;

    private int signatureModifier;

    private Class signatureDeclaringType;

    public Class getSignatureDeclaringType() {
        return signatureDeclaringType;
    }

    public void setSignatureDeclaringType(Class signatureDeclaringType) {
        this.signatureDeclaringType = signatureDeclaringType;
    }

    public int getSignatureModifier() {
        return signatureModifier;
    }

    public void setSignatureModifier(int signatureModifier) {
        this.signatureModifier = signatureModifier;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getThisObject() {
        return thisObject;
    }

    public void setThisObject(Object thisObject) {
        this.thisObject = thisObject;
    }
}
