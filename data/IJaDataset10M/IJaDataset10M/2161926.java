package org.arastreju.api.ontology.model.sn;

import java.util.Date;
import org.arastreju.api.common.ElementaryDataType;
import org.arastreju.api.ontology.model.values.SimpleTimeSpec;
import org.arastreju.api.ontology.model.values.TimeMask;
import org.arastreju.api.ontology.model.values.TimeSpec;

/**
 * Semantic node representing a time specification.
 * 
 * Created: 31.01.2008 
 *
 * @author Oliver Tigges
 */
public class SNTimeSpec extends ValueView {

    /**
	 * Creates a new transient time specification.
	 * @param time
	 * @param mask
	 */
    public SNTimeSpec(final Date time, final TimeMask mask) {
        super(ElementaryDataType.getCorresponding(mask));
        setValue(ElementaryDataType.getCorresponding(mask), time);
    }

    /**
	 * Creates a new time specification view for given value.
	 * @param value The value to be wrapped.
	 */
    public SNTimeSpec(final SNValue value) {
        super(value);
    }

    public TimeSpec getTime() {
        final TimeMask mask = TimeMask.getCorresponding(getValueAttachment().getType());
        return new SimpleTimeSpec(mask, getValueAttachment().getTimeValue());
    }

    public void setTime(final Date time, final TimeMask mask) {
        setValue(ElementaryDataType.getCorresponding(mask), time);
    }
}
