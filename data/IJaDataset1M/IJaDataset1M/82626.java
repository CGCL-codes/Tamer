package surfer.yahoohd.desktop;

import surfer.yahoohd.core.MainPanel;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * created on: 2007-10-12 by tzvetan
 */
public class IndexesPanel extends JPanel implements MainPanel {

    IndexesListModel listModel;

    JList indexesList;

    JTextField textField;

    int selectedIndex = -1;

    volatile Map<String, JTable> tables;

    JTable table;

    public IndexesPanel() {
        listModel = new IndexesListModel();
        setLayout(new BorderLayout());
        indexesList = new JList(listModel);
        indexesList.setPreferredSize(new Dimension(100, 300));
        indexesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(indexesList);
        add(scrollPane, BorderLayout.WEST);
        textField = new JTextField(6);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addIndex = new JButton("Add");
        JButton removeIndex = new JButton("Remove");
        JButton showData = new JButton("Show HD");
        controls.add(textField);
        controls.add(addIndex);
        controls.add(removeIndex);
        controls.add(showData);
        add(controls, BorderLayout.SOUTH);
        textField.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                indexesList.clearSelection();
                indexesList.updateUI();
                selectedIndex = -1;
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        addIndex.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String index = textField.getText();
                if (!index.isEmpty()) {
                    listModel.add(index);
                    indexesList.updateUI();
                    textField.setText("");
                }
            }
        });
        indexesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    selectedIndex = indexesList.getSelectedIndex();
                    textField.setText((String) indexesList.getSelectedValue());
                }
            }
        });
        removeIndex.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selectedIndex > -1) {
                    listModel.remove(selectedIndex);
                    textField.setText("");
                    indexesList.updateUI();
                    selectedIndex = -1;
                } else {
                    listModel.remove(textField.getText());
                    textField.setText("");
                    indexesList.updateUI();
                    selectedIndex = -1;
                }
            }
        });
        showData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String index = (String) listModel.getElementAt(indexesList.getSelectedIndex());
                JTable viewTable;
                if ((viewTable = tables.get(index)) == null) {
                    viewTable = new JTable();
                    tables.put(index, viewTable);
                }
                table = viewTable;
                table.updateUI();
            }
        });
        tables = new HashMap<String, JTable>();
        table = new JTable();
        table.setPreferredSize(new Dimension(400, 300));
        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.EAST);
    }

    public ListModel getSelectedIndexes() {
        return listModel;
    }

    public Map<String, JTable> getTables() {
        return tables;
    }

    public JPanel getPanel() {
        return this;
    }
}
