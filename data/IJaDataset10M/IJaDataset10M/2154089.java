package ghm.follow;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class FilterableOutputDestination implements OutputDestination {

    private ArrayList _views;

    public FilterableOutputDestination() {
        _views = new ArrayList();
    }

    /**
	 * Add a view that filters printed text. <code>dest</code> can be a
	 * standard output or a {@link FilteredDestination}
	 */
    public void addView(OutputDestination dest) {
        _views.add(dest);
    }

    /**
	 * Remove a view that filters printed text
	 */
    public void removeView(OutputDestination dest) {
        if (!_views.isEmpty() && _views.contains(dest)) {
            _views.remove(dest);
        }
    }

    protected void notifyViews(String s) {
        Iterator i = _views.iterator();
        while (i.hasNext()) {
            ((OutputDestination) i.next()).print(s);
        }
    }

    public void print(String s) {
        handlePrint(s);
        notifyViews(s);
    }

    protected abstract void handlePrint(String s);
}
