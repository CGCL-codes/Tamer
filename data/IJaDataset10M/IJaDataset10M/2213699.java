package org.blogtrader.platform.core.netbeans.actions;

import javax.swing.JOptionPane;
import org.blogtrader.platform.core.analysis.chartview.AbstractChartViewContainer;
import org.blogtrader.platform.core.netbeans.windows.AnalysisChartTopComponent;
import org.blogtrader.platform.core.netbeans.windows.TickeringChartTopComponent;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Caoyuan Deng
 */
public class SwitchHideShowCursorCrossAction extends CallableSystemAction {

    /** Creates a new instance
     */
    public SwitchHideShowCursorCrossAction() {
    }

    public void performAction() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
                if (tc == null) return;
                AbstractChartViewContainer viewContainer = null;
                if (tc instanceof AnalysisChartTopComponent) {
                    for (AnalysisChartTopComponent aTc : AnalysisChartTopComponent.getTopComponents()) {
                        viewContainer = aTc.getSelectedViewContainer();
                        viewContainer.setCursorCrossVisible(!viewContainer.isCursorCrossVisible());
                        viewContainer.repaint();
                    }
                } else if (tc instanceof TickeringChartTopComponent) {
                    for (TickeringChartTopComponent aTc : TickeringChartTopComponent.getTopComponents()) {
                        viewContainer = aTc.getViewContainer();
                        viewContainer.setCursorCrossVisible(!viewContainer.isCursorCrossVisible());
                        viewContainer.repaint();
                    }
                }
            }
        });
    }

    public String getName() {
        return "Hide/Show Cross Cursor";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected String iconResource() {
        return "org/blogtrader/platform/core/netbeans/resources/hideCursorCross.gif";
    }

    protected boolean asynchronous() {
        return false;
    }
}
