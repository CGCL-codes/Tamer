package org.fest.swing.hierarchy;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.Collection;
import org.fest.swing.annotation.RunsInCurrentThread;
import org.fest.swing.monitor.WindowMonitor;
import static org.fest.swing.awt.AWT.*;

/**
 * Understands access to the current AWT hierarchy.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ExistingHierarchy implements ComponentHierarchy {

    private static WindowMonitor windowMonitor = WindowMonitor.instance();

    private final ParentFinder parentFinder;

    private final ChildrenFinder childrenFinder;

    /** Creates a new </code>{@link ExistingHierarchy}</code>. */
    public ExistingHierarchy() {
        this(new ParentFinder(), new ChildrenFinder());
    }

    ExistingHierarchy(ParentFinder parentFinder, ChildrenFinder childrenFinder) {
        this.parentFinder = parentFinder;
        this.childrenFinder = childrenFinder;
    }

    /** {@inheritDoc} */
    public Collection<? extends Container> roots() {
        return windowMonitor.rootWindows();
    }

    /** {@inheritDoc} */
    @RunsInCurrentThread
    public Container parentOf(Component c) {
        return parentFinder.parentOf(c);
    }

    /**
   * Returns whether the given component is reachable from any of the root windows. The default is to consider all
   * components to be contained in the hierarchy, whether they are reachable or not.
   * @param c the given component.
   * @return <code>true</code>.
   */
    public boolean contains(Component c) {
        return true;
    }

    /**
   * Returns all descendants of interest of the given component.
   * @param c the given component.
   * @return all descendants of interest of the given component.
   */
    @RunsInCurrentThread
    public Collection<Component> childrenOf(Component c) {
        return childrenFinder.childrenOf(c);
    }

    /**
   * Properly dispose of the given window, making it and its native resources available for garbage collection.
   * @param w the window to dispose.
   */
    @RunsInCurrentThread
    public void dispose(Window w) {
        if (isAppletViewer(w)) return;
        for (Window owned : w.getOwnedWindows()) dispose(owned);
        if (isSharedInvisibleFrame(w)) return;
        w.dispose();
    }

    ParentFinder parentFinder() {
        return parentFinder;
    }

    ChildrenFinder childrenFinder() {
        return childrenFinder;
    }
}
