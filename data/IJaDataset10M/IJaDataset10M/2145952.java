package util;

import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

public class KillRing {

    Vector ring;

    int index;

    JTextArea textarea;

    JLabel label;

    JList kill_list;

    JFrame window;

    public KillRing() {
        ring = new Vector();
        ring.add(new Object[] { "", new Integer(0), new Integer(0), new Integer(0) });
        index = 0;
        textarea = new JTextArea(10, 50);
        textarea.setBackground(java.awt.Color.yellow);
        label = new JLabel("" + index);
        kill_list = new JList(ring);
        kill_list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ;
                ;
            }
        });
    }

    public String getNthKill(int N) {
        Object[] kill = (Object[]) ring.get(N);
        return (String) kill[0];
    }

    public void showNthKill(int N) {
        ;
    }

    public void createKillWin() {
        ;
    }

    public void addKill(String str, int offset, int len, int hist_point) {
        ;
    }

    public void show() {
        ;
    }

    public JList getKillList() {
        return kill_list;
    }
}
