package ledestin.swing.jdom;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.*;
import java.awt.GridLayout;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;

abstract class AbstractDocumentListener implements DocumentListener {

    public void insertUpdate(DocumentEvent e) {
    }

    ;

    public void removeUpdate(DocumentEvent e) {
    }

    ;

    public void changedUpdate(DocumentEvent e) {
    }

    ;
}

/**
The class implements a map of data from XML (JDOM) source to a number of 
JTextFields.
*/
public class JDOM2SwingMap {

    protected final int READ_ALL = 0;

    protected final int RESTORE_CHANGED = 1;

    /**
	Contains mapping between <code>org.jdom.Element</code>s and 
	<code>JDOM2SwingMap.Entry</code>s
	*/
    private HashMap elements = new HashMap();

    public void setElements(HashMap m) {
        elements = m;
    }

    public HashMap getElements() {
        return elements;
    }

    public static JPanel generateUI(Element e, JDOM2SwingMap map) {
        List list = e.getChildren();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(e.getName()));
        for (int i = 0; i < list.size(); i++) {
            Element el = (Element) list.get(i);
            if (el.getChildren().size() == 0) {
                panel.add(new JLabel(el.getName()));
                JTextField fld = new JTextField(el.getText());
                map.getElements().put(el, new Entry(fld));
                panel.add(fld);
            } else {
                panel.add(generateUI(el, map));
            }
        }
        return panel;
    }

    public void saveDataToJDOM() {
        Iterator it = elements.entrySet().iterator();
        JDOM2SwingMap.Entry entry;
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            entry = (JDOM2SwingMap.Entry) e.getValue();
            if (entry.changed) {
                Element el = (Element) e.getKey();
                el.setText(entry.getComponent().getText());
                entry.changed = false;
            }
        }
    }

    public void readDataFromJDOM() {
        readDataFromJDOM(READ_ALL);
    }

    public void restoreDataFromJDOM() {
        readDataFromJDOM(RESTORE_CHANGED);
    }

    protected void readDataFromJDOM(int option) {
        Iterator it = elements.entrySet().iterator();
        org.jdom.Element el;
        JDOM2SwingMap.Entry entry;
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            el = (org.jdom.Element) e.getKey();
            entry = (JDOM2SwingMap.Entry) e.getValue();
            if (entry.changed || option == READ_ALL) {
                entry.getComponent().setText(el.getText());
                entry.changed = false;
            }
        }
    }

    ;

    public static class Entry implements DocumentListener {

        protected JTextField c;

        boolean changed;

        public Entry(JTextField c) {
            setComponent(c);
        }

        public JTextField getComponent() {
            return c;
        }

        public void setComponent(JTextField c) {
            this.c = c;
            c.getDocument().addDocumentListener(this);
            changed = false;
        }

        public void changedUpdate(DocumentEvent e) {
        }

        ;

        public void insertUpdate(DocumentEvent e) {
            changed = true;
        }

        ;

        public void removeUpdate(DocumentEvent e) {
            changed = true;
        }

        ;
    }

    ;
}

;
