package edu.arizona.cs.mbel.signature;

/** This class describes a pointer type
  * @author Michael Stepp
  */
public class PointerTypeSignature extends TypeSpecSignature {

    private java.util.Vector customMods;

    private TypeSignature type;

    /** Constructor that specifies the CustomModifiers applied to this signature
     * @param mods an array of CustomModifiers to be applied to this signature (can be null)
     * @param sig the type of pointer this is
     */
    public PointerTypeSignature(CustomModifierSignature[] mods, TypeSignature sig) throws SignatureException {
        super(ELEMENT_TYPE_PTR);
        if (sig == null) throw new SignatureException("PointerTypeSignature: null type specified");
        type = sig;
        customMods = new java.util.Vector(10);
        if (mods != null) {
            for (int i = 0; i < mods.length; i++) if (mods[i] != null) customMods.add(mods[i]);
        }
    }

    /** Constructor for a VOID pointer, with CustomModifiers
     * @param mods the CustomModifiers for this pointer (can be null)
     */
    public PointerTypeSignature(CustomModifierSignature[] mods) throws SignatureException {
        super(ELEMENT_TYPE_PTR);
        type = null;
        customMods = new java.util.Vector(10);
        if (mods != null) {
            for (int i = 0; i < mods.length; i++) if (mods[i] != null) customMods.add(mods[i]);
        }
    }

    private PointerTypeSignature() {
        super(ELEMENT_TYPE_PTR);
    }

    /** Factory method for parsing a PointerTypeSignature from a binary blob
     * @param buffer the buffer to read from
     * @param group a TypeGroup to reconcile tokens to mbel references
     * @return a PointerTypeSignature representing the given binary blob, or null if there was a parse error
     */
    public static TypeSignature parse(edu.arizona.cs.mbel.ByteBuffer buffer, edu.arizona.cs.mbel.mbel.TypeGroup group) {
        PointerTypeSignature blob = new PointerTypeSignature();
        byte data = buffer.get();
        if (data != ELEMENT_TYPE_PTR) return null;
        blob.customMods = new java.util.Vector(10);
        int pos = buffer.getPosition();
        CustomModifierSignature temp = CustomModifierSignature.parse(buffer, group);
        while (temp != null) {
            blob.customMods.add(temp);
            pos = buffer.getPosition();
            temp = CustomModifierSignature.parse(buffer, group);
        }
        buffer.setPosition(pos);
        data = buffer.peek();
        if (data != ELEMENT_TYPE_VOID) {
            blob.type = TypeSignature.parse(buffer, group);
            if (blob.type == null) return null;
        } else {
            blob.type = null;
            buffer.get();
        }
        return blob;
    }

    /** Status method telling whether this is a VOID pointer or not 
     * (isVoid() implies getPointerType()==null)
     * @return true if this is a VOID pointer, false otherwise
     */
    public boolean isVoid() {
        return (type == null);
    }

    /** Getter method for the type of pointer this is (or null if VOID)
     */
    public TypeSignature getPointerType() {
        return type;
    }

    /** Getter method for the CustomModifiers applied to this signature.
     * This method will never return null, but it may return a 0-length array.
     */
    public CustomModifierSignature[] getCustomMods() {
        CustomModifierSignature[] sigs = new CustomModifierSignature[customMods.size()];
        for (int i = 0; i < sigs.length; i++) sigs[i] = (CustomModifierSignature) customMods.get(i);
        return sigs;
    }

    public void emit(edu.arizona.cs.mbel.ByteBuffer buffer, edu.arizona.cs.mbel.emit.ClassEmitter emitter) {
        buffer.put(ELEMENT_TYPE_PTR);
        for (int i = 0; i < customMods.size(); i++) ((CustomModifierSignature) customMods.get(i)).emit(buffer, emitter);
        if (type == null) buffer.put(ELEMENT_TYPE_VOID); else type.emit(buffer, emitter);
    }
}
