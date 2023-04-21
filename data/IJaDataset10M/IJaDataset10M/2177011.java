package org.outerj.pollo.xmleditor;

import org.outerj.pollo.DomConnected;
import org.outerj.pollo.gui.FocusHighlightComponent;
import org.outerj.pollo.gui.SemiBevelBorder;
import org.outerj.pollo.xmleditor.displayspec.ElementSpec;
import org.outerj.pollo.xmleditor.displayspec.IDisplaySpecification;
import org.outerj.pollo.xmleditor.model.XmlModel;
import org.outerj.pollo.xmleditor.schema.ElementSchema;
import org.outerj.pollo.xmleditor.schema.ISchema;
import org.outerj.pollo.xmleditor.util.DomUtils;
import org.outerj.pollo.xmleditor.util.QuickSort;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;

public class NodeInsertionPanel extends JPanel implements DomConnected {

    protected XmlEditorPanel xmlEditorPanel;

    protected ISchema schema;

    protected IDisplaySpecification displaySpec;

    protected Object[] emptyArray = new Object[0];

    protected InsertUnlistedElement insertUnlistedElement = new InsertUnlistedElement();

    protected QuickSort quickSort = new QuickSort();

    private static final Icon textIcon = IconManager.getIcon("org/outerj/pollo/resource/font.gif");

    protected NodeInsertionList insertBeforeList;

    protected NodeInsertionList insertAfterList;

    protected NodeInsertionList insertInsideList;

    private static Font labelFont = new Font("Dialog", 0, 10);

