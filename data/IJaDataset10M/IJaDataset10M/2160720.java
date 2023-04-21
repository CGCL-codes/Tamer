package action.attack;

import java.util.ArrayList;
import equipment.Equipment;
import attribute.Att;

public class Swing extends Attack {

    /**
    * Empty Attack object.
    */
    public Swing() {
        super();
    }

    /**
    * Constructor.
    * 
    * @param id
    * @param outputList
    */
    public Swing(ArrayList origin, ArrayList destination, Equipment weapon, ArrayList<Att> outputList) {
        super(origin, destination, weapon, outputList);
    }
}
