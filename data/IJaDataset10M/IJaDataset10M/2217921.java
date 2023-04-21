package name.fraser.neil.plaintext;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * TODO Description
 * 
 * @author tobias
 */
public abstract class DiffListCleanupTraverser {

    protected LinkedList<Diff> diffs;

    protected Diff currentDiff;

    protected ListIterator<Diff> diffIterator;

    /**
   * 
   */
    public DiffListCleanupTraverser() {
        super();
    }

    public DiffListCleanupTraverser(LinkedList<Diff> diffs) {
        this.diffs = diffs;
    }

    public void cleanup() {
        prepareState();
        performCleanup();
    }

    protected void prepareState() {
        diffIterator = diffs.listIterator();
    }

    protected abstract void performCleanup();

    protected void iterateBackTo(Diff element) {
        while (currentDiff != element) {
            currentDiff = diffIterator.previous();
        }
    }

    protected void iterateToStart() {
        while (diffIterator.hasPrevious()) {
            diffIterator.previous();
        }
    }

    protected void iterateBackwardToCurrentDiff() {
        while (currentDiff != diffIterator.previous()) {
        }
    }
}
