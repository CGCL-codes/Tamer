package org.one.stone.soup.xapp.application.grfxML.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.one.stone.soup.grfxML.DataStore;
import org.one.stone.soup.grfxML.DataText;
import org.one.stone.soup.grfxML.DataTitle;
import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.stringhelper.StringArrayHelper;
import org.one.stone.soup.swing.JRootFrame;
import org.one.stone.soup.swing.JTextConsole;
import org.one.stone.soup.xml.XmlDocument;
import org.one.stone.soup.xml.XmlParseException;
import org.one.stone.soup.xml.stream.XmlLoader;

public class GrfxMLSourceConsole extends JRootFrame implements ActionListener {

    private GrfxMLEngine engine;

    private JTextConsole textConsole;

    public GrfxMLSourceConsole(GrfxMLEngine engine) {
        super("Source View", new String[] {});
        this.engine = engine;
        textConsole = new JTextConsole();
        textConsole.setBackground(new Color(0, 40, 0));
        textConsole.setForeground(new Color(50, 250, 50));
        textConsole.setFont(new Font("Courier", Font.BOLD, 14));
        getContentPane().add(textConsole, BorderLayout.NORTH);
        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(this);
        JMenuItem find = new JMenuItem("Find");
        find.addActionListener(this);
        JMenuItem replace = new JMenuItem("Replace");
        replace.addActionListener(this);
        JMenu edit = new JMenu("Edit");
        edit.add(clear);
        edit.add(find);
        edit.add(replace);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(edit);
        setJMenuBar(menuBar);
        this.pack();
        this.setVisible(true);
    }

    public boolean destroy(Object source) {
        setVisible(false);
        dispose();
        return false;
    }

    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand().toLowerCase();
        if (command.equals("clear")) {
            textConsole.clear();
        } else if (command.equals("find")) {
            String selected = textConsole.getSelected();
            if (selected == null) {
                selected = "";
            }
            String find = JOptionPane.showInputDialog(this, "Find", selected);
            textConsole.find(find);
        } else if (command.equals("replace")) {
            String selected = textConsole.getSelected();
            if (selected == null) {
                selected = "";
            }
            String find = JOptionPane.showInputDialog(this, "Find", selected);
            String replace = JOptionPane.showInputDialog(this, "Replace With");
            textConsole.replace(find, replace);
        } else if (command.equals("distinct")) {
            String data = textConsole.getText();
            String[] items = StringArrayHelper.parseFields(data, "\n\r");
            System.out.println(items.length);
            Hashtable hash = new Hashtable();
            for (int loop = 0; loop < items.length; loop++) {
                hash.put(items[loop], items[loop]);
            }
            Object[] itemObjects = hash.keySet().toArray();
            items = new String[itemObjects.length];
            for (int loop = 0; loop < items.length; loop++) {
                items[loop] = itemObjects[loop].toString();
            }
            items = StringArrayHelper.sort(items, StringArrayHelper.ASCENDING);
            System.out.println(items.length);
            data = StringArrayHelper.arrayToString(items, "\n");
            textConsole.setText(data);
        } else if (command.equals("sort ascending")) {
            String data = textConsole.getText();
            String[] items = StringArrayHelper.parseFields(data, "\n\r");
            items = StringArrayHelper.sort(items, StringArrayHelper.ASCENDING);
            data = StringArrayHelper.arrayToString(items, "\n");
            textConsole.setText(data);
        } else if (command.equals("sort descending")) {
            String data = textConsole.getText();
            String[] items = StringArrayHelper.parseFields(data, "\n\r");
            items = StringArrayHelper.sort(items, StringArrayHelper.DESCENDING);
            data = StringArrayHelper.arrayToString(items, "\n");
            textConsole.setText(data);
        } else if (command.equals("sort special")) {
            String data = textConsole.getText();
            String[] rows = StringArrayHelper.parseFields(data, "\n\r");
            StringBuffer buffer = new StringBuffer();
            for (int loop = 0; loop < rows.length; loop++) {
                String[] fields = StringArrayHelper.parseFields(rows[loop], "\t");
                int oldId = Integer.parseInt(fields[1]);
                boolean shouldChange = false;
                if ((oldId & 8) > 0 && (oldId & 4096) > 0) {
                    shouldChange = true;
                }
                if (!fields[1].equals(fields[2]) && shouldChange == false) {
                    buffer.append(loop);
                    buffer.append(" ");
                    buffer.append(rows[loop]);
                    buffer.append("\n");
                }
            }
            textConsole.setText(buffer.toString());
        }
    }

    public void setSource() {
        try {
            XmlDocument doc = XmlLoader.getStandardParser().parseDocument(engine.getAPI().getBase().togrfxML("grfxML"));
            textConsole.setText(doc.toXmlBeautify());
        } catch (XmlParseException e) {
            textConsole.setText(e.getMessage());
        }
    }

    public void setNotes() {
        DataTitle info = engine.getAPI().getBase().getINFO();
        StringBuffer text = new StringBuffer();
        text.append("  Title: ");
        text.append(info.getTitle().getValue());
        text.append("\n  Author: ");
        text.append(info.getAuthor().getValue());
        text.append("\n  Version: ");
        text.append(info.getVersion().getValue());
        text.append("\n\n  Notes:\n");
        DataText notes = info.getNotes();
        for (int loop = 0; loop < notes.size(); loop++) {
            text.append("    ");
            text.append(notes.get(loop).getValue());
            text.append('\n');
        }
        text.append("\n\nPath: ");
        text.append(engine.currentPage);
        textConsole.setText(text.toString());
        textConsole.setEnabled(false);
    }

    public void setStore() {
        DataStore store = engine.getAPI().getStore();
        StringBuffer buffer = new StringBuffer();
        for (int loop = 0; loop < store.size(); loop++) {
            buffer.append(store.get(loop).getId());
            buffer.append("   ");
            buffer.append(store.get(loop).toString());
            buffer.append('\n');
        }
        textConsole.setText(buffer.toString());
    }
}
