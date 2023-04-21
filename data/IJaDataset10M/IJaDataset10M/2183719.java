package com.jasperassistant.designer.viewer.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import com.jasperassistant.designer.viewer.IReportViewer;
import com.jasperassistant.designer.viewer.ReportViewerEvent;

/**
 * Fit page zoom action
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class ZoomFitPageAction extends AbstractReportViewerAction {

    private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(ZoomFitPageAction.class, "images/zoomfitpage.gif");

    private static final ImageDescriptor DISABLED_ICON = ImageDescriptor.createFromFile(ZoomFitPageAction.class, "images/zoomfitpaged.gif");

    /**
	 * @see AbstractReportViewerAction#AbstractReportViewerAction(IReportViewer)
	 */
    public ZoomFitPageAction(IReportViewer viewer) {
        super(viewer);
        setText(Messages.getString("ZoomFitPageAction.label"));
        setToolTipText(Messages.getString("ZoomFitPageAction.tooltip"));
        setImageDescriptor(ICON);
        setDisabledImageDescriptor(DISABLED_ICON);
        update();
    }

    private void update() {
        setChecked(getReportViewer().getZoomMode() == IReportViewer.ZOOM_MODE_FIT_PAGE);
    }

    /**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractReportViewerAction#runBusy()
	 */
    protected void runBusy() {
        getReportViewer().setZoomMode(IReportViewer.ZOOM_MODE_FIT_PAGE);
        update();
    }

    /**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractReportViewerAction#calculateEnabled()
	 */
    protected boolean calculateEnabled() {
        return getReportViewer().canChangeZoom();
    }

    /**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractReportViewerAction#viewerStateChanged(com.jasperassistant.designer.viewer.ReportViewerEvent)
	 */
    public void viewerStateChanged(ReportViewerEvent evt) {
        update();
        super.viewerStateChanged(evt);
    }
}
