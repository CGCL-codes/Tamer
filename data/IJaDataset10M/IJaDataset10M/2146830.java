package jflag.core.entities.actions;

import jflag.misc.Position;
import jflag.core.entities.Unit;
import java.util.ArrayList;

/** An action that can be accomplished by an unit.
  */
public abstract class Action {

    /** Units to which this action is associated. */
    private ArrayList<Unit> targetUnits;

    public Action() {
        targetUnits = new ArrayList<Unit>();
    }

    /** Set the units to which this action is associated.
	  *
	  * @param units unit list
	  */
    public void setTargetUnits(ArrayList<Unit> units) {
        targetUnits = new ArrayList<Unit>();
        for (int i = 0; i < units.size(); ++i) targetUnits.add(units.get(i));
    }

    /** Return the units to which this action is associated.
	  *
	  * @return target unit list
	  */
    public ArrayList<Unit> getTargetUnits() {
        return (targetUnits);
    }

    /** Add an unit to the unit target list.
	  *
	  * @param unit unit to add
	  */
    public void addTargetUnit(Unit unit) {
        targetUnits.add(unit);
    }

    /** Perform the action. */
    public abstract void doIt(Position pos);

    /** Return the action identifier. */
    public abstract String getId();

    /** Test whether this action needs a position argument. */
    public abstract boolean needsPosition();
}
