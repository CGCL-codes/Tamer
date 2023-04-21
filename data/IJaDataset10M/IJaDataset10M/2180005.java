package edu.ups.gamedev.weapons;

import com.jme.scene.Node;

/**
 * This is a subclass of Weapon that also keeps track of a current
 * target (an object in the game world.) Fireables fired from weapons that 
 * extend this class can either be assigned a target at the time of firing,
 * or poll for new targets.
 */
public class GuidedWeapon extends Weapon {

    private static final long serialVersionUID = 7861830226861230453L;

    /**
	 * The target that is currently being locked.
	 */
    private Node lockingTarget;

    /**
	 * The weapon's current locked target.
	 */
    private Node target;

    /**
	 * When true the locking counter decrements, and if the locking counter
	 * drops to zero the target is set to the locking target.
	 */
    private boolean locking;

    /**
	 * The time in ms that it takes to lock the target from initial selection.
	 */
    private int timeTillLocked = 0;

    /**
	 * The counter that is set to timeTillLocked when locking starts.
	 * When this reaches zero target is set to lockingTarget.
	 */
    private int lockingCounter = timeTillLocked;

    public GuidedWeapon(Class fireableClass, Warhead warhead) {
        super(fireableClass, warhead, 0);
    }

    /**
	 * Sets the target that is desired to be locked.
	 */
    public void setLockingTarget(Node lockingTarget) {
        if (timeTillLocked <= 0) {
            this.target = lockingTarget;
            this.lockingTarget = lockingTarget;
        } else {
            if (this.lockingTarget == lockingTarget) {
            } else {
                lockingCounter = timeTillLocked;
                this.lockingTarget = lockingTarget;
            }
        }
    }

    /**
	 * Returns the target that is in the process of being locked.
	 */
    public Node getLockingTarget() {
        return lockingTarget;
    }

    /**
	 * Only use this if you want to bypass the built in locking mechanism.
	 * If you want to get the same effect you can also set timeTillLock to 0
	 * and call setLockingTarget
	 */
    public void setTarget(Node target) {
        this.target = target;
        this.lockingTarget = target;
    }

    /**
	 * Returns the current target.
	 */
    public Node getTarget() {
        return target;
    }

    /**
	 * Returns timeTillLock (the value that the locking counter gets set
	 * to when locking starts).
	 */
    public int getTimeTillLocked() {
        return timeTillLocked;
    }

    /**
	 * Sets timeTillLock (the value that the locking counter gets set
	 * to when locking starts).
	 */
    public void setTimeTillLocked(int timeTillLocked) {
        this.timeTillLocked = timeTillLocked;
    }

    /**
	 * Returns the time till the current lock process completes.
	 */
    public int getLockingCounter() {
        return lockingCounter;
    }

    /**
	 * You probably should not use this.
	 * Sets the time till the current lock process is completed.
	 */
    public void setLockingCounter(int lockingCounter) {
        this.lockingCounter = lockingCounter;
    }

    public boolean fire() {
        return false;
    }
}
