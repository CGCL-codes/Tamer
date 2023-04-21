package sfa.projetIHM.horloge.controls;

import sfa.projetIHM.horloge.models.PanelCentralM;
import sfa.projetIHM.horloge.view.PanelCentralV;

public class PanelCentralC {

    protected PanelCentralM modele;

    protected PanelCentralV vue;

    public PanelCentralM getModele() {
        return modele;
    }

    public void setModele(PanelCentralM modele) {
        this.modele = modele;
    }

    public PanelCentralV getVue() {
        return vue;
    }

    public void setVue(PanelCentralV vue) {
        this.vue = vue;
    }
}
