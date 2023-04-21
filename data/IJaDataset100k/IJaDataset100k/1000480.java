package org.goniolab.cogo;

import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.almondframework.commons.ASwing;
import org.almondframework.l10n.ALexicon;
import org.goniolab.math.geometry.GDistance;

/**
 *
 * @author  Patrik Karlsson
 */
public class ModScale extends Mod implements Calculateable {

    public ModScale() {
        initComponents();
        initObjects();
    }

    public ModScale(ALexicon lexicon) {
        initComponents();
        initObjects();
        lexicon.setPair(realityRadioButton, "mod.scalecalculator.reality");
        lexicon.setPair(mapRadioButton, "mod.scalecalculator.map");
        lexicon.setPair(scaleRadioButton, "mod.scalecalculator.scale");
        setLocale(getAApplication().getLocale());
    }

    @Override
    public void doActivate() {
    }

    @Override
    public void doCalculate() {
        calcButtonActionPerformed(null);
    }

    @Override
    public void doReset() {
    }

    private void initObjects() {
        getAApplication().getLexiconNotificationList().add(this);
        realityUnitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mm", "cm", "dm", "m", "km", "in", "ft", "yd", "mi" }));
        mapUnitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mm", "cm", "dm", "m", "km", "in", "ft", "yd", "mi" }));
        realityUnitComboBox.setSelectedIndex(3);
        mapRadioButton.doClick();
    }

    private void initScale() {
        try {
            String tmpScale = scaleTextField.getText().replace(',', '.');
            scale = Double.parseDouble(tmpScale);
        } catch (NumberFormatException e) {
        }
        scaleTextField.setText(scaleDecimalFormat.format(scale));
        if (ratio1NRadioButton.isSelected()) {
            scale = 1 / scale;
        }
    }

    private void initMap() {
        try {
            String tmpMap = mapTextField.getText().replace(',', '.');
            map = Double.parseDouble(tmpMap);
        } catch (NumberFormatException e) {
        }
        mapTextField.setText(distDecimalFormat.format(map));
        int i = mapUnitComboBox.getSelectedIndex();
        switch(GDistance.Unit.class.getEnumConstants()[i]) {
            case MM:
                mapDistance.setMilliMeter(map);
                break;
            case CM:
                mapDistance.setCentiMeter(map);
                break;
            case DM:
                mapDistance.setDeciMeter(map);
                break;
            case M:
                mapDistance.setMeter(map);
                break;
            case KM:
                mapDistance.setKiloMeter(map);
                break;
            case IN:
                mapDistance.setInch(map);
                break;
            case FT:
                mapDistance.setFeet(map);
                break;
            case YD:
                mapDistance.setYard(map);
                break;
            case MI:
                mapDistance.setMile(map);
                break;
        }
    }

