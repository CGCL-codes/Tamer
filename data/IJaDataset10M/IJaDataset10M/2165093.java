package org.subrecord.index.btree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.subrecord.exception.IndexingException;
import org.subrecord.index.EqualsConstraint;
import org.subrecord.index.Index;
import org.subrecord.model.Triple;

/**
 * @author przemek
 * 
 */
public class BTreeIndex implements Index<Serializable> {

    @Override
    public void index(Triple<Serializable> triple, Serializable value) throws IndexingException {
    }

    @Override
    public Iterator<Triple<Serializable>> query(List<EqualsConstraint> conditions, int limit) throws IndexingException {
        return null;
    }
}
