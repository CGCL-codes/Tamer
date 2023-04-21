package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.debug;

public interface IOcl4tstDebugEventListener {

    /**
	 * Notification that the given event occurred in the while debugging.
	 */
    public void handleMessage(fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.debug.Ocl4tstDebugMessage message);
}
