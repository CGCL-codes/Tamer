package org.archive.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import org.apache.commons.collections.Predicate;

/**
 * @author gojomo
 */
public class DiskBackedDeque extends DiskBackedQueue implements Deque, Serializable {

    protected DiskStack stack;

    /**
     * @param dir
     * @param name
     * @param reuse
     * @param headMax
     * @throws IOException
     */
    public DiskBackedDeque(File dir, String name, boolean reuse, int headMax) throws IOException {
        super(dir, name, reuse, headMax);
        stack = new DiskStack(new File(dir, name + ".top"));
    }

    public void push(Object object) {
        headQ.addFirst(object);
        enforceHeadSize();
    }

    /**
     * Ensure that only the chosen maximum number of
     * items are held in memory, pushing any excess
     * to the stack as necessary.
     */
    private void enforceHeadSize() {
        while (headQ.size() > headMax) {
            stack.push(headQ.removeLast());
        }
    }

    /**
     * Set the number of items to keep in memory,
     * and adjust current head to match.
     *
     * @param hm
     */
    public void setHeadMax(int hm) {
        super.setHeadMax(hm);
        enforceHeadSize();
    }

    public Object pop() {
        return dequeue();
    }

    public Object peek() {
        throw new UnsupportedOperationException("no peek for deque");
    }

    public long height() {
        return length();
    }

    protected Object backingDequeue() {
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return tailQ.dequeue();
    }

    protected void discardBacking() {
        super.discardBacking();
        stack.release();
    }

    public long deleteMatchedItems(Predicate matcher) {
        return super.deleteMatchedItems(matcher);
    }

    public Iterator getIterator(boolean inCacheOnly) {
        return super.getIterator(inCacheOnly);
    }

    protected int headTargetSize() {
        return super.headTargetSize() / 2;
    }

    public boolean isEmpty() {
        return super.isEmpty() && stack.isEmpty();
    }

    public long length() {
        return super.length() + stack.height();
    }

    public void disconnect() {
        super.disconnect();
        stack.disconnect();
    }

    /**
     * @return
     */
    public int memoryLoad() {
        return headQ.size();
    }
}
