package org.xaware.tracer.actions;

import org.eclipse.swt.graphics.Image;

/**
 * Bring up the Input dialog from Designer and load inputs
 * 
 * @author jtarnowski
 */
public class LoadInputAction extends AbstractTracerExecutionAction {

    /**
	 * Constructor
	 */
    public LoadInputAction() {
        super();
    }

    /**
	 * Constructor used when this is menu item
	 * 
	 * @param text -
	 *            String
	 * @param image -
	 *            Image
	 */
    public LoadInputAction(final String text, final Image image) {
        super(text, image);
    }

    /**
	 * Execute the action
	 */
    @Override
    public void run() {
        LoadInputDataHelper.loadInputData(mainTracer.getActiveTracer());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (!isEnabled()) {
            logger.printStackTrace(new Exception("Set Enabled False Debugging"));
        }
    }

    @Override
    protected void evaluateState() {
        if (mainTracer == null) {
            setEnabled(false);
        } else {
            setEnabled(mainTracer.needsInput());
        }
    }
}
