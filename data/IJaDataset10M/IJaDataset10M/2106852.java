package de.kapsi.net.daap;

/**
 * 
 * @author  Roger Kapsi
 */
public abstract class DaapNoContentResponse implements DaapResponse {

    protected final DaapRequest request;

    protected final byte[] header;

    /** Creates a new instance of DaapNoContentResponse */
    public DaapNoContentResponse(DaapRequest request) {
        this.request = request;
        header = DaapHeaderConstructor.createNoContentHeader(request);
    }

    public String toString() {
        return (new String(header));
    }
}
