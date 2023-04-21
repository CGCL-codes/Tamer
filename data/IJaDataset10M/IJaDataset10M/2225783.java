package org.xmlhammer.gui.output;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.bounce.FormLayout;
import org.xmlhammer.model.project.NodeType;

public class NodePanel extends DetailsPanel {

    private static final long serialVersionUID = 6396657641170177743L;

    private static final Dimension EDITORPANE_SIZE = new Dimension(100, 80);

    private JTextField baseURIField = null;

    private JTextField namespaceURIField = null;

    private JTextField prefixField = null;

    private JTextField localNameField = null;

    private JTextField nodeNameField = null;

    private JTextArea nodeValueField = null;

    private JTextArea textContentField = null;

    public NodePanel() {
        super(new FormLayout(5, 5), "node");
        baseURIField = new JTextField();
        baseURIField.setEditable(false);
        add(new JLabel("Base URI:"), LABEL_CONSTRAINTS);
        add(baseURIField, FormLayout.RIGHT_FILL);
        add(Box.createVerticalStrut(10), FormLayout.FULL);
        prefixField = new JTextField();
        prefixField.setEditable(false);
        add(new JLabel("Prefix:"), LABEL_CONSTRAINTS);
        add(prefixField, FormLayout.RIGHT_FILL);
        namespaceURIField = new JTextField();
        namespaceURIField.setEditable(false);
        add(new JLabel("Namespace URI:"), LABEL_CONSTRAINTS);
        add(namespaceURIField, FormLayout.RIGHT_FILL);
        add(Box.createVerticalStrut(10), FormLayout.FULL);
        localNameField = new JTextField();
        localNameField.setEditable(false);
        add(new JLabel("Local Name:"), LABEL_CONSTRAINTS);
        add(localNameField, FormLayout.RIGHT_FILL);
        nodeNameField = new JTextField();
        nodeNameField.setEditable(false);
        add(new JLabel("Node Name:"), LABEL_CONSTRAINTS);
        add(nodeNameField, FormLayout.RIGHT_FILL);
        nodeValueField = new JTextArea();
        nodeValueField.setEditable(false);
        nodeValueField.setLineWrap(false);
        add(new JLabel("Node Value:"), LABEL_CONSTRAINTS);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(nodeValueField, BorderLayout.CENTER);
        JScrollPane scroller = new JScrollPane(panel);
        scroller.setPreferredSize(EDITORPANE_SIZE);
        add(scroller, FormLayout.RIGHT_FILL);
        textContentField = new JTextArea();
        textContentField.setEditable(false);
        textContentField.setLineWrap(false);
        add(new JLabel("Text Content:"), LABEL_CONSTRAINTS);
        panel = new JPanel(new BorderLayout());
        panel.add(textContentField, BorderLayout.CENTER);
        scroller = new JScrollPane(panel);
        scroller.setPreferredSize(EDITORPANE_SIZE);
        add(scroller, FormLayout.RIGHT_FILL);
    }

    public void setNode(NodeType node) {
        baseURIField.setText(node.getBaseURI());
        baseURIField.setCaretPosition(0);
        prefixField.setText(node.getPrefix());
        prefixField.setCaretPosition(0);
        namespaceURIField.setText(node.getNamespaceURI());
        namespaceURIField.setCaretPosition(0);
        localNameField.setText(node.getLocalName());
        localNameField.setCaretPosition(0);
        nodeNameField.setText(node.getNodeName());
        nodeNameField.setCaretPosition(0);
        nodeValueField.setText(node.getNodeValue());
        nodeValueField.setCaretPosition(0);
        textContentField.setText(node.getTextContent());
        textContentField.setCaretPosition(0);
    }
}
