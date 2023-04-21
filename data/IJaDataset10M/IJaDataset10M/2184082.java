package org.yaoqiang.bpmn.editor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.yaoqiang.bpmn.editor.dialog.panels.ResourceRolePanel;
import org.yaoqiang.bpmn.editor.util.BPMNEditorUtils;
import org.yaoqiang.bpmn.editor.view.BPMNGraph;
import org.yaoqiang.bpmn.model.BPMNModelConstants;
import org.yaoqiang.bpmn.model.BPMNModelUtils;
import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.XMLCollection;
import org.yaoqiang.bpmn.model.elements.XMLComplexElement;
import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.XMLExtensionElement;
import org.yaoqiang.bpmn.model.elements.XMLFactory;
import org.yaoqiang.bpmn.model.elements.XMLTextElement;
import org.yaoqiang.bpmn.model.elements.XMLTextElements;
import org.yaoqiang.bpmn.model.elements.activities.ResourceParameterBinding;
import org.yaoqiang.bpmn.model.elements.activities.ResourceRole;
import org.yaoqiang.bpmn.model.elements.artifacts.Category;
import org.yaoqiang.bpmn.model.elements.artifacts.CategoryValue;
import org.yaoqiang.bpmn.model.elements.artifacts.Group;
import org.yaoqiang.bpmn.model.elements.core.common.FlowElement;
import org.yaoqiang.bpmn.model.elements.core.common.PartnerEntity;
import org.yaoqiang.bpmn.model.elements.core.common.PartnerRole;
import org.yaoqiang.bpmn.model.elements.core.common.ResourceParameter;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;
import org.yaoqiang.bpmn.model.elements.core.foundation.Documentation;
import org.yaoqiang.bpmn.model.elements.core.foundation.RootElements;
import org.yaoqiang.bpmn.model.elements.core.infrastructure.Definitions;
import org.yaoqiang.bpmn.model.elements.core.infrastructure.Import;
import org.yaoqiang.bpmn.model.elements.data.DataAssociation;
import org.yaoqiang.bpmn.model.elements.data.DataInput;
import org.yaoqiang.bpmn.model.elements.data.DataInputAssociations;
import org.yaoqiang.bpmn.model.elements.data.DataObject;
import org.yaoqiang.bpmn.model.elements.data.DataObjectReference;
import org.yaoqiang.bpmn.model.elements.data.DataOutput;
import org.yaoqiang.bpmn.model.elements.data.DataOutputAssociations;
import org.yaoqiang.bpmn.model.elements.data.DataStore;
import org.yaoqiang.bpmn.model.elements.data.DataStoreReference;
import org.yaoqiang.bpmn.model.elements.data.InputOutputSpecification;
import org.yaoqiang.bpmn.model.elements.data.ItemAwareElement;
import org.yaoqiang.bpmn.model.elements.events.CatchEvent;
import org.yaoqiang.bpmn.model.elements.events.Event;
import org.yaoqiang.bpmn.model.elements.events.EventDefinition;
import org.yaoqiang.bpmn.model.elements.events.EventDefinitions;
import org.yaoqiang.bpmn.model.elements.events.ThrowEvent;
import org.yaoqiang.graph.editor.dialog.PanelContainer;
import org.yaoqiang.graph.util.Constants;
import org.yaoqiang.graph.util.Utils;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxResources;

