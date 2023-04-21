package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.ResizeSystem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class DirectoryDialog extends Dialog {

    String message = "", filterPath = "";

    String directoryPath;

    /**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
    public DirectoryDialog(Shell parent) {
        this(parent, SWT.PRIMARY_MODAL);
    }

    /**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
    public DirectoryDialog(Shell parent, int style) {
        super(parent, style);
        checkSubclass();
    }

    /**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 * 
 * @see #setFilterPath
 */
    public String getFilterPath() {
        return filterPath;
    }

    /**
 * Returns the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @return the message
 */
    public String getMessage() {
        return message;
    }

    /**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the selected directory,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
    public String open() {
        dialogShell = new Shell(parent.display, style | SWT.CLOSE | SWT.APPLICATION_MODAL);
        dialogShell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event event) {
            }
        });
        dialogShell.setText(title);
        dialogShell.setLayout(new GridLayout(2, false));
        Label icon = new Label(dialogShell, SWT.NONE);
        icon.setImage(parent.display.getSystemImage(SWT.ICON_WARNING));
        GridData gridData = new GridData(32, 32);
        icon.setLayoutData(gridData);
        Label label = new Label(dialogShell, SWT.NONE);
        label.setText("Not implemented yet.");
        Composite buttonPanel = new Composite(dialogShell, SWT.NONE);
        GridData gd = new GridData(GridData.END, GridData.CENTER, false, false);
        gd.horizontalSpan = 2;
        buttonPanel.setLayoutData(gd);
        buttonPanel.setLayout(new GridLayout());
        Button btn = new Button(buttonPanel, SWT.PUSH);
        btn.setText("&OK");
        btn.setLayoutData(new GridData(75, 24));
        btn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                dialogShell.close();
            }
        });
        dialogShell.pack();
        dialogShell.open();
        Point size = dialogShell.getSize();
        int y = (dialogShell.getMonitor().clientHeight - size.y) / 2 - 20;
        if (y < 0) {
            y = 0;
        }
        dialogShell.setLocation((dialogShell.getMonitor().clientWidth - size.x) / 2, y);
        ResizeSystem.register(dialogShell, SWT.CENTER);
        return directoryPath;
    }

    /**
 * Sets the path that the dialog will use to filter
 * the directories it shows to the argument, which may
 * be null. If the string is null, then the operating
 * system's default filter path will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the filter path
 */
    public void setFilterPath(String string) {
        filterPath = string;
    }

    /**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
    public void setMessage(String string) {
        if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
        message = string;
    }
}