    private void initReality() {
        try {
            String tmpReality = realityTextField.getText().replace(',', '.');
            reality = Double.parseDouble(tmpReality);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        realityTextField.setText(distDecimalFormat.format(reality));
        int i = realityUnitComboBox.getSelectedIndex();
        switch(GDistance.Unit.class.getEnumConstants()[i]) {
            case MM:
                realityDistance.setMilliMeter(reality);
                break;
            case CM:
                realityDistance.setCentiMeter(reality);
                break;
            case DM:
                realityDistance.setDeciMeter(reality);
                break;
            case M:
                realityDistance.setMeter(reality);
                break;
            case KM:
                realityDistance.setKiloMeter(reality);
                break;
            case IN:
                realityDistance.setInch(reality);
                break;
            case FT:
                realityDistance.setFeet(reality);
                break;
            case YD:
                realityDistance.setYard(reality);
                break;
            case MI:
                realityDistance.setMile(reality);
                break;
        }
    }

    private void calcMap() {
        mapDistance.set(realityDistance.get() * (scale));
        mapUnitComboBoxActionPerformed(null);
    }

    private void calcReality() {
        realityDistance.set(mapDistance.get() * (1 / scale));
        realityUnitComboBoxActionPerformed(null);
    }

    private void calcScale() {
        scale = realityDistance.get() / mapDistance.get();
        if (scale < 1) {
            ratioN1RadioButton.setSelected(true);
            scale = 1 / scale;
        } else {
            ratio1NRadioButton.setSelected(true);
        }
        scaleTextField.setText(scaleDecimalFormat.format(scale));
    }

    private void enableAllInput() {
        realityTextField.setEnabled(true);
        realityTextField.setBackground(INPUT_EN);
        realityUnitComboBox.setBackground(INPUT_EN);
        mapTextField.setEnabled(true);
        mapTextField.setBackground(INPUT_EN);
        mapUnitComboBox.setBackground(INPUT_EN);
        scaleTextField.setEnabled(true);
        scaleTextField.setBackground(INPUT_EN);
        ratio1NRadioButton.setEnabled(true);
        ratioN1RadioButton.setEnabled(true);
    }

    private void initComponents() {
        scaleButtonGroup = new javax.swing.ButtonGroup();
        calcButtonGroup = new javax.swing.ButtonGroup();
        realityTextField = new javax.swing.JTextField();
        realityUnitComboBox = new javax.swing.JComboBox();
        mapTextField = new javax.swing.JTextField();
        mapUnitComboBox = new javax.swing.JComboBox();
        ratio1NRadioButton = new javax.swing.JRadioButton();
        scaleTextField = new javax.swing.JTextField();
        ratioN1RadioButton = new javax.swing.JRadioButton();
        realityRadioButton = new javax.swing.JRadioButton();
        mapRadioButton = new javax.swing.JRadioButton();
        scaleRadioButton = new javax.swing.JRadioButton();
        realityTextField.setFont(new java.awt.Font("Dialog", 1, 14));
        realityTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        realityTextField.setText("1");
        realityTextField.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        realityTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                realityTextFieldKeyTyped(evt);
            }
        });
        realityUnitComboBox.setFont(new java.awt.Font("Dialog", 0, 12));
        realityUnitComboBox.setMaximumRowCount(99);
        realityUnitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1" }));
        realityUnitComboBox.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                realityUnitComboBoxActionPerformed(evt);
            }
        });
        mapTextField.setFont(new java.awt.Font("Dialog", 1, 14));
        mapTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        mapTextField.setText("1");
        mapTextField.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        mapTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mapTextFieldKeyTyped(evt);
            }
        });
        mapUnitComboBox.setFont(new java.awt.Font("Dialog", 0, 12));
        mapUnitComboBox.setMaximumRowCount(99);
        mapUnitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        mapUnitComboBox.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapUnitComboBoxActionPerformed(evt);
            }
        });
        scaleButtonGroup.add(ratio1NRadioButton);
        ratio1NRadioButton.setSelected(true);
        ratio1NRadioButton.setText("1:");
        scaleTextField.setFont(new java.awt.Font("Dialog", 1, 14));
        scaleTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        scaleTextField.setText("1000");
        scaleTextField.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        scaleTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                scaleTextFieldKeyTyped(evt);
            }
        });
        scaleButtonGroup.add(ratioN1RadioButton);
        ratioN1RadioButton.setText(":1");
        calcButtonGroup.add(realityRadioButton);
        realityRadioButton.setFont(new java.awt.Font("Dialog", 0, 12));
        realityRadioButton.setText("jRadioButton1");
        realityRadioButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                realityRadioButtonActionPerformed(evt);
            }
        });
        calcButtonGroup.add(mapRadioButton);
        mapRadioButton.setFont(new java.awt.Font("Dialog", 0, 12));
        mapRadioButton.setText("jRadioButton2");
        mapRadioButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapRadioButtonActionPerformed(evt);
            }
        });
        calcButtonGroup.add(scaleRadioButton);
        scaleRadioButton.setFont(new java.awt.Font("Dialog", 0, 12));
        scaleRadioButton.setText("jRadioButton3");
        scaleRadioButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleRadioButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        add(Box.createHorizontalGlue());
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addComponent(realityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(realityUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(realityRadioButton).addComponent(scaleRadioButton).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(21, 21, 21).addComponent(ratio1NRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(scaleTextField)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(21, 21, 21).addComponent(mapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(mapRadioButton, javax.swing.GroupLayout.Alignment.LEADING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mapUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ratioN1RadioButton)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(realityRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(realityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(realityUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(mapRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(mapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(mapUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(scaleRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ratio1NRadioButton).addComponent(scaleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ratioN1RadioButton)).addGap(18, 18, 18).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void realityTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        if (!ASwing.isNumericTextField(evt)) {
            evt.consume();
        }
    }

    private void realityUnitComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (realityRadioButton.isSelected()) {
            int i = realityUnitComboBox.getSelectedIndex();
            switch(GDistance.Unit.class.getEnumConstants()[i]) {
                case MM:
                    reality = realityDistance.getMilliMeter();
                    break;
                case CM:
                    reality = realityDistance.getCentiMeter();
                    break;
                case DM:
                    reality = realityDistance.getDeciMeter();
                    break;
                case M:
                    reality = realityDistance.getMeter();
                    break;
                case KM:
                    reality = realityDistance.getKiloMeter();
                    break;
                case IN:
                    reality = realityDistance.getInch();
                    break;
                case FT:
                    reality = realityDistance.getFeet();
                    break;
                case YD:
                    reality = realityDistance.getYard();
                    break;
                case MI:
                    reality = realityDistance.getMile();
                    break;
            }
            realityTextField.setText(distDecimalFormat.format(reality));
        }
    }

    private void mapTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        if (!ASwing.isNumericTextField(evt)) {
            evt.consume();
        }
    }

    private void mapUnitComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (mapRadioButton.isSelected()) {
            int i = mapUnitComboBox.getSelectedIndex();
            switch(GDistance.Unit.class.getEnumConstants()[i]) {
                case MM:
                    map = mapDistance.getMilliMeter();
                    break;
                case CM:
                    map = mapDistance.getCentiMeter();
                    break;
                case DM:
                    map = mapDistance.getDeciMeter();
                    break;
                case M:
                    map = mapDistance.getMeter();
                    break;
                case KM:
                    map = mapDistance.getKiloMeter();
                    break;
                case IN:
                    map = mapDistance.getInch();
                    break;
                case FT:
                    map = mapDistance.getFeet();
                    break;
                case YD:
                    map = mapDistance.getYard();
                    break;
                case MI:
                    map = mapDistance.getMile();
                    break;
            }
            mapTextField.setText(distDecimalFormat.format(map));
        }
    }

    private void scaleTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        if (!ASwing.isNumericTextField(evt)) {
            evt.consume();
        }
    }

    private void realityRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        enableAllInput();
        realityTextField.setEnabled(false);
        realityTextField.setBackground(INPUT_DI);
        realityUnitComboBox.setBackground(INPUT_DI);
    }

    private void mapRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        enableAllInput();
        mapTextField.setEnabled(false);
        mapTextField.setBackground(INPUT_DI);
        mapUnitComboBox.setBackground(INPUT_DI);
    }

    private void scaleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        enableAllInput();
        scaleTextField.setEnabled(false);
        scaleTextField.setBackground(INPUT_DI);
        ratio1NRadioButton.setEnabled(false);
        ratioN1RadioButton.setEnabled(false);
    }

    private void calcButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (realityRadioButton.isSelected()) {
            initScale();
            initMap();
            calcReality();
        }
        if (mapRadioButton.isSelected()) {
            initScale();
            initReality();
            calcMap();
        }
        if (scaleRadioButton.isSelected()) {
            initMap();
            initReality();
            calcScale();
        }
    }

    private ButtonGroup calcButtonGroup;

    private JRadioButton mapRadioButton;

    private JTextField mapTextField;

    private JComboBox mapUnitComboBox;

    private JRadioButton ratio1NRadioButton;

    private JRadioButton ratioN1RadioButton;

    private JRadioButton realityRadioButton;

    private JTextField realityTextField;

    private JComboBox realityUnitComboBox;

    private ButtonGroup scaleButtonGroup;

    private JRadioButton scaleRadioButton;

    private JTextField scaleTextField;

    private double scale = 1.0;

    private double reality = 1.0;

    private double map = 1.0;

    private GDistance realityDistance = new GDistance();

    private GDistance mapDistance = new GDistance();

    public static final Color INPUT_DI = Color.decode("0XFFDDDD");

    public static final Color INPUT_EN = Color.decode("0XDDFFDD");

    private DecimalFormat distDecimalFormat = new DecimalFormat("0.000");

    private DecimalFormat scaleDecimalFormat = new DecimalFormat("0.000");
}
