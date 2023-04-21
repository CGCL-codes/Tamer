package de.jlab.ui.modules.panels.dds;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.jlab.GlobalsLocator;
import de.jlab.boards.Board;
import de.jlab.boards.DDSBoard;
import de.jlab.boards.DIVBoard;
import de.jlab.boards.SweepListener;
import de.jlab.boards.DDSBoard.SweepThread;
import de.jlab.lab.Lab;
import de.jlab.ui.tools.DoublePoint;
import de.jlab.ui.tools.DynamicGraphPanel;
import de.jlab.ui.valuewatch.ValueWatchManager;

@SuppressWarnings("serial")
public class DDSSweepPanel extends JPanel implements SweepListener {

    private static Logger stdlog = Logger.getLogger(DDSSweepPanel.class.getName());

    JPanel jPanelSettings = new JPanel();

    DynamicGraphPanel jPanelGraph = null;

    JLabel jLabelDelay = new JLabel(GlobalsLocator.translate("label-module-dds-sweep-delay"));

    JLabel jLabelFrequencyStart = new JLabel(GlobalsLocator.translate("label-module-dds-sweep-lower-freq"));

    JLabel jLabelFrequencyEnd = new JLabel(GlobalsLocator.translate("label-module-dds-sweep-upper-freq"));

    JLabel jLabelVeff = new JLabel("Veff");

    DecimalFormat frequencyFormat = new DecimalFormat("#.#");

    DecimalFormat delayFormat = new DecimalFormat("#");

    JFormattedTextField jTextFieldFrequencyStart = new JFormattedTextField(frequencyFormat);

    JFormattedTextField jTextFieldFrequencyEnd = new JFormattedTextField(frequencyFormat);

    JFormattedTextField jTextFieldVeff = new JFormattedTextField(frequencyFormat);

    JFormattedTextField jTextFieldDelay = new JFormattedTextField(delayFormat);

    JButton jButtonNewSweep = new JButton(GlobalsLocator.translate("button-module-dds-sweep-start-sweep"));

    JButton jButtonClearSweeps = new JButton(GlobalsLocator.translate("button-module-dds-sweep-clear-sweeps"));

    JButton jButtonStopSweep = new JButton(GlobalsLocator.translate("button-module-dds-sweep-stop-sweep"));

    JButton jButtonExportAsCSV = new JButton(GlobalsLocator.translate("button-module-dds-sweep-export-sweep"));

    JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10000);

    JPanel jPanelSweepState = new JPanel();

    Lab theLab = null;

    Board theBoard = null;

    private static double MINIMUMDB = -70;

    ValueWatchManager vwManager = null;

    double lowFreqLog = 0;

    double hiFreqLog = 0;

    double rangeLog = 0;

    List<DoublePoint> graph = new ArrayList<DoublePoint>();

    SweepThread sweepThread = null;

    DecimalFormat exportFomat = new DecimalFormat("#0.0#");

    JLabel jLabelMeasuremnet = new JLabel("Messung");

    JComboBox jComboBoxMeasurement = new JComboBox();

    JRadioButton jRadioAuto = new JRadioButton(GlobalsLocator.translate("checkbox-module-dds-sweep-autoscale"));

    JRadioButton jRadioSlider = new JRadioButton(GlobalsLocator.translate("checkbox-module-dds-sweep-slider"));

    JRadioButton jRadioText = new JRadioButton(GlobalsLocator.translate("checkbox-module-dds-sweep-text"));

    JSlider jSliderLow = new JSlider(-100, 30);

    JSlider jSliderHigh = new JSlider(-100, 30);

    JTextField jTextFieldLow = new JTextField(4);

    JTextField jTextFieldHigh = new JTextField(4);

    ButtonGroup groupScale = new ButtonGroup();

    public DDSSweepPanel(Lab lab, Board aBoard, ValueWatchManager vwManager) {
        theLab = lab;
        this.theBoard = aBoard;
        this.vwManager = vwManager;
        initUI();
    }

    private void initUI() {
        this.setPreferredSize(new Dimension(900, 500));
        jSliderLow.setMajorTickSpacing(20);
        jSliderHigh.setMajorTickSpacing(20);
        jSliderLow.setMinorTickSpacing(5);
        jSliderHigh.setMinorTickSpacing(5);
        jSliderHigh.setPaintTicks(true);
        jSliderLow.setPaintTicks(true);
        jSliderHigh.setPaintLabels(true);
        jSliderLow.setPaintLabels(true);
        groupScale.add(jRadioAuto);
        groupScale.add(jRadioSlider);
        groupScale.add(jRadioText);
        jRadioAuto.setSelected(true);
        jSliderLow.setValue(-30);
        jSliderHigh.setValue(10);
        jTextFieldLow.setText("-30");
        jTextFieldHigh.setText("10");
        jPanelSweepState.setLayout(new GridBagLayout());
        jPanelSweepState.add(progressBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSweepState.add(jButtonStopSweep, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSweepState.setVisible(false);
        jTextFieldFrequencyEnd.setValue((double) 10000);
        jTextFieldFrequencyStart.setValue((double) 10);
        jTextFieldDelay.setValue(0);
        jTextFieldVeff.setValue((double) 5);
        jPanelGraph = new DynamicGraphPanel(1, 10000, MINIMUMDB, 10);
        jPanelGraph.setBackground(Color.BLACK);
        jPanelGraph.setColorMajorGrid(Color.GRAY);
        jPanelGraph.setColorMinorGrid(Color.DARK_GRAY);
        jPanelGraph.setColorAxis(Color.WHITE);
        jPanelGraph.setLabelFormatX(new DecimalFormat("#0 Hz"));
        jPanelGraph.setLabelFormatY(new DecimalFormat("#0 dB"));
        jPanelGraph.setLogarithmicX(true);
        jPanelGraph.setTicks(1, 5, 1, 20);
        this.setLayout(new GridBagLayout());
        jPanelSettings.setLayout(new GridBagLayout());
        jTextFieldFrequencyStart.setColumns(8);
        jTextFieldFrequencyEnd.setColumns(8);
        jTextFieldVeff.setColumns(6);
        jPanelSettings.add(jLabelFrequencyStart, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldFrequencyStart, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jLabelFrequencyEnd, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldFrequencyEnd, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jLabelDelay, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldDelay, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jLabelVeff, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldVeff, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jLabelMeasuremnet, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jComboBoxMeasurement, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jRadioAuto, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jRadioSlider, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jSliderLow, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jSliderHigh, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jRadioText, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldLow, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jTextFieldHigh, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jButtonNewSweep, new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jButtonClearSweeps, new GridBagConstraints(0, 11, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jButtonExportAsCSV, new GridBagConstraints(0, 12, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelSettings.add(jPanelSweepState, new GridBagConstraints(0, 13, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelSettings, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelGraph, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        jButtonNewSweep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                startSweep();
            }
        });
        jSliderLow.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setScale();
            }
        });
        jSliderHigh.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setScale();
            }
        });
        jTextFieldLow.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                setScale();
            }
        });
        jTextFieldHigh.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                setScale();
            }
        });
        jRadioAuto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setScale();
            }
        });
        jRadioSlider.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setScale();
            }
        });
        jRadioText.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setScale();
            }
        });
        jButtonClearSweeps.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jPanelGraph.removeAllGraphs();
            }
        });
        jButtonStopSweep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sweepThread.stopSweep();
            }
        });
        jButtonExportAsCSV.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exportGraphs();
            }
        });
        checkMeasurements(null);
        setScale();
    }

    private void setScale() {
        if (jRadioAuto.isSelected()) {
            jPanelGraph.setAutoscaleY(true);
            jSliderHigh.setEnabled(false);
            jSliderLow.setEnabled(false);
            jTextFieldHigh.setEnabled(false);
            jTextFieldLow.setEnabled(false);
        } else if (jRadioText.isSelected()) {
            jPanelGraph.setAutoscaleY(false);
            jSliderHigh.setEnabled(false);
            jSliderLow.setEnabled(false);
            jTextFieldHigh.setEnabled(true);
            jTextFieldLow.setEnabled(true);
            double low = -70;
            if (jTextFieldLow.getText().length() != 0) low = Double.parseDouble(jTextFieldLow.getText());
            double high = 10;
            if (jTextFieldHigh.getText().length() != 0) high = Double.parseDouble(jTextFieldHigh.getText());
            jPanelGraph.setRangeY(low, high);
        } else {
            jPanelGraph.setAutoscaleY(false);
            jSliderHigh.setEnabled(true);
            jSliderLow.setEnabled(true);
            jTextFieldHigh.setEnabled(false);
            jTextFieldLow.setEnabled(false);
            double low = jSliderLow.getValue();
            double high = jSliderHigh.getValue();
            jPanelGraph.setRangeY(low, high);
        }
        if (sweepThread == null || !sweepThread.isAlive()) {
            if (jPanelGraph.getAllGraphs().size() > 0) jPanelGraph.refreshGraphs();
        }
    }

    public void exportGraphs() {
        List<List<DoublePoint>> graphs = jPanelGraph.getAllGraphs();
        JFileChooser exportFileChooser = new JFileChooser();
        int reply = exportFileChooser.showSaveDialog(GlobalsLocator.getMainFrame());
        if (reply == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fos = new FileOutputStream(exportFileChooser.getSelectedFile());
                String header = "JLab DDS Sweep Export " + new Date() + "\n";
                fos.write(header.getBytes());
                for (int i = 0; i < graphs.size(); ++i) {
                    List<DoublePoint> currGraph = graphs.get(i);
                    String graphHeader = "Graph #" + i + " (f;dB)\n";
                    fos.write(graphHeader.getBytes());
                    for (int u = 0; u < currGraph.size(); ++u) {
                        DoublePoint currPoint = currGraph.get(u);
                        String line = exportFomat.format(currPoint.getX()) + ";" + exportFomat.format(currPoint.getY()) + "\n";
                        fos.write(line.getBytes());
                    }
                }
                fos.close();
            } catch (Exception e) {
                stdlog.log(Level.SEVERE, "Error in writing csv", e);
            }
        }
    }

    public void startSweep() {
        double lowFreq = 0;
        long delay = 0;
        try {
            delay = (Long) jTextFieldDelay.getValue();
        } catch (RuntimeException e1) {
        }
        try {
            lowFreq = (Double) jTextFieldFrequencyStart.getValue();
        } catch (RuntimeException e1) {
            lowFreq = (Long) jTextFieldFrequencyStart.getValue();
        }
        double hiFreq = 0;
        try {
            hiFreq = (Double) jTextFieldFrequencyEnd.getValue();
        } catch (RuntimeException e1) {
            hiFreq = (Long) jTextFieldFrequencyEnd.getValue();
        }
        lowFreqLog = Math.log10(lowFreq);
        hiFreqLog = Math.log10(hiFreq);
        rangeLog = hiFreqLog - lowFreqLog;
        jPanelGraph.setRangeX(lowFreq, hiFreq);
        double vEff = 0;
        try {
            vEff = (Double) jTextFieldVeff.getValue();
        } catch (RuntimeException e1) {
            vEff = (Long) jTextFieldVeff.getValue();
        }
        int resolution = (int) (jPanelGraph.getSize().getWidth());
        DDSBoard dds = (DDSBoard) theBoard;
        graph = new ArrayList<DoublePoint>();
        jPanelGraph.addGraph(graph);
        sweepThread = dds.threadedSweep(vEff, lowFreq, hiFreq, delay, resolution, MINIMUMDB, (Board) jComboBoxMeasurement.getSelectedItem(), this);
        sweepThread.start();
        jPanelSweepState.setVisible(true);
        jButtonNewSweep.setEnabled(false);
        jButtonClearSweeps.setEnabled(false);
    }

    public void finished() {
        jPanelSweepState.setVisible(false);
        jButtonNewSweep.setEnabled(true);
        jButtonClearSweeps.setEnabled(true);
    }

    public void setValue(double freq, double db) {
        double currValue = Math.log10(freq);
        int percent = (int) ((currValue - lowFreqLog) / rangeLog * 10000);
        progressBar.setValue(percent);
        graph.add(new DoublePoint(freq, db));
        jPanelGraph.refreshGraphs();
    }

    private void checkMeasurements(Board defaultBoard) {
        jComboBoxMeasurement.removeAllItems();
        jComboBoxMeasurement.addItem(theBoard);
        jComboBoxMeasurement.setSelectedItem(theBoard);
        boolean measurementfits = false;
        for (Board currBoard : theLab.getAllBoardsFound()) {
            if (currBoard instanceof DIVBoard) {
                jComboBoxMeasurement.addItem(currBoard);
                if (defaultBoard != null && defaultBoard.equals(currBoard)) jComboBoxMeasurement.setSelectedItem(defaultBoard);
            }
        }
        jComboBoxMeasurement.setRenderer(new MeasurementRenderer());
    }

    class MeasurementRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Board board = (Board) value;
            if (board != null) {
                label.setText(board.getBoardInstanceIdentifier());
            }
            return label;
        }
    }

    public Board getMeasurement() {
        if (jComboBoxMeasurement.getSelectedItem() instanceof String) return null; else return (Board) jComboBoxMeasurement.getSelectedItem();
    }

    public void setMeasurement(Board measurement) {
        checkMeasurements(measurement);
    }
}
