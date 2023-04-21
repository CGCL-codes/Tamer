package com.cosmos.acacia.gui;

import com.cosmos.acacia.crm.data.DataObjectBean;
import com.cosmos.acacia.crm.gui.AcaciaApplication;
import com.cosmos.acacia.crm.gui.contactbook.CityPanel;
import com.cosmos.acacia.security.GUIAccessControl;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

/**
 *
 * @author  miro
 */
public class CRUDButtonPanel extends AcaciaPanel {

    protected static Logger log = Logger.getLogger(CRUDButtonPanel.class);

    public enum Button {

        Select("selectAction"), New("newAction"), Modify("modifyAction"), Delete("deleteAction"), Refresh("refreshAction"), Close("closeAction");

        private Button(String actionName) {
            this.actionName = actionName;
        }

        private String actionName;

        public String getActionName() {
            return actionName;
        }
    }

    ;

    /** Creates new form CRUDButtonPanel */
    public CRUDButtonPanel() {
        super();
        initComponents();
        initData();
    }

    private void initComponents() {
        closeButton = new com.cosmos.swingb.JBButton();
        refreshButton = new com.cosmos.swingb.JBButton();
        deleteButton = new com.cosmos.swingb.JBButton();
        modifyButton = new com.cosmos.swingb.JBButton();
        newButton = new com.cosmos.swingb.JBButton();
        selectButton = new com.cosmos.swingb.JBButton();
        setName("Form");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.cosmos.acacia.crm.gui.AcaciaApplication.class).getContext().getActionMap(CRUDButtonPanel.class, this);
        closeButton.setAction(actionMap.get("closeAction"));
        closeButton.setName("closeButton");
        refreshButton.setAction(actionMap.get("refreshAction"));
        refreshButton.setName("refreshButton");
        deleteButton.setAction(actionMap.get("deleteAction"));
        deleteButton.setName("deleteButton");
        modifyButton.setAction(actionMap.get("modifyAction"));
        modifyButton.setName("modifyButton");
        newButton.setAction(actionMap.get("newAction"));
        newButton.setName("newButton");
        selectButton.setAction(actionMap.get("selectAction"));
        selectButton.setName("selectButton");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(43, Short.MAX_VALUE).addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { closeButton, deleteButton, modifyButton, newButton, refreshButton, selectButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(modifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { closeButton, deleteButton, modifyButton, newButton, refreshButton, selectButton });
    }

    private com.cosmos.swingb.JBButton closeButton;

    private com.cosmos.swingb.JBButton deleteButton;

    private com.cosmos.swingb.JBButton modifyButton;

    private com.cosmos.swingb.JBButton newButton;

    private com.cosmos.swingb.JBButton refreshButton;

    private com.cosmos.swingb.JBButton selectButton;

    private TableSelectionListener tableSelectionListener;

    private GUIAccessControl accessControl;

    private CRUDButtonActionsListener buttonActionsListener;

    protected void initData() {
        setEnabled(CRUDButtonPanel.Button.Select, false);
        setEnabled(CRUDButtonPanel.Button.Modify, false);
        setEnabled(CRUDButtonPanel.Button.Delete, false);
    }

    @Action
    public void selectAction() {
    }

    @Action
    public void closeAction() {
        if (buttonActionsListener != null) buttonActionsListener.closeAction();
    }

    @Action
    public Task refreshAction() {
        return new RefreshActionTask(Application.getInstance(AcaciaApplication.class));
    }

    private class RefreshActionTask extends Task<Object, Void> {

        RefreshActionTask(Application app) {
            super(app);
            if (buttonActionsListener != null) buttonActionsListener.refreshAction();
        }

        @Override
        protected Object doInBackground() {
            return null;
        }

        @Override
        protected void succeeded(Object result) {
        }
    }

