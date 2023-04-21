package er.directtoweb.components.buttons;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableArray;
import er.directtoweb.interfaces.ERDPickPageInterface;

/**
 * For editing a selection in a list repetition. You'd typicically but this somewhere into the actions.
 * @author ak on Thu Sep 04 2003
 * @project ERDirectToWeb
 */
public class ERDSelectionComponent extends ERDActionButton {

    /**
     * Public constructor
     * @param context the context
     */
    public ERDSelectionComponent(WOContext context) {
        super(context);
    }

    public boolean checked() {
        return selectedObjects().containsObject(object());
    }

    public void setChecked(boolean newChecked) {
        if (newChecked) {
            if (!selectedObjects().containsObject(object())) {
                selectedObjects().addObject(object());
            }
        } else {
            selectedObjects().removeObject(object());
        }
    }

    public NSMutableArray selectedObjects() {
        ERDPickPageInterface pickPage = parentPickPage();
        return (NSMutableArray) pickPage.selectedObjects();
    }

    public String selectionWidgetName() {
        return booleanValueForBinding("singleSelection") ? "WORadioButton" : "WOCheckBox";
    }
}
