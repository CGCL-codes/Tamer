package de.fraunhofer.isst.axbench.timing.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Eine Transaktion, also eine Menge von Tasks mit einer Periode
 *
 * @author shanno
 */
public class Transaction implements Serializable, Iterable<Task> {

    private static final long serialVersionUID = 4325643453482319231L;

    public String name;

    /**
     * Die Periode der Transaktion in ms.
     */
    public double t;

    /**
     * Die Liste der Tasks.
     */
    public Task tau[];

    /**
     * @deprecated only for XMLEncoder
     */
    public Transaction() {
        this(null, 0, (Task[]) null);
    }

    /**
     * @param name
     * @param t Die Periode der Transaktion in ms.
     * @param tau Die Liste der Tasks.
     */
    public Transaction(String name, double t, List<Task> tau) {
        this(name, t, tau.toArray(new Task[tau.size()]));
    }

    /**
     * @param transactionNo zero based!
     * @param t Die Periode der Transaktion in ms.
     * @param tau Die Liste der Tasks.
     */
    public Transaction(int transactionNo, double t, List<Task> tau) {
        this("Transaktion " + (transactionNo + 1), t, tau);
    }

    /**
     * @param t Die Periode der Transaktion in ms.
     * @param tasks Die Liste der Tasks.
     */
    public Transaction(String name, double t, Task[] tau) {
        this.name = name;
        this.t = t;
        this.tau = tau;
    }

    /**
     * @param transactionNo zero based!
     * @param t Die Periode der Transaktion in ms.
     * @param tasks Die Liste der Tasks.
     */
    public Transaction(int transactionNo, double t, Task[] tasks) {
        this("Transaktion " + (transactionNo + 1), t, tasks);
    }

    /**
     * Copy constructor (deep copy). The included Tasks are copied by using the Task copy constructor. 
     * @param toCopy the Transaction to copy.
     */
    public Transaction(Transaction toCopy) {
        this(toCopy.name, toCopy.t, createDeepCopyOfTasks(toCopy));
    }

    public static class ArrayIterator<T> implements Iterator<T> {

        private int index = 0;

        private final T[] array;

        public ArrayIterator(T[] array) {
            this.array = array;
        }

        public boolean hasNext() {
            return index < array.length;
        }

