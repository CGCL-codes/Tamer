package principal;

import Composite.*;
import images.VignettesListModel;
import java.awt.event.*;

public class ControleurGetPere implements ActionListener {

    private VignettesListModel model;

    public ControleurGetPere(VignettesListModel model) {
        this.model = model;
    }

    public void actionPerformed(ActionEvent e) {
        Dossier modelA = (Dossier) model.getModelAdapte();
        if (modelA != null && modelA.getPere() != null) {
            model.setModelAdapte((Dossier) modelA.getPere());
        }
    }
}
