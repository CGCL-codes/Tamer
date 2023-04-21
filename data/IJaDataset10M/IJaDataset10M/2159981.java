package fr.tango.tangopanels.devicepanels.mfdbkPll;

import fr.esrf.tangoatk.core.IAttribute;
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.esrf.tangoatk.core.IEnumScalar;
import fr.esrf.tangoatk.core.INumberScalar;
import fr.esrf.tangoatk.core.IStringScalar;
import fr.esrf.tangoatk.widget.attribute.BooleanScalarCheckBoxViewer;
import fr.esrf.tangoatk.widget.attribute.BooleanScalarComboEditor;
import fr.esrf.tangoatk.widget.attribute.EnumScalarComboEditor;
import fr.esrf.tangoatk.widget.attribute.NumberScalarComboEditor;
import fr.esrf.tangoatk.widget.attribute.NumberScalarWheelEditor;
import fr.esrf.tangoatk.widget.attribute.ScalarListViewer;
import fr.esrf.tangoatk.widget.attribute.SimpleEnumScalarViewer;
import fr.esrf.tangoatk.widget.attribute.SimpleScalarViewer;
import fr.esrf.tangoatk.widget.attribute.StringScalarComboEditor;
import fr.esrf.tangoatk.widget.attribute.StringScalarEditor;
import fr.esrf.tangoatk.widget.properties.LabelViewer;
import fr.esrf.tangoatk.widget.util.JSmoothLabel;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author poncet
 */
public class PllScalarListViewer extends ScalarListViewer {

    IStringScalar modeAtt = null;

    private boolean unitVisible;

    private boolean setterEnabled;

    private boolean setterVisible;

    private String booleanSetterType;

    private String toolTipDisplay;

    private boolean labelVisible;

    private boolean propertyButtonVisible;

    public PllScalarListViewer() {
        super();
    }

    PllModeSetter getModeSetter(IStringScalar modeAtt) {
        if (modeAtt == null) return null;
        int modeIndex = -1;
        modeIndex = listModel.indexOf(modeAtt);
        if (modeIndex >= 0) {
            JComponent modeSetter = null;
            try {
                modeSetter = scalarSetters.get(modeIndex);
            } catch (Exception ex) {
            }
            if (modeSetter == null) return null;
            if (modeSetter instanceof PllModeSetter) {
                PllModeSetter pms = (PllModeSetter) modeSetter;
                return pms;
            }
        }
        return null;
    }

