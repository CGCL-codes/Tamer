package client.commands;

import client.IGameState;
import client.Player;
import client.StateContext;
import client.World;
import client.states.ViewSpellsScreen;
import client.states.ViewWorldScreen;

public class ViewWorldCommand implements ISubCommand {

    private IGameState subState;

    public ViewWorldCommand(World world, Player player) {
        subState = new ViewWorldScreen(world, player);
    }

    @Override
    public void execute(StateContext state) {
    }

    @Override
    public IGameState getSubState() {
        return subState;
    }

    public String toString() {
        return "View World Map";
    }
}
