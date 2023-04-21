package org.openremote.modeler.client.rpc;

import java.util.List;
import org.openremote.modeler.domain.DeviceCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DeviceCommandRPCService</code>.
 */
public interface DeviceCommandRPCServiceAsync {

    /**
    * Save all.
    * 
    * @param deviceCommands the device commands
    * @param callback the callback
    */
    void saveAll(List<DeviceCommand> deviceCommands, AsyncCallback<List<DeviceCommand>> callback);

    /**
    * Save.
    * 
    * @param deviceCommand the device command
    * @param callback the callback
    */
    void save(DeviceCommand deviceCommand, AsyncCallback<DeviceCommand> callback);

    /**
    * Update.
    * 
    * @param deviceCommand the device command
    * @param callback the callback
    */
    void update(DeviceCommand deviceCommand, AsyncCallback<DeviceCommand> callback);

    /**
    * Load by id.
    * 
    * @param id the id
    * @param callback the callback
    */
    void loadById(long id, AsyncCallback<DeviceCommand> callback);

    /**
    * Delete command.
    * 
    * @param id
    *           the id
    * @param callback
    *           the callback
    */
    void deleteCommand(long id, AsyncCallback<Boolean> callback);

    /**
    * Load by device.
    * 
    * @param id the id
    * @param asyncCallback the async callback
    */
    void loadByDevice(long id, AsyncCallback<List<DeviceCommand>> asyncCallback);
}
