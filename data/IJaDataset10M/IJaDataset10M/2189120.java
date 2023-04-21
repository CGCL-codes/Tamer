package sfa.projetIHM.horloge.controls;

import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;
import sfa.projetIHM.horloge.models.HorlogeM;
import sfa.projetIHM.horloge.models.PanelAlarmeM;
import sfa.projetIHM.horloge.view.PanelAlarmeV;

public class PanelAlarmeC extends PanelCentralC {

    private Date date;

    public PanelAlarmeC() {
        modele = new PanelAlarmeM(new HorlogeM(0, 0, 0, 0));
        vue = new PanelAlarmeV(modele);
        modele.setTache(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                date = new Date();
                double h = date.getHours();
                double m = date.getMinutes();
                ((PanelAlarmeM) modele).isHour(h, m);
            }
        });
        modele.setDelais(30000);
        modele.setTimer(new Timer(modele.getDelais(), modele.getTache()));
    }
}