        public T next() {
            return array[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Task> iterator() {
        return new ArrayIterator<Task>(tau);
    }

    public int size() {
        return (tau != null) ? tau.length : 0;
    }

    private static Task[] createDeepCopyOfTasks(Transaction toCopy) {
        Task[] ret = new Task[toCopy.tau.length];
        for (int i = 0; i < toCopy.tau.length; i++) {
            ret[i] = new Task(toCopy.tau[i]);
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final double getT() {
        return t;
    }

    public final void setT(double t) {
        this.t = t;
    }

    /**
     * @deprecated Only for XML-Serialization
     * @return
     */
    public final Task[] getTau() {
        return tau;
    }

    /**
     * @deprecated Only for XML-Serialization
     * @param tau
     */
    public final void setTau(Task[] tau) {
        this.tau = tau;
    }

    public final Task getTau(int index) {
        return tau[index];
    }

    public final double T() {
        return t;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String toLongString() {
        String ret = name + ": ";
        ret += "t (Periode) = " + t + "\n";
        for (int i = 0; i < tau.length; i++) {
            ret += tau[i].toLongString() + "\n";
        }
        return ret;
    }

    public String toBareHtmlString() {
        String ret = "<b>" + name + ":</b><p>";
        ret += "t (Periode) = " + t + "<p>";
        for (int i = 0; i < tau.length; i++) {
            ret += tau[i].toString() + "<p>";
        }
        return ret;
    }

    public String toHtmlString() {
        return "<html>" + toBareHtmlString() + "</html>";
    }

    /**
     * Helper function to allow Matlab-similar syntax:<br>
     * transaction.tau(0) instead of transaction.getTau(0) or transaction.tau[0] 
     * @param index Index of Task
     * @return the Task
     */
    public final Task tau(int index) {
        return tau[index];
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction tr = (Transaction) o;
        if (t != tr.t) return false;
        if (!name.equals(tr.name)) return false;
        if (tau.length != tr.tau.length) return false;
        for (int i = 0; i < tau.length; i++) {
            if (!tau[i].equals(tr.tau[i])) return false;
        }
        return true;
    }

    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (name == null ? 0 : name.hashCode());
        hashCode = 31 * hashCode + (int) Double.doubleToLongBits(t);
        for (int i = 0; i < tau.length; i++) {
            Task t = tau[i];
            hashCode = 31 * hashCode + (t == null ? 0 : t.hashCode());
        }
        return hashCode;
    }

    public int indexOf(Task task) {
        return Arrays.asList(tau).indexOf(task);
    }

    public boolean contains(Task task) {
        return Arrays.asList(tau).contains(task);
    }

    /**
     * Inserts the specified element at the specified position.
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * @throws    IndexOutOfBoundsException if index is out of range
     *		  <tt>(index &lt; 0 || index &gt; size())</tt>.
     */
    public void add(int index, Task ta) {
        Task[] newTau = new Task[tau.length + 1];
        System.arraycopy(tau, 0, newTau, 0, index);
        System.arraycopy(tau, index, newTau, index + 1, tau.length - index);
        newTau[index] = ta;
        tau = newTau;
    }

    public Task[] splitTasks(int index) {
        Task[] retTau = new Task[tau.length - index];
        System.arraycopy(tau, index, retTau, 0, tau.length - index);
        Task[] newTau = new Task[index];
        System.arraycopy(tau, 0, newTau, 0, index);
        tau = newTau;
        return retTau;
    }

    public boolean remove(Task ta) {
        int index = indexOf(ta);
        if (index < 0) {
            return false;
        }
        Task[] newTau = new Task[tau.length - 1];
        System.arraycopy(tau, 0, newTau, 0, index);
        System.arraycopy(tau, index + 1, newTau, index, tau.length - index - 1);
        tau = newTau;
        return true;
    }

    /**
	 * @return Returns the predecessor task or null. The parent Transaction must be given.
	 */
    public final Task predecessorOf(Task tauIJ) {
        assert indexOf(tauIJ) != -1 : "The task tauIJ must be a member of the transaction gammaI";
        if (tauIJ.pred == 0) {
            return null;
        }
        return tau[tauIJ.pred - 1];
    }

    /**
	 * Returns the predecessor tasks in backward order as iterator. Warning: untested yet.
	 */
    public Iterator<Task> predecessorIteratorOf(final Task tauIJ) {
        assert indexOf(tauIJ) != -1 : "The task tauIJ must be a member of the transaction gammaI";
        return new Iterator<Task>() {

            Task curTask = tauIJ;

            public boolean hasNext() {
                return curTask.pred != 0;
            }

            public Task next() {
                curTask = getTau(curTask.pred - 1);
                return curTask;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
	 * Returns the predecessor tasks in backward order as iterable. Warning: untested yet.
	 */
    public Iterable<Task> predecessorsOf(final Task tauIJ) {
        return new Iterable<Task>() {

            public Iterator<Task> iterator() {
                return predecessorIteratorOf(tauIJ);
            }
        };
    }

    /** Returns a list of predecessors containing also the task itself. */
    public Set<Task> predecessorSetOf(Task tau_ij, boolean includingSelf) {
        Set<Task> ret = new HashSet<Task>();
        if (includingSelf) {
            ret.add(tau_ij);
        }
        for (Task pred : predecessorsOf(tau_ij)) {
            ret.add(pred);
        }
        return ret;
    }

    /**
	 * Returns true if the test task is one of the predecessors
	 * of the succeeding task.
	 */
    public boolean precedes(Task testTask, Task succeedingTask) {
        Set<Task> preds = predecessorSetOf(succeedingTask, false);
        return preds.contains(testTask);
    }
}
