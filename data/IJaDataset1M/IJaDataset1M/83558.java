package edu.udo.cs.ai.trude.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import edu.udo.cs.ai.trude.predicates.Predicate;
import edu.udo.cs.ai.trude.util.AbstractUnmodifiableSet;

/**
 * The result of a rename applied to a IndexedRelation.
 *
 * @author oflasch
 */
class RenamedRelation extends AbstractUnmodifiableSet<Tuple> implements IndexedRelation {

    protected final IndexedRelation rename;

    public RenamedRelation(final IndexedRelation relation, final String oldName, final String newName) {
        try {
            relation.getHeader().getAttributeTemplate(oldName);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("the old name must be contained in the relation header");
        }
        this.rename = makeRename(relation, oldName, newName);
    }

    protected IndexedRelation makeRename(final IndexedRelation relation, final String oldName, final String newName) {
        final Set<Header> renamedIndexSpecs = makeRenamedIndexSpecs(relation.getIndexSpecs(), oldName, newName);
        final Set<Tuple> renamedTuples = makeRenamedTuples(relation, oldName, newName);
        return new StoredRelation(relation.getHeader().rename(oldName, newName), renamedIndexSpecs, renamedTuples);
    }

    protected Set<Header> makeRenamedIndexSpecs(final Set<Header> indexSpecs, final String oldName, final String newName) {
        final Set<Header> renamedIndexSpecs = new HashSet<Header>();
        for (Header indexSpec : indexSpecs) {
            try {
                indexSpec.getAttributeTemplate(oldName);
                renamedIndexSpecs.add(indexSpec.rename(oldName, newName));
            } catch (NoSuchElementException e) {
                renamedIndexSpecs.add(indexSpec);
            }
        }
        return renamedIndexSpecs;
    }

    protected Set<Tuple> makeRenamedTuples(final Relation relation, final String oldName, final String newName) {
        final Set<Tuple> renamedTuples = new HashSet<Tuple>();
        for (Tuple tuple : relation) {
            renamedTuples.add(tuple.rename(oldName, newName));
        }
        return renamedTuples;
    }

    public Set<Header> getIndexSpecs() {
        return rename.getIndexSpecs();
    }

    public Map<Header, Index> getIndices() {
        return rename.getIndices();
    }

    public boolean isEmpty() {
        return rename.isEmpty();
    }

    public int dimension() {
        return rename.dimension();
    }

    public int cardinality() {
        return rename.cardinality();
    }

    public Header getHeader() {
        return rename.getHeader();
    }

    public IndexedRelation restrict(final Predicate predicate) {
        return rename.restrict(predicate);
    }

    public IndexedRelation project(final Header subHeader) {
        return rename.project(subHeader);
    }

    public IndexedRelation join(final IndexedRelation relation) {
        return rename.join(relation);
    }

    public IndexedRelation union(final IndexedRelation relation) {
        return rename.union(relation);
    }

    public IndexedRelation rename(final String oldName, final String newName) {
        return rename.rename(oldName, newName);
    }

    public IndexedRelation subtract(final IndexedRelation subtrahend) {
        return rename.subtract(subtrahend);
    }

    public int size() {
        return rename.size();
    }

    public boolean contains(final Object o) {
        return rename.contains(o);
    }

    public Iterator<Tuple> iterator() {
        return rename.iterator();
    }

    public Object[] toArray() {
        return rename.toArray();
    }

    public <T> T[] toArray(final T[] typeProvider) {
        return rename.toArray(typeProvider);
    }

    public boolean containsAll(final Collection<?> c) {
        return rename.containsAll(c);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("<Renamed Relation ");
        sb.append(rename.toString());
        sb.append(">");
        return sb.toString();
    }
}
