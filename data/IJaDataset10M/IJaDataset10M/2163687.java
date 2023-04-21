package org.openremote.controller.control.switchtoggle;

import java.util.List;
import org.jdom.Element;
import org.openremote.controller.command.Command;
import org.openremote.controller.command.ExecutableCommand;
import org.openremote.controller.command.StatusCommand;
import org.openremote.controller.control.Control;
import org.openremote.controller.control.ControlBuilder;
import org.openremote.controller.control.Status;
import org.openremote.controller.exception.NoSuchCommandException;

/**
 * It is mainly responsible for build Switch control with control element and commandParam.
 * 
 * @author Handy.Wang 2009-10-23
 */
public class SwitchBuilder extends ControlBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public Control build(Element controlElement, String commandParam) {
        if (!isContainAction(commandParam)) {
            return new Switch();
        }
        List<Element> operationElements = controlElement.getChildren();
        Switch switchToggle = new Switch();
        for (Element operationElement : operationElements) {
            if (commandParam.equalsIgnoreCase(operationElement.getName()) && Control.STATUS_ELEMENT_NAME.equals(operationElement.getName())) {
                Element statusCommandRefElement = (Element) operationElement.getChildren().get(0);
                String statusCommandID = statusCommandRefElement.getAttributeValue(Control.CONTROL_COMMAND_REF_ATTRIBUTE_NAME);
                Element statusCommandElement = remoteActionXMLParser.queryElementFromXMLById(statusCommandID);
                if (statusCommandElement != null) {
                    StatusCommand statusCommand = (StatusCommand) commandFactory.getCommand(statusCommandElement);
                    switchToggle.setStatus(new Status(statusCommand));
                    break;
                } else {
                    throw new NoSuchCommandException("Cannot find that command with id = " + statusCommandID);
                }
            }
            if (commandParam.equalsIgnoreCase(operationElement.getName())) {
                List<Element> commandRefElements = operationElement.getChildren();
                for (Element commandRefElement : commandRefElements) {
                    String commandID = commandRefElement.getAttributeValue(Control.CONTROL_COMMAND_REF_ATTRIBUTE_NAME);
                    Element commandElement = remoteActionXMLParser.queryElementFromXMLById(commandID);
                    Command command = commandFactory.getCommand(commandElement);
                    switchToggle.addExecutableCommand((ExecutableCommand) command);
                }
                break;
            }
        }
        return switchToggle;
    }

    /**
    * Checks if is contain action.
    * 
    * @return true, if is contain action
    */
    private boolean isContainAction(String commandParam) {
        for (String action : Switch.AVAILABLE_ACTIONS) {
            if (action.equalsIgnoreCase(commandParam)) {
                return true;
            }
        }
        return false;
    }
}
