package org.posterita.form;

import org.posterita.beans.AttributeValueDetailBean;
import org.posterita.struts.core.DefaultForm;

public class AttributeValuesForm extends DefaultForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public AttributeValuesForm() {
        setBean(new AttributeValueDetailBean());
    }
}
