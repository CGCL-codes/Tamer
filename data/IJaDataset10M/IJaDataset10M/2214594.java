package org.pubcurator.core.dialogs;

import java.util.List;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Shell;
import org.pubcurator.core.managers.ExtensionManager;
import org.pubcurator.core.model.DocumentImporterDescriptor;
import org.pubcurator.core.ui.ExtendedSelectionDialog;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class SelectDocumentImporterDialog extends ExtendedSelectionDialog {

    public SelectDocumentImporterDialog(Shell parentShell) {
        super(parentShell);
        super.setTitle("Document Importers");
        super.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                DocumentImporterDescriptor descriptor = (DocumentImporterDescriptor) element;
                return descriptor.getName();
            }
        });
        super.setComparator(new ViewerComparator() {

            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                DocumentImporterDescriptor descriptor1 = (DocumentImporterDescriptor) e1;
                DocumentImporterDescriptor descriptor2 = (DocumentImporterDescriptor) e2;
                return descriptor1.getName().compareTo(descriptor2.getName());
            }
        });
        List<DocumentImporterDescriptor> descriptors = ExtensionManager.INSTANCE.getDocumentImporterDescriptors();
        super.setInput(descriptors);
    }

    public DocumentImporterDescriptor getSelectedDescriptor() {
        if (super.getResult().length > 0) {
            return (DocumentImporterDescriptor) super.getResult()[0];
        } else {
            return null;
        }
    }
}
