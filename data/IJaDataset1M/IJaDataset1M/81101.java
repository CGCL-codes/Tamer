package net.sourceforge.mystopwatch.elog;

import org.junit.Test;

/**
 * Adding <code>null</code> listeners or removing non existing listeners will be
 * detected by the event log registry.
 */
public class Test_EL_IU_4_InvalidListenerRegistration {

    /**
	 * Cannot add a <code>null</code> listener.
	 * <ol>
	 * <li>Create an event log registry.</li>
	 * <li>Add a <code>null</code> listener.</li>
	 * </ol>
	 * 
	 * @throws Exception failed
	 */
    @Test(expected = IllegalArgumentException.class)
    public void t1_addNullListener() throws Exception {
        ELogRegistry elr = new ELogRegistry();
        elr.addListener(null);
    }

    /**
	 * Cannot remove a <code>null</code> listener.
	 * <ol>
	 * <li>Create an event log registry.</li>
	 * <li>Remove a <code>null</code> listener.</li>
	 * </ol>
	 * 
	 * @throws Exception failed
	 */
    @Test(expected = IllegalArgumentException.class)
    public void t2_removingNullListener() throws Exception {
        ELogRegistry elr = new ELogRegistry();
        elr.removeListener(null);
    }

    /**
	 * Cannot remove a listener which has not been registered.
	 * <ol>
	 * <li>Create an event log registry.</li>
	 * <li>Create an event log registry listener.</li>
	 * <li>Remove the listener (without have registed it first).</li>
	 * </ol>
	 * 
	 * @throws Exception failed
	 */
    @Test(expected = IllegalArgumentException.class)
    public void t3_removingUnexistingListener() throws Exception {
        ELogRegistry elr = new ELogRegistry();
        DataKeeperListener dkl = new DataKeeperListener();
        elr.removeListener(dkl);
    }
}
