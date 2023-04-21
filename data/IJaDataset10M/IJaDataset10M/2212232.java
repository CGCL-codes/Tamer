package org.mati.geotech.sandbox;

import org.mati.geotech.model.cellcover.CellCoverModel;
import org.mati.geotech.sandbox.gui.CellCoverDrawPanel;
import org.mati.geotech.sandbox.gui.MainFrame;

public class CellCoverApp {

    public static void main(String[] args) {
        MainFrame mf = new MainFrame(new CellCoverDrawPanel());
        CellCoverModel model = new CellCoverModel();
        mf.setModel(model);
        mf.setVisible(true);
    }
}
