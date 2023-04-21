package genetictest;

import genetic.component.context.Context;
import genetic.component.command.Command;

/**
 *
 * @author Calvin Ashmore
 */
public class Command2 extends Command {

    @Override
    public void execute(Context context, Object... parameters) {
        System.out.println("Command2 called " + parameters[0]);
    }

    @Override
    public int getNumberInputs() {
        return 1;
    }

    @Override
    public String getInputName(int i) {
        return "commandInput";
    }

    @Override
    public Class getInputType(int i) {
        return Integer.TYPE;
    }
}
