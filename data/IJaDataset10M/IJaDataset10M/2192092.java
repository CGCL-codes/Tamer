package org.plazmaforge.bsolution.document.client.swing.forms;

import org.plazmaforge.bsolution.base.SessionContext;
import org.plazmaforge.bsolution.base.SessionEnvironment;
import org.plazmaforge.bsolution.document.DocumentEnvironment;
import org.plazmaforge.bsolution.document.common.beans.Document;
import org.plazmaforge.framework.client.swing.forms.EXTEditForm;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.resources.Resources;

/**
 * 
 * <code>Abstract Document Edit Form</code> Use <code>Document</code>.
 * Specific initialize data
 * 
 * @author Oleh Hapon
 * 
 */
public abstract class AbstractDocumentEdit extends EXTEditForm {

    public AbstractDocumentEdit() {
        super();
    }

    public AbstractDocumentEdit(Resources resources) {
        super(resources);
    }

    /**
     * Get Document
     * 
     * @return
     */
    protected Document getDocument() {
        return (Document) this.getEntity();
    }

    /**
     * Initialize Data
     */
    protected void initData() throws ApplicationException {
        super.initData();
        initDocument();
    }

    /**
     * Initialize Document
     */
    protected void initDocument() {
        DocumentEnvironment.initDocument(getDocument(), getEntityCode(), getEnterpriseContext());
    }

    protected SessionContext getEnterpriseContext() {
        return SessionEnvironment.getContext();
    }
}
