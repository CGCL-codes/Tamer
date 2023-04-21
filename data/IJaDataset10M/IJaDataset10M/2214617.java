package org.posterita.form;

import org.posterita.beans.ItemBean;
import org.posterita.struts.core.DefaultForm;

public class ItemForm extends DefaultForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ItemForm() {
        setBean(new ItemBean());
    }
}
