package net.sf.doolin.gui.core.action;

import net.sf.doolin.gui.core.View;

/**
 * Default closing action for the views.
 * 
 * @author Damien Coraboeuf
 * @version $Id: ActionClose.java,v 1.4 2007/08/15 09:05:30 guinnessman Exp $
 */
public class ActionClose extends AbstractAction {

    private boolean validate;

    /**
	 * This method checks if the view can be closed and then closes the view.
	 * 
	 * @see #canClose()
	 * @see #close()
	 * @see net.sf.doolin.gui.core.action.AbstractAction#process()
	 */
    @Override
    protected void process() {
        if (canClose()) {
            close();
        }
    }

    /**
	 * Actually closes the view
	 * 
	 * @see View#close()
	 */
    protected void close() {
        getView().close();
    }

    /**
	 * Tests if the view can be closed.
	 * 
	 * If the <code>validate</code> property is set to <code>true</code>, a
	 * validation is first performed.
	 * 
	 * If the validation is ok or if no validation is required, the view can be
	 * closed.
	 * 
	 * @return <code>true</code> if the view can be closed, <code>false</code>
	 *         otherwise.
	 */
    protected boolean canClose() {
        boolean validationOk = true;
        if (validate) {
            validationOk = validateView();
        }
        if (validationOk) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Gets if this action must validate the view before closing it.
	 * 
	 * @return <code>true</code> if the view must be validated before being
	 *         closed, <code>false</code> otherwise.
	 */
    public boolean isValidate() {
        return validate;
    }

    /**
	 * Sets if this action must validate the view before closing it.
	 * 
	 * @param validate
	 *            <code>true</code> if the view must be validated before being
	 *            closed, <code>false</code> otherwise.
	 */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
