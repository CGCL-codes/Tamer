package com.iver.cit.gvsig.gui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.gvsig.gui.beans.controls.dnd.JDnDList;
import org.gvsig.gui.beans.controls.dnd.JDnDListModel;
import org.gvsig.gui.beans.swing.JButton;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FMapWCSParameter;
import com.iver.cit.gvsig.fmap.layers.WCSLayer;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.wcs.WCSWizardData;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;

/**
 * This class implements the coverage option panel.
 *
 * It includes a set of Listeners that implement some control rules which
 * refresh the component values that depends on those selected in the other
 * components to avoid to choose an invalid set of options. It also includes a
 * method (isCorrectlyConfigured()) that checks if the current set of values is enough to
 * correctly launch a GetCoverage request.
 *
 * The information is obtained from a WCSWizardDataSource object.
 *
 *
 * Esta clase implementa el panel de opciones disponibles sobre la cobertura.
 *
 * Incluye una serie de Listeners que implementan unas reglas de control que
 * refrescan los valores de componentes cuyos valores dependen de aqu�llos
 * seleccionados en otros componentes para evitar que se escoja una combinaci�n
 * de opciones err�nea as� como una funci�n (isCorrectlyConfigured()) que
 * comprueba si la combinaci�n escogida actualmente es suficiente para lanzar
 * una operaci�n GetCoverage correctamente.
 *
 * La informaci�n obtiene a partir de un objeto WCSWizardDataSource.
 *
 * @author jaume - jaume dominguez faus
 *
 */
public class WCSParamsPanel extends WizardPanel {

    private final int SINGLE_VALUE = 0;

    private final int INTERVAL = 1;

    private JTabbedPane jTabbedPane = null;

    private JPanel formatsPanel = null;

    private JPanel timesPanel = null;

    private JList lstCRSs = null;

    private JList lstTimes = null;

    private JComboBox cmbParam = null;

    private JDnDList lstParamValues = null;

    private JButton btnDelTime = null;

    private JList lstSelectedTimes = null;

    private JCheckBox chkExtendedNames = null;

    private JList lstFormats = null;

    private JPanel parameterPanel = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JPanel coveragePanel = null;

    private JPanel jPanel4 = null;

    private JTextField txtName = null;

    private JScrollPane jScrollPane = null;

    private JButton btnAddTime = null;

    private LayerList lstCoverages = null;

    private JScrollPane jScrollPane2 = null;

    private JScrollPane jScrollPane3 = null;

    private JScrollPane jScrollPane4 = null;

    private JScrollPane jScrollPane5 = null;

    private JScrollPane jScrollPane1 = null;

    private WCSWizardData data;

    private WizardListenerSupport listenerSupport;

    private int parameterType;

    private int indCoverage;

    private int indFormat;

    private int indTime;

    private int indParameter;

    public static Preferences fPrefs = Preferences.userRoot().node("gvsig.wcs-wizard");

    private JComboBox cmbInterpolationMethods = null;

    private JCheckBox chkUseInterpolationMethod = null;

    private InfoPanel infoPanel;

    private int intInfo;

    private JPanel pnlName;

    /**
	 * This method initializes jTabbedPane
	 *
	 * @return javax.swing.JTabbedPane
	 */
    public WCSParamsPanel() {
        super();
        initialize();
    }

    /**
	 * Returns the tab amount that the WCSParamsPanel currently have
	 *
	 * Devuelve el n�mero de solapas que tiene actualmente el WCSParamsPanel
	 *
	 * @return int
	 */
    public int getNumTabs() {
        return getJTabbedPane().getTabCount();
    }

    private void initialize() {
        this.setLayout(null);
        this.setVisible(false);
        this.setBounds(0, 0, 510, 427);
        this.add(getJTabbedPane(), null);
    }

    /**
	 * Sets the focus to the tab next to the current one.
	 *
	 * Enfoca a la solapa siguiente a la actualmente enfocada del TabbedPane
	 *
	 */
    public void avanzaTab() {
        int currentPage = currentPage();
        int nPages = getNumTabs();
        if (nPages - 1 > currentPage) {
            getJTabbedPane().setSelectedIndex(nextPageEnabled());
        }
    }

    /**
	 * Sets the focus to the tab previous to the current one.
	 *
	 * Enfoca a la solapa anterior a la actualmente enfocada del TabbedPane
	 *
	 */
    public void retrocedeTab() {
        this.getJTabbedPane().setSelectedIndex(previousEnabledPage());
    }

