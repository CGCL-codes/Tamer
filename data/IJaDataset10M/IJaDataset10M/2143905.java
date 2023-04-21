package org.akrogen.tkui.samples.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.akrogen.tkui.core.ITkuiConfiguration;
import org.akrogen.tkui.core.TkuiConfigurationFactory;
import org.akrogen.tkui.core.dom.ITkuiDocument;
import org.akrogen.tkui.core.gui.IGuiBuilder;
import org.akrogen.tkui.core.loader.ITkuiLoader;
import org.akrogen.tkui.samples.grammars.loader.HTMLLoader;
import org.akrogen.tkui.samples.gui.SwtGuiBuilderImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class HTMLLoaderTest {

    public static void main(String[] args) {
        try {
            TkuiConfigurationFactory factory = TkuiConfigurationFactory.newInstance();
            ITkuiConfiguration configuration = factory.getConfiguration();
            IGuiBuilder swtGuiBuilder = new SwtGuiBuilderImpl();
            configuration.registerGuiBuilder(swtGuiBuilder);
            String guiId = swtGuiBuilder.getId();
            ITkuiLoader htmlLoader = HTMLLoader.getInstance();
            htmlLoader.initialize(configuration);
            Display display = new Display();
            Shell shell = new Shell(display, SWT.SHELL_TRIM);
            FillLayout layout = new FillLayout();
            shell.setLayout(layout);
            ITkuiDocument htmlDocument = htmlLoader.newDocument(guiId, shell);
            File f = new File("html/test.html");
            InputStream sourceStream = new FileInputStream(f);
            htmlLoader.load(sourceStream, htmlDocument);
            shell.pack();
            shell.open();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
        } catch (Exception e) {
        }
    }
}
