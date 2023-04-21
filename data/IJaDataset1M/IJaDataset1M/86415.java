package org.jpf.fertigation;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.tinyos.tinysoa.common.Reading;
import org.jpf.pluginmanager.Connection;

class Fertigation extends JPanel implements ActionListener, FocusListener {

    private static final long serialVersionUID = 6036643890421802367L;

    private Vector<String> vWNAniones;

    private Vector<String> vWNCationes;

    private Vector<Double> vWCAniones;

    private Vector<Double> vWCCationes;

    private Vector<String> vSNAniones;

    private Vector<String> vSNCationes;

    private Vector<Double> vSCAniones;

    private Vector<Double> vSCCationes;

    private Vector<Double> vCValue;

    private Vector<String> vCName;

    private Vector<Double> vAniones;

    private Vector<Double> vCationes;

    private JTable tSol;

    private JPanel pVar, pCalc;

    private JTextField jce;

    private JRadioButton jRSoil, jRSubs;

    public static JButton bWA, bSA, bEv;

    private JButton bCrop, bFS, bCalc;

    private CropForm formCrop;

    private Connection objCon;

    private NutrientSolution fsn;

    private Analysis wAnalysis, sAnalysis;

    private AnalysisForm wa, sa;

    private PeriodicTable pt;

    private LoadCapsule capsule;

    private double cts = 0;

    private Crop crops;

    private FDialog dlg;

    private Object[][] clasific = { { "Carbonatos", 0.1 }, { "Bicarbonatos", 0.2 }, { "Cloruros", 0.3 }, { "Sulfatos", 0.35 }, { "Fosfatos", 0.05 }, { "Nitratos", 0.60 }, { "Calcio", 0.40 }, { "Magnesio", 0.25 }, { "Sodio", 0.043 }, { "Potasio", 0.35 } };

    private Vector<String> vElement = new Vector<String>();

    private Vector<Double> vElemValue = new Vector<Double>();

