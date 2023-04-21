package org.eclipse.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * A central repository for commands -- both in the defined and undefined
 * states. Commands can be created and retrieved using this manager. It is
 * possible to listen to changes in the collection of commands by attaching a
 * listener to the manager.
 * </p>
 * 
 * @see CommandManager#getCommand(String)
 * @since 3.1
 */
public final class CommandManager implements ICategoryListener, ICommandListener {

    /**
	 * A listener that forwards incoming execution events to execution listeners
	 * on this manager. The execution events will come from any command on this
	 * manager.
	 * 
	 * @since 3.1
	 */
    private final class ExecutionListener implements IExecutionListener {

        public final void notHandled(final String commandId, final NotHandledException exception) {
            if (executionListeners != null) {
                final int executionListenersSize = executionListeners.size();
                if (executionListenersSize > 0) {
                    final IExecutionListener[] listeners = (IExecutionListener[]) executionListeners.toArray(new IExecutionListener[executionListenersSize]);
                    for (int i = 0; i < executionListenersSize; i++) {
                        final IExecutionListener listener = listeners[i];
                        listener.notHandled(commandId, exception);
                    }
                }
            }
        }

        public final void postExecuteFailure(final String commandId, final ExecutionException exception) {
            if (executionListeners != null) {
                final int executionListenersSize = executionListeners.size();
                if (executionListenersSize > 0) {
                    final IExecutionListener[] listeners = (IExecutionListener[]) executionListeners.toArray(new IExecutionListener[executionListenersSize]);
                    for (int i = 0; i < executionListenersSize; i++) {
                        final IExecutionListener listener = listeners[i];
                        listener.postExecuteFailure(commandId, exception);
                    }
                }
            }
        }

        public final void postExecuteSuccess(final String commandId, final Object returnValue) {
            if (executionListeners != null) {
                final int executionListenersSize = executionListeners.size();
                if (executionListenersSize > 0) {
                    final IExecutionListener[] listeners = (IExecutionListener[]) executionListeners.toArray(new IExecutionListener[executionListenersSize]);
                    for (int i = 0; i < executionListenersSize; i++) {
                        final IExecutionListener listener = listeners[i];
                        listener.postExecuteSuccess(commandId, returnValue);
                    }
                }
            }
        }

        public final void preExecute(final String commandId, final ExecutionEvent event) {
            if (executionListeners != null) {
                final int executionListenersSize = executionListeners.size();
                if (executionListenersSize > 0) {
                    final IExecutionListener[] listeners = (IExecutionListener[]) executionListeners.toArray(new IExecutionListener[executionListenersSize]);
                    for (int i = 0; i < executionListenersSize; i++) {
                        final IExecutionListener listener = listeners[i];
                        listener.preExecute(commandId, event);
                    }
                }
            }
        }
    }

    /**
	 * The map of category identifiers (<code>String</code>) to categories (
	 * <code>Category</code>). This collection may be empty, but it is never
	 * <code>null</code>.
	 */
    private final Map categoriesById = new HashMap();

    /**
	 * The collection of listeners to this command manager. This collection is
	 * <code>null</code> if there are no listeners.
	 */
    private Collection commandManagerListeners = null;

    /**
	 * The map of command identifiers (<code>String</code>) to commands (
	 * <code>Command</code>). This collection may be empty, but it is never
	 * <code>null</code>.
	 */
    private final Map commandsById = new HashMap();

    /**
	 * The set of identifiers for those categories that are defined. This value
	 * may be empty, but it is never <code>null</code>.
	 */
    private final Set definedCategoryIds = new HashSet();

    /**
	 * The set of identifiers for those commands that are defined. This value
	 * may be empty, but it is never <code>null</code>.
	 */
    private final Set definedCommandIds = new HashSet();

    /**
	 * The execution listener for this command manager. This just forwards
	 * events from commands controlled by this manager to listeners on this
	 * manager.
	 */
    private IExecutionListener executionListener = null;