    public NodeInsertionPanel(XmlEditorPanel xmlEditorPanel) {
        this.xmlEditorPanel = xmlEditorPanel;
        this.schema = xmlEditorPanel.getSchema();
        XmlEditor xmlEditor = xmlEditorPanel.getXmlEditor();
        this.displaySpec = xmlEditor.getDisplaySpec();
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new BorderLayout());
        Box box = new Box(BoxLayout.Y_AXIS);
        Dimension dimension;
        JComponent title = createTitleLabel("Insert before:");
        box.add(title);
        insertBeforeList = new NodeInsertionList(NodeInsertionList.MODE_INSERT_BEFORE);
        xmlEditor.getSelectionInfo().addListener(insertBeforeList);
        insertBeforeList.setCellRenderer(new ElementSpecCellRenderer());
        insertBeforeList.addFocusListener(new FocusHighlightComponent(title));
        JScrollPane scrollPane1 = new JScrollPane(insertBeforeList);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder());
        dimension = insertBeforeList.getPreferredSize();
        dimension.width = Integer.MAX_VALUE;
        insertBeforeList.setMaximumSize(dimension);
        box.add(scrollPane1);
        title = createTitleLabel("Insert after:");
        box.add(title);
        insertAfterList = new NodeInsertionList(NodeInsertionList.MODE_INSERT_AFTER);
        xmlEditor.getSelectionInfo().addListener(insertAfterList);
        insertAfterList.setCellRenderer(new ElementSpecCellRenderer());
        insertAfterList.addFocusListener(new FocusHighlightComponent(title));
        JScrollPane scrollPane2 = new JScrollPane(insertAfterList);
        scrollPane2.setBorder(BorderFactory.createEmptyBorder());
        dimension = insertAfterList.getPreferredSize();
        dimension.width = Integer.MAX_VALUE;
        insertAfterList.setMaximumSize(dimension);
        box.add(scrollPane2);
        title = createTitleLabel("Append child:");
        box.add(title);
        insertInsideList = new NodeInsertionList(NodeInsertionList.MODE_INSERT_INSIDE);
        xmlEditor.getSelectionInfo().addListener(insertInsideList);
        insertInsideList.setCellRenderer(new ElementSpecCellRenderer());
        insertInsideList.addFocusListener(new FocusHighlightComponent(title));
        JScrollPane scrollPane3 = new JScrollPane(insertInsideList);
        scrollPane3.setBorder(BorderFactory.createEmptyBorder());
        dimension = insertInsideList.getPreferredSize();
        dimension.width = Integer.MAX_VALUE;
        insertInsideList.setMaximumSize(dimension);
        box.add(scrollPane3);
        add(box, BorderLayout.CENTER);
    }

    private final JComponent createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        JPanel panel = new JPanel();
        Dimension dimension = panel.getPreferredSize();
        dimension.width = Integer.MAX_VALUE;
        panel.setMaximumSize(dimension);
        panel.setBorder(SemiBevelBorder.getInstance());
        panel.setLayout(new BorderLayout(0, 0));
        panel.add(label, BorderLayout.CENTER);
        panel.setOpaque(true);
        return panel;
    }

    public void activateInsertBefore() {
        if (insertBeforeList.getSelectedIndex() == -1) {
            insertBeforeList.setSelectedIndex(0);
        }
        insertBeforeList.requestFocus();
    }

    public void activateInsertAfter() {
        if (insertAfterList.getSelectedIndex() == -1) {
            insertAfterList.setSelectedIndex(0);
        }
        insertAfterList.requestFocus();
    }

    public void activateInsertInside() {
        if (insertInsideList.getSelectedIndex() == -1) {
            insertInsideList.setSelectedIndex(0);
        }
        insertInsideList.requestFocus();
    }

    public class NodeInsertionList extends JList implements SelectionListener {

        public static final int MODE_INSERT_BEFORE = 1;

        public static final int MODE_INSERT_AFTER = 2;

        public static final int MODE_INSERT_INSIDE = 3;

        protected int mode;

        protected Node node;

        protected InsertNodeAction insertNodeAction = new InsertNodeAction();

        public NodeInsertionList(int mode) {
            this.mode = mode;
            this.addMouseListener(new DoubleClickListener());
            InputMap inputMap = getInputMap();
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selected");
            inputMap.put(KeyStroke.getKeyStroke('j'), "select-next");
            inputMap.put(KeyStroke.getKeyStroke('k'), "select-previous");
            ActionMap actionMap = getActionMap();
            actionMap.put("selected", insertNodeAction);
            actionMap.put("select-next", new AbstractAction() {

                public void actionPerformed(ActionEvent event) {
                    int index = getSelectedIndex();
                    if (index < getModel().getSize() - 1) {
                        setSelectedIndex(index + 1);
                        ensureIndexIsVisible(index + 1);
                    }
                }
            });
            actionMap.put("select-previous", new AbstractAction() {

                public void actionPerformed(ActionEvent event) {
                    int index = getSelectedIndex();
                    if (index > 0) {
                        setSelectedIndex(index - 1);
                        ensureIndexIsVisible(index - 1);
                    }
                }
            });
        }

        public void nodeSelected(Node node) {
            setEnabled(true);
            this.node = node;
            switch(mode) {
                case MODE_INSERT_BEFORE:
                case MODE_INSERT_AFTER:
                    {
                        if (node == xmlEditorPanel.getXmlEditor().getRootElement() || node.getParentNode() instanceof Document) {
                            this.setListData(emptyArray);
                        } else {
                            Element parentElement = (Element) node.getParentNode();
                            Collection subElementsList = schema.getAllowedSubElements(parentElement);
                            Collection subtexts = schema.getAllowedSubTexts((Element) node.getParentNode());
                            Iterator subElementsIt = subElementsList.iterator();
                            Object[] data = new Object[subElementsList.size() + subtexts.size() + 1];
                            int i = 1;
                            while (subElementsIt.hasNext()) {
                                ElementSchema.SubElement subElement = (ElementSchema.SubElement) subElementsIt.next();
                                data[i] = displaySpec.getElementSpec(subElement.namespaceURI, subElement.localName, parentElement);
                                i++;
                            }
                            if (subtexts.size() > 0) {
                                Iterator subTextIt = subtexts.iterator();
                                while (subTextIt.hasNext()) {
                                    data[i] = subTextIt.next();
                                    i++;
                                }
                            }
                            data[0] = insertUnlistedElement;
                            quickSort.sortPartial(data, 1, subElementsList.size());
                            quickSort.sortPartial(data, subElementsList.size() + 1);
                            this.setListData(data);
                        }
                        break;
                    }
                case MODE_INSERT_INSIDE:
                    {
                        if (node instanceof Element) {
                            Collection subElementsList = schema.getAllowedSubElements((Element) node);
                            Collection subtexts = schema.getAllowedSubTexts((Element) node);
                            Iterator subElementsIt = subElementsList.iterator();
                            Object[] data = new Object[subElementsList.size() + subtexts.size() + 1];
                            int i = 1;
                            while (subElementsIt.hasNext()) {
                                ElementSchema.SubElement subElement = (ElementSchema.SubElement) subElementsIt.next();
                                data[i] = displaySpec.getElementSpec(subElement.namespaceURI, subElement.localName, (Element) node);
                                i++;
                            }
                            if (subtexts.size() > 0) {
                                Iterator subTextIt = subtexts.iterator();
                                while (subTextIt.hasNext()) {
                                    data[i] = subTextIt.next();
                                    i++;
                                }
                            }
                            data[0] = insertUnlistedElement;
                            quickSort.sortPartial(data, 1, subElementsList.size());
                            quickSort.sortPartial(data, subElementsList.size() + 1);
                            this.setListData(data);
                        } else if (node instanceof Document) {
                            Object data[] = { insertUnlistedElement };
                            this.setListData(data);
                        } else {
                            this.setListData(emptyArray);
                        }
                        break;
                    }
            }
        }

        public void nodeUnselected(Node node) {
            setEnabled(false);
        }

        public class DoubleClickListener extends MouseAdapter {

            public void mouseClicked(MouseEvent e) {
                if (!isEnabled()) return;
                if (e.getClickCount() == 2) {
                    int index = locationToIndex(e.getPoint());
                    if (index == -1) return;
                    insertNodeAction.actionPerformed(null);
                }
            }
        }

        public class InsertNodeAction extends AbstractAction {

            public void actionPerformed(ActionEvent event) {
                XmlModel xmlModel = xmlEditorPanel.getXmlModel();
                Node selectedNode = xmlEditorPanel.getXmlEditor().getSelectedNode();
                Object selectedItem = getModel().getElementAt(getSelectedIndex());
                String localName = null;
                String namespaceURI = null;
                String prefix = null;
                Node newNode = null;
                if (selectedItem instanceof ElementSpec) {
                    ElementSpec elementSpec = (ElementSpec) getModel().getElementAt(getSelectedIndex());
                    localName = elementSpec.localName;
                    namespaceURI = elementSpec.nsUri;
                } else if (selectedItem instanceof InsertUnlistedElement) {
                    String elementName = JOptionPane.showInputDialog(getTopLevelAncestor(), "Please enter the element name (optionally qualified with a namespace prefix):", "New element", JOptionPane.QUESTION_MESSAGE);
                    if (elementName == null || elementName.trim().equals("")) {
                        return;
                    }
                    if (!org.apache.xerces.dom.DocumentImpl.isXMLName(elementName)) {
                        JOptionPane.showMessageDialog(getTopLevelAncestor(), "That is not a valid XML element name.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int prefixpos = elementName.indexOf(":");
                    if (prefixpos != -1) {
                        prefix = elementName.substring(0, prefixpos);
                        localName = elementName.substring(prefixpos + 1, elementName.length());
                    } else {
                        localName = elementName;
                    }
                } else if (selectedItem instanceof String) {
                    newNode = xmlModel.getDocument().createTextNode((String) selectedItem);
                }
                Element namespaceSearchNode = null;
                if (!(selectedNode instanceof Document)) {
                    switch(mode) {
                        case MODE_INSERT_BEFORE:
                        case MODE_INSERT_AFTER:
                            namespaceSearchNode = (Element) selectedNode.getParentNode();
                            break;
                        case MODE_INSERT_INSIDE:
                            namespaceSearchNode = (Element) selectedNode;
                    }
                }
                if (selectedItem instanceof ElementSpec && namespaceURI != null && namespaceURI.length() > 0) {
                    prefix = xmlModel.findPrefixForNamespace(namespaceSearchNode, namespaceURI);
                    if (prefix == null) {
                        JOptionPane.showMessageDialog(getTopLevelAncestor(), "No prefix declaration found for namespace '" + namespaceURI + "'");
                        return;
                    }
                    newNode = xmlModel.getDocument().createElementNS(namespaceURI, DomUtils.getQName(prefix, localName));
                } else if (selectedItem instanceof InsertUnlistedElement) {
                    if (prefix != null) {
                        namespaceURI = xmlModel.findNamespaceForPrefix(namespaceSearchNode, prefix);
                        if (namespaceURI == null && !prefix.equals("xmlns")) {
                            JOptionPane.showMessageDialog(getTopLevelAncestor(), "No namespace declaration found for namespace prefix " + prefix + ". Element will not be added.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        namespaceURI = xmlModel.findDefaultNamespace(namespaceSearchNode);
                    }
                    newNode = xmlModel.getDocument().createElementNS(namespaceURI, DomUtils.getQName(prefix, localName));
                } else if (selectedItem instanceof String) {
                } else {
                    newNode = xmlModel.getDocument().createElementNS(null, localName);
                }
                switch(mode) {
                    case MODE_INSERT_BEFORE:
                        selectedNode.getParentNode().insertBefore(newNode, selectedNode);
                        break;
                    case MODE_INSERT_AFTER:
                        Node nextSibling = selectedNode.getNextSibling();
                        if (nextSibling != null) selectedNode.getParentNode().insertBefore(newNode, nextSibling); else selectedNode.getParentNode().appendChild(newNode);
                        break;
                    case MODE_INSERT_INSIDE:
                        selectedNode.appendChild(newNode);
                        break;
                }
                xmlEditorPanel.getXmlEditor().requestFocus();
            }
        }
    }

    public class ElementSpecCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof ElementSpec) {
                ElementSpec elementSpec = (ElementSpec) value;
                setText(elementSpec.getLabel());
                setIcon(elementSpec.icon);
                setFont(list.getFont());
            } else if (value instanceof InsertUnlistedElement) {
                setText(value.toString());
                setFont(list.getFont());
                setIcon(null);
            } else if (value instanceof String) {
                if (value.equals("")) {
                    Font font = list.getFont().deriveFont(Font.ITALIC);
                    setFont(font);
                    setText("(text)");
                } else {
                    setText((String) value);
                    setFont(list.getFont());
                }
                setIcon(textIcon);
            } else {
                System.out.println("[NodeInsertionPanel] Unexpected value: " + value);
            }
            setOpaque(true);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            return this;
        }
    }

    public class InsertUnlistedElement {

        public String toString() {
            return "Insert unlisted element...";
        }
    }

    public void disconnectFromDom() {
    }

    public void reconnectToDom() {
        insertBeforeList.setEnabled(false);
        insertAfterList.setEnabled(false);
        insertInsideList.setEnabled(false);
    }
}
