package eu.redseeds.sc.editor.rsl.editors.domain.controls.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;

public class NotionSpecialisationsContentProvider implements IStructuredContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof NotionDTO) {
            NotionDTO notion = (NotionDTO) inputElement;
            return notion.getNotionSpecialisationDTOList().toArray();
        }
        return new Object[0];
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
