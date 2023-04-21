package us.wthr.jdem846.ui.optionModels;

import us.wthr.jdem846.gis.projections.MapProjectionEnum;
import us.wthr.jdem846.ui.base.JComboBoxModel;

public class MapProjectionListModel extends JComboBoxModel<String> {

    public MapProjectionListModel() {
        for (MapProjectionEnum projectionEnum : MapProjectionEnum.values()) {
            addItem(projectionEnum.projectionName(), projectionEnum.identifier());
        }
    }
}
