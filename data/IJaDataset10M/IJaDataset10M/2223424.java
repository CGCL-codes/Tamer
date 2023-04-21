package net.sf.bacchus.container;

import net.sf.bacchus.Control;
import net.sf.bacchus.Header;

/**
 * Support class for building classes that maintain a {@link Header} and
 * {@link Control} record.
 */
public class AbstractControlled {

    /** the header record. */
    private Header header;

    /** the control record. */
    private Control control;

    /** Create a controlled object. */
    public AbstractControlled() {
        this(null);
    }

    /**
     * Create a controlled object.
     * @param header the header record.
     */
    public AbstractControlled(final Header header) {
        this(header, null);
    }

    /**
     * Create a controlled object.
     * @param header the header record.
     * @param control the control record.
     */
    public AbstractControlled(final Header header, final Control control) {
        this.header = header;
        this.control = control;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Header getHeader() {
        return this.header;
    }

    /**
     * Sets the header record.
     * @param header the header record.
     */
    public void setHeader(final Header header) {
        this.header = header;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Control getControl() {
        return this.control;
    }

    /**
     * Sets the control record.
     * @param control the control record.
     */
    public void setControl(final Control control) {
        this.control = control;
    }
}
