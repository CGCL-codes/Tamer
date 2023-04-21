package org.gamenet.application.mm8leveleditor.control;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.gamenet.application.mm8leveleditor.data.DObjListBin;

public class DObjListBinControl extends JPanel {

    private DObjListBin dObjListBin = null;

    public DObjListBinControl(DObjListBin srcDObjListBin) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.dObjListBin = srcDObjListBin;
        this.add(new JLabel("Offset: " + String.valueOf(dObjListBin.getDObjListBinOffset())));
    }

    public Object getDObjListBin() {
        return dObjListBin;
    }
}
