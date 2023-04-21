package org.openremote.controller.protocol.infrared;

import java.util.List;
import org.jdom.Element;
import org.openremote.controller.command.Command;
import org.openremote.controller.command.CommandBuilder;
import org.openremote.controller.exception.CommandBuildException;
import org.openremote.controller.utils.CommandUtil;

/**
 * The IREvent Builder which can build a IREvent from a DOM Element in controller.xml.
 * 
 * @author Dan 2009-4-3
 */
public class IRCommandBuilder implements CommandBuilder {

    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    public Command build(Element element) {
        IRCommand irCommand = new IRCommand();
        String command = "";
        List<Element> propertyEles = element.getChildren("property", element.getNamespace());
        String name = "";
        for (Element ele : propertyEles) {
            if ("name".equals(ele.getAttributeValue("name"))) {
                name = ele.getAttributeValue("value");
            } else if ("command".equals(ele.getAttributeValue("name"))) {
                command = ele.getAttributeValue("value");
            }
        }
        if ("".equals(command.trim()) || "".equals(name.trim())) {
            throw new CommandBuildException("Cannot build a IREvent with empty property : command=" + command + ",name=" + name);
        } else {
            irCommand.setCommand(CommandUtil.parseStringWithParam(element, command));
            irCommand.setName(name);
        }
        return irCommand;
    }
}
