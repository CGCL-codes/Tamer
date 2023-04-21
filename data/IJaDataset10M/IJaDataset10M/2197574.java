package org.akrogen.tkui.usecases.toolkit.swt.bb;

import java.io.InputStream;
import org.akrogen.tkui.core.dom.interceptors.IDOMInterceptor;
import org.akrogen.tkui.core.ui.toolkit.IUIToolkit;
import org.akrogen.tkui.impl.core.dom.interceptors.backingbean.BackingBeanDOMInterceptor;
import org.akrogen.tkui.ui.swt.toolkit.SWTToolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BackingBeanTest {

    public static void main(String[] args) {
        try {
            Display display = new Display();
            Shell shell = new Shell(display, SWT.SHELL_TRIM);
            FillLayout layout = new FillLayout();
            shell.setLayout(layout);
            Composite topPanel = new Composite(shell, SWT.NONE);
            FillLayout layoutToPanel = new FillLayout();
            topPanel.setLayout(layoutToPanel);
            InputStream source = BackingBeanTest.class.getResourceAsStream("example1.html");
            IUIToolkit toolkit = SWTToolkit.getDefaultToolkit();
            IDOMInterceptor backingBeanInterceptor = BackingBeanDOMInterceptor.getInstance();
            shell.pack();
            shell.open();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
            shell.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
