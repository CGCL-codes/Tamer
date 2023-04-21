package ebiCRM.gui.dialogs;

import ebiCRM.EBICRMModule;
import ebiCRM.table.models.MyTableModelCRMAddress;
import ebiNeutrinoSDK.EBIPGFactory;
import ebiNeutrinoSDK.model.hibernate.Companyaddress;
import ebiNeutrinoSDK.model.hibernate.Companycontactaddress;
import org.jdesktop.swingx.sort.RowFilters;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Set;

public class EBIAddressSelectionDialog {

    private MyTableModelCRMAddress tabModel = null;

    private Set<Companyaddress> caddressList = null;

    private Set<Companycontactaddress> coaddressList = null;

    private int selRow = -1;

    private EBICRMModule ebiModule = null;

    public EBIAddressSelectionDialog(EBICRMModule module, Set<Companyaddress> clist, Set<Companycontactaddress> colist) {
        ebiModule = module;
        tabModel = new MyTableModelCRMAddress();
        caddressList = clist;
        coaddressList = colist;
        ebiModule.guiRenderer.loadGUI("CRMDialog/crmSelectionDialog.xml");
        showCollectionList();
    }

    public void setVisible() {
        ebiModule.guiRenderer.getEBIDialog("abstractSelectionDialog").setTitle(EBIPGFactory.getLANG("EBI_LANG_C_ADRESS_DATA"));
        ebiModule.guiRenderer.getVisualPanel("abstractSelectionDialog").setModuleTitle(EBIPGFactory.getLANG("EBI_LANG_C_ADRESS_DATA"));
        ebiModule.guiRenderer.getTextfield("filterTableText", "abstractSelectionDialog").addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + ebiModule.guiRenderer.getTextfield("filterTableText", "abstractSelectionDialog").getText()));
            }

            public void keyReleased(KeyEvent e) {
                ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + ebiModule.guiRenderer.getTextfield("filterTableText", "abstractSelectionDialog").getText()));
            }
        });
        ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").setModel(tabModel);
        ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selRow = ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    ebiModule.guiRenderer.getButton("applyButton", "abstractSelectionDialog").setEnabled(false);
                    selRow = -1;
                } else if (!tabModel.getRow(0)[0].toString().equals(EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"))) {
                    selRow = lsm.getMinSelectionIndex();
                    ebiModule.guiRenderer.getButton("applyButton", "abstractSelectionDialog").setEnabled(true);
                }
            }
        });
        ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1) {
                    selRow = ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()));
                }
                if (e.getClickCount() == 2) {
                    if (selRow < 0 || EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selRow][0].toString())) {
                        return;
                    }
                    ebiModule.guiRenderer.getEBIDialog("abstractSelectionDialog").setVisible(false);
                    fillCollection();
                }
            }
        });
        ebiModule.guiRenderer.getButton("applyButton", "abstractSelectionDialog").setText(EBIPGFactory.getLANG("EBI_LANG_APPLY"));
        ebiModule.guiRenderer.getButton("applyButton", "abstractSelectionDialog").setEnabled(false);
        ebiModule.guiRenderer.getButton("applyButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (selRow < 0 || EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selRow][0].toString())) {
                    return;
                }
                fillCollection();
                ebiModule.guiRenderer.getEBIDialog("abstractSelectionDialog").setVisible(false);
            }
        });
        ebiModule.guiRenderer.getButton("closeButton", "abstractSelectionDialog").setText(EBIPGFactory.getLANG("EBI_LANG_CANCEL"));
        ebiModule.guiRenderer.getButton("closeButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ebiModule.guiRenderer.getEBIDialog("abstractSelectionDialog").setVisible(false);
            }
        });
        ebiModule.guiRenderer.showGUI();
    }

    private void copyCollection(int[] id) {
        if (caddressList != null) {
            int i = 0;
            Iterator iter = this.caddressList.iterator();
            while (iter.hasNext()) {
                Companyaddress address = (Companyaddress) iter.next();
                if (address.getAddressid() == id[i]) {
                    Companycontactaddress addr = new Companycontactaddress();
                    addr.setAddressid(((i + 1) * (-1)));
                    addr.setAddresstype(address.getAddresstype());
                    addr.setCountry(address.getCountry());
                    addr.setStreet(address.getStreet());
                    addr.setZip(address.getZip());
                    addr.setLocation(address.getLocation());
                    addr.setPbox(address.getPbox());
                    addr.setCreateddate(address.getCreateddate());
                    addr.setCreatedfrom(address.getCreatedfrom());
                    addr.setChangeddate(address.getChangeddate());
                    addr.setChangedfrom(address.getChangedfrom());
                    coaddressList.add(addr);
                    i++;
                }
            }
        }
    }

    private void fillCollection() {
        int[] rows = ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").getSelectedRows();
        int[] id = new int[rows.length + 1];
        for (int i = 0; i < rows.length; i++) {
            id[i] = Integer.parseInt(tabModel.data[ebiModule.guiRenderer.getTable("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(rows[i])][6].toString());
        }
        copyCollection(id);
    }

    private void showCollectionList() {
        if (this.caddressList.size() > 0) {
            tabModel.data = new Object[this.caddressList.size()][7];
            Iterator itr = this.caddressList.iterator();
            int i = 0;
            while (itr.hasNext()) {
                Companyaddress obj = (Companyaddress) itr.next();
                tabModel.data[i][0] = obj.getAddresstype() == null ? "" : obj.getAddresstype();
                tabModel.data[i][1] = obj.getStreet() == null ? "" : obj.getStreet();
                tabModel.data[i][2] = obj.getZip() == null ? "" : obj.getZip();
                tabModel.data[i][3] = obj.getLocation() == null ? "" : obj.getLocation();
                tabModel.data[i][4] = obj.getPbox() == null ? "" : obj.getPbox();
                tabModel.data[i][5] = obj.getCountry() == null ? "" : obj.getCountry();
                tabModel.data[i][6] = obj.getAddressid();
                i++;
            }
        } else {
            tabModel.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "" } };
        }
        tabModel.fireTableDataChanged();
    }
}
