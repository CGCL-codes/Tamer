package com.shithead.ui.svg.action;

import com.shithead.ui.svg.SVGWrapper;

public interface UIAction {

    /**
	 * Actions that involve updating the screen/svg.  Will be run in the UI
	 * do later thread
	 * @param wrapper
	 */
    public void executeUIAction(SVGWrapper wrapper);

    /**
	 * Other actions that need to occur, e.g. Sleep.  Will not be run in the UI
	 * thread
	 * @param wrapper
	 */
    public void executeOtherAction(SVGWrapper wrapper);
}
