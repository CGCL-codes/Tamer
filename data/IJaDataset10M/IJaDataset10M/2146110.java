package net.sf.isolation.threading;

import net.sf.isolation.core.IsoInformation;

/**
 * <p>
 * Class for manage thread. For example when need sleep a thread an manage them
 * with other one.
 * </p>
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>YES</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 * @see Thread
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 104 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/threading/IsoThreadNode.java $", lastChangedDate = "$LastChangedDate: 2009-09-16 00:40:22 -0400 (Wed, 16 Sep 2009) $")
public abstract class IsoThreadNode {

    /**
	 * Reference to the creator {@link Thread}
	 */
    private final Thread thread;

    /**
	 * Constructs a <code>IsoThreadNode</code> with a reference to execution
	 * thread
	 * 
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    protected IsoThreadNode(Thread thread) {
        this.thread = thread;
    }

    /**
	 * Interrupts the referenced thread
	 * 
	 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
	 * @since 1.0
	 */
    protected final void interrupt() {
        try {
            thread.interrupt();
        } catch (RuntimeException exception) {
        }
    }
}
