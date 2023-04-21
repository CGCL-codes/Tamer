package oclac.view.application.actions;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.mxgraph.swing.mxGraphComponent;
import de.unistuttgart.iev.osm.osmosiscontrol.OsmosisResult;
import oclac.data.Pipeline;
import oclac.io.CommandLineIO;
import oclac.io.OsmosisRunner;
import oclac.util.I18N;
import oclac.view.ui.MainWindow;
import oclac.view.ui.OsmosisResultDialog;
import oclac.view.ui.OsmosisRunningDialog;
import oclac.view.ui.models.PipelineGraphModel;

@SuppressWarnings("serial")
public class RunOsmosisAction extends AbstractAction implements ChangeListener {

    /**
	 * Constructor for this action.
	 */
    public RunOsmosisAction() {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/oclac/res/start.png")));
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.NAME, I18N.instance.getMessage("RunOsmosisAction.Label"));
        putValue(Action.SHORT_DESCRIPTION, I18N.instance.getMessage("RunOsmosisAction.ShortDescription"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        setEnabled(false);
    }

    /**
	 * Tries to run Osmosis for the currently open pipeline.
	 */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        Component component = MainWindow.instance.getCenterPane().getSelectedComponent();
        if (component instanceof mxGraphComponent) {
            Pipeline pipeline = ((PipelineGraphModel) ((mxGraphComponent) component).getGraph().getModel()).getPipeline();
            if (!pipeline.isValid()) {
                JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("QuickViewAction.PipelineInvalid"), I18N.instance.getMessage("DefaultDialogs.Warning"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                CommandLineIO commandLineIO = new CommandLineIO(CommandLineIO.ScriptFormats.SF_BATCH, false);
                String[] parameters = commandLineIO.createPipelineParameters(pipeline, false);
                String workingDir;
                if (pipeline.getProperties(false).getUseSolutionFolder()) {
                    if (MainWindow.instance.getStatusBar().getCurrentFile() == null) {
                        JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("RunOsmosisAction.WorkinDirError"), I18N.instance.getMessage("DefaultDialogs.Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    workingDir = MainWindow.instance.getStatusBar().getCurrentFile().getParentFile().getAbsolutePath();
                } else {
                    workingDir = pipeline.getProperties(false).getWorkingDirectory();
                }
                MainWindow.instance.getStatusBar().setStatusText(I18N.instance.getMessage("StatusBar.RuningOsmosisStatus"));
                OsmosisRunningDialog runningDialog = new OsmosisRunningDialog(MainWindow.instance, new OsmosisRunner(parameters, new File(workingDir)));
                runningDialog.setVisible(true);
                OsmosisResult result = runningDialog.getResult();
                MainWindow.instance.getStatusBar().setStatusText(I18N.instance.getMessage("StatusBar.ReadyStatus"));
                if (result == null) {
                    JOptionPane.showMessageDialog(MainWindow.instance, I18N.instance.getMessage("RunOsmosisAction.RunError"), I18N.instance.getMessage("DefaultDialogs.Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                OsmosisResultDialog dialog = new OsmosisResultDialog(MainWindow.instance, result);
                dialog.setVisible(true);
            }
        }
    }

    public void updateEnabled() {
        setEnabled(MainWindow.instance.getCenterPane().getSelectedComponent() != null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateEnabled();
    }
}
