package org.digitall.common.cashflow.interfaces.voucher;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JSeparator;
import org.digitall.common.cashflow.classes.ExpenditureAccount.CCItem;
import org.digitall.common.cashflow.interfaces.voucher.VoucherDetailMgmt;
import org.digitall.common.cashflow.classes.EntityTypes;
import org.digitall.common.cashflow.classes.ExpenditureAccount;
import org.digitall.common.cashflow.classes.VoucherDetail;
import org.digitall.common.cashflow.classes.Voucher;
import org.digitall.common.cashflow.interfaces.voucher.VouchersList;
import org.digitall.common.resourcescontrol.classes.Brands;
import org.digitall.common.resourcescontrol.classes.Resource;
import org.digitall.common.resourcescontrol.classes.ResourceBrands;
import org.digitall.common.resourcescontrol.classes.Units;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.grid.GridPanel;

public class VoucherDetailList extends BasicPrimitivePanel {

    private int[] sizeColumnList = { 205, 195, 118, 92, 89 };

    private Vector dataRow = new Vector();

    private GridPanel listPanel = new GridPanel(30, sizeColumnList, "Detalle del Comprobante de Gasto", dataRow);

    private Vector headerList = new Vector();

    private JSeparator jSeparator1 = new JSeparator();

    private CloseButton btnClose = new CloseButton();

    private AddButton btnAdd = new AddButton();

    private ModifyButton btnModify = new ModifyButton();

    private Voucher voucher;

    private VoucherDetail selectedVoucherDetail;

    private VoucherDetailMgmt voucherDetailMgmt;

    private Resource resource;

    private ResourceBrands resourceBrand;

    private VouchersList parentList;

    public VoucherDetailList() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(730, 290));
        this.setPreferredSize(new Dimension(730, 290));
        listPanel.setBounds(new Rectangle(5, 0, 720, 280));
        jSeparator1.setBounds(new Rectangle(0, 430, 730, 5));
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAdd_actionPerformed(e);
            }
        });
        btnModify.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnModify_actionPerformed(e);
            }
        });
        this.addButton(btnClose);
        this.addButton(btnModify);
        this.addButton(btnAdd);
        this.add(jSeparator1, null);
        this.add(listPanel, null);
        setHeaderList();
        btnModify.setEnabled(false);
        btnModify.setToolTipText("Modificar el �tem seleccionado");
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
        getParentInternalFrame().setInfo("Seleccione un �tem para modificar o agregue uno nuevo");
    }

    private void setHeaderList() {
        headerList.removeAllElements();
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("Tipo de Gasto");
        headerList.addElement("*");
        headerList.addElement("Recurso");
        headerList.addElement("*");
        headerList.addElement("Marca");
        headerList.addElement("*");
        headerList.addElement("Cantidad");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("($) Total");
        headerList.addElement("*");
        headerList.addElement("*");
        headerList.addElement("*");
        listPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                loadObject();
                if (me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2) {
                    loadVoucherDetailMgmt();
                } else if (me.getButton() == MouseEvent.BUTTON3 && me.getClickCount() == 2) {
                    int index = listPanel.getTable().rowAtPoint(me.getPoint());
                    listPanel.getTable().getSelectionModel().setSelectionInterval(index, index);
                }
            }
        });
        listPanel.setParams("cashflow.getAllVoucherDetail", "-1", headerList);
    }

    public void refresh() {
        String params = "" + voucher.getIdVoucher();
        listPanel.refresh(params);
    }

    public void clearList() {
        listPanel.getTable().clearSelection();
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
        if (voucher.isClosed()) {
            btnAdd.setEnabled(false);
        } else {
            btnAdd.setEnabled(true);
        }
        refresh();
    }

    private void loadObject() {
        if (!dataRow.isEmpty()) {
            selectedVoucherDetail = new VoucherDetail();
            selectedVoucherDetail.setIdVoucherDetail(Integer.parseInt("" + dataRow.elementAt(0)));
            selectedVoucherDetail.setVoucher(voucher);
            selectedVoucherDetail.setExpenditureAccount(new ExpenditureAccount.CCItem(Integer.parseInt("" + dataRow.elementAt(2)), "" + dataRow.elementAt(3)));
            resourceBrand = new ResourceBrands();
            resource = new Resource(EntityTypes.RESOURCE, Integer.parseInt("" + dataRow.elementAt(4)));
            resource.setName("" + dataRow.elementAt(5));
            resourceBrand.setResource(resource);
            resourceBrand.setBrands(new Brands(Integer.parseInt("" + dataRow.elementAt(6))));
            resourceBrand.setPrice(Double.parseDouble("" + dataRow.elementAt(10)));
            resourceBrand.setUnit(new Units(Integer.parseInt("" + dataRow.elementAt(18)), dataRow.elementAt(19).toString()));
            selectedVoucherDetail.setResourceBrand(resourceBrand);
            selectedVoucherDetail.setOriginalQty(Double.parseDouble("" + dataRow.elementAt(8)));
            selectedVoucherDetail.setFinalQty(Double.parseDouble("" + dataRow.elementAt(9)));
            selectedVoucherDetail.setPartialWOTaxes(Double.parseDouble("" + dataRow.elementAt(11)));
            selectedVoucherDetail.setTaxes(Double.parseDouble("" + dataRow.elementAt(12)));
            selectedVoucherDetail.setPartialWTaxes(Double.parseDouble("" + dataRow.elementAt(13)));
            selectedVoucherDetail.setVATAmount(Double.parseDouble("" + dataRow.elementAt(14)));
            selectedVoucherDetail.setVATp(Double.parseDouble("" + dataRow.elementAt(15)));
            selectedVoucherDetail.setAmount(Double.parseDouble("" + dataRow.elementAt(16)));
            selectedVoucherDetail.setDescription("" + dataRow.elementAt(17));
            btnModify.setEnabled(true);
            btnModify.setToolTipText("<html><center><u>Modificar �tem</u><br>" + resource.getName() + " - " + dataRow.elementAt(7) + "</center></html>");
        } else {
            btnModify.setToolTipText("Modificar el �tem seleccionado");
        }
    }

    private void loadVoucherDetailMgmt() {
        voucherDetailMgmt = new VoucherDetailMgmt();
        voucherDetailMgmt.setVoucherDetail(selectedVoucherDetail);
        voucherDetailMgmt.setParentList(this);
        voucherDetailMgmt.setVouchersList(parentList);
        ExtendedInternalFrame voucherDetailMgmtContainer;
        if (selectedVoucherDetail.getIdVoucherDetail() != -1) {
            voucherDetailMgmtContainer = new ExtendedInternalFrame("Modificar �tem");
        } else {
            voucherDetailMgmtContainer = new ExtendedInternalFrame("Agregar nuevo item");
        }
        voucherDetailMgmtContainer.setCentralPanel(voucherDetailMgmt);
        voucherDetailMgmtContainer.show();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        parentList.refresh();
        getParentInternalFrame().close();
    }

    private void btnModify_actionPerformed(ActionEvent e) {
        loadVoucherDetailMgmt();
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        if (!voucher.isClosed()) {
            selectedVoucherDetail = new VoucherDetail();
            selectedVoucherDetail.setVoucher(voucher);
            loadVoucherDetailMgmt();
        } else {
            Advisor.messageBox("Imposible agregar mas items", "Comprobante cerrado");
        }
    }

    public void setParentList(VouchersList parentList) {
        this.parentList = parentList;
    }
}
