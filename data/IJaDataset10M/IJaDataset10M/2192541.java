package com.empower.client.controller.lists;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.ecs.etrade.uiinterfaces.EmployeeUI;
import com.ecs.etrade.uiinterfaces.EmployeeUIImpl;
import com.empower.client.controller.ApplicationController;
import com.empower.client.controller.create.CreateEmployeeController;
import com.empower.client.tablemodels.EmployeeListTableModel;
import com.empower.client.utils.MessageDisplayUtils;
import com.empower.client.view.create.CreateEmployeeFrame;
import com.empower.client.view.lists.EmployeeListFrame;
import com.empower.constants.AppClientConstants;
import com.empower.model.EmployeeListModel;
import com.empower.model.EmployeeModel;

public class EmployeeListController {

    private boolean tableInitialized = false;

    private EmployeeListFrame currEmpListFrame;

    private JTable empListTbl;

    ResourceBundle resMsg = ResourceBundle.getBundle("com.empower.client.Messages");

    ResourceBundle resWindowTitles = ResourceBundle.getBundle("com.empower.client.WindowTitles");

    public EmployeeListController(EmployeeListFrame empListFrame) {
        this.currEmpListFrame = empListFrame;
        empListTbl = currEmpListFrame.getTable();
        buildView();
    }

    private void buildView() {
        setEditDelBtnsState(false);
        initTable();
        addListeners();
        populateListFromDB();
    }

    private void setEditDelBtnsState(boolean state) {
        currEmpListFrame.getEditBtn().setEnabled(state);
        currEmpListFrame.getDeleteBtn().setEnabled(state);
    }

    private void initTable() {
        EmployeeListTableModel tableModel = null;
        if (tableInitialized) {
            tableModel = (EmployeeListTableModel) empListTbl.getModel();
            setAvailableTableColumnNamesAndWidths(empListTbl, tableModel);
        } else {
            empListTbl.setCellSelectionEnabled(false);
            empListTbl.setColumnSelectionAllowed(false);
            empListTbl.setRowSelectionAllowed(true);
            empListTbl.setShowGrid(false);
            empListTbl.setAutoCreateColumnsFromModel(true);
            tableModel = new EmployeeListTableModel();
            empListTbl.setModel(tableModel);
            setAvailableTableColumnNamesAndWidths(empListTbl, tableModel);
            empListTbl.getTableHeader().setReorderingAllowed(false);
            empListTbl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableInitialized = true;
        }
    }

    private void setAvailableTableColumnNamesAndWidths(JTable tmpTbl, EmployeeListTableModel tableModel) {
        tableModel.setColumnNames();
        tmpTbl.getColumnModel().getColumn(0).setPreferredWidth(200);
        tmpTbl.getColumnModel().getColumn(1).setPreferredWidth(300);
        tmpTbl.getColumnModel().getColumn(2).setPreferredWidth(300);
        tmpTbl.getColumnModel().getColumn(3).setPreferredWidth(300);
        tmpTbl.getColumnModel().getColumn(4).setPreferredWidth(175);
        tmpTbl.getColumnModel().getColumn(5).setPreferredWidth(175);
    }

