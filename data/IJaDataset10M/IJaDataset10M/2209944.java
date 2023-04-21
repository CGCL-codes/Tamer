package org.posterita.form;

import org.posterita.beans.ProductAttributeBean;
import org.posterita.struts.core.DefaultForm;

public class ProductAttributeForm extends DefaultForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ProductAttributeForm() {
        setBean(new ProductAttributeBean());
    }
}
