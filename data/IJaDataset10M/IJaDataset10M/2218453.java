package net.sf.crsx.util;

import java.util.Iterator;
import net.sf.crsx.CRS;
import net.sf.crsx.Kind;
import net.sf.crsx.Term;

/**
 * Iterator to terms in CRS ;-sequence.
 * 
 * @author <a href="http://www.research.ibm.com/people/k/krisrose">Kristoffer Rose</a>.
 * @version $Id: SequenceIterator.java,v 1.7 2008/05/22 00:39:11 krisrose Exp $
 */
public class SequenceIterator implements Iterable<Term> {

    /**
	 * Whether the term is a simple sequence constructor.
	 * @param term the term that should be a sequence
	 */
    public static boolean isSequencing(Term term) {
        if (term.kind() == Kind.CONSTRUCTION) {
            switch(term.arity()) {
                case 0:
                    return term.constructor().symbol().equals(CRS.NIL_SYMBOL);
                case 2:
                    return term.constructor().symbol().equals(CRS.CONS_SYMBOL);
            }
        }
        return false;
    }

    /** Container of remaining members. */
    private Term parent;

    /** Construct iterator to all terms in sequence under parent. */
    public SequenceIterator(Term parent) {
        this.parent = parent;
    }

    public Iterator<Term> iterator() {
        return new Iterator<Term>() {

            public boolean hasNext() {
                if (parent == null) return false;
                while (isSequencing(parent) && parent.arity() == 2 && isSequencing(parent.sub(0))) {
                    assert parent.sub(0).arity() == 0 : "Cannot iterate over nested sequence.";
                    parent = parent.sub(1);
                }
                if (isSequencing(parent)) return parent.arity() > 0;
                return true;
            }

            public Term next() {
                assert parent != null : "No term!";
                if (isSequencing(parent)) {
                    switch(parent.arity()) {
                        case 1:
                            {
                                Term term = parent.sub(0);
                                parent = null;
                                return term;
                            }
                        case 2:
                            {
                                assert !isSequencing(parent.sub(0)) : "Next element must be first in sequence!";
                                Term term = parent.sub(0);
                                parent = parent.sub(1);
                                return term;
                            }
                        default:
                            return null;
                    }
                } else {
                    Term term = parent;
                    parent = null;
                    return term;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
