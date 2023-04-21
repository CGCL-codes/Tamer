package artem.finance.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 * @author bia
 *
 */
public class MenuActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        JMenuItem combo = (JMenuItem) ae.getSource();
        String choise = combo.getActionCommand();
        if (choise.equals("Organizations")) {
            SpravochnikOrganizaziy2 spo = new SpravochnikOrganizaziy2();
            spo.setVisible(true);
        }
        if (choise.equals("Suppliers")) {
            SpravochnikPostavshikov sppos = new SpravochnikPostavshikov();
            sppos.setVisible(true);
        }
        if (choise.equals("Departments")) {
            SpravochnikPodrazdeleniy sppod = new SpravochnikPodrazdeleniy();
            sppod.setVisible(true);
        }
        if (choise.equals("Banks")) {
            SpravochnikBankov spb = new SpravochnikBankov();
            spb.setVisible(true);
        }
        if (choise.equals("SubContracts")) {
            SpravochnikDopSoglasheniy spdops = new SpravochnikDopSoglasheniy();
            spdops.setVisible(true);
        }
        if (choise.equals("Services")) {
            SpravochnikUslug spu = new SpravochnikUslug();
            spu.setVisible(true);
        }
    }
}
