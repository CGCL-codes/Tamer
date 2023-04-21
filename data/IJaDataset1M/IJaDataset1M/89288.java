package fr.itris.glips.rtdaeditor.anim.action;

import javax.swing.*;
import fr.itris.glips.rtda.toolkit.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;

/**
 * the class displayed a dialog box used to configure a custom action
 * @author ITRIS, Jordi SUC
 */
public abstract class ActionConfigurator {

    /**
	 * displays the configuration dialog displayed relatively to the given component
	 * so that the action is configured by the user
	 * @param actionElement the element defining the action
	 * @param originComponent a component
	 */
    public abstract void configure(Element actionElement, JComponent originComponent);

    /**
	 * shows a dialog that requires the user to select a new enumerated tag value
	 * @param originComponent a component relatively from which the tag chooser dialog will be displayed
	 * @param actionElement the element defining the action
	 * @param initialTagValue the inital tag value
	 * @return the selected tag name
	 */
    protected String showEnumeratedTagValueChooser(JComponent originComponent, Element actionElement, String initialTagValue) {
        String tagValue = "";
        DataBaseNodeFilter filter = new DataBaseNodeFilter(initialTagValue, 0, TagToolkit.ENUMERATED, false, false, null);
        DataBaseNodeChooser nodeChooser = DataBaseNodeChooser.getDataBaseNodeChooser(Editor.getParent(), actionElement.getOwnerDocument(), filter, false, false);
        nodeChooser.showDialog(originComponent);
        nodeChooser.disposeDialog();
        DataBaseNodeInformation info = nodeChooser.getInfo();
        if (info != null) {
            String newValue = info.getXmlPath();
            if (newValue == null) {
                newValue = "";
            }
            tagValue = newValue;
        }
        return tagValue;
    }

    /**
	 * shows a dialog that requires the user to select a new analogic tag value
	 * @param originComponent a component relatively from which the tag chooser dialog will be displayed
	 * @param actionElement the element defining the action
	 * @param initialTagValue the inital tag value
	 * @return the selected tag name
	 */
    protected String showAnalogicTagValueChooser(JComponent originComponent, Element actionElement, String initialTagValue) {
        String tagValue = "";
        DataBaseNodeFilter filter = new DataBaseNodeFilter(initialTagValue, 0, TagToolkit.ANALOGIC, false, false, null);
        DataBaseNodeChooser nodeChooser = DataBaseNodeChooser.getDataBaseNodeChooser(Editor.getParent(), actionElement.getOwnerDocument(), filter, false, false);
        nodeChooser.showDialog(originComponent);
        nodeChooser.disposeDialog();
        DataBaseNodeInformation info = nodeChooser.getInfo();
        if (info != null) {
            String newValue = info.getXmlPath();
            if (newValue == null) {
                newValue = "";
            }
            tagValue = newValue;
        }
        return tagValue;
    }

    /**
	 * shows a dialog that requires the user to select a new view name
	 * @param originComponent a component relatively from which the view chooser dialog will be displayed
	 * @param actionElement the element defining the action
	 * @param initialViewName the inital view name
	 * @return the selected tag name
	 */
    protected String showViewNameChooser(JComponent originComponent, Element actionElement, String initialViewName) {
        String viewName = "";
        DataBaseNodeFilter filter = new DataBaseNodeFilter(initialViewName, 0, TagToolkit.VIEW, false, false, null);
        DataBaseNodeChooser nodeChooser = DataBaseNodeChooser.getDataBaseNodeChooser(Editor.getParent(), actionElement.getOwnerDocument(), filter, true, false);
        nodeChooser.showDialog(originComponent);
        nodeChooser.disposeDialog();
        DataBaseNodeInformation info = nodeChooser.getInfo();
        if (info != null) {
            String newValue = info.getXmlPath();
            if (newValue == null) {
                newValue = "";
            }
            viewName = newValue;
        }
        return viewName;
    }
}
