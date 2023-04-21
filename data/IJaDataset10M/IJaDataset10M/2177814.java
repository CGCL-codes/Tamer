package jgnash.engine.event;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jgnash.util.WorkQueue;

/**
 * This class works similar to Observable, all Observers are stored as weak refrences
 * which makes clean up easy.  The Observer can register to listen to 4 different types
 * of messages (System, Account, Transaction, Commodity).<p>
 *
 * The segmentation of the message types reduces the number of unwanted messages
 * a <b>WeakObserver</b> receives.  A <b>WeakObserver</b> can be added to more than one type of 
 * list.  This class does not prevent a <b>WeakObserver</b> from being added more than 
 * once to the same list.
 * <p>
 * $Id: WeakObservable.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class WeakObservable {

    private ArrayList accountList = new ArrayList();

    private ArrayList transactionList = new ArrayList();

    private ArrayList systemList = new ArrayList();

    private ArrayList currencyList = new ArrayList();

    private ArrayList reminderList = new ArrayList();

    protected WorkQueue workQueue = new WorkQueue(1);

    /**
     * Adds a WeakObserver to the list of observers for this object
     *
     * This is also a good time to check for any dead observers
     *
     * @param   o       a WeakObserver to be added.
     * @param   list    list to add observer to
     */
    private void addObserver(WeakObserver o, List list) {
        synchronized (list) {
            list.add(new WeakReference(o));
            purgeWeakObservers(list);
        }
    }

    /** Adds a WeakObserver to the account event list
     * @param o new observer to add to the account event list
     */
    public void addAccountObserver(WeakObserver o) {
        addObserver(o, accountList);
    }

    /** Adds a WeakObserver to the transaction event list
     * @param o observer to add to the transaction event list
     */
    public void addTransactionObserver(WeakObserver o) {
        addObserver(o, transactionList);
    }

    /** Adds a WeakObserver to the commodity event list
     * @param o observer to add to the commodity event list
     */
    public void addCommodityObserver(WeakObserver o) {
        addObserver(o, currencyList);
    }

    /** Adds a WeakObserver to the reminder event list
     * @param o observer to add to the system event list
     */
    public void addReminderObserver(WeakObserver o) {
        addObserver(o, reminderList);
    }

    /** Adds a WeakObserver to the system event list
     * @param o observer to add to the system event list
     */
    public void addSystemObserver(WeakObserver o) {
        addObserver(o, systemList);
    }

    /** Removes all references that are no longer accessable
     * @param list list to purge
     */
    private void purgeWeakObservers(List list) {
        synchronized (list) {
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Object o = ((Reference) i.next()).get();
                if (o == null) {
                    i.remove();
                }
            }
        }
    }

    /** Deletes a WeakObserver from the set of observers of this object.  This 
     * will also remove an dead observers it finds along the way.
     * @param  o    the observer to be deleted.
     * @parm list   list to look through
     */
    private void removeObserver(WeakObserver o, ArrayList list) {
        synchronized (list) {
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                Object obj = ((Reference) i.next()).get();
                if (obj == null || obj == o) {
                    i.remove();
                }
            }
        }
    }

    /** Remove an account event observer
     * @param o Observer to remove from the account event list.
     */
    public void removeAccountObserver(WeakObserver o) {
        removeObserver(o, accountList);
    }

    /** Remove a currency event observer
     * @param o Observer to remove from the currency event list.
     */
    public void removeCommodityObserver(WeakObserver o) {
        removeObserver(o, currencyList);
    }

    /** Remove a reminder event observer
     * @param o Observer to remove from the system event list.
     */
    public void removeReminderObserver(WeakObserver o) {
        removeObserver(o, reminderList);
    }

    /** Remove a system event observer
     * @param o Observer to remove from the system event list.
     */
    public void removeSystemObserver(WeakObserver o) {
        removeObserver(o, systemList);
    }

    /** Remove a transaction event observer
     * @param o Observer to remove from the transaction event list.
     */
    public void removeTransactionObserver(WeakObserver o) {
        removeObserver(o, transactionList);
    }

    /** Notifies all WeakObservers of the specified list with he specified
     * jgnashEvent object.<br>
     * Iteration is performed on a clone of the observer list to prevent
     * concurrent modification errors.
     * @param event The event to send to the observers
     * @param list  The list of observers to send the event to
     */
    protected void notifyObservers(jgnashEvent event, ArrayList list) {
        ArrayList tList;
        synchronized (list) {
            purgeWeakObservers(list);
            tList = (ArrayList) list.clone();
        }
        WeakObserver ref;
        for (Iterator i = tList.iterator(); i.hasNext(); ) {
            ref = (WeakObserver) ((Reference) i.next()).get();
            if (ref != null) {
                ref.update(this, event);
            }
        }
    }

    /** This is a method to start an account event thread.
     * @param event The event to send to account observers
     */
    protected void notifyAccountObservers(jgnashEvent event) {
        workQueue.execute(new NotifyListeners(event, accountList));
    }

    /** This is a method to start a currency event thread
     * @param event The event to send to currency observers
     */
    protected void notifyCurrencyObservers(jgnashEvent event) {
        workQueue.execute(new NotifyListeners(event, currencyList));
    }

    /** This is a method to start a system event thread
     * @param event The event to send to system observers
     */
    protected void notifyReminderObservers(jgnashEvent event) {
        workQueue.execute(new NotifyListeners(event, reminderList));
    }

    /** This is a method to start a transaction event thread
     * @param event The event to send to transaction observers
     */
    protected void notifyTransactionObservers(jgnashEvent event) {
        workQueue.execute(new NotifyListeners(event, transactionList));
    }

    /** This is a method to start a system event thread
     * @param event The event to send to system observers
     */
    protected void notifySystemObservers(jgnashEvent event) {
        workQueue.execute(new NotifyListeners(event, systemList));
    }

    /** This nested class is used to update any listeners in a sperate thread.
     */
    public final class NotifyListeners implements Runnable {

        jgnashEvent event;

        ArrayList list;

        NotifyListeners(jgnashEvent event, ArrayList list) {
            this.event = event;
            this.list = list;
        }

        public void run() {
            notifyObservers(event, list);
        }
    }
}
