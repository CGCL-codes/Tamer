package net.sf.jukebox.sem;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import net.sf.jukebox.util.PackageNameStripper;
import net.sf.jukebox.util.SimpleQueue;

/**
 * Allows to wait for multiple semaphores. Doesn't allow to trigger multiple
 * semaphores at once, because
 * <ol>
 * <li>I don't see much need to do that;
 * <li>It's not so complicated as multiple-wait, just put them into the
 * container and iterate through.
 * </ol>
 * VT: FIXME: This class, though works perfectly, contains some uncoolness
 * related to a confusion between <code>EventSemaphore</code> and
 * <code>Semaphore</code>. Should be looked at.
 *
 * @version $Id: SemaphoreGroup.java,v 1.2 2007-06-14 04:32:18 vtt Exp $
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim
 * Tkachenko</a> 1995-1998
 */
public class SemaphoreGroup extends Semaphore implements EventListener {

    /**
     * All semaphores belonging to this group.
     */
    protected final Set<EventSemaphore> group = new HashSet<EventSemaphore>();

    /**
     * Queue for semaphores being posted.
     */
    protected final SimpleQueue<EventSemaphore> posted = new SimpleQueue<EventSemaphore>();

    /**
     * Default constructor.
     */
    public SemaphoreGroup() {
        super(null);
    }

    /**
     * Creates named SemaphoreGroup.
     *
     * @param groupName The name assigned for the group.
     */
    public SemaphoreGroup(String groupName) {
        super(groupName);
    }

    /**
     * Creates named SemaphoreGroup with default name.
     * <p>
     * The default name is
     * {@code owner.getClass().getName()+"/"+Integer.toHexString(owner.hashCode())}.
     *
     * @param owner The object that gives the name to this semaphore.
     */
    public SemaphoreGroup(Object owner) {
        super(owner);
    }

