package org.digitall.apps.resourcesrequests.interfaces.resourcesrequests;

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
import org.digitall.common.cashflow.classes.CostsCentre;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.common.resourcesrequests.classes.ResourcesRequest;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;

public class ResourcesRequestsList extends BasicPrimitivePanel {

    private int[] sizeColumnList = { 85, 90, 163, 93, 123, 163 };

    private Vector dataRow = new Vector();

    private GridPanel listPanel = new GridPanel(30, sizeColumnList, "Pedidos de Materiales realizados", dataRow) {

        @Override
        public void finishLoading() {
            btnReport.setEnabled(hasItems());
        }
    };

    private ResourcesRequest selectedResourcesRequest;

    private BasicPanel searchPanel = new BasicPanel();

    private TFInput tfNumber = new TFInput(DataTypes.INTEGER, "Number", false);

    private TFInput tfStartDate = new TFInput(DataTypes.SIMPLEDATE, "StartDate", false);

    private TFInput tfEndDate = new TFInput(DataTypes.SIMPLEDATE, "EndDate", false);

    private CBInput cbCostsCentre = new CBInput(CachedCombo.COSTSCENTRE, "CostsCentre", false);

    private FindButton btnSearch = new FindButton();

    private PrintButton btnReport = new PrintButton();

    private PrintButton btnPrint = new PrintButton();

    private BasicPanel northPanel = new BasicPanel();

    private int[] detailSizeColumnList = { 322, 150, 79, 85, 85, 78 };

    private Vector detailDataRow = new Vector();

    private GridPanel detailPanel = new GridPanel(30, detailSizeColumnList, "Detalle", detailDataRow) {

        @Override
        public void finishLoading() {
            btnPrint.setEnabled(hasItems());
        }
    };

