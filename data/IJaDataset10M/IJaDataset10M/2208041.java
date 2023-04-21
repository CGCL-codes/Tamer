package ch.HaagWeirich.Agenda;

import javax.swing.JDialog;
import ch.rgw.swingtools.ButtonFactory;
import ch.rgw.swingtools.LblInput;
import ch.rgw.tools.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Neuer Mandant */
@SuppressWarnings("serial")
public class MandNeuDlg extends JDialog {

    static final String Version = "1.1.3";

    Agenda hub;

    SettingsDlg par;

    MandNeuDlg self = this;

    LblInput liLabel = new LblInput("Kurzname", 10, LblInput.VERTICAL);

    LblInput liName = new LblInput("Anrede,Name", 10, LblInput.VERTICAL);

    LblInput li4D = new LblInput("Name in 4D", 10, LblInput.VERTICAL);

    blisten lis = new blisten();

    Agenda.agMandant m;

    public MandNeuDlg(Agenda h, SettingsDlg parent, Agenda.agMandant m) {
        hub = h;
        par = parent;
        this.m = m;
        JPanel bck = new JPanel(new GridLayout(4, 1));
        if (m != null) {
            liName.set(m.getName());
            liLabel.set(m.getLabel());
            li4D.set(m.getMapping());
        }
        bck.add(liLabel);
        bck.add(liName);
        bck.add(li4D);
        bck.add(ButtonFactory.getOKCancelPanel("OK", "Abbrechen", getRootPane(), lis, lis));
        getContentPane().add(bck);
        setTitle("Mandant bearbeiten");
        pack();
    }

    class blisten implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            if (ev.getActionCommand().equals("OK")) {
                if ((liLabel.get() != null) && (liName.get() != null)) {
                    if (m == null) {
                        Agenda.agMandant agm = new Agenda.agMandant(liName.get(), liLabel.get(), 30, null);
                        par.vMandanten.add(agm);
                        agm.setMapping(li4D.get());
                        Agenda.j.exec("INSERT INTO agnMandanten (label,name,Map4D) VALUES (" + JdbcLink.wrap(liLabel.get()) + "," + JdbcLink.wrap(liName.get()) + "," + JdbcLink.wrap(li4D.get()) + ")");
                        agm.flush();
                        par.cbMands.addItem(liLabel.get());
                    } else {
                        m.setLabel(liLabel.get());
                        m.setName(liName.get());
                        m.setMapping(li4D.get());
                    }
                }
            }
            dispose();
        }
    }
}
