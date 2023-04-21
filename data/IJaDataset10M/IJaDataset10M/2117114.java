package org.gamegineer.engine.core;

/**
 * A command is an abstract representation for some semantic game behavior.
 * 
 * <p>
 * Commands are instances of the Command pattern. They encapsulate game behavior
 * to be executed within the context of a game engine. Commands are the only
 * model entities that can read and modify the engine state. Thus, a game is
 * driven solely by the execution of commands.
 * </p>
 * 
 * <p>
 * In order to modify the engine state, a command must acquire the engine write
 * lock. A command implementation acquires the write lock by applying the
 * {@link CommandBehavior} annotation to the command class with the
 * {@code writeLockRequired} element set to {@code true}. Otherwise, the
 * command will not be allowed to modify the engine state. The engine will throw
 * an exception if the write lock is not acquired before the command attempts to
 * modify the engine state.
 * </p>
 * 
 * <p>
 * A command that modifies the engine state may choose to implement the
 * {@link IInvertibleCommand} interface to provide a custom implementation for
 * undoing the modifications it makes to the engine state. If a command does not
 * support this interface, the engine will provide an adapter that uses a
 * default undo implementation. The default implementation may not be as
 * efficient as a custom implementation. Therefore, implementors should
 * seriously consider providing their own inverse command implementations when
 * appropriate.
 * </p>
 * 
 * <p>
 * This interface is intended to be implemented but not extended by clients.
 * </p>
 * 
 * @param <T>
 *        The result type of the command.
 */
public interface ICommand<T> {

    public T execute(IEngineContext context) throws EngineException;

    public String getType();
}
