package objects.production;

import objects.Galaxy;
import java.util.Queue;

/**
 * Author: serhiy
* Created on Apr 5, 2007, 7:53:57 PM
*/
public abstract class ProductionAction implements Comparable<ProductionAction> {

    public ProductionAction(int priority) {
        this.priority = priority;
    }

    private int priority;

    public final int compareTo(ProductionAction elem) {
        return priority - elem.priority;
    }

    public abstract void produce(Queue<ProductionAction> productions, Galaxy galaxy);
}
