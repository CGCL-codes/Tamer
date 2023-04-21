package action.attack;

import java.util.ArrayList;
import equipment.Equipment;
import attribute.Att;

public class GroupEffect extends Attack {

    /**
    * Empty Attack object.
    */
    public GroupEffect() {
        super();
    }

    /**
    * Constructor.
    * 
    * @param id
    * @param outputList
    */
    public GroupEffect(ArrayList origin, ArrayList destination, Equipment weapon, ArrayList<Att> outputList) {
        super(origin, destination, weapon, outputList);
    }
}
