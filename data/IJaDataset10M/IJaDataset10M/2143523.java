package org.gudy.azureus2.ui.swt.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author TuxPaper
 * @created Dec 1, 2006
 *
 */
public class SashFormTest {

    public static void main(String[] args) {
        Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());
        SashForm sf = new SashForm(shell, SWT.VERTICAL);
        sf.SASH_WIDTH = 10;
        sf.setLayout(new FillLayout());
        new Composite(sf, SWT.BORDER);
        new Composite(sf, SWT.BORDER);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
