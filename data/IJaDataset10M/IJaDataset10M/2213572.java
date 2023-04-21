package net.sf.maple.resources;

import java.io.OutputStream;

public interface InOutLocation extends InLocation {

    public InOutLocation plus(Object... fragments);

    public OutputStream openOutput();
}
