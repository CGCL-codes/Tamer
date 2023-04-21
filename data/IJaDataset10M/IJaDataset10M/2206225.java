package sfa.projetIHM.horloge.models;

public class PanelDateM extends PanelCentralM {

    public PanelDateM(HorlogeM m) {
        super(m);
    }

    public void setHeure(int h) {
        getHorloge().setHeure(h);
    }

    public void setMinute(int m) {
        getHorloge().setMinute(m);
    }

    public void setSeconde(int s) {
        getHorloge().setSeconde(s);
    }

    public void incremente() {
        getHorloge().incremente();
        getHorloge().setTextHeure(getHorloge().getHeure() + ":" + getHorloge().getMinute() + ":" + getHorloge().getSeconde() + ":" + getHorloge().getDixieme());
    }
}
