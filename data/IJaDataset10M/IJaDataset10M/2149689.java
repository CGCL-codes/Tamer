package search.methods;

import java.util.Comparator;
import list.AbstractList;
import trees.binary.BST;

/**
 * Class responsible for the construction of the Binary Search Tree equivalent for the list and
 * manages the search through the tree
 * 
 * @author Mostafa Mahmoud Mahmoud Eweda
 * @version 1.0
 * @since JDK 1.6
 * 
 * @see AbstractSearch
 * @see AbstractList
 *
 * @param <E> the element to search for
 * @param <K> the applicable comparator of the list
 */
public class BSTSearch<E, K extends Comparator<E>> extends AbstractSearch<E, K> {

    private BST<E, K> tree;

    public BSTSearch(AbstractList<E> list, K comp) {
        super(list, comp);
        tree = new BST<E, K>(comp);
        for (int i = 0, n = list.size(); i < n; i++) {
            tree.insert(list.get(i));
        }
    }

    @Override
    public E find(E e) {
        long start = System.nanoTime();
        E found = tree.find(e);
        runningTime = System.nanoTime() - start;
        comparisons = tree.getComparsons();
        return found;
    }

    @Override
    public Object[] findAll(E e) {
        return tree.findAll(e);
    }
}
