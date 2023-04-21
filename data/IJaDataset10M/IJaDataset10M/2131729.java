package org.masukomi.serverWatcher.transactions;

import java.util.Date;
import org.masukomi.serverWatcher.ServerWatcherStorage;
import org.prevayler.Transaction;

/**
 * @author Valued Customer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RemoveAimTransaction implements Transaction {

    String aimAddress;

    public RemoveAimTransaction() {
    }

    public RemoveAimTransaction(String aimAddress) {
        this.aimAddress = aimAddress;
    }

    public void executeOn(Object prevalentSystem, Date ignored) {
        ((ServerWatcherStorage) prevalentSystem).removeAimAddress(aimAddress);
    }
}
