package net.sf.depcon.contact.ui.perspectives;

import net.sf.depcon.contact.ui.views.contact.ContactView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ContactPerspective implements IPerspectiveFactory {

    public static String ID = "net.sf.depcon.contact.ui.perspectives.ContactPerspective";

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(true);
        layout.setFixed(false);
        layout.addView(ContactView.ID, IPageLayout.TOP, 0.33f, editorArea);
    }
}
