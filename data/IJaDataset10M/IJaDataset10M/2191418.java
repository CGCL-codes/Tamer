package org.arastreju.api.ontology.model.sn.views;

import org.arastreju.api.common.ElementaryDataType;
import org.arastreju.api.ontology.model.sn.SNValue;
import org.arastreju.api.ontology.model.sn.ValueView;

/**
 * Representation of a text value.
 * 
 * Created: 25.05.2009
 *
 * @author Oliver Tigges
 */
public class SNText extends ValueView {

    /**
	 * Creates a new text node.
	 * @param text
	 */
    public SNText(final String text) {
        super(ElementaryDataType.STRING);
        setValue(ElementaryDataType.STRING, text);
    }

    /**
	 * Creates a new text view for given value.
	 * @param value The value to be wrapped.
	 */
    public SNText(final SNValue value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValueAsString();
    }
}
