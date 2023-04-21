package net.sf.jukebox.service;

/**
 * The intent of this class is to shut down the service attached to it on the
 * JVM shutdown. This is possible only with JDK 1.3 and up.
 * 
 * @since Jukebox v2
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-2009
 */
public class ShutdownHandler extends Thread {

    /**
   * Service to stop in case of a system shutdown.
   */
    private StoppableService target;

    /**
   * Default constructor, exist to make {@code Class.newInstance()} happy.
   */
    public ShutdownHandler() {
        this(null);
    }

    /**
   * Create an instance.
   * 
   * @param target
   */
    public ShutdownHandler(StoppableService target) {
        this.target = target;
        Runtime.getRuntime().addShutdownHook(this);
    }

    /**
   * {@inheritDoc}
   */
    public synchronized void setTarget(StoppableService target) {
        if (this.target != null) {
            throw new IllegalStateException("Already set the target");
        }
        this.target = target;
    }

    /**
   * Stop the {@link #target target}.
   */
    @Override
    public void run() {
        if (target == null) {
            System.err.println("ShutdownHandler: oh well, you've failed to set the target before system went down...");
        }
        try {
            System.err.println("Stopping target: " + target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()));
            target.stop().waitFor();
        } catch (Throwable t) {
            System.err.println("Failed to shut down cleanly, cause:");
            t.printStackTrace();
        }
    }
}
