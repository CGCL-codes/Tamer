package civquest.swing.gameChange;

import civquest.core.Game;
import civquest.gameChange.GameChangeManager;
import civquest.gameChange.abs.AbstractFieldGUIChange;
import civquest.map.Field;
import civquest.map.MapData;
import civquest.swing.MapViewComponent;
import civquest.util.Coordinate;
import java.util.ArrayList;
import java.util.List;

/**
 * SetWorkingField.java
 *
 *
 *
 */
public class SetWorkingField extends AbstractFieldGUIChange {

    private MapViewComponent mapViewComponent;

    private Coordinate coord;

    /**
	 * Constructs a new SetWorkingField-GUIChange.
	 * NOTE: Do only use this GUIChange if the WF really changes.
	 *
	 * @param mapViewComponent this MVC's working-field will be set by this 
	 *                         GUIChange
	 * @param coord Coordinate of the new working-field; null if no new
	 *              working-field exists
	 */
    public SetWorkingField(MapViewComponent mapViewComponent, Coordinate coord) {
        super(getCoordArray(mapViewComponent, coord));
        this.mapViewComponent = mapViewComponent;
        this.coord = coord;
    }

    private static Coordinate[] getCoordArray(MapViewComponent mvc, Coordinate coord) {
        List<Coordinate> coordList = new ArrayList<Coordinate>();
        if (mvc.getWorkingField() != null) {
            coordList.add(mvc.getWorkingField().getPosition());
        }
        if (coord != null) {
            coordList.add(coord);
        }
        return coordList.toArray(new Coordinate[coordList.size()]);
    }

    public void execute() {
        notifyBefore();
        Field field = null;
        if (coord != null) {
            field = Game.getMapData().getField(coord);
        }
        mapViewComponent.setWorkingField(field);
        notifyAfter();
    }
}
