package jEDF.EDF;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import jEDF.JEDF.*;
import projetotcc2.ProjetoTCC2View;

/**
 *
 * <p>Titre : jEDF</p>
 * <p>Description : Java European Data Format Viewer and Analyser</p>
 * <p>Author : Nizar Kerkeni</p>
 * <p>Copyright : Copyright (c) 2003-2006</p>
 * <p>Version 2.0</p>
 */
public class EDFSignalToTextDialog extends JDialog {

    private JComboBox jcbSignals = new JComboBox();

    private JComboBox jcbSamplig = new JComboBox();

    private JLabel jlSampling = new JLabel(JEDFMainWindow.getLocalizedString("Sampling Rate"));

    private JTextField jtfDuration = new JTextField();

    private JTextField jtfFile = new JTextField();

    private JTextField jtfStartTime = new JTextField();

    private JTextField jtfEndTime = new JTextField();

    private JButton jbOK = new JButton(JEDFMainWindow.getLocalizedString("OK"));

    private JButton jbCancel = new JButton(JEDFMainWindow.getLocalizedString("Cancel"));

    private JButton jbBrowse = new JButton(JEDFMainWindow.getLocalizedString("Browse"));

    private JRadioButton jrbAllFile = new JRadioButton(JEDFMainWindow.getLocalizedString("Complete channel"));

    private JRadioButton jrbDuration = new JRadioButton(JEDFMainWindow.getLocalizedString("By duration (s)"));

    private ButtonGroup bg = new ButtonGroup();

    private JLabel jlDataChannel = new JLabel(JEDFMainWindow.getLocalizedString("Channel"));

    private JLabel jlFrom = new JLabel(JEDFMainWindow.getLocalizedString("Epochs from"));

    private JLabel jlTo = new JLabel(JEDFMainWindow.getLocalizedString("to"));

    private JLabel jlFile = new JLabel(JEDFMainWindow.getLocalizedString("Output file"));

    private ProjetoTCC2View mainWindow = null;

    private Polysomnograph polysomnograph = null;

    private JProgressBar jProgressBar = new JProgressBar();

    private boolean signalIsInverted;

