package org.rapla.gui;

import org.rapla.framework.RaplaException;
import org.rapla.gui.toolkit.RaplaWidget;

public interface EditComponent extends RaplaWidget {

    /** maps all fields back to the current object.*/
    public void mapToObject() throws RaplaException;

    public Object getObject();

    public void setObject(Object o) throws RaplaException;
}
