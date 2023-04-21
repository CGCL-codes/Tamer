package de.mogwai.common.web.component.input;

import de.mogwai.common.web.validator.MailValidator;

/**
 * Input field for email addresses.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:25 $
 */
public class EMailInputfieldComponent extends EMailInputfieldComponentBase {

    public EMailInputfieldComponent() {
        addValidator(new MailValidator());
    }
}
