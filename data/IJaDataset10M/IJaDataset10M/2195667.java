package org.digitall.apps.taxes.interfases.clearfees;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.SwingConstants;
import org.digitall.apps.taxes.classes.Rent;
import org.digitall.apps.taxes.interfases.rentsadmin.RentsMgmt;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicTitleLabel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.buttons.RefreshGridButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class ClearRentTaxes extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel();

    private BasicPanel findPanel = new BasicPanel("Buscar");

    private BasicPanel impuestosPanel = new BasicPanel();

    private BorderLayout borderLayout4 = new BorderLayout();

    private TFInput tfEmpresa = new TFInput(DataTypes.STRING, "Enterprise", false);

    private TFInput tfResponsable = new TFInput(DataTypes.STRING, "PersonCharge", false);

    private TFInput tfPredio = new TFInput(DataTypes.STRING, "Predio", false);

    private int[] propertiesColumnSize = { 202, 160, 190, 80 };

    private Vector dataRow = new Vector();

    private GridPanel propertiesPanel = new GridPanel(50000, propertiesColumnSize, "Alquileres", dataRow);

    private Vector propertiesHeader = new Vector();

    private FindButton btnSearch = new FindButton();

    private DeleteButton btnDeleteRentFees = new DeleteButton();

    private RefreshGridButton btnRecalcRentFees = new RefreshGridButton();

    private PrintButton btnPrintRentFees = new PrintButton();

    private AddButton btnAddRent = new AddButton();

    private ModifyButton btnModifyRent = new ModifyButton();

    private CBInput cbRentMonth = new CBInput(0, "Advance", false);

    private CBInput cbRentYear = new CBInput(0, "FileYear", false);

    private int idalquiler = -1;

    private BasicPanel rentPanel = new BasicPanel();

    private GridLayout gridLayout1 = new GridLayout();

    private BasicTitleLabel lblRentTitle = new BasicTitleLabel();

    private Rent rent;

    private RentsMgmt rentsMgmt;

    public ClearRentTaxes() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(700, 479));
        tfResponsable.setBounds(new Rectangle(320, 20, 110, 35));
        tfPredio.setBounds(new Rectangle(475, 20, 125, 35));
        tfEmpresa.setBounds(new Rectangle(50, 20, 220, 35));
        btnSearch.setBounds(new Rectangle(645, 25, 30, 30));
        btnSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSearch_actionPerformed(e);
            }
        });
        impuestosPanel.setLayout(gridLayout1);
        cbRentMonth.setBounds(new Rectangle(40, 30, 85, 35));
        cbRentMonth.setOpaque(false);
        cbRentYear.setBounds(new Rectangle(140, 30, 100, 35));
        cbRentYear.setOpaque(false);
        rentPanel.setLayout(null);
        rentPanel.setBackground(new Color(152, 132, 112));
        lblRentTitle.setText("IMPUESTO ALQUILERES");
        lblRentTitle.setBounds(new Rectangle(0, 5, 700, 25));
        lblRentTitle.setFont(new Font("Lucida Sans", 1, 15));
        lblRentTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblRentTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        impuestosPanel.setBounds(new Rectangle(5, 300, 405, 200));
        impuestosPanel.setPreferredSize(new Dimension(405, 80));
        rentPanel.add(lblRentTitle, null);
        rentPanel.add(cbRentYear, null);
        rentPanel.add(cbRentMonth, null);
        rentPanel.add(btnDeleteRentFees, null);
        rentPanel.add(btnRecalcRentFees, null);
        cbRentMonth.autoSize();
        cbRentYear.autoSize();
        findPanel.setBounds(new Rectangle(5, 5, 685, 45));
        findPanel.setLayout(null);
        findPanel.setPreferredSize(new Dimension(1, 65));
        propertiesPanel.setBounds(new Rectangle(5, 55, 685, 155));
        propertiesPanel.setPreferredSize(new Dimension(400, 400));
        btnRecalcRentFees.setBounds(new Rectangle(430, 45, 235, 25));
        btnRecalcRentFees.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnRecalcCommerceFees_actionPerformed(e);
            }
        });
        btnDeleteRentFees.setBounds(new Rectangle(285, 45, 90, 25));
        btnDeleteRentFees.setHorizontalAlignment(SwingConstants.LEFT);
        btnDeleteRentFees.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDeleteRentFees.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnDeleteCommerceFees_actionPerformed(e);
            }
        });
        content.setLayout(borderLayout4);
        findPanel.add(tfEmpresa, null);
        findPanel.add(btnSearch, null);
        findPanel.add(tfPredio, null);
        findPanel.add(tfResponsable, null);
        this.add(content, null);
        content.add(findPanel, BorderLayout.NORTH);
        content.add(propertiesPanel, BorderLayout.CENTER);
        impuestosPanel.add(rentPanel, null);
        content.add(impuestosPanel, BorderLayout.SOUTH);
        tfPredio.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if ((caracter == KeyEvent.VK_ENTER)) {
                    refresh();
                }
            }
        });
        tfEmpresa.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if ((caracter == KeyEvent.VK_ENTER)) {
                    refresh();
                }
            }
        });
        tfResponsable.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if ((caracter == KeyEvent.VK_ENTER)) {
                    refresh();
                }
            }
        });
        this.addButton(btnPrintRentFees);
        this.addButton(btnModifyRent);
        this.addButton(btnAddRent);
        btnPrintRentFees.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPrintRentFees_actionPerformed(e);
            }
        });
        btnAddRent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAddRent_actionPerformed(e);
            }
        });
        btnModifyRent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnModifyRent_actionPerformed(e);
            }
        });
        setPropertiesHeader();
        loadCombos();
        setEnabledCombosAndButtons(false);
        btnRecalcRentFees.setToolTipText("<html><center>Recalcular las Mensualidades de Alquileres<br>para el predio seleccionado</center></html>");
        btnRecalcRentFees.setHorizontalAlignment(SwingConstants.LEFT);
        btnRecalcRentFees.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnPrintRentFees.setToolTipText("<html><center>Imprimir los anticipos de los Alquileres<br>para la Empresa seleccionada</center></html>");
        btnDeleteRentFees.setText("Borrar anticipos");
        btnRecalcRentFees.setText("Restaurar anticipos");
        btnRecalcRentFees.setToolTipText("<html><center>Restaurar los anticipos del Impuesto a los Alquileres<br>para la Empresa seleccionada</center></html>");
        btnAddRent.setToolTipText("<html><center>Agregar nueva Empresa</center></html>");
        btnModifyRent.setToolTipText("<html><center>Modificar la Empresa seleccionada</center></html>");
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
    }

    private void loadCombos() {
        int actualYear = Integer.parseInt("0" + Environment.currentYear);
        int cont = 0;
        for (int i = 2000; i <= (actualYear); i++) {
            cont++;
            cbRentYear.getCombo().addItem(i, cont);
        }
        cbRentYear.setSelectedID(cont);
        cont = 1;
        while (cont <= 12) {
            cbRentMonth.getCombo().addItem(cont, cont);
            cont = cont + 1;
        }
    }

    private void setEnabledCombosAndButtons(boolean _valor) {
        cbRentMonth.setEnabled(_valor);
        cbRentYear.setEnabled(_valor);
        btnDeleteRentFees.setEnabled(_valor);
        btnRecalcRentFees.setEnabled(_valor);
        btnPrintRentFees.setEnabled(_valor);
        btnModifyRent.setEnabled(_valor);
    }

    private void setPropertiesHeader() {
        propertiesHeader.removeAllElements();
        propertiesHeader.addElement("*");
        propertiesHeader.addElement(Environment.lang.getProperty("Enterprise"));
        propertiesHeader.addElement(Environment.lang.getProperty("PersonCharge"));
        propertiesHeader.addElement(Environment.lang.getProperty("Predio"));
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("*");
        propertiesHeader.addElement("Ult. Anticipo");
        propertiesPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                loadObjectSelected();
                if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
                } else if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                    setEnabledCombosAndButtons(true);
                }
            }
        });
        String params = "'" + tfEmpresa.getString() + "','" + tfResponsable.getString() + "','" + tfPredio.getString() + "'";
        propertiesPanel.setParams("taxes.getAllRents", params, propertiesHeader);
    }

    public void refresh() {
        String params = "'" + tfEmpresa.getString() + "','" + tfResponsable.getString() + "','" + tfPredio.getString() + "'";
        propertiesPanel.refresh(params);
        setEnabledCombosAndButtons(false);
    }

    private void loadObjectSelected() {
        if (!dataRow.isEmpty()) {
            idalquiler = Integer.parseInt("" + dataRow.elementAt(10));
            rent = new Rent();
            rent.setIdempresa(Integer.parseInt("" + dataRow.elementAt(0)));
            rent.setEmpresa(dataRow.elementAt(1).toString());
            rent.setResponsable(dataRow.elementAt(2).toString());
            rent.setPredio(dataRow.elementAt(3).toString());
            rent.setContratocomodato(dataRow.elementAt(4).toString());
            if (!dataRow.elementAt(5).equals("null")) {
                rent.setVigencia(Proced.setFormatDate("" + dataRow.elementAt(5), false));
            }
            rent.setDuracion(dataRow.elementAt(6).toString());
            rent.setImportemensual(Double.parseDouble(dataRow.elementAt(7).toString()));
            rent.setObservacion(dataRow.elementAt(8).toString());
        }
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        refresh();
    }

    private void btnDeleteCommerceFees_actionPerformed(ActionEvent e) {
        if (Advisor.question("Aviso!", "�Est� seguro de borrar las cuotas?")) {
            String params = idalquiler + "," + cbRentMonth.getSelectedItem() + "," + cbRentYear.getSelectedItem();
            if (LibSQL.getInt("taxes.deleteRentsFees", params) > 0) {
                refresh();
            }
        }
    }

    private void btnRecalcCommerceFees_actionPerformed(ActionEvent e) {
        if (Advisor.question("Aviso!", "�Est� seguro de regenerar las cuotas\npara el automotor seleccionado?")) {
            if (LibSQL.getInt("taxes.generarCuotaAlquiler", idalquiler) == 0) {
                refresh();
            }
        }
    }

    private void btnPrintRentFees_actionPerformed(ActionEvent e) {
        if (!dataRow.isEmpty()) {
            int idempresa = Integer.parseInt(dataRow.elementAt(0).toString());
            BasicReport report = new BasicReport(ClearRentTaxes.class.getResource("xml/RentFeesReport.xml"));
            report.addTableModel("taxes.xmlGetRent", idempresa);
            report.addTableModel("taxes.xmlGetRentFees", idalquiler);
            report.doReport();
        } else {
            Advisor.messageBox("Debe seleccionar una Empresa", "Error");
        }
    }

    private void loadMgmt(boolean _addAction) {
        if (_addAction) {
            rent = new Rent();
        }
        rentsMgmt = new RentsMgmt();
        rentsMgmt.setRent(rent);
        rentsMgmt.setParentList(this);
        ExtendedInternalFrame rentsMgmtContainer = new ExtendedInternalFrame("Agregar/Modificar");
        rentsMgmtContainer.setCentralPanel(rentsMgmt);
        rentsMgmtContainer.show();
    }

    private void btnAddRent_actionPerformed(ActionEvent e) {
        loadMgmt(true);
    }

    private void btnModifyRent_actionPerformed(ActionEvent e) {
        loadMgmt(false);
    }
}
