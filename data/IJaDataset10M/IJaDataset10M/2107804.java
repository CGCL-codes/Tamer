package org.gridbus.broker.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.gridbus.broker.gui.model.TreeModelDOMAdapter;
import org.gridbus.broker.gui.util.BaseUtil;
import org.gridbus.broker.gui.util.Constants;
import org.w3c.dom.Node;

/**
 * @author Xingchen Chu
 * @version 1.0
 *
 */
public class TextEditor extends JDialog {

    private NotificationTree xmlTree = new NotificationTree();

    private JTextArea valueField = new JTextArea();

    private JComboBox valueBox = new JComboBox();

    private JButton update = new JButton();

    private Node textNode;

    public TextEditor(NotificationTree xmlTree, Node textNode) {
        this.xmlTree = xmlTree;
        this.textNode = textNode;
        setTitle("TextEditor");
        init();
    }

    private void init() {
        if (textNode == null) {
            JOptionPane.showMessageDialog(this, "Can't handler 'null' node");
            dispose();
        }
        getContentPane().setLayout(new BorderLayout());
        JPanel north = new JPanel();
        north.add(new JLabel("Text Value"));
        update.setIcon(BaseUtil.createIcon(BaseUtil.IMAGE_BASE_DIR, "ok", "gif"));
        update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == update) {
                    if (textNode != null && textNode.getNodeType() == Node.TEXT_NODE) {
                        String value = "";
                        if (valueBox.getSelectedItem() != null && !"".equals(valueBox.getSelectedItem().toString().trim())) {
                            value = valueBox.getSelectedItem().toString();
                        } else {
                            value = valueField.getText();
                        }
                        if (value == null || value.trim().equals("")) {
                            JOptionPane.showMessageDialog(update.getRootPane(), "Can't update with empty value");
                        } else {
                            textNode.setNodeValue(value);
                            xmlTree.setModel(new TreeModelDOMAdapter(textNode.getOwnerDocument().getFirstChild()));
                            xmlTree.updateUI();
                            BaseUtil.expand(xmlTree, textNode);
                            dispose();
                        }
                    }
                }
            }
        });
        String[] supportedValues = BaseUtil.getSupportedValue(textNode.getParentNode());
        if (supportedValues != null && supportedValues.length > 0) {
            valueBox.setModel(new DefaultComboBoxModel(supportedValues));
            for (int i = 0; i < supportedValues.length; i++) {
                if (textNode.getNodeValue() == supportedValues[i]) {
                    valueBox.setSelectedItem(supportedValues[i]);
                }
            }
            north.add(valueBox);
            north.add(update);
            setSize(250, 200);
        } else {
            JScrollPane center = new JScrollPane(valueField);
            if (textNode != null) valueField.setText(textNode.getNodeValue());
            getContentPane().add(center, BorderLayout.CENTER);
            JPanel south = new JPanel();
            south.add(update);
            getContentPane().add(south, BorderLayout.SOUTH);
            setSize(400, 300);
        }
        getContentPane().add(north, BorderLayout.NORTH);
        setLocation(200, 200);
    }
}
