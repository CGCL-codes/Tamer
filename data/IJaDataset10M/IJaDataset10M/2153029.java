package org.zkoss.zk.au.out;

import org.zkoss.zk.au.AuResponse;

/**
 * A response to ask the client to submit the form with the specified ID,
 * if any.
 *
 * <p>data[0]: the form's UUID.
 * 
 * @author tomyeh
 * @since 3.0.0
 */
public class AuSubmitForm extends AuResponse {

    /**
	 * @param formId the form's ID to submit, which is UUID if a component
	 * is used.
	 */
    public AuSubmitForm(String formId) {
        super("submit", formId);
    }
}