    /**
     * Creates named SemaphoreGroup with default name and the string name
     * appended to it, *
     * {@code owner.getClass().getName()+"/"+Integer.toHexString(owner.hashCode())+"/"+qualifier}
     *
     * @param owner Semaphore owner.
     * @param qualifier The additional name.
     */
    public SemaphoreGroup(Object owner, String qualifier) {
        super(owner, qualifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Add the {@link Semaphore semaphore} to the group.
     *
     * @param ES Semaphore to add to the group.
     */
    public synchronized void add(EventSemaphore ES) {
        if (!group.contains(ES)) {
            group.add(ES);
            ES.addListener(this);
        }
    }

    /**
     * Removes the {@link Semaphore semaphore} from the group.
     *
     * @param ES Semaphore to remove from the group
     */
    public synchronized void remove(EventSemaphore ES) {
        if (!group.remove(ES)) {
            throw new IllegalArgumentException("remove(): " + Integer.toHexString(ES.hashCode()) + ": semaphore was NOT in the group " + Integer.toHexString(hashCode()));
        }
        ES.removeListener(this);
    }

    /**
     * Wait for all semaphores in the group for the specified amount of time.
     *
     * @param millis Time to wait, in milliseconds.
     * @return true if all semaphores have been posted, false if at least one
     * semaphore has been cleared or timed out.
     * @exception SemaphoreTimeoutException if timed out.
     * @see Semaphore
     */
    public boolean waitForAll(long millis) throws SemaphoreTimeoutException {
        throw new SemaphoreTimeoutException("Not Implemented");
    }

    /**
     * Wait forever for all semaphores in the group to trigger.
     * <p>
     * The semaphores which were {@link EventSemaphore#isTriggered triggered}
     * before this method was called take part in the group wait, too. In other
     * words, if all the semaphores in the group were triggered before the call,
     * it returns immediately.
     *
     * @return <code>true</code>.
     * @exception InterruptedException if the process has been interrupted.
     * @see Semaphore
     */
    public synchronized boolean waitForAll() throws InterruptedException {
        if (group.isEmpty()) {
            throw new IllegalStateException("waitForAll() on the empty group");
        }
        Set<EventSemaphore> localGroup = new HashSet<EventSemaphore>(group);
        if (!checkBehind(localGroup, false, false)) {
            return false;
        }
        while (localGroup.size() > 0) {
            EventSemaphore esPosted = posted.waitObject();
            localGroup.remove(esPosted);
        }
        localGroup = null;
        return true;
    }

    /**
     * Check if some semaphores from the group were already triggered.
     * <p>
     * This method is to be called from <code>waitForAll(..)</code> methods
     * only.
     * <p>
     * <strong>Side effect:</code> <code>localGroup</code> parameter is by
     * reference, not by value, and gets modified if required - the semaphores
     * that were already triggered with the right value are removed from it.
     *
     * @param localGroup HashSet of the semaphores to check.
     * @param checkValue true if I want to check the third parameter, the value.
     * @param value Value to check against.
     * @return <code>false</code> if I want to check for values and at least
     * one of the values wasn't the one I want, <code>true</code> otherwise.
     */
    private boolean checkBehind(Set<EventSemaphore> localGroup, boolean checkValue, boolean value) {
        for (Iterator<EventSemaphore> e = localGroup.iterator(); e.hasNext(); ) {
            EventSemaphore sem = e.next();
            if (sem.isTriggered()) {
                if (checkValue && sem.getStatus() != value) {
                    return false;
                }
                e.remove();
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitFor() throws InterruptedException {
        return waitForAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitFor(long millis) throws SemaphoreTimeoutException {
        return waitForAll(millis);
    }

    /**
     * Wait forever for all semaphores in the group to trigger with a specified
     * value.
     * <p>
     * This method is equivalent to {@link #waitForAll(boolean, boolean)
     * waitForAll(value, true)}.
     *
     * @param value Value to wait for.
     * @return true if all semaphores have been posted with a specified value,
     * false otherwise.
     * @throws InterruptedException if the wait is interrupted.
     */
    public boolean waitForAll(boolean value) throws InterruptedException {
        return waitForAll(value, true);
    }

    /**
     * Wait forever for all semaphores in the group to trigger with a specified
     * value.
     * <p>
     * The semaphores which were {@link EventSemaphore#isTriggered triggered}
     * before this method was called take part in the group wait, too. In other
     * words, if all the semaphores in the group were triggered before the call,
     * it returns immediately with the proper value.
     *
     * @param value Value to wait for.
     * @param returnImmediately if {@code true}, first value that is not the
     * value specified as a first parameter will cause the method to return with
     * {@code false}. Otherwise, it will still return {@code false}, but only
     * after <strong>all</strong> the semaphores have triggered.
     * @return true if all semaphores have been posted with a specified value,
     * false otherwise.
     * @exception InterruptedException if process has been interrupted.
     * @see Semaphore
     */
    public synchronized boolean waitForAll(boolean value, boolean returnImmediately) throws InterruptedException {
        if (group.isEmpty()) {
            throw new IllegalStateException("waitForAll(" + value + ") on the empty group");
        }
        Set<EventSemaphore> localGroup = new HashSet<EventSemaphore>(group);
        boolean result = true;
        if (!checkBehind(localGroup, true, value)) {
            if (returnImmediately) {
                return false;
            }
            result = false;
        }
        while (localGroup.size() > 0) {
            EventSemaphore esPosted = posted.waitObject();
            if (!localGroup.contains(esPosted)) {
                if (!group.contains(esPosted)) {
                    throw new Error("waitForAll(): not in group: " + esPosted.toString());
                }
            }
            if (esPosted.getStatus() != value) {
                if (returnImmediately) {
                    return false;
                }
                result = false;
            }
            localGroup.remove(esPosted);
        }
        localGroup = null;
        return result;
    }

    /**
     * Wait forever for the first semaphore from the group to be triggered.
     * <p>
     * Unlike the {@link #waitForAll() waitForAll()} behavior, this method waits
     * for the first semaphore {@link EventSemaphore#isTriggered triggered}
     * <b>after</b> the method was called.
     *
     * @return the semaphore that has been triggered.
     * @exception InterruptedException if the thread which the semaphore waits
     * in has been interrupted.
     * @see EventSemaphore
     */
    public synchronized EventSemaphore waitForOne() throws InterruptedException {
        if (group.isEmpty()) {
            throw new IllegalStateException("waitForOne() on the empty group");
        }
        EventSemaphore esPosted = posted.waitObject();
        if (!group.contains(esPosted)) {
            throw new Error("waitForOne(): " + Integer.toHexString(esPosted.hashCode()) + ": semaphore is not in group " + Integer.toHexString(hashCode()));
        }
        return esPosted;
    }

    /**
     * Wait forever for the first semaphore from the group to be triggered with
     * a desired status. <br>
     * Good to use in cases when "one of many" condition means success, as
     * opposed to "all of them".
     * <p>
     * Unlike the {@link #waitForAll() waitForAll()} behavior, this method waits
     * for the first semaphore {@link EventSemaphore#isTriggered triggered}
     * <b>after</b> the method was called.
     *
     * @param value The desired semaphore status.
     * @return the first semaphore that has been triggered with a desired
     * status, or null if no semaphores in the group were triggered with that
     * status.
     * @exception InterruptedException if the thread which the semaphore waits
     * in has been interrupted.
     * @see EventSemaphore
     */
    public synchronized EventSemaphore waitForOne(boolean value) throws InterruptedException {
        if (group.isEmpty()) {
            throw new IllegalStateException("waitForOne(" + value + ") on the empty group");
        }
        Set<EventSemaphore> localGroup = new HashSet<EventSemaphore>(group);
        while (localGroup.size() > 0) {
            EventSemaphore esPosted = posted.waitObject();
            if (!localGroup.contains(esPosted)) {
                if (!group.contains(esPosted)) {
                    throw new Error("waitForOne(" + value + "): semaphore is not in group");
                }
            }
            if (esPosted.getStatus() == value) {
                return esPosted;
            }
            localGroup.remove(esPosted);
        }
        localGroup = null;
        return null;
    }

    /**
     * Wait for the first semaphore from the group to be triggered.
     *
     * @param millis Time to wait, in milliseconds.
     * @exception SemaphoreTimeoutException if timed out.
     * @return the semaphore that has been triggered.
     * @see EventSemaphore
     */
    public EventSemaphore waitForOne(long millis) throws SemaphoreTimeoutException {
        throw new SemaphoreTimeoutException("Not Implemented");
    }

    /**
     * Remove all semaphores from the group.
     */
    public synchronized void clear() {
        for (Iterator<EventSemaphore> i = group.iterator(); i.hasNext(); ) {
            EventSemaphore es = i.next();
            es.removeListener(this);
            i.remove();
        }
    }

    /**
     * Check if the semaphore is in the group. This is internal method, and
     * that's why the implementation is so cruel: throwing the unchecked
     * exception. If this happens, it means that there's been some programming
     * error, not a normal condition.
     * <p>
     * This method is intended to be used only from within {@code wait*}
     * methods.
     *
     * @param sem Semaphore to check for. @{@link #group group} is being
     * checked.
     * @exception Error if the semaphore checked doesn't belong to the
     * {@link #group group}.
     */
    protected void checkContains(Semaphore sem) {
        if (!group.contains(sem)) {
            throw new IllegalArgumentException("waitFor*: not in group: " + sem);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String result = PackageNameStripper.stripPackage(getClass().getName());
        if (!"".equals(name)) {
            result += "[" + name + "]";
        }
        result += " (";
        Iterator<EventSemaphore> e = group.iterator();
        while (e.hasNext()) {
            EventSemaphore es = e.next();
            result += " " + es.getName() + "." + Integer.toHexString(es.hashCode());
        }
        e = null;
        result += " )";
        return result;
    }

    /**
     * Receive the notification about the semaphore triggered.
     * <p>
     * It doesn't make a sense to make this method synchronized because the
     * {@link #posted underlying queue} access is already synchronized, and all
     * that happens before is just a couple of sanity checks.
     *
     * @param producer The semaphore being triggered. Must belong to the group,
     * or the <code>Error</code> will be thrown.
     * @param status Status to notify with.
     * @exception IllegalArgumentException if the producer is not a {@link
     * Semaphore Semaphore}. See {@link #checkContains contains}.
     */
    public void eventNotification(Object producer, Object status) {
        if (!(producer instanceof EventSemaphore)) {
            throw new IllegalArgumentException("producer should be the EventSemaphore");
        }
        checkContains((EventSemaphore) producer);
        if (!(status instanceof Boolean)) {
            throw new IllegalArgumentException("status should be the Boolean");
        }
        posted.put((EventSemaphore) producer);
    }

    /**
     * @return number of the semaphores in the group.
     */
    public int size() {
        return group.size();
    }
}
