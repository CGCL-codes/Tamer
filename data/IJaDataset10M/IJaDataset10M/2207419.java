package org.adempiere.webui.window;

import org.adempiere.webui.component.Window;
import org.adempiere.webui.session.SessionManager;
import org.compiere.print.ReportEngine;
import org.compierezk.util.ReportViewerProvider;

/**
 * 
 * @author Low Heng Sin
 *
 */
public class ZkReportViewerProvider implements ReportViewerProvider {

    public void openViewer(ReportEngine report) {
        Window viewer = new ZkReportViewer(report, report.getName());
        viewer.setWidth("95%");
        viewer.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
        viewer.setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
        SessionManager.getAppDesktop().showWindow(viewer);
    }
}
