package net.sf.edevtools.tools.logviewer.core.ui.components.logtable.impl;

import net.sf.edevtools.tools.logviewer.core.model.DefaultLogChangeListener;
import net.sf.edevtools.tools.logviewer.core.model.ILogChangeListener;
import net.sf.edevtools.tools.logviewer.core.model.ILogEntry;
import net.sf.edevtools.tools.logviewer.core.model.ILogSummary;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

/**
 * TODO comment
 * 
 * @author Christoph Graupner
 * @since 0.1.0
 * @version 0.1.0
 */
public class LogTableContentProvider implements IStructuredContentProvider {

    ILogSummary fInput;

    TableViewer fViewer;

    ILogChangeListener fListener = new DefaultLogChangeListener() {

        @Override
        public void handleLogCleared() {
            if (fViewer != null) Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    fViewer.refresh();
                }
            });
        }

        @Override
        public void handleLogEvent(final ILogEntry aNewEntry) {
            if (fViewer != null) Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    fViewer.add(aNewEntry);
                    if (fParent.getAutoScroll()) scrollToLast();
                }

                private void scrollToLast() {
                    Table lTable = fViewer.getTable();
                    int lIter = lTable.getItemCount() - 1;
                    lTable.showItem(lTable.getItem(lIter));
                }
            });
        }
    };

    private final LogTableViewComponent fParent;

    public LogTableContentProvider(LogTableViewComponent aComp) {
        fParent = aComp;
    }

    public void dispose() {
    }

    public Object[] getElements(Object aInputElement) {
        return ((ILogSummary) aInputElement).getLogEntries().toArray();
    }

    public void inputChanged(Viewer aViewer, Object aOldInput, Object aNewInput) {
        if (aOldInput != null) {
            ILogSummary lLog = (ILogSummary) aOldInput;
            lLog.removeLogListener(fListener);
        }
        fInput = (ILogSummary) aNewInput;
        if (fInput != null) {
            fInput.addLogListener(fListener);
        }
        fViewer = (TableViewer) aViewer;
    }
}
