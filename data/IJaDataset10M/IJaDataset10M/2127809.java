package de.grogra.pf.io;

import java.io.InputStream;
import de.grogra.util.MimeType;
import de.grogra.util.ModifiableMap;

public class InputStreamSourceImpl extends FilterSourceBase implements InputStreamSource {

    private final InputStream in;

    private final String systemId;

    public InputStreamSourceImpl(InputStream in, String systemId, MimeType mimeType, de.grogra.pf.registry.Registry r, ModifiableMap metaData) {
        super(new IOFlavor(mimeType, IOFlavor.INPUT_STREAM, null), r, metaData);
        this.in = in;
        this.systemId = systemId;
    }

    public String getSystemId() {
        return systemId;
    }

    public InputStream getInputStream() {
        return in;
    }

    public long length() {
        return -1;
    }
}
