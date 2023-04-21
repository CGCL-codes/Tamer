package org.aiotrade.platform.core.ui.netbeans.explorer;

import java.awt.Image;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.aiotrade.math.timeseries.Frequency;
import org.aiotrade.util.swing.action.AddAction;
import org.aiotrade.util.swing.action.SaveAction;
import org.aiotrade.util.swing.action.ViewAction;
import org.aiotrade.charting.view.ChartViewContainer;
import org.aiotrade.charting.descriptor.DrawingDescriptor;
import org.aiotrade.math.timeseries.descriptor.AnalysisContents;
import org.aiotrade.math.timeseries.Unit;
import org.aiotrade.platform.core.netbeans.GroupDescriptor;
import org.aiotrade.platform.core.ui.netbeans.windows.AnalysisChartTopComponent;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

/**
 *
 * @author Caoyuan Deng
 */
public class DrawingGroupDescriptor extends GroupDescriptor<DrawingDescriptor> {

    public static final String NAME = "Drawings";

    private static final Image ICON = Utilities.loadImage("org/aiotrade/platform/core/ui/netbeans/resources/drawings.gif");

    public Class<DrawingDescriptor> getBindClass() {
        return DrawingDescriptor.class;
    }

    public Action[] createActions(AnalysisContents contents) {
        return new Action[] { new AddDrawingAction(contents) };
    }

    public String getDisplayName() {
        return NAME;
    }

    public String getTooltip() {
        return NAME;
    }

    public Image getIcon(int type) {
        return ICON;
    }

    private static class AddDrawingAction extends AddAction {

        private final AnalysisContents contents;

        AddDrawingAction(AnalysisContents contents) {
            this.contents = contents;
            putValue(Action.NAME, "Add Layer");
        }

        public void execute() {
            String layerName = JOptionPane.showInputDialog(WindowManager.getDefault().getMainWindow(), "Please Input Layer Name:", "Add Drawing Layer", JOptionPane.OK_CANCEL_OPTION);
            if (layerName == null) {
                return;
            }
            layerName = layerName.trim();
            Frequency freq = new Frequency(Unit.Day, 1);
            AnalysisChartTopComponent analysisTc = AnalysisChartTopComponent.lookupTopComponent(contents.getUniSymbol());
            if (analysisTc != null) {
                ChartViewContainer viewContainer = analysisTc.getSelectedViewContainer();
                if (viewContainer != null) {
                    freq = viewContainer.getController().getMasterSer().getFreq();
                }
            }
            DrawingDescriptor descriptor = contents.lookupDescriptor(DrawingDescriptor.class, layerName, freq);
            if (descriptor == null) {
                descriptor = contents.createDescriptor(DrawingDescriptor.class, layerName, freq);
            }
            if (descriptor != null) {
                contents.lookupAction(SaveAction.class).execute();
                descriptor.lookupAction(ViewAction.class).execute();
            }
        }
    }
}
