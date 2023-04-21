package fr.esrf.tangoatk.widget.attribute;

import javax.swing.*;
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.widget.properties.LabelViewer;
import fr.esrf.tangoatk.widget.properties.UnitViewer;

/** A NumberScalarSetPanel is a Swing JPanel which displays the "read value" of 
 * the numberScalar below a NumberScalarWheelEditor. At the left side the label of
 * the attribute is displayed and at the right the unit is displayed.
 * The label and unit are optional.
 *
 */
public class NumberScalarSetPanel extends JPanel {

    private LabelViewer attLabelViewer;

    private NumberScalarViewer attNumberScalarViewer;

    private NumberScalarWheelEditor attNumberScalarWheelEditor;

    private UnitViewer attUnitViewer;

    private INumberScalar numberAtt = null;

    private boolean labelVisible = true;

    private boolean unitVisible = true;

    public NumberScalarSetPanel() {
        initComponents();
    }

    public void setFont(java.awt.Font font) {
        super.setFont(font);
        if (attNumberScalarWheelEditor != null) attNumberScalarWheelEditor.setFont(font);
        if (attNumberScalarViewer != null) attNumberScalarViewer.setFont(font);
        if (attLabelViewer != null) attLabelViewer.setFont(font);
        if (attUnitViewer != null) attUnitViewer.setFont(font);
    }

    public void setBackground(java.awt.Color bg) {
        super.setBackground(bg);
        if (attNumberScalarWheelEditor != null) attNumberScalarWheelEditor.setBackground(bg);
        if (attNumberScalarViewer != null) attNumberScalarViewer.setBackground(bg);
        if (attLabelViewer != null) attLabelViewer.setBackground(bg);
        if (attUnitViewer != null) attUnitViewer.setBackground(bg);
    }

    public void setAttModel(INumberScalar ins) {
        if (numberAtt != null) clearModel();
        if (ins == null) return;
        if (!ins.isWritable()) return;
        numberAtt = ins;
        attLabelViewer.setModel(numberAtt);
        attNumberScalarViewer.setModel(numberAtt);
        attNumberScalarViewer.setToolTipText(numberAtt.getName());
        attNumberScalarWheelEditor.setModel(numberAtt);
        attUnitViewer.setModel(numberAtt);
    }

    public INumberScalar getAttModel() {
        return numberAtt;
    }

    public void setLabelVisible(boolean lv) {
        if (lv == labelVisible) return;
        labelVisible = lv;
        attLabelViewer.setVisible(labelVisible);
        revalidate();
    }

    public boolean getLabelVisible() {
        return labelVisible;
    }

    public void setUnitVisible(boolean uv) {
        if (uv == unitVisible) return;
        unitVisible = uv;
        attUnitViewer.setVisible(unitVisible);
        revalidate();
    }

    public boolean getUnitVisible() {
        return unitVisible;
    }

    public void clearModel() {
        if (numberAtt == null) return;
        attLabelViewer.setModel(null);
        attNumberScalarViewer.setModel(null);
        attNumberScalarViewer.setToolTipText(null);
        attNumberScalarWheelEditor.setModel(null);
        attUnitViewer.setModel(null);
        numberAtt = null;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        attNumberScalarWheelEditor = new NumberScalarWheelEditor();
        attLabelViewer = new LabelViewer();
        attNumberScalarViewer = new NumberScalarViewer();
        attUnitViewer = new UnitViewer();
        setLayout(new java.awt.GridBagLayout());
        attNumberScalarWheelEditor.setBackground(getBackground());
        attNumberScalarWheelEditor.setFont(getFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 0, 9);
        add(attNumberScalarWheelEditor, gridBagConstraints);
        attLabelViewer.setFont(getFont());
        attLabelViewer.setBackground(getBackground());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 9, 5, 5);
        add(attLabelViewer, gridBagConstraints);
        attNumberScalarViewer.setBackground(new java.awt.Color(212, 208, 200));
        attNumberScalarViewer.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        attNumberScalarViewer.setFont(getFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(attNumberScalarViewer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(attUnitViewer, gridBagConstraints);
    }
}