    /**
	 * Returns the current tab index.
	 *
	 * Devuelve el n�mero de solapa en que se encuentra
	 *
	 * @return
	 */
    public int getIndiceSolapaActual() {
        return this.getJTabbedPane().getSelectedIndex();
    }

    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setBounds(4, 4, 502, 415);
            jTabbedPane.addTab(PluginServices.getText(this, "info"), null, getInfoPanel());
            intInfo = jTabbedPane.getTabCount() - 1;
            jTabbedPane.addTab(PluginServices.getText(this, "coverage"), null, getPanelCovertures(), null);
            indCoverage = jTabbedPane.getTabCount() - 1;
            jTabbedPane.addTab(PluginServices.getText(this, "format"), null, getFormatsPanel(), null);
            indFormat = jTabbedPane.getTabCount() - 1;
            jTabbedPane.addTab(PluginServices.getText(this, "time"), null, getTimePanel(), null);
            indTime = jTabbedPane.getTabCount() - 1;
            jTabbedPane.setEnabledAt(indTime, false);
            jTabbedPane.addTab(PluginServices.getText(this, "parameters"), null, getParameterPanel(), null);
            indParameter = jTabbedPane.getTabCount() - 1;
            jTabbedPane.setEnabledAt(indParameter, false);
        }
        return jTabbedPane;
    }

    /**
	 * This method initializes pnlName
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getPnlName() {
        if (pnlName == null) {
            pnlName = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pnlName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "nombre_cobertura"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            pnlName.setBounds(new java.awt.Rectangle(7, 4, 482, 52));
            pnlName.add(getTxtName(), null);
        }
        return pnlName;
    }

    private InfoPanel getInfoPanel() {
        if (infoPanel == null) {
            infoPanel = new InfoPanel();
            infoPanel.addFocusListener(new FocusListener() {

                public void focusGained(java.awt.event.FocusEvent e) {
                    refreshInfo();
                }

                public void focusLost(FocusEvent e) {
                }
            });
        }
        return infoPanel;
    }

    /**
	 * This method initializes panelFormato
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getFormatsPanel() {
        if (formatsPanel == null) {
            formatsPanel = new JPanel();
            formatsPanel.setLayout(null);
            formatsPanel.add(getJPanel3(), null);
            formatsPanel.add(getJPanel2(), null);
        }
        return formatsPanel;
    }

    /**
	 * This method initializes panelTemps
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getTimePanel() {
        if (timesPanel == null) {
            timesPanel = new JPanel();
            timesPanel.setLayout(null);
            timesPanel.setBounds(5, 17, 464, 374);
            timesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_tiempo"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            timesPanel.add(getBtnAddTime(), null);
            timesPanel.add(getBtnDelTime(), null);
            timesPanel.add(getJScrollPane3(), null);
            timesPanel.add(getJScrollPane4(), null);
            timesPanel.setLayout(null);
            timesPanel.setBounds(5, 17, 464, 374);
            timesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_tiempo"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            timesPanel.add(getBtnAddTime(), null);
            timesPanel.add(getBtnDelTime(), null);
            timesPanel.add(getJScrollPane3(), null);
            timesPanel.add(getJScrollPane4(), null);
        }
        return timesPanel;
    }

    /**
	 * This method initializes lstCRSs
	 *
	 * @return javax.swing.JList
	 */
    public JList getLstCRSs() {
        if (lstCRSs == null) {
            lstCRSs = new JList();
            lstCRSs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            lstCRSs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    listenerSupport.callStateChanged(isCorretlyConfigured());
                }
            });
        }
        return lstCRSs;
    }

    /**
	 * This method initializes lstTemps
	 *
	 * @return javax.swing.JList
	 */
    public JList getLstTimes() {
        if (lstTimes == null) {
            lstTimes = new JList();
        }
        return lstTimes;
    }

    /**
	 * This method initializes cmbParam
	 *
	 * @return javax.swing.JComboBox
	 */
    public JComboBox getCmbParam() {
        if (cmbParam == null) {
            cmbParam = new JComboBox();
            cmbParam.setEditable(false);
            cmbParam.setBounds(10, 26, 229, 24);
            cmbParam.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    FMapWCSParameter p = (FMapWCSParameter) cmbParam.getSelectedItem();
                    if (p != null) {
                        getSingleParamValuesList().setListData(p.getValueList().toArray());
                    }
                    fireWizardComplete(isCorretlyConfigured());
                }
            });
        }
        return cmbParam;
    }

    /**
	 * This method initializes lstParamValues
	 *
	 * @return javax.swing.JList
	 */
    public JDnDList getSingleParamValuesList() {
        if (lstParamValues == null) {
            lstParamValues = new JDnDList();
            lstParamValues.setModel(new JDnDListModel());
            lstParamValues.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            lstParamValues.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    fireWizardComplete(isCorretlyConfigured());
                }
            });
        }
        return lstParamValues;
    }

    /**
	 * Fires a notification to this wizard listeners telling them if the
	 * configuration is fair enough to send a GetCoverage request.
	 * @param b
	 */
    private void fireWizardComplete(boolean b) {
        listenerSupport.callStateChanged(b);
        callStateChanged(b);
    }

    /**
	 * Refreshes the info shown in the info tab.
	 */
    public void refreshInfo() {
        FMapWCSParameter p = (FMapWCSParameter) getCmbParam().getSelectedItem();
        String pString = (p != null && getParameterString() != null) ? p.toString() + "=" + getParameterString().split("=")[1] : null;
        infoPanel.refresh(data, (WCSLayer) getLstCoverages().getSelectedValue(), getTime(), getFormat(), getSRS(), pString);
    }

    /**
	 * This method initializes btnDelTemps
	 *
	 * @return org.gvsig.gui.beans.swing.JButton
	 */
    public JButton getBtnDelTime() {
        if (btnDelTime == null) {
            btnDelTime = new JButton();
            btnDelTime.setBounds(200, 127, 50, 20);
            btnDelTime.setText("<");
            btnDelTime.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    delTime();
                }
            });
        }
        return btnDelTime;
    }

    /**
	 * This method initializes lstSelectedTimes
	 *
	 * @return javax.swing.JList
	 */
    public JList getLstSelectedTimes() {
        if (lstSelectedTimes == null) {
            lstSelectedTimes = new JList();
            lstSelectedTimes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    listenerSupport.callStateChanged(isCorretlyConfigured());
                }
            });
        }
        return lstSelectedTimes;
    }

    /**
	 * This method initializes lstFormats
	 *
	 * @return javax.swing.JList
	 */
    public JList getLstFormats() {
        if (lstFormats == null) {
            lstFormats = new JList();
            lstFormats.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            lstFormats.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    listenerSupport.callStateChanged(isCorretlyConfigured());
                }
            });
        }
        return lstFormats;
    }

    /**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getParameterPanel() {
        if (parameterPanel == null) {
            parameterPanel = new JPanel();
            parameterPanel.setLayout(null);
            parameterPanel.setBounds(5, 17, 464, 374);
            parameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_parametros"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            parameterPanel.add(getCmbParam(), null);
            parameterPanel.add(getJScrollPane5(), null);
        }
        return parameterPanel;
    }

    /**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(null);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_CRS"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.setBounds(0, 210, 499, 181);
            jPanel2.add(getJScrollPane2(), null);
        }
        return jPanel2;
    }

    /**
	 * This method initializes jPanel3
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            jPanel3 = new JPanel();
            jPanel3.setLayout(null);
            jPanel3.setBounds(0, 0, 499, 200);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_formato"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel3.add(getJScrollPane1(), null);
            jPanel3.add(getCmbInterpolationMethods(), null);
            jPanel3.add(getChkUseInterpolationMethod(), null);
        }
        return jPanel3;
    }

    /**
	 * This method initializes panelCovertures
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelCovertures() {
        if (coveragePanel == null) {
            coveragePanel = new JPanel();
            coveragePanel.setLayout(null);
            coveragePanel.add(getJPanel4(), null);
            coveragePanel.add(getPnlName(), null);
            coveragePanel.add(getChkExtendedNames(), null);
        }
        return coveragePanel;
    }

    /**
	 * This method initializes jPanel4
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            jPanel4 = new JPanel();
            jPanel4.setLayout(null);
            jPanel4.setBounds(7, 56, 482, 305);
            jPanel4.add(getJScrollPane(), null);
            jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "seleccionar_coberturas"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        }
        return jPanel4;
    }

    /**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
    public JTextField getTxtName() {
        if (txtName == null) {
            txtName = new JTextField();
            txtName.setPreferredSize(new Dimension(469, 23));
        }
        return txtName;
    }

    /**
	 * This method initializes chkExtendedNames
	 *
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getChkExtendedNames() {
        if (chkExtendedNames == null) {
            chkExtendedNames = new JCheckBox();
            chkExtendedNames.setText(PluginServices.getText(this, "show_layer_names"));
            chkExtendedNames.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    boolean b = chkExtendedNames.isSelected();
                    getLstCoverages().showLayerNames = b;
                    getLstCoverages().repaint();
                }
            });
            chkExtendedNames.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fPrefs.putBoolean("show_layer_names", chkExtendedNames.isSelected());
                }
            });
            chkExtendedNames.setBounds(10, 368, 438, 20);
            chkExtendedNames.setSelected(fPrefs.getBoolean("show_layer_names", false));
        }
        return chkExtendedNames;
    }

    /**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(7, 17, 467, 282);
            jScrollPane.setViewportView(getLstCoverages());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jButton2
	 *
	 * @return org.gvsig.gui.beans.swing.JButton
	 */
    public JButton getBtnAddTime() {
        if (btnAddTime == null) {
            btnAddTime = new JButton();
            btnAddTime.setBounds(200, 151, 50, 20);
            btnAddTime.setText(">");
            btnAddTime.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addTime();
                }
            });
        }
        return btnAddTime;
    }

    /**
	 * This method initializes jList1
	 *
	 * @return javax.swing.JList
	 */
    public LayerList getLstCoverages() {
        if (lstCoverages == null) {
            lstCoverages = new LayerList();
            lstCoverages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            lstCoverages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    refreshData();
                }
            });
        }
        return lstCoverages;
    }

    /**
	 * This method initializes jScrollPane2
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setBounds(6, 19, 483, 155);
            jScrollPane2.setViewportView(getLstCRSs());
        }
        return jScrollPane2;
    }

    /**
	 * This method initializes jScrollPane3
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane3() {
        if (jScrollPane3 == null) {
            jScrollPane3 = new JScrollPane();
            jScrollPane3.setBounds(10, 30, 178, 334);
            jScrollPane3.setViewportView(getLstTimes());
        }
        return jScrollPane3;
    }

    /**
	 * This method initializes jScrollPane4
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane4() {
        if (jScrollPane4 == null) {
            jScrollPane4 = new JScrollPane();
            jScrollPane4.setBounds(304, 30, 178, 334);
            jScrollPane4.setViewportView(getLstSelectedTimes());
        }
        return jScrollPane4;
    }

    /**
	 * This method initializes jScrollPane5
	 *
	 * @return javax.swing.JScrollPane
	 */
    public JScrollPane getJScrollPane5() {
        if (jScrollPane5 == null) {
            jScrollPane5 = new JScrollPane();
            jScrollPane5.setBounds(254, 26, 236, 357);
            jScrollPane5.setViewportView(getSingleParamValuesList());
        }
        return jScrollPane5;
    }

    /**
	 * This method initializes jScrollPane1
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(6, 19, 257, 174);
            jScrollPane1.setViewportView(getLstFormats());
        }
        return jScrollPane1;
    }

    /**
	 * Verifies that the selected parameters are enough to request the coverage
	 * to the server.
	 *
	 * Comprueba que los par�metros seleccionados son suficientes para pedir la
	 * cobertura al servidor.
	 *
	 * @return boolean
	 */
    public boolean isCorretlyConfigured() {
        if (getCurrentCoverageName() == null) return false;
        if (timeRequired() && getTime() == null) return false;
        if (getCmbParam().getSelectedItem() != null) {
            if (parameterType == SINGLE_VALUE && getParameterString() == null) return false;
            if (parameterType == INTERVAL) {
            }
        }
        if (getSRS() == null) return false;
        if (getFormat() == null) return false;
        return true;
    }

    /**
	 * Extracts the parameter query string.
	 * @return
	 */
    public String getParameterString() {
        FMapWCSParameter p = (FMapWCSParameter) getCmbParam().getSelectedItem();
        if (p == null) return null;
        if (p.getType() == FMapWCSParameter.VALUE_LIST) {
            Object[] v = getSingleParamValuesList().getSelectedValues();
            if (p != null && v.length > 0) {
                String s = p.getName() + "=" + (String) v[0];
                for (int i = 1; i < v.length; i++) {
                    s += "," + v[i];
                }
                return s;
            }
        }
        return null;
    }

    /**
	 * True if the TIME parameter is required, else false.
	 *
	 * True si se requiere especificar una posici�n para el tiempo.
	 * (jo crec que a�� no val per a res, per� per si de cas la tinc)
	 *
	 * @return
	 */
    private boolean timeRequired() {
        return getLstTimes().getModel().getSize() > 0;
    }

    /**
	 * Returns the time positions or null if none.
	 * @return String
	 */
    public String getTime() {
        String[] ss = new String[getLstSelectedTimes().getModel().getSize()];
        for (int i = 0; i < ss.length; i++) {
            ss[i] = (String) getLstSelectedTimes().getModel().getElementAt(i);
        }
        String s = null;
        if (ss.length > 0) {
            s = ss[0];
            for (int i = 1; i < ss.length; i++) {
                s += "," + ss[i];
            }
        }
        System.out.println(s);
        return s;
    }

    /**
	 * Returns the selected CRS.
	 *
	 * Devuelve el CRS seleccionado.
	 *
	 * @return String
	 */
    public String getSRS() {
        return (String) getLstCRSs().getSelectedValue();
    }

    /**
	 * Returns the selected format.
	 *
	 * Devuelve el formato seleccionado.
	 *
	 * @return String
	 */
    public String getFormat() {
        return (String) getLstFormats().getSelectedValue();
    }

    /**
	 * Returns the name of the selected coverage.
	 *
	 * Devuelve el nombre de la cobertura seleccionada.
	 *
	 * @return String
	 */
    public String getLayerName() {
        return getTxtName().getText();
    }

    /**
	 * Cleans up the wizard's components but the server's layers list.
	 *
	 * Limpia todos los componentes del wizard excepto la lista de capas del
	 * servidor.
	 */
    public void cleanupWizard() {
        Object[] nada = new Object[0];
        getLstCRSs().clearSelection();
        getLstCRSs().setListData(nada);
        getLstFormats().clearSelection();
        getLstFormats().setListData(nada);
        getLstTimes().clearSelection();
        getLstTimes().setListData(nada);
        getLstSelectedTimes().clearSelection();
        getLstSelectedTimes().setListData(nada);
        getCmbParam().removeAllItems();
        getSingleParamValuesList().setVisible(true);
        getSingleParamValuesList().clearSelection();
        getSingleParamValuesList().setListData(nada);
    }

    /**
	 * Returns the selected coverage name at the server. This is the value to use
	 * in a GetCoverage request
	 *
	 * @return String
	 */
    public String getCurrentCoverageName() {
        if (getLstCoverages().getSelectedValue() == null) return null;
        return ((WCSLayer) getLstCoverages().getSelectedValue()).getName();
    }

    /**
	 * Refreshes the wizard components data each time a coverage is selected.
	 *
	 * Actualiza los datos de los componentes del wizard cada vez que se selecciona
	 * una cobertura diferente.
	 */
    public void refreshData() {
        String coverageName = getCurrentCoverageName();
        cleanupWizard();
        if (coverageName != null) {
            WCSLayer lyr = data.getLayer(coverageName);
            getTxtName().setText(lyr.getTitle());
            getLstCRSs().clearSelection();
            getLstCRSs().setListData(lyr.getSRSs().toArray());
            getLstFormats().clearSelection();
            getLstFormats().setListData(lyr.getFormats().toArray());
            boolean b = lyr.getInterpolationMethods() != null;
            cmbInterpolationMethods.removeAllItems();
            if (b) {
                ArrayList im = lyr.getInterpolationMethods();
                for (int i = 0; i < im.size(); i++) cmbInterpolationMethods.addItem(im.get(i));
            }
            getChkUseInterpolationMethod().setEnabled(b);
            getCmbInterpolationMethods().setEnabled(b);
            getLstTimes().removeAll();
            ArrayList list = lyr.getTimePositions();
            b = !(list == null || list.isEmpty());
            if (b) getLstTimes().setListData(list.toArray());
            jTabbedPane.setEnabledAt(indTime, b);
            cmbParam.removeAllItems();
            list = lyr.getParameterList();
            b = !(list == null || list.isEmpty());
            if (b) {
                for (int i = 0; i < list.size(); i++) cmbParam.addItem(list.get(i));
                getSingleParamValuesList().setListData(((FMapWCSParameter) cmbParam.getSelectedItem()).getValueList().toArray());
            }
            jTabbedPane.setEnabledAt(indParameter, b);
            fireWizardComplete(isCorretlyConfigured());
        }
    }

    /**
	 * Sets the object that holds the wizard data.
	 *
	 * Establece el objeto que contiene los datos del wizard.
	 *
	 * @param data
	 */
    public void setWizardData(WCSWizardData data) {
        this.data = data;
        getLstCoverages().setListData(data.getCoverageList());
        refreshInfo();
    }

    /**
	 * Adds the selected items from the time list to the selected times list.
	 *
	 * A�ade los items seleccionados de la lista de tiempos a la
	 * lista de tiempos seleccionados.
	 */
    public void addTime() {
        ArrayList times = new ArrayList();
        for (int i = 0; i < getLstSelectedTimes().getModel().getSize(); i++) {
            times.add(getLstSelectedTimes().getModel().getElementAt(i));
        }
        Object[] obj = getLstTimes().getSelectedValues();
        for (int i = 0; i < obj.length; i++) {
            if (!times.contains(obj[i])) times.add(obj[i]);
        }
        getLstSelectedTimes().setListData(times.toArray());
        fireWizardComplete(isCorretlyConfigured());
    }

    /**
	 * Removes the selected items from the selected times list.
	 *
	 * Quita posiciones de tiempo de la lista de tiempos seleccionados.
	 */
    public void delTime() {
        ArrayList tiempos = new ArrayList();
        Object[] obj = getLstSelectedTimes().getSelectedValues();
        for (int i = 0; i < getLstSelectedTimes().getModel().getSize(); i++) tiempos.add(getLstSelectedTimes().getModel().getElementAt(i));
        for (int j = 0; j < obj.length; j++) if (tiempos.contains(obj[j])) tiempos.remove(obj[j]);
        obj = new Object[tiempos.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = tiempos.get(i);
        }
        getLstSelectedTimes().setListData(tiempos.toArray());
        fireWizardComplete(isCorretlyConfigured());
    }

    /**
	 * The coverage name (will be used at the TOC)
	 *
	 * La etiqueta de la cobertura
	 * @return String
	 */
    public String getCoverageName() {
        return getTxtName().getText();
    }

    /**
	 * Returns the extent of the currently selected coverage for the currently
	 * selected SRS.
	 *
	 * Devuelve el Extent de la cobertura actualmente seleccionada para el SRS
	 * actualmente seleccionado
	 *
	 * @return Rectangle2D
	 */
    public Rectangle2D getExtent() {
        String cName = getCurrentCoverageName();
        if (cName != null) {
            return data.getLayer(cName).getExtent(getSRS());
        }
        return null;
    }

    /**
	 * Sets the wizard's data source
	 *
	 * Establece el origen de los datos del wizard
	 * @param dataSource
	 */
    public void setDataSource(WCSWizardData dataSource) {
        this.data = dataSource;
    }

    /**
	 * Returns the index of the coverage within the coverages list
	 *
	 * Devuelve la posici�n que ocupa la cobertura en la lista de coberturas
	 *
	 * @param coverageName
	 * @return The coverage's index if it exists, -1 if it not exists.
	 */
    public int getCoverageIndex(String coverageName) {
        for (int i = 0; i < getLstCoverages().getModel().getSize(); i++) {
            if (coverageName.equals(((WCSLayer) getLstCoverages().getModel().getElementAt(i)).getName())) return i;
        }
        return -1;
    }

    /**
	 * Returns the index of the CRS within the CRS list.
	 *
	 * Devuelve la posicion que ocupa el CRS en la lista de CRS
	 *
	 * @param crs
	 * @return The CRS's index if it exists, -1 if it not exists.
	 */
    public int getSRSIndex(String crs) {
        for (int i = 0; i < getLstCRSs().getModel().getSize(); i++) {
            if (crs.equals(getLstCRSs().getModel().getElementAt(i))) return i;
        }
        return -1;
    }

    /**
	 * Returns the index of the format within the formats list.
	 *
	 * Devuelve la posicion que ocupa el formato en la lista de formatos
	 * @param format
	 *
	 * @return The format's index if it exists, -1 if it not exists.
	 */
    public int getFormatIndex(String format) {
        for (int i = 0; i < getLstFormats().getModel().getSize(); i++) {
            if (format.equals(getLstFormats().getModel().getElementAt(i))) return i;
        }
        return -1;
    }

    /**
	 * Returns the parameter's name's index within the cmbBox.
	 *
	 * Devuelve la posicion que ocupa el nombre dle par�metro en el comboBox
	 * de par�metros
	 *
	 * @param nParam
	 * @return The index if it exists, -1 if it not exists.
	 */
    public int getParamIndex(String nParam) {
        for (int i = 0; i < getCmbParam().getModel().getSize(); i++) {
            if (nParam.equals(((FMapWCSParameter) getCmbParam().getItemAt(i)).getName())) return i;
        }
        return -1;
    }

    /**
	 * Returns the position for the parameter's value within the parameter
	 * values list.
	 *
	 * Devuelve la posicion que ocupa el valor para parametro
	 * en la lista de valores para par�metro
	 *
	 * @param valor
	 * @return The index if it exists, -1 if it not exists.
	 */
    public int getValueIndex(String valor) {
        for (int i = 0; i < getSingleParamValuesList().getModel().getSize(); i++) {
            if (valor.equals(getSingleParamValuesList().getModel().getElementAt(i))) return i;
        }
        return -1;
    }

    /**
	 * Returns the max supported resolution for the coverage.
	 *
	 * Recupera la resoluci�n m�xima soportada por la cobertura.
	 * @return double
	 */
    public Point2D getMaxRes() {
        return data.getLayer(getCurrentCoverageName()).getMaxRes();
    }

    /**
     * Returns the next enabled tab's index.
     *
     * Devuelve el �ndicie de la siguiente pesta�a habilitada del wizard o -1 si no hay
     * ninguna.
     *
     * @return The index or -1 if there is no one.
     */
    public int nextPageEnabled() {
        int currentPage = currentPage();
        int nPages = getNumTabs();
        if (currentPage == nPages) return -1;
        for (int i = currentPage + 1; i < nPages; i++) {
            if (getJTabbedPane().isEnabledAt(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the current tab.
     *
     * Devuelve el �ndice de la p�gina actual del wizard.
     *
     * @return
     */
    public int currentPage() {
        return getJTabbedPane().getSelectedIndex();
    }

    /**
     * Returns the index of the previous enabled tab.
     *
     * Devuelve el �ndice de la anterior p�gina habilitada del wizard o -1 si no hay
     * ninguna.
     *
     * @return The index, or -1 if there is no one.
     */
    public int previousEnabledPage() {
        int currentPage = currentPage();
        int j = 0;
        if (currentPage == 0) j = -1;
        for (int i = currentPage - 1; i > -1; i--) {
            if (getJTabbedPane().isEnabledAt(i)) {
                j = i;
                break;
            }
        }
        return j;
    }

    public FMapWCSDriver getDriver() {
        return data.getDriver();
    }

    public void initWizard() {
    }

    public void execute() {
    }

    public FLayer getLayer() {
        return null;
    }

    public void setListenerSupport(WizardListenerSupport support) {
        listenerSupport = support;
    }

    /**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getCmbInterpolationMethods() {
        if (cmbInterpolationMethods == null) {
            cmbInterpolationMethods = new JComboBox();
            cmbInterpolationMethods.setBounds(new java.awt.Rectangle(273, 45, 215, 20));
            cmbInterpolationMethods.setEnabled(false);
            cmbInterpolationMethods.setEditable(false);
        }
        return cmbInterpolationMethods;
    }

    /**
	 * This method initializes jCheckBox
	 *
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getChkUseInterpolationMethod() {
        if (chkUseInterpolationMethod == null) {
            chkUseInterpolationMethod = new JCheckBox();
            chkUseInterpolationMethod.setText(PluginServices.getText(this, "use_interpolation_method"));
            chkUseInterpolationMethod.setSelected(false);
            chkUseInterpolationMethod.setEnabled(false);
            chkUseInterpolationMethod.setBounds(new java.awt.Rectangle(273, 19, 210, 20));
            chkUseInterpolationMethod.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    cmbInterpolationMethods.setEnabled(chkUseInterpolationMethod.isSelected());
                }
            });
        }
        return chkUseInterpolationMethod;
    }
}
