package neon.tools.objects;

import java.awt.event.*;
import javax.swing.*;
import neon.tools.Editor;
import org.jdom.Element;
import java.awt.BorderLayout;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class LevelSpellEditor implements ObjectEditor, MouseListener {

    private JDialog frame;

    private Element data;

    private JTable table;

    private DefaultTableModel model;

    public LevelSpellEditor(JFrame parent, Element data) {
        frame = new JDialog(parent, "Leveled Spell Editor: " + data.getAttributeValue("id"));
        JPanel content = new JPanel(new BorderLayout());
        this.data = data;
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
        Object[][] list = initProps();
        String[] columns = { "id", "level" };
        model = new DefaultTableModel(list, columns);
        table = new JTable(model) {

            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return vColIndex == 1;
            }
        };
        table.addMouseListener(this);
        table.getTableHeader().addMouseListener(this);
        JScrollPane scroller = new JScrollPane(table);
        content.add(scroller, BorderLayout.CENTER);
        frame.setContentPane(content);
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    private void save() {
        data.removeChildren("spell");
        for (int i = 0; i < table.getModel().getRowCount(); i++) {
            Element spell = new Element("spell");
            spell.setAttribute("id", (String) table.getModel().getValueAt(i, 0));
            spell.setAttribute("l", (String) table.getModel().getValueAt(i, 1));
            data.addContent(spell);
        }
    }

    private Object[][] initProps() {
        if (data.getName() == "newt") {
            createDefault();
        }
        List<Element> elements = data.getChildren();
        Object[][] list = new Object[elements.size()][2];
        for (int i = 0; i < elements.size(); i++) {
            list[i][0] = elements.get(i).getAttributeValue("id");
            list[i][1] = elements.get(i).getAttributeValue("l");
        }
        return list;
    }

    public void createDefault() {
        data.setName("list");
    }

    public void actionPerformed(ActionEvent e) {
        if ("Ok".equals(e.getActionCommand())) {
            save();
            frame.dispose();
        } else if ("Cancel".equals(e.getActionCommand())) {
            frame.dispose();
        } else if ("Apply".equals(e.getActionCommand())) {
            save();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            int rowNumber = table.rowAtPoint(e.getPoint());
            ListSelectionModel model = table.getSelectionModel();
            model.setSelectionInterval(rowNumber, rowNumber);
            JPopupMenu menu = new JPopupMenu();
            menu.add(new ClickAction("Delete spell"));
            menu.add(new ClickAction("Add spell"));
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public class ClickAction extends AbstractAction {

        public ClickAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Add spell")) {
                ArrayList<String> items = new ArrayList<String>();
                for (Element item : Editor.getStore().getSpells().values()) {
                    items.add(item.getAttributeValue("id"));
                }
                String s = (String) JOptionPane.showInputDialog(neon.tools.Editor.getFrame(), "Add spell:", "Add spell", JOptionPane.PLAIN_MESSAGE, null, items.toArray(), 0);
                if ((s != null) && (s.length() > 0)) {
                    String[] item = { s, "0" };
                    model.addRow(item);
                }
            } else if (e.getActionCommand().equals("Delete spell")) {
                model.removeRow(table.getSelectedRow());
            }
        }
    }
}