    /**
	 * The collection of execution listeners. This collection is
	 * <code>null</code> if there are no listeners.
	 */
    private Collection executionListeners = null;

    /**
	 * Adds a listener to this command manager. The listener will be notified
	 * when the set of defined commands changes. This can be used to track the
	 * global appearance and disappearance of commands.
	 * 
	 * @param listener
	 *            The listener to attach; must not be <code>null</code>.
	 */
    public final void addCommandManagerListener(final ICommandManagerListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (commandManagerListeners == null) {
            commandManagerListeners = new ArrayList(1);
        } else if (!commandManagerListeners.contains(listener)) {
            return;
        }
        commandManagerListeners.add(listener);
    }

    /**
	 * Adds an execution listener to this manager. This listener will be
	 * notified if any of the commands controlled by this manager execute. This
	 * can be used to support macros and instrumentation of commands.
	 * 
	 * @param listener
	 *            The listener to attach; must not be <code>null</code>.
	 */
    public final void addExecutionListener(final IExecutionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null execution listener");
        }
        if (executionListeners == null) {
            executionListeners = new ArrayList(1);
            executionListener = new ExecutionListener();
            final Iterator commandItr = commandsById.values().iterator();
            while (commandItr.hasNext()) {
                final Command command = (Command) commandItr.next();
                command.addExecutionListener(executionListener);
            }
        } else if (executionListeners.contains(listener)) {
            return;
        }
        executionListeners.add(listener);
    }

    public final void categoryChanged(CategoryEvent categoryEvent) {
        if (categoryEvent.isDefinedChanged()) {
            final Category category = categoryEvent.getCategory();
            final String categoryId = category.getId();
            final boolean categoryIdAdded = category.isDefined();
            if (categoryIdAdded) {
                definedCategoryIds.add(categoryId);
            } else {
                definedCategoryIds.remove(categoryId);
            }
            fireCommandManagerChanged(new CommandManagerEvent(this, null, false, false, categoryId, categoryIdAdded, true));
        }
    }

    public final void commandChanged(final CommandEvent commandEvent) {
        if (commandEvent.isDefinedChanged()) {
            final Command command = commandEvent.getCommand();
            final String commandId = command.getId();
            final boolean commandIdAdded = command.isDefined();
            if (commandIdAdded) {
                definedCommandIds.add(commandId);
            } else {
                definedCommandIds.remove(commandId);
            }
            fireCommandManagerChanged(new CommandManagerEvent(this, commandId, commandIdAdded, true, null, false, false));
        }
    }

    /**
	 * Notifies all of the listeners to this manager that the set of defined
	 * command identifiers has changed.
	 * 
	 * @param commandManagerEvent
	 *            The event to send to all of the listeners; must not be
	 *            <code>null</code>.
	 */
    private final void fireCommandManagerChanged(final CommandManagerEvent commandManagerEvent) {
        if (commandManagerEvent == null) throw new NullPointerException();
        if (commandManagerListeners != null) {
            final int commandManagerListenersSize = commandManagerListeners.size();
            if (commandManagerListenersSize > 0) {
                final ICommandManagerListener[] listeners = (ICommandManagerListener[]) commandManagerListeners.toArray(new ICommandManagerListener[commandManagerListenersSize]);
                for (int i = 0; i < commandManagerListenersSize; i++) {
                    final ICommandManagerListener listener = listeners[i];
                    listener.commandManagerChanged(commandManagerEvent);
                }
            }
        }
    }

    /**
	 * Gets the category with the given identifier. If no such category
	 * currently exists, then the category will be created (but be undefined).
	 * 
	 * @param categoryId
	 *            The identifier to find; must not be <code>null</code>.
	 * @return The category with the given identifier; this value will never be
	 *         <code>null</code>, but it might be undefined.
	 * @see Category
	 */
    public final Category getCategory(final String categoryId) {
        if (categoryId == null) throw new NullPointerException();
        Category category = (Category) categoriesById.get(categoryId);
        if (category == null) {
            category = new Category(categoryId);
            categoriesById.put(categoryId, category);
            category.addCategoryListener(this);
        }
        return category;
    }

    /**
	 * Gets the command with the given identifier. If no such command currently
	 * exists, then the command will be created (but will be undefined).
	 * 
	 * @param commandId
	 *            The identifier to find; must not be <code>null</code> and
	 *            must not be zero-length.
	 * @return The command with the given identifier; this value will never be
	 *         <code>null</code>, but it might be undefined.
	 * @see Command
	 */
    public final Command getCommand(final String commandId) {
        if (commandId == null) {
            throw new NullPointerException("A command may not have a null identifier");
        }
        if (commandId.length() < 1) {
            throw new IllegalArgumentException("The command must not have a zero-length identifier");
        }
        Command command = (Command) commandsById.get(commandId);
        if (command == null) {
            command = new Command(commandId);
            commandsById.put(commandId, command);
            command.addCommandListener(this);
            if (executionListener != null) {
                command.addExecutionListener(executionListener);
            }
        }
        return command;
    }

    /**
	 * Returns the set of identifiers for those category that are defined.
	 * 
	 * @return The set of defined category identifiers; this value may be empty,
	 *         but it is never <code>null</code>.
	 */
    public final Set getDefinedCategoryIds() {
        return Collections.unmodifiableSet(definedCategoryIds);
    }

    /**
	 * Returns the set of identifiers for those commands that are defined.
	 * 
	 * @return The set of defined command identifiers; this value may be empty,
	 *         but it is never <code>null</code>.
	 */
    public final Set getDefinedCommandIds() {
        return Collections.unmodifiableSet(definedCommandIds);
    }

    /**
	 * Removes a listener from this command manager.
	 * 
	 * @param listener
	 *            The listener to be removed; must not be <code>null</code>.
	 */
    public final void removeCommandManagerListener(final ICommandManagerListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (commandManagerListeners == null) {
            return;
        }
        commandManagerListeners.remove(listener);
        if (commandManagerListeners.isEmpty()) {
            commandManagerListeners = null;
        }
    }

    /**
	 * Removes an execution listener from this command manager.
	 * 
	 * @param listener
	 *            The listener to be removed; must not be <code>null</code>.
	 */
    public final void removeExecutionListener(final IExecutionListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot remove a null listener");
        }
        if (executionListeners == null) {
            return;
        }
        executionListeners.remove(listener);
        if (executionListeners.isEmpty()) {
            executionListeners = null;
            final Iterator commandItr = commandsById.values().iterator();
            while (commandItr.hasNext()) {
                final Command command = (Command) commandItr.next();
                command.removeExecutionListener(executionListener);
            }
            executionListener = null;
        }
    }

    /**
	 * Block updates all of the handlers for all of the commands. If the handler
	 * is <code>null</code> or the command id does not exist in the map, then
	 * the command becomes unhandled. Otherwise, the handler is set to the
	 * corresponding value in the map.
	 * 
	 * @param handlersByCommandId
	 *            A map of command identifiers (<code>String</code>) to
	 *            handlers (<code>IHandler</code>). This map may be
	 *            <code>null</code> if all handlers should be cleared.
	 *            Similarly, if the map is empty, then all commands will become
	 *            unhandled.
	 */
    public final void setHandlersByCommandId(final Map handlersByCommandId) {
        final Iterator commandIdItr = handlersByCommandId.keySet().iterator();
        while (commandIdItr.hasNext()) {
            getCommand((String) commandIdItr.next());
        }
        final Iterator commandItr = commandsById.values().iterator();
        while (commandItr.hasNext()) {
            final Command command = (Command) commandItr.next();
            final String commandId = command.getId();
            final Object value = handlersByCommandId.get(commandId);
            if (value instanceof IHandler) {
                command.setHandler((IHandler) value);
            } else {
                command.setHandler(null);
            }
        }
    }
}
