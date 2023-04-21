package org.formaria.swt;

import org.formaria.aria.MessageBoxSetup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * <p>
 * Creates a simple modal MessageBox with a label for the text and a close
 * button
 * </p>
 * <p>
 * Copyright (c) Formaria Ltd., 2008<br>
 * License: see license.txt $Revision: 2.7 $
 */
public class MessageBox extends org.eclipse.swt.widgets.MessageBox implements MessageBoxSetup {

    /**
   * Create a new checkbox
   * 
   * @param parent
   *          the message box parent, normally the shell
   */
    public MessageBox(Object parent) {
        super((Shell) parent, SWT.OK | SWT.ICON_INFORMATION);
    }

    /**
   * Suppress the subclassing exception
   */
    protected void checkSubclass() {
    }

    /**
   * Setup the content of the message box
   * 
   * @param title
   *          the message box title or caption
   * @param msg
   *          the text of the message
   * @param size
   *          the size of the owner page
   * @param page
   *          the content page
   */
    public void setup(String title, String msg, Object size, Object page) {
        setText(title);
        setMessage(msg);
        open();
    }
}
