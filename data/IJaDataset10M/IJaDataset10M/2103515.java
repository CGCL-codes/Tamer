package net.sf.rcpmoney.ui.provider;

import net.sf.rcpmoney.entity.Category;
import net.sf.rcpmoney.entity.CategoryType;
import org.eclipse.jface.viewers.LabelProvider;

public class CategoryLabelProvider extends LabelProvider {

    @Override
    public String getText(Object element) {
        if (element instanceof Category) {
            Category category = (Category) element;
            StringBuilder sb = new StringBuilder();
            if (category.getParent() == null) {
                CategoryType type = category.getType();
                switch(type) {
                    case CREDIT:
                        sb.append("Revenus : ");
                        break;
                    case SPENDING:
                        sb.append("D�penses : ");
                        break;
                    default:
                        sb.append("?? : ");
                }
            }
            sb.append(category.getName());
            return sb.toString();
        } else if (element instanceof CategoryType) {
            CategoryType type = (CategoryType) element;
            switch(type) {
                case CREDIT:
                    return "Revenus";
                case SPENDING:
                    return "D�penses";
                default:
                    return super.getText(element);
            }
        } else {
            return super.getText(element);
        }
    }
}
