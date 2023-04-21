package fr.lelouet.server.perf.snapshot;

import fr.lelouet.tools.Configurable;

/**
 * can be notified of modification of {@link HVSnapshot} or
 * {@link ActivityReport}.
 */
public interface SnapshotReceiver extends Configurable {

    /**
	 * notification of a modification of the state or of the list of VMs in an
	 * hypervisor.
	 * <p>
	 * This is different from
	 * <code>resourceModification(hypervisor, hypervisorName)</code> because it
	 * considers that the internal list of vms of the hypervisor may have
	 * changed.
	 * </p>
	 * 
	 * @param hypervisor
	 *            the {@link HVSnapshot} that has been modified.
	 */
    void hypervisorModification(HVSnapshot hypervisor, String name);

    /** notification of the modification of the resource usages */
    void resourceModification(ActivityReport snapshot, String name);
}
