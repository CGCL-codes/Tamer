package com.rapidminer.gui.r.actions;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import org.freehep.util.export.ExportDialog;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.tools.ResourceAction;

/**
 * This method will export the current plot
 * @author Sebastian Land
 *
 */
public class ExportPlotAction extends ResourceAction {

    private JComponent plotPanel;

    public ExportPlotAction(JComponent plotPanel) {
        super(true, "r.save_plot");
        this.plotPanel = plotPanel;
    }

    private static final long serialVersionUID = -849342688456226409L;

    @Override
    public void actionPerformed(ActionEvent e) {
        ExportDialog exportDialog = new ExportDialog("RapidMiner");
        exportDialog.showExportDialog(RapidMinerGUI.getMainFrame(), "Export", plotPanel, "R Plot");
    }
}
