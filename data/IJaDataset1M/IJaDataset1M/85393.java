package org.posterita.form;

import org.posterita.beans.ProductKeywordsBean;
import org.posterita.struts.core.DefaultForm;

public class ProductKeywordsForm extends DefaultForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ProductKeywordsForm() {
        setBean(new ProductKeywordsBean());
    }
}
