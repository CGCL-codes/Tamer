package org.blogtrader.platform.core.netbeans.actions;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import org.blogtrader.platform.core.action.HideAction;
import org.blogtrader.platform.core.action.ViewAction;
import org.blogtrader.platform.core.analysis.chartview.AbstractChartView;
import org.blogtrader.platform.core.analysis.chartview.AbstractChartViewContainer;
import org.blogtrader.platform.core.analysis.chartview.WithDrawingPart;
import org.blogtrader.platform.core.analysis.chartview.part.DrawingPart;
import org.blogtrader.platform.core.netbeans.explorer.DescriptorGroupNode;
import org.blogtrader.platform.core.netbeans.windows.AnalysisChartTopComponent;
import org.blogtrader.platform.core.analysis.descriptor.Descriptors;
import org.blogtrader.platform.core.analysis.descriptor.DrawingDescriptor;
import org.blogtrader.platform.core.data.timeseries.MasterTimeSeries;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author Caoyuan Deng
 */
public class SwitchHideShowDrawingLineAction extends CallableSystemAction {

    private static JToggleButton toggleButton;

    /** Creates a new instance
     */
    public SwitchHideShowDrawingLineAction() {
    }

    public void performAction() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (toggleButton.isSelected()) {
                    toggleButton.setSelected(false);
                } else {
                    toggleButton.setSelected(true);
                }
            }
        });
    }

    public String getName() {
        return "Hide or Show Drawing Line";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected String iconResource() {
        return "org/blogtrader/platform/core/netbeans/resources/hideDrawingLine.gif";
    }

    protected boolean asynchronous() {
        return false;
    }

    public static void updateToolbar(AbstractChartViewContainer selectedViewContainer) {
        AbstractChartView masterView = selectedViewContainer.getMasterView();
        if (masterView instanceof WithDrawingPart) {
            DrawingPart drawing = ((WithDrawingPart) masterView).getCurrentDrawing();
            if (drawing != null) {
                boolean selected = drawing.isActived();
                toggleButton.setSelected(selected);
            } else {
                toggleButton.setSelected(false);
            }
        }
    }

    public Component getToolbarPresenter() {
        Image iconImage = Utilities.loadImage("org/blogtrader/platform/core/netbeans/resources/hideDrawingLine.gif");
        ImageIcon icon = new ImageIcon(iconImage);
        toggleButton = new JToggleButton();
        toggleButton.setIcon(icon);
        toggleButton.setToolTipText("Show Drawing");
        toggleButton.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                AnalysisChartTopComponent analysisTc = AnalysisChartTopComponent.getSelected();
                if (analysisTc == null) {
                    return;
                }
                AbstractChartViewContainer viewContainer = analysisTc.getSelectedViewContainer();
                WithDrawingPart masterView = (WithDrawingPart) viewContainer.getMasterView();
                if (masterView.getCurrentDrawing() == null) {
                    return;
                }
                MasterTimeSeries timeSeries = viewContainer.getMasterTimeSeries();
                Descriptors descriptors = viewContainer.getDescriptors();
                DrawingDescriptor descriptor = descriptors.findDrawingDescriptor(masterView.getCurrentDrawing().getLayerName(), timeSeries.getUnit(), timeSeries.getNUnits());
                if (descriptor == null) {
                    return;
                }
                Node stockNode = analysisTc.getActivatedNodes()[0];
                Node node = stockNode.getChildren().findChild(DescriptorGroupNode.DRAWINGS).getChildren().findChild(descriptor.getDisplayName());
                if (state == ItemEvent.SELECTED) {
                    if (!((WithDrawingPart) masterView).getCurrentDrawing().isActived()) {
                        ViewAction action = (ViewAction) node.getLookup().lookup(ViewAction.class);
                        if (action != null) {
                            action.view();
                        }
                    }
                } else {
                    if (((WithDrawingPart) masterView).getCurrentDrawing().isActived()) {
                        HideAction action = (HideAction) node.getLookup().lookup(HideAction.class);
                        if (action != null) {
                            action.hide();
                        }
                    }
                }
            }
        });
        return toggleButton;
    }
}
