package psvb.horloge.date;

import psvb.horloge.horloge.HorlogeAiguille;
import psvb.horloge.panelcentral.PanelCentralM;
import psvb.horloge.panelcentral.PanelCentralV;

public class PanelDateV extends PanelCentralV {

    public PanelDateV(PanelCentralM mod) {
        super("Date", mod);
        setHorloge(new HorlogeAiguille(400, modele.getHorloge()));
        getHorloge().setDixieme(false);
    }
}
