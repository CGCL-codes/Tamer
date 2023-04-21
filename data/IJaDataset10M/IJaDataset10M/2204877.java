package quj;

import java.util.EventObject;

class SelectionEvent extends EventObject {

    Item selected;

    public SelectionEvent(Object source, Item selected_) {
        super(source);
        selected = selected_;
    }

    public Item getSelected() {
        return selected;
    }
}
