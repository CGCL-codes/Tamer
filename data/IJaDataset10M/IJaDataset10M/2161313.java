package name.angoca.db2sa.cli.jline;

import name.angoca.db2sa.ExecutionState;
import name.angoca.db2sa.cli.AbstractInterfaceController;
import name.angoca.db2sa.cli.InputReader;
import name.angoca.db2sa.cli.OutputWriter;
import name.angoca.db2sa.cli.exceptions.InputReaderException;
import name.angoca.db2sa.cli.system.SystemOutputWriter;
import name.angoca.db2sa.core.InterfaceCore;
import name.angoca.db2sa.exceptions.AbstractDB2SAException;
import name.angoca.db2sa.messages.Messages;

/**
 * This is the interface controller that manage the read and the printer.<br/>
 * <b>Control Version</b><br />
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.0.2 Annotations.</li>
 * <li>0.0.3 Execution state.</li>
 * <li>0.0.4 Enum.</li>
 * <li>0.0.5 Read string just once.</li>
 * <li>0.0.6 Name of a state.</li>
 * </ul>
 * 
 * @author Andr�s G�mez
 * @version 0.0.6 09/06/2009
 */
public class JlineInterfaceController extends AbstractInterfaceController {

    /**
	 * Input processor.
	 */
    private final InputReader m_input;

    /**
	 * Screen printer.
	 */
    private final OutputWriter m_output;

    /**
	 * Constructor that creates a reader and a printer.
	 * 
	 * @param prompt
	 *            Prompt to show in each line of the console.@throws
	 *            InputReaderException When there is a problem establishing the
	 *            input or the output.
	 * @throws InputReaderException
	 *             When there is a problem establishing the input or the output.
	 */
    public JlineInterfaceController(final String prompt) throws InputReaderException {
        super();
        this.m_output = new SystemOutputWriter();
        this.m_input = new JlineInputReader(prompt);
    }

    /**
	 * Start to read commands from the user and process them.
	 * 
	 * @throws AbstractDB2SAException
	 *             When there is a IO problem.
	 */
    @Override
    public final void start() throws AbstractDB2SAException {
        String phrase = null;
        this.m_output.writeLine(Messages.getString("JlineInterfaceController.introduction"));
        ExecutionState execState = ExecutionState.UNKNOWN;
        while (execState != ExecutionState.APPLICATION_EXIT) {
            phrase = this.m_input.readString();
            execState = InterfaceCore.executeCommand(phrase, this.m_output);
        }
    }
}
