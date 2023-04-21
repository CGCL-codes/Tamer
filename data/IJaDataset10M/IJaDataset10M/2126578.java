package org.openremote.controller.protocol.wol;

import java.util.List;
import org.jdom.Element;
import org.openremote.controller.Constants;
import org.openremote.controller.command.Command;
import org.openremote.controller.command.CommandBuilder;
import org.openremote.controller.exception.NoSuchCommandException;
import org.openremote.controller.utils.Logger;

/**
 * Builds a WakeOnLandCommand which can be used to send WOL magic packets onto the LAN to wakeup a pc.
 * 
 * @author Marcus Redeker
 */
public class WakeOnLanCommandBuilder implements CommandBuilder {

    public static final String WOL_PROTOCOL_LOG_CATEGORY = Constants.CONTROLLER_PROTOCOL_LOG_CATEGORY + "WOL";

    private static final String STR_ATTRIBUTE_NAME_MAC_ADDRESS = "macAddress";

    private static final String STR_ATTRIBUTE_NAME_BROADCAST_IP = "broadcastIp";

    private static final Logger logger = Logger.getLogger(WOL_PROTOCOL_LOG_CATEGORY);

    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    public Command build(Element element) {
        logger.debug("Building WOL command");
        List<Element> propertyEles = element.getChildren("property", element.getNamespace());
        String macAddress = null;
        String broadcastIp = null;
        for (Element ele : propertyEles) {
            String elementName = ele.getAttributeValue(CommandBuilder.XML_ATTRIBUTENAME_NAME);
            String elementValue = ele.getAttributeValue(CommandBuilder.XML_ATTRIBUTENAME_VALUE);
            if (STR_ATTRIBUTE_NAME_MAC_ADDRESS.equals(elementName)) {
                macAddress = elementValue;
                logger.debug("WOL Command: macAddress = " + macAddress);
            } else if (STR_ATTRIBUTE_NAME_BROADCAST_IP.equals(elementName)) {
                broadcastIp = elementValue;
                logger.debug("WOL Command: broadcastIp = " + broadcastIp);
            }
        }
        if (null == macAddress || null == broadcastIp || macAddress.trim().length() == 0 || broadcastIp.trim().length() == 0) {
            throw new NoSuchCommandException("WOL command must have a both properties 'macAddress' and 'broadcastIp'.");
        }
        logger.debug("WOL Command created successfully");
        return new WakeOnLanCommand(macAddress, broadcastIp);
    }
}
