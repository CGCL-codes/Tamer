package org.openremote.beehive.api.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the top level hierarchy shown in http://lirc.sourceforge.net/remotes/. Such as Sony, Apple, Samsung etc.
 * 
 * @author allen.wei 2009-2-17
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "vendor")
public class VendorDTO extends BusinessEntityDTO {

    private String name;

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
