package org.openremote.modeler.client.rpc;

import java.util.List;
import org.openremote.modeler.domain.DeviceMacro;
import org.openremote.modeler.domain.DeviceMacroItem;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface defines methods to operate macro and macroItem.
 */
@RemoteServiceRelativePath("deviceMacro.smvc")
public interface DeviceMacroRPCService extends RemoteService {

    /**
    * Load all device macros.
    * 
    * @return the list< device macro>
    */
    List<DeviceMacro> loadAll();

    /**
    * Save device macro.
    * 
    * @param deviceMacro the device macro
    * 
    * @return the device macro
    */
    DeviceMacro saveDeviceMacro(DeviceMacro deviceMacro);

    /**
    * Update device macro.
    * 
    * @param deviceMacro the device macro
    * 
    * @return the device macro
    */
    DeviceMacro updateDeviceMacro(DeviceMacro deviceMacro);

    /**
    * Delete device macro by id.
    * 
    * @param id the id
    */
    void deleteDeviceMacro(long id);

    /**
    * Load all device macro items under a device macro.
    * 
    * @param deviceMacro the device macro
    * 
    * @return the list< device macro item>
    */
    List<DeviceMacroItem> loadDeviceMacroItems(DeviceMacro deviceMacro);
}
