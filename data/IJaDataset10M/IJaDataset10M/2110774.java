package edu.arizona.cs.mbel.mbel;

/** This class represents a CustomAttribute on any metadata table that may hold one.
  * The other MBEL classes that correspond to metadat tables will contain vectors of 
  * CustomAttributes inside them.
  * It has a refrence to the method that is the CustomAttribute constructor, and a raw
  * byte blob that is the values passed to the constructor in this instance.
  * The raw byte blob is currently unparsable. 
  * @author Michael Stepp
  */
public class CustomAttribute {

    private byte[] signature;

    private MethodDefOrRef constructor;

    /** Makes a new CustomAttribute witht he given parameter blob and constructor 
     * @param blob the parameter list blob for this instance
     * @param method the constructor method of this attribute class
     */
    protected CustomAttribute(byte[] blob, MethodDefOrRef method) {
        signature = blob;
        constructor = method;
    }

    /** Returns the raw parameter blob of this instance
     */
    public byte[] getSignature() {
        return signature;
    }

    /** Returns the method reference of the constructor of this Attribute class
     */
    public MethodDefOrRef getConstructor() {
        return constructor;
    }
}
