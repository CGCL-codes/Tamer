package g4mfs.impl.org.peertrust.strategy;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;
import g4mfs.impl.org.peertrust.meta.Tree;
import java.util.Vector;

/**
 * <p>
 * Specific queue with a simple FIFO (First In First Out) strategy.
 * </p><p>
 * $Id: FIFOQueue.java,v 1.1 2005/11/30 10:35:14 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:14 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla
 */
public class FIFOQueue implements Queue, Configurable {

    Vector _queue;

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#add(trust.meta.Tree)
	 */
    public synchronized void add(Tree tree) {
        _queue.add(tree);
    }

    public void init() throws ConfigurationException {
        _queue = new Vector();
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#add(trust.meta.Tree)
	 */
    public synchronized void add(Tree[] trees) {
        for (int i = 0; i < trees.length; i++) _queue.add(trees[i]);
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#pop()
	 */
    public synchronized Tree pop() {
        Tree tmp;
        for (int i = 0; i < _queue.size(); i++) {
            tmp = (Tree) _queue.elementAt(i);
            if (tmp.isProcessable()) {
                _queue.remove(i);
                return tmp;
            } else if (tmp.isOutDated()) _queue.remove(i);
        }
        return null;
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#getFirst()
	 */
    public synchronized Tree getFirst() {
        Tree tmp;
        for (int i = 0; i < _queue.size(); i++) {
            tmp = (Tree) _queue.elementAt(i);
            if (tmp.isProcessable()) return tmp;
        }
        return null;
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#search(trust.meta.Tree)
	 */
    public synchronized Tree search(Tree pattern) {
        int index = _queue.indexOf(pattern);
        if (index == -1) return null; else return (Tree) _queue.elementAt(index);
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#update(trust.meta.Tree, trust.meta.Tree)
	 */
    public synchronized int update(Tree pattern, Tree newTree) {
        int index = _queue.indexOf(pattern);
        if (index == -1) return -1; else {
            Tree tree = (Tree) _queue.elementAt(index);
            tree.update(newTree);
            return 0;
        }
    }

    /**
	 * @see g4mfs.impl.org.peertrust.strategy.Queue#replace(trust.meta.Tree, trust.meta.Tree)
	 */
    public synchronized int replace(Tree pattern, Tree newTree) {
        int index = _queue.indexOf(pattern);
        if (index == -1) return -1; else {
            _queue.setElementAt(newTree, index);
            return 0;
        }
    }

    public synchronized Tree remove(Tree pattern) {
        int index = _queue.indexOf(pattern);
        if (index == -1) return null; else return (Tree) _queue.remove(index);
    }
}
