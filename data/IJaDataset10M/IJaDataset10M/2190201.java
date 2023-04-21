package uk.ac.liv.jt.format.elements;

import java.io.IOException;
import uk.ac.liv.jt.format.ByteReader;

public class FloatingPointPropertyAtomElement extends BasePropertyAtomData {

    public float value;

    @Override
    public void read() throws IOException {
        super.read();
        int versionNumber = -1;
        if (getReader().MAJOR_VERSION >= 9) {
            reader.readBytes(2);
            versionNumber = reader.readI16();
            if (versionNumber != 1) {
                throw new IllegalArgumentException("Found invalid version number: " + versionNumber);
            }
        }
        value = reader.readF32();
        ovalue = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
