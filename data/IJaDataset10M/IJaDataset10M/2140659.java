package net.sf.jleonardo.ui.views;

import net.sf.jleonardo.core.BusinessObject;
import net.sf.jleonardo.core.Reference;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Andreas Beckers
 * 
 */
public class ProjectLabelProvider extends LabelProvider implements IBaseLabelProvider {

    @Override
    public String getText(Object element) {
        if (element instanceof Reference) {
            return getText(((Reference) element).getObject());
        }
        if (element instanceof BusinessObject) {
            BusinessObject bo = (BusinessObject) element;
            return (String) bo.get("name");
        }
        if (element instanceof PropertyEntry) {
            return ((PropertyEntry) element).getLabel();
        }
        return super.getText(element);
    }
}
