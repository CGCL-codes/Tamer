package astcentric.structure.basic.tool;

/**
 * Synchronously implementation of the {@link Executor} interface.
 *
 */
public class ImmediateExecutor implements Executor {

    /** The one and only implementation. */
    public static final Executor EXECUTOR = new ImmediateExecutor();

    private ImmediateExecutor() {
    }

    /**
   * Immediately invokes <code>runnable.run()</code>.
   */
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
