package org.springframework.richclient.application.docking.swing;

import javax.swing.JDesktopPane;
import net.sf.swingdocking.TabbingDesktopPane;
import org.springframework.core.JdkVersion;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.PageDescriptor;

/**
 * @author Arne Limburg
 */
public class TabbedSwingDockingApplicationPage extends SwingDockingApplicationPage {

    public TabbedSwingDockingApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {
        super(window, pageDescriptor);
    }

    protected JDesktopPane createDesktopPane() {
        if (!JdkVersion.isAtLeastJava16()) {
            throw new IllegalStateException("At least Java Version 6 is needed for tabbed Swing-Docking.");
        }
        return new TabbingDesktopPane();
    }
}
