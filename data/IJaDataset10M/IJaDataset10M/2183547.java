package model;

import model.interfaces.selectable.SelectableCommand;
import model.interfaces.commandTypes.ArgumentsVisitor;
import model.interfaces.commandTypes.ArmyArgs;

/**
 * Command that tells a unit to move to a specific RallyPoint, joining that
 * RallyPoint's army if it is not in it already.
 * 
 * @author Christopher Dudley
 */
class GoToRallyPoint implements Command, SelectableCommand, ArmyArgs, NotPartOfTheSystem {

    private static final String name = "Go To RP";

    private int argument;

    /**
     * Creates a new GoToRallyPoint command with a default rally point ID of -1.
     */
    public GoToRallyPoint() {
        argument = -1;
    }

    /**
     * Creates a new GoToRallyPoint command with a set rally point ID.
     * 
     * @param armyID
     *            the number of the rally point to go to.
     */
    private GoToRallyPoint(int rallyPointID) {
        argument = rallyPointID;
    }

    /**
     * Accepts an ArgumentsVisitor that correctly sets the army ID for the
     * command.
     * 
     * @param visitor
     *            the ArgumentsVisitor.
     */
    public void accept(ArgumentsVisitor visitor) {
        visitor.visitArmyArgs(this);
    }

    /**
     * Creates a copy of the command with the same argument with a reference
     * type of Command.
     * 
     * @return a Command version of the command.
     */
    public Command getCommand() {
        Command copy = new GoToRallyPoint(argument);
        argument = -1;
        return copy;
    }

    /**
     * Returns the name of the command.
     * 
     * @return the name of the command.
     */
    public String getName() {
        String zeName = name;
        if (argument > -1) {
            zeName = zeName.concat(" ");
            zeName = zeName.concat(Integer.toString(argument));
        }
        return zeName;
    }

    /**
     * Executes the command.
     * 
     * @param player
     *            the player who owns the unit.
     * @param instance
     *            the unit that is moving to the rally point.
     */
    public void run(Player player, ModelInstance instance) {
        player.joinRallyPoint(instance, argument);
    }

    /**
     * Sets the rally point ID desired as the argument of the command.
     * 
     * @param rallyPointID
     *            the id of the rally point for the unit to join or move to.
     */
    public void setArgs(int rallyPointID) {
        argument = rallyPointID;
    }

    /**
     * It's not part of your system, man!
     */
    public void throwItOnTheGround() {
        new ThrowItOnTheGround().cuzMyDadIsNotA(this);
    }
}
