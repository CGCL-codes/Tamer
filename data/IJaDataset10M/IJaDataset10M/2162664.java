package ie.omk.jest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import ie.omk.smpp.message.*;
import ie.omk.debug.Debug;

public class AddrDialog extends javax.swing.JDialog implements java.awt.event.MouseListener {

    JFrame fparent;

    AddrPanel addr;

    JButton ok;

    public AddrDialog(JFrame fparent) {
        this(fparent, new SmeAddress());
    }

    public AddrDialog(JFrame fparent, SmeAddress d) {
        super(fparent);
        if (Debug.dbg) Debug.d(this, "<init>", "entered", Debug.DBG_5);
        if (fparent == null) throw new NullPointerException();
        this.fparent = fparent;
        setTitle("Sme Address Dialog");
        addr = new AddrPanel(d, true, true);
        ok = new JButton("Ok");
        ok.addMouseListener(this);
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.fill = gbc1.BOTH;
        gbc1.anchor = gbc1.NORTHWEST;
        gbc1.weightx = 1.0;
        gbc1.weighty = 1.0;
        gbc1.gridwidth = gbc1.RELATIVE;
        Container cpane = getContentPane();
        cpane.setLayout(gb);
        gb.setConstraints(addr, gbc1);
        cpane.add(addr);
        gbc1.fill = gbc1.HORIZONTAL;
        gbc1.anchor = gbc1.SOUTH;
        gbc1.weightx = 0.0;
        gbc1.gridwidth = gbc1.REMAINDER;
        gb.setConstraints(ok, gbc1);
        cpane.add(ok);
    }

    public SmeAddress getAddr() {
        if (Debug.dbg) Debug.d(this, "getAddr", "entered", Debug.DBG_5);
        if (addr.fillFields()) return addr.addr; else {
            Debug.d(this, "getAddr", "fillFields failed.", Debug.DBG_3);
            return null;
        }
    }

    public void setVisible(boolean b) {
        if (Debug.dbg) Debug.d(this, "setVisible", "entered", Debug.DBG_5);
        Cursor c;
        if (b) {
            c = new Cursor(Cursor.WAIT_CURSOR);
        } else {
            c = new Cursor(Cursor.DEFAULT_CURSOR);
        }
        fparent.setCursor(c);
        super.setVisible(b);
    }

    public void mouseClicked(MouseEvent e) {
        if (Debug.dbg) Debug.d(this, "mouseClicked", "entered", Debug.DBG_5);
        this.setVisible(false);
    }

    public void mousePressed(MouseEvent e) {
        return;
    }

    public void mouseReleased(MouseEvent e) {
        return;
    }

    public void mouseEntered(MouseEvent e) {
        return;
    }

    public void mouseExited(MouseEvent e) {
        return;
    }
}
