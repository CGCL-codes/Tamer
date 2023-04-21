package muse.external.console;

import java.util.List;
import muse.external.model.ProcessListner;

/**
 * Bridge of Program Command Line type .
 * Method getCommandLine() returns List of console parameters.
 *
 * @author Korchak
 */
public interface ProgramBridge {

    /**
	 * Getting command line.
	 * @return List of String console parameters. 
	 */
    List getCommandLine();

    /**
	 * Adding listener to bridge for getting information about process execution.
	 * @param processListner - listener of bridge process. 
	 */
    void addProcessListener(ProcessListner processListner);
}
