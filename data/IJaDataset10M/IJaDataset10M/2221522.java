package genetic.component.command;

import genetic.component.context.ContextModel;
import genetic.AllComponents;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Calvin Ashmore
 */
public class AllCommands implements AllComponents<Command> {

    public List<Command> allInstances(ContextModel cm) {
        return new ArrayList<Command>();
    }
}
