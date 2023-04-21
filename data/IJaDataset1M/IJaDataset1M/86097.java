package edu.upmc.opi.caBIG.caTIES.cadsr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_JDomUtils;
import edu.upmc.opi.caBIG.caTIES.database.domain.PathologyReport;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.OrderItemImpl;
import gov.nih.nci.cadsr.domain.NonenumeratedValueDomain;
import gov.nih.nci.cadsr.domain.PermissibleValue;

public class CaTIES_CaDSRForm implements FocusListener, TreeSelectionListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(CaTIES_CaDSRForm.class);

    private JScrollPane formScrollPane = null;

    private JPanel submissionFormPanel = null;

    private JPanel formPanel = null;

    private JPanel actionPanel = null;

    private JTree cdeNavigationTree = null;

    private DefaultMutableTreeNode formData = null;

    private Document formDocument = null;

    private Hashtable formWidgetToTreeNodeMap = new Hashtable();

    private Hashtable treeNodeToFormWidgetMap = new Hashtable();

    private CaTIES_CaDSRViewer cadsrViewer;

    public CaTIES_CaDSRForm(CaTIES_CaDSRViewer cadsrViewer, JTree cdeNavigationTree, DefaultMutableTreeNode formData) {
        this.cadsrViewer = cadsrViewer;
        this.cdeNavigationTree = cdeNavigationTree;
        this.cdeNavigationTree.addTreeSelectionListener(this);
        this.formData = formData;
        this.formPanel = new JPanel(new GridBagLayout());
        this.formPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        for (int idx = 0; idx < formData.getChildCount(); idx++) {
            JPanel dataElementForm = buidFormFromDataElement((DefaultMutableTreeNode) formData.getChildAt(idx));
            GridBagConstraints formConstraints = new GridBagConstraints();
            formConstraints.gridx = 0;
            formConstraints.gridy = idx;
            formConstraints.gridwidth = 1;
            formConstraints.gridheight = 1;
            formConstraints.weightx = 1.0;
            formConstraints.weighty = 1.0;
            formConstraints.ipadx = 0;
            formConstraints.ipady = 0;
            formConstraints.anchor = GridBagConstraints.NORTHWEST;
            formConstraints.fill = GridBagConstraints.BOTH;
            formConstraints.insets = new Insets(5, 5, 5, 5);
            formPanel.add(dataElementForm, formConstraints);
            formWidgetToTreeNodeMap.put(dataElementForm, formData);
            treeNodeToFormWidgetMap.put(formData, dataElementForm);
        }
        this.formScrollPane = new JScrollPane(this.formPanel);
        this.actionPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.setActionCommand("Submit");
        submitButton.addActionListener(this);
        this.actionPanel.add(submitButton);
        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("Reset");
        resetButton.addActionListener(this);
        this.actionPanel.add(resetButton);
        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand("Clear");
        clearButton.addActionListener(this);
        this.actionPanel.add(clearButton);
        this.submissionFormPanel = new JPanel(new BorderLayout());
        this.submissionFormPanel.add(this.formScrollPane, BorderLayout.CENTER);
        this.submissionFormPanel.add(this.actionPanel, BorderLayout.SOUTH);
    }

    private JPanel buidFormFromDataElement(DefaultMutableTreeNode dataElementNode) {
        JPanel dataElementForm = new JPanel(new GridBagLayout());
        this.formWidgetToTreeNodeMap.put(dataElementForm, dataElementNode);
        this.treeNodeToFormWidgetMap.put(dataElementNode, dataElementForm);
        dataElementForm.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), getNameFromTreeNode(dataElementNode), TitledBorder.LEFT, TitledBorder.TOP));
        DefaultMutableTreeNode valueDomainNode = (DefaultMutableTreeNode) dataElementNode.getLastChild();
        if (valueDomainNode.getChildCount() > 0) {
            buildCheckBoxForm(dataElementForm, valueDomainNode);
        } else {
            buildTextFieldForm(dataElementForm, valueDomainNode);
        }
        return dataElementForm;
    }

    private void buildCheckBoxForm(JPanel dataElementForm, DefaultMutableTreeNode valueDomainNode) {
        for (int idx = 0; idx < valueDomainNode.getChildCount(); idx++) {
            DefaultMutableTreeNode permissibleValueNode = (DefaultMutableTreeNode) valueDomainNode.getChildAt(idx);
            JCheckBox checkBox = new JCheckBox();
            formWidgetToTreeNodeMap.put(checkBox, permissibleValueNode);
            treeNodeToFormWidgetMap.put(permissibleValueNode, checkBox);
            checkBox.setFocusable(true);
            checkBox.addFocusListener(this);
            GridBagConstraints checkBoxConstraints = new GridBagConstraints();
            checkBoxConstraints.gridx = 0;
            checkBoxConstraints.gridy = idx;
            checkBoxConstraints.gridwidth = 1;
            checkBoxConstraints.gridheight = 1;
            checkBoxConstraints.weightx = 0.0;
            checkBoxConstraints.weighty = 0.0;
            checkBoxConstraints.ipadx = 0;
            checkBoxConstraints.ipady = 0;
            checkBoxConstraints.anchor = GridBagConstraints.NORTHWEST;
            checkBoxConstraints.fill = GridBagConstraints.NONE;
            checkBoxConstraints.insets = new Insets(5, 5, 5, 5);
            dataElementForm.add(checkBox, checkBoxConstraints);
            JLabel checkBoxLabel = new JLabel(getNameFromTreeNode(permissibleValueNode));
            checkBoxLabel.setLabelFor(checkBox);
            GridBagConstraints labelConstraints = new GridBagConstraints();
            labelConstraints.gridx = 1;
            labelConstraints.gridy = idx;
            labelConstraints.gridwidth = 1;
            labelConstraints.gridheight = 1;
            labelConstraints.weightx = 1.0;
            labelConstraints.weighty = 0.0;
            labelConstraints.ipadx = 0;
            labelConstraints.ipady = 0;
            labelConstraints.anchor = GridBagConstraints.NORTHWEST;
            labelConstraints.fill = GridBagConstraints.NONE;
            labelConstraints.insets = new Insets(5, 5, 5, 5);
            dataElementForm.add(checkBoxLabel, labelConstraints);
        }
    }

    private void buildTextFieldForm(JPanel dataElementForm, DefaultMutableTreeNode valueDomainData) {
        JTextField textField = new JTextField();
        formWidgetToTreeNodeMap.put(textField, valueDomainData);
        treeNodeToFormWidgetMap.put(valueDomainData, textField);
        textField.setFocusable(true);
        textField.addFocusListener(this);
        JLabel textFieldLabel = new JLabel(getNameFromTreeNode(valueDomainData));
        textFieldLabel.setLabelFor(textFieldLabel);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 1;
        labelConstraints.gridheight = 1;
        labelConstraints.weightx = 0.0;
        labelConstraints.weighty = 0.0;
        labelConstraints.ipadx = 0;
        labelConstraints.ipady = 0;
        labelConstraints.anchor = GridBagConstraints.NORTHEAST;
        labelConstraints.fill = GridBagConstraints.NONE;
        labelConstraints.insets = new Insets(5, 5, 5, 5);
        dataElementForm.add(textFieldLabel, labelConstraints);
        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        textFieldConstraints.gridwidth = 1;
        textFieldConstraints.gridheight = 1;
        textFieldConstraints.weightx = 1.0;
        textFieldConstraints.weighty = 0.0;
        textFieldConstraints.ipadx = 0;
        textFieldConstraints.ipady = 0;
        textFieldConstraints.anchor = GridBagConstraints.NORTHWEST;
        textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraints.insets = new Insets(5, 5, 5, 5);
        dataElementForm.add(textField, textFieldConstraints);
    }

    private String getNameFromTreeNode(DefaultMutableTreeNode treeNode) {
        CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) treeNode.getUserImplObject();
        String name = resourceData.getName();
        name = name.replaceAll("Value Domain: ", "");
        name = name.replaceAll(" NCI Concept Code", "");
        name = name.replaceAll(" Text", "");
        name = name.replaceAll(" Integer", "");
        return name;
    }

    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        Rectangle widgetBounds = component.getBounds();
        printRect("Widget rect is ", widgetBounds);
        double widgetY = widgetBounds.getY();
        double widgetHeight = widgetBounds.getHeight();
        Container parentContainer = component.getParent();
        Rectangle cdeFormBounds = parentContainer.getBounds();
        printRect("CDE Form rect is ", cdeFormBounds);
        double cdeFormY = cdeFormBounds.getY();
        double centerY = cdeFormY + widgetY + 0.5 * widgetHeight;
        System.out.println("Center-Y = " + centerY);
        double viewPortHeight = this.formScrollPane.getViewport().getHeight();
        System.out.println("Viewport height is " + viewPortHeight);
        double repositionY = Math.max(0.0d, centerY - (0.5 * viewPortHeight));
        Rectangle repositionRect = new Rectangle(0, (int) repositionY, 1, (int) viewPortHeight);
        printRect("Repositioning Rect is ", repositionRect);
        this.formScrollPane.revalidate();
    }

    private Rectangle calculateView() {
        JViewport viewPort = this.formScrollPane.getViewport();
        Rectangle viewRect = viewPort.getViewRect();
        printRect("Viewport rect is ", viewRect);
        double viewPortHeight = viewRect.getHeight();
        double viewPortWidth = viewRect.getWidth();
        Dimension viewDimension = viewPort.getViewSize();
        System.out.println("The view size is " + viewDimension.getWidth() + ", " + viewDimension.getHeight());
        Component view = this.formScrollPane.getViewport().getView();
        System.out.println("Component view from viewPort size is " + view.getWidth() + ", " + view.getHeight());
        Rectangle scrollPaneRect = this.formScrollPane.getViewportBorderBounds();
        printRect("ScrollPane rect is ", scrollPaneRect);
        viewPortHeight = scrollPaneRect.getHeight();
        viewPortWidth = scrollPaneRect.getWidth();
        return scrollPaneRect;
    }

    private void printRect(String message, Rectangle widgetBounds) {
        System.out.println(message + " at (" + widgetBounds.getX() + ", " + widgetBounds.getY() + ", " + widgetBounds.getWidth() + ", " + widgetBounds.getHeight() + ")");
    }

    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
    }

    public void valueChanged(TreeSelectionEvent e) {
        ;
    }

    protected void submit() {
        this.formDocument = new Document(new Element("CAP_CheckListValues"));
        Element capRootNode = this.formDocument.getRootElement();
        CaTIES_CaDSRResourceData formResourceData = (CaTIES_CaDSRResourceData) formData.getUserImplObject();
        String classificationSchemeItemName = formResourceData.getName();
        logger.debug("Submitting based on classificationScheme " + classificationSchemeItemName);
        capRootNode.setAttribute(CaTIES_CaDSR.CLASSIFICATION_SCHEME_ITEM_TAG, classificationSchemeItemName);
        for (int idx = 0; idx < this.formData.getChildCount(); idx++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) this.formData.getChildAt(idx);
            submitRecursively(capRootNode, child);
        }
        String checkListAsString = CaTIES_JDomUtils.convertDocumentToString(this.formDocument, Format.getPrettyFormat());
        logger.debug("\n" + checkListAsString);
        CaTIES_DynamicTree retrospectiveStudyNavigationPanel = this.cadsrViewer.getRetrospectiveStudyNavigationPanel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) retrospectiveStudyNavigationPanel.getTree().getLastSelectedPathComponent();
        if (node == null) {
            JOptionPane.showMessageDialog(this.submissionFormPanel, "Save failed. No Document is selected.", "Save Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) node.getUserImplObject();
        logger.debug("Checking " + resourceData.getDataClassName());
        if (resourceData.getDataClassName().startsWith(OrderItemImpl.class.getName())) {
            saveCheckListAsString(resourceData, checkListAsString);
        }
    }

    private void saveCheckListAsString(CaTIES_CaDSRResourceData resourceData, String checkListAsString) {
        OrderItemImpl orderItem = (OrderItemImpl) resourceData.getUserImplObject();
        logger.debug("Found orderItem to update at " + orderItem.getUuid());
        Session ctrmSession = this.cadsrViewer.getCtrmSession();
        try {
            Transaction tx = ctrmSession.beginTransaction();
            orderItem = (OrderItemImpl) ctrmSession.get(OrderItemImpl.class, orderItem.getId());
            orderItem.setComment(checkListAsString);
            ctrmSession.saveOrUpdate(orderItem);
            tx.commit();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void reset() {
        if (this.formDocument != null) {
            populateForm(this.formDocument);
        } else {
            clear();
        }
    }

    private void clear() {
        for (Iterator keysIterator = this.formWidgetToTreeNodeMap.keySet().iterator(); keysIterator.hasNext(); ) {
            Component formWidget = (Component) keysIterator.next();
            if (formWidget instanceof JCheckBox) {
                ((JCheckBox) formWidget).setSelected(false);
            } else if (formWidget instanceof JTextField) {
                ((JTextField) formWidget).setText("");
            }
        }
    }

    /**
	 * @param ctrmPathologyReport - The CTRM index to a public data store pathology report
	 */
    public PathologyReport getPathologyReport(Session ctrmSession, PathologyReport ctrmPathologyReport) {
        Transaction txPub = ctrmSession.beginTransaction();
        System.out.println("Fetching pathology report uuid = " + ctrmPathologyReport.getUuid());
        System.out.println("The public datastore uuid is " + ctrmPathologyReport.getOriginalId());
        PathologyReport pathologyReportCtrm = (PathologyReport) ctrmSession.createCriteria(PathologyReport.class).add(Expression.eq("originalId", ctrmPathologyReport.getUuid())).uniqueResult();
        txPub.commit();
        return pathologyReportCtrm;
    }

    protected void submitRecursively(Element parent, DefaultMutableTreeNode child) {
        CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) child.getUserImplObject();
        String childClassName = resourceData.getDataClassName();
        Element childElement = null;
        boolean traversing = true;
        if (childClassName.equals(PermissibleValue.class.getName())) {
            childElement = createPermissibleValueNode(child, resourceData);
            traversing = false;
        } else if (childClassName.equals(NonenumeratedValueDomain.class.getName())) {
            childElement = createTextFieldNode(child, resourceData);
            traversing = false;
        } else {
            int lastPeriodIdx = childClassName.lastIndexOf(".");
            String truncatedClassName = childClassName.substring(lastPeriodIdx + 1, childClassName.length());
            childElement = createStructureNode(truncatedClassName, resourceData);
        }
        if (childElement != null) {
            parent.addContent(childElement);
        }
        if (traversing) {
            for (int idx = 0; idx < child.getChildCount(); idx++) {
                submitRecursively(childElement, (DefaultMutableTreeNode) child.getChildAt(idx));
            }
        }
    }

    protected Element createStructureNode(String name, CaTIES_CaDSRResourceData resourceData) {
        Element structureNode = new Element(name);
        String idAsString = resourceData.getId();
        if (idAsString != null) {
            structureNode.setAttribute("id", idAsString);
        }
        String nameAsString = resourceData.getName();
        if (nameAsString != null) {
            structureNode.setAttribute("name", nameAsString);
        }
        return structureNode;
    }

    protected Element createPermissibleValueNode(DefaultMutableTreeNode pvNode, CaTIES_CaDSRResourceData resourceData) {
        Element structureNode = new Element("PermissibleValue");
        structureNode.setAttribute("id", resourceData.getId());
        structureNode.setAttribute("name", resourceData.getName());
        Component component = (Component) this.treeNodeToFormWidgetMap.get(pvNode);
        if (component instanceof JCheckBox) {
            String isSelected = (((JCheckBox) component).isSelected()) ? "true" : "false";
            structureNode.addContent(isSelected);
        }
        return structureNode;
    }

    protected Element createTextFieldNode(DefaultMutableTreeNode textNode, CaTIES_CaDSRResourceData resourceData) {
        Element structureNode = new Element("NonenumeratedValueDomain");
        structureNode.setAttribute("id", resourceData.getId());
        structureNode.setAttribute("name", resourceData.getName());
        Component component = (Component) this.treeNodeToFormWidgetMap.get(textNode);
        if (component instanceof JTextField) {
            String textValue = ((JTextField) component).getText().trim();
            structureNode.addContent(textValue);
        }
        return structureNode;
    }

    public JScrollPane getFormScrollPane() {
        return formScrollPane;
    }

    public void setFormScrollPane(JScrollPane formScrollPane) {
        this.formScrollPane = formScrollPane;
    }

    public JPanel getSubmissionFormPanel() {
        return submissionFormPanel;
    }

    public void setSubmissionFormPanel(JPanel submissionFormPanel) {
        this.submissionFormPanel = submissionFormPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Submit")) {
            submit();
        } else if (e.getActionCommand().equals("Reset")) {
            reset();
        } else if (e.getActionCommand().equals("Clear")) {
            clear();
        }
    }

    public void populateForm(Document document) {
        clear();
        this.formDocument = document;
        Element documentRootElement = document.getRootElement();
        List documentChildren = documentRootElement.getChildren();
        for (int idx = 0; idx < this.formData.getChildCount(); idx++) {
            DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode) this.formData.getChildAt(idx);
            Element childElement = (Element) documentChildren.get(idx);
            populateRecursively(childTreeNode, childElement);
        }
    }

    private void populateRecursively(DefaultMutableTreeNode parentTreeNode, Element parentElement) {
        Component formWidget = (Component) this.treeNodeToFormWidgetMap.get(parentTreeNode);
        boolean traversing = true;
        if (formWidget != null) {
            if (formWidget instanceof JCheckBox) {
                ((JCheckBox) formWidget).setSelected(parentElement.getText().equals("true"));
                traversing = false;
            } else if (formWidget instanceof JTextField) {
                ((JTextField) formWidget).setText(parentElement.getText());
                traversing = false;
            }
        }
        if (traversing) {
            List documentChildren = parentElement.getChildren();
            for (int idx = 0; idx < parentTreeNode.getChildCount(); idx++) {
                DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode) parentTreeNode.getChildAt(idx);
                Element childElement = (Element) documentChildren.get(idx);
                populateRecursively(childTreeNode, childElement);
            }
        }
    }

    public CaTIES_CaDSRViewer getCadsrViewer() {
        return cadsrViewer;
    }

    public void setCadsrViewer(CaTIES_CaDSRViewer cdeViewer) {
        this.cadsrViewer = cdeViewer;
    }

    public Hashtable getFormWidgetToTreeNodeMap() {
        return formWidgetToTreeNodeMap;
    }

    public void setFormWidgetToTreeNodeMap(Hashtable formWidgetToTreeNodeMap) {
        this.formWidgetToTreeNodeMap = formWidgetToTreeNodeMap;
    }

    public Hashtable getTreeNodeToFormWidgetMap() {
        return treeNodeToFormWidgetMap;
    }

    public void setTreeNodeToFormWidgetMap(Hashtable treeNodeToFormWidgetMap) {
        this.treeNodeToFormWidgetMap = treeNodeToFormWidgetMap;
    }
}
