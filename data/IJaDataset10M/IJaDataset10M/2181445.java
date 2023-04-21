package fr.itris.glips.rtda.action.svg;

import java.awt.Container;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.action.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 *the class used to change a value
 * @author ITRIS, Jordi SUC
 */
public class SendMeasure extends AbstractSendMeasure {

    /**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the parent container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
    public SendMeasure(SVGPicture picture, String projectName, Container parent, JComponent component, Object actionObject, Element actionElement, JWidgetRuntime jwidgetRuntime) {
        super(picture, projectName, parent, component, actionObject, actionElement, jwidgetRuntime);
        eventsManager = new SVGEventsManager(this);
        initializeAction();
    }

    @Override
    protected void initializeAction() {
        super.initializeAction();
        try {
            toolTipText = bundle.getString("tooltip_sendMeasure") + "<i>" + actionElement.getAttribute(tagToWriteName) + "</i>";
        } catch (Exception ex) {
        }
    }
}
