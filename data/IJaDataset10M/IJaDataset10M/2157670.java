package neon.tools;

import javax.swing.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.border.*;
import org.jdom.Element;

public class ThemeEditor implements ListSelectionListener, ActionListener, MouseListener {

    private JDialog frame;

    private JList list;

    private DefaultListModel model;

    private JTextField typeField, floorField, wallsField, doorsField;

    private JFormattedTextField minField, maxField;

    private DefaultTableModel creatureModel, itemModel, featureModel;

    private JTable creatureTable, itemTable, featureTable;

    public ThemeEditor(JFrame parent) {
        frame = new JDialog(parent, "Theme Editor");
        frame.setPreferredSize(new Dimension(500, 400));
        model = new DefaultListModel();
        list = new JList(model);
        list.addListSelectionListener(this);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setBorder(new TitledBorder("Themes"));
        JPanel props = new JPanel();
        GroupLayout layout = new GroupLayout(props);
        props.setLayout(layout);
        layout.setAutoCreateGaps(true);
        JScrollPane propScroller = new JScrollPane(props);
        propScroller.setBorder(new TitledBorder("Properties"));
        typeField = new JTextField(15);
        floorField = new JTextField(15);
        wallsField = new JTextField(15);
        doorsField = new JTextField(15);
        minField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        maxField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        JLabel typeLabel = new JLabel("Type: ");
        JLabel floorLabel = new JLabel("Floors: ");
        JLabel wallsLabel = new JLabel("Walls: ");
        JLabel doorsLabel = new JLabel("Doors: ");
        JLabel minLabel = new JLabel("Min. size: ");
        JLabel maxLabel = new JLabel("Max. size: ");
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(typeLabel).addComponent(typeField).addComponent(floorLabel).addComponent(floorField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(wallsLabel).addComponent(wallsField).addComponent(doorsLabel).addComponent(doorsField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(minLabel).addComponent(minField).addComponent(maxLabel).addComponent(maxField)));
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(typeLabel).addComponent(wallsLabel).addComponent(minLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(typeField).addComponent(wallsField).addComponent(minField)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(floorLabel).addComponent(doorsLabel).addComponent(maxLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(floorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(doorsField).addComponent(maxField)));
        JTabbedPane stuff = new JTabbedPane();
        String[] columns = { "id", "chance" };
        itemModel = new ThemesTableModel(columns, String.class, Integer.class);
        itemTable = new JTable(itemModel);
        itemTable.setFillsViewportHeight(true);
        itemTable.addMouseListener(this);
        JScrollPane itemScroller = new JScrollPane(itemTable);
        creatureModel = new ThemesTableModel(columns, String.class, Integer.class);
        creatureTable = new JTable(creatureModel);
        creatureTable.setFillsViewportHeight(true);
        creatureTable.addMouseListener(this);
        JScrollPane creatureScroller = new JScrollPane(creatureTable);
        String[] moreColumns = { "id", "type", "size", "chance" };
        featureModel = new ThemesTableModel(moreColumns, String.class, String.class, Integer.class, Integer.class);
        featureTable = new JTable(featureModel);
        featureTable.setFillsViewportHeight(true);
        featureTable.addMouseListener(this);
        TableColumn typeColumn = featureTable.getColumnModel().getColumn(1);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("stain");
        comboBox.addItem("lake");
        comboBox.addItem("patch");
        comboBox.addItem("river");
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
        JScrollPane featureScroller = new JScrollPane(featureTable);
        stuff.add("Features", featureScroller);
        stuff.add("Items", itemScroller);
        stuff.add("Creatures", creatureScroller);
        stuff.setBorder(new TitledBorder("Contents"));
        JPanel center = new JPanel(new BorderLayout());
        center.add(propScroller, BorderLayout.PAGE_START);
        center.add(stuff);
        JPanel content = new JPanel(new BorderLayout());
        content.add(listScroller, BorderLayout.LINE_START);
        content.add(center, BorderLayout.CENTER);
        frame.setContentPane(content);
        JPanel buttons = new JPanel();
        content.add(buttons, BorderLayout.PAGE_END);
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        JButton apply = new JButton("Apply");
        apply.addActionListener(this);
        buttons.add(ok);
        buttons.add(cancel);
        buttons.add(apply);
        list.addMouseListener(this);
    }

    public void show() {
        model.clear();
        for (Map.Entry<String, Element> entry : Editor.getStore().getThemes().entrySet()) {
            Theme theme = new Theme(entry);
            model.addElement(theme);
        }
        frame.pack();
        frame.setVisible(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            Theme previous = null;
            Theme current = null;
            if (list.isSelectedIndex(e.getFirstIndex())) {
                current = (Theme) model.getElementAt(e.getFirstIndex());
                previous = (Theme) model.getElementAt(e.getLastIndex());
            } else if (list.isSelectedIndex(e.getLastIndex())) {
                current = (Theme) model.getElementAt(e.getLastIndex());
                previous = (Theme) model.getElementAt(e.getFirstIndex());
            }
            if (previous != null && previous != current) {
                save(previous.theme);
            }
            if (current != null) {
                load(current.theme);
            }
        }
    }

    private void save(Element theme) {
        theme.removeContent();
        String params = typeField.getText() + ";" + floorField.getText() + ";" + wallsField.getText() + ";" + doorsField.getText();
        theme.setAttribute("type", params);
        theme.setAttribute("min", minField.getText());
        theme.setAttribute("max", maxField.getText());
        Element creatures = new Element("creatures");
        for (Vector data : (Vector<Vector>) creatureModel.getDataVector()) {
            Element creature = new Element(data.get(0).toString());
            creature.setAttribute("n", data.get(1).toString());
            creatures.addContent(creature);
        }
        theme.addContent(creatures);
        Element features = new Element("features");
        for (Vector data : (Vector<Vector>) featureModel.getDataVector()) {
            Element feature = new Element(data.get(1).toString());
            feature.setAttribute("t", data.get(0).toString());
            feature.setAttribute("s", data.get(2).toString());
            feature.setAttribute("n", data.get(3).toString());
            features.addContent(feature);
        }
        theme.addContent(features);
        Element items = new Element("items");
        for (Vector data : (Vector<Vector>) itemModel.getDataVector()) {
            Element item = new Element(data.get(0).toString());
            item.setAttribute("n", data.get(1).toString());
            items.addContent(item);
        }
        theme.addContent(items);
    }

    private void load(Element theme) {
        String[] params = theme.getAttributeValue("type").split(";");
        typeField.setText(params[0]);
        floorField.setText(params[1]);
        wallsField.setText(params[2]);
        doorsField.setText(params[3]);
        minField.setValue(Integer.parseInt(theme.getAttributeValue("min")));
        maxField.setValue(Integer.parseInt(theme.getAttributeValue("max")));
        creatureModel.setNumRows(0);
        featureModel.setNumRows(0);
        itemModel.setNumRows(0);
        for (Element creature : (List<Element>) theme.getChild("creatures").getChildren()) {
            String[] data = { creature.getName(), creature.getAttributeValue("n") };
            creatureModel.insertRow(0, data);
        }
        for (Element item : (List<Element>) theme.getChild("items").getChildren()) {
            String[] data = { item.getName(), item.getAttributeValue("n") };
            itemModel.insertRow(0, data);
        }
        for (Element feature : (List<Element>) theme.getChild("features").getChildren()) {
            String[] data = { feature.getAttributeValue("t"), feature.getName(), feature.getAttributeValue("s"), feature.getAttributeValue("n") };
            featureModel.insertRow(0, data);
        }
    }

    private void saveAll() {
        Editor.getStore().getThemes().clear();
        for (Object object : model.toArray()) {
            Editor.getStore().getThemes().put(object.toString(), ((Theme) object).theme);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Apply")) {
            saveAll();
        } else if (e.getActionCommand().equals("Cancel")) {
            frame.dispose();
        } else if (e.getActionCommand().equals("Ok")) {
            saveAll();
            frame.dispose();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            if (e.getSource().equals(list)) {
                menu.add(new ClickAction("New theme"));
                menu.add(new ClickAction("Delete theme"));
                list.setSelectedIndex(list.locationToIndex(new Point(e.getX(), e.getY())));
            } else if (e.getSource().equals(itemTable)) {
                menu.add(new ClickAction("Add item"));
                menu.add(new ClickAction("Remove item"));
                int row = itemTable.rowAtPoint(e.getPoint());
                itemTable.getSelectionModel().setSelectionInterval(row, row);
            } else if (e.getSource().equals(creatureTable)) {
                menu.add(new ClickAction("Add creature"));
                menu.add(new ClickAction("Remove creature"));
                int row = creatureTable.rowAtPoint(e.getPoint());
                creatureTable.getSelectionModel().setSelectionInterval(row, row);
            } else if (e.getSource().equals(featureTable)) {
                menu.add(new ClickAction("Add feature"));
                menu.add(new ClickAction("Remove feature"));
                int row = featureTable.rowAtPoint(e.getPoint());
                featureTable.getSelectionModel().setSelectionInterval(row, row);
            }
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private class ClickAction extends AbstractAction {

        public ClickAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("New theme")) {
                String s = (String) JOptionPane.showInputDialog(frame, "New theme:", "New theme", JOptionPane.QUESTION_MESSAGE);
                if ((s != null) && (s.length() > 0)) {
                    Theme theme = new Theme(s, new Element("theme"));
                    model.addElement(theme);
                    list.setSelectedValue(s, true);
                }
            } else if (e.getActionCommand().equals("Delete theme")) {
                try {
                    if (list.getSelectedIndex() >= 0) {
                        int index = list.getSelectedIndex();
                        model.remove(index);
                    }
                } catch (ArrayIndexOutOfBoundsException a) {
                }
            } else if (e.getActionCommand().equals("Add item")) {
                Object[] items = Editor.getStore().getItems().keySet().toArray();
                String s = (String) JOptionPane.showInputDialog(frame, "Choose item:", "Add item", JOptionPane.PLAIN_MESSAGE, null, items, "ham");
                if (s != null) {
                    String[] row = { s, "1" };
                    itemModel.addRow(row);
                }
            } else if (e.getActionCommand().equals("Remove item")) {
                itemModel.removeRow(itemTable.getSelectedRow());
            } else if (e.getActionCommand().equals("Add feature")) {
                Object[] terrain = Editor.getStore().getItems().keySet().toArray();
                String s = (String) JOptionPane.showInputDialog(frame, "Choose terrain type:", "Add feature", JOptionPane.PLAIN_MESSAGE, null, terrain, "ham");
                if (s != null) {
                    String[] row = { s, "patch", "1", "1" };
                    featureModel.addRow(row);
                }
            } else if (e.getActionCommand().equals("Remove feature")) {
                featureModel.removeRow(featureTable.getSelectedRow());
            } else if (e.getActionCommand().equals("Add creature")) {
                Object[] creatures = Editor.getStore().getSpecies().keySet().toArray();
                String s = (String) JOptionPane.showInputDialog(frame, "Choose creature:", "Add creature", JOptionPane.PLAIN_MESSAGE, null, creatures, "ham");
                if (s != null) {
                    String[] row = { s, "1" };
                    creatureModel.addRow(row);
                }
            } else if (e.getActionCommand().equals("Remove creature")) {
                creatureModel.removeRow(creatureTable.getSelectedRow());
            }
        }
    }

    private class Theme {

        private String name;

        private Element theme;

        public Theme(Map.Entry<String, Element> entry) {
            name = entry.getKey();
            theme = entry.getValue();
        }

        public Theme(String name, Element theme) {
            this.name = name;
            this.theme = theme;
        }

        public String toString() {
            return name;
        }
    }

    private class ThemesTableModel extends DefaultTableModel {

        private Class<?>[] classes;

        public ThemesTableModel(String[] columns, Class<?>... classes) {
            super(columns, 0);
            this.classes = classes;
        }

        public Class<?> getColumnClass(int i) {
            return classes[i];
        }

        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    }
}
