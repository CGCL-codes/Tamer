package sim.app.ubik.domoticDevices;

import sim.app.ubik.MovementTools;
import sim.app.ubik.Ubik;
import sim.app.ubik.building.Door;
import sim.app.ubik.building.OfficeFloor;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.MutableInt2D;

/**
 * Metodos para sugerir puerta y dirección. Al no almacenar estado, sólo hay una instancia de la aplicación,
 * no es muy eficiente. A cambio es sencillo crear nuevas clases que extiendan a AmIApplication. 
 
 * @author fox
 */
public class SimpleFleeingApplication extends AmIApplication {

    protected Door lastSuggestedDoor;

    protected Int2D lastSuggestedDoorDirection;

    public Door getSuggestedDoor(SimState state, DomoticDevice domotic) {
        Int2D fireDirection = super.fireDirection(state, domotic);
        Ubik ubik = (Ubik) state;
        OfficeFloor of = ubik.building.getFloor(domotic.floor);
        return getSuggestedDoorExceptAdvancingToADirection(state, domotic.position, of, fireDirection);
    }

    public Int2D getSuggestedDirection(SimState state, DomoticDevice domotic) {
        return this.lastSuggestedDoorDirection;
    }

    /**
     * Este método se puede llamar desde fuera sin fuego ni dispositivo domotico para testear que funcione bien. Hay un test comentado en el actuador.
     * trabajadores.
     * @param state
     * @param position
     * @param of
     * @param direction
     * @return
     */
    public Door getSuggestedDoorExceptAdvancingToADirection(SimState state, Int2D position, OfficeFloor of, Int2D direction) {
        Bag doorExceptions = new Bag();
        Door d;
        Door chosenDoor = null;
        Int2D doorDirection = null;
        Door firstDoor = of.getNearestDoorWithExceptions(position.x, position.y, null);
        do {
            d = of.getNearestDoorWithExceptions(position.x, position.y, doorExceptions);
            if (d != null) {
                doorDirection = MovementTools.getInstance().generateDirectionToLocation(position.x, position.y, d.initialPosition().x, d.initialPosition().y);
                if (MovementTools.getInstance().isNotAnAdvanceDirection(state, doorDirection, direction)) {
                    chosenDoor = d;
                    lastSuggestedDoor = d;
                    lastSuggestedDoorDirection = doorDirection;
                } else {
                    doorExceptions.add(d);
                }
            }
        } while (chosenDoor == null && d != null);
        if (d == null) {
            lastSuggestedDoor = firstDoor;
            lastSuggestedDoorDirection = MovementTools.getInstance().generateDirectionToLocation(position.x, position.y, firstDoor.initialPosition().x, firstDoor.initialPosition().y);
        }
        return lastSuggestedDoor;
    }
}
