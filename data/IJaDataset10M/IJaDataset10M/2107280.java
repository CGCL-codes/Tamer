package launcher.contentpanel.runnerviews;

import javax.swing.JComponent;
import launcher.model.CommandTemplate;
import launcher.runtime.ExecutingCommand;

/**
 * Handles the view aspect for a running {@link CommandTemplate}
 * 
 * @author Ramon Servadei
 * @version $Revision: 1.4 $
 * 
 */
public interface RunnerView {

    JComponent getViewComponent();

    /**
   * Exposed so that internal runner view components can send a message to the
   * main runner view to display. Typically, there may be many running
   * {@link CommandTemplate} objects so the executing command identifies itself
   * as the source.
   * 
   * @param source
   * @param message
   */
    void displayMessage(ExecutingCommand source, String message);

    /**
   * Get the {@link ExecutingCommand} that is being displayed.
   * 
   * @return
   */
    ExecutingCommand getExecutingCommand();

    /**
   * Display the executing <code>command</code>. This may be the process
   * output or some other aspect.
   * 
   * @param command
   */
    void displayExecutingCommand(ExecutingCommand command);

    /**
   * Triggered when <code>command</code> has finished
   * 
   * @param command
   */
    void processHasTerminated(ExecutingCommand command);

    /**
   * Triggered when <code>command</code> has started or is running
   * 
   * @param command
   */
    void processIsRunning(ExecutingCommand command);

    /**
   * Set the title for the process running view
   * 
   * @param command
   */
    void setProcessTitle(ExecutingCommand command);

    /**
   * Signal that the process is in an unknown state
   * 
   * @param command
   */
    void processInUnknownState(ExecutingCommand command);
}
