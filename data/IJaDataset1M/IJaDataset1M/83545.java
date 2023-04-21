package de.jmulti;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.apache.log4j.Logger;
import com.jstatcom.component.TopFrame;
import com.jstatcom.engine.DefaultPCallControl;
import com.jstatcom.project.ProjectManager;
import com.jstatcom.ts.DefaultTSListPopup;
import de.jmulti.tools.PlotControlDialog;
import de.jmulti.tools.PlotControlModel;
import de.jmulti.tools.TSPlotAction;

/**
 * Main frame of the JMulTi application.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig</a>
 */
public final class JM extends TopFrame {

    private static final Logger log = Logger.getLogger(JM.class);

    private PlotControlDialog plotDialog = null;

    /**
     * Not to be invoked except from <code>main</code>.
     */
    private JM() {
        super();
    }

    private PlotControlDialog getPlotDialog() {
        if (plotDialog == null) {
            plotDialog = PlotControlDialog.valueOfGlobal();
            plotDialog.setLocationRelativeTo(this);
            plotDialog.setPlotControlModel(PlotControlModel.SHARED_PLOT_MODEL);
        }
        return plotDialog;
    }

    /**
     * Overwrites supermethod.
     */
    protected void extra() {
        JMenuItem item = new JMenuItem("Global GAUSS Graphics Settings");
        item.addActionListener(new AbstractAction("Global GAUSS Graphics Settings") {

            public void actionPerformed(ActionEvent evt) {
                getPlotDialog().setVisible(true);
            }
        });
        getControlMenu().add(item, 0);
        getControlMenu().add(new JSeparator(), 1);
        ProjectManager.getInstance().addPackagePrefix("jmulti", "de.jmulti");
        DefaultTSListPopup.getSharedInstance().addTSActionAt(TSPlotAction.TSPLOT_GAUSS, 2);
    }

    /**
     * Starts the application.
     * 
     * @param args
     *            an array of command-line arguments
     */
    public static void main(String[] args) {
        try {
            JM jmFrame = new JM();
            DefaultPCallControl.getInstance().setStopButtonVisible(false);
            jmFrame.skeleton();
        } catch (Throwable exception) {
            log.error("Exception in main()", exception);
        }
    }
}
