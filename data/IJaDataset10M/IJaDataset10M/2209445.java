package org.expasy.jpl.dev.libraryviewer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class DataInfoPanel extends JPanel {

    private JLabel fileName;

    private JLabel spectraCount;

    private JLabel infoLabel;

    private static JProgressBar pBar;

    public DataInfoPanel() {
        super(new BorderLayout(10, 1));
        setBackground(Color.WHITE);
        infoLabel = new JLabel();
        add(infoLabel, BorderLayout.WEST);
        fileName = new JLabel("");
        add(fileName, BorderLayout.NORTH);
        spectraCount = new JLabel();
        add(spectraCount, BorderLayout.SOUTH);
        pBar = new JProgressBar(0, 100);
        add(pBar, BorderLayout.EAST);
        pBar.setVisible(false);
        displayStartUp();
    }

    public void displayFileInfo(String file, int spectraC) {
        infoLabel.setVisible(false);
        fileName.setVisible(true);
        spectraCount.setVisible(true);
        pBar.setVisible(false);
        this.setBackground(Color.WHITE);
        setFileNameLabel(file);
        setSpectraCountLabel(spectraC);
    }

    /**
	 * @param infoLabel the infoLabel to set
	 */
    public void displayLoadingDataLabel(String info) {
        infoLabel.setVisible(true);
        fileName.setVisible(false);
        spectraCount.setVisible(false);
        pBar.setVisible(true);
        this.setBackground(Color.yellow);
        setInfoLabel(info);
    }

    public JProgressBar getProgressBar() {
        return pBar;
    }

    public void setProgressBar(JProgressBar pBar) {
        DataInfoPanel.pBar = pBar;
    }

    public void displayStartUp() {
        infoLabel.setVisible(true);
        infoLabel.setText("OPEN FILE");
        fileName.setVisible(false);
        spectraCount.setVisible(false);
        pBar.setVisible(false);
        this.setBackground(Color.WHITE);
    }

    public void displayErrorLabel(String file) {
        infoLabel.setVisible(true);
        infoLabel.setText("ERROR reading file " + file);
        fileName.setVisible(false);
        spectraCount.setVisible(false);
        pBar.setVisible(false);
        this.setBackground(Color.RED);
    }

    public void displayEmptyFileLabel(String file) {
        infoLabel.setVisible(true);
        infoLabel.setText("File empty: " + file);
        fileName.setVisible(false);
        spectraCount.setVisible(false);
        pBar.setVisible(false);
        this.setBackground(Color.WHITE);
    }

    public void setFileNameLabel(String file) {
        this.fileName.setText(file);
        this.repaint();
    }

    public void setInfoLabel(String info) {
        this.infoLabel.setText(info);
        this.repaint();
    }

    public void setSpectraCountLabel(int sC) {
        this.spectraCount.setText("Spectra in Table: " + sC);
        this.repaint();
    }
}