    public ResourcesRequestsList() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(760, 512));
        this.setPreferredSize(new Dimension(760, 512));
        listPanel.setPreferredSize(new Dimension(400, 200));
        northPanel.setLayout(new BorderLayout());
        detailPanel.setSize(new Dimension(760, 200));
        searchPanel.add(btnSearch, null);
        searchPanel.add(tfEndDate, null);
        searchPanel.add(tfStartDate, null);
        searchPanel.add(tfNumber, null);
        searchPanel.add(cbCostsCentre);
        northPanel.add(searchPanel, BorderLayout.NORTH);
        northPanel.add(listPanel, BorderLayout.CENTER);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(detailPanel, BorderLayout.CENTER);
        searchPanel.setLayout(null);
        searchPanel.setPreferredSize(new Dimension(1, 70));
        tfNumber.setBounds(new Rectangle(20, 25, 90, 35));
        tfStartDate.setBounds(new Rectangle(155, 25, 115, 35));
        tfEndDate.setBounds(new Rectangle(285, 25, 115, 35));
        cbCostsCentre.setBounds(new Rectangle(450, 25, 255, 35));
        btnSearch.setBounds(new Rectangle(710, 30, 35, 30));
        btnSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSearch_actionPerformed(e);
            }
        });
        setHeaderList();
        setHeaderDetail();
        cbCostsCentre.getCombo().addItem("Todos", "-1");
        btnReport.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnReport_actionPerformed(e);
            }
        });
        addButton(btnReport);
        addButton(btnPrint);
        btnReport.setToolTipText("Imprimir el Listado de Pedidos de Materiales");
        btnPrint.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPrint_actionPerformed(e);
            }
        });
        btnPrint.setToolTipText("Imprimir el Pedido de Materiales seleccionado");
        searchPanel.setBorder(BorderPanel.getBorderPanel("Buscar"));
        btnPrint.setEnabled(false);
        btnReport.setEnabled(false);
        KeyAdapter findListener = new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                    refresh();
                }
            }
        };
        tfNumber.getTextField().addKeyListener(findListener);
        tfStartDate.getTextField().addKeyListener(findListener);
        tfEndDate.getTextField().addKeyListener(findListener);
        listPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                    loadSelectedObject();
                } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON3) {
                    int index = listPanel.getTable().rowAtPoint(e.getPoint());
                    listPanel.getTable().getSelectionModel().setSelectionInterval(index, index);
                }
            }
        });
        detailPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON3) {
                }
            }
        });
    }

    private void setHeaderList() {
        Vector headerList = new Vector();
        headerList.removeAllElements();
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("RequestNumber"));
        headerList.addElement(Environment.lang.getProperty("Date"));
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Solicitor"));
        headerList.addElement(Environment.lang.getProperty("Cost"));
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Status"));
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Motivo del pedido"));
        headerList.addElement("*");
        String params = "-1,'','','',-1,-1, -1";
        listPanel.setParams("resourcescontrol.getAllResourcesRequests", params, headerList);
    }

    public void refresh() {
        String params = cbCostsCentre.getSelectedValue() + "," + tfNumber.getStringForSQL() + "," + tfStartDate.getDateForSQL() + "," + tfEndDate.getDateForSQL() + ",-1,-1,-1";
        listPanel.refresh(params);
        btnPrint.setToolTipText("Imprimir el Pedido de Materiales seleccionado");
        btnPrint.setEnabled(false);
    }

    private void loadSelectedObject() {
        if (!dataRow.isEmpty()) {
            selectedResourcesRequest = new ResourcesRequest();
            selectedResourcesRequest.setIdResourcesRequest(Integer.parseInt("" + dataRow.elementAt(0)));
            selectedResourcesRequest.setCostsCentre(new CostsCentre(Integer.parseInt("" + dataRow.elementAt(1))));
            selectedResourcesRequest.getCostsCentre().setName("" + dataRow.elementAt(2));
            selectedResourcesRequest.setNumber(Integer.parseInt("0" + dataRow.elementAt(3)));
            selectedResourcesRequest.setDate(Proced.setFormatDate(dataRow.elementAt(4).toString(), false));
            selectedResourcesRequest.setIdSolicitor(Integer.parseInt("" + dataRow.elementAt(5)));
            selectedResourcesRequest.setEstimatedAmount(Double.parseDouble("" + dataRow.elementAt(7)));
            selectedResourcesRequest.setIdRequestStatus(Integer.parseInt("" + dataRow.elementAt(8)));
            selectedResourcesRequest.setIdPriorityRequest(Integer.parseInt("" + dataRow.elementAt(10)));
            selectedResourcesRequest.setDescription(dataRow.elementAt(12).toString());
            refreshDetail();
            btnPrint.setToolTipText("<html><center><u>Imprimir Pedido de Materiales</u><br>N� " + selectedResourcesRequest.getNumber() + "</center></html>");
            btnPrint.setEnabled(true);
        } else {
            btnPrint.setToolTipText("Imprimir el Pedido de Materiales seleccionado");
            btnPrint.setEnabled(false);
        }
    }

    private void setHeaderDetail() {
        Vector headerDetail = new Vector();
        headerDetail.removeAllElements();
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement(Environment.lang.getProperty("Material"));
        headerDetail.addElement("*");
        headerDetail.addElement(Environment.lang.getProperty("Brand"));
        headerDetail.addElement(Environment.lang.getProperty("Cantidad"));
        headerDetail.addElement("*");
        headerDetail.addElement(Environment.lang.getProperty("Unit"));
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement(Environment.lang.getProperty("Autorizado"));
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("*");
        headerDetail.addElement("A Comprar");
        String params = "-1";
        detailPanel.setParams("resourcescontrol.getAllResourcesRequestDetail", params, headerDetail);
    }

    private void refreshDetail() {
        detailPanel.refresh(selectedResourcesRequest.getIdResourcesRequest());
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        refresh();
        selectedResourcesRequest = new ResourcesRequest(-1);
        refreshDetail();
    }

    private void btnReport_actionPerformed(ActionEvent e) {
        if (listPanel.hasItems()) {
            String params = cbCostsCentre.getSelectedValue() + ",'" + tfNumber.getString() + "','" + Proced.setFormatDate(tfStartDate.getDate(), false) + "','" + Proced.setFormatDate(tfEndDate.getDate(), false) + "',-1,-1,-1";
            BasicReport report = new BasicReport(ResourcesRequestsList.class.getResource("xml/ResourcesRequestsListReport.xml"));
            report.addTableModel("resourcescontrol.xmlGetAllResourcesRequests", params);
            report.setProperty("startdate", tfStartDate.getDate());
            report.setProperty("enddate", tfEndDate.getDate());
            report.setProperty("costcentre", cbCostsCentre.getSelectedItem().toString());
            report.setProperty("codecostcenter", cbCostsCentre.getSelectedItem().toString().substring(0, cbCostsCentre.getSelectedItem().toString().indexOf("-")));
            report.doReport();
        }
    }

    private void btnPrint_actionPerformed(ActionEvent e) {
        BasicReport report = new BasicReport(ResourcesRequestsMain.class.getResource("xml/resourcesRequest.xml"));
        report.addTableModel("resourcescontrol.xmlGetResourcesRequest", "" + selectedResourcesRequest.getIdResourcesRequest());
        report.addTableModel("resourcescontrol.xmlGetAllResourcesRequestDetail", "" + selectedResourcesRequest.getIdResourcesRequest());
        report.doReport();
    }
}