    @Action
    public void deleteAction() {
        if (buttonActionsListener != null) {
            ResourceMap resource = getResourceMap();
            String title = resource.getString("deleteAction.ConfirmDialog.title");
            String message = resource.getString("deleteAction.ConfirmDialog.message");
            Icon icon = resource.getImageIcon("deleteAction.ConfirmDialog.icon");
            int result = JOptionPane.showConfirmDialog(this.getParent(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
            if (JOptionPane.YES_OPTION == result) buttonActionsListener.deleteAction();
        }
    }

    @Action
    public void modifyAction() {
        if (buttonActionsListener != null) buttonActionsListener.modifyAction();
    }

    @Action
    public void newAction() {
        if (buttonActionsListener != null) buttonActionsListener.newAction();
    }

    public javax.swing.Action getAction(Button button) {
        ApplicationActionMap actionMap = getApplicationActionMap();
        if (actionMap != null && button != null) {
            return actionMap.get(button.getActionName());
        }
        return null;
    }

    public void setEnabled(Button button, boolean enabled) {
        ApplicationAction action = (ApplicationAction) getAction(button);
        if (action != null) {
            action.setEnabled(enabled);
        }
    }

    public void setSelected(Button button, boolean enabled) {
        ApplicationAction action = (ApplicationAction) getAction(button);
        if (action != null) {
            action.setSelected(enabled);
        }
    }

    public void setVisible(Button button, boolean visible) {
        switch(button) {
            case Select:
                selectButton.setVisible(visible);
            case New:
                newButton.setVisible(visible);
                break;
            case Modify:
                modifyButton.setVisible(visible);
                break;
            case Delete:
                deleteButton.setVisible(visible);
                break;
            case Refresh:
                refreshButton.setVisible(visible);
                break;
            case Close:
                closeButton.setVisible(visible);
                break;
        }
    }

    public boolean isVisible(Button button) {
        switch(button) {
            case Select:
                return selectButton.isVisible();
            case New:
                return newButton.isVisible();
            case Modify:
                return modifyButton.isVisible();
            case Delete:
                return deleteButton.isVisible();
            case Refresh:
                return refreshButton.isVisible();
            case Close:
                return closeButton.isVisible();
        }
        throw new IllegalArgumentException("Unknown or unsupported Button enumeration: " + button);
    }

    public void addListSelectionListener(AcaciaTable table) {
        tableSelectionListener = new TableSelectionListener(table);
    }

    public GUIAccessControl getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(GUIAccessControl accessControl) {
        this.accessControl = accessControl;
    }

    public CRUDButtonActionsListener getButtonActionsListener() {
        return buttonActionsListener;
    }

    public void setButtonActionsListener(CRUDButtonActionsListener buttonActionsListener) {
        this.buttonActionsListener = buttonActionsListener;
    }

    private class TableSelectionListener implements ListSelectionListener {

        private AcaciaTable table;

        public TableSelectionListener(AcaciaTable table) {
            table.addListSelectionListener(this);
            this.table = table;
        }

        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                log.info("CRUDButtonPanel.TableSelectionListener.valueChanged.event: " + event);
                ListSelectionModel selectionModel = (ListSelectionModel) event.getSource();
                if (selectionModel.isSelectionEmpty()) {
                    setEnabled(CRUDButtonPanel.Button.Modify, false);
                    setEnabled(CRUDButtonPanel.Button.Delete, false);
                    setEnabled(CRUDButtonPanel.Button.Select, false);
                } else {
                    GUIAccessControl accessControl = getAccessControl();
                    Object selectedObject = table.getSelectedRowObject();
                    log.info("selectedObject: " + selectedObject);
                    if (accessControl != null && selectedObject instanceof DataObjectBean) {
                        DataObjectBean entity = (DataObjectBean) selectedObject;
                        setEnabled(CRUDButtonPanel.Button.Modify, accessControl.canModify(entity));
                        setEnabled(CRUDButtonPanel.Button.Delete, accessControl.canDelete(entity));
                    } else {
                        setEnabled(CRUDButtonPanel.Button.Modify, true);
                        setEnabled(CRUDButtonPanel.Button.Delete, true);
                    }
                    setEnabled(CRUDButtonPanel.Button.Select, true);
                }
            }
        }
    }
}
