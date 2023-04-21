package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: Oct 16, 2010
 * Time: 10:39:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartPreferencesDialog extends PreferencesDialog {

    private JCheckBox drawTitleBox;

    private JCheckBox drawGridLinesBox;

    private JCheckBox drawSpeedLegendBox;

    private JRadioButton jpegButton;

    private JRadioButton pngButton;

    public ChartPreferencesDialog() {
        super("Chart preferences");
        this.add(createCentralPanel(), BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(200, 200));
    }

    private Component createCentralPanel() {
        centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
        drawTitleBox = new JCheckBox("Draw title line", PreferencesHolder.isDrawTitle());
        drawGridLinesBox = new JCheckBox("Draw grid lines", PreferencesHolder.isDrawGridLines());
        drawSpeedLegendBox = new JCheckBox("Draw legend", PreferencesHolder.isDrawSpeedLegend());
        pngButton = new JRadioButton("png", PreferencesHolder.isPng());
        jpegButton = new JRadioButton("jpeg", PreferencesHolder.isJpeg());
        ButtonGroup group = new ButtonGroup();
        group.add(pngButton);
        group.add(jpegButton);
        JPanel saveOptionsPanel = new JPanel();
        saveOptionsPanel.setBorder(new TitledBorder("File type: "));
        saveOptionsPanel.setAlignmentX(0);
        saveOptionsPanel.setLayout(new GridLayout(0, 1));
        saveOptionsPanel.add(pngButton);
        saveOptionsPanel.add(jpegButton);
        centralPanel.add(drawTitleBox);
        centralPanel.add(drawGridLinesBox);
        centralPanel.add(drawSpeedLegendBox);
        centralPanel.add(saveOptionsPanel);
        centralPanel.setAlignmentX(0);
        centralPanel.add(Box.createVerticalGlue());
        return centralPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getOkButton()) {
            PreferencesHolder.setDrawTitle(drawTitleBox.isSelected());
            PreferencesHolder.setDrawGridLines(drawGridLinesBox.isSelected());
            PreferencesHolder.setDrawSpeedLegend(drawSpeedLegendBox.isSelected());
            PreferencesHolder.setJpeg(jpegButton.isSelected());
            PreferencesHolder.setPng(pngButton.isSelected());
            this.setVisible(false);
            this.dispose();
        }
        if (e.getSource() == getCancelButton()) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
