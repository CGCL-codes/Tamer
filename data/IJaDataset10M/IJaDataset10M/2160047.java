package eu.redseeds.sc.editor.rsl.editors.domain.controls.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import eu.redseeds.sc.editor.rsl.Activator;
import eu.redseeds.sc.editor.rsl.editors.IImageKeys;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionSpecialisationDTO;

public class NotionSpecialisationsLabelProvider extends LabelProvider implements ITableLabelProvider {

    private NotionDTO parentNotion;

    public NotionSpecialisationsLabelProvider(NotionDTO parentNotion) {
        super();
        this.parentNotion = parentNotion;
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 0) {
            return Activator.getDefault().createImageDescriptorFor(IImageKeys.NOTION).createImage();
        } else {
            return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        if (!(element instanceof NotionSpecialisationDTO)) {
            return result;
        }
        NotionDTO relatedElem = NotionSpecialisationsList.getElementToDisplay((NotionSpecialisationDTO) element, this.parentNotion);
        switch(columnIndex) {
            case 0:
                result = ((NotionDTO) relatedElem).getName();
                break;
            case 1:
                result = NotionSpecialisationsList.getRoleToDisplay((NotionSpecialisationDTO) element, this.parentNotion);
                break;
            default:
                result = "--TODO--";
                break;
        }
        return result;
    }
}
