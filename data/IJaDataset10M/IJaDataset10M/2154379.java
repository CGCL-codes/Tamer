package de.spotnik.mail.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * MailPerspective
 * 
 * @author Jens Rehp�hler
 * @since 04.05.2006
 */
public class SpotnikPerspective implements IPerspectiveFactory {

    /**
     * Creates the initial layout for a page.
     * 
     * @param layout the page layout
     */
    public void createInitialLayout(IPageLayout layout) {
        addPerspectiveShortcuts(layout);
    }

    /**
     * Add perspective shortcuts to the perspective.
     * 
     * @param layout the page layout
     */
    private void addPerspectiveShortcuts(IPageLayout layout) {
        layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
    }
}
