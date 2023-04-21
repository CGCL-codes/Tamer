package edu.hawaii.ics.ami.app.experiment.view;

import edu.hawaii.ics.ami.app.experiment.model.ExperimentRunner;
import edu.hawaii.ics.ami.app.experiment.model.ExperimentSelection;
import edu.hawaii.ics.ami.lib.io.SimpleFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel that enables to select an experiment to run.
 * 
 * @author  king
 * @since   October 21, 2004
 */
public class ExperimentSelectionPanel extends JPanel {

    /** The label for the connection file. */
    private JLabel connectionFileLabel;

    /** The label for the experiment file. */
    private JLabel experimentFileLabel;

    /**
   * Constructor for the panel.
   * 
   * @param experimentSelection  Allows to select experiment file to run and where to
   *                             send the data to.
   */
    public ExperimentSelectionPanel(final ExperimentSelection experimentSelection) {
        FormLayout layout = new FormLayout("4dlu, fill:pref:grow, 4dlu", "2dlu, fill:pref:grow, 2dlu, fill:pref:grow, 2dlu, pref, 2dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new FormLayout("fill:pref:grow, 2dlu, pref", "pref"));
        connectionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Data Collection Connection"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(connectionPanel, cc.xy(2, 2));
        String connectionFileName = new File(experimentSelection.getConnectionFile()).getName();
        this.connectionFileLabel = new JLabel(connectionFileName);
        connectionPanel.add(this.connectionFileLabel, cc.xy(1, 1));
        final JButton connectionButton = new JButton("Open ...");
        connectionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser("scenario/experiment");
                SimpleFileFilter objFileFilter = new SimpleFileFilter("esx", "Event Stream Connection File");
                chooser.addChoosableFileFilter(objFileFilter);
                chooser.setMultiSelectionEnabled(false);
                int returnValue = chooser.showOpenDialog(connectionButton);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    experimentSelection.setConnectionFile(selectedFile.getPath());
                    connectionFileLabel.setText(selectedFile.getName());
                }
            }
        });
        connectionPanel.add(connectionButton, cc.xy(3, 1));
        JPanel experimentPanel = new JPanel();
        experimentPanel.setLayout(new FormLayout("fill:pref:grow, 2dlu, pref", "pref"));
        experimentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Experiment"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(experimentPanel, cc.xy(2, 4));
        String experimentFileName = new File(experimentSelection.getExperimentFile()).getName();
        this.experimentFileLabel = new JLabel(experimentFileName);
        experimentPanel.add(this.experimentFileLabel, cc.xy(1, 1));
        final JButton experimentButton = new JButton("Open ...");
        experimentButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser("scenario/experiment");
                SimpleFileFilter objFileFilter = new SimpleFileFilter("ese", "Event Stream Experiment File");
                chooser.addChoosableFileFilter(objFileFilter);
                chooser.setMultiSelectionEnabled(false);
                int returnValue = chooser.showOpenDialog(experimentButton);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    experimentSelection.setExperimentFile(selectedFile.getPath());
                    experimentFileLabel.setText(selectedFile.getName());
                }
            }
        });
        experimentPanel.add(experimentButton, cc.xy(3, 1));
        JButton startButton = new JButton("Start Experiment");
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                ExperimentRunner experiment = new ExperimentRunner(experimentSelection.getConnectionFile(), experimentSelection.getExperimentFile());
                new ExperimentWindow(experiment);
                getParent().getParent().getParent().getParent().setVisible(false);
            }
        });
        add(startButton, cc.xy(2, 6));
    }
}
