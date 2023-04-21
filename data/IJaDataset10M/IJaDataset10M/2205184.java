package royere.cwi.structure;

import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.*;
import royere.cwi.util.ArrayListHelper;

/** 
 * StoredTraversal is simply a list of elements. The storage is designed for
 * speed of access and iteration 
 */
public class StoredTraversal extends Traversal implements java.io.Serializable {

    ArrayList theList = new ArrayList();

    public StoredTraversal(String name) {
        super(name);
    }

    public int getNumberOfElements() {
        return theList.size();
    }

    public void add(Element element) {
        if (entryPoint == null) {
            entryPoint = element;
            exitPoint = element;
            ArrayListHelper.addUnique(theList, element);
        } else {
            ArrayListHelper.addUnique(theList, element);
            exitPoint = element;
        }
    }

    public void remove(Element element) {
        theList.remove(element);
    }

    protected ArrayList getList() {
        return theList;
    }

    public Iterator iterator() {
        return new StoredIterator(theList);
    }

    public boolean contains(Element elt) {
        return theList.contains(elt);
    }

    /**
   * Returns an iterator starting from Element startFrom.
   * Returns null if Element startFrom is not in Traversal
   */
    public Iterator iterator(Element startFrom) {
        return new StoredIterator(theList, startFrom);
    }

    public Iterator iterator(Selector selector) {
        return new StoredIterator(theList, selector);
    }

    /** creates an iterator which selects only nodes */
    public FilteredIterator nodeIterator() {
        return (FilteredIterator) iterator(new ClassSelector(Node.class));
    }

    /** creates an iterator which selects only edges */
    public FilteredIterator edgeIterator() {
        return (FilteredIterator) iterator(new ClassSelector(Edge.class));
    }

    /** creates an iterator which selects only graphs */
    public FilteredIterator graphIterator() {
        return (FilteredIterator) iterator(new ClassSelector(Graph.class));
    }

    protected class StoredIterator extends FilteredIterator {

        private ListIterator theIterator;

        private ArrayList theList = null;

        private int listIndex = 0;

        private Selector selector = null;

        private Element cachedNextElt = null;

        private Element cachedPrevElt = null;

        public StoredIterator(ListIterator theIterator) {
            super(null, null, null);
            this.theIterator = theIterator;
        }

        public StoredIterator(ListIterator theIterator, Element startFrom) {
            super(null, null, null);
            int startIndex = theList.indexOf(startFrom);
            theIterator = theList.listIterator(startIndex);
            listIndex = startIndex;
        }

        public StoredIterator(ArrayList theList) {
            super(null, null, null);
            this.theList = theList;
        }

        public StoredIterator(ArrayList theList, Element startFrom) {
            super(null, null, null);
            this.theList = theList;
            listIndex = theList.indexOf(startFrom);
        }

        public StoredIterator(ArrayList theList, Selector selector) {
            super(null, null, null);
            this.theList = theList;
            this.selector = selector;
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            if (cachedNextElt != null) return true;
            cachedNextElt = (Element) next();
            if (cachedNextElt != null) return true;
            return false;
        }

        public boolean hasPrevious() {
            if (cachedPrevElt != null) return true;
            cachedPrevElt = previous();
            if (cachedPrevElt != null) return true;
            return false;
        }

        public Object next() {
            if (cachedNextElt != null) {
                Element temp = cachedNextElt;
                cachedNextElt = null;
                return temp;
            }
            Element elt = null;
            for (; listIndex < theList.size(); listIndex++) {
                elt = (Element) theList.get(listIndex);
                if (selector == null || selector.meetsCriteria(elt)) {
                    listIndex++;
                    return elt;
                }
            }
            return null;
        }

        public Element previous() {
            if (cachedPrevElt != null) {
                Element temp = cachedPrevElt;
                cachedPrevElt = null;
                return temp;
            }
            Element elt = null;
            for (; listIndex >= 0; listIndex--) {
                elt = (Element) theList.get(listIndex);
                if (selector == null || selector.meetsCriteria(elt)) {
                    listIndex--;
                    return elt;
                }
            }
            return null;
        }
    }
}