    /**
     * @param mainWindow the parent window
     * @param polysomnograph the corresponding polysomnograph
     */
    public EDFSignalToTextDialog(ProjetoTCC2View mainWindow, Polysomnograph polysomnograph) {
        super(mainWindow.getFrame(), true);
        this.mainWindow = mainWindow;
        this.polysomnograph = polysomnograph;
        this.signalIsInverted = mainWindow.getsignalInverted();
        setTitle(JEDFMainWindow.getLocalizedString("Export channel to text file"));
        initialize();
        arrange();
        addListeners();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initialize() {
        jrbAllFile.setSelected(true);
        bg.add(jrbAllFile);
        bg.add(jrbDuration);
        jtfFile.setColumns(20);
        jtfStartTime.setColumns(3);
        jtfEndTime.setColumns(3);
        jtfDuration.setColumns(3);
        EDFFile edfFile = mainWindow.getEdfFile();
        jtfDuration.setEditable(false);
        if (edfFile == null) {
            return;
        }
        JEDFGraph[] graphs = polysomnograph.getAllGraphs();
        EDFSignal[] signals = new EDFSignal[graphs.length];
        for (int i = 0; i < signals.length; i++) {
            signals[i] = graphs[i].getSignal();
        }
        if (signals == null) {
            return;
        }
        if (signals.length == 0) {
            return;
        }
        for (int i = 0; i < signals.length; i++) {
            jcbSignals.addItem(signals[i]);
        }
        jProgressBar.setStringPainted(true);
        jtfStartTime.setText("1");
        jtfEndTime.setText("" + edfFile.getNbRecords());
        jtfDuration.setText("2");
        onComboBoxSelection();
    }

    /**
     * addSamplingRates : Add a list of possible sampling rates for the selected signal.
     */
    private void addSamplingRates() {
        EDFSignal signal = (EDFSignal) jcbSignals.getSelectedItem();
        int samplingRate = (int) signal.getSamplingRate();
        int max = (int) Math.ceil(Math.log(samplingRate) / Math.log(2));
        int samplingRates[] = new int[max];
        jcbSamplig.removeAllItems();
        samplingRates[0] = samplingRate;
        jcbSamplig.addItem(new Integer(samplingRates[0]));
        int i = 1;
        while (((samplingRate % 2) == 0) && (samplingRate > 32)) {
            samplingRate /= 2;
            samplingRates[i] = samplingRate;
            jcbSamplig.addItem(new Integer(samplingRates[i]));
            i++;
        }
    }

    /**
     * getSelectedSamplingRate()
     * @return int the selected sampling rate
     */
    public int getSelectedSamplingRate() {
        Integer i = (Integer) jcbSamplig.getSelectedItem();
        return i.intValue();
    }

    /**
     * get the EDF signal status, inverted or not.
     * @return boolean the status
     */
    public boolean getsignalInverted() {
        return signalIsInverted;
    }

    private void arrange() {
        JPanel jpLine1 = new JPanel();
        jpLine1.add(jlDataChannel);
        jpLine1.add(jcbSignals);
        JPanel jpLine2 = new JPanel();
        jpLine2.add(jlFile);
        jpLine2.add(jtfFile);
        jpLine2.add(jbBrowse);
        JPanel jpLine3 = new JPanel();
        jpLine3.add(jrbAllFile);
        jpLine3.add(jrbDuration);
        jpLine3.add(jtfDuration);
        jpLine3.add(jlSampling);
        jpLine3.add(jcbSamplig);
        JPanel jpLine4 = new JPanel();
        jpLine4.add(jlFrom);
        jpLine4.add(jtfStartTime);
        jpLine4.add(jlTo);
        jpLine4.add(jtfEndTime);
        JPanel jpLine5 = new JPanel();
        jpLine5.add(jbOK);
        jpLine5.add(jbCancel);
        JPanel clientArea = new JPanel();
        clientArea.setLayout(new BoxLayout(clientArea, BoxLayout.PAGE_AXIS));
        clientArea.add(jpLine1);
        clientArea.add(jpLine2);
        clientArea.add(jpLine3);
        clientArea.add(jpLine4);
        clientArea.add(jProgressBar);
        clientArea.add(jpLine5);
        setContentPane(clientArea);
    }

    private void onRadioButtonClick() {
        jtfDuration.setEditable(jrbDuration.isSelected());
    }

    private void addListeners() {
        jbCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        jrbAllFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                onRadioButtonClick();
            }
        });
        jrbDuration.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                onRadioButtonClick();
            }
        });
        jbBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                onBrowseButtonClick();
            }
        });
        jbOK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                onOKButtonClick();
            }
        });
        jcbSignals.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                onComboBoxSelection();
            }
        });
    }

    private void onOKButtonClick() {
        File file = new File(jtfFile.getText());
        int startTime = 0;
        int endTime = 0;
        int duration = 0;
        try {
            if (jrbDuration.isSelected()) {
                duration = new Integer(jtfDuration.getText().trim()).intValue();
            }
            startTime = new Integer(jtfStartTime.getText().trim()).intValue();
            endTime = new Integer(jtfEndTime.getText().trim()).intValue();
            if (file.exists()) {
                int result = JOptionPane.showConfirmDialog(this, JEDFMainWindow.getLocalizedString("The specified file already exists.\nAre you sure you want to replace its content ?"), JEDFMainWindow.getLocalizedString("Warning"), JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
                file.delete();
            }
            EDFSignal signal = (EDFSignal) jcbSignals.getSelectedItem();
            if (signal == null) {
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (jrbDuration.isSelected()) {
                signal.saveToTextByEpoch(file, startTime - 1, endTime, duration, jProgressBar, this);
            } else {
                signal.saveToTextByEpoch(file, startTime - 1, endTime, 0, jProgressBar, this);
            }
            jbOK.setEnabled(false);
            jbBrowse.setEnabled(false);
            jtfDuration.setEnabled(false);
            jtfEndTime.setEnabled(false);
            jtfStartTime.setEnabled(false);
            jtfFile.setEnabled(false);
            jcbSignals.setEnabled(false);
            jrbAllFile.setEnabled(false);
            jrbDuration.setEnabled(false);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, JEDFMainWindow.getLocalizedString("Please verify that you have entered valid parameters."), JEDFMainWindow.getLocalizedString("Error"), JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, JEDFMainWindow.getLocalizedString("Please verify that you have entered valid parameters."), JEDFMainWindow.getLocalizedString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBrowseButtonClick() {
        File file = new File(jtfFile.getText());
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(file);
        String[] extensions = { "txt" };
        EDFFileFilter edfFileFilter = new EDFFileFilter(extensions, JEDFMainWindow.getLocalizedString("Text file"));
        chooser.setFileFilter(edfFileFilter);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            edfFileFilter.changeFileExtension(chooser.getSelectedFile(), "");
            String chosenFile = chooser.getSelectedFile().getAbsolutePath();
            if (!chosenFile.endsWith(".txt")) {
                chosenFile += ".txt";
            }
            jtfFile.setText(chosenFile);
        }
    }

    private void onComboBoxSelection() {
        EDFFile edfFile = mainWindow.getEdfFile();
        addSamplingRates();
        if (edfFile == null) {
            return;
        }
        String selectedValue = jcbSignals.getSelectedItem().toString();
        String fileName = EDFFileFilter.changeFileExtension(edfFile.getFile(), "_" + selectedValue + ".txt");
        jtfFile.setText(fileName);
    }
}
