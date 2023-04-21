package org.posterita.form;

import org.posterita.beans.PaymentHistoryBean;
import org.posterita.struts.core.DefaultForm;

public class PaymentHistoryForm extends DefaultForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public PaymentHistoryForm() {
        setBean(new PaymentHistoryBean());
    }
}
