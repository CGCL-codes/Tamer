package writer2latex.office;

import writer2latex.util.SimpleZipReader;

/**
 * This class represents an embedded object with a binary representation in an ODF package document
 */
public class EmbeddedBinaryObject extends EmbeddedObject {

    /** The object's binary representation. */
    private byte[] objData = null;

    /**
     * Package private constructor for use when reading an object from a 
     * package ODF file
     *
     * @param   name    The name of the object.
     * @param   type    The MIME-type of the object.
     * @param   source  A <code>SimpleZipReader</code> containing the object
     */
    protected EmbeddedBinaryObject(String sName, String sType, SimpleZipReader source) {
        super(sName, sType);
        objData = source.getEntry(sName);
    }

    /** Get the binary data for this object
     *
     * @return  A <code>byte</code> array containing the object's data.
     */
    public byte[] getBinaryData() {
        return objData;
    }
}
