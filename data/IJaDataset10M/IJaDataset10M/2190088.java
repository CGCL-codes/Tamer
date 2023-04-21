package fr.itris.glips.extension.jwidget.base.runtime.actions;

import java.awt.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the run application action
 * @author ITRIS, Jordi SUC
 */
public class MenuItemRunApplication extends AbstractRunApplication {

    /**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
    public MenuItemRunApplication(SVGPicture picture, String projectName, Container parent, JComponent component, Object actionObject, Element actionElement, JWidgetRuntime jwidgetRuntime) {
        super(picture, projectName, parent, component, actionObject, actionElement, jwidgetRuntime);
        initializeAction();
    }

    @Override
    public Runnable dataChanged(DataEvent evt) {
        super.dataChanged(evt);
        if (isEntitled()) {
            jwidgetRuntime.refreshComponentState();
        }
        return null;
    }
}
