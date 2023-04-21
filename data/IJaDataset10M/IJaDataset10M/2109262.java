package net.sf.ediknight.common.edi.directory;

import java.util.Iterator;
import java.util.Stack;
import net.sf.ediknight.edi.directory.Message;
import net.sf.ediknight.edi.directory.SegmentEnumerator;
import net.sf.ediknight.edi.directory.SegmentNode;

/**
 * @author Holger Joest
 */
public final class SegmentEnumeratorImpl implements SegmentEnumerator {

    private Stack<SegmentVisitor> segmentStack = new Stack<SegmentVisitor>();

    private String skipSegmentId;

    /**
     * Creates a segment enumerator for a given message.
     *
     * @param message the message
     */
    SegmentEnumeratorImpl(Message message) {
        Iterator<SegmentNode> iterator = message.getSegmentNodes().iterator();
        if (iterator.hasNext()) {
            segmentStack.push(new SegmentVisitor(iterator, 1));
        }
    }

    /**
     * {@inheritDoc}
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements() {
        skipSegmentId = null;
        return !segmentStack.isEmpty();
    }

    /**
     * {@inheritDoc}
     * @see net.sf.ediknight.edi.directory.SegmentEnumerator
     *      #hasMoreElements(java.lang.String)
     */
    public boolean hasMoreElements(String segmentId) {
        if (segmentStack.isEmpty()) {
            return false;
        }
        skipSegmentId = segmentId;
        SegmentVisitor visitor = segmentStack.peek();
        SegmentNodeImpl head = findHeadSegment(visitor.next);
        while (!segmentId.equals(head.getId())) {
            iterate();
            if (segmentStack.isEmpty()) {
                return false;
            }
            visitor = segmentStack.peek();
            head = findHeadSegment(visitor.next);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * @see net.sf.ediknight.edi.directory.SegmentEnumerator#getOccurs()
     */
    public int getOccurs() {
        if (segmentStack.isEmpty()) {
            return 0;
        }
        SegmentVisitor visitor = segmentStack.peek();
        return visitor.occurs;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Enumeration#nextElement()
     */
    public SegmentNode nextElement() {
        if (segmentStack.isEmpty()) {
            return null;
        }
        SegmentVisitor visitor = segmentStack.peek();
        SegmentNodeImpl result = visitor.next;
        iterate();
        return result;
    }

    /**
     * Iterate to the next element.
     */
    private void iterate() {
        if (segmentStack.isEmpty()) {
            return;
        }
        SegmentVisitor visitor = segmentStack.peek();
        SegmentNodeImpl head = findHeadSegment(visitor.next);
        SegmentVisitor childrenVisitor = null;
        if (visitor.next.getSegmentCount() > 0) {
            if (skipSegmentId == null || skipSegmentId.equals(head.getId())) {
                childrenVisitor = new SegmentVisitor(visitor.next.getSegmentNodes().iterator(), 1);
            }
        }
        if (visitor.occurs++ < visitor.next.getMaxOccurs() && (skipSegmentId == null || skipSegmentId.equals(head.getId()))) {
        } else {
            visitor.occurs = 1;
            if (visitor.iterator.hasNext()) {
                SegmentNode nextSegmentNode = visitor.iterator.next();
                visitor.next = (SegmentNodeImpl) nextSegmentNode;
            } else {
                segmentStack.pop();
            }
        }
        if (childrenVisitor != null) {
            segmentStack.push(childrenVisitor);
        }
    }

    /**
     * An internal class for iteration.
     */
    private final class SegmentVisitor {

        private Iterator<SegmentNode> iterator;

        private SegmentNodeImpl next;

        private int occurs;

        /**
         * @param iterator the iterator
         * @param occurs the number of occurrences
         */
        private SegmentVisitor(Iterator<SegmentNode> iterator, int occurs) {
            this.iterator = iterator;
            this.next = (SegmentNodeImpl) iterator.next();
            this.occurs = occurs;
        }
    }

    /**
     * @param segmentNode the segment to start from
     * @return the head segment
     */
    private SegmentNodeImpl findHeadSegment(SegmentNodeImpl segmentNode) {
        if (segmentNode.getSegmentCount() == 0) {
            return segmentNode;
        }
        return findHeadSegment((SegmentNodeImpl) segmentNode.getSegmentNodes().get(0));
    }
}
