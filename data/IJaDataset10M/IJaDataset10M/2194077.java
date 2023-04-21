package net.sourceforge.basher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/** Holder class for a <code>Task</code> and <code>TaskConfiguration</code> combination.  Class also manages basic
 * statistics for the invocation of a class, such as invocation counts etc.
 * @author Johan Lindquist
 */
public class TaskExecutionContext {

    /** Unique identifier associated with this context.
     *
     */
    private UUID _identifier;

    /**
     *
     */
    private Task _task;

    /**
     *
     */
    private TaskConfiguration _taskConfiguration;

    private Set<TaskExecutionContext> _followers = new HashSet<TaskExecutionContext>();

    /**
     * instance failure counter
     */
    private volatile int _failures = 0;

    /**
     * instance success counter
     */
    private volatile int _successes = 0;

    /**
     * instance not run counter
     */
    private volatile int _notRun = 0;

    private AtomicInteger _reservations = new AtomicInteger(0);

    /**
     *
     * @param task
     * @deprecated
     */
    public TaskExecutionContext(final Task task) {
        _task = task;
    }

    /** Creates a new <code>TaskExecutionContext</code>, wrapping the specified task and task configuration.
     *
     * @param identifier Identifier of the context, used for referencing this context
     * @param task The task to wrap
     * @param taskConfiguration The configuration of the task
     */
    public TaskExecutionContext(final UUID identifier, final Task task, final TaskConfiguration taskConfiguration) {
        _identifier = identifier;
        _task = task;
        _taskConfiguration = taskConfiguration;
    }

    /**
     * Retrieves the task embedded in this <code>TaskExecutionContext</code>.
     *
     * @return The embedded task
     */
    public Task getTask() {
        return _task;
    }

    public TaskConfiguration getTaskConfiguration() {
        return _taskConfiguration;
    }

    /**
     * Retrieves the number of failed invocation of the embedded task.
     *
     * @return The number of failed invocations.
     */
    public int getFailures() {
        return _failures;
    }

    /**
     * Retrieves the number of successful invocations of the embedded task.
     *
     * @return The number of successful invocations.
     */
    public int getSuccesses() {
        return _successes;
    }

    /**
     * Retrieves the number times the task chose to not run.  The task <strong>must</strong> throw a <code>NotRunException</code>
     * for this counter to be incremented.
     *
     * @return The number of times the embedded task chose to not run.
     */
    public int getNotRun() {
        return _notRun;
    }

    public void reserveInvocation() {
        _reservations.incrementAndGet();
    }

    public void releaseInvocation() {
        _reservations.decrementAndGet();
    }

    /**
     * Retrieves the number of times this task was invoked (successes+failures).
     *
     * @return The number of invocations.
     */
    public int getInvocations() {
        return _reservations.get() + _successes + _failures;
    }

    /** Main method that should be called by the component that executes tasks.  This method ensures that
     * counters of failures/successes are kept up to date for the task in question.
     *
     * @throws Throwable Any exception thrown by the embedded task when invoking the <code>executeTask</code> method are
     * re-thrown by this method.
     */
    public void executeTask() throws Throwable {
        try {
            _task.executeTask();
            _successes++;
            _taskConfiguration.reCalculateWeight();
        } catch (TaskNotRunException e) {
            _notRun++;
            throw e;
        } catch (Throwable throwable) {
            _failures++;
            throw throwable;
        }
    }

    public UUID getIdentifier() {
        return _identifier;
    }

    public void setIdentifier(final UUID identifier) {
        _identifier = identifier;
    }

    public void addFollower(final TaskExecutionContext taskExecutionContext) {
        _followers.add(taskExecutionContext);
    }

    public void clearFollowers() {
        _followers.clear();
    }

    public Set<TaskExecutionContext> getFollowers() {
        return new HashSet<TaskExecutionContext>(_followers);
    }

    @Override
    public String toString() {
        return "TaskExecutionContext{" + "_identifier=" + _identifier + ", _task=" + _task + ", _taskConfiguration=" + _taskConfiguration + ", _failures=" + _failures + ", _successes=" + _successes + ", _notRun=" + _notRun + ", _reservations=" + _reservations + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskExecutionContext)) {
            return false;
        }
        final TaskExecutionContext that = (TaskExecutionContext) o;
        if (_failures != that._failures) {
            return false;
        }
        if (_notRun != that._notRun) {
            return false;
        }
        if (_successes != that._successes) {
            return false;
        }
        if (_identifier != null ? !_identifier.equals(that._identifier) : that._identifier != null) {
            return false;
        }
        if (_reservations != null ? !_reservations.equals(that._reservations) : that._reservations != null) {
            return false;
        }
        if (_task != null ? !_task.equals(that._task) : that._task != null) {
            return false;
        }
        if (_taskConfiguration != null ? !_taskConfiguration.equals(that._taskConfiguration) : that._taskConfiguration != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = _identifier != null ? _identifier.hashCode() : 0;
        result = 31 * result + (_task != null ? _task.hashCode() : 0);
        result = 31 * result + (_taskConfiguration != null ? _taskConfiguration.hashCode() : 0);
        result = 31 * result + _failures;
        result = 31 * result + _successes;
        result = 31 * result + _notRun;
        result = 31 * result + (_reservations != null ? _reservations.hashCode() : 0);
        return result;
    }
}
