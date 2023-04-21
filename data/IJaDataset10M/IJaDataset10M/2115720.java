package sisc.ser;

import java.io.*;

public class NestedObjectOutputStream extends ObjectOutputStream {

    private Serializer s;

    NestedObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    public Serializer getSerializerInstance() {
        return s;
    }

    public void setSerializerInstance(Serializer s) {
        this.s = s;
    }
}
