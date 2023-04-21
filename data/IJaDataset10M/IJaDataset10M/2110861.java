package org.aiotrade.platform.core.ui.netbeans.actions;

import org.aiotrade.platform.core.ui.netbeans.explorer.IndicatorGroupDescriptor;
import org.aiotrade.util.swing.action.AddAction;
import org.aiotrade.math.timeseries.descriptor.AnalysisContents;
import org.aiotrade.platform.core.netbeans.NetBeansPersistenceManager;
import org.aiotrade.platform.core.ui.netbeans.explorer.SymbolListTopComponent;
import org.aiotrade.platform.core.ui.netbeans.windows.AnalysisChartTopComponent;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;

/**
 *
 * @author Caoyuan Deng
 */
public class PickIndicatorAction extends CallableSystemAction {

    /** Creates a new instance of ZoomInAction
     */
    public PickIndicatorAction() {
    }

    public void performAction() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                AnalysisChartTopComponent analysisWin = AnalysisChartTopComponent.getSelected();
                AnalysisContents contents = null;
                if (analysisWin == null) {
                    SymbolListTopComponent symbolListWin = (SymbolListTopComponent) WindowManager.getDefault().findTopComponent("StockListTopComponent");
                    Node[] nodes = symbolListWin.getExplorerManager().getSelectedNodes();
                    if (nodes.length > 0) {
                        contents = nodes[0].getLookup().lookup(AnalysisContents.class);
                    } else {
                        return;
                    }
                } else {
                    contents = analysisWin.getSelectedViewContainer().getController().getContents();
                }
                Node secNode = NetBeansPersistenceManager.getOccupantNode(contents);
                if (secNode != null) {
                    Node node = secNode.getChildren().findChild(IndicatorGroupDescriptor.NAME);
                    if (node != null) {
                        node.getLookup().lookup(AddAction.class).execute();
                    }
                }
            }
        });
    }

    public String getName() {
        return "Pick Indicator";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected String iconResource() {
        return "org/aiotrade/platform/core/ui/netbeans/resources/newIndicator.gif";
    }

    protected boolean asynchronous() {
        return false;
    }
}
