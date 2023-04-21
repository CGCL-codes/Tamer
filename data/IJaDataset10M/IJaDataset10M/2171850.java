package wood.view.manager;

import static lawu.util.iterator.Iterators.iterator;
import java.util.Stack;
import lawu.util.iterator.UniversalIterator;
import wood.view.draw.Canvas;
import wood.view.viewport.Viewport;

public abstract class DrawEventManager<C extends Canvas> {

    private final Stack<Viewport<C>> viewports;

    public DrawEventManager() {
        viewports = new Stack<Viewport<C>>();
    }

    public void push(Viewport<C> v) {
        viewports.push(v);
    }

    public Viewport<C> pop() {
        Viewport<C> v;
        v = viewports.pop();
        v.disable();
        return v;
    }

    protected UniversalIterator<Viewport<C>> getViewports() {
        return iterator(viewports);
    }

    public void swap(Viewport<C> oldViewport, Viewport<C> newViewport) {
        if (!viewports.contains(oldViewport)) throw new RuntimeException();
        int index = viewports.indexOf(oldViewport);
        viewports.set(index, newViewport);
    }
}
