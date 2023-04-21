package org.openremote.beehive.domain.modeler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.openremote.beehive.api.dto.modeler.ProtocolAttrDTO;
import org.openremote.beehive.domain.BusinessEntity;

/**
 * The Attribute of Protocol.
 * 
 * @author Dan 2009-7-6
 */
@Entity
@Table(name = "protocol_attr")
public class ProtocolAttr extends BusinessEntity {

    private static final long serialVersionUID = 7659446044086879559L;

    /** The name. */
    private String name;

    /** The value. */
    private String value;

    /** The protocol. */
    private Protocol protocol;

    /**
    * Gets the name.
    * 
    * @return the name
    */
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
    * Sets the name.
    * 
    * @param name the new name
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Gets the value.
    * 
    * @return the value
    */
    @Column(nullable = false)
    @Lob
    public String getValue() {
        return value;
    }

    /**
    * Sets the value.
    * 
    * @param value the new value
    */
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * Gets the protocol.
    * 
    * @return the protocol
    */
    @ManyToOne
    @JoinColumn(nullable = false)
    public Protocol getProtocol() {
        return protocol;
    }

    /**
    * Sets the protocol.
    * 
    * @param protocol the new protocol
    */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ProtocolAttrDTO toDTO() {
        ProtocolAttrDTO protocolDTO = new ProtocolAttrDTO();
        protocolDTO.setId(getOid());
        protocolDTO.setName(name);
        protocolDTO.setValue(value);
        return protocolDTO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProtocolAttr other = (ProtocolAttr) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
