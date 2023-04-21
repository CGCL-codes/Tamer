package net.sf.doolin.gui.spring;

import net.sf.doolin.gui.field.FieldLong;

/**
 * Parser for {@link FieldLong}
 * 
 * @author Damien Coraboeuf
 * 
 */
@SuppressWarnings("unchecked")
public class LongParser extends AbstractPropertyFieldTypeParser<FieldLong> {

    /**
	 * Constructor
	 */
    public LongParser() {
        super(FieldLong.class);
        addSimpleAttribute("minimum");
        addSimpleAttribute("maximum");
        addSimpleAttribute("minimumProperty");
        addSimpleAttribute("maximumProperty");
        addSimpleAttribute("spinner");
        addSimpleAttribute("step");
    }
}
