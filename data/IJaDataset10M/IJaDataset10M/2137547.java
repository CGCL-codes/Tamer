package uk.ac.rothamsted.ovtk.ExtensionCore;

import java.util.List;
import uk.ac.rothamsted.ovtk.ExtensionCore.GUI.CommandListener;

/**
 * Controller that runs the command through a stack of interpreters
 * @author lysenkoa
 *
 */
public interface InterpretationController extends CommandListener {

    /**
	 * 
	 * @param an interpreter to add to the stack
	 */
    public void addInterpreter(CommandInterpreter interpreter);

    /**
	 * 
	 * @param interpreters to add to the stack
	 */
    public void addInterpreters(List<CommandInterpreter> interpreters);

    /**
	 * 
	 * @param interpreter to add
	 * @param level level to add it at
	 */
    public void insertInterpreterAtLevel(CommandInterpreter interpreter, int level);

    /**
	 * removes interpreter at a level specified
	 * @param level
	 */
    public void removeInterpreterAtLevel(int level);

    /**
	 * removes interpreter specified
	 * @param interpreter
	 */
    public void removeInterpreter(CommandInterpreter interpreter);

    /**
	 * remove all interpreters
	 * 
	 */
    public void removeAllInterpreters();
}
