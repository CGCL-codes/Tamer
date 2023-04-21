package com.lti.swtutils;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * Wraps a SelectionListener with catching all Exceptions (logging, error dialog).
 * @author Ken Larson
 */
public class ExceptionSafeSelectionAdapter implements SelectionListener {

    private final SelectionListener selectionListener;

    private final Logger logger;

    private final Shell shell;

    public ExceptionSafeSelectionAdapter(final Shell shell, final Logger logger, final SelectionListener selectionListener) {
        super();
        this.selectionListener = selectionListener;
        this.logger = logger;
        this.shell = shell;
    }

    public void widgetDefaultSelected(SelectionEvent arg0) {
        try {
            if (selectionListener != null) selectionListener.widgetDefaultSelected(arg0);
        } catch (Throwable e) {
            if (logger != null) logger.error(e, e);
            LoggedExceptionMessageBox.showError(shell, e);
        }
    }

    public void widgetSelected(SelectionEvent arg0) {
        try {
            if (selectionListener != null) selectionListener.widgetSelected(arg0);
        } catch (Throwable e) {
            if (logger != null) logger.error(e, e);
            LoggedExceptionMessageBox.showError(shell, e);
        }
    }
}
