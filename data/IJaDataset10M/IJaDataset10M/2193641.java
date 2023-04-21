package model;

import _bye_util.IntRecycler;
import model.interfaces.selectable.SelectableCommand;
import java.util.List;
import java.util.LinkedList;

/**
 * A colonist is required to esatablish a base. Colonists build bases on the
 * same tile they occupy.
 * 
 * @author Chris
 * 
 */
class Colonist extends Unit {

    private static LinkedList<SelectableCommand> additionalCommands = new LinkedList<SelectableCommand>();

    {
        additionalCommands.add(new BuildBase());
        additionalCommands.add(new BuildTower());
        additionalCommands.add(new Harvest());
    }

    public Colonist(IntRecycler i) {
        super(i, InstanceType.COLONIST, 2, 2, 2, 3, "Colonist", 0, 0, 5);
    }

    /**
     * Returns the additional commands that the colonist allows a rally point to
     * perform.
     * 
     * @return the additional commands this unit brings to a rally point.
     */
    public List<SelectableCommand> getAdditionalCommands() {
        return additionalCommands;
    }
}
