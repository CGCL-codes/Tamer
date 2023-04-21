package org.plazmaforge.studio.reportdesigner.parts.properties.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.plazmaforge.studio.reportdesigner.model.Group;

public class GroupLabelProvider extends LabelProvider {

    public GroupLabelProvider() {
    }

    public String getText(Object obj) {
        if (obj == null) {
            return "";
        }
        return ((Group) obj).getName();
    }
}
