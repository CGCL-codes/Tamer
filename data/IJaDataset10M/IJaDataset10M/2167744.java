package android.widget.listview;

import android.util.ListScenario;
import android.util.ListItemFactory;
import android.view.View;
import android.view.ViewGroup;

/**
 * Each list item has two focusables that are close enough together that
 * it shouldn't require panning to move focus.
 */
public class ListItemFocusablesClose extends ListScenario {

    /**
     * Get the child of a list item.
     * @param listIndex The index of the currently visible items
     * @param index The index of the child.
     */
    public View getChildOfItem(int listIndex, int index) {
        return ((ViewGroup) getListView().getChildAt(listIndex)).getChildAt(index);
    }

    @Override
    protected void init(Params params) {
        params.setItemsFocusable(true).setNumItems(2).setItemScreenSizeFactor(0.55);
    }

    @Override
    protected View createView(int position, ViewGroup parent, int desiredHeight) {
        return ListItemFactory.twoButtonsSeparatedByFiller(position, parent.getContext(), desiredHeight);
    }
}
