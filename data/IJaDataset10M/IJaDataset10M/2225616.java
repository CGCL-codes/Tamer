package com.cosmos.acacia.crm.gui.deliverycertificates;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.swing.JOptionPane;
import org.apache.commons.collections.CollectionUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingx.error.ErrorInfo;
import com.cosmos.acacia.crm.bl.impl.DeliveryCertificatesRemote;
import com.cosmos.acacia.crm.bl.invoice.InvoiceListRemote;
import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.crm.data.Classifier;
import com.cosmos.acacia.crm.data.contacts.ContactPerson;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.data.warehouse.DeliveryCertificate;
import com.cosmos.acacia.crm.data.warehouse.DeliveryCertificateAssignment;
import com.cosmos.acacia.crm.data.warehouse.DeliveryCertificateItem;
import com.cosmos.acacia.crm.data.sales.SalesInvoice;
import com.cosmos.acacia.crm.data.sales.SalesInvoiceItem;
import com.cosmos.acacia.crm.data.contacts.Organization;
import com.cosmos.acacia.crm.data.predicates.ValidDeliveryCertificateAssignmentPredicate;
import com.cosmos.acacia.crm.enums.DeliveryCertificateMethodType;
import com.cosmos.acacia.crm.enums.DeliveryCertificateReason;
import com.cosmos.acacia.crm.enums.DeliveryCertificateStatus;
import com.cosmos.acacia.crm.gui.contactbook.AddressListPanel;
import com.cosmos.acacia.crm.gui.contactbook.ContactPersonsListPanel;
import com.cosmos.acacia.crm.gui.contactbook.OrganizationsListPanel;
import com.cosmos.acacia.crm.gui.invoice.InvoiceListPanel;
import com.cosmos.acacia.crm.validation.ValidationException;
import com.cosmos.acacia.gui.AbstractTablePanel;
import com.cosmos.acacia.gui.AcaciaComboBox;
import com.cosmos.acacia.gui.AcaciaLookup;
import com.cosmos.acacia.gui.AcaciaLookupProvider;
import com.cosmos.acacia.gui.BaseEntityPanel;
import com.cosmos.acacia.gui.EntityFormButtonPanel;
import com.cosmos.acacia.util.AcaciaUtils;
import com.cosmos.beansbinding.EntityProperties;
import com.cosmos.swingb.DialogResponse;
import com.cosmos.swingb.JBButton;
import com.cosmos.swingb.JBErrorPane;

/**
 *
 * @author  daniel
 */
