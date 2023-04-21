package org.digitall.lib.geo.gaia.components.infrastructure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class GaiaTiposActividadesAdminyServList extends BasicPrimitivePanel {

    private BasicPanel panelData = new BasicPanel();

    private int[] sizeColumnList = { 41, 278 };

    private Vector headerList = new Vector();

    private Vector dataRow = new Vector();

    private GridPanel listPanel = new GridPanel(30, sizeColumnList, "Actividades", dataRow);

    private TFInput tfName = new TFInput(DataTypes.STRING, "Name", false);

    private CloseButton btnClose = new CloseButton();

    private AssignButton btnAdd = new AssignButton(true);

    private CancelDataButton btnCancel = new CancelDataButton();

    private FindButton btnFind = new FindButton();

    private BasicDialog parentContainer;

    private GaiaTipoActividadAdminyServ tipoActividad;

    private boolean addAction = true;

    public GaiaTiposActividadesAdminyServList() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(360, 285));
        listPanel.setBounds(new Rectangle(0, 70, 340, 310));
        panelData.setBounds(new Rectangle(0, 0, 340, 70));
        panelData.setLayout(null);
        tfName.setBounds(new Rectangle(15, 25, 165, 35));
        btnClose.setBounds(new Rectangle(305, 410, 40, 25));
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnAdd.setBounds(new Rectangle(190, 35, 40, 25));
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAdd_actionPerformed(e);
            }
        });
        btnCancel.setBounds(new Rectangle(240, 35, 40, 25));
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCancel_actionPerformed(e);
            }
        });
        btnFind.setBounds(new Rectangle(290, 35, 40, 25));
        btnFind.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnFind_actionPerformed(e);
            }
        });
        panelData.add(btnFind, null);
        panelData.add(btnCancel, null);
        panelData.add(tfName, null);
        panelData.add(btnAdd, null);
        this.add(panelData, BorderLayout.NORTH);
        this.add(listPanel, null);
        panelData.setBorder(BorderPanel.getBorderPanel("Agregar nuevo Tipo de Actividad"));
        panelData.setSize(new Dimension(360, 11));
        panelData.setMinimumSize(new Dimension(1, 100));
        panelData.setPreferredSize(new Dimension(1, 70));
        setHeaderList();
    }

    private void setHeaderList() {
        headerList.removeAllElements();
        headerList.addElement("ID");
        headerList.addElement("Nombre");
        headerList.addElement("*");
        listPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                    loadUnit();
                    loadData();
                }
            }
        });
        listPanel.setParams("gis_oran.getAllTiposActividadesAdminyServ", "''", headerList);
    }

    public void refresh() {
        String params = "''";
        listPanel.refresh(params);
    }

    private void loadUnit() {
        if (tipoActividad == null) {
            tipoActividad = new GaiaTipoActividadAdminyServ(Integer.parseInt("" + dataRow.elementAt(0)), dataRow.elementAt(1).toString());
        } else {
            tipoActividad.setIdTipoActividadAdminyServ(Integer.parseInt("" + dataRow.elementAt(0)));
            tipoActividad.setNombre(dataRow.elementAt(1).toString());
        }
    }

    private void loadData() {
        tfName.setValue(tipoActividad.getNombre());
        addAction = false;
    }

    private void clearData() {
        tfName.setValue("");
        addAction = true;
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        clearData();
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        if (addAction) {
            tipoActividad = new GaiaTipoActividadAdminyServ();
        }
        tipoActividad.setNombre(tfName.getString());
        tipoActividad.saveData();
        refresh();
        clearData();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        parentContainer.setVisible(false);
    }

    public void setParentContainer(BasicDialog parentContainer) {
        this.parentContainer = parentContainer;
    }

    private void btnFind_actionPerformed(ActionEvent e) {
        refresh();
    }
}
