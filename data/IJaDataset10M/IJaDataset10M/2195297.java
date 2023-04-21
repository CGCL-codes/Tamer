package blue.event;

import java.util.ArrayList;

/**
 * @author steven
 */
public class SelectionList extends ArrayList implements SelectionListener {

    public void selectionPerformed(SelectionEvent e) {
        switch(e.getSelectionType()) {
            case SelectionEvent.SELECTION_CLEAR:
                this.clear();
                break;
            case SelectionEvent.SELECTION_SINGLE:
                Object selectedItem = e.getSelectedItem();
                if (this.contains(selectedItem)) {
                    return;
                }
                this.clear();
                this.add(selectedItem);
                break;
            case SelectionEvent.SELECTION_ADD:
                this.add(e.getSelectedItem());
                break;
            case SelectionEvent.SELECTION_REMOVE:
                this.remove(e.getSelectedItem());
                break;
        }
    }
}
