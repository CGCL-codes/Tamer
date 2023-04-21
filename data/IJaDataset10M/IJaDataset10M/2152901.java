package fmpp.dataloaders;

import java.util.List;
import org.apache.tools.ant.Task;
import fmpp.Engine;

/**
 * Returns the FMPP Ant task object. 
 */
public class AntTaskDataLoader extends AntDataLoader {

    public Object load(Engine eng, List args, Task task) {
        if (args.size() != 0) {
            throw new IllegalArgumentException("antTask data loader has no parameters");
        }
        return task;
    }
}
