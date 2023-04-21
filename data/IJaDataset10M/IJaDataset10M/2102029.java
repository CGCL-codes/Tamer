package org.openremote.android.controller.protocol.socket;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.openremote.android.controller.command.CommandBuilder;
import org.openremote.android.controller.command.ExecutableCommand;
import org.openremote.android.controller.util.CommandUtil;

/**
 * The Class SocketEventBuilder.
 *
 * @author Marcus 2009-4-26
 * @author marcf@openremote.org
 */
public class TCPSocketCommandBuilder implements CommandBuilder {

    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    public ExecutableCommand build(Element element) {
        TCPSocketCommand tcpEvent = new TCPSocketCommand();
        NodeList properties = element.getElementsByTagName("property");
        for (int i = 0; i < properties.getLength(); i++) {
            Element property = (Element) properties.item(i);
            if ("name".equals(property.getAttribute("name"))) {
                tcpEvent.setName(property.getAttribute("value"));
            } else if ("port".equals(property.getAttribute("name"))) {
                tcpEvent.setPort(property.getAttribute("value"));
            } else if ("ipAddress".equals(property.getAttribute("name"))) {
                tcpEvent.setIp(property.getAttribute("value"));
            } else if ("command".equals(property.getAttribute("name"))) {
                tcpEvent.setCommand(CommandUtil.parseStringWithParam(element, property.getAttribute("value")));
            }
        }
        return tcpEvent;
    }
}
