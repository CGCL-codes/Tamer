package uk.ac.liv.jt.format.elements;

import java.io.IOException;
import uk.ac.liv.jt.format.JTFile;

/**
 * This class represents nodes that can instantiate other nodes; 
 * this is used for reusing existing model data. 
 * The case is handled by the read method in the LSGSegment class 
 * @author fabio
 *
 */
public class InstanceNodeElement extends BaseNodeElement {

    public int instancedNodeObjectId;

    @Override
    public void read() throws IOException {
        super.read();
        short versionNumber = -1;
        if (reader.MAJOR_VERSION >= 9) {
            versionNumber = reader.readI16();
            if (versionNumber != 1) {
                throw new IllegalArgumentException("Found invalid version number: " + versionNumber);
            }
        }
        instancedNodeObjectId = reader.readI32();
    }

    @Override
    public String toString() {
        return super.toString() + " [" + instancedNodeObjectId + "]";
    }
}
