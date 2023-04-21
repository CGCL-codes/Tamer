package org.openremote.beehive.api.service.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.openremote.beehive.api.service.DeviceMacroItemService;
import org.openremote.beehive.domain.modeler.DeviceCommand;
import org.openremote.beehive.domain.modeler.DeviceCommandRef;
import org.openremote.beehive.domain.modeler.DeviceMacro;
import org.openremote.beehive.domain.modeler.DeviceMacroItem;
import org.openremote.beehive.domain.modeler.DeviceMacroRef;

/**
 *
 * @author tomsky
 */
public class DeviceMacroItemServiceImpl extends BaseAbstractService<DeviceMacroItem> implements DeviceMacroItemService {

    public void deleteByDeviceMacro(DeviceMacro targetDeviceMacro) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DeviceMacroRef.class);
        List<DeviceMacroRef> deviceMacroRefs = genericDAO.findByDetachedCriteria(criteria.add(Restrictions.eq("targetDeviceMacro", targetDeviceMacro)));
        genericDAO.deleteAll(deviceMacroRefs);
    }

    /**
    * {@inheritDoc}
    * @see org.openremote.modeler.service.DeviceMacroItemService#deleteByDeviceCommand(org.openremote.modeler.domain.DeviceCommand)
    */
    public void deleteByDeviceCommand(DeviceCommand deviceCommand) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DeviceCommandRef.class);
        List<DeviceCommandRef> deviceCommandRefs = genericDAO.findByDetachedCriteria(criteria.add(Restrictions.eq("deviceCommand", deviceCommand)));
        genericDAO.deleteAll(deviceCommandRefs);
    }
}
