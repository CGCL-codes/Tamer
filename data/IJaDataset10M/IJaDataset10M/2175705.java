package de.iph.arbeitsgruppenassistent.client.uzipro;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import de.iph.arbeitsgruppenassistent.client.uzipro.utils.CmnFunctions;
import de.iph.arbeitsgruppenassistent.server.properties.session.PropertiesAdminRemote;

/**
 * @author Bruns
 *
 */
public class PanKeyFigures extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel panKeyFigures;

    private JSpinner spnAdherenceToSheduleTill;

    private JSpinner spnAdherenceToSheduleFrom;

    private JLabel lblAdherenceToSheduleFrom;

    private JLabel lblTurnoverPeriod;

    private JLabel lblFlexibilityYellow;

    private JSpinner spnEfficiencyRed;

    private JSpinner spnEfficiencyYellow;

    private JLabel lblEfficiencyYellow;

    private JPanel panEfficiency;

    private JSpinner spnCycleTimeRed;

    private JSpinner spnCycleTimeYellow;

    private JLabel lblCycleTimeYellow;

    private JPanel panCycleTime;

    private JSpinner spnFlexibilityRed;

    private JSpinner spnFlexibilityYellow;

    private JPanel panFlexibility;

    private JLabel lblAdherenceToShedule;

    private JSpinner spnTurnoverPeriod;

    private JTextField txtTurnover;

    private JLabel lblTurnover;

    private JSpinner spnCycleTime;

    private JPanel panTurnover;

    private JSpinner spnAdherenceToSheduleRed;

    private JSpinner spnAdherenceToSheduleYellow;

    private JPanel panAdherenceToShedule;

    private JSpinner spnTurnoverRed;

    private JLabel lblTurnoverYellow;

    private JSpinner spnTurnoverYellow;

    private JButton btnSaveKeyFigures;

    private JSpinner spnFlexibility;

    private JLabel lblFlexibility;

    private JLabel lblCycleTime;

    private JSpinner spnEfficiency;

    private JLabel lblEfficiency;

    private JLabel lblAdherenceToSheduleTill;

    private PropertiesAdminRemote srvProps;

    public PanKeyFigures(PropertiesAdminRemote srvProps) {
        this.srvProps = srvProps;
        initGUI();
        initKeyFigures();
    }

    private void initKeyFigures() {
        ArrayList<Object> turnover = srvProps.getGOTurnover();
        if (turnover == null) {
            turnover = new ArrayList<Object>(2);
        }
        if (!(turnover.get(0) instanceof Long)) {
            turnover.set(0, 0L);
        }
        if (!(turnover.get(1) instanceof Integer)) {
            turnover.set(1, 1);
        }
        txtTurnover.setText(String.valueOf(turnover.get(0)));
        spnTurnoverPeriod.setValue(turnover.get(1));
        spnTurnoverYellow.setValue(srvProps.getGOTurnoverYellow());
        spnTurnoverRed.setValue(srvProps.getGOTurnoverRed());
        spnCycleTime.setValue(srvProps.getGOCycleTime());
        spnCycleTimeYellow.setValue(srvProps.getGOCycleTimeYellow());
        ArrayList<Integer> adherenceToShedule = srvProps.getGOAdherenceToShedules();
        if (!(adherenceToShedule.get(0) instanceof Integer)) {
            adherenceToShedule.set(0, 0);
        }
        if (!(adherenceToShedule.get(1) instanceof Integer)) {
            adherenceToShedule.set(1, 1);
        }
        spnAdherenceToSheduleFrom.setValue(adherenceToShedule.get(0));
        spnAdherenceToSheduleTill.setValue(adherenceToShedule.get(1));
        spnEfficiency.setValue(srvProps.getGOEfficiency());
        spnEfficiencyYellow.setValue(srvProps.getGOEfficiencyYellow());
        spnEfficiencyRed.setValue(srvProps.getGOEfficiencyRed());
        spnFlexibility.setValue(srvProps.getGOFlexibility());
        spnFlexibilityYellow.setValue(srvProps.getGOFlexibilityYellow());
        spnFlexibilityRed.setValue(srvProps.getGOFlexibilityRed());
    }

    private void colorSpinnerRed(JSpinner spinner) {
        JSpinner.NumberEditor cTEditorR = (JSpinner.NumberEditor) spinner.getEditor();
        cTEditorR.getTextField().setBackground(new Color(255, 180, 180));
    }

    private void colorSpinnerYellow(JSpinner spinner) {
        JSpinner.NumberEditor cTEditorR = (JSpinner.NumberEditor) spinner.getEditor();
        cTEditorR.getTextField().setBackground(new Color(255, 255, 180));
    }

    private void initGUI() {
        this.setPreferredSize(new java.awt.Dimension(518, 342));
        panKeyFigures = new JPanel();
        GroupLayout panKFTurnoverLayout = new GroupLayout((JComponent) panKeyFigures);
        panKeyFigures.setLayout(panKFTurnoverLayout);
        panKeyFigures.setBorder(BorderFactory.createTitledBorder("Kennzahlen"));
        btnSaveKeyFigures = new JButton();
        btnSaveKeyFigures.setText("Speichern");
        btnSaveKeyFigures.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                btnSaveKeyFiguresActionPerformed(evt);
            }
        });
        panTurnover = new JPanel();
        GroupLayout panTurnoverLayout = new GroupLayout((JComponent) panTurnover);
        panTurnover.setLayout(panTurnoverLayout);
        panTurnover.setBorder(BorderFactory.createTitledBorder("Umsatz"));
        lblTurnover = new JLabel();
        lblTurnover.setText("Umsatz (€)");
        txtTurnover = new JTextField();
        txtTurnover.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt) {
                txtTurnoverFocusLost(evt);
            }
        });
        lblTurnoverPeriod = new JLabel();
        lblTurnoverPeriod.setText("Zeitraum (M)");
        lblTurnoverYellow = new JLabel();
        lblTurnoverYellow.setText("Zielerreichung");
        SpinnerNumberModel spnTurnoverRedModel = new SpinnerNumberModel(75, 1, 100, 1);
        spnTurnoverRed = new JSpinner(spnTurnoverRedModel);
        colorSpinnerRed(spnTurnoverRed);
        SpinnerNumberModel spnTurnoverPeriodModel = new SpinnerNumberModel(1, 1, 24, 1);
        spnTurnoverPeriod = new JSpinner(spnTurnoverPeriodModel);
        SpinnerNumberModel spnTurnoverModelYellow = new SpinnerNumberModel(90, 1, 100, 1);
        spnTurnoverYellow = new JSpinner(spnTurnoverModelYellow);
        colorSpinnerYellow(spnTurnoverYellow);
        panFlexibility = new JPanel();
        panFlexibility.setBorder(BorderFactory.createTitledBorder("Flexibilität"));
        lblFlexibility = new JLabel();
        lblFlexibility.setText("Flexibilität SOLL (%)");
        SpinnerNumberModel spnFlexibilityModel = new SpinnerNumberModel(20.0, 0.0, 100.0, 0.1);
        spnFlexibility = new JSpinner(spnFlexibilityModel);
        lblFlexibilityYellow = new JLabel();
        lblFlexibilityYellow.setText("Zielerreichung");
        SpinnerNumberModel spnFlexibilityYellowModel = new SpinnerNumberModel(90, 1, 100, 1);
        spnFlexibilityYellow = new JSpinner(spnFlexibilityYellowModel);
        colorSpinnerYellow(spnFlexibilityYellow);
        SpinnerNumberModel spnFlexibilityRedModel = new SpinnerNumberModel(75, 1, 100, 1);
        spnFlexibilityRed = new JSpinner(spnFlexibilityRedModel);
        colorSpinnerRed(spnFlexibilityRed);
        panAdherenceToShedule = new JPanel();
        panAdherenceToShedule.setBorder(BorderFactory.createTitledBorder("Termintreue"));
        lblAdherenceToSheduleFrom = new JLabel();
        lblAdherenceToSheduleFrom.setText("Termintreue von");
        lblAdherenceToSheduleTill = new JLabel();
        lblAdherenceToSheduleTill.setText("bis (Wochen)");
        SpinnerNumberModel spnAdherenceToSheduleFromModel = new SpinnerNumberModel(-1, -99, 999, 1);
        spnAdherenceToSheduleFrom = new JSpinner(spnAdherenceToSheduleFromModel);
        SpinnerNumberModel spnAdherenceToSheduleTillModel = new SpinnerNumberModel(1, -99, 999, 1);
        spnAdherenceToSheduleTill = new JSpinner(spnAdherenceToSheduleTillModel);
        SpinnerNumberModel spnAdherenceToSheduleYellowModel = new SpinnerNumberModel(90, 1, 100, 1);
        spnAdherenceToSheduleYellow = new JSpinner(spnAdherenceToSheduleYellowModel);
        colorSpinnerYellow(spnAdherenceToSheduleYellow);
        SpinnerNumberModel spnAdherenceToSheduleRedModel = new SpinnerNumberModel(75, 1, 100, 1);
        spnAdherenceToSheduleRed = new JSpinner(spnAdherenceToSheduleRedModel);
        colorSpinnerRed(spnAdherenceToSheduleRed);
        lblAdherenceToShedule = new JLabel();
        lblAdherenceToShedule.setText("Zielerreichung");
        panEfficiency = new JPanel();
        panEfficiency.setBorder(BorderFactory.createTitledBorder("Auslastung"));
        lblEfficiency = new JLabel();
        lblEfficiency.setText("Auslastung SOLL (%)");
        SpinnerNumberModel spnEfficiencyModel = new SpinnerNumberModel(20.0, 0.0, 100.0, 0.1);
        spnEfficiency = new JSpinner(spnEfficiencyModel);
        lblEfficiencyYellow = new JLabel();
        lblEfficiencyYellow.setText("Zielerreichung");
        SpinnerNumberModel spnEfficiencyYellowModel = new SpinnerNumberModel(90, 1, 100, 1);
        spnEfficiencyYellow = new JSpinner(spnEfficiencyYellowModel);
        colorSpinnerYellow(spnEfficiencyYellow);
        SpinnerNumberModel spnEfficiencyRedModel = new SpinnerNumberModel(75, 1, 100, 1);
        spnEfficiencyRed = new JSpinner(spnEfficiencyRedModel);
        colorSpinnerRed(spnEfficiencyRed);
        panCycleTime = new JPanel();
        panCycleTime.setBorder(BorderFactory.createTitledBorder("Durchlaufzeit"));
        lblCycleTime = new JLabel();
        lblCycleTime.setText("Durchlaufzeit SOLL");
        SpinnerNumberModel spnCycleTimeModel = new SpinnerNumberModel(4, 1, 999, 1);
        spnCycleTime = new JSpinner(spnCycleTimeModel);
        lblCycleTimeYellow = new JLabel();
        lblCycleTimeYellow.setText("Zielerreichung");
        SpinnerNumberModel spnCycleTimeYellowModel = new SpinnerNumberModel(90, 1, 100, 1);
        spnCycleTimeYellow = new JSpinner(spnCycleTimeYellowModel);
        colorSpinnerYellow(spnCycleTimeYellow);
        SpinnerNumberModel spnCycleTimeRedModel = new SpinnerNumberModel(75, 1, 100, 1);
        spnCycleTimeRed = new JSpinner(spnCycleTimeRedModel);
        colorSpinnerRed(spnCycleTimeRed);
        GroupLayout turnoverLayout = new GroupLayout(panTurnover);
        turnoverLayout.setHorizontalGroup(turnoverLayout.createParallelGroup(Alignment.LEADING).addGroup(turnoverLayout.createSequentialGroup().addContainerGap().addGroup(turnoverLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblTurnover, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblTurnoverPeriod, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblTurnoverYellow, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(turnoverLayout.createParallelGroup(Alignment.TRAILING).addComponent(txtTurnover, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(spnTurnoverPeriod, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addGroup(Alignment.CENTER, turnoverLayout.createSequentialGroup().addComponent(spnTurnoverYellow, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(spnTurnoverRed, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))).addContainerGap()));
        turnoverLayout.setVerticalGroup(turnoverLayout.createParallelGroup(Alignment.LEADING).addGroup(turnoverLayout.createSequentialGroup().addGroup(turnoverLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTurnover).addComponent(txtTurnover)).addPreferredGap(ComponentPlacement.RELATED).addGroup(turnoverLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTurnoverPeriod).addComponent(spnTurnoverPeriod)).addPreferredGap(ComponentPlacement.RELATED).addGroup(turnoverLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTurnoverYellow).addComponent(spnTurnoverYellow).addComponent(spnTurnoverRed)).addContainerGap()));
        panTurnover.setLayout(turnoverLayout);
        GroupLayout adherenceToSheduleLayout = new GroupLayout(panAdherenceToShedule);
        adherenceToSheduleLayout.setHorizontalGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.LEADING).addGroup(adherenceToSheduleLayout.createSequentialGroup().addContainerGap().addGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblAdherenceToSheduleFrom, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblAdherenceToSheduleTill, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.TRAILING).addComponent(spnAdherenceToSheduleFrom, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(spnAdherenceToSheduleTill, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)).addContainerGap()));
        adherenceToSheduleLayout.setVerticalGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.LEADING).addGroup(adherenceToSheduleLayout.createSequentialGroup().addGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAdherenceToSheduleFrom).addComponent(spnAdherenceToSheduleFrom)).addPreferredGap(ComponentPlacement.RELATED).addGroup(adherenceToSheduleLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAdherenceToSheduleTill).addComponent(spnAdherenceToSheduleTill)).addContainerGap()));
        panAdherenceToShedule.setLayout(adherenceToSheduleLayout);
        GroupLayout efficiencyLayout = new GroupLayout(panEfficiency);
        efficiencyLayout.setHorizontalGroup(efficiencyLayout.createParallelGroup(Alignment.LEADING).addGroup(efficiencyLayout.createSequentialGroup().addContainerGap().addGroup(efficiencyLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblEfficiency, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblEfficiencyYellow, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(efficiencyLayout.createParallelGroup(Alignment.TRAILING).addComponent(spnEfficiency, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addGroup(Alignment.CENTER, efficiencyLayout.createSequentialGroup().addComponent(spnEfficiencyYellow, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(spnEfficiencyRed, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))).addContainerGap()));
        efficiencyLayout.setVerticalGroup(efficiencyLayout.createParallelGroup(Alignment.LEADING).addGroup(efficiencyLayout.createSequentialGroup().addGroup(efficiencyLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblEfficiency).addComponent(spnEfficiency)).addPreferredGap(ComponentPlacement.RELATED).addGroup(efficiencyLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblEfficiencyYellow).addComponent(spnEfficiencyYellow).addComponent(spnEfficiencyRed)).addContainerGap()));
        panEfficiency.setLayout(efficiencyLayout);
        GroupLayout cycleTimeLayout = new GroupLayout(panCycleTime);
        cycleTimeLayout.setHorizontalGroup(cycleTimeLayout.createParallelGroup(Alignment.LEADING).addGroup(cycleTimeLayout.createSequentialGroup().addContainerGap().addGroup(cycleTimeLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblCycleTime, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblCycleTimeYellow, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(cycleTimeLayout.createParallelGroup(Alignment.TRAILING).addComponent(spnCycleTime, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addGroup(Alignment.CENTER, cycleTimeLayout.createSequentialGroup().addComponent(spnCycleTimeYellow, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(spnCycleTimeRed, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))).addContainerGap()));
        cycleTimeLayout.setVerticalGroup(cycleTimeLayout.createParallelGroup(Alignment.LEADING).addGroup(cycleTimeLayout.createSequentialGroup().addGroup(cycleTimeLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblCycleTime).addComponent(spnCycleTime)).addPreferredGap(ComponentPlacement.RELATED).addGroup(cycleTimeLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblCycleTimeYellow).addComponent(spnCycleTimeYellow).addComponent(spnCycleTimeRed)).addContainerGap()));
        panCycleTime.setLayout(cycleTimeLayout);
        GroupLayout flexibilityLayout = new GroupLayout(panFlexibility);
        flexibilityLayout.setHorizontalGroup(flexibilityLayout.createParallelGroup(Alignment.LEADING).addGroup(flexibilityLayout.createSequentialGroup().addContainerGap().addGroup(flexibilityLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblFlexibility, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblFlexibilityYellow, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(flexibilityLayout.createParallelGroup(Alignment.TRAILING).addComponent(spnFlexibility, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addGroup(Alignment.CENTER, flexibilityLayout.createSequentialGroup().addComponent(spnFlexibilityYellow, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(spnFlexibilityRed, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))).addContainerGap()));
        flexibilityLayout.setVerticalGroup(flexibilityLayout.createParallelGroup(Alignment.LEADING).addGroup(flexibilityLayout.createSequentialGroup().addGroup(flexibilityLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblFlexibility).addComponent(spnFlexibility)).addPreferredGap(ComponentPlacement.RELATED).addGroup(flexibilityLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblFlexibilityYellow).addComponent(spnFlexibilityYellow).addComponent(spnFlexibilityRed)).addContainerGap()));
        panFlexibility.setLayout(flexibilityLayout);
        GroupLayout layout = new GroupLayout(panKeyFigures);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(panTurnover, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(panEfficiency, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(panFlexibility, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)).addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(panAdherenceToShedule, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(panCycleTime, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE).addComponent(btnSaveKeyFigures, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(panTurnover).addComponent(panAdherenceToShedule)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(panEfficiency).addComponent(panCycleTime)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(panFlexibility).addComponent(btnSaveKeyFigures)).addContainerGap()));
        panKeyFigures.setLayout(layout);
        GroupLayout thisLayout = new GroupLayout(this);
        thisLayout.setHorizontalGroup(thisLayout.createParallelGroup(Alignment.LEADING).addComponent(panKeyFigures, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));
        thisLayout.setVerticalGroup(thisLayout.createParallelGroup(Alignment.LEADING).addComponent(panKeyFigures, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        this.setLayout(thisLayout);
    }

    private void btnSaveKeyFiguresActionPerformed(ActionEvent evt) {
        try {
            srvProps.setGOTurnover(Long.parseLong(txtTurnover.getText()), (Integer) spnTurnoverPeriod.getValue());
        } catch (NumberFormatException e) {
            System.out.println("NFE beim speichern");
        }
        srvProps.setGOTurnoverYellow((Integer) spnTurnoverYellow.getValue());
        srvProps.setGOTurnoverRed((Integer) spnTurnoverRed.getValue());
        srvProps.setGOCycleTime((Integer) spnCycleTime.getValue());
        srvProps.setGOCycleTimeYellow((Integer) spnCycleTimeYellow.getValue());
        srvProps.setGOCycleTimeRed((Integer) spnCycleTimeRed.getValue());
        if ((Integer) spnAdherenceToSheduleFrom.getValue() <= 0 && (Integer) spnAdherenceToSheduleTill.getValue() >= 0) {
            srvProps.setGOAdherenceToShedules((Integer) spnAdherenceToSheduleFrom.getValue(), (Integer) spnAdherenceToSheduleTill.getValue());
        } else {
            CmnFunctions.inputFailureDialog("Termintreue", "Bereich muss 0 enthalten");
        }
        try {
            Double d = (Double) spnEfficiency.getValue();
            srvProps.setGOEfficiency(d.floatValue());
        } catch (ClassCastException e) {
            Float f = (Float) spnEfficiency.getValue();
            srvProps.setGOEfficiency(f);
        }
        srvProps.setGOEfficiencyYellow((Integer) spnEfficiencyYellow.getValue());
        srvProps.setGOEfficiencyRed((Integer) spnEfficiencyRed.getValue());
        try {
            Double d = (Double) spnFlexibility.getValue();
            srvProps.setGOFlexibility(d.floatValue());
        } catch (ClassCastException e) {
            Float f = (Float) spnFlexibility.getValue();
            srvProps.setGOFlexibility(f);
        }
        srvProps.setGOFlexibilityYellow((Integer) spnFlexibilityYellow.getValue());
        srvProps.setGOFlexibilityRed((Integer) spnFlexibilityRed.getValue());
    }

    private void txtTurnoverFocusLost(FocusEvent evt) {
        String s = txtTurnover.getText();
        s = s.trim();
        s = s.replace(".", "");
        int occ = s.indexOf(",");
        if (occ >= 0) {
            s = s.substring(0, occ);
        }
        txtTurnover.setText(s);
    }
}
