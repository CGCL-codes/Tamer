package net.sf.leechget.database.au;

/**
 * @author Rogiel
 *
 */
public interface AutomaticUpdate {

    /**
	 * @return true if continues to update, false if not
	 */
    public boolean update();

    public long interval();
}
