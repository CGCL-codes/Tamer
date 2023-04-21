package net.sf.rcpforms.widgetwrapper.wrapper.advanced.helper;

import java.util.logging.Logger;
import net.sf.rcpforms.widgetwrapper.wrapper.advanced.RCPAdvancedViewer;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IWidgetCreationListener2;
import net.sf.rcpforms.widgetwrapper.wrapper.event.WidgetCreationEvent;
import net.sf.rcpforms.widgetwrapper.wrapper.event.WidgetCreationEvent2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public abstract class AbstractAdvViewerResizeAdapter<VIEWER extends Control, COLUMN extends Item> implements IWidgetCreationListener2 {

    @SuppressWarnings("all")
    private static final Logger log = Logger.getLogger(AbstractAdvViewerResizeAdapter.class.getName());

    protected final RCPAdvancedViewer m_advancedViewer;

    public AbstractAdvViewerResizeAdapter(final RCPAdvancedViewer advViewer) {
        m_advancedViewer = advViewer;
        if (advViewer.getSWTControl() != null && !advViewer.getSWTControl().isDisposed()) {
            installTo(advViewer.getSWTControl());
        } else {
            m_advancedViewer.addWidgetCreationListener(this);
        }
    }

    protected void installTo(final Control swtControl) {
        swtControl.addListener(SWT.Resize, new Listener() {

            @Override
            public void handleEvent(final Event event) {
                proTableResized((VIEWER) event.widget);
            }
        });
        final Listener columnResizeListener = new Listener() {

            @Override
            public void handleEvent(final Event event) {
                proColumnResized((COLUMN) event.widget);
            }
        };
        if (swtControl instanceof Table) {
            final Table table = (Table) swtControl;
            final TableColumn[] columns = table.getColumns();
            for (int i = 0; i < columns.length; i++) {
                columns[i].addListener(SWT.Resize, columnResizeListener);
            }
        } else if (swtControl instanceof Tree) {
            final Tree tree = (Tree) swtControl;
            final TreeColumn[] columns = tree.getColumns();
            for (int i = 0; i < columns.length; i++) {
                columns[i].addListener(SWT.Resize, columnResizeListener);
            }
        } else {
            throw new IllegalStateException("unsupported control type, must be Table or Tree but is: " + swtControl.getClass());
        }
    }

    protected abstract void proTableResized(final VIEWER table);

    protected abstract void proColumnResized(final COLUMN widget);

    @Override
    public void widgetCreated(WidgetCreationEvent2 event) {
        if (event.getSwtControl() != m_advancedViewer.getSWTControl()) {
            log.warning("potential implementation error: spawned table is not table or advanced-table's I expected to spawn!");
        }
        installTo(m_advancedViewer.getSWTControl());
        m_advancedViewer.removeWidgetCreationListener(this);
    }

    @Override
    public void widgetCreated(WidgetCreationEvent wce) {
        installTo(m_advancedViewer.getSWTControl());
        m_advancedViewer.removeWidgetCreationListener(this);
    }
}
