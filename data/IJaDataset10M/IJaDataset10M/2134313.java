package net.sf.rcpforms.widgetwrapper.wrapper.advanced.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

public abstract class AbstractCustomItemHandler {

    private Composite m_control;

    public AbstractCustomItemHandler(final Table table) {
        this((Composite) table);
    }

    public AbstractCustomItemHandler(final Tree tree) {
        this((Composite) tree);
    }

    protected AbstractCustomItemHandler(final Composite control) {
        m_control = control;
        applyTo(control);
    }

    protected Composite getControl() {
        return m_control;
    }

    protected void setControl(final Composite control) {
        m_control = control;
    }

    protected void applyTo(final Composite control) {
        control.addListener(SWT.MeasureItem, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                proHandleMeasureItem(event);
            }
        });
        control.addListener(SWT.EraseItem, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                proHandleEraseItem(event);
            }
        });
        control.addListener(SWT.PaintItem, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                proHandlePaintItem(event);
            }
        });
        control.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(final DisposeEvent e) {
                dispose();
                setControl(null);
            }
        });
    }

    protected abstract void dispose();

    protected abstract void proHandlePaintItem(Event event);

    protected abstract void proHandleEraseItem(Event event);

    protected abstract void proHandleMeasureItem(Event event);
}
