package ghm.follow.io;

import java.util.ArrayList;

public abstract class FilterableOutputDestination implements OutputDestination {

    private ArrayList<OutputDestination> views;

    public FilterableOutputDestination() {
        views = new ArrayList<OutputDestination>();
    }

    /**
	 * Add a view that filters printed text. <code>dest</code> can be a
	 * standard output or a {@link FilteredDestination}
	 */
    public void addView(OutputDestination dest) {
        views.add(dest);
    }

    /**
	 * Remove a view that filters printed text
	 */
    public void removeView(OutputDestination dest) {
        if (!views.isEmpty() && views.contains(dest)) {
            views.remove(dest);
        }
    }

    protected void notifyViews(String s) {
        for (OutputDestination view : views) {
            view.print(s);
        }
    }

    public void print(String s) {
        handlePrint(s);
        notifyViews(s);
    }

    protected abstract void handlePrint(String s);
}
