package vademecum.ui.visualizer.vgraphics.box.parameterpanel;

import vademecum.ui.visualizer.panel.XplorePanel;
import vademecum.ui.visualizer.vgraphics.AbstractParameterPanel;
import vademecum.ui.visualizer.vgraphics.VGraphics;
import vademecum.ui.visualizer.vgraphics.box.VGTextBox;

public class ParameterBoxText extends AbstractParameterPanel {

    private VGTextBox figure;

    private javax.swing.JComboBox comboBoxFont;

    private javax.swing.JComboBox comboBoxFontSize;

    private javax.swing.JCheckBox jCheckBoxText;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JPanel panelIndicatorFontColor;

    private javax.swing.JTextArea textArea;

    public ParameterBoxText(VGTextBox box) {
        super();
        figure = box;
        initPanel();
        this.setName("Text");
    }

    @Override
    public VGraphics getVGraphics() {
        return this.figure;
    }

    @Override
    public XplorePanel getXplorePanel() {
        return (XplorePanel) figure.getParent();
    }

    private void initPanel() {
        jCheckBoxText = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        panelIndicatorFontColor = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboBoxFont = new javax.swing.JComboBox();
        comboBoxFontSize = new javax.swing.JComboBox();
        setLayout(null);
        jCheckBoxText.setSelected(true);
        jCheckBoxText.setText("Text");
        jCheckBoxText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxText.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxText.setName("checkboxBorder");
        add(jCheckBoxText);
        jCheckBoxText.setBounds(10, 10, 80, 15);
        jLabel2.setText("Text  Color :");
        add(jLabel2);
        jLabel2.setBounds(240, 30, 90, 15);
        panelIndicatorFontColor.setBackground(new java.awt.Color(0, 0, 0));
        panelIndicatorFontColor.setName("indicatorBorder");
        add(panelIndicatorFontColor);
        panelIndicatorFontColor.setBounds(320, 28, 10, 10);
        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);
        add(jScrollPane1);
        jScrollPane1.setBounds(10, 30, 223, 78);
        jLabel1.setText("Font :");
        add(jLabel1);
        jLabel1.setBounds(240, 90, 35, 15);
        jLabel3.setText("Font-Size :");
        add(jLabel3);
        jLabel3.setBounds(240, 60, 70, 15);
        comboBoxFont.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(comboBoxFont);
        comboBoxFont.setBounds(320, 84, 230, 24);
        comboBoxFontSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(comboBoxFontSize);
        comboBoxFontSize.setBounds(320, 54, 50, 24);
    }
}
