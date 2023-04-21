package gov.nasa.jpf.jvm.bytecode;

/**
 * Load int from array
 * ..., arrayref, index => ..., value
 */
public class IALOAD extends ArrayLoadInstruction {

    public int getByteCode() {
        return 0x2E;
    }
}
