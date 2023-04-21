package freecake.games.core.commands;

import freecake.games.core.AbstractGame;
import freecake.games.core.Command;
import freecake.games.core.model.Direction;

public class NavigateCommand extends AbstractCommand {

    @Override
    public String[] getRecognizedCommands() {
        String commands[] = { "w", "s", "e", "n", "sw", "se", "nw", "ne" };
        return commands;
    }

    @Override
    public boolean execute(Command command) {
        Direction direction;
        if (command.getCommandWord().equals("n")) {
            direction = Direction.north;
        } else if (command.getCommandWord().equals("e")) {
            direction = Direction.east;
        } else if (command.getCommandWord().equals("s")) {
            direction = Direction.south;
        } else if (command.getCommandWord().equals("w")) {
            direction = Direction.west;
        } else if (command.getCommandWord().equals("se")) {
            direction = Direction.southeast;
        } else if (command.getCommandWord().equals("sw")) {
            direction = Direction.southwest;
        } else if (command.getCommandWord().equals("ne")) {
            direction = Direction.northeast;
        } else if (command.getCommandWord().equals("nw")) {
            direction = Direction.northwest;
        } else {
            direction = null;
        }
        AbstractGame.instance.goInDirection(direction);
        return false;
    }
}
