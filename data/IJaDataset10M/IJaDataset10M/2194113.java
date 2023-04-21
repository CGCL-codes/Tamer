package org.kubiki.gui;

import java.awt.event.*;
import javax.swing.*;
import org.kubiki.xml.*;
import org.kubiki.base.*;
import org.kubiki.gui.*;
import org.kubiki.ide.*;
import org.kubiki.graphic.*;

public class ObjectInspector extends BasicPanel {

    JPanel pp;

    BasicClass o;

    RefreshableComponent rc;

    public ObjectInspector() {
        setLayout(null);
        addMouseListener(this);
    }

    public void setPropertyPanel(JPanel jp, BasicClass o, RefreshableComponent rc) {
        this.rc = rc;
        this.o = o;
        System.out.println(o);
        try {
            remove(pp);
        } catch (Exception e) {
        }
        this.pp = jp;
        pp.setBounds(0, 10, 800, 1000);
        add(pp);
        pp.repaint();
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
        o.initObject();
        rc.refresh(this);
        ;
    }
}
