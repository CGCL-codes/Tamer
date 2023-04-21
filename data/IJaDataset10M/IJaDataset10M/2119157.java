package net.sf.imca.web.backingbeans.tablemodels;

import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import net.sf.imca.model.BoatBO;

public class BoatTableData {

    private ArrayDataModel model = null;

    public BoatTableData(BoatBO[] boats) {
        model = new ArrayDataModel(boats);
    }

    public DataModel getBoats() {
        return model;
    }

    public BoatBO getSelectedBoat() {
        BoatBO[] boats = (BoatBO[]) model.getWrappedData();
        for (int i = 0; i < boats.length; i++) {
            if (boats[i].getIsSelected()) {
                return boats[i];
            }
        }
        return null;
    }
}
