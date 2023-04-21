package dk.i2m.converge.core.plugin;

import dk.i2m.converge.core.content.NewsItem;
import dk.i2m.converge.core.security.UserAccount;
import dk.i2m.converge.core.workflow.WorkflowStepAction;
import java.util.Map;

/**
 * Interface for implementing a {@link WorkflowAction}.
 *
 * @author Allan Lykke Christensen
 */
public interface WorkflowAction extends Plugin {

    /**
     * Executes the {@link WorkflowAction}.
     *
     * @param ctx Context for which the workflow is being executed
     * @param item
     *          {@link NewsItem} being processed
     * @param stepAction
     *          {@link WorkflowStepAction} to be executed
     * @param user User that selected the step
     */
    public abstract void execute(PluginContext ctx, NewsItem item, WorkflowStepAction stepAction, UserAccount user);

    /**
     * Provides a map of possible properties for the action.
     *
     * @return Map of possible action properties
     */
    public abstract Map<String, String> getAvailableProperties();
}
