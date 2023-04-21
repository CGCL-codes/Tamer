package model.command;

import model.Command;
import model.Reference;
import controller.RuntimeState;

public class Brz extends Command {

    private static final long serialVersionUID = 8813191619683631004L;

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public String getCommandName() {
        return "BRZ";
    }

    @Override
    public void run(RuntimeState runtimeState, Reference... refs) {
        if (runtimeState.getAccumulator() == 0) {
            runtimeState.setPointer(refs[0].getAddress());
        }
    }
}
