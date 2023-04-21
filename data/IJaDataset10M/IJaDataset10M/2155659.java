package org.arastreju.api.ontology.model.values;

import java.util.Date;

/**
 * Simple specification for a span in time.
 * 
 * Created: 11.08.2009
 *
 * @author Oliver Tigges 
 */
public class SimpleTimeSpec implements TimeSpec {

    private final TimeMask mask;

    private final Date time;

    /**
	 * Creates a new simple time spec.
	 * @param mask
	 * @param time
	 */
    public SimpleTimeSpec(final TimeMask mask, final Date time) {
        this.mask = mask;
        this.time = time;
    }

    public Date getBegin() {
        return time;
    }

    public Date getEnd() {
        return time;
    }

    public TimeMask getMask() {
        return mask;
    }
}
