package org.mushroomdb.transaction;

/**
 * @author Tomas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface TransactionalResource {

    public void onCommit();

    public void onRollback();

    public void onBeginTrx();

    public boolean isInTrx();
}
