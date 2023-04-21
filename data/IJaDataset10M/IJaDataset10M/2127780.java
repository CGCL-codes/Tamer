package org.gridbus.broker.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.gridbus.broker.gui.model.AttributeTableModel;
import org.gridbus.broker.gui.model.DimensionEditorModel;
import org.gridbus.broker.gui.model.NodeAdapter;
import org.gridbus.broker.gui.util.BaseUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> NodeStructureView </node>
 */
public class NodeStructureView extends JPanel implements TreeSelectionListener {

    private Node current;

    private JTextField nodeField = new JTextField();

    private JTextField parentField = new JTextField();

    private CellEditableTable attrTable = new CellEditableTable(new AttributeTableModel());

    private JList childrenList = new JList(new DefaultListModel());

    private JTextArea valueField = new JTextArea(2, 10);

    public NodeStructureView() {
        this(null);
    }

    public NodeStructureView(Node node) {
        current = node;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void valueChanged(TreeSelectionEvent event) {
        try {
            current = ((NodeAdapter) event.getPath().getLastPathComponent()).getNode();
            if (current.getNodeType() != Node.ATTRIBUTE_NODE) {
                updateDisplay();
            } else {
                current = ((NodeAdapter) event.getPath().getParentPath().getLastPathComponent()).getNode();
                updateDisplay();
            }
        } catch (Exception e) {
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void clear() {
        current = null;
        nodeField.setText("");
        parentField.setText("");
        attrTable.setModel(new AttributeTableModel());
        childrenList.setModel(new DefaultListModel());
        repaint();
    }

    private void init() {
        EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
        BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
        CompoundBorder CB = new CompoundBorder(eb, bb);
        nodeField.setBorder(CB);
        parentField.setBorder(CB);
        attrTable.addMouseListener(new NodeViewerMouseListenerAdapter(this));
        childrenList.addMouseListener(new NodeViewerMouseListenerAdapter(this));
        JPanel northPanel = new JPanel();
        nodeField.setEditable(false);
        parentField.setEditable(false);
        northPanel.setLayout(new GridLayout(2, 4));
        northPanel.setBorder(new CompoundBorder(CB, eb));
        northPanel.add(new JLabel("Node Information"));
        northPanel.add(new JLabel());
        northPanel.add(nodeField);
        northPanel.add(new JLabel());
        northPanel.add(new JLabel("Parent Node"));
        northPanel.add(new JLabel());
        northPanel.add(parentField);
        northPanel.add(new JLabel());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JScrollPane attrScrollPane = new JScrollPane(attrTable);
        attrTable.setPreferredScrollableViewportSize(new Dimension(250, 200));
        JPanel attrPanel = new JPanel();
        attrPanel.setLayout(new BorderLayout());
        attrPanel.add(new JLabel("Attributes"), BorderLayout.NORTH);
        attrPanel.add(attrScrollPane, BorderLayout.CENTER);
        childrenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane childrenScrollPane = new JScrollPane(childrenList);
        JPanel childrenPanel = new JPanel();
        childrenPanel.setLayout(new BorderLayout());
        childrenPanel.add(new JLabel("Children Nodes"), BorderLayout.NORTH);
        childrenPanel.add(childrenScrollPane, BorderLayout.CENTER);
        childrenPanel.setBorder(new CompoundBorder(CB, eb));
        attrPanel.setBorder(new CompoundBorder(CB, eb));
        centerPanel.add(childrenPanel, BorderLayout.NORTH);
        centerPanel.add(attrPanel, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.SOUTH);
    }

    private void updateDisplay() {
        nodeField.setText(current.getNodeName());
        parentField.setText(current.getParentNode().getNodeName());
        if (current.getNodeType() == Node.ELEMENT_NODE) {
            attrTable.setModel(new AttributeTableModel(current));
            attrTable.setDimensionEditorModel(new DimensionEditorModel());
            parseAttributes();
            DefaultListModel newModel = new DefaultListModel();
            NodeList children = current.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                newModel.add(i, new NodeAdapter(children.item(i)));
            }
            childrenList.setModel(newModel);
        } else {
            attrTable.setModel(new AttributeTableModel(null));
            childrenList.setModel(new DefaultListModel());
        }
    }

    private void parseAttributes() {
        DimensionEditorModel editor = new DimensionEditorModel();
        Map attrsMap = BaseUtil.getSupportedAttributes(current.getNodeName(), current.getParentNode());
        Object[] names = attrsMap.keySet().toArray();
        for (int i = 0; i < names.length; i++) {
            int rowIndex = ((AttributeTableModel) attrTable.getModel()).getRowIndexForName(names[i]);
            String[] values = (String[]) attrsMap.get(names[i]);
            setTableEditor(values, rowIndex, 1);
        }
    }

    private void setTableEditor(Object[] data, int row, int col) {
        DimensionEditorModel model = attrTable.getDimensionEditorModel();
        JComboBox cb = new JComboBox(data);
        if (data.length <= 1) {
            cb.setEditable(true);
        }
        model.addEditor(row, col, new DefaultCellEditor(cb));
    }
}

class NodeViewerMouseListenerAdapter extends MouseAdapter {

    private NodeStructureView adaptee;

    public NodeViewerMouseListenerAdapter(NodeStructureView adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.mouseClicked(e);
    }
}
