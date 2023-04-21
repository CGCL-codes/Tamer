package org.akrogen.tkui.usecases.platform.swt.xul;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.akrogen.tkui.core.dom.bindings.IDOMDocumentBindable;
import org.akrogen.tkui.core.platform.IPlatform;
import org.akrogen.tkui.platform.swt.SWTPlatform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTXULLabelTest {

    public static void main(String[] args) {
        try {
            Display display = new Display();
            Shell shell = new Shell(display, SWT.SHELL_TRIM);
            FillLayout layout = new FillLayout();
            shell.setLayout(layout);
            Composite topPanel = new Composite(shell, SWT.NONE);
            FillLayout layoutToPanel = new FillLayout();
            topPanel.setLayout(layoutToPanel);
            InputStream source = new FileInputStream(new File("resources/xul/label/label.xul"));
            IPlatform platform = SWTPlatform.getDefaultPlatform();
            IDOMDocumentBindable document = platform.createDocument(topPanel, null, null);
            platform.loadDocument(source, document);
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
