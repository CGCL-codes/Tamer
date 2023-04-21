package net.sourceforge.aglets.examples.itinerary;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import com.ibm.aglet.util.AddressChooser;
import com.ibm.agletx.util.MessengerItinerary;

class MessengerFrame extends Frame implements WindowListener, ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 4088340869215914328L;

    MessengerAglet aglet;

    List list = new List(10, false);

    AddressChooser address = new AddressChooser(15);

    Choice choice = new Choice();

    MessengerFrame(MessengerAglet a) {
        this.aglet = a;
        this.addWindowListener(this);
        this.setLayout(new BorderLayout());
        this.add("Center", this.list);
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        p.add(this.address);
        p.add(this.choice);
        Button ad = new Button("Add");
        Button remove = new Button("Remove");
        ad.addActionListener(this);
        remove.addActionListener(this);
        p.add(ad);
        p.add(remove);
        this.add("North", p);
        p = new Panel();
        p.setLayout(new FlowLayout());
        Button start = new Button("Start!");
        start.addActionListener(this);
        p.add(start);
        this.add("South", p);
        this.choice.addItem("dispose");
        this.update();
    }

    /**
     * Handles the action event
     * 
     * @param ae
     *            the event to be handled
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if ("Start!".equals(ae.getActionCommand())) {
            if (this.aglet.itinerary == null) {
                this.aglet.itinerary = new MessengerItinerary(this.aglet, this.choice.getSelectedItem());
            }
            this.aglet.start();
        } else if ("Remove".equals(ae.getActionCommand())) {
            int i = this.list.getSelectedIndex();
            if (i >= 0) {
                this.aglet.addresses.removeElementAt(i);
                this.list.remove(i);
            }
        } else if ("Add".equals(ae.getActionCommand())) {
            this.aglet.addresses.addElement(this.address.getAddress());
            this.update();
        }
    }

    private void update() {
        this.list.removeAll();
        Vector addrs = this.aglet.addresses;
        int size = addrs.size();
        for (int i = 0; i < size; i++) {
            this.list.add((String) addrs.elementAt(i));
        }
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    /**
     * Handles the window event
     * 
     * @param we
     *            the event to be handled
     */
    @Override
    public void windowClosing(WindowEvent we) {
        this.dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }
}
