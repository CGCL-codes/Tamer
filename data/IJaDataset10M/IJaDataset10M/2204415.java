package org.echarts;

import org.echarts.util.FIFOImpl;

/**
  This represents the common operations one can perform on a port that
  is local to an EChart.  FSM message transitions can only refer to
  LocalPort instances.
*/
public abstract class LocalPort extends Port {

    public static final long serialVersionUID = 1;

    /**
	   Queue into which messages are placed by peer ports.
	*/
    protected FIFOImpl inputQueue;

    public LocalPort() {
        inputQueue = new FIFOImpl();
    }

    public FIFOImpl getInputQueue() {
        return inputQueue;
    }

    /**
	  Reference to least common ancestor machine of all machines in
	  which there exists an enabled message transition waiting on this
	  port in the machine's current state.
	 */
    private Machine lcaMachineReference = null;

    /**
	   Flag indicating whether or not above machine reference is the
	   only machine for which there exist enabled message transitions
	   waiting on this port in the machine's current state.
	 */
    private boolean uniqueMachineReference = false;

    /**
	   Number of transitions currently referencing this port.
	 */
    private int referenceCount = 0;

    /**
	   If port is referenced in a machine that is a descendant of at
	   least one dynamic machine then this value references the first
	   such dynamic parent machine encountered traversing in depth
	   first order. This value is referenced only by
	   DynamicMachine.getMaxEnabledMessageTransition().
	 */
    private DynamicMachine dynamicParent = null;

    /**
	   If port is referenced in a machine that is a descendant of at
	   least one dynamic machine then this value references the index
	   of the first such dynamic machine instance in the first dynamic
	   parent machine encountered traversing in depth first
	   order. This value is referenced only by
	   DynamicMachine.getMaxEnabledMessageTransition().
	 */
    private int dynamicChildIndex = -1;

    /**
	   Sets machine reference to least common ancestor of current
	   reference and 'machine' parameter.

	   Also adds a reference to the max dynamic submachine that exists
	   on path to a machine that defines a transition that references
	   this port. This is used by
	   DynamicMachine.getMaxEnabledMessageTransition() to localize a
	   submachine containing an enabled message transition referencing
	   this port. If a value is already set, then new values are
	   ignored. It is assumed that new values exist at the same or
	   lower levels in the machine hierarchy relative to the current
	   value.
	 */
    protected void setMachineReference(Machine machine, DynamicMachine dynamicParent, int dynamicChildIndex) throws MachineException {
        referenceCount = referenceCount + 1;
        if (lcaMachineReference == null) {
            lcaMachineReference = machine;
            uniqueMachineReference = true;
        } else if (machine != lcaMachineReference) {
            uniqueMachineReference = false;
            lcaMachineReference = machine.leastCommonAncestor(lcaMachineReference);
        }
        if (this.dynamicParent == null) {
            this.dynamicParent = dynamicParent;
            this.dynamicChildIndex = dynamicChildIndex;
        }
    }

    protected void clearMachineReference(Machine machine) throws MachineException {
        if (referenceCount > 0) {
            referenceCount = referenceCount - 1;
        } else {
            throw new MachineException("Attempt to decrement LocalPort reference count below 0: " + this);
        }
        if ((uniqueMachineReference && lcaMachineReference == machine) || referenceCount == 0) {
            lcaMachineReference = null;
            uniqueMachineReference = false;
        }
        dynamicParent = null;
        dynamicChildIndex = -1;
    }

    protected DynamicMachine getDynamicParent() {
        return dynamicParent;
    }

    protected int getDynamicChildIndex() {
        return dynamicChildIndex;
    }

    protected boolean isUniquelyReferencedByMachine(Machine machine) {
        boolean returnVal = false;
        if (uniqueMachineReference && (machine == lcaMachineReference)) {
            returnVal = true;
        }
        return returnVal;
    }

    protected boolean isUniquelyReferenced() {
        return uniqueMachineReference;
    }

    /**
	   Used in Machine.getMaxEnabledMessageTransition() to guide search
	   for enabledMessageTransitions. Returns true if 'machine' param
	   is submachine or supermachine of lowest common ancestor machine
	   referencing this port. Also true if no LCA machine defined for
	   this port. Otherwise returns false.
	 */
    protected boolean isPossiblyReferencedBy(Machine machine) throws MachineException {
        boolean returnVal = true;
        if (lcaMachineReference != null) {
            returnVal = lcaMachineReference.code.isSubmachineOf(machine.code);
            if (returnVal == false) {
                returnVal = machine.code.isSubmachineOf(lcaMachineReference.code);
            }
        }
        return returnVal;
    }

    /**
	   Used by Machine.getMaxEnabledMessageTransition to determine if
	   neccessary to look for additional enabled message
	   transitions. Returns true if least common ancestor machine of
	   machines referencing this port is a supermachine of the
	   'machine' param. Also returns true if no LCA machine is defined
	   for this port. Otherwise returns false.
	 */
    protected boolean isPossiblyReferencedBySupermachineOf(Machine machine) throws MachineException {
        boolean returnVal = true;
        if (lcaMachineReference != null) {
            returnVal = machine.code.isSubmachineOf(lcaMachineReference.code);
        }
        return returnVal;
    }

    /**
	   Returns port properties for this port.
	 */
    public abstract PortProperties getMonitorProperties();
}
