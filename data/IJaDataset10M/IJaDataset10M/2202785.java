package org.in4ama.documentengine.output;

import java.io.InputStream;

/** Represents an open office document */
public class OdtOutputDocument extends OutputDocument {

    public static final String FILE_EXTENSION = "odt";

    public static final String TYPE = "odt";

    /** Creates a new instance of OdtOutputDocument */
    public OdtOutputDocument(String name, InputStream inputStream) {
        super(name, inputStream);
    }

    /** Gets the type of this document */
    @Override
    public String getType() {
        return TYPE;
    }

    /** Gets the extension of a file that this document should be stored to */
    @Override
    public String getFileExtension() {
        return FILE_EXTENSION;
    }
}
