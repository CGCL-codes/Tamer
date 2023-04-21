package net.sf.cplab.sequence;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

/**
 * @author jtse
 *
 */
public class SequencePoolImpl extends GenericKeyedObjectPool implements SequencePool {

    public SequencePoolImpl(SequenceFactory factory) {
        super(factory);
    }

    public Sequence borrowSequence(String key) throws Exception {
        return (Sequence) borrowObject(key);
    }

    public void returnSequence(String key, Sequence sequence) throws Exception {
        returnObject(key, sequence);
    }
}
