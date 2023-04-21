package org.plazmaforge.studio.reportviewer;

import java.util.EventListener;

/**
 * Interface implemented by classes interested in observing report viewer state
 * changes.
 * 
 */
public interface IReportViewerListener extends EventListener {

    /**
	 * This method is invoked when the state of the report viewer has changed.
	 * 
	 * @param evt
	 *            change event
	 */
    public void viewerStateChanged(ReportViewerEvent evt);
}