    private void addListeners() {
        currEmpListFrame.getEmpCodeTxf().addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent arg0) {
                empListTbl.clearSelection();
            }
        });
        currEmpListFrame.getGoBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                goProcess();
            }
        });
        currEmpListFrame.getNewBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                newProcess();
            }
        });
        currEmpListFrame.getEditBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                editProcess();
            }
        });
        currEmpListFrame.getDeleteBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                deleteProcess();
            }
        });
        empListTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    setEditDelBtnsState(true);
                } else {
                    setEditDelBtnsState(false);
                }
            }
        });
        empListTbl.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 2) {
                    editProcess();
                }
                if (arg0.getClickCount() == 1) {
                    setEditDelBtnsState(true);
                }
            }
        });
    }

    private void goProcess() {
        int rowCnt = empListTbl.getRowCount();
        String srchStr = currEmpListFrame.getEmpCodeTxf().getText();
        for (int i = 0; i < rowCnt; i++) {
            if (empListTbl.getValueAt(i, 0).toString().toUpperCase().contains(srchStr.toUpperCase())) {
                empListTbl.clearSelection();
                empListTbl.setRowSelectionInterval(i, i);
                setEditDelBtnsState(true);
                return;
            }
        }
        MessageDisplayUtils.displayInfoMsg(currEmpListFrame, resMsg.getString("SEARCH_EMPLOYEE_FALIED"));
        currEmpListFrame.getEmpCodeTxf().requestFocus();
    }

    private void newProcess() {
        CreateEmployeeFrame tmpWndw = showEmpListFrame();
        if (null != tmpWndw) {
            new CreateEmployeeController(tmpWndw, AppClientConstants.CREATE_MODE, null, this);
        } else return;
    }

    private void editProcess() {
        CreateEmployeeFrame tmpWndw = showEmpListFrame();
        EmployeeModel employeeModel = getSelectedEmployeeModel();
        if (null != tmpWndw) {
            tmpWndw.getSalutationCbx().requestFocus();
            new CreateEmployeeController(tmpWndw, AppClientConstants.EDIT_MODE, employeeModel, this);
        } else return;
    }

    private void deleteProcess() {
        int slctdRowNbr = empListTbl.getSelectedRow();
        String empCode = empListTbl.getValueAt(slctdRowNbr, 0).toString();
        if (MessageDisplayUtils.displayDeleteConfirmMsg(currEmpListFrame, " employee")) {
            EmployeeUI employeeUI = new EmployeeUIImpl();
            try {
                employeeUI.deleteEmployeeDetails(empCode);
                MessageDisplayUtils.displayInfoMsg(currEmpListFrame, resMsg.getString("EMPLOYEE_DELETE_SUCCESSFUL"));
            } catch (Exception e) {
                MessageDisplayUtils.displayFailureMsg(currEmpListFrame, resMsg.getString("EMPLOYEE_DELETE_FAILED"));
            }
            if (-1 != slctdRowNbr) {
                ((EmployeeListTableModel) empListTbl.getModel()).removeRow(slctdRowNbr);
                currEmpListFrame.getEmpCodeTxf().requestFocus();
            }
        }
    }

    private CreateEmployeeFrame showEmpListFrame() {
        CreateEmployeeFrame tmpWndw = new CreateEmployeeFrame("CREATE EMPLOYEE", false, true, false, true);
        SwingUtilities.updateComponentTreeUI(tmpWndw);
        tmpWndw.setBounds(400, 230, 750, 390);
        tmpWndw.setVisible(true);
        ApplicationController.getAppFrame().getContentPane().add(tmpWndw);
        return tmpWndw;
    }

    private EmployeeModel getSelectedEmployeeModel() {
        EmployeeModel tmpEmpModel = new EmployeeModel();
        int slctdRowNbr = empListTbl.getSelectedRow();
        String empCode = empListTbl.getValueAt(slctdRowNbr, 0).toString();
        EmployeeUI employeeUI = new EmployeeUIImpl();
        try {
            tmpEmpModel = employeeUI.getEmployeeDetails(empCode);
        } catch (Exception e) {
            ;
        }
        return tmpEmpModel;
    }

    private void setModelsToEmpListTbl(ArrayList<EmployeeListModel> tmpMdl) {
        EmployeeListTableModel tableModel = (EmployeeListTableModel) empListTbl.getModel();
        Iterator<EmployeeListModel> iter = tmpMdl.iterator();
        while (iter.hasNext()) {
            Vector<EmployeeListModel> dataRow = new Vector<EmployeeListModel>();
            dataRow.add(iter.next());
            tableModel.addRow(dataRow);
        }
    }

    public void refreshList() {
        int count = ((EmployeeListTableModel) empListTbl.getModel()).getRowCount();
        for (int i = 0; i < count; i++) ((EmployeeListTableModel) empListTbl.getModel()).removeRow(0);
        populateListFromDB();
    }

    private void populateListFromDB() {
        EmployeeUI employeeUI = new EmployeeUIImpl();
        try {
            ArrayList<EmployeeListModel> employeeListModel = employeeUI.getAllEmployees();
            if (employeeListModel != null) {
                setModelsToEmpListTbl(employeeListModel);
            }
        } catch (Exception e) {
            MessageDisplayUtils.displayInfoMsg(currEmpListFrame, resMsg.getString("FETCH_EMPLOYEE_FAILED"));
        }
    }
}
