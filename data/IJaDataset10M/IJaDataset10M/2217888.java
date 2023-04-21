package eu.popeye.network;

import org.jgroups.*;
import eu.popeye.network.TransportPopeye;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author micky
 */
public class PruebaTransportSuperPeer implements TransportPopeye, ActionListener {

    PullPushAdapterInterface fachade = null;

    /** Creates a new instance of PruebaTransportSuperPeer */
    public PruebaTransportSuperPeer() {
        JFrame jf = new JFrame("caca");
        JButton b = new JButton("empieza");
        b.setActionCommand("empieza");
        b.addActionListener(this);
        jf.add(b);
        jf.setPreferredSize(new Dimension(400, 600));
        jf.setVisible(true);
        ejecuta();
        fachade.disconnect();
        fachade = null;
    }

    public void receiveData(Message msg, String nom) {
    }

    public void receiveStruct(Message msg) {
    }

    public static void main(String[] args) {
        new PruebaTransportSuperPeer();
    }

    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equals("empieza")) {
            ejecuta();
        }
    }

    public void ejecuta() {
        try {
            fachade = new PullPushAdapterInterface(this, "uno", true, "hola");
            fachade.connect();
        } catch (ChannelException ex) {
            ex.printStackTrace();
        }
    }
}
