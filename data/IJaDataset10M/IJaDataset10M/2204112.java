package org.eclipse.jface.internal.databinding.provisional.swt;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.ObservableTracker;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

/**
 * NON-API - This class can be used to update a composite with automatic dependency tracking.
 * @since 1.1
 * 
 */
public abstract class CompositeUpdater {

    private class UpdateRunnable implements Runnable, IChangeListener {

        private Widget widget;

        Object element;

        private boolean dirty = true;

        private IObservable[] dependencies = new IObservable[0];

        UpdateRunnable(Widget widget, Object element) {
            this.widget = widget;
            this.element = element;
        }

        public void run() {
            if (theComposite != null && !theComposite.isDisposed() && widget != null && !widget.isDisposed()) {
                updateIfNecessary();
            }
        }

        private void updateIfNecessary() {
            if (dirty) {
                dependencies = ObservableTracker.runAndMonitor(new Runnable() {

                    public void run() {
                        updateWidget(widget, element);
                    }
                }, this, null);
                dirty = false;
            }
        }

        public void handleChange(ChangeEvent event) {
            makeDirty();
        }

        protected final void makeDirty() {
            if (!dirty) {
                dirty = true;
                stopListening();
                if (!theComposite.isDisposed()) {
                    SWTUtil.runOnce(theComposite.getDisplay(), this);
                }
            }
        }

        private void stopListening() {
            for (int i = 0; i < dependencies.length; i++) {
                IObservable observable = dependencies[i];
                observable.removeChangeListener(this);
            }
        }
    }

    private class PrivateInterface implements DisposeListener, IListChangeListener {

        public void widgetDisposed(DisposeEvent e) {
            CompositeUpdater.this.dispose();
        }

        public void handleListChange(ListChangeEvent event) {
            ListDiffEntry[] diffs = event.diff.getDifferences();
            for (int i = 0; i < diffs.length; i++) {
                ListDiffEntry listDiffEntry = diffs[i];
                if (listDiffEntry.isAddition()) {
                    Widget newChild = createWidget(listDiffEntry.getPosition());
                    final UpdateRunnable updateRunnable = new UpdateRunnable(newChild, listDiffEntry.getElement());
                    newChild.setData(updateRunnable);
                    updateRunnable.updateIfNecessary();
                } else {
                    theComposite.getChildren()[listDiffEntry.getPosition()].dispose();
                }
            }
            theComposite.layout();
        }
    }

    private PrivateInterface privateInterface = new PrivateInterface();

    private Composite theComposite;

    private IObservableList model;

    /**
	 * Creates an updater for the given control and list. For each element of
	 * the list, a child widget of the composite will be created using
	 * {@link #createWidget(int)}.
	 * 
	 * @param toUpdate
	 *            composite to update
	 * @param model
	 *            an observable list to track
	 */
    public CompositeUpdater(Composite toUpdate, IObservableList model) {
        this.theComposite = toUpdate;
        this.model = model;
        model.addListChangeListener(privateInterface);
        theComposite.addDisposeListener(privateInterface);
    }

    /**
	 * This is called automatically when the control is disposed. It may also be
	 * called explicitly to remove this updator from the control. Subclasses
	 * will normally extend this method to detach any listeners they attached in
	 * their constructor.
	 */
    public void dispose() {
        theComposite.removeDisposeListener(privateInterface);
        model.removeListChangeListener(privateInterface);
    }

    /**
	 * Creates a new child widget for the target composite at the given index.
	 * 
	 * <p>
	 * Subclasses should implement this method to provide the code that creates
	 * a child widget at a specific index. Note that
	 * {@link #updateWidget(Widget, Object)} will be called after this method
	 * returns. Only those properties of the widget that don't change over time
	 * should be set in this method.
	 * </p>
	 * 
	 * @param index
	 *            the at which to create the widget
	 * @return the widget
	 */
    protected abstract Widget createWidget(int index);

    /**
	 * Updates the given widget based on the element found in the model list.
	 * This method will be invoked once after the widget is created, and once
	 * before any repaint during which the control is visible and dirty.
	 * 
	 * <p>
	 * Subclasses should implement this method to provide any code that changes
	 * the appearance of the widget.
	 * </p>
	 * 
	 * @param widget
	 *            the widget to update
	 * @param element
	 *            the element associated with the widget
	 */
    protected abstract void updateWidget(Widget widget, Object element);
}
