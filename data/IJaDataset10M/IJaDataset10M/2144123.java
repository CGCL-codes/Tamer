package net.sf.saxon.evpull;

import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.om.VirtualUntypedCopy;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.Type;
import java.util.Iterator;

/**
 * This class is an EventIterator that filters a stream of pull events setting
 * the type annotation on element nodes to xs:untyped and on attribute nodes to
 * xs:untypedAtomic
 */
public class EventAnnotationStripper implements EventIterator {

    private EventIterator base;

    /**
     * Create an EventAnnotationStripper
     * @param base the stream of events whose type annotations are to be stripped (set to untyped)
     */
    public EventAnnotationStripper(EventIterator base) {
        this.base = EventStackIterator.flatten(base);
    }

    /**
     * Get the next event in the sequence
     *
     * @return the next event, or null when the sequence is exhausted. Note that since an EventIterator is
     *         itself a PullEvent, this method may return a nested iterator.
     * @throws net.sf.saxon.trans.XPathException
     *          if a dynamic evaluation error occurs
     */
    public PullEvent next() throws XPathException {
        PullEvent pe = base.next();
        if (pe instanceof StartElementEvent) {
            StartElementEvent see = (StartElementEvent) pe;
            see.stripTypeAnnotations();
            return see;
        } else if (pe instanceof NodeInfo) {
            switch(((NodeInfo) pe).getNodeKind()) {
                case Type.ELEMENT:
                case Type.ATTRIBUTE:
                    return VirtualUntypedCopy.makeVirtualUntypedCopy((NodeInfo) pe, (NodeInfo) pe);
                default:
                    return pe;
            }
        } else {
            return pe;
        }
    }

    /**
     * Determine whether the EventIterator returns a flat sequence of events, or whether it can return
     * nested event iterators
     *
     * @return true if the next() method is guaranteed never to return an EventIterator
     */
    public boolean isFlatSequence() {
        return true;
    }
}
