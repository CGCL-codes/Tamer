package org.digitall.apps.taxes.interfases.carsadmin;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Types;
import java.util.Vector;
import org.digitall.apps.taxes.classes.Cadastral;
import org.digitall.apps.taxes.classes.Car;
import org.digitall.apps.taxes.interfases.TaxesCar;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;
import org.digitall.lib.sql.LibSQL;

public class UpdateCarFeesMgmt extends BasicPrimitivePanel {

    private BasicPanel contentPanel = new BasicPanel();

    private BasicPanel searchPanel = new BasicPanel();

    private TFInput tfPersonName = new TFInput(DataTypes.STRING, "Name", false);

    private TFInput tfDni = new TFInput(DataTypes.STRING, "DNI", false);

    private TFInput tfDomain = new TFInput(DataTypes.STRING, "Domain", false);

    private int[] sizeColumnList = { 28, 37, 80, 82, 44, 57, 57, 57, 57, 80, 52, 52 };

    private Vector tgsFeesHeader = new Vector();

    private Vector dataRow = new Vector();

    private GridPanel tgsGridPanel = new GridPanel(100, sizeColumnList, "Listado de Anticipos", dataRow);

    private SaveDataButton btnSaveData = new SaveDataButton();

    private CloseButton btnClose = new CloseButton();

    private Car car;

    private TaxesCar parentMain;

    public UpdateCarFeesMgmt() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(710, 388));
        this.setPreferredSize(new Dimension(710, 515));
        tfPersonName.setBounds(new Rectangle(50, 10, 235, 35));
        tgsGridPanel.setBounds(new Rectangle(5, 75, 700, 270));
        contentPanel.setBounds(new Rectangle(5, 5, 700, 500));
        contentPanel.setLayout(null);
        contentPanel.setSize(new Dimension(700, 515));
        searchPanel.setBounds(new Rectangle(5, 10, 690, 50));
        searchPanel.setLayout(null);
        tfDomain.setBounds(new Rectangle(475, 10, 140, 35));
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnSaveData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSaveData_actionPerformed(e);
            }
        });
        tfDni.setBounds(new Rectangle(310, 10, 140, 35));
        searchPanel.add(tfDni, null);
        searchPanel.add(tfDomain, null);
        searchPanel.add(tfPersonName, null);
        contentPanel.add(tgsGridPanel, null);
        contentPanel.add(searchPanel, null);
        this.add(contentPanel, null);
        tgsGridPanel.setCellEditor(Types.DOUBLE, 4);
        tgsGridPanel.getTable().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                double basicAmount = Double.parseDouble(tgsGridPanel.getTable().getValueAt(tgsGridPanel.getTable().getSelectedRow(), 5).toString());
                if (basicAmount > 0) {
                    double mora = (basicAmount * Double.parseDouble(tgsGridPanel.getTable().getValueAt(tgsGridPanel.getTable().getSelectedRow(), 6).toString())) / 100;
                    double descuento = (basicAmount * Double.parseDouble(tgsGridPanel.getTable().getValueAt(tgsGridPanel.getTable().getSelectedRow(), 8).toString())) / 100;
                    double totalAmount = (basicAmount + mora - descuento) * (1 - (Double.parseDouble(tgsGridPanel.getTable().getValueAt(tgsGridPanel.getTable().getSelectedRow(), 12).toString()) / 100));
                    tgsGridPanel.getTable().setValueAt(String.valueOf(Format.toDouble(mora)), tgsGridPanel.getTable().getSelectedRow(), 7);
                    tgsGridPanel.getTable().setValueAt(String.valueOf(Format.toDouble(descuento)), tgsGridPanel.getTable().getSelectedRow(), 9);
                    tgsGridPanel.getTable().setValueAt(String.valueOf(Format.toDouble(totalAmount)), tgsGridPanel.getTable().getSelectedRow(), 11);
                } else {
                    Advisor.messageBox("No est� permitido ingresar valores menores o igual a cero", "Error");
                }
            }
        });
        tfPersonName.getTextField().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) refresh();
            }
        });
        tfDomain.getTextField().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) refresh();
            }
        });
        tfPersonName.setEditable(false);
        tfDni.setEditable(false);
        tfDomain.setEditable(false);
        setCadastralHeader();
        addButton(btnClose);
        addButton(btnSaveData);
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
    }

    public void refresh() {
        String params = "'" + tfPersonName.getString().trim() + "','" + tfDni.getString().trim() + "','" + tfDomain.getString().trim() + "'";
        tgsGridPanel.refresh(params);
    }

    private void setCadastralHeader() {
        tgsFeesHeader.removeAllElements();
        tgsFeesHeader.addElement("*");
        tgsFeesHeader.addElement("N�");
        tgsFeesHeader.addElement("A�o");
        tgsFeesHeader.addElement("Fecha");
        tgsFeesHeader.addElement("Vence");
        tgsFeesHeader.addElement("Valor");
        tgsFeesHeader.addElement("% Mora");
        tgsFeesHeader.addElement("$ Mora");
        tgsFeesHeader.addElement("% Desc.");
        tgsFeesHeader.addElement("$ Desc.");
        tgsFeesHeader.addElement("F� Act.");
        tgsFeesHeader.addElement("$ Total");
        tgsFeesHeader.addElement("% Pagado");
        tgsFeesHeader.addElement("*");
        tgsGridPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
                } else if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                }
            }
        });
        tgsGridPanel.setParams("taxes.getCuotasAutomotor", "-1", tgsFeesHeader);
    }

    public void setCar(Car _car) {
        car = _car;
        if (car.getIddominio() != -1) {
            tfPersonName.setValue(car.getTitular());
            tfDni.setValue(car.getDni());
            tfDomain.setValue(car.getDominio());
            getCuotas();
        }
    }

    private void getCuotas() {
        int resul = LibSQL.getInt("taxes.setCarFees", car.getIddominio());
        tgsGridPanel.refresh("" + car.getIddominio());
        tgsGridPanel.calculate();
    }

    private void btnSaveData_actionPerformed(ActionEvent e) {
        if (Advisor.question("Aviso", "�Esta seguro de actualizar las cuotas?")) {
            tgsGridPanel.selectAllItems(true);
            Vector aux = tgsGridPanel.getSelectedsVector();
            String params = "";
            boolean ok = true;
            for (int i = 0; i < aux.size(); i++) {
                Vector vec = (Vector) aux.elementAt(i);
                int anticipo = Integer.parseInt(vec.get(1).toString());
                int anio = Integer.parseInt(vec.get(2).toString());
                Double montoBase = Double.parseDouble(vec.get(5).toString());
                Double mora = Double.parseDouble(vec.get(7).toString());
                Double descuento = Double.parseDouble(vec.get(9).toString());
                Double montoTotal = Double.parseDouble(vec.get(11).toString());
                params = car.getIddominio() + "," + anticipo + "," + anio + "," + montoBase + "," + mora + "," + descuento + "," + montoTotal;
                if (LibSQL.getInt("taxes.updateCarFees", params) <= 0) {
                    ok = false;
                }
            }
            if (ok) {
                parentMain.getCuotas();
                getParentInternalFrame().close();
            } else {
                Advisor.messageBox("Ocurri� un error al actualizar los datos", "Error");
            }
        }
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        getParentInternalFrame().close();
    }

    public void setParentMain(TaxesCar _parentMain) {
        parentMain = _parentMain;
    }
}
