package net.sf.rcpforms.widgetwrapper.wrapper.advanced.helper;

import java.util.Vector;
import net.sf.rcpforms.widgetwrapper.wrapper.advanced.RCPAdvancedViewer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;

public class AdvViewerPaintRefreshRunner {

    private static final int DEFAULT_DELAY = 100;

    private final RCPAdvancedViewer m_advViewer;

    private boolean m_isRunning;

    private Vector<Item> m_tableItemsToBeUpdated;

    private int m_millis;

    public AdvViewerPaintRefreshRunner(final RCPAdvancedViewer advViewer) {
        m_advViewer = advViewer;
        m_isRunning = false;
        m_tableItemsToBeUpdated = new Vector<Item>(100);
        m_millis = DEFAULT_DELAY;
    }

    public void redrawIn(final int redrawInMillis, final Object rowModel, final Item tableItem) {
        if (redrawInMillis > 5 && redrawInMillis < m_millis) {
            m_millis = redrawInMillis;
        }
        addItemsToBeUpdated(tableItem);
        assertRunning();
    }

    protected void assertRunning() {
        if (!m_isRunning) {
            startAgain();
        }
    }

    protected synchronized void startAgain() {
        if (m_isRunning) {
            return;
        }
        final Runnable runnable = new Runnable() {

            private int flipper = -1;

            private Rectangle bounds = null;

            @Override
            public void run() {
                m_isRunning = true;
                final TableItem[] items = collectItemsToBeUpdated();
                if (items == null || items.length == 0) {
                    m_isRunning = false;
                    return;
                } else {
                    final Control swtControl = m_advViewer.getSWTControl();
                    if (!swtControl.isDisposed()) {
                        swtControl.redraw();
                    }
                    clearItems();
                    m_isRunning = true;
                    Display.getCurrent().timerExec(m_millis, this);
                }
            }
        };
        Display.getCurrent().timerExec(m_millis, runnable);
        m_isRunning = true;
    }

    private TableItem[] collectItemsToBeUpdated() {
        synchronized (m_tableItemsToBeUpdated) {
            final int size = m_tableItemsToBeUpdated.size();
            return m_tableItemsToBeUpdated.toArray(new TableItem[size]);
        }
    }

    private void addItemsToBeUpdated(final Item tableItem) {
        synchronized (m_tableItemsToBeUpdated) {
            final TableItem[] items = collectItemsToBeUpdated();
            for (int i = 0; i < items.length; i++) {
                if (items[i] == tableItem) {
                    return;
                }
            }
            m_tableItemsToBeUpdated.add(tableItem);
        }
    }

    private void clearItems() {
        synchronized (m_tableItemsToBeUpdated) {
            m_tableItemsToBeUpdated.clear();
        }
    }
}
