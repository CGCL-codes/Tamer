package com.cosylab.vdct.undo;

import com.cosylab.vdct.graphics.objects.Descriptable;

/**
 * Insert the type's description here.
 * Creation date: (3.5.2001 15:23:26)
 * @author 
 */
public class DescriptionChangeAction extends ActionObject {

    private Descriptable object;

    private String oldValue;

    private String newValue;

    /**
 * Insert the method's description here.
 * Creation date: (3.5.2001 15:30:47)
 * @param objectDescriptable
 * @param oldValue java.lang.String
 * @param newValue java.lang.String
 */
    public DescriptionChangeAction(Descriptable object, String oldValue, String newValue) {
        this.object = object;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
 * Insert the method's description here.
 * Creation date: (3.5.2001 15:50:49)
 * @return java.lang.String
 */
    public java.lang.String getDescription() {
        return "Description changed [" + object + "](\"" + oldValue + "\" to \"" + newValue + "\")";
    }

    /**
 * This method was created in VisualAge.
 */
    protected void redoAction() {
        object.setDescription(newValue);
    }

    /**
 * This method was created in VisualAge.
 */
    protected void undoAction() {
        object.setDescription(oldValue);
    }
}
