package cz.cuni.mff.abacs.burglar.logics.objects.positions;

/**
 * Interface of objects that can be opened, closed, locked, unlocked.
 * 
 * @author abacs
 *
 */
public interface Lockable {

    /** 
	 * Returns success indicator.
	 * 
	 * @return success indicator
	 */
    public boolean open();

    /** 
	 * Returns success indicator.
	 * 
	 * @return success indicator
	 */
    public boolean close();

    /** 
	 * Returns success indicator.
	 * 
	 * @param keyId
	 * @return success indicator
	 */
    public boolean unlock(int keyId);

    /** 
	 * Returns success indicator.
	 * 
	 * @param keyId
	 * @return success indicator
	 */
    public boolean lock(int keyId);

    /**
	 * If no key is set, returns 0.
	 * 
	 * @return 0 if not set.
	 */
    public int getKeyId();

    /**  
	 * Whether the object is closed.
	 */
    public boolean isClosed();

    /**
	 * Whether the object is locked.
	 */
    public boolean isLocked();
}
