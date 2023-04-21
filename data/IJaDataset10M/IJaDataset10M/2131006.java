package jlibs.xml.xsd.sequences;

import jlibs.core.graph.sequences.AbstractSequence;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

/**
 * @author Santhosh Kumar T
 */
public class XSObjectListSequence<E extends XSObject> extends AbstractSequence<E> {

    private XSObjectList list;

    public XSObjectListSequence(XSObjectList list) {
        this.list = list;
        _reset();
    }

    private int i;

    @Override
    @SuppressWarnings({ "unchecked" })
    protected E findNext() {
        return (E) list.item(++i);
    }

    @Override
    public void reset() {
        super.reset();
        _reset();
    }

    private void _reset() {
        i = -1;
    }

    @Override
    public XSObjectListSequence<E> copy() {
        return new XSObjectListSequence<E>(list);
    }

    @Override
    public int length() {
        return list.getLength();
    }
}
