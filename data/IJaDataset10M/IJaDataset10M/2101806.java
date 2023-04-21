package ie.omk.jest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ie.omk.smpp.message.*;
import ie.omk.debug.Debug;

public class PacketDialog extends javax.swing.JDialog {

    JButton header;

    JButton send;

    JButton cancel;

    JFrame fparent;

    SMPPPacket packet;

    public JPanel ppanel;

    public JPanel hpanel;

    public static final int SEND = 1, CANCEL = 2;

    public int action = CANCEL;

    public PacketDialog(JFrame f, SMPPPacket p, boolean sender) {
        super(f);
        fparent = f;
        if (p == null) throw new NullPointerException("Packet cannot be null.");
        packet = p;
        ppanel = PanelFactory.createPanel(p, sender);
        if (ppanel instanceof MessagePanel) ((MessagePanel) ppanel).setDialogParent(fparent);
        header = new JButton("Show Header...");
        if (sender) {
            send = new JButton("Send");
            cancel = new JButton("Cancel");
        } else cancel = new JButton("Close");
        Listener ml = new Listener();
        header.addMouseListener(ml);
        if (send != null) send.addMouseListener(ml);
        cancel.addMouseListener(ml);
        JPanel hp = new JPanel();
        hp.setLayout(new FlowLayout());
        hp.add(header);
        Panel p1 = new Panel();
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(2, 1));
        p1.setLayout(new BorderLayout());
        if (sender) p2.add(send);
        p2.add(cancel);
        p1.add("South", p2);
        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        if (!(ppanel instanceof HeaderPanel)) cpane.add("North", hp);
        cpane.add("Center", ppanel);
        cpane.add("East", p1);
    }

    void showHeader() {
        HeaderPanel hpanel = new HeaderPanel(packet, false);
        JOptionPane.showMessageDialog(this, hpanel, "Packet header", JOptionPane.PLAIN_MESSAGE);
    }

    public class Listener extends java.awt.event.MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            Object o = e.getSource();
            if (o.equals(send)) {
                PacketDialog.this.action = SEND;
                PacketDialog.this.setVisible(false);
            } else if (o.equals(cancel)) {
                PacketDialog.this.action = CANCEL;
                PacketDialog.this.setVisible(false);
            } else if (o.equals(header)) {
                PacketDialog.this.showHeader();
            }
        }
    }
}