public class DeliveryCertificatePanel extends BaseEntityPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9063542129569961080L;

    @EJB
    private DeliveryCertificatesRemote formSession;

    @EJB
    private InvoiceListRemote invoicesBean;

    private DeliveryCertificate entity;

    private DeliveryCertificateAssignment assignment;

    private DeliveryCertificateItemListPanel itemsTablePanel;

    private List<DeliveryCertificateItem> deliveryCertificateItems;

    private Address forwarderBranch;

    private EntityProperties entityProps;

    JBButton deliverButton = null;

    JBButton refreshButton = null;

    private BindingGroup bindingGroup;

    private Binding recipientNameBinding;

    private Binding recipientBranchNameBinding;

    private Binding recipientContactNameBinding;

    public DeliveryCertificatePanel(DeliveryCertificate ds, DataObject parent) {
        super(parent != null ? parent.getDataObjectId() : null);
        this.entity = ds;
        this.assignment = ds.getDocumentAssignment();
        init();
    }

    /** Creates new form DeliveryCertificatePanel */
    public DeliveryCertificatePanel(DeliveryCertificate ds) {
        this(ds, null);
    }

    @Override
    protected void init() {
        initComponents();
        initComponentsCustom();
        super.init();
    }

    private void initComponentsCustom() {
        itemsTablePanel = new DeliveryCertificateItemListPanel(entity.getDeliveryCertificateId());
        itemsTablePanel.setVisible(AbstractTablePanel.Button.SpecialModify);
        itemsTablePanel.setSpecialButtonBehavior(false);
        itemsTablePanel.setSpecialCaption("deliveryCertificateItemsAction.Action.text");
        addNestedFormListener(itemsTablePanel);
        itemsTableHolderPanel.add(itemsTablePanel);
        deliverButton = new JBButton();
        deliverButton.setAction(getContext().getActionMap(this).get("deliver"));
        deliverButton.setText(getResourceMap().getString("deliver.button.text"));
        entityFormButtonPanel1.addButton(deliverButton);
        refreshButton = new JBButton();
        refreshButton.setAction(getContext().getActionMap(this).get("refresh"));
        refreshButton.setText(getResourceMap().getString("refresh.button.text"));
        refreshButton.setEnabled(false);
        entityFormButtonPanel1.addButton(refreshButton);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void performSave(boolean closeAfter) {
        List<DeliveryCertificateItem> items = new ArrayList<DeliveryCertificateItem>(itemsTablePanel.getListData());
        entity = getFormSession().saveDeliveryCertificate(entity, assignment, items);
        setDialogResponse(DialogResponse.SAVE);
        setSelectedValue(entity);
        if (closeAfter) {
            close();
        } else {
            bindingGroup.unbind();
            initData();
        }
    }

    @Override
    public BindingGroup getBindingGroup() {
        if (bindingGroup == null) {
            bindingGroup = new BindingGroup();
        }
        return bindingGroup;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public EntityFormButtonPanel getButtonPanel() {
        return entityFormButtonPanel1;
    }

    @Override
    protected void initData() {
        BindingGroup bg = getBindingGroup();
        if (entity == null) {
            entity = getFormSession().newDeliveryCertificate(getParentDataObjectId());
            assignment = getFormSession().newDeliveryCertificateAssignment();
        }
        entityProps = getFormSession().getDeliveryCertificateEntityProperties();
        EntityProperties assignmentProps = getFormSession().getDeliveryCertificateAssignmentEntityProperties();
        numberTextField.bind(bg, entity, entityProps.getEntityProperty("deliveryCertificateNumber"));
        creationDatePicker.bind(bg, entity, entityProps.getEntityProperty("deliveryCertificateDate"), AcaciaUtils.getShortDateFormat());
        Binding binding1 = reasonComboBox.bind(bg, getFormSession().getReasons(), entity, entityProps.getEntityProperty("deliveryCertificateReason"));
        binding1.addBindingListener(new AbstractBindingListener() {

            @Override
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                if (event.getNewValue() instanceof DbResource) {
                    if (!DeliveryCertificateReason.Invoice.equals(((DbResource) event.getNewValue()).getEnumValue())) {
                        JOptionPane.showMessageDialog(DeliveryCertificatePanel.this, "Not implemented, yet. Right! You here WELL");
                        return;
                    }
                }
            }
        });
        documentsAcaciaLookup.bind(new AcaciaLookupProvider() {

            @Override
            public Object showSelectionControl() {
                return onChooseAssignment();
            }
        }, bg, assignment, assignmentProps.getEntityProperty("documentNumber"), UpdateStrategy.READ_WRITE);
        creatorNameTextField.bind(bg, entity, entityProps.getEntityProperty("creatorName"));
        creatorOrganizationTextField.bind(bg, entity, entityProps.getEntityProperty("creatorOrganizationName"));
        creatorBranchTextField.bind(bg, entity, entityProps.getEntityProperty("creatorBranchName"));
        recipientNameBinding = recipientNameTextField.bind(bg, entity, entityProps.getEntityProperty("recipientName"));
        recipientBranchNameBinding = recipientBranchTextField.bind(bg, entity, entityProps.getEntityProperty("recipientBranchName"));
        recipientContactNameBinding = recipientContactPersonAcaciaLookup.bind(new AcaciaLookupProvider() {

            @Override
            public Object showSelectionControl() {
                return onChooseRecipientContactPerson();
            }
        }, bg, entity, entityProps.getEntityProperty("recipientContact"), UpdateStrategy.READ_WRITE);
        deliveryTypeComboBox.bind(bg, getFormSession().getDeliveryTypes(), entity, entityProps.getEntityProperty("deliveryCertificateMethodType"));
        deliveryTypeComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AcaciaComboBox acb = (AcaciaComboBox) e.getSource();
                DbResource selectedDeliveryType = (DbResource) acb.getSelectedItem();
                if (selectedDeliveryType != null) {
                    boolean isForwarding = selectedDeliveryType.getEnumValue().equals(DeliveryCertificateMethodType.Forwarder);
                    for (Component c : forwarderPanel.getComponents()) {
                        if (c instanceof AcaciaLookup) {
                            ((AcaciaLookup) c).setEnabled(isForwarding);
                        }
                    }
                }
            }
        });
        forwardersLookup.bind(new AcaciaLookupProvider() {

            @Override
            public Object showSelectionControl() {
                return onChooseForwarder();
            }
        }, bg, entity, entityProps.getEntityProperty("forwarder"), "${organizationName}", UpdateStrategy.READ_WRITE);
        forwarderBranchAcaciaLookup.bind(new AcaciaLookupProvider() {

            @Override
            public Object showSelectionControl() {
                return onChooseForwarderBranch();
            }
        }, bg, entity, entityProps.getEntityProperty("forwarderBranch"), "${addressName}", UpdateStrategy.READ_WRITE);
        forwarderContactPersonAcaciaLookup.bind(new AcaciaLookupProvider() {

            @Override
            public Object showSelectionControl() {
                return onChooseForwarderContactPerson();
            }
        }, bg, entity, entityProps.getEntityProperty("forwarderContact"), "${displayName}", UpdateStrategy.READ_WRITE);
        if (DeliveryCertificateStatus.Delivered.equals(entity.getStatus().getEnumValue())) {
            this.setReadonly();
        }
        bg.bind();
    }

    protected Object onChooseRecipientContactPerson() {
        if (entity.getRecipientBranch() == null) {
            return null;
        }
        ContactPersonsListPanel listPanel = new ContactPersonsListPanel(entity.getRecipientBranch().getAddressId());
        DialogResponse dResponse = listPanel.showDialog(this);
        if (DialogResponse.SELECT.equals(dResponse)) {
            ContactPerson selectedContactPerson = (ContactPerson) listPanel.getSelectedRowObject();
            return selectedContactPerson;
        } else {
            return null;
        }
    }

    protected String onChooseAssignment() {
        if (DeliveryCertificateReason.Invoice.equals(entity.getDeliveryCertificateReason().getEnumValue())) {
            UUID invoicesParentId = entity.getCreatorOrganization().getId();
            List<SalesInvoice> invoices = getInvoicesSession().listInvoices(invoicesParentId, false);
            CollectionUtils.filter(invoices, new ValidDeliveryCertificateAssignmentPredicate());
            InvoiceListPanel listPanel = new InvoiceListPanel(invoicesParentId, invoices, false);
            listPanel.setVisibleSelectButtons(true);
            DialogResponse dResponse = listPanel.showDialog(this);
            if (DialogResponse.SELECT.equals(dResponse)) {
                SalesInvoice selectedDocument = (SalesInvoice) listPanel.getSelectedRowObject();
                assignment.setDocumentId(selectedDocument.getId());
                assignment.setDocumentNumber(String.valueOf(selectedDocument.getInvoiceNumber()));
                entity.setRecipient(selectedDocument.getRecipient());
                entity.setRecipientBranch(selectedDocument.getBranch());
                entity.setRecipientContact(selectedDocument.getRecipientContact());
                bindDeliveryCertificateItems(selectedDocument.getInvoiceId());
                bindingGroup.removeBinding(recipientNameBinding);
                recipientNameBinding = recipientNameTextField.bind(bindingGroup, entity, entityProps.getEntityProperty("recipientName"));
                recipientNameBinding.bind();
                bindingGroup.removeBinding(recipientBranchNameBinding);
                recipientBranchNameBinding = recipientBranchTextField.bind(bindingGroup, entity, entityProps.getEntityProperty("recipientBranchName"));
                recipientBranchNameBinding.bind();
                bindingGroup.removeBinding(recipientContactNameBinding);
                recipientContactNameBinding = recipientContactPersonAcaciaLookup.bind(new AcaciaLookupProvider() {

                    @Override
                    public Object showSelectionControl() {
                        return onChooseRecipientContactPerson();
                    }
                }, bindingGroup, entity, entityProps.getEntityProperty("recipientContact"), "${contact.displayName}", UpdateStrategy.READ_WRITE);
                recipientContactNameBinding.bind();
                return assignment.getDocumentNumber();
            } else {
                return null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Not implemented, yet. The only reason document can be an invoice.");
            return null;
        }
    }

    protected void bindDeliveryCertificateItems(UUID invoiceId) {
        List<SalesInvoiceItem> invoiceItems = getInvoicesSession().getInvoiceItems(invoiceId);
        if (invoiceItems != null) {
            deliveryCertificateItems = new java.util.ArrayList<DeliveryCertificateItem>();
            for (SalesInvoiceItem invoiceItem : invoiceItems) {
                DeliveryCertificateItem dci = getFormSession().newDeliveryCertificateItem(invoiceItem);
                deliveryCertificateItems.add(dci);
            }
            itemsTablePanel.refreshList(deliveryCertificateItems);
        }
    }

    protected Object onChooseForwarder() {
        OrganizationsListPanel listPanel = new OrganizationsListPanel(null);
        listPanel.setClassifier(getClassifiersManager().getClassifier(Classifier.ShippingAgent.getClassifierCode()));
        DialogResponse dResponse = listPanel.showDialog(this);
        if (DialogResponse.SELECT.equals(dResponse)) {
            Organization selectedForwarder = (Organization) listPanel.getSelectedRowObject();
            forwarderBranchAcaciaLookup.clearSelectedValue();
            forwarderContactPersonAcaciaLookup.clearSelectedValue();
            return selectedForwarder;
        } else {
            return null;
        }
    }

    protected Object onChooseForwarderBranch() {
        UUID organizationId = entity.getForwarder() != null ? entity.getForwarder().getId() : null;
        AddressListPanel listPanel = new AddressListPanel(organizationId);
        DialogResponse dResponse = listPanel.showDialog(this);
        if (DialogResponse.SELECT.equals(dResponse)) {
            forwarderBranch = (Address) listPanel.getSelectedRowObject();
            forwarderContactPersonAcaciaLookup.clearSelectedValue();
            return forwarderBranch;
        } else {
            return null;
        }
    }

    protected Object onChooseForwarderContactPerson() {
        ContactPersonsListPanel listPanel = new ContactPersonsListPanel(forwarderBranch.getAddressId());
        DialogResponse dResponse = listPanel.showDialog(this);
        if (DialogResponse.SELECT.equals(dResponse)) {
            ContactPerson selectedContactPerson = (ContactPerson) listPanel.getSelectedRowObject();
            return selectedContactPerson.getPerson();
        } else {
            return null;
        }
    }

    @Action
    public void deliver() {
        try {
            List<DeliveryCertificateItem> items = new ArrayList<DeliveryCertificateItem>(itemsTablePanel.getListData());
            setDialogResponse(DialogResponse.SAVE);
            entity = getFormSession().deliverDeliveryCertificate(entity, assignment, items);
            setSelectedValue(entity);
            close();
        } catch (Exception ex) {
            ValidationException ve = extractValidationException(ex);
            if (ve != null) {
                String message = getValidationErrorsMessage(ve);
                refreshButton.setEnabled(true);
                JBErrorPane.showDialog(this, createSaveErrorInfo(message, null));
            } else {
                ex.printStackTrace();
                String basicMessage = getResourceMap().getString("saveAction.Action.error.basicMessage", ex.getMessage());
                JBErrorPane.showDialog(this, createSaveErrorInfo(basicMessage, ex));
            }
        }
    }

    private ErrorInfo createSaveErrorInfo(String basicMessage, Exception ex) {
        ResourceMap resource = getResourceMap();
        String title = resource.getString("saveAction.Action.error.title");
        String detailedMessage = resource.getString("saveAction.Action.error.detailedMessage");
        String category = DeliveryCertificatePanel.class.getName() + ": saveAction.";
        Level errorLevel = Level.WARNING;
        Map<String, String> state = new HashMap<String, String>();
        state.put("deliveryCertificateId", String.valueOf(entity.getDeliveryCertificateId()));
        ErrorInfo errorInfo = new ErrorInfo(title, basicMessage, detailedMessage, category, ex, errorLevel, state);
        return errorInfo;
    }

    @Action
    public void refresh() {
        if (entity.getDeliveryCertificateId() == null) {
            return;
        }
        List<SalesInvoiceItem> invoiceItems = getInvoicesSession().getInvoiceItems(entity.getDocumentAssignment().getDocumentId());
        if (deliveryCertificateItems == null) {
            deliveryCertificateItems = getFormSession().getDeliveryCertificateItems(entity.getDeliveryCertificateId());
        }
        for (DeliveryCertificateItem item : deliveryCertificateItems) {
            for (SalesInvoiceItem invoiceItem : invoiceItems) {
                if (invoiceItem.getInvoiceItemId().equals(item.getReferenceItemId())) {
                    DeliveryCertificateItem dci = getFormSession().newDeliveryCertificateItem(invoiceItem);
                    item.setQuantity(dci.getQuantity());
                }
            }
        }
        itemsTablePanel.refreshList(deliveryCertificateItems);
    }

    @Override
    public void setReadonly() {
        super.setReadonly();
        this.itemsTablePanel.setReadonly();
        this.deliverButton.setEnabled(false);
        this.deliverButton.setVisible(false);
    }

    protected DeliveryCertificatesRemote getFormSession() {
        if (formSession == null) {
            formSession = getBean(DeliveryCertificatesRemote.class);
        }
        return formSession;
    }

    protected InvoiceListRemote getInvoicesSession() {
        if (invoicesBean == null) {
            invoicesBean = getBean(InvoiceListRemote.class);
        }
        return invoicesBean;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        detailsPanel = new com.cosmos.swingb.JBPanel();
        numberLabel = new com.cosmos.swingb.JBLabel();
        numberTextField = new com.cosmos.swingb.JBTextField();
        reasonLabel = new com.cosmos.swingb.JBLabel();
        dateLabel = new com.cosmos.swingb.JBLabel();
        documentLabel = new com.cosmos.swingb.JBLabel();
        reasonComboBox = new com.cosmos.acacia.gui.AcaciaComboBox();
        creationDatePicker = new com.cosmos.swingb.JBDatePicker();
        documentsAcaciaLookup = new com.cosmos.acacia.gui.AcaciaLookup();
        creatorPanel = new com.cosmos.swingb.JBPanel();
        organizationLabel = new com.cosmos.swingb.JBLabel();
        creatorOrganizationTextField = new com.cosmos.swingb.JBTextField();
        supplierBranchLabel = new com.cosmos.swingb.JBLabel();
        creatorBranchTextField = new com.cosmos.swingb.JBTextField();
        supplerEmployeeLabel = new com.cosmos.swingb.JBLabel();
        creatorNameTextField = new com.cosmos.swingb.JBTextField();
        recipientPanel = new com.cosmos.swingb.JBPanel();
        clientNameLabel = new com.cosmos.swingb.JBLabel();
        recipientNameTextField = new com.cosmos.swingb.JBTextField();
        clientBranchLabel = new com.cosmos.swingb.JBLabel();
        recipientBranchTextField = new com.cosmos.swingb.JBTextField();
        clientContactPersonLabel = new com.cosmos.swingb.JBLabel();
        recipientContactPersonAcaciaLookup = new com.cosmos.acacia.gui.AcaciaLookup();
        entityFormButtonPanel1 = new com.cosmos.acacia.gui.EntityFormButtonPanel();
        deliveryMethodPanel = new com.cosmos.swingb.JBPanel();
        deliveryTypeComboBox = new com.cosmos.acacia.gui.AcaciaComboBox();
        forwarderPanel = new com.cosmos.swingb.JBPanel();
        forwardNameLabel = new com.cosmos.swingb.JBLabel();
        forwarderBranchLabel = new com.cosmos.swingb.JBLabel();
        forwarderContactNameLabel = new com.cosmos.swingb.JBLabel();
        forwardersLookup = new com.cosmos.acacia.gui.AcaciaLookup();
        forwarderBranchAcaciaLookup = new com.cosmos.acacia.gui.AcaciaLookup();
        forwarderContactPersonAcaciaLookup = new com.cosmos.acacia.gui.AcaciaLookup();
        itemsTableHolderPanel = new com.cosmos.acacia.gui.TableHolderPanel();
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.cosmos.acacia.crm.gui.AcaciaApplication.class).getContext().getResourceMap(DeliveryCertificatePanel.class);
        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("detailsPanel.border.title")));
        detailsPanel.setName("detailsPanel");
        numberLabel.setText(resourceMap.getString("numberLabel.text"));
        numberLabel.setName("numberLabel");
        numberTextField.setText(resourceMap.getString("numberTextField.text"));
        numberTextField.setName("numberTextField");
        numberTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberTextFieldActionPerformed(evt);
            }
        });
        reasonLabel.setText(resourceMap.getString("reasonLabel.text"));
        reasonLabel.setName("reasonLabel");
        dateLabel.setText(resourceMap.getString("dateLabel.text"));
        dateLabel.setName("dateLabel");
        documentLabel.setText(resourceMap.getString("documentLabel.text"));
        documentLabel.setName("documentLabel");
        reasonComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        reasonComboBox.setName("reasonComboBox");
        creationDatePicker.setEditable(false);
        creationDatePicker.setName("creationDatePicker");
        documentsAcaciaLookup.setName("documentsAcaciaLookup");
        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(detailsPanelLayout.createSequentialGroup().addContainerGap().addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(numberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(reasonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(30, 30, 30).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(reasonComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(numberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(93, 93, 93).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(documentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(documentsAcaciaLookup, 0, 0, Short.MAX_VALUE).addComponent(creationDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)).addContainerGap()));
        detailsPanelLayout.setVerticalGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(detailsPanelLayout.createSequentialGroup().addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(detailsPanelLayout.createSequentialGroup().addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(numberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(reasonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(reasonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(detailsPanelLayout.createSequentialGroup().addGap(1, 1, 1).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(creationDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(documentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(documentsAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(46, Short.MAX_VALUE)));
        creatorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("creatorPanel.border.title")));
        creatorPanel.setName("creatorPanel");
        organizationLabel.setText(resourceMap.getString("organizationLabel.text"));
        organizationLabel.setName("organizationLabel");
        creatorOrganizationTextField.setText(resourceMap.getString("creatorOrganizationTextField.text"));
        creatorOrganizationTextField.setName("creatorOrganizationTextField");
        supplierBranchLabel.setText(resourceMap.getString("supplierBranchLabel.text"));
        supplierBranchLabel.setName("supplierBranchLabel");
        creatorBranchTextField.setText(resourceMap.getString("creatorBranchTextField.text"));
        creatorBranchTextField.setName("creatorBranchTextField");
        supplerEmployeeLabel.setText(resourceMap.getString("supplerEmployeeLabel.text"));
        supplerEmployeeLabel.setName("supplerEmployeeLabel");
        creatorNameTextField.setText(resourceMap.getString("text"));
        creatorNameTextField.setName("");
        javax.swing.GroupLayout creatorPanelLayout = new javax.swing.GroupLayout(creatorPanel);
        creatorPanel.setLayout(creatorPanelLayout);
        creatorPanelLayout.setHorizontalGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(creatorPanelLayout.createSequentialGroup().addContainerGap().addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(creatorPanelLayout.createSequentialGroup().addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(organizationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(supplierBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(creatorBranchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE).addComponent(creatorOrganizationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))).addGroup(creatorPanelLayout.createSequentialGroup().addComponent(supplerEmployeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(creatorNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))).addContainerGap()));
        creatorPanelLayout.setVerticalGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(creatorPanelLayout.createSequentialGroup().addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(organizationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(creatorOrganizationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(supplierBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(creatorBranchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(creatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(supplerEmployeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(creatorNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(40, Short.MAX_VALUE)));
        recipientPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("recipientPanel.border.title")));
        recipientPanel.setName("recipientPanel");
        clientNameLabel.setText(resourceMap.getString("clientNameLabel.text"));
        clientNameLabel.setName("clientNameLabel");
        recipientNameTextField.setText(resourceMap.getString("recipientNameTextField.text"));
        recipientNameTextField.setName("recipientNameTextField");
        clientBranchLabel.setText(resourceMap.getString("clientBranchLabel.text"));
        clientBranchLabel.setName("clientBranchLabel");
        recipientBranchTextField.setText(resourceMap.getString("recipientBranchTextField.text"));
        recipientBranchTextField.setName("recipientBranchTextField");
        clientContactPersonLabel.setText(resourceMap.getString("clientContactPersonLabel.text"));
        clientContactPersonLabel.setName("clientContactPersonLabel");
        recipientContactPersonAcaciaLookup.setName("recipientContactPersonAcaciaLookup");
        javax.swing.GroupLayout recipientPanelLayout = new javax.swing.GroupLayout(recipientPanel);
        recipientPanel.setLayout(recipientPanelLayout);
        recipientPanelLayout.setHorizontalGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(recipientPanelLayout.createSequentialGroup().addContainerGap().addGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(clientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(clientBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(clientContactPersonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE).addGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(recipientBranchTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(recipientNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE).addComponent(recipientContactPersonAcaciaLookup, 0, 0, Short.MAX_VALUE)).addContainerGap()));
        recipientPanelLayout.setVerticalGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(recipientPanelLayout.createSequentialGroup().addGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(clientNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(recipientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(clientBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(recipientBranchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(recipientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(clientContactPersonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(recipientContactPersonAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(38, Short.MAX_VALUE)));
        entityFormButtonPanel1.setName("entityFormButtonPanel1");
        deliveryMethodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("deliveryMethodPanel.border.title")));
        deliveryMethodPanel.setName("deliveryMethodPanel");
        deliveryTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        deliveryTypeComboBox.setName("deliveryTypeComboBox");
        forwarderPanel.setName("forwarderPanel");
        forwardNameLabel.setText(resourceMap.getString("forwardNameLabel.text"));
        forwardNameLabel.setName("forwardNameLabel");
        forwarderBranchLabel.setText(resourceMap.getString("forwarderBranchLabel.text"));
        forwarderBranchLabel.setName("forwarderBranchLabel");
        forwarderContactNameLabel.setText(resourceMap.getString("forwarderContactNameLabel.text"));
        forwarderContactNameLabel.setName("forwarderContactNameLabel");
        forwardersLookup.setName("forwardersLookup");
        forwarderBranchAcaciaLookup.setName("forwarderBranchAcaciaLookup");
        forwarderContactPersonAcaciaLookup.setName("forwarderContactPersonAcaciaLookup");
        javax.swing.GroupLayout forwarderPanelLayout = new javax.swing.GroupLayout(forwarderPanel);
        forwarderPanel.setLayout(forwarderPanelLayout);
        forwarderPanelLayout.setHorizontalGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(forwarderPanelLayout.createSequentialGroup().addContainerGap().addGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(forwardNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderContactNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(36, 36, 36).addGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(forwarderBranchAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwardersLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderContactPersonAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        forwarderPanelLayout.setVerticalGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(forwarderPanelLayout.createSequentialGroup().addContainerGap().addGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(forwarderPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(forwardersLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(forwardNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(forwarderBranchAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderBranchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(forwarderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(forwarderContactPersonAcaciaLookup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderContactNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout deliveryMethodPanelLayout = new javax.swing.GroupLayout(deliveryMethodPanel);
        deliveryMethodPanel.setLayout(deliveryMethodPanelLayout);
        deliveryMethodPanelLayout.setHorizontalGroup(deliveryMethodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(deliveryMethodPanelLayout.createSequentialGroup().addContainerGap().addGroup(deliveryMethodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(deliveryTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(forwarderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(275, Short.MAX_VALUE)));
        deliveryMethodPanelLayout.setVerticalGroup(deliveryMethodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(deliveryMethodPanelLayout.createSequentialGroup().addContainerGap().addComponent(deliveryTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(forwarderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        itemsTableHolderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("itemsTableHolderPanel.border.title")));
        itemsTableHolderPanel.setName("itemsTableHolderPanel");
        javax.swing.GroupLayout itemsTableHolderPanelLayout = new javax.swing.GroupLayout(itemsTableHolderPanel);
        itemsTableHolderPanel.setLayout(itemsTableHolderPanelLayout);
        itemsTableHolderPanelLayout.setHorizontalGroup(itemsTableHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 819, Short.MAX_VALUE));
        itemsTableHolderPanelLayout.setVerticalGroup(itemsTableHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 196, Short.MAX_VALUE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(itemsTableHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(creatorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(recipientPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(entityFormButtonPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE).addComponent(deliveryMethodPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(creatorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(recipientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(itemsTableHolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(deliveryMethodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(entityFormButtonPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void numberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private com.cosmos.swingb.JBLabel clientBranchLabel;

    private com.cosmos.swingb.JBLabel clientContactPersonLabel;

    private com.cosmos.swingb.JBLabel clientNameLabel;

    private com.cosmos.swingb.JBDatePicker creationDatePicker;

    private com.cosmos.swingb.JBTextField creatorBranchTextField;

    private com.cosmos.swingb.JBTextField creatorNameTextField;

    private com.cosmos.swingb.JBTextField creatorOrganizationTextField;

    private com.cosmos.swingb.JBPanel creatorPanel;

    private com.cosmos.swingb.JBLabel dateLabel;

    private com.cosmos.swingb.JBPanel deliveryMethodPanel;

    private com.cosmos.acacia.gui.AcaciaComboBox deliveryTypeComboBox;

    private com.cosmos.swingb.JBPanel detailsPanel;

    private com.cosmos.swingb.JBLabel documentLabel;

    private com.cosmos.acacia.gui.AcaciaLookup documentsAcaciaLookup;

    private com.cosmos.acacia.gui.EntityFormButtonPanel entityFormButtonPanel1;

    private com.cosmos.swingb.JBLabel forwardNameLabel;

    private com.cosmos.acacia.gui.AcaciaLookup forwarderBranchAcaciaLookup;

    private com.cosmos.swingb.JBLabel forwarderBranchLabel;

    private com.cosmos.swingb.JBLabel forwarderContactNameLabel;

    private com.cosmos.acacia.gui.AcaciaLookup forwarderContactPersonAcaciaLookup;

    private com.cosmos.swingb.JBPanel forwarderPanel;

    private com.cosmos.acacia.gui.AcaciaLookup forwardersLookup;

    private com.cosmos.acacia.gui.TableHolderPanel itemsTableHolderPanel;

    private com.cosmos.swingb.JBLabel numberLabel;

    private com.cosmos.swingb.JBTextField numberTextField;

    private com.cosmos.swingb.JBLabel organizationLabel;

    private com.cosmos.acacia.gui.AcaciaComboBox reasonComboBox;

    private com.cosmos.swingb.JBLabel reasonLabel;

    private com.cosmos.swingb.JBTextField recipientBranchTextField;

    private com.cosmos.acacia.gui.AcaciaLookup recipientContactPersonAcaciaLookup;

    private com.cosmos.swingb.JBTextField recipientNameTextField;

    private com.cosmos.swingb.JBPanel recipientPanel;

    private com.cosmos.swingb.JBLabel supplerEmployeeLabel;

    private com.cosmos.swingb.JBLabel supplierBranchLabel;
}
