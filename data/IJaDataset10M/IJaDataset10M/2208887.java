package org.kubiki.gui;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import org.kubiki.xml.*;
import org.kubiki.base.*;
import org.kubiki.gui.*;
import org.kubiki.ide.*;
import org.kubiki.openoffice.*;
import org.kubiki.database.*;

public class PropertyWindow extends BasicFrame implements MouseListener {

    AbstractApplication aa;

    BasicClass o;

    public JButton cancelButton, saveButton, addButton, deleteButton;

    JTabbedPane tabs = null;

    RefreshableComponent rf = null;

    JScrollPane scp;

    int mode = 1;

    BasicPanel p;

    int width;

    BasicPanel[] subelements;

    Recordset rs;

    public PropertyWindow(BasicClass o) {
        this.aa = aa;
        this.o = o;
        init();
    }

    public PropertyWindow(BasicClass o, AbstractApplication aa) {
        this.aa = aa;
        this.o = o;
        init();
    }

    public PropertyWindow(BasicClass o, AbstractApplication aa, RefreshableComponent rf) {
        this.aa = aa;
        this.o = o;
        this.rf = rf;
        init();
    }

    public PropertyWindow(BasicClass o, AbstractApplication aa, int mode) {
        this.aa = aa;
        this.o = o;
        this.mode = mode;
        init();
    }

    public PropertyWindow(Recordset rs, AbstractApplication aa, int mode) {
        this.aa = aa;
        this.rs = rs;
        this.mode = mode;
        init();
    }

    public PropertyWindow(BasicClass o, AbstractApplication aa, RefreshableComponent rf, int mode) {
        this.aa = aa;
        this.o = o;
        this.rf = rf;
        this.mode = mode;
        init();
    }

    public PropertyWindow(BasicClass o, AbstractApplication aa, RefreshableComponent rf, BasicPanel[] subelements) {
        this.aa = aa;
        this.o = o;
        this.rf = rf;
        this.subelements = subelements;
        init();
    }

    public void init() {
        width = 600;
        if (rs != null) {
            o = (BasicClass) rs.getObjectByName("Records", "Record1");
        }
        getContentPane().setLayout(null);
        if (rs != null) {
            tabs = new JTabbedPane();
            p = (BasicPanel) o.getPropertyPanel(200);
            tabs.add("Standard", p);
        } else {
            p = (BasicPanel) o.getPropertyPanel(200);
            JLabel label = (JLabel) p.getElementAt(0);
            if (label != null) {
                label.setText("Parameter erfassen");
            }
            p.setContainer(this);
        }
        Toolkit t = getToolkit();
        int x = (int) ((t.getScreenSize().getWidth() / 2) - (width / 2));
        int y = 0;
        int height = (p.numElements * 30) + 100;
        int sheight = (int) t.getScreenSize().getHeight() - 50;
        if (subelements != null) {
            for (int i = 0; i < subelements.length; i++) {
                subelements[i].setBounds(0, height - 75, subelements[i].getWidth(), subelements[i].getHeight());
                getContentPane().add(subelements[i]);
                subelements[i].resizeComponent();
                height += subelements[i].getHeight();
            }
        }
        if (height > sheight) {
            p.setPreferredSize(new Dimension(width - 40, p.numElements * 30));
            scp = new JScrollPane(p);
            scp.setBounds(0, 0, width - 20, sheight - 100);
            getContentPane().add(scp);
            setBounds(x, y, width, sheight);
        } else {
            y = (int) (t.getScreenSize().getHeight() / 2) - (height / 2);
            if (tabs != null) {
                tabs.setBounds(0, 30, width - 10, p.numElements * 30);
                getContentPane().add(tabs);
                addButton = new JButton("+");
                addButton.setBounds(width - 30, 5, 20, 20);
                addButton.setMargin(new Insets(1, 1, 1, 1));
                addButton.addMouseListener(this);
                getContentPane().add(addButton);
                deleteButton = new JButton("-");
                deleteButton.setBounds(width - 60, 5, 20, 20);
                deleteButton.setMargin(new Insets(1, 1, 1, 1));
                deleteButton.addMouseListener(this);
                getContentPane().add(deleteButton);
                setBounds(x, y, width, height);
            } else {
                p.setBounds(0, 0, width, p.numElements * 30);
                getContentPane().add(p);
                setBounds(x, y, width, height);
            }
        }
        cancelButton = new JButton("Abbrechen");
        cancelButton.setBounds((getWidth() / 2) - 120, getHeight() - 60, 120, 25);
        getContentPane().add(cancelButton);
        cancelButton.addMouseListener(this);
        saveButton = new JButton("Uebernehmen");
        saveButton.setBounds((getWidth() / 2), getHeight() - 60, 120, 25);
        getContentPane().add(saveButton);
        saveButton.addMouseListener(this);
        validate();
        repaint();
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
        if (mode == 1) {
            if (e.getSource().equals(saveButton)) {
                o.initObject();
                aa.refresh();
                if (rf != null) {
                    rf.refresh(null);
                }
                dispose();
            } else if (e.getSource().equals(addButton)) {
                BasicClass newObject = (BasicClass) o.clone();
                rs.addSubobject("Records", newObject);
                BasicPanel p = newObject.getPropertyPanel();
                tabs.add("Variante " + rs.getObjectCollection("Records").getLastUsedId(), p);
            } else if (e.getSource().equals(deleteButton)) {
                if (tabs.getTabCount() > 1) {
                    BasicPanel bp = (BasicPanel) tabs.getSelectedComponent();
                    BasicClass deleteObject = bp.getObject();
                    rs.getObjectCollection("Records").removeObject(deleteObject);
                    tabs.remove(bp);
                }
            } else {
                dispose();
            }
        } else if (mode == 2) {
            if (e.getSource().equals(saveButton)) {
                if (rf != null) {
                    rf.refresh(null);
                }
            }
        } else {
            if (e.getSource().equals(saveButton)) {
                o.initObject();
                if (rf != null) {
                    rf.refresh(o);
                }
                dispose();
            } else {
                dispose();
            }
        }
    }

    public void setWidth(int width) {
        this.width = width;
        resize();
    }

    public void setObject(BasicClass bc) {
        this.o = bc;
        System.out.println("Object set: " + bc);
        if (p != null) getContentPane().remove(p);
        if (scp != null) getContentPane().remove(scp);
        p = (BasicPanel) bc.getPropertyPanel(100);
        p.setContainer(this);
        Toolkit t = getToolkit();
        int y = 0;
        int x = getX();
        int height = (p.numElements * 30) + 100;
        int sheight = (int) t.getScreenSize().getHeight() - 50;
        if (height > sheight) {
            p.setPreferredSize(new Dimension(width, p.numElements * 30));
            scp = new JScrollPane(p);
            scp.setBounds(0, 0, width, sheight - 100);
            getContentPane().add(scp);
            setBounds(x, y, width, sheight);
        } else {
            y = (int) (t.getScreenSize().getHeight() / 2) - (height / 2);
            p.setBounds(0, 0, width, p.numElements * 30);
            getContentPane().add(p);
            setBounds(x, y, width, height);
        }
        resize();
    }

    public void resize() {
        cancelButton.setBounds((getWidth() / 2) - 120, getHeight() - 100, 120, 25);
        getContentPane().add(cancelButton);
        cancelButton.addMouseListener(this);
        saveButton.setBounds((getWidth() / 2), getHeight() - 100, 120, 25);
        getContentPane().add(saveButton);
        saveButton.addMouseListener(this);
    }
}
