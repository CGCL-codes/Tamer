package it.battlehorse.rcp.tools.log.dialogs;

import it.battlehorse.rcp.tools.commons.EventType;
import it.battlehorse.rcp.tools.log.IDetailHelper;
import it.battlehorse.rcp.tools.log.ILoggable;
import it.battlehorse.rcp.tools.log.internal.LogItemContentProvider;
import it.battlehorse.rcp.tools.log.internal.LogItemLabelProvider;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Defines helper methods to be used whenever dealing with {@code ILoggable} detail dialogs
 * 
 * @author battlehorse
 * @since Apr 1, 2006
 */
public class DetailHelper implements IDetailHelper {

    public DetailHelper() {
    }

    public void addDetailControl(Composite parent, ILoggable detail, int colspan, int widthHint, int heightHint) {
        if (detail.getType() == EventType.EXCEPTION_EVENT && detail.getThrowable() != null) {
            Text t = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
            StringWriter sw = new StringWriter();
            detail.getThrowable().printStackTrace(new PrintWriter(sw));
            t.setText(sw.toString());
            t.setEditable(false);
            GridData gd = new GridData(GridData.FILL_BOTH);
            gd.horizontalSpan = colspan;
            if (widthHint != SWT.DEFAULT) gd.widthHint = widthHint;
            gd.heightHint = heightHint == SWT.DEFAULT ? 200 : heightHint;
            t.setLayoutData(gd);
        } else {
            TreeViewer logViewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
            Tree t = logViewer.getTree();
            t.setHeaderVisible(true);
            t.setLinesVisible(true);
            GridData gd = new GridData(GridData.FILL_BOTH);
            gd.horizontalSpan = colspan;
            if (widthHint != SWT.DEFAULT) gd.widthHint = widthHint;
            gd.heightHint = heightHint == SWT.DEFAULT ? 200 : heightHint;
            t.setLayoutData(gd);
            TreeColumn tc;
            tc = new TreeColumn(t, SWT.LEFT);
            tc.setText("Item");
            tc = new TreeColumn(t, SWT.LEFT);
            tc.setText("Severity");
            tc = new TreeColumn(t, SWT.LEFT);
            tc.setText("Message");
            tc = new TreeColumn(t, SWT.CENTER);
            tc.setText("Time");
            tc = new TreeColumn(t, SWT.LEFT);
            tc.setText("Source");
            tc = new TreeColumn(t, SWT.LEFT);
            tc.setText("Type");
            logViewer.setContentProvider(new LogItemContentProvider());
            logViewer.setLabelProvider(new LogItemLabelProvider());
            logViewer.setInput(detail);
            for (TreeColumn col : t.getColumns()) col.pack();
        }
    }

    public boolean haveDetails(ILoggable detail) {
        return (detail.getType() == EventType.EXCEPTION_EVENT && detail.getThrowable() != null) || detail.getSubLogs() != null && detail.getSubLogs().size() > 0;
    }
}
