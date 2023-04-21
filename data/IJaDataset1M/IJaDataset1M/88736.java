package uk.ac.ebi.pride.gui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.core.MzGraph;
import javax.swing.*;
import java.awt.*;

/**
 * MzTabPane displays MzGraph related data, such as: Spectra or Chromatograms
 * User: rwang
 * Date: 01-Mar-2010
 * Time: 15:08:48
 */
public class MzDataTabPane extends JPanel {

    private static final Logger logger = Logger.getLogger(MzDataTabPane.class.getName());

    private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.7;

    private static final String PANE_TITLE = "Mz Data";

    private DataAccessController controller = null;

    public MzDataTabPane(DataAccessController controller) {
        this.setLayout(new BorderLayout());
        this.setName(PANE_TITLE);
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        if (SwingUtilities.isEventDispatchThread()) {
            addComponents();
        } else {
            Runnable eventDispatcher = new Runnable() {

                public void run() {
                    addComponents();
                }
            };
            SwingUtilities.invokeLater(eventDispatcher);
        }
    }

    private void addComponents() {
        MzDataSelectionPane mzSelectionPane = new MzDataSelectionPane(controller);
        MzGraph defaultMzGraph = controller.getForegroundSpectrum();
        if (defaultMzGraph == null) {
            controller.getForegroundChromatogram();
        }
        MzGraphViewPane mzViewPane = new MzGraphViewPane(defaultMzGraph);
        mzSelectionPane.addMzGraphChangeListener(mzViewPane);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mzViewPane, mzSelectionPane);
        splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
        splitPane.setOneTouchExpandable(true);
        this.add(splitPane, BorderLayout.CENTER);
    }
}
