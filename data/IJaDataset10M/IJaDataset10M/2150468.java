package ru.flashcard4u.persistence;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 * Class fow wrapping up some code so it can be 
 * executed within a context of transaction. 
 * A piece of code is wrapped using {@link TransactionWrapper}
 * <br/>
 * <br/>
 * 
 * Usage:
 * <pre>
 * Long id = TransactionManager.runWithinTx(new TransactionWrapper<Long>() {
 *     public Long withinTx(PersistenceManager pm) {
 *         pm.makePersistent(e);
 *         return e.getId();
 *     }
 * });
 * 
 * return id;
 * </pre>
 * 
 * @author Alexey Grigorev
 * @see TransactionWrapper
 */
public class TransactionManager {

    /**
     * Runs a piece of code wrapped in {@link TransactionWrapper}.
     * A reference to PersistenceManager within {@link TransactionWrapper} is 
     * available through the first parameter of {@link TransactionWrapper#withinTx(PersistenceManager)}.
     * An example of usage see at {@link TransactionManager}
     *  
     * @param <V> returning value 
     * @param w wrapped code
     * @return what the wrapped core returned
     */
    public static <V> V runWithinTx(TransactionWrapper<V> w) {
        PersistenceManager pm = JDOManager.manager();
        Transaction tx = pm.currentTransaction();
        V result = null;
        try {
            tx.begin();
            result = w.withinTx(pm);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        return result;
    }
}
