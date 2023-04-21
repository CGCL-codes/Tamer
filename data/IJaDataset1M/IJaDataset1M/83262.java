package org.hemera;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Hemera - Intelligent System
 * Web Service processing controller.
 * 
 * @author Bertrand Benoit <projettwk@users.sourceforge.net>
 * @since 0.2
 */
public class ProcessingAction extends ActionSupport {

    /****************************************************************************************/
    private static final long serialVersionUID = 1761869323713553713L;

    /****************************************************************************************/
    private ProcessingModel processingModel;

    /**
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public final String execute() throws Exception {
        processingModel = new ProcessingModel();
        return SUCCESS;
    }

    /**
     * @return the processing model.
     */
    public final ProcessingModel getProcessingModel() {
        return processingModel;
    }
}
