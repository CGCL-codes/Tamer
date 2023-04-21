package org.ztemplates.validation;

import org.ztemplates.message.ZMessages;
import org.ztemplates.property.ZPropertyException;

public interface ZIValidator {

    public void validate(ZMessages messages) throws ZPropertyException;
}
