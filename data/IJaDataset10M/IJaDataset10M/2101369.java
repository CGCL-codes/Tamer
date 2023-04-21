package clutrfree;

import java.util.Vector;

public class Coll {

    private Vector v;

    private Vector orderedv;

    private int[] cardinals;

    public Coll() {
        v = new Vector();
        orderedv = new Vector();
    }

    public void computeCardinals() {
        int vs = v.size();
        cardinals = new int[vs];
        int index;
        for (int i = 0; i < vs; i++) {
            if ((index = orderedv.indexOf(v.elementAt(i))) != -1) {
                cardinals[index]++;
            } else {
                orderedv.addElement(v.elementAt(i));
                cardinals[orderedv.size()]++;
            }
        }
    }

    public void sort() {
    }

    public void addElement(Object o) {
        v.addElement(o);
    }

    public Vector getOrderedVector() {
        return orderedv;
    }

    public int[] getCardinals() {
        return cardinals;
    }
}
