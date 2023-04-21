package algorithms.dfbnb.searchOrder;

import java.util.Comparator;
import algorithms.dfbnb.InfNode;

public class LastCreatedFirstExpanded<E> implements Comparator<InfNode<E>> {

    @Override
    public int compare(InfNode<E> arg0, InfNode<E> arg1) {
        return arg1.getID() - arg0.getID();
    }
}
