package org.simbrain.network.gui.trainer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.simbrain.network.gui.trainer.TrainerPanel.TrainerDataType;
import org.simbrain.network.trainers.InvalidDataException;
import org.simbrain.network.trainers.IterableAlgorithm;
import org.simbrain.network.trainers.Trainer;
import org.simbrain.resource.ResourceManager;
import org.simbrain.util.SFileChooser;
import org.simbrain.util.propertyeditor.ReflectivePropertyEditor;
import org.simbrain.util.table.NumericTable;
import org.simbrain.util.table.SimbrainJTable;
import org.simbrain.util.table.TableActionManager;

/**
 * Contains actions for use in Trainer GUI.
 *
 * @author jyoshimi
 */
public class TrainerGuiActions {

    /** System file separator property. */
    public static final String FS = System.getProperty("file.separator");

    /** Directory where text files for dictionaries are stored. */
    private static String DEFAULT_DIR = "." + FS + "simulations" + FS + "tables";

    /** The main user preference object. */
    private static final Preferences THE_PREFS = Preferences.userRoot().node("org/simbrain/network/trainer");

    /**
     * Action invoked when pressing the input or training data button on the
     * trainer panel.
     *
     * @param trainerPanel the parent trainer panel
     * @param type whether this is input or training data
     * @return the action
     */
    public static Action getEditDataAction(final TrainerPanel trainerPanel, final TrainerDataType type) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Table.png"));
                if (type == TrainerDataType.Input) {
                    putValue(NAME, "Input data");
                } else {
                    putValue(NAME, "Training data");
                }
                putValue(SHORT_DESCRIPTION, "Edit data");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                TrainerGuiActions.displayDataInViewerPanel(trainerPanel, type);
            }
        };
    }

    /**
     * Display the relevant type of data in a data viewer panel with its own
     * buttons.
     *
     * @param trainerPanel the parent panel
     * @param type whether this is input or training data
     * @param theFile the File associated with this data
     */
    public static void displayDataInViewerPanel(final TrainerPanel trainerPanel, final TrainerDataType type) {
        JFrame frame = new JFrame();
        final DataViewer viewer = new DataViewer(trainerPanel, type);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add("Center", viewer);
        JPanel toolbars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToolBar fileToolBar = new JToolBar();
        fileToolBar.add(getOpenCSVAction(trainerPanel, viewer.getTable(), type));
        fileToolBar.add(TableActionManager.getSaveCSVAction((NumericTable) viewer.getTable().getData()));
        toolbars.add(fileToolBar);
        JToolBar editToolBar = new JToolBar();
        editToolBar.add(TableActionManager.getInsertRowAction(viewer.getTable()));
        editToolBar.add(TableActionManager.getDeleteRowAction(viewer.getTable()));
        toolbars.add(editToolBar);
        toolbars.add(viewer.getTable().getToolbarRandomize());
        mainPanel.add("North", toolbars);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        int buffer = 10;
        int xposition = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
        int yposition = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (mainPanel.getHeight() / 2);
        if (type == TrainerDataType.Input) {
            xposition = xposition - mainPanel.getWidth() - buffer;
        } else {
            xposition = xposition + buffer;
        }
        frame.setLocation(xposition, yposition);
        frame.setVisible(true);
    }

    /**
     * Sets the current data directory in user preferences (memory for file
     * chooser).
     *
     * @param dir directory to set
     */
    public static void setDataDirectory(final String dir) {
        THE_PREFS.put("dataDirectory", dir);
    }

    /**
     * Return the current data directory.
     *
     * @return return the data directory
     */
    public static String getDataDirectory() {
        return THE_PREFS.get("dataDirectory", DEFAULT_DIR);
    }

    /**
     * Action for opening from comma separated value file. Replaces the default
     * simbrainjtable action for this, so that the trainer and trainer panel can
     * be updated as appropriate.
     *
     * @param table table to load data in to
     * @return the action
     */
    public static Action getOpenCSVAction(final TrainerPanel trainer, final SimbrainJTable table, final TrainerDataType type) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Open.png"));
                if (type == TrainerDataType.Input) {
                    putValue(NAME, "Open input data (.csv)");
                } else {
                    putValue(NAME, "Open training data (.csv)");
                }
                putValue(SHORT_DESCRIPTION, "Open .csv data...");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                SFileChooser chooser = new SFileChooser(getDataDirectory(), "comma-separated-values (csv)", "csv");
                File theFile = chooser.showOpenDialog();
                if (theFile != null) {
                    if (type == TrainerDataType.Input) {
                        try {
                            trainer.getTrainer().setInputData(theFile);
                            ((NumericTable) table.getData()).readData(theFile);
                            ((JFrame) table.getTopLevelAncestor()).setTitle(theFile.getName());
                        } catch (InvalidDataException exception) {
                            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        try {
                            trainer.getTrainer().setTrainingData(theFile);
                            ((NumericTable) table.getData()).readData(theFile);
                            ((JFrame) table.getTopLevelAncestor()).setTitle(theFile.getName());
                        } catch (InvalidDataException exception) {
                            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    setDataDirectory(chooser.getCurrentLocation());
                }
            }
        };
    }

    /**
     * Returns a "play" action, that can be used to repeatedly iterate iterable
     * training algorithms.
     *
     * @param trainerGui reference to trainer gui
     * @return the action
     */
    public static Action getRunAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Play.png"));
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                if (trainerGui.isUpdateCompleted()) {
                    trainerGui.setUpdateCompleted(false);
                    Executors.newSingleThreadExecutor().submit(new Runnable() {

                        public void run() {
                            while (!trainerGui.isUpdateCompleted()) {
                                trainerGui.iterate();
                            }
                            {
                                putValue(SMALL_ICON, ResourceManager.getImageIcon("Play.png"));
                            }
                        }
                    });
                    putValue(SMALL_ICON, ResourceManager.getImageIcon("Stop.png"));
                } else {
                    trainerGui.setUpdateCompleted(true);
                    putValue(SMALL_ICON, ResourceManager.getImageIcon("Play.png"));
                }
            }
        };
    }

    /**
     * Returns a step action, for iterating iteratable learning algorithms one time.
     *
     * @param trainerGui reference to trainer gui
     * @return the action
     */
    public static Action getStepAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Step.png"));
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                trainerGui.iterate();
            }
        };
    }

    /**
     * Batch train network.
     *
     * @param trainerGui reference to trainer gui
     * @return the action
     */
    public static Action getBatchTrainAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("BatchPlay.png"));
                putValue(SHORT_DESCRIPTION, "Batch train network");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                trainerGui.batchTrain();
            }
        };
    }

    /**
     * Randomizes network.
     *
     * @param trainerGui reference to trainer gui
     * @return the action
     */
    public static Action getRandomizeNetworkAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Rand.png"));
                putValue(SHORT_DESCRIPTION, "Randomize network");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                if (trainerGui.getTrainer() != null) {
                    if (trainerGui.getTrainer() instanceof IterableAlgorithm) {
                        trainerGui.getTrainer().randomize();
                        trainerGui.getTrainer().getNetwork().getRootNetwork().fireNetworkChanged();
                    }
                }
            }
        };
    }

    /**
     * Clear the error graph.
     *
     * @param trainerGui reference to trainer gui
     * @return the action
     */
    public static Action getClearGraphAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Eraser.png"));
                putValue(SHORT_DESCRIPTION, "Clear graph data");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
            }
        };
    }

    /**
     * Show properties dialog for currently selected trainer.
     *
     * @param trainerGui trainer panel
     * @return the action
     */
    public static AbstractAction getPropertiesDialogAction(final TrainerPanel trainerGui) {
        return new AbstractAction() {

            {
                putValue(SMALL_ICON, ResourceManager.getImageIcon("Prefs.png"));
                putValue(SHORT_DESCRIPTION, "Show properties");
            }

            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent arg0) {
                Trainer trainer = trainerGui.getTrainer();
                ReflectivePropertyEditor editor = new ReflectivePropertyEditor();
                editor.setUseSuperclass(false);
                editor.setObject(trainer);
                JDialog dialog = editor.getDialog();
                dialog.setModal(true);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        };
    }
}
