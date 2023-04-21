package org.openremote.modeler.client.rpc;

import java.util.List;
import org.openremote.modeler.domain.Account;
import org.openremote.modeler.domain.Device;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface DeviceService, define the method to operate device.
 */
@RemoteServiceRelativePath("device.smvc")
public interface DeviceRPCService extends RemoteService {

    /**
    * Save device.
    * 
    * @param device the device
    * 
    * @return the device
    */
    Device saveDevice(Device device);

    /**
    * Update device.
    * 
    * @param device the device
    */
    void updateDevice(Device device);

    /**
    * Delete device.
    * 
    * @param id the id
    */
    void deleteDevice(long id);

    /**
    * Load by id.
    * 
    * @param id the id
    * 
    * @return the device
    */
    Device loadById(long id);

    /**
    * Load all.
    * 
    * @return the list< device>
    */
    List<Device> loadAll();

    /**
    * Load all.
    * 
    * @param account the account
    * 
    * @return the list< device>
    */
    List<Device> loadAll(Account account);

    Account getAccount();
}
