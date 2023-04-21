package client.tabs.primary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import client.ClientConnect;
import client.ClientSender;
import client.GUILogger;
import client.GUIXMLCreator;
import common.Handler;
import common.Variables;

public class PrimaryButtonPanel extends JPanel {

    JButton xmlsend, connect, disconnect, reset, beameron, beameroff, status, muteoff, muteon, freezeon, freezeoff, matrixsend, matrixconn, matrixdisconn, update_values, mute, freeze;

    GUILogger log2 = new GUILogger();

    Socket s;

    ClientSender csender;

    Handler handler;

    XMLOutputter out = Variables.getXMLOutputter();

    SAXBuilder builder = new SAXBuilder();

    Document doc;

    GUIXMLCreator xmlcreator = new GUIXMLCreator();

    GridBagConstraints c = new GridBagConstraints();

    /**
   * Constructs a Panel with some Buttons of primary importance.
   */
    public PrimaryButtonPanel() {
        this.setLayout(new GridBagLayout());
        reset = new JButton("reset");
        reset.setToolTipText("Send a reset message to the server. All Jobs in the queue will be deleted");
        status = new JButton("status");
        status.setToolTipText("A status message of the server will be returned (Jobs in the queues etc.)");
        muteoff = new JButton("mute on");
        muteoff.setToolTipText("Blank the screen of the beamers");
        muteon = new JButton("mute off");
        muteon.setToolTipText("Show the beamer screen again");
        freezeon = new JButton("freeze on");
        freezeon.setToolTipText("Freeze the beamer screen");
        freezeoff = new JButton("freeze off");
        freezeoff.setToolTipText("Unfreeze the beamer screen");
        update_values = new JButton("Update_Values");
        update_values.setToolTipText("Get the current values from the beamer \n(usefull if you want to save a preset on the client) ");
        mute = new JButton("mute screen");
        mute.setToolTipText("Mute/unmute the beamer screen (blank screen)");
        freeze = new JButton("freeze screen");
        freeze.setToolTipText("Freeze/unfreeze the beamer screen");
        reset.addActionListener(new PrimaryButtonListener(this));
        status.addActionListener(new PrimaryButtonListener(this));
        update_values.addActionListener(new PrimaryButtonListener(this));
        mute.addActionListener(new PrimaryButtonListener(this));
        freeze.addActionListener(new PrimaryButtonListener(this));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridy = 0;
        c.gridx = 0;
        this.add(status, c);
        c.gridx = 1;
        this.add(reset, c);
        c.gridx = 2;
        this.add(update_values, c);
        c.gridy = 1;
        c.gridx = 0;
        this.add(mute, c);
        c.gridx = 2;
        this.add(freeze, c);
    }

    /**
   * Method is called if one of the JButtons is pressed
   * Corresponding to the pressed button, an action is
   * performed.
   * @param e ActionEvent
   */
    public void DomPanelActionPerformed(ActionEvent e) {
        try {
            if ((JButton) e.getSource() == this.reset) {
                Element beamer = xmlcreator.getBeamerReset();
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.status) {
                Element beamer = xmlcreator.getBeamerStatus();
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.muteoff) {
                Element beamer = xmlcreator.getBElement("MUTE", "on", "1,2,3,4");
                out.output(beamer, System.out);
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.muteon) {
                Element beamer = xmlcreator.getBElement("MUTE", "off", "1,2,3,4");
                out.output(beamer, System.out);
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.freezeon) {
                Element beamer = xmlcreator.getBElement("FREEZE", "on", "1,2,3,4");
                out.output(beamer, System.out);
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.freezeoff) {
                Element beamer = xmlcreator.getBElement("FREEZE", "off", "1,2,3,4");
                out.output(beamer, System.out);
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.update_values) {
                Element beamer = xmlcreator.getUpdateEverything();
                ClientConnect.writeBeamer(beamer);
            } else if ((JButton) e.getSource() == this.freeze) {
                JButton b = (JButton) e.getSource();
                if (ClientConnect.isBeamerConnected()) {
                    if (b.getText().equalsIgnoreCase("freeze screen")) {
                        Element beamer = xmlcreator.getBElement("FREEZE", "on", "1,2,3,4");
                        out.output(beamer, System.out);
                        ClientConnect.writeBeamer(beamer);
                        freeze.setText("unfreeze screen");
                    } else if (ClientConnect.isBeamerConnected()) {
                        Element beamer = xmlcreator.getBElement("FREEZE", "off", "1,2,3,4");
                        out.output(beamer, System.out);
                        ClientConnect.writeBeamer(beamer);
                        freeze.setText("freeze screen");
                    }
                } else {
                    ClientConnect.writeBeamer(new Element("dummy"));
                }
            } else if ((JButton) e.getSource() == this.mute) {
                JButton b = (JButton) e.getSource();
                if (ClientConnect.isBeamerConnected()) {
                    if (b.getText().equalsIgnoreCase("mute screen") && ClientConnect.isBeamerConnected()) {
                        Element beamer = xmlcreator.getBElement("MUTE", "on", "1,2,3,4");
                        out.output(beamer, System.out);
                        ClientConnect.writeBeamer(beamer);
                        mute.setText("unmute screen");
                    } else if (ClientConnect.isBeamerConnected()) {
                        Element beamer = xmlcreator.getBElement("MUTE", "off", "1,2,3,4");
                        out.output(beamer, System.out);
                        ClientConnect.writeBeamer(beamer);
                        mute.setText("mute screen");
                    }
                } else {
                    ClientConnect.writeBeamer(new Element("dummy"));
                }
            } else {
            }
        } catch (NullPointerException f) {
            log2.krit("DomButtonNull: Cannot write! Is the server running?");
            log2.logkonsoleAll("DomButtonNull: Cannot write! Is the server running?\n");
        } catch (ConnectException f) {
            log2.krit("DomButton: Connection refused. Is the server running?");
            log2.logkonsoleAll("DomButton: Connection refused. Is the server running?\n");
        } catch (Exception f) {
            log2.krit("DomButton:");
            log2.krit(f);
            log2.logkonsoleAll(f.getMessage() + "\n");
        }
    }
}

class PrimaryButtonListener implements ActionListener {

    private PrimaryButtonPanel adaptee;

    PrimaryButtonListener(PrimaryButtonPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DomPanelActionPerformed(e);
    }
}
