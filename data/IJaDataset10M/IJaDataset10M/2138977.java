package net.sf.opendf.hades.des.components;

/**
    This specifies the embedding of a subsignature of an interface into a signature, in
    particular this includes the direction (inverted or straight) and a name of the embedded
    signature relative to the surrounding one.
*/
public class InterfaceDescriptor {

    protected String name;

    protected SignatureDescriptor signature;

    protected boolean inverted;

    public String getName() {
        return name;
    }

    public SignatureDescriptor getSignature() {
        return signature;
    }

    public boolean isInverted() {
        return inverted;
    }

    public InterfaceDescriptor(String name, SignatureDescriptor signature, boolean inv) {
        this.name = name;
        this.signature = signature;
        inverted = inv;
    }
}
