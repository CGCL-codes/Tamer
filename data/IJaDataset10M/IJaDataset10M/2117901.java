package net.sf.doolin.gui.spring;

import net.sf.doolin.gui.field.FieldPassword;

/**
 * Parser for {@link FieldPassword}
 * 
 * @author Damien Coraboeuf
 * 
 */
@SuppressWarnings("unchecked")
public class PasswordParser extends AbstractPropertyFieldTypeParser<FieldPassword> {

    /**
	 * Constructor
	 */
    public PasswordParser() {
        super(FieldPassword.class);
        addSimpleAttribute("columns");
        addSimpleAttribute("digest");
        addSimpleAttribute("store");
    }
}
