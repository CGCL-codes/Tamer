package org.digitall.apps.taxes.interfases.taxesadmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.digitall.apps.taxes.classes.TipoImpuesto;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class TaxesAdminMgmt extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel();

    private BasicPanel northPanel = new BasicPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    private int[] sizeColumnList = { 200, 150, 180, 180, 180 };

    private Vector dataRow = new Vector();

    private GridPanel centerPanel = new GridPanel(50000, sizeColumnList, "Listado de Impuestos", dataRow) {

        public void finishLoading() {
            controlBotones();
        }
    };

    private Vector headerList = new Vector();

    private AssignButton btnSaveData = new AssignButton(true);

    private CloseButton btnClose = new CloseButton();

    private DeleteButton btnDel = new DeleteButton();

    private AddButton btnClearFields = new AddButton();

    private TFInput tfNombreImpuesto = new TFInput(DataTypes.STRING, "Impuesto", false);

    private TFInput tfBuscarCtaDebe = new TFInput(DataTypes.STRING, "Buscar Cta.", false);

    private TFInput tfBuscarCtaHaber = new TFInput(DataTypes.STRING, "Buscar Cta.", false);

    private TFInput tfBuscarCtaDeduccion = new TFInput(DataTypes.STRING, "Buscar Cta.", false);

    private TFInput tfBuscarCtaInteres = new TFInput(DataTypes.STRING, "Buscar Cta.", false);

    private CBInput cbCtaDebe = new CBInput(0, "Cuenta Debe", false);

    private CBInput cbCtahaber = new CBInput(0, "Cuenta Haber", false);

    private CBInput cbCtaDeduccion = new CBInput(0, "Cuenta Deducci�n", false);

    private CBInput cbCtaInteres = new CBInput(0, "Cuenta Inter�s", false);

    private TipoImpuesto tipoIpuesto;

    private int error = 0;

    private static int NOMBRE = 1;

    private static int CBDEBE = 2;

    private static int CBHABER = 3;

    private static int CBDEDUCCION = 4;

    private static int CBINTERES = 5;

    public TaxesAdminMgmt() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(850, 470));
        this.setPreferredSize(new Dimension(850, 500));
        this.setTitle("Administraci�n de Impuestos");
        centerPanel.setSize(new Dimension(840, 300));
        content.setLayout(borderLayout1);
        northPanel.setLayout(null);
        northPanel.setPreferredSize(new Dimension(840, 150));
        btnSaveData.setBounds(new Rectangle(810, 110, 30, 25));
        btnSaveData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSaveData_actionPerformed(e);
            }
        });
        btnSaveData.setToolTipText("Agregar impuesto");
        northPanel.add(btnClearFields, null);
        northPanel.add(cbCtahaber, null);
        northPanel.add(cbCtaInteres, null);
        northPanel.add(cbCtaDeduccion, null);
        northPanel.add(cbCtaDebe, null);
        northPanel.add(tfBuscarCtaInteres, null);
        northPanel.add(tfBuscarCtaHaber, null);
        northPanel.add(tfNombreImpuesto, null);
        northPanel.add(tfBuscarCtaDebe, null);
        northPanel.add(tfBuscarCtaDeduccion, null);
        northPanel.add(btnSaveData, null);
        content.add(northPanel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        add(content, null);
        addButton(btnClose);
        addButton(btnDel);
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnDel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnDel_actionPerformed(e);
            }
        });
        setHeaderList();
        tfNombreImpuesto.setBounds(new Rectangle(10, 20, 390, 35));
        tfBuscarCtaDebe.setBounds(new Rectangle(10, 60, 105, 35));
        cbCtaDebe.setBounds(new Rectangle(120, 60, 280, 35));
        tfBuscarCtaHaber.setBounds(new Rectangle(415, 60, 105, 35));
        cbCtahaber.setBounds(new Rectangle(525, 60, 280, 35));
        tfBuscarCtaDeduccion.setBounds(new Rectangle(10, 100, 105, 35));
        cbCtaDeduccion.setBounds(new Rectangle(120, 100, 280, 35));
        tfBuscarCtaInteres.setBounds(new Rectangle(415, 100, 105, 35));
        cbCtaInteres.setBounds(new Rectangle(525, 100, 280, 35));
        btnClearFields.setToolTipText("Agregar nuevo");
        btnClearFields.setBounds(new Rectangle(810, 80, 30, 25));
        btnClearFields.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClearFields_actionPerformed(e);
            }
        });
        tfBuscarCtaDebe.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                    cbCtaDebe.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,'" + tfBuscarCtaDebe.getString() + "'");
                }
            }
        });
        tfBuscarCtaHaber.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                    cbCtahaber.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,'" + tfBuscarCtaHaber.getString() + "'");
                }
            }
        });
        tfBuscarCtaDeduccion.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                    cbCtaDeduccion.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,'" + tfBuscarCtaDeduccion.getString() + "'");
                }
            }
        });
        tfBuscarCtaInteres.getTextField().addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                    cbCtaInteres.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,'" + tfBuscarCtaInteres.getString() + "'");
                }
            }
        });
        northPanel.setBorder(BorderPanel.getBorderPanel("Configurar Impuesto"));
        loadCombos();
        refresh();
    }

    private void loadCombos() {
        cbCtaDebe.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,''");
        cbCtahaber.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,''");
        cbCtaDeduccion.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,''");
        cbCtaInteres.loadJCombo("accounting.getAllAccountsForBookKeepingEntry", "-1,''");
    }

    private void setHeaderList() {
        headerList.removeAllElements();
        headerList.addElement("*");
        headerList.addElement("Nombre");
        headerList.addElement("*");
        headerList.addElement("Cuenta Debe");
        headerList.addElement("*");
        headerList.addElement("Cuenta Haber");
        headerList.addElement("*");
        headerList.addElement("Cuenta Deducci�n");
        headerList.addElement("*");
        headerList.addElement("Cuenta Inter�s");
        centerPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                    loadObjectSelected();
                } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
                }
            }
        });
        centerPanel.setParams("taxes.getAllTiposImpuestos", "", headerList);
    }

    private void loadObjectSelected() {
        if (!dataRow.isEmpty()) {
            tipoIpuesto = new TipoImpuesto();
            tipoIpuesto.setIdTipoImpuesto(Integer.parseInt("" + dataRow.elementAt(0)));
            tipoIpuesto.retrieveData();
            btnDel.setEnabled(true);
            loadForm();
        }
    }

    public void refresh() {
        centerPanel.refresh("");
        btnDel.setEnabled(false);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        getParentInternalFrame().setIcon(true);
    }

    private void btnDel_actionPerformed(ActionEvent e) {
        if (Advisor.question("Aviso", "�Est� seguro de borrar el impuesto \"" + tipoIpuesto.getNombre() + "\"?")) {
            if (tipoIpuesto.deleteData()) {
                clearFields();
                refresh();
            } else {
                Advisor.messageBox("Ocurri� un error, no se pudo borrar el Impuesto seleccionado", "Mensaje");
            }
        }
    }

    private void btnSaveData_actionPerformed(ActionEvent e) {
        if (control()) {
            loadTipoImpuesto();
            if (tipoIpuesto.saveData() > 0) {
                clearFields();
                refresh();
            } else {
                Advisor.messageBox("Ocurri� un error al grabar los datos", "Mensaje");
            }
        } else {
            if (error == NOMBRE) {
                Advisor.messageBox("El campo \"Impuesto\" no debe estar vac�o", "Mensaje");
            } else if (error == CBDEBE) {
                Advisor.messageBox("Debe seleccionar una cuenta para el Debe", "Mensaje");
            } else if (error == CBHABER) {
                Advisor.messageBox("Debe seleccionar una cuenta para el Haber", "Mensaje");
            } else if (error == CBDEDUCCION) {
                Advisor.messageBox("Debe seleccionar una cuenta para la Deducci�n", "Mensaje");
            } else if (error == CBINTERES) {
                Advisor.messageBox("Debe seleccionar una cuenta para el Inter�s", "Mensaje");
            }
        }
    }

    private boolean control() {
        boolean valor = true;
        if (tfNombreImpuesto.getString().equals("")) {
            valor = false;
            error = NOMBRE;
        } else if (cbCtaDebe == null) {
            valor = false;
            error = CBDEBE;
        } else if (cbCtahaber == null) {
            valor = false;
            error = CBHABER;
        } else if (cbCtaDeduccion == null) {
            valor = false;
            error = CBDEDUCCION;
        } else if (cbCtaInteres == null) {
            valor = false;
            error = CBINTERES;
        }
        return valor;
    }

    private void loadTipoImpuesto() {
        if (tipoIpuesto == null) {
            tipoIpuesto = new TipoImpuesto();
        }
        tipoIpuesto.setNombre(tfNombreImpuesto.getString());
        tipoIpuesto.setIdCuentaDebe(Integer.parseInt(cbCtaDebe.getSelectedValue().toString()));
        tipoIpuesto.setIdCuentaHaber(Integer.parseInt(cbCtahaber.getSelectedValue().toString()));
        tipoIpuesto.setIdCuentaDeduccion(Integer.parseInt(cbCtaDeduccion.getSelectedValue().toString()));
        tipoIpuesto.setIdCuentaInteres(Integer.parseInt(cbCtaInteres.getSelectedValue().toString()));
    }

    private void clearFields() {
        tfNombreImpuesto.setValue("");
        tfBuscarCtaDebe.setValue("");
        tfBuscarCtaHaber.setValue("");
        tfBuscarCtaDeduccion.setValue("");
        tfBuscarCtaInteres.setValue("");
        loadCombos();
        tipoIpuesto = new TipoImpuesto();
    }

    private void loadForm() {
        loadCombos();
        tfNombreImpuesto.setValue(tipoIpuesto.getNombre());
        cbCtaDebe.setSelectedID(tipoIpuesto.getIdCuentaDebe());
        cbCtahaber.setSelectedID(tipoIpuesto.getIdCuentaHaber());
        cbCtaDeduccion.setSelectedID(tipoIpuesto.getIdCuentaDeduccion());
        cbCtaInteres.setSelectedID(tipoIpuesto.getIdCuentaInteres());
    }

    private void btnClearFields_actionPerformed(ActionEvent e) {
        clearFields();
    }

    private void controlBotones() {
        clearFields();
    }
}
