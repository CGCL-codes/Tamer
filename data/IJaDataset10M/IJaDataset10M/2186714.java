package freetm.client.ui.trees.explorer;

import freetm.client.ui.trees.LabelProviderGeneral;
import net.mygwt.ui.client.data.Model;

/**
 *
 * @author Yorgos
 */
public class LabelProviderExplorer extends LabelProviderGeneral {

    /** Creates a new instance of LabelProviderTreeHierarchies */
    public LabelProviderExplorer() {
    }

    public String getIconStyle(Object element) {
        Model model = (Model) element;
        if (model instanceof ModelExpRole) {
            return "icon-role";
        }
        return super.getIconStyle(element);
    }
}
