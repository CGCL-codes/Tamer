package calclipse.caldron;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import calclipse.lib.math.mp.MPUtil;
import calclipse.lib.math.rpn.RPNException;
import calclipse.mcomp.MCContext;
import calclipse.mcomp.MComp;
import calclipse.mcomp.script.IdRegistry;

/**
 * An environment conceptually consists of an
 * {@link calclipse.mcomp.MCContext}
 * and an {@link calclipse.mcomp.script.IdRegistry}.
 * The main purpose of an environment is to block the thread during execution
 * to hold the context in place.
 * 
 * @see #LOCK
 * @see calclipse.caldron.EnvironmentContainer
 * 
 * @author T. Sommerland
 */
public final class Environment implements MComp {

    /**
     * This field is used to coordinate access to global resources,
     * in particular the current environment and the current context.
     * Before the methods
     * {@link calclipse.mcomp.MCContainer#getCurrentContext()} and
     * {@link calclipse.caldron.EnvironmentContainer#getCurrentEnvironment()}
     * are called, this lock should be locked.
     */
    public static final Lock LOCK = new ReentrantLock(true);

    private final String name;

    private final Environment parent;

    private final IdRegistry idRegistry = new IdRegistry();

    private final Object block = new Object();

    private Environment(final String name, final Environment parent) {
        this.name = name;
        this.parent = parent;
    }

    public Environment getParent() {
        return parent;
    }

    public IdRegistry getIdRegistry() {
        return idRegistry;
    }

    static void enterRoot(final String name) throws RPNException {
        final Environment root = new Environment(name, null);
        EnvironmentContainer.enter(root);
    }

    /**
     * Enters a new environment, making it the
     * {@link calclipse.caldron.EnvironmentContainer#getCurrentEnvironment()
     * current} one.
     * This method blocks until the environment is exited.
     * The {@link #LOCK} is unlocked after the transition to the new
     * environment.
     * It is locked again just before exiting the new environment.
     */
    public void enter(final String next) throws RPNException {
        final Environment child = new Environment(next, this);
        EnvironmentContainer.enter(child);
    }

    /**
     * Exits this environment.
     */
    public void exit() {
        synchronized (block) {
            block.notifyAll();
        }
    }

    @Override
    public void execute(final MCContext context) throws RPNException {
        try {
            synchronized (block) {
                LOCK.unlock();
                try {
                    block.wait();
                } catch (final InterruptedException ex) {
                    throw MPUtil.interrupt(ex);
                }
            }
        } finally {
            LOCK.lock();
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
