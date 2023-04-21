package uk.ac.cam.caret.minibix.qtibank.impl;

import java.io.IOException;
import java.io.OutputStream;
import uk.ac.cam.caret.minibix.archive.api.Metadatum;
import uk.ac.cam.caret.minibix.archive.api.MetadatumFactory;

public class StringMetadatum implements Metadatum, GetValue {

    private String value;

    public StringMetadatum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public MetadatumFactory getFactory() {
        return new StringMetadatumFactory();
    }

    public void serialize(OutputStream os) throws IOException {
        byte[] data = value.getBytes("UTF8");
        os.write(data);
    }

    @Override
    public String toString() {
        return value;
    }
}
