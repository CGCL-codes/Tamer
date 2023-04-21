package org.openremote.beehive.api.dto.modeler;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.openremote.beehive.api.dto.AccountDTO;
import org.openremote.beehive.api.dto.BusinessEntityDTO;
import org.openremote.beehive.domain.Account;
import org.openremote.beehive.domain.modeler.DeviceMacro;

/**
 * The Class is used for transmitting device macro info.
 *
 * @author tomsky
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "deviceMacro")
public class DeviceMacroDTO extends BusinessEntityDTO {

    private List<DeviceMacroItemDTO> deviceMacroItems = new ArrayList<DeviceMacroItemDTO>();

    private String name;

    private AccountDTO account;

    @XmlElementWrapper(name = "deviceMacroItems")
    @XmlElementRef(type = DeviceMacroItemDTO.class)
    public List<DeviceMacroItemDTO> getDeviceMacroItems() {
        return deviceMacroItems;
    }

    public void setDeviceMacroItems(List<DeviceMacroItemDTO> deviceMacroItems) {
        this.deviceMacroItems = deviceMacroItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public void addDeviceMacroItem(DeviceMacroItemDTO deviceMacroItemDTO) {
        deviceMacroItems.add(deviceMacroItemDTO);
    }

    public DeviceMacro toDeviceMacro() {
        DeviceMacro deviceMacro = new DeviceMacro();
        deviceMacro.setName(name);
        deviceMacro.setOid(getId());
        return deviceMacro;
    }

    public DeviceMacro toDeviceMacroWithContent(Account dbAccount) {
        DeviceMacro deviceMacro = toDeviceMacro();
        deviceMacro.setAccount(dbAccount);
        if (deviceMacroItems != null) {
            for (DeviceMacroItemDTO deviceMacroItemDTO : deviceMacroItems) {
                deviceMacro.addDeviceMacroItem(deviceMacroItemDTO.toDeviceMacroItem(deviceMacro));
            }
        }
        return deviceMacro;
    }
}
