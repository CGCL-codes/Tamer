package bll;

import java.util.ArrayList;
import java.util.ListIterator;

public class ItemList {

    public ArrayList Items;

    public ItemList() {
        super();
        Items = new ArrayList();
    }

    public void enableArcs() {
        ListIterator listIterator = Items.listIterator();
        while (listIterator.hasNext()) {
            Object item = listIterator.next();
            if (item instanceof Arc) {
                if (((Arc) item).getTypeArc() == 0) {
                    boolean enabled = ((Place) ((Arc) item).getItemFrom()).getTokens() >= ((Arc) item).getMultiplicity();
                    ((Arc) item).setEnabled(enabled);
                }
            }
        }
    }

    public void enableTransitions() {
        ListIterator listIterator = Items.listIterator();
        while (listIterator.hasNext()) {
            Object item = listIterator.next();
            if (item instanceof Transition) {
                boolean enabled = true;
                ListIterator listIterator1 = Items.listIterator();
                while (listIterator1.hasNext() && enabled) {
                    Object item1 = listIterator1.next();
                    if (item1 instanceof Arc) {
                        if (((Arc) item1).getItemTo() == item) {
                            enabled = ((Arc) item1).isEnabled();
                        }
                    }
                }
                ((Transition) item).setEnabled(enabled);
            }
        }
    }
}
