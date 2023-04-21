package net.sf.saxon.evpull;

import javax.xml.transform.Source;

/**
 * A PullSource is a JAXP Source that encapsulates a PullProvider - that is, an object
 * that supplies an XML document as a sequence of events that are read under the control
 * of the recipient. Note that although PullSource implements the JAXP Source interface,
 * it is not necessarily acceptable to every JAXP implementation that accepts a Source
 * as input: Source is essentially a marker interface and users of Source objects need
 * to understand the individual implementation.
 */
public class PullEventSource implements Source {

    private String systemId;

    private EventIterator provider;

    /**
     * Create a PullSource based on a supplied EventIterator
     * @param provider the underlying EventIterator
     */
    public PullEventSource(EventIterator provider) {
        this.provider = provider;
    }

    /**
     * Get the EventIterator
     * @return the underlying EventIterator
     */
    public EventIterator getEventIterator() {
        return provider;
    }

    /**
     * Set the system identifier for this Source.
     * <p/>
     * <p>The system identifier is optional if the source does not
     * get its data from a URL, but it may still be useful to provide one.
     * The application can use a system identifier, for example, to resolve
     * relative URIs and to include in error messages and warnings.</p>
     *
     * @param systemId The system identifier as a URL string.
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * Get the system identifier that was set with setSystemId.
     *
     * @return The system identifier that was set with setSystemId, or null
     *         if setSystemId was not called.
     */
    public String getSystemId() {
        return systemId;
    }
}
