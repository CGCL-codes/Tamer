package research.ui.editors.tableeditors.formula;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import research.core.Activator;
import research.domain.ComponentValue;
import research.ui.editors.tableeditors.EntityLabelProvider;

public class FormulaLabelProvider extends EntityLabelProvider implements ITableLabelProvider {

    public static final Image CHECKED_IMAGE = Activator.getImageDescriptor("icons/checked.gif").createImage();

    public static final Image UNCHECKED_IMAGE = Activator.getImageDescriptor("icons/unchecked.gif").createImage();

    private Image getImage(boolean isSelected) {
        return isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        String result = "";
        ComponentValue par = (ComponentValue) element;
        switch(columnIndex) {
            case 0:
                result = render(par.getComponent() == null ? "" : par.getComponent().getName());
                break;
            case 1:
                result = render(par.getPercent());
                break;
            case 2:
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return (columnIndex == 2) ? getImage(((ComponentValue) element).isIs_main()) : null;
    }
}
