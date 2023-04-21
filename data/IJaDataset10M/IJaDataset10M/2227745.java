package de.uniwue.tm.cev.views.selection;

import de.uniwue.tm.cev.data.CEVDocument;
import de.uniwue.tm.cev.editor.CEVViewer;
import de.uniwue.tm.cev.extension.ICEVView;
import de.uniwue.tm.cev.extension.ICEVViewFactory;

public class CEVSelectionViewFactory implements ICEVViewFactory {

    public CEVSelectionViewFactory() {
    }

    public ICEVView createView(CEVViewer viewer, CEVDocument cevDocument, int index) {
        return new CEVSelectionPage(viewer, cevDocument, index);
    }

    public Class<?> getAdapterInterface() {
        return ICEVSelectionPage.class;
    }
}