/**
 * XMLTablePanel
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class XMLTablePanel extends XMLPanel {

    private static final long serialVersionUID = 1L;

    private XMLPanel source = this;

    protected String type;

    protected boolean hasToolbar = true;

    protected boolean readonly = false;

    protected Vector<String> columnNames;

    protected List<String> columnsToShow;

    protected JPanel toolbox;

    protected JTable table;

    protected JPopupMenu popup = new JPopupMenu();

    protected LinkedHashMap<String, String> choiceMap = new LinkedHashMap<String, String>();

    public XMLTablePanel(PanelContainer pc, XMLElement owner, String type, String title, List<String> columnsToShow, Object elementsToShow, int width, int height, boolean hasToolbar, boolean readonly) {
        super(pc, owner);
        this.setLayout(new BorderLayout());
        if (title != null) {
            this.setBorder(BorderFactory.createTitledBorder(mxResources.get(title)));
        } else {
            this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        this.type = type;
        this.hasToolbar = hasToolbar;
        this.readonly = readonly;
        this.columnNames = getColumnNames(columnsToShow);
        this.columnsToShow = columnsToShow;
        this.table = createTable();
        setupTable(width, height);
        fillTableContent(elementsToShow);
        if (hasToolbar) {
            add(createToolbar(), BorderLayout.NORTH);
        }
        add(createTablePane(), BorderLayout.CENTER);
    }

    protected JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        if (owner instanceof EventDefinitions || owner instanceof RootElements && type.equals(RootElements.TYPE_EVENT_DEFINITION)) {
            List<String> choices = new ArrayList<String>();
            choices.add(EventDefinitions.TYPE_CANCEL_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_COMPENSATE_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_CONDITIONAL_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_ERROR_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_ESCALATION_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_MESSAGE_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_SIGNAL_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_TERMINATE_EVENT_DEFINITION);
            choices.add(EventDefinitions.TYPE_TIMER_EVENT_DEFINITION);
            toolbar.add(createToolbarPopupButton(choices, popupNewElementAction));
        } else if (owner instanceof RootElements && type.equals(RootElements.TYPE_PARTNER)) {
            List<String> choices = new ArrayList<String>();
            choices.add(RootElements.TYPE_PARTNER_ENTITY);
            choices.add(RootElements.TYPE_PARTNER_ROLE);
            toolbar.add(createToolbarPopupButton(choices, popupNewElementAction));
        } else if (owner instanceof InputOutputSpecification) {
            List<String> choices = new ArrayList<String>();
            choices.add("dataInput");
            choices.add("dataOutput");
            toolbar.add(createToolbarPopupButton(choices, popupNewElementAction));
        } else if (type != null && type.equals("dataAssociations") && !(owner instanceof Event)) {
            List<String> choices = new ArrayList<String>();
            choices.add("dataInputAssociation");
            choices.add("dataOutputAssociation");
            toolbar.add(createToolbarPopupButton(choices, popupNewElementAction));
        } else {
            toolbar.add(createToolbarButton(newElementAction));
        }
        toolbar.add(createToolbarButton(editElementAction));
        toolbar.add(createToolbarButton(deleteElementAction));
        return toolbar;
    }

    protected JScrollPane createTablePane() {
        JScrollPane allItemsPane = new JScrollPane();
        allItemsPane.setViewportView(table);
        return allItemsPane;
    }

    protected JTable createTable() {
        return new XMLSortingTable(this, new Vector<XMLElement>(), columnNames) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int col) {
                return getColumnName(col).length() == 0;
            }
        };
    }

    public boolean setSelectedElement(Object el) {
        int rc = table.getRowCount();
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                if (el == table.getValueAt(i, 0)) {
                    table.setRowSelectionInterval(i, i);
                    JViewport viewport = (JViewport) table.getParent();
                    Rectangle rect = table.getCellRect(i, 0, true);
                    Point pt = viewport.getViewPosition();
                    rect.setLocation(rect.x - pt.x, rect.y - pt.y);
                    viewport.scrollRectToVisible(rect);
                    return true;
                }
            }
        }
        return false;
    }

    public Object getSelectedElement() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            return table.getValueAt(row, 0);
        }
        return null;
    }

    protected void setupTable(int width, int height) {
        TableColumn column;
        int count = table.getColumnModel().getColumnCount();
        column = table.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        for (int i = 1; i < count; i++) {
            column = table.getColumnModel().getColumn(i);
            if (column.getHeaderValue().toString().length() == 0) {
                column.setMinWidth(20);
                column.setMaxWidth(20);
                column.setPreferredWidth(20);
                column.setResizable(false);
            } else if (column.getHeaderValue().toString().equals(mxResources.get("id"))) {
                column.setMinWidth(50);
                column.setPreferredWidth(80);
            } else if (column.getHeaderValue().toString().equals(mxResources.get("isRequired")) || column.getHeaderValue().toString().equals(mxResources.get("isUnlimited")) || column.getHeaderValue().toString().equals(mxResources.get("isCollection")) || column.getHeaderValue().toString().equals(mxResources.get("reference")) || column.getHeaderValue().toString().equals(mxResources.get("references"))) {
                column.setMinWidth(40);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if (column.getHeaderValue().toString().equals(mxResources.get("itemKind"))) {
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if (column.getHeaderValue().toString().equals(mxResources.get("type"))) {
                if ("dataInputOutputs".equals(type)) {
                    column.setMinWidth(80);
                    column.setMaxWidth(80);
                    column.setPreferredWidth(80);
                } else if ("dataAssociations".equals(type)) {
                    column.setMinWidth(145);
                    column.setMaxWidth(145);
                    column.setPreferredWidth(145);
                }
            } else if (column.getHeaderValue().toString().equals(mxResources.get("description"))) {
                column.setMinWidth(200);
                column.setPreferredWidth(200);
            } else if ("namespace".equals(type) && column.getHeaderValue().toString().equals(mxResources.get("name"))) {
                column.setMinWidth(70);
                column.setMaxWidth(120);
                column.setPreferredWidth(80);
            }
        }
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        Dimension tDim = new Dimension(width, height);
        table.setPreferredScrollableViewportSize(new Dimension(tDim));
        table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "edit");
        table.getActionMap().put("edit", editElementAction);
        table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false), "delete");
        table.getActionMap().put("delete", deleteElementAction);
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() > 1) {
                    editElementAction.actionPerformed(null);
                }
            }
        });
    }

    protected Action editElementAction = new AbstractAction("edit") {

        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent ae) {
            Object editElement = getSelectedElement();
            if (editElement != null && hasToolbar) {
                boolean openfile = false;
                String filepath = "";
                if (editElement instanceof Documentation) {
                    String format = ((Documentation) editElement).getTextFormat();
                    if (format.length() != 0 && !format.equals("text/plain")) {
                        filepath = BPMNEditorUtils.getFilePath(((Documentation) editElement).toValue());
                        if (new File(filepath).exists()) {
                            openfile = true;
                        }
                    }
                }
                if (openfile) {
                    try {
                        if (Constants.OS.startsWith("Windows")) {
                            Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + filepath);
                        } else if (Constants.OS.startsWith("Mac OS")) {
                            Runtime.getRuntime().exec("open " + filepath);
                        } else {
                            try {
                                Runtime.getRuntime().exec(new String[] { "gnome-open", filepath });
                            } catch (IOException e1) {
                                try {
                                    Runtime.getRuntime().exec(new String[] { "kfmclient exec", filepath });
                                } catch (IOException e2) {
                                    Runtime.getRuntime().exec(new String[] { "konqueror", filepath });
                                }
                            }
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, editElement);
                    }
                } else {
                    getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, editElement);
                }
            }
        }
    };

    protected Action deleteElementAction = new AbstractAction("delete") {

        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent ae) {
            Object selectElement = getSelectedElement();
            BPMNGraph graph = source.getGraphComponent().getGraph();
            if (selectElement != null && selectElement instanceof XMLComplexElement && hasToolbar) {
                XMLComplexElement deleteElement = (XMLComplexElement) selectElement;
                String item = "";
                if (deleteElement instanceof Import) {
                    item = deleteElement.get("location").toValue();
                } else {
                    item = deleteElement.get("id").toValue();
                    if (deleteElement instanceof DataAssociation && graph.getModel().getCells().containsKey(item)) {
                        JOptionPane.showMessageDialog(null, "Cannot delete Item '" + item + "'", mxResources.get("Warning"), JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Item '" + item + "'?", "Confirm Item Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    XMLCollection parent = (XMLCollection) deleteElement.getParent();
                    if (deleteElement instanceof EventDefinition && parent.getParent() instanceof Event) {
                        ((Event) parent.getParent()).removeEventDefinition(item);
                    } else if (deleteElement instanceof Import) {
                        parent.remove(deleteElement.get("location").toValue());
                    } else {
                        parent.remove(item);
                        if (deleteElement instanceof DataInput) {
                            if (parent.getParent() instanceof InputOutputSpecification) {
                                ((InputOutputSpecification) parent.getParent()).removeDataInputRefs(item);
                            } else {
                                ((ThrowEvent) parent.getParent()).removeDataInputRefs(item);
                            }
                        } else if (deleteElement instanceof DataOutput) {
                            if (parent.getParent() instanceof InputOutputSpecification) {
                                ((InputOutputSpecification) parent.getParent()).removeDataOutputRefs(item);
                            } else {
                                ((CatchEvent) parent.getParent()).removeDataOutputRefs(item);
                            }
                        }
                    }
                    int row = getElementRow(deleteElement);
                    if (row != -1) {
                        removeRow(row);
                        if (deleteElement instanceof Category) {
                            Category category = (Category) deleteElement;
                            for (mxCell group : Utils.getAllGroups(graph)) {
                                Group groupElement = (Group) graph.getBpmnElementMap().get(group.getId());
                                String categoryValueId = groupElement.getCategoryValueRef();
                                if (BPMNModelUtils.getAllCategoryValueIds(category).contains(categoryValueId)) {
                                    group.setValue("");
                                    groupElement.setCategoryValueRef("");
                                }
                            }
                            for (XMLElement catValue : category.getCategoryValueList()) {
                                CategoryValue categoryValue = (CategoryValue) catValue;
                                String categoryValueId = categoryValue.getId();
                                for (XMLElement el : categoryValue.getCategorizedFlowElements()) {
                                    ((FlowElement) el).removeCategoryValueRef(categoryValueId);
                                }
                            }
                        } else if (deleteElement instanceof CategoryValue) {
                            CategoryValue categoryValue = (CategoryValue) deleteElement;
                            String categoryValueId = categoryValue.getId();
                            for (mxCell group : Utils.getAllGroups(graph)) {
                                Group groupElement = (Group) graph.getBpmnElementMap().get(group.getId());
                                if (categoryValueId.equals(groupElement.getCategoryValueRef())) {
                                    group.setValue("");
                                    groupElement.setCategoryValueRef("");
                                }
                            }
                            for (XMLElement el : categoryValue.getCategorizedFlowElements()) {
                                ((FlowElement) el).removeCategoryValueRef(categoryValueId);
                            }
                        } else if (deleteElement instanceof DataStore) {
                            DataStore dataStore = (DataStore) deleteElement;
                            String dataStoreId = dataStore.getId();
                            for (XMLElement dsRef : BPMNModelUtils.getAllDataStoreRefs(graph.getBpmnModel())) {
                                DataStoreReference dataStoreRef = (DataStoreReference) dsRef;
                                if (dataStoreId.equals(dataStoreRef.getDataStoreRef())) {
                                    dataStoreRef.setDataStoreRef("");
                                    ((mxCell) graph.getModel().getCell(dataStoreRef.getId())).setValue("");
                                }
                            }
                        } else if (deleteElement instanceof DataObject) {
                            DataObject dataObject = (DataObject) deleteElement;
                            String dataObjectId = dataObject.getId();
                            for (DataObjectReference dataObjectRef : dataObject.getParent().getDataObjectRefs(dataObjectId)) {
                                dataObjectRef.setDataObjectRef("");
                                dataObjectRef.setDataState("");
                                ((mxCell) graph.getModel().getCell(dataObjectRef.getId())).setValue("");
                            }
                        } else if (deleteElement instanceof ResourceParameterBinding) {
                            if (parent.size() == 0) {
                                ResourceRolePanel panel = (ResourceRolePanel) source.getParent().getParent();
                                panel.getResourceRefPanel().setEnabled(true);
                            }
                        } else if (deleteElement instanceof EventDefinition) {
                            for (XMLElement el : BPMNModelUtils.getAllEvents(deleteElement)) {
                                ((Event) el).removeEventDefinition(deleteElement.get("id").toValue());
                            }
                        }
                        graph.refresh();
                    }
                }
            } else if (selectElement instanceof FlowElement && !readonly) {
                FlowElement deleteElement = (FlowElement) selectElement;
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Item '" + deleteElement.getId() + "'?", "Confirm Item Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    removeRow(getElementRow(deleteElement));
                }
            } else if (selectElement instanceof XMLTextElement && !readonly) {
                XMLTextElement deleteElement = (XMLTextElement) selectElement;
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Item '" + deleteElement.toValue() + "'?", "Confirm Item Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    XMLCollection parent = (XMLCollection) deleteElement.getParent();
                    parent.remove(deleteElement.toValue());
                    removeRow(getElementRow(deleteElement));
                }
            } else if (selectElement instanceof XMLExtensionElement && !readonly) {
                XMLExtensionElement deleteElement = (XMLExtensionElement) selectElement;
                String item = deleteElement.getAttribute("id") == null ? deleteElement.getAttribute("name") == null ? deleteElement.toValue() : deleteElement.getAttribute("name").toValue() : deleteElement.getAttribute("id").toValue();
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Item '" + item + "'?", "Confirm Item Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    XMLExtensionElement parent = (XMLExtensionElement) deleteElement.getParent();
                    parent.removeChildElement(deleteElement);
                    removeRow(getElementRow(deleteElement));
                }
            } else if (selectElement instanceof Map.Entry<?, ?> && !readonly) {
                Map.Entry<?, ?> deleteElement = (Map.Entry<?, ?>) selectElement;
                if (deleteElement.getKey().toString().startsWith(BPMNModelConstants.BPMN_TARGET_MODEL_NS) || BPMNModelConstants.READONLY_ELEMENT.contains(deleteElement.getKey())) {
                    JOptionPane.showMessageDialog(null, "Cannot delete Item '" + deleteElement.getValue() + "'", mxResources.get("Warning"), JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Item '" + deleteElement.getValue() + "'?", "Confirm Item Delete", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    graph.getBpmnModel().getNamespaces().remove(deleteElement.getKey());
                    removeRow(getElementRow(deleteElement));
                }
            }
        }
    };

    protected Action newElementAction = new AbstractAction("new") {

        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent ae) {
            BPMNElementDialog parent = getPanelContainer().getParentDialog();
            if (parent.getParentPanel() != null) {
                getPanelContainer().setModified(true);
            }
            if (type != null && owner instanceof Definitions) {
                if (type.equals("namespace")) {
                    Map<String, String> namespaces = ((Definitions) owner).getNamespaces();
                    namespaces.put("", "");
                    Map.Entry<?, ?> entry = null;
                    for (Map.Entry<?, ?> e : namespaces.entrySet()) {
                        if (e.getKey().toString().length() == 0) {
                            entry = e;
                            break;
                        }
                    }
                    getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, entry);
                    namespaces.remove("");
                }
            } else if (type != null && owner instanceof Event) {
                XMLElement newElement = ((XMLCollection) ((Event) owner).get(type)).generateNewElement();
                getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, newElement);
            } else if (type != null && owner instanceof XMLExtensionElement) {
                XMLExtensionElement newElement = new XMLExtensionElement(getOwner(), type);
                getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, newElement);
            } else {
                if (type != null && owner instanceof XMLFactory) {
                    ((XMLFactory) owner).setType(type);
                }
                XMLElement newElement = ((XMLCollection) owner).generateNewElement();
                getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, newElement);
            }
        }
    };

    protected Action popupNewElementAction = new AbstractAction("new") {

        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() instanceof JButton) {
                JButton btn = (JButton) ae.getSource();
                popup.show(btn.getParent(), btn.getX(), btn.getY() + btn.getHeight());
            } else {
                JMenuItem selected = (JMenuItem) ae.getSource();
                String sel = selected.getText();
                String type = choiceMap.get(sel);
                XMLElement newElement = null;
                if (owner instanceof XMLFactory) {
                    ((XMLFactory) owner).setType(type);
                    newElement = ((XMLCollection) owner).generateNewElement();
                } else if (owner instanceof InputOutputSpecification) {
                    if (type.equals("dataInput")) {
                        newElement = ((InputOutputSpecification) owner).getDataInputs().generateNewElement();
                    } else {
                        newElement = ((InputOutputSpecification) owner).getDataOutputs().generateNewElement();
                    }
                } else if (type.equals("dataInputAssociation")) {
                    newElement = ((DataInputAssociations) ((XMLComplexElement) owner).get("dataInputAssociations")).generateNewElement();
                } else if (type.equals("dataOutputAssociation")) {
                    newElement = ((DataOutputAssociations) ((XMLComplexElement) owner).get("dataOutputAssociations")).generateNewElement();
                } else if (owner instanceof XMLExtensionElement) {
                    newElement = new XMLExtensionElement(getOwner(), type);
                }
                getEditor().createBpmnElementDialog().initDialog().editBPMNElement(source, type, newElement);
            }
        }
    };

    public void fillTableContent(Object elementsToShow) {
        if (elementsToShow instanceof List) {
            for (Object obj : (List<?>) elementsToShow) {
                ((DefaultTableModel) table.getModel()).addRow(getRow(obj));
            }
        } else if (elementsToShow instanceof Map) {
            for (Object obj : ((Map<?, ?>) elementsToShow).entrySet()) {
                ((DefaultTableModel) table.getModel()).addRow(getRow(obj));
            }
        }
    }

    public void selectAllElements(boolean select) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int count = dtm.getRowCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            dtm.setValueAt(new Boolean(select), i, 1);
        }
    }

    public List<XMLElement> getAllElements() {
        List<XMLElement> rowElements = new ArrayList<XMLElement>();
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int count = dtm.getRowCount();
        if (count == 0) {
            return rowElements;
        }
        Vector<?> data = dtm.getDataVector();
        for (int i = 0; i < count; i++) {
            Vector<?> row = (Vector<?>) data.elementAt(i);
            Object obj = row.elementAt(0);
            if (obj instanceof XMLElement) {
                rowElements.add((XMLElement) obj);
            }
        }
        return rowElements;
    }

    public List<XMLElement> getCheckedElements() {
        List<XMLElement> rowElements = new ArrayList<XMLElement>();
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int count = dtm.getRowCount();
        if (count == 0) {
            return rowElements;
        }
        Vector<?> data = dtm.getDataVector();
        for (int i = 0; i < count; i++) {
            Vector<?> row = (Vector<?>) data.elementAt(i);
            Object obj = row.elementAt(1);
            if (obj instanceof Boolean) {
                if (((Boolean) obj).booleanValue()) {
                    rowElements.add((XMLElement) row.elementAt(0));
                }
            }
        }
        return rowElements;
    }

    protected Vector<?> getRow(Object elem) {
        Vector<Object> v = new Vector<Object>();
        if (elem instanceof XMLComplexElement) {
            XMLComplexElement cmel = (XMLComplexElement) elem;
            for (String elName : columnsToShow) {
                XMLElement el = cmel.get(elName);
                if (el != null) {
                    if (elName.equals("sourceRef")) {
                        if (cmel instanceof DataAssociation) {
                            String sourceRef = "";
                            for (XMLElement s : ((XMLTextElements) el).getXMLElements()) {
                                if (sourceRef.length() == 0) {
                                    sourceRef += s.toValue();
                                } else {
                                    sourceRef += ";" + s.toValue();
                                }
                            }
                            v.add(sourceRef);
                        }
                    } else if (elName.equals("parameterRef")) {
                        if (cmel instanceof ResourceParameterBinding) {
                            ResourceParameter p = BPMNModelUtils.getResourceParameter(BPMNModelUtils.getDefinitions(cmel), el.toValue());
                            if (p == null || p.getName().length() == 0) {
                                v.add(new XMLElementView(el, XMLElementView.TOVALUE));
                            } else {
                                v.add(p.getName());
                            }
                        }
                    } else {
                        v.add(new XMLElementView(el, XMLElementView.TOVALUE));
                    }
                } else if (elName.length() == 0) {
                    if (owner instanceof CategoryValue && elem instanceof FlowElement) {
                        XMLPanel targetPanel = getPanelContainer().getParentDialog().getParentPanel();
                        v.add(new Boolean(((XMLTablePanel) targetPanel).getAllElements().contains(elem)));
                    }
                } else if (elName.equals("type")) {
                    if (cmel instanceof ResourceRole) {
                        v.add(mxResources.get(cmel.toName()));
                    } else if (cmel instanceof Documentation) {
                        v.add(((Documentation) cmel).getTextFormat());
                    } else if (cmel instanceof EventDefinition || cmel instanceof PartnerRole || cmel instanceof PartnerEntity || cmel instanceof ItemAwareElement || cmel instanceof DataAssociation) {
                        v.add(mxResources.get(cmel.toName()));
                    } else {
                        v.add(cmel.getClass().getSimpleName());
                    }
                } else if (elName.equals("reference")) {
                    if (cmel instanceof EventDefinition) {
                        v.add(String.valueOf(!((XMLCollection) cmel.getParent()).contains(((EventDefinition) cmel).getId())));
                    }
                } else if (elName.equals("references")) {
                    if (cmel instanceof EventDefinition) {
                        v.add(String.valueOf(BPMNModelUtils.getEventDefinitionRefNumbers(cmel, ((EventDefinition) cmel).getId())));
                    }
                } else if (elName.equals("resourceRef")) {
                    if (cmel instanceof ResourceRole) {
                        v.add(((ResourceRole) cmel).getResourceRef());
                    }
                } else if (elName.equals("value")) {
                    if (cmel instanceof ResourceParameterBinding) {
                        v.add(((ResourceParameterBinding) cmel).getExpressionElement().toValue());
                    }
                } else if (elName.equals("description")) {
                    if (cmel instanceof Documentation) {
                        if (((Documentation) cmel).getTextFormat().equals("text/plain")) {
                            v.add(cmel.toValue());
                        } else {
                            v.add(cmel.toValue());
                        }
                    }
                }
            }
        } else if (elem instanceof XMLTextElement) {
            v.add(((XMLTextElement) elem).toValue());
        } else if (elem instanceof XMLExtensionElement) {
            XMLExtensionElement extEl = (XMLExtensionElement) elem;
            for (String elName : columnsToShow) {
                XMLElement el = extEl.getAttribute(elName);
                if (el == null && !elName.equals("value")) {
                    el = new XMLAttribute(extEl, elName);
                    extEl.addAttribute((XMLAttribute) el);
                }
                if (el != null) {
                    v.add(new XMLElementView(el, XMLElementView.TOVALUE));
                }
            }
        } else if (elem instanceof Map.Entry<?, ?>) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) elem;
            v.add(new XMLElementView(entry.getValue().toString()));
            v.add(new XMLElementView(entry.getKey().toString()));
        }
        v.add(0, elem);
        return v;
    }

    public int getElementRow(Object el) {
        int row = -1;
        for (int i = 0; i < table.getRowCount(); i++) {
            Object toCompare = table.getValueAt(i, 0);
            if (el == toCompare || toCompare instanceof Import && ((Import) toCompare).getLocation().equals(((Import) el).getLocation()) || toCompare instanceof XMLTextElement && ((XMLTextElement) toCompare).toValue().equals(((XMLTextElement) el).toValue()) || toCompare instanceof BaseElement && ((BaseElement) toCompare).getId().equals(((BaseElement) el).getId()) || toCompare instanceof Map.Entry<?, ?> && ((Map.Entry<?, ?>) toCompare).getKey().equals(((Map.Entry<?, ?>) el).getKey())) {
                row = i;
                break;
            }
        }
        return row;
    }

    public void addRow(XMLElement e) {
        int pos = getElementRow(e);
        if (pos == -1) {
            pos = table.getRowCount();
        }
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        Vector<?> v = getRow(e);
        if (pos != table.getRowCount()) {
            dtm.removeRow(pos);
        }
        dtm.insertRow(pos, v);
    }

    public void addRow(BaseElement e, int pos) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        Vector<?> v = getRow(e);
        if (pos != table.getRowCount()) {
            dtm.removeRow(pos);
        }
        dtm.insertRow(pos, v);
    }

    public void removeRow(int row) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.removeRow(row);
    }

    public void removeAllRows() {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        while (table.getRowCount() > 0) {
            dtm.removeRow(0);
        }
    }

    protected Vector<String> getColumnNames(List<String> columnsToShow) {
        Vector<String> cnames = new Vector<String>();
        cnames.add("Element");
        for (String column : columnsToShow) {
            cnames.add(column.length() == 0 ? "" : mxResources.get(column));
        }
        return cnames;
    }

    public JButton createToolbarPopupButton(List<String> choices, Action a) {
        for (String choice : choices) {
            choiceMap.put(mxResources.get(choice), choice);
            JMenuItem mi = popup.add(mxResources.get(choice));
            mi.addActionListener(popupNewElementAction);
        }
        String actionName = (String) a.getValue(Action.NAME);
        JButton b = new JButton(new ImageIcon(XMLTablePanel.class.getResource("/org/yaoqiang/graph/editor/images/new.png")));
        b.setName(actionName);
        b.addActionListener(a);
        b.setToolTipText(mxResources.get(actionName));
        return b;
    }

    public JButton createToolbarButton(Action a) {
        String actionName = (String) a.getValue(Action.NAME);
        ImageIcon curIc = null;
        if ("new".equals(actionName)) {
            curIc = new ImageIcon(XMLTablePanel.class.getResource("/org/yaoqiang/graph/editor/images/new.png"));
        } else if ("edit".equals(actionName)) {
            curIc = new ImageIcon(XMLTablePanel.class.getResource("/org/yaoqiang/graph/editor/images/edit.png"));
        } else if ("delete".equals(actionName)) {
            curIc = new ImageIcon(XMLTablePanel.class.getResource("/org/yaoqiang/graph/editor/images/delete.png"));
        }
        JButton b = new JButton(curIc);
        b.setName(actionName);
        b.addActionListener(a);
        b.setToolTipText(mxResources.get(actionName));
        return b;
    }
}
