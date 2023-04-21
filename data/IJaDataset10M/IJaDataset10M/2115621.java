package org.pixory.pxapplication.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidator;
import org.apache.tapestry.valid.ValidationDelegate;

public class ApplicationValidationDelegate extends ValidationDelegate {

    private static final Log LOG = LogFactory.getLog(ApplicationValidationDelegate.class);

    /**
	 * override to knock out the super implementation
	 */
    public void writeSuffix(IMarkupWriter writer, IRequestCycle cycle, IFormComponent component, IValidator validator) {
    }
}
