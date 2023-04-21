package com.metanology.mde.core.ui.views;

import org.eclipse.swt.widgets.Composite;
import com.metanology.mde.core.ui.plugin.*;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class StdLogViewer extends ConsoleViewer {

    private static Color errColor = MDEPlugin.getShell().getDisplay().getSystemColor(SWT.COLOR_RED);

    private static Color warnColor = MDEPlugin.getShell().getDisplay().getSystemColor(SWT.COLOR_MAGENTA);

    private static Color debugColor = MDEPlugin.getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK);

    private static Color traceColor = MDEPlugin.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);

    /**
     * @see com.metanology.mde.ui.views.ConsoleViewer#addOutputText(String)
     */
    public void addErrText(String text) {
        if (text == null || text.length() == 0) return;
        if (document == null) {
            document = new Document("");
            viewer.setDocument(document);
        }
        int start = this.document.getLength();
        int len = text.length();
        super.addOutputText(text);
        viewer.setTextColor(errColor, start, len, false);
    }

    public void addWarnText(String text) {
        if (text == null || text.length() == 0) return;
        if (document == null) {
            document = new Document("");
            viewer.setDocument(document);
        }
        int start = this.document.getLength();
        int len = text.length();
        super.addOutputText(text);
        viewer.setTextColor(warnColor, start, len, false);
    }

    public void addInfoText(String text) {
        super.addOutputText(text);
    }

    public void addDebugText(String text) {
        if (text == null || text.length() == 0) return;
        if (document == null) {
            document = new Document("");
            viewer.setDocument(document);
        }
        int start = this.document.getLength();
        int len = text.length();
        super.addOutputText(text);
        viewer.setTextColor(debugColor, start, len, false);
    }

    public void addTraceText(String text) {
        if (text == null || text.length() == 0) return;
        if (document == null) {
            document = new Document("");
            viewer.setDocument(document);
        }
        int start = this.document.getLength();
        int len = text.length();
        super.addOutputText(text);
        viewer.setTextColor(traceColor, start, len, false);
    }

    public static final String VIEW_ID = "com.metanology.mde.core.ui.views.StdLogViewer";

    /**
	 * The constructor.
	 */
    public StdLogViewer() {
    }

    /**
	 * Create controls for this view
	 * @see ViewPart#createPartControl
	 */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        Color color = MDEPlugin.getShell().getDisplay().getSystemColor(SWT.COLOR_BLUE);
        if (color != null) {
            viewer.getTextWidget().setForeground(color);
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
    }
}
