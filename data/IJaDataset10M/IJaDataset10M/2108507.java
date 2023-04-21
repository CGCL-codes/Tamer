package org.xactor.tm;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * A TransactionLocal is similar to ThreadLocal except it is keyed on the
 * Transactions. A transaction local variable is cleared after the transaction
 * completes.
 *
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 37634 $
 */
public class TransactionLocal {

    /**
    * To simplify null values handling in the preloaded data pool we use 
    * this value instead of 'null'
    */
    private static final Object NULL_VALUE = new Object();

    /**
    * The transaction manager is maintained by the system and
    * manges the assocation of transaction to threads.
    */
    protected final TransactionManager transactionManager;

    /**
    * The delegate
    */
    protected TransactionLocalDelegate delegate;

    /**
    * Creates a thread local variable.
    * @throws IllegalStateException if there is no system transaction manager
    */
    public TransactionLocal() {
        try {
            InitialContext context = new InitialContext();
            transactionManager = (TransactionManager) context.lookup("java:/TransactionManager");
        } catch (NamingException e) {
            throw new IllegalStateException("An error occured while looking up the transaction manager: " + e);
        }
        initDelegate();
    }

    /**
    * Creates a transaction local variable. Using the given transaction manager
    *
    * @param tm the transaction manager
    */
    public TransactionLocal(TransactionManager tm) {
        if (tm == null) throw new IllegalArgumentException("Null transaction manager");
        this.transactionManager = tm;
        initDelegate();
    }

    /**
    * Lock the TransactionLocal using the current transaction<p>
    * 
    * WARN: The current implemention just "locks the transactions"
    * 
    * @throws IllegalStateException if the transaction is not active
    * @throws InterruptedException if the thread is interrupted
    */
    public void lock() throws InterruptedException {
        lock(getTransaction());
    }

    /**
    * Lock the TransactionLocal using the provided transaction<p>
    * 
    * WARN: The current implemention just "locks the transactions"
    * 
    * @param transaction the transaction
    * @throws IllegalStateException if the transaction is not active
    * @throws InterruptedException if the thread is interrupted
    */
    public void lock(Transaction transaction) throws InterruptedException {
        if (transaction == null) return;
        delegate.lock(this, transaction);
    }

    /**
    * Unlock the TransactionLocal using the current transaction
    */
    public void unlock() {
        unlock(getTransaction());
    }

    /**
    * Unlock the ThreadLocal using the provided transaction
    * 
    * @param transaction the transaction
    */
    public void unlock(Transaction transaction) {
        if (transaction == null) return;
        delegate.unlock(this, transaction);
    }

    /**
    * Returns the initial value for this thransaction local.  This method 
    * will be called once per accessing transaction for each TransactionLocal,
    * the first time each transaction accesses the variable with get or set. 
    * If the programmer desires TransactionLocal variables to be initialized to 
    * some value other than null, TransactionLocal must be subclassed, and this
    * method overridden. Typically, an anonymous inner class will be used. 
    * Typical implementations of initialValue will call an appropriate 
    * constructor and return the newly constructed object.
    *
    * @return the initial value for this TransactionLocal
    */
    protected Object initialValue() {
        return null;
    }

    /**
    * get the transaction local value.
    */
    protected Object getValue(Transaction tx) {
        return delegate.getValue(this, tx);
    }

    /**
    * put the value in the TransactionImpl map
    */
    protected void storeValue(Transaction tx, Object value) {
        delegate.storeValue(this, tx, value);
    }

    /**
    * does Transaction contain object?
    */
    protected boolean containsValue(Transaction tx) {
        return delegate.containsValue(this, tx);
    }

    /**
    * Returns the value of this TransactionLocal variable associated with the
    * thread context transaction. Creates and initializes the copy if this is
    * the first time the method is called in a transaction.
    *
    * @return the value of this TransactionLocal
    */
    public Object get() {
        return get(getTransaction());
    }

    /**
    * Returns the value of this TransactionLocal variable associated with the 
    * specified transaction. Creates and initializes the copy if this is the 
    * first time the method is called in a transaction.
    *
    * @param transaction the transaction for which the variable it to 
    * be retrieved
    * @return the value of this TransactionLocal
    * @throws IllegalStateException if an error occures while registering
    * a synchronization callback with the transaction
    */
    public Object get(Transaction transaction) {
        if (transaction == null) return initialValue();
        Object value = getValue(transaction);
        if (value == null) {
            value = initialValue();
            if (value == null) {
                value = NULL_VALUE;
            }
            storeValue(transaction, value);
        }
        if (value == NULL_VALUE) {
            return null;
        }
        return value;
    }

    /**
    * Sets the value of this TransactionLocal variable associtated with the 
    * thread context transaction. This is only used to change the value from
    * the one assigned by the initialValue method, and many applications will 
    * have no need for this functionality.
    *
    * @param value the value to be associated with the thread context 
    * transactions's TransactionLocal
    */
    public void set(Object value) {
        set(getTransaction(), value);
    }

    /**
    * Sets the value of this TransactionLocal variable associtated with the 
    * specified transaction. This is only used to change the value from
    * the one assigned by the initialValue method, and many applications will 
    * have no need for this functionality.
    *
    * @param transaction the transaction for which the value will be set
    * @param value the value to be associated with the thread context 
    * transactions's TransactionLocal
    */
    public void set(Transaction transaction, Object value) {
        if (transaction == null) throw new IllegalStateException("there is no transaction");
        if (!containsValue(transaction)) {
            initialValue();
        }
        if (value == null) {
            value = NULL_VALUE;
        }
        storeValue(transaction, value);
    }

    public Transaction getTransaction() {
        try {
            return transactionManager.getTransaction();
        } catch (SystemException e) {
            throw new IllegalStateException("An error occured while getting the " + "transaction associated with the current thread: " + e);
        }
    }

    /**
    * Initialise the delegate
    */
    protected void initDelegate() {
        if (transactionManager instanceof TransactionLocalDelegate) delegate = (TransactionLocalDelegate) transactionManager; else delegate = new TransactionLocalDelegateImpl(transactionManager);
    }
}
