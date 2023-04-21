package Application.MainForm;

import java.awt.*;
import java.awt.event.*;
import Application.Elements.BTN;

/**
 * BTN osztalyt megjelenito es kontrollalo osztaly
 * @author Stikmann
 */
public class BTNControl extends Panel implements ActionListener {

    private BTN btn;

    private Label label;

    private NetworkUpdateListener updatelistener;

    /**
     * A belso Button osztaly klikkeventjere ez van felirva, azaz a gombok
     * megnyomasakor ez hivodik meg. A gombok ActionCommand-ja adja meg hogy
     * melyik lett megnyomva.
     * @param e Java standard
     */
    public void actionPerformed(ActionEvent e) {
        boolean mire = e.getActionCommand().equals("1");
        btn.setErtek(mire);
        label.setText("kimenet: " + (mire ? "1" : "0"));
        if (updatelistener != null) updatelistener.networkUpdate();
    }

    /**
     * Az osztaly konstruktora
     * @param abtn A BTN, amire figyel
     * @param aupdatelistener Egy observer, aminek jelez, ha a buttonon
     * valtozas all be.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BTNControl(BTN abtn, NetworkUpdateListener aupdatelistener) {
        super();
        btn = abtn;
        updatelistener = aupdatelistener;
        setLayout(null);
        setSize(70, 35);
        label = new Label("kimenet: " + (btn.getOut() ? "1" : "0"));
        add(label);
        label.setSize(70, 15);
        label.setLocation(0, 0);
        Button button0, button1;
        button0 = new Button("0");
        add(button0);
        button0.setSize(35, 20);
        button0.setLocation(0, 15);
        button0.setActionCommand("0");
        button0.addActionListener(this);
        button1 = new Button("1");
        add(button1);
        button1.setSize(35, 20);
        button1.setLocation(35, 15);
        button1.setActionCommand("1");
        button1.addActionListener(this);
        validate();
    }
}
