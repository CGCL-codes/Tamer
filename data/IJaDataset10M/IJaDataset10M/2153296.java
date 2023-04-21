package org.hardtokenmgmt.core.token;

/**
 * Class used to give information about the status
 * of a certain PIN and it's PUK codes.
 * 
 * @author Philip Vendil 2006-aug-30
 *
 * @version $Id$
 */
public class PINInfo {

    boolean loginRequired = true;

    boolean pINCountLow = false;

    boolean pINFinalTry = false;

    boolean pINLocked = false;

    boolean pUKCountLow = false;

    boolean pUKFinalTry = false;

    boolean pUKLocked = false;

    /**
	 * 
	 * 
	 * @param loginRequired
	 * @param pINCountLow
	 * @param pINFinalTry
	 * @param pINLocked
	 * @param pUKCountLow
	 * @param pUKFinalTry
	 * @param pUKLocked
	 */
    public PINInfo(boolean loginRequired, boolean pINCountLow, boolean pINFinalTry, boolean pINLocked, boolean pUKCountLow, boolean pUKFinalTry, boolean pUKLocked) {
        super();
        this.loginRequired = loginRequired;
        this.pINCountLow = pINCountLow;
        this.pINFinalTry = pINFinalTry;
        this.pINLocked = pINLocked;
        this.pUKCountLow = pUKCountLow;
        this.pUKFinalTry = pUKFinalTry;
        this.pUKLocked = pUKLocked;
        if (this.pINFinalTry) {
            this.pINCountLow = false;
        }
        if (this.pINLocked) {
            this.pINCountLow = false;
            this.pINFinalTry = false;
        }
        if (this.pUKFinalTry) {
            this.pUKCountLow = false;
        }
        if (this.pUKLocked) {
            this.pUKCountLow = false;
            this.pUKFinalTry = false;
        }
    }

    /**
	 * Is true if login in required to the PIN area inorder to make writable
	 * operations
	 * 
	 * @return Returns the loginRequired.
	 */
    public boolean isLoginRequired() {
        return loginRequired;
    }

    /**
	 * Indicates if at least on false login have been done.
	 * 
	 * @return Returns the pINCountLow.
	 */
    public boolean isPINCountLow() {
        return pINCountLow;
    }

    /**
	 * Indicates of this is the last attempt
	 * 
	 * @return Returns the pINFinalTry.
	 */
    public boolean isPINFinalTry() {
        return pINFinalTry;
    }

    /**
	 * Indicates if the PIN is blocked
	 * 
	 * @return Returns the pINLocked.
	 */
    public boolean isPINLocked() {
        return pINLocked;
    }

    /**
	 * Returns true if PIN have the maximum number of tries left
	 * can be used to indicate that a login went successful
	 * 
	 * Is the same as !(isPINCountLow || isPINFinalTry || isPINLocked)
	 */
    public boolean isPINOK() {
        return !(isPINCountLow() || isPINFinalTry() || isPINLocked());
    }

    /**
	 * Indicates if at least on false login have been done.
	 * 
	 * @return Returns the pUKCountLow.
	 */
    public boolean isPUKCountLow() {
        return pUKCountLow;
    }

    /**
	 * Indicates of this is the last attempt
	 * 
	 * @return Returns the pUKFinalTry.
	 */
    public boolean isPUKFinalTry() {
        return pUKFinalTry;
    }

    /**
	 * Indicates if the PUK is blocked
	 * 
	 * @return Returns the pUKLocked.
	 */
    public boolean isPUKLocked() {
        return pUKLocked;
    }

    /**
	 * Returns true if PUK have the maximum number of tries left
	 * can be used to indicate that a login went successful
	 * 
	 * Is the same as !(isPUKCountLow || isPUKFinalTry || isPUKLocked)
	 */
    public boolean isPUKOK() {
        return !(isPUKCountLow() || isPUKFinalTry() || isPUKLocked());
    }
}
