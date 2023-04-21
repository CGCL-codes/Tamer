package org.openremote.beehive.api.dto.modeler;

import javax.xml.bind.annotation.XmlRootElement;
import org.openremote.beehive.api.dto.BusinessEntityDTO;
import org.openremote.beehive.domain.modeler.Protocol;
import org.openremote.beehive.domain.modeler.ProtocolAttr;

/**
 * The Class is used for transmitting protocol attribute info.
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "attribute")
public class ProtocolAttrDTO extends BusinessEntityDTO {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ProtocolAttr toProtocolAttr(Protocol protocol) {
        ProtocolAttr protocolAttr = new ProtocolAttr();
        protocolAttr.setOid(getId());
        protocolAttr.setName(name);
        protocolAttr.setValue(value);
        protocolAttr.setProtocol(protocol);
        return protocolAttr;
    }
}