    /**
     * 
     */
    public Fertigation() {
        super();
        String[] columnNames = { "Nombre", "Cantidad en Gramos" };
        Object[][] columnValues = {};
        JPanel pMain = new JPanel();
        pMain.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pMain.setLayout(new BorderLayout());
        JPanel pResults = new JPanel();
        pResults.setLayout(new BorderLayout());
        pVar = new JPanel();
        pVar.setBorder(BorderFactory.createTitledBorder("Parámetros Deseados"));
        pVar.setLayout(new GridBagLayout());
        GridBagConstraints gVar = new GridBagConstraints();
        gVar.gridx = 1;
        gVar.gridy = 1;
        gVar.gridwidth = 1;
        gVar.gridheight = 1;
        gVar.fill = GridBagConstraints.HORIZONTAL;
        gVar.insets = new Insets(0, 4, 4, 4);
        JLabel lce = new JLabel("Conductividad Eléctrica: ");
        lce.setHorizontalAlignment(SwingConstants.RIGHT);
        pVar.add(lce, gVar);
        gVar.gridx = 2;
        jce = new JTextField(10);
        jce.addActionListener(this);
        pVar.add(jce, gVar);
        gVar.gridx = 1;
        gVar.gridy = 2;
        gVar.gridwidth = 2;
        JPanel pRadio = new JPanel();
        pRadio.setLayout(new FlowLayout());
        JLabel lMSel = new JLabel("Tipo de cultivo: ");
        pRadio.add(lMSel);
        jRSoil = new JRadioButton("Suelo");
        jRSoil.addActionListener(this);
        pRadio.add(jRSoil);
        jRSubs = new JRadioButton("Sustrato");
        jRSubs.setSelected(true);
        jRSubs.addActionListener(this);
        pRadio.add(jRSubs);
        ButtonGroup group = new ButtonGroup();
        group.add(jRSoil);
        group.add(jRSubs);
        pVar.add(pRadio, gVar);
        pResults.add(pVar, BorderLayout.NORTH);
        gVar.gridx = 1;
        gVar.gridy = 3;
        gVar.gridwidth = 2;
        JPanel pCalc = new JPanel();
        pCalc.setLayout(new FlowLayout());
        bCalc = new JButton("Calcular");
        bCalc.addActionListener(this);
        pCalc.add(bCalc);
        pVar.add(pCalc, gVar);
        pCalc = new JPanel();
        pCalc.setBorder(BorderFactory.createTitledBorder("Resultados"));
        pCalc.setLayout(new BorderLayout());
        tSol = new JTable(columnValues, columnNames);
        tSol.setPreferredScrollableViewportSize(new Dimension(200, 90));
        JScrollPane scrollTable = new JScrollPane(tSol);
        pCalc.add(scrollTable, BorderLayout.CENTER);
        JLabel lwater = new JLabel("Solución por litro de agua");
        pCalc.add(lwater, BorderLayout.SOUTH);
        pResults.add(pCalc, BorderLayout.CENTER);
        JPanel pControls = new JPanel();
        pControls.setLayout(new FlowLayout());
        bFS = new JButton("Formula");
        bFS.addActionListener(this);
        pControls.add(bFS);
        bCrop = new JButton("Cultivo");
        bCrop.addActionListener(this);
        pControls.add(bCrop);
        bWA = new JButton("   Agua   ");
        bWA.setEnabled(false);
        bWA.addActionListener(this);
        pControls.add(bWA);
        bSA = new JButton("   Suelo   ");
        bSA.setEnabled(false);
        bSA.addActionListener(this);
        pControls.add(bSA);
        bEv = new JButton("Evapotrans");
        bEv.setEnabled(false);
        bEv.addActionListener(this);
        pControls.add(bEv);
        pMain.add(pResults, BorderLayout.CENTER);
        pMain.add(pControls, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(pMain, BorderLayout.CENTER);
        formCrop = new CropForm();
        fsn = new NutrientSolution();
        wa = new AnalysisForm("water");
        sa = new AnalysisForm("soil");
        wAnalysis = new Analysis();
        sAnalysis = new Analysis();
        crops = new Crop();
        dlg = new FDialog();
        pt = new PeriodicTable();
        objCon = new Connection();
        capsule = new LoadCapsule();
        vElement.addElement("Carbonatos");
        vElement.addElement("Bicarbonatos");
        vElement.addElement("Cloruros");
        vElement.addElement("Sulfatos");
        vElement.addElement("Fosfatos");
        vElement.addElement("Nitratos");
        vElement.addElement("Calcio");
        vElement.addElement("Magnesio");
        vElement.addElement("Sodio");
        vElement.addElement("Potasio");
    }

    private void loadWaterDataAnalysis() {
        JTable tWAniones = wAnalysis.getTAniones();
        JTable tWCationes = wAnalysis.getTCationes();
        vWNAniones = new Vector<String>();
        vWNCationes = new Vector<String>();
        vWCAniones = new Vector<Double>();
        vWCCationes = new Vector<Double>();
        for (int i = 0; i < tWAniones.getRowCount(); i++) {
            vWNAniones.addElement(tWAniones.getModel().getValueAt(i, 0).toString());
            vWCAniones.addElement(Double.parseDouble(tWAniones.getModel().getValueAt(i, 1).toString()));
        }
        for (int i = 0; i < tWCationes.getRowCount(); i++) {
            vWNCationes.addElement(tWCationes.getModel().getValueAt(i, 0).toString());
            vWCCationes.addElement(Double.parseDouble(tWCationes.getModel().getValueAt(i, 1).toString()));
        }
    }

    private void loadSoilDataAnalysis() {
        JTable tSAniones = wAnalysis.getTAniones();
        JTable tSCationes = wAnalysis.getTCationes();
        vSNAniones = new Vector<String>();
        vSNCationes = new Vector<String>();
        vSCAniones = new Vector<Double>();
        vSCCationes = new Vector<Double>();
        for (int i = 0; i < tSAniones.getRowCount(); i++) {
            vSNAniones.addElement(tSAniones.getModel().getValueAt(i, 0).toString());
            vSCAniones.addElement(Double.parseDouble(tSAniones.getModel().getValueAt(i, 1).toString()));
        }
        for (int i = 0; i < tSCationes.getRowCount(); i++) {
            vSNCationes.addElement(tSCationes.getModel().getValueAt(i, 0).toString());
            vSCCationes.addElement(Double.parseDouble(tSCationes.getModel().getValueAt(i, 1).toString()));
        }
    }

    private void startWaterValues(Vector vElements) {
        Vector<String> vNTemp = new Vector<String>();
        Vector<Double> vCTemp = new Vector<Double>();
        for (int i = 0; i < vWNAniones.size(); i++) {
            for (int j = 0; j < vElements.size(); j++) {
                if (vWNAniones.elementAt(i).equals(vElements.elementAt(j))) {
                    vNTemp.addElement(vWNAniones.elementAt(i));
                    vCTemp.addElement(vWCAniones.elementAt(i));
                }
            }
        }
        vWNAniones = (Vector<String>) vNTemp.clone();
        vWCAniones = (Vector<Double>) vCTemp.clone();
        vNTemp.removeAllElements();
        vCTemp.removeAllElements();
        for (int i = 0; i < vWNCationes.size(); i++) {
            for (int j = 0; j < vElements.size(); j++) {
                if (vWNCationes.elementAt(i).equals(vElements.elementAt(j))) {
                    vNTemp.addElement(vWNCationes.elementAt(i));
                    vCTemp.addElement(vWCCationes.elementAt(i));
                }
            }
        }
        vWNCationes = vNTemp;
        vWCCationes = vCTemp;
    }

    private void startCropValues() {
        vCValue = crops.getMeq();
        vCName = crops.getPhenologyStage();
        System.out.println(vCName);
    }

    private void startSoilValues(Vector vElements) {
        Vector<String> vNTemp = new Vector<String>();
        Vector<Double> vCTemp = new Vector<Double>();
        for (int i = 0; i < vSNAniones.size(); i++) {
            for (int j = 0; j < vElements.size(); j++) {
                if (vSNAniones.elementAt(i).equals(vElements.elementAt(j))) {
                    vNTemp.addElement(vSNAniones.elementAt(i));
                    vCTemp.addElement(vSCAniones.elementAt(i));
                }
            }
        }
        vSNAniones = (Vector<String>) vNTemp.clone();
        vSCAniones = (Vector<Double>) vCTemp.clone();
        vNTemp.removeAllElements();
        vCTemp.removeAllElements();
        for (int i = 0; i < vSNCationes.size(); i++) {
            for (int j = 0; j < vElements.size(); j++) {
                if (vSNCationes.elementAt(i).equals(vElements.elementAt(j))) {
                    vNTemp.addElement(vSNCationes.elementAt(i));
                    vCTemp.addElement(vSCCationes.elementAt(i));
                }
            }
        }
        vSNCationes = vNTemp;
        vSCCationes = vCTemp;
    }

    private double getPercentage(String element) {
        double val = 0;
        for (int i = 0; i < clasific.length; i++) {
            if (clasific[i][0].toString().equals(element)) {
                val = Double.parseDouble(clasific[i][1].toString());
            }
        }
        return val;
    }

    /**
     * 
     */
    private void substrateResults() {
        Vector<String> vElements = new Vector<String>();
        Vector<String> vNames = new Vector<String>();
        Vector<Double> vAmount = new Vector<Double>();
        Vector<String> vFertilizers = new Vector<String>();
        Vector<Double> vFerAmount = new Vector<Double>();
        double cTotSal = 0;
        if (validation("substrate") == true) {
            if (jce.getText().length() == 0) {
                FDialog dlg = new FDialog();
                dlg.showDialog(new JFrame(), "Error", "Es necesario definir CE");
            } else {
                try {
                    vCName = new Vector<String>();
                    vCValue = new Vector<Double>();
                    vCationes = new Vector<Double>();
                    vAniones = new Vector<Double>();
                    loadWaterDataAnalysis();
                    cTotSal = Double.parseDouble(jce.getText().toString()) * 10;
                    Fertilizer fer = new Fertilizer();
                    vFertilizers = fsn.getFormulates();
                    for (int j = 0; j < vFertilizers.size(); j++) {
                        fer.getFertilizer(vFertilizers.elementAt(j));
                        vElements.addElement(fer.getClassification());
                    }
                    startCropValues();
                    startWaterValues(vElements);
                    for (int j = 0; j < vWNCationes.size(); j++) {
                        vCationes.addElement(cTotSal * getPercentage(vWNCationes.elementAt(j)));
                    }
                    for (int j = 0; j < vWNAniones.size(); j++) {
                        vAniones.addElement(cTotSal * getPercentage(vWNAniones.elementAt(j)));
                    }
                    for (int j = 0; j < vWNCationes.size(); j++) {
                        vCationes.setElementAt((vCationes.elementAt(j) - vWCCationes.elementAt(j)), j);
                    }
                    for (int j = 0; j < vWNAniones.size(); j++) {
                        vAniones.setElementAt((vAniones.elementAt(j) - vWCAniones.elementAt(j)), j);
                    }
                    for (int j = 0; j < vFertilizers.size(); j++) {
                        Fertilizer fert = new Fertilizer();
                        fert.getFertilizer(vFertilizers.elementAt(j));
                        for (int k = 0; k < vWNAniones.size(); k++) {
                            if (fert.getClassification().equals(vWNAniones.elementAt(k))) {
                                double vA = (vAniones.elementAt(k) * fert.getWeight());
                                vAmount.add(j, rounding(vA, 3));
                            }
                        }
                        for (int k = 0; k < vWNCationes.size(); k++) {
                            if (fert.getClassification().equals(vWNCationes.elementAt(k))) {
                                double vC = (vCationes.elementAt(k) * fert.getWeight());
                                vAmount.add(j, rounding(vC, 3));
                            }
                        }
                    }
                    vNames = fsn.getFormulates();
                    for (int k = 0; k < vCName.size(); k++) {
                        if (vAmount.elementAt(k).doubleValue() <= 0d) {
                            vAmount.setElementAt(0d, k);
                        }
                    }
                    DefaultTableModel mod = new DefaultTableModel();
                    mod.addColumn("Nombre", vNames);
                    mod.addColumn("Cantidad en Gramos", vAmount);
                    tSol.setModel(mod);
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * 
     */
    private void soilResults() {
        Vector<String> vElements = new Vector<String>();
        Vector<String> vNames = new Vector<String>();
        Vector<Double> vAmount = new Vector<Double>();
        Vector<String> vFertilizers = new Vector<String>();
        Vector<Double> vFerAmount = new Vector<Double>();
        double cTotSal = 0;
        if (validation("soil") == true) {
            if (jce.getText().length() == 0) {
                FDialog dlg = new FDialog();
                dlg.showDialog(new JFrame(), "Error", "Es necesario definir CE");
            } else {
                try {
                    vCName = new Vector<String>();
                    vCValue = new Vector<Double>();
                    vCationes = new Vector<Double>();
                    vAniones = new Vector<Double>();
                    loadWaterDataAnalysis();
                    loadSoilDataAnalysis();
                    cTotSal = Double.parseDouble(jce.getText().toString()) * 10;
                    Fertilizer fer = new Fertilizer();
                    vFertilizers = fsn.getFormulates();
                    for (int j = 0; j < vFertilizers.size(); j++) {
                        fer.getFertilizer(vFertilizers.elementAt(j));
                        vElements.addElement(fer.getClassification());
                    }
                    startCropValues();
                    startWaterValues(vElements);
                    startSoilValues(vElements);
                    for (int j = 0; j < vWNCationes.size(); j++) {
                        vCationes.addElement(cTotSal * getPercentage(vWNCationes.elementAt(j)));
                    }
                    for (int j = 0; j < vWNAniones.size(); j++) {
                        vAniones.addElement(cTotSal * getPercentage(vWNAniones.elementAt(j)));
                    }
                    for (int j = 0; j < vWNCationes.size(); j++) {
                        vCationes.setElementAt((vCationes.elementAt(j) - (vWCCationes.elementAt(j) + vSCCationes.elementAt(j))), j);
                    }
                    for (int j = 0; j < vWNAniones.size(); j++) {
                        vAniones.setElementAt((vAniones.elementAt(j) - (vWCAniones.elementAt(j) + vSCAniones.elementAt(j))), j);
                    }
                    for (int j = 0; j < vFertilizers.size(); j++) {
                        Fertilizer fert = new Fertilizer();
                        fert.getFertilizer(vFertilizers.elementAt(j));
                        for (int k = 0; k < vWNAniones.size(); k++) {
                            if (fert.getClassification().equals(vWNAniones.elementAt(k))) {
                                double vA = (vAniones.elementAt(k) * fert.getWeight());
                                vAmount.add(j, rounding(vA, 3));
                            }
                        }
                        for (int k = 0; k < vWNCationes.size(); k++) {
                            if (fert.getClassification().equals(vWNCationes.elementAt(k))) {
                                double vC = (vCationes.elementAt(k) * fert.getWeight());
                                vAmount.add(j, rounding(vC, 3));
                            }
                        }
                    }
                    vNames = fsn.getFormulates();
                    for (int k = 0; k < vCName.size(); k++) {
                        if (vAmount.elementAt(k).doubleValue() <= 0d) {
                            vAmount.setElementAt(0d, k);
                        }
                    }
                    DefaultTableModel mod = new DefaultTableModel();
                    mod.addColumn("Nombre", vNames);
                    mod.addColumn("Cantidad en Gramos", vAmount);
                    tSol.setModel(mod);
                } catch (Exception ex) {
                }
            }
        }
    }

    /***************************************************************************
	 * Function for rounding numbers
	 * 
	 * @param number Number to rounding double
	 * @param decimals Decimals required integer
	 * @return Rounding number double	
	 **************************************************************************/
    private double rounding(double number, int decimals) {
        return Math.round(number * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    /****
     * 
     ****/
    private boolean validation(String type) {
        boolean val = true;
        dlg = new FDialog();
        crops = formCrop.getCrop();
        wAnalysis = wa.getAnalysis();
        if (crops.getControl() == false || wAnalysis.getValidation() == false) {
            val = false;
            dlg.showDialog(new JFrame(), "Error", "Defina un análisis y un cultivo");
        } else {
            if (type.equals("soil")) {
                sAnalysis = sa.getAnalysis();
                if (sAnalysis.getValidation() == false) {
                    val = false;
                    dlg.showDialog(new JFrame(), "Error", "Defina un análisis de suelo");
                }
            }
        }
        if (fsn.iscreate() == false) {
            dlg.showDialog(new JFrame(), "Error", "Defina una solución nutritiva");
            val = false;
        }
        return val;
    }

    /**
     * 
     */
    private boolean validateDate(Date ini, Date end) {
        boolean val = true;
        if (end.before(ini)) {
            System.out.println("La inicial es mayor");
            val = false;
        } else {
            System.out.println("La inicial es menor o igual");
        }
        return val;
    }

    /**
     * 
     */
    public void loadService() {
        Date dateInitial = null, dateFinal = null;
        Vector<Vector> vData = new Vector<Vector>();
        Vector<Date> vDay = new Vector<Date>();
        Vector<Double> vValue = new Vector<Double>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateInitial = new Date();
        dateFinal = new Date();
        String dInitial = dateFormat.format(dateInitial);
        String dFinal = dateFormat.format(dateFinal);
        objCon = formCrop.getConnection();
        if (validateDate(dateInitial, dateFinal) == true) {
            capsule.setConfiguration(dInitial, dFinal, objCon, "soiltemp");
            Vector<Reading> readings = new Vector<Reading>();
            readings = capsule.getReading();
            if (readings.size() > 0) {
                for (int i = 0; i < readings.size(); i++) {
                    Reading l = readings.get(i);
                    try {
                        vDay.addElement(dateFormat.parse(l.getDateTime()));
                        vValue.addElement(Double.parseDouble(l.getValue()));
                        System.out.println(l.getValue());
                    } catch (Exception e) {
                        vValue.addElement(0d);
                    }
                }
                vData.addElement(vDay);
                vData.addElement(vValue);
            } else {
                System.out.println("No hay datos disponibles");
            }
        }
    }

    /**
     * 
     */
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == bWA) {
            wa.showForm();
        } else if (source == bSA) {
            sa.showForm();
        } else if (source == jce) {
            cts = Double.parseDouble(jce.getText()) * 10;
        } else if (source == bCrop) {
            formCrop.showForm();
        } else if (source == bFS) {
            fsn.showForm();
        } else if (source == bCalc) {
            if (jRSubs.isSelected() == true) {
                substrateResults();
            } else {
                soilResults();
            }
        } else if (source == bEv) {
            loadService();
        }
    }

    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) ((JTextField) e.getSource()).selectAll();
    }

    public void focusLost(FocusEvent e) {
    }
}
