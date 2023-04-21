package de.blitzcoder.collide.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author blitzcoder
 */
public abstract class Document extends JPanel {

    public abstract String getTitle();

    public boolean close() {
        return true;
    }

    ;

    public abstract Icon getIcon();

    public abstract String getToolTip();

    public void focus() {
    }

    public void documentInserted(JPopupMenu popupMenu) {
    }

    public abstract boolean equalsFile(File file);

    public void load() throws Exception {
    }

    protected boolean finishedLoading = false;

    public final void afterLoad() {
        finishedLoading = true;
        ListIterator<Runnable> it = queue.listIterator();
        while (it.hasNext()) SwingUtilities.invokeLater(it.next());
        queue.clear();
    }

    LinkedList<UpdateListener> updateListenerList = new LinkedList<UpdateListener>();

    public interface UpdateListener {

        public void update(Document doc);
    }

    protected final void fireDocumentUpdate() {
        ListIterator<UpdateListener> it = updateListenerList.listIterator();
        while (it.hasNext()) {
            it.next().update(this);
        }
    }

    public final void addUpdateListener(UpdateListener l) {
        updateListenerList.addLast(l);
    }

    LinkedList<Runnable> queue = new LinkedList<Runnable>();

    public final synchronized void schedule(Runnable run) {
        if (finishedLoading) {
            SwingUtilities.invokeLater(run);
            return;
        }
        queue.addLast(run);
    }

    public boolean isClonable() {
        return false;
    }

    public Document cloneDocument() {
        throw new UnsupportedOperationException("Cloning not supported");
    }

    public boolean isClone() {
        return false;
    }

    public Document getOriginal() {
        throw new UnsupportedOperationException("Not a clone");
    }
}
