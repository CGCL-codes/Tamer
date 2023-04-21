package org.plazmaforge.studio.dbdesigner.parts;

import org.eclipse.swt.graphics.Image;
import org.plazmaforge.studio.dbdesigner.DBDesignerResources;

public class ERTreeViewNodeEditPart extends ERTreeTableNodeEditPart {

    public ERTreeViewNodeEditPart(Object obj) {
        super(obj);
    }

    protected Image getElementImage() {
        return DBDesignerResources.VIEW_ICON;
    }
}
