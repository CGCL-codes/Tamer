package jsmex.function;

/**
 *
 * @author Tobias Senger
 */
public class DG14DataContainer {

    private byte[] rawData;

    /** Creates a new instance of DG14DataContainer */
    public DG14DataContainer(byte[] rawBytes) {
        this.rawData = rawBytes.clone();
    }

    public byte[] getBytes() {
        return rawData;
    }
}
