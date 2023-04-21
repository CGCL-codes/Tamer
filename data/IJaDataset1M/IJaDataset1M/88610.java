package net.maizegenetics.stats.GLM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.maizegenetics.data.*;
import net.maizegenetics.stats.GLM.*;

/**
 * Created using IntelliJ IDEA.
 * Author: Peter Bradbury
 * Date: May 17, 2004
 * Time: 2:54:03 PM
 */
public class DataDefinitionDialog extends JDialog {

    private JLabel[] columnLabels;

    private JTextField[] headerLabels;

    private JCheckBox[] chkUse;

    private JCheckBox chkOneAnalysisPerColumn;

    private JComboBox[] cboType;

    private int NumberOfColumns;

    private int NumberOfHeaders;

    private String[] headers;

    private DataDefinition dataDef;

    private boolean runAnalysis;

    ConcatenatedAlignmentWithCharacters theAlignment;

    public DataDefinitionDialog() {
        super((Frame) null, "Input Data Definition", true);
        NumberOfColumns = 12;
        NumberOfHeaders = 3;
        columnLabels = new JLabel[NumberOfColumns];
        runAnalysis = false;
        for (int i = 0; i < NumberOfColumns; i++) {
            String col = "";
            for (int j = 0; j < NumberOfHeaders; j++) {
                String head = i + "_" + j;
                if (head.length() > 0) {
                    if (col.length() > 0) col += ".";
                    col += head;
                }
            }
            columnLabels[i] = new JLabel(col);
        }
        initDialog();
    }

    public DataDefinitionDialog(ConcatenatedAlignmentWithCharacters anAlignment, Frame parentFrame) {
        super(parentFrame, "Input Data Definition", true);
        theAlignment = anAlignment;
        dataDef = theAlignment.getDataDefinition();
        NumberOfHeaders = dataDef.getNumberOfHeaders();
        NumberOfColumns = dataDef.getNumberOfTraits();
        columnLabels = new JLabel[NumberOfColumns];
        getColumnLabels();
        initDialog();
        readDataDefinition();
    }

    public static void main(String[] args) {
        DataDefinitionDialog ddd = new DataDefinitionDialog();
        ddd.setVisible(true);
        System.exit(0);
    }