    @Override
    protected void initComponents(fr.esrf.tangoatk.core.AttributeList scalarList) {
        int nbAtts, idx, viewerRow, nbScalarViewers;
        boolean containsNumberScalar;
        Object elem;
        INumberScalar ins;
        IStringScalar iss;
        IBooleanScalar ibs;
        IEnumScalar ies;
        java.awt.GridBagConstraints gridBagConstraints;
        LabelViewer scalarLabel = null;
        SimpleScalarViewer ssViewer = null;
        BooleanScalarCheckBoxViewer boolViewer = null;
        BooleanScalarComboEditor boolComboSetter = null;
        SimpleEnumScalarViewer enumViewer = null;
        EnumScalarComboEditor enumComboSetter = null;
        NumberScalarWheelEditor wheelSetter = null;
        NumberScalarComboEditor comboSetter = null;
        StringScalarEditor stringSetter = null;
        StringScalarComboEditor stringComboSetter = null;
        JComponent jcomp = null;
        JComponent viewer = null;
        JComponent setter = null;
        JButton propertyButton = null;
        int maxRowElementHeight;
        int currH;
        int hMargin;
        boolean insHasValueList, issHasValueList;
        unitVisible = super.getUnitVisible();
        setterEnabled = super.getSetterEnabled();
        setterVisible = super.getSetterVisible();
        booleanSetterType = super.getBooleanSetterType();
        toolTipDisplay = super.getToolTipDisplay();
        labelVisible = super.getLabelVisible();
        propertyButtonVisible = super.getPropertyButtonVisible();
        listModel = new Vector<IAttribute>();
        scalarLabels = new Vector<LabelViewer>();
        scalarViewers = new Vector<JComponent>();
        scalarSetters = new Vector<JComponent>();
        scalarPropButtons = new Vector<JButton>();
        viewerRow = 0;
        nbAtts = scalarList.size();
        maxRowElementHeight = 0;
        for (idx = 0; idx < nbAtts; idx++) {
            scalarLabel = null;
            viewer = null;
            boolViewer = null;
            boolComboSetter = null;
            enumViewer = null;
            enumComboSetter = null;
            ssViewer = null;
            wheelSetter = null;
            comboSetter = null;
            setter = null;
            stringSetter = null;
            stringComboSetter = null;
            propertyButton = null;
            elem = scalarList.getElementAt(idx);
            if ((elem instanceof INumberScalar) || (elem instanceof IStringScalar) || (elem instanceof IBooleanScalar) || (elem instanceof IEnumScalar)) {
                ins = null;
                iss = null;
                ibs = null;
                ies = null;
                if (elem instanceof INumberScalar) {
                    ssViewer = new SimpleScalarViewer();
                    java.awt.Insets marge = ssViewer.getMargin();
                    marge.left = marge.left + 2;
                    marge.right = marge.right + 2;
                    ssViewer.setMargin(marge);
                    viewer = ssViewer;
                    ins = (INumberScalar) elem;
                    insHasValueList = false;
                    if (ins.getPossibleValues() != null) if (ins.getPossibleValues().length > 0) insHasValueList = true;
                    if (insHasValueList) {
                        comboSetter = new NumberScalarComboEditor();
                        comboSetter.setFont(theFont);
                        comboSetter.setBackground(getBackground());
                        comboSetter.setUnitVisible(unitVisible);
                        comboSetter.setEnabled(setterEnabled);
                        if (ins.isWritable()) {
                            comboSetter.setNumberModel(ins);
                            comboSetter.setVisible(setterVisible);
                        } else comboSetter.setVisible(false);
                        scalarSetters.add(comboSetter);
                        setter = comboSetter;
                    } else {
                        wheelSetter = new NumberScalarWheelEditor();
                        wheelSetter.setFont(theFont);
                        wheelSetter.setBackground(getBackground());
                        wheelSetter.setEnabled(setterEnabled);
                        if (ins.isWritable()) {
                            wheelSetter.setModel(ins);
                            wheelSetter.setVisible(setterVisible);
                        } else wheelSetter.setVisible(false);
                        scalarSetters.add(wheelSetter);
                        setter = wheelSetter;
                    }
                } else if (elem instanceof IBooleanScalar) {
                    ibs = (IBooleanScalar) elem;
                    boolViewer = new BooleanScalarCheckBoxViewer();
                    boolViewer.setTrueLabel(new String());
                    boolViewer.setFalseLabel(new String());
                    boolViewer.setEnabled(setterEnabled);
                    viewer = boolViewer;
                    if (ibs.isWritable()) {
                        if (booleanSetterType.equalsIgnoreCase(BOOLEAN_COMBO_SETTER)) {
                            boolComboSetter = new BooleanScalarComboEditor();
                            boolComboSetter.setFont(theFont);
                            boolComboSetter.setBackground(getBackground());
                            boolComboSetter.setAttModel(ibs);
                            boolComboSetter.setVisible(setterVisible);
                            boolComboSetter.setEnabled(setterEnabled);
                            setter = boolComboSetter;
                        } else setter = null;
                    } else {
                        setter = null;
                    }
                    scalarSetters.add(setter);
                } else if (elem instanceof IEnumScalar) {
                    ies = (IEnumScalar) elem;
                    enumViewer = new SimpleEnumScalarViewer();
                    viewer = enumViewer;
                    if (ies.isWritable()) {
                        enumComboSetter = new EnumScalarComboEditor();
                        enumComboSetter.setFont(theFont);
                        enumComboSetter.setBackground(getBackground());
                        enumComboSetter.setEnumModel(ies);
                        enumComboSetter.setVisible(setterVisible);
                        enumComboSetter.setEnabled(setterEnabled);
                        setter = enumComboSetter;
                    } else {
                        setter = null;
                    }
                    scalarSetters.add(setter);
                } else {
                    ssViewer = new SimpleScalarViewer();
                    java.awt.Insets marge = ssViewer.getMargin();
                    marge.left = marge.left + 2;
                    marge.right = marge.right + 2;
                    ssViewer.setMargin(marge);
                    viewer = ssViewer;
                    iss = (IStringScalar) elem;
                    issHasValueList = false;
                    if (iss.getPossibleValues() != null) if (iss.getPossibleValues().length > 0) issHasValueList = true;
                    if (issHasValueList) {
                        stringComboSetter = new StringScalarComboEditor();
                        stringComboSetter.setFont(theFont);
                        stringComboSetter.setEnabled(setterEnabled);
                        if (iss.isWritable()) {
                            stringComboSetter.setStringModel(iss);
                            stringComboSetter.setVisible(setterVisible);
                        } else stringComboSetter.setVisible(false);
                        scalarSetters.add(stringComboSetter);
                        setter = stringComboSetter;
                    } else {
                        if (iss.getNameSansDevice().equalsIgnoreCase(PllConstants.MODE_ATT)) {
                            PllModeSetter pllMS = new PllModeSetter();
                            pllMS.setFont(theFont);
                            pllMS.setEnabled(setterEnabled);
                            if (iss.isWritable()) {
                                pllMS.setModeModel(iss);
                                pllMS.setVisible(setterVisible);
                            } else pllMS.setVisible(false);
                            scalarSetters.add(pllMS);
                            setter = pllMS;
                        } else {
                            stringSetter = new StringScalarEditor();
                            stringSetter.setFont(theFont);
                            stringSetter.setEnabled(setterEnabled);
                            if (iss.isWritable()) {
                                stringSetter.setModel(iss);
                                stringSetter.setVisible(setterVisible);
                            } else stringSetter.setVisible(false);
                            scalarSetters.add(stringSetter);
                            setter = stringSetter;
                        }
                    }
                }
                scalarLabel = new LabelViewer();
                propertyButton = new javax.swing.JButton();
                scalarLabel.setFont(theFont);
                scalarLabel.setHorizontalAlignment(JSmoothLabel.RIGHT_ALIGNMENT);
                scalarLabel.setBackground(getBackground());
                if (ins != null) scalarLabel.setModel(ins); else if (iss != null) scalarLabel.setModel(iss); else if (ibs != null) scalarLabel.setModel(ibs); else if (ies != null) scalarLabel.setModel(ies);
                if (ssViewer != null) {
                    if (toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_ALL)) {
                        ssViewer.setHasToolTip(true);
                        ssViewer.setQualityInTooltip(true);
                    } else if (toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_NAME_ONLY)) {
                        ssViewer.setHasToolTip(true);
                        ssViewer.setQualityInTooltip(false);
                    } else {
                        ssViewer.setHasToolTip(false);
                        ssViewer.setQualityInTooltip(false);
                    }
                    ssViewer.setFont(theFont);
                    ssViewer.setUnitVisible(unitVisible);
                    ssViewer.setBackgroundColor(getBackground());
                    ssViewer.setBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
                    ssViewer.setAlarmEnabled(true);
                    ssViewer.addTextListener(this);
                    if (ins != null) ssViewer.setModel(ins); else ssViewer.setModel(iss);
                } else {
                    if (boolViewer != null) {
                        if (toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_ALL) || toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_NAME_ONLY)) boolViewer.setHasToolTip(true); else boolViewer.setHasToolTip(false);
                        boolViewer.setAttModel(ibs);
                        boolViewer.setBackground(getBackground());
                    } else {
                        if (enumViewer != null) {
                            if (toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_ALL)) {
                                enumViewer.setHasToolTip(true);
                                enumViewer.setQualityInTooltip(true);
                            } else if (toolTipDisplay.equalsIgnoreCase(TOOLTIP_DISPLAY_NAME_ONLY)) {
                                enumViewer.setHasToolTip(true);
                                enumViewer.setQualityInTooltip(false);
                            } else {
                                enumViewer.setHasToolTip(false);
                                enumViewer.setQualityInTooltip(false);
                            }
                            enumViewer.setFont(theFont);
                            enumViewer.setBackgroundColor(getBackground());
                            enumViewer.setBorder(javax.swing.BorderFactory.createLoweredBevelBorder());
                            enumViewer.setAlarmEnabled(true);
                            enumViewer.addTextListener(this);
                            enumViewer.setModel(ies);
                        }
                    }
                }
                propertyButton.setFont(theFont);
                propertyButton.setBackground(getBackground());
                propertyButton.setText(" ... ");
                propertyButton.setMargin(new java.awt.Insets(-3, 0, 3, 0));
                propertyButton.setToolTipText("Attribute Properties");
                scalarLabel.setVisible(labelVisible);
                propertyButton.setVisible(propertyButtonVisible);
                if (ins != null) ins.refresh(); else if (iss != null) iss.refresh(); else if (ibs != null) ibs.refresh(); else if (ies != null) ies.refresh();
                maxRowElementHeight = 0;
                currH = scalarLabel.getPreferredSize().height + 4;
                if (currH > maxRowElementHeight) maxRowElementHeight = currH;
                currH = viewer.getPreferredSize().height + 4;
                if (currH > maxRowElementHeight) maxRowElementHeight = currH;
                if (setter != null) {
                    if (setter.isVisible()) {
                        currH = setter.getPreferredSize().height + 4;
                        if (currH > maxRowElementHeight) maxRowElementHeight = currH;
                    }
                }
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = viewerRow;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 1);
                add(scalarLabel, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = viewerRow;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 1);
                add(viewer, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = viewerRow;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                if (setter != null) {
                    if (ins != null) {
                        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
                        add(setter, gridBagConstraints);
                    } else if (iss != null) {
                        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 1);
                        add(setter, gridBagConstraints);
                    } else if (ibs != null) {
                        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 1);
                        add(setter, gridBagConstraints);
                    } else if (ies != null) {
                        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 1);
                        add(setter, gridBagConstraints);
                    }
                }
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = viewerRow;
                gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 6);
                add(propertyButton, gridBagConstraints);
                if (ins != null) listModel.add(ins); else if (iss != null) listModel.add(iss); else if (ibs != null) listModel.add(ibs); else if (ies != null) listModel.add(ies);
                scalarLabels.add(scalarLabel);
                scalarViewers.add(viewer);
                scalarPropButtons.add(propertyButton);
                if (viewer instanceof SimpleScalarViewer) {
                    SimpleScalarViewer sv = (SimpleScalarViewer) viewer;
                    currH = viewer.getPreferredSize().height;
                    if (currH < maxRowElementHeight) hMargin = (maxRowElementHeight - currH) / 2; else hMargin = 0;
                    java.awt.Insets marge = sv.getMargin();
                    marge.top = marge.top + hMargin;
                    marge.bottom = marge.bottom + hMargin;
                    marge.left = marge.left + 2;
                    marge.right = marge.right + 2;
                    sv.setMargin(marge);
                } else if (viewer instanceof SimpleEnumScalarViewer) {
                    SimpleEnumScalarViewer sesv = (SimpleEnumScalarViewer) viewer;
                    currH = viewer.getPreferredSize().height;
                    if (currH < maxRowElementHeight) hMargin = (maxRowElementHeight - currH) / 2; else hMargin = 0;
                    java.awt.Insets marge = sesv.getMargin();
                    marge.top = marge.top + hMargin;
                    marge.bottom = marge.bottom + hMargin;
                    marge.left = marge.left + 2;
                    marge.right = marge.right + 2;
                    sesv.setMargin(marge);
                }
                if ((setter instanceof StringScalarEditor) && (setter.isVisible())) {
                    StringScalarEditor sse = (StringScalarEditor) setter;
                    currH = setter.getPreferredSize().height;
                    if (currH < maxRowElementHeight) hMargin = (maxRowElementHeight - currH) / 2; else hMargin = 0;
                    java.awt.Insets marge = sse.getMargin();
                    marge.top = marge.top + hMargin;
                    marge.bottom = marge.bottom + hMargin;
                    marge.left = marge.left + 2;
                    marge.right = marge.right + 2;
                    sse.setMargin(marge);
                }
                viewerRow++;
            }
        }
    }
}