    private void initDialog() {
        this.setBounds(50, 50, 800, 450);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        Box mainBox = new Box(BoxLayout.X_AXIS);
        Box dataTypeBox = new Box(BoxLayout.Y_AXIS);
        Box titleBox = new Box(BoxLayout.X_AXIS);
        Box labelBox = new Box(BoxLayout.X_AXIS);
        contentPane.add(mainBox, BorderLayout.CENTER);
        headerLabels = new JTextField[NumberOfHeaders];
        chkUse = new JCheckBox[NumberOfHeaders];
        cboType = new JComboBox[NumberOfColumns];
        JLabel lblInputTitle = new JLabel("Input Data Types");
        JLabel lblInput = new JLabel("Input");
        JLabel lblType = new JLabel("Type");
        Font mainFont = lblType.getFont().deriveFont(14F);
        Font mainFontBold = mainFont.deriveFont(Font.BOLD);
        Font mainFontPlain = mainFont.deriveFont(Font.PLAIN);
        lblInputTitle.setFont(mainFontBold);
        lblInput.setFont(mainFontBold);
        lblType.setFont(mainFontBold);
        titleBox.add(Box.createHorizontalGlue());
        titleBox.add(lblInputTitle);
        titleBox.add(Box.createHorizontalGlue());
        labelBox.add(Box.createHorizontalGlue());
        labelBox.add(lblInput);
        labelBox.add(Box.createHorizontalStrut(90));
        labelBox.add(lblType);
        labelBox.add(Box.createHorizontalGlue());
        dataTypeBox.add(titleBox);
        dataTypeBox.add(labelBox);
        String[] types = { DataDefinition.DATA, DataDefinition.FACTOR, DataDefinition.COVAR, DataDefinition.EXCLUDE };
        for (int i = 0; i < NumberOfColumns; i++) {
            cboType[i] = new JComboBox(types);
        }
        JPanel dataTypePanel = new JPanel();
        dataTypePanel.setLayout(new GridBagLayout());
        JScrollPane scr = new JScrollPane(dataTypePanel);
        scr.setBorder(BorderFactory.createEtchedBorder());
        dataTypeBox.add(scr);
        mainBox.add(dataTypeBox);
        GridBagConstraints gbc = new GridBagConstraints();
        for (int i = 0; i < NumberOfColumns; i++) {
            gbc.gridy = i + 3;
            gbc.gridx = 0;
            gbc.insets = new Insets(5, 20, 5, 10);
            dataTypePanel.add(columnLabels[i], gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 5, 40);
            dataTypePanel.add(cboType[i], gbc);
        }
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());
        labelPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblHeaderTitle = new JLabel("Data Headers");
        JLabel lblHeader = new JLabel("Header");
        JLabel lblLabel = new JLabel("Label");
        JLabel lblUse = new JLabel("Use");
        chkOneAnalysisPerColumn = new JCheckBox("Analyze Each Data Column Separately", false);
        lblHeaderTitle.setFont(mainFontBold);
        lblHeader.setFont(mainFontBold);
        lblLabel.setFont(mainFontBold);
        lblUse.setFont(mainFontBold);
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        labelPanel.add(lblHeaderTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        labelPanel.add(lblHeader, gbc);
        gbc.gridx = 1;
        labelPanel.add(lblLabel, gbc);
        gbc.gridx = 2;
        labelPanel.add(lblUse, gbc);
        for (int i = 0; i < NumberOfHeaders; i++) {
            gbc.gridy = 3 + i;
            gbc.gridx = 0;
            JLabel lh = new JLabel(Integer.toString(i + 1));
            labelPanel.add(lh, gbc);
            gbc.gridx = 1;
            headerLabels[i] = new JTextField(10);
            labelPanel.add(headerLabels[i], gbc);
            gbc.gridx = 2;
            chkUse[i] = new JCheckBox("", true);
            labelPanel.add(chkUse[i], gbc);
        }
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        labelPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 0;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.SOUTH;
        labelPanel.add(chkOneAnalysisPerColumn, gbc);
        headerLabels[0].setText("trait");
        mainBox.add(labelPanel);
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(Box.createHorizontalGlue());
        JButton excludeButton = new JButton("Exclude all");
        excludeButton.setFont(mainFontBold);
        buttonBox.add(excludeButton);
        excludeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                excludeButton_action(e);
            }
        });
        buttonBox.add(Box.createHorizontalStrut(50));
        JButton okButton = new JButton("OK");
        okButton.setFont(mainFontBold);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_action(e);
            }
        });
        buttonBox.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(mainFontBold);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_action(e);
            }
        });
        buttonBox.add(cancelButton);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contentPane.add(buttonBox, BorderLayout.SOUTH);
    }

    private void excludeButton_action(ActionEvent e) {
        for (int i = 0; i < cboType.length; i++) {
            cboType[i].setSelectedItem(DataDefinition.EXCLUDE);
        }
    }

    private void okButton_action(ActionEvent e) {
        for (int i = 0; i < NumberOfColumns; i++) {
            dataDef.setTraitType(i, (String) cboType[i].getSelectedItem());
        }
        for (int i = 0; i < NumberOfHeaders; i++) {
            dataDef.setHeaderLabel(i, headerLabels[i].getText());
            dataDef.useHeader(i, chkUse[i].isSelected());
        }
        runAnalysis = true;
        setVisible(false);
    }

    private void cancelButton_action(ActionEvent e) {
        setVisible(false);
    }

    private void getColumnLabels() {
        for (int i = 0; i < NumberOfColumns; i++) {
            String col = "";
            for (int j = 0; j < NumberOfHeaders; j++) {
                String head = theAlignment.getHeader(i, j);
                if (head.length() > 0 && !(head.equals("NA"))) {
                    if (col.length() > 0) col += ".";
                    col += head;
                }
            }
            columnLabels[i] = new JLabel(col);
        }
    }

    public boolean runAnalysis() {
        return runAnalysis;
    }

    public boolean runColumnsSeparately() {
        return chkOneAnalysisPerColumn.isSelected();
    }

    private void readDataDefinition() {
        for (int i = 0; i < NumberOfColumns; i++) {
            cboType[i].setSelectedItem(dataDef.getTraitType(i));
        }
        for (int i = 0; i < NumberOfHeaders; i++) {
            headerLabels[i].setText(dataDef.getHeaderName(i));
            headerLabels[i].setEnabled(true);
            if (dataDef.isHeaderTrait(i)) {
                headerLabels[i].setText("Trait");
                headerLabels[i].setEnabled(false);
            }
            chkUse[i].setSelected(dataDef.isHeaderUseable(i));
        }
    }
}
