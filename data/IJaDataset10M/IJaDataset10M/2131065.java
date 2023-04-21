package com.abiquo.api.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.abiquo.framework.config.FrameworkTypes.ResourceStatus;
import com.abiquo.framework.domain.InstanceName;
import com.abiquo.framework.domain.ResourceInstance;
import com.abiquo.framework.domain.types.DataType;
import com.abiquo.framework.domain.types.IBaseType;
import com.abiquo.framework.domain.types.StringType;
import com.abiquo.framework.task.Job;
import com.abiquo.util.Base64Utils;

/**
 * This class represents a BluetoothResource, and allows to interact with Bluetooth devices.
 * 
 * @note XML description of the resource:
 @verbatim 
 <?xml version="1.0" encoding="utf-8"?>
 <Resource xmlns="http://www.abiquo.com/schema/Domain.xsd" LocalId="BluetoothResource">
  		<FunctionalityList> 
  			<Functionality LocalId="inquiry">
  				<AttributeList>
            		<Attribute Name="Type" Type="String" />
            	</AttributeList>
           </Functionality>
         	<Functionality LocalId="sendfile">
         		<AttributeList>
         			<Attribute Name="Input" Type="DataReference" />
         			<Attribute Name="MAC" Type="String" />
         		</AttributeList>
         	</Functionality>
		</FunctionalityList> 
 </Resource>
 @endverbatim
 */
public class BluetoothResource extends ResourceInstance {

    /**
	 * String containing the identifier for this resource.
	 * @note To avoid misspellings when provision a resource, you should use this constant ID instead of a manually typed string.
	 */
    public static final String ID = "BluetoothResource";

    /**
	 * Converts a generic ResourceInstance into a new one of type BluetoothResource.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 * 
	 * @return The resultant BluetoothResource
	 */
    public static BluetoothResource cast(ResourceInstance resourceInstance) {
        return new BluetoothResource(resourceInstance);
    }

    /**
	 * Creates a new BluetoothResource based on a generic ResourceInstance.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 */
    public BluetoothResource(ResourceInstance resourceInstance) {
        super(resourceInstance);
    }

    /**
	 * Creates a new BluetoothResource based on the parameters.
	 * 
	 * @param name
	 *            The name of the resource
	 * @param instanceName
	 *            An InstanceName
	 * @param propertiesList
	 *            Properties list
	 */
    public BluetoothResource(String name, InstanceName instanceName, List<IBaseType> propertiesList) {
        super(name, instanceName, propertiesList, ResourceStatus.UNKNOWN);
    }

    /**
	 * Creates a new Job which once executed, returns the list of friendly names for all the Bluetooth devices.
	 * 
	 * @return The Job containing information about the demand
	 */
    public Job friendlyNameInquiry() {
        return inquiry("FriendlyName");
    }

    /**
	 * Creates a new Job which once executed, returns the list of MAC for all the Bluetooth devices.
	 * 
	 * @return The Job containing information about the demand
	 */
    public Job macInquiry() {
        return inquiry("MAC");
    }

    /**
	 * Handles all the available types of inquiry in the same method.
	 * 
	 * @param type
	 *            The type of the inquiry, MAC or FriendlyName
	 * 
	 * @return The Job containing information about the demand
	 */
    private Job inquiry(String type) {
        List<IBaseType> attrs = new ArrayList<IBaseType>();
        attrs.add(new StringType("Type", type));
        Job job = new Job(getInstanceName(), "inquiry", attrs);
        job.setAccess(getAccess());
        return job;
    }

    /**
	 * Creates a new Job which once executed, sends a file through the Bluetooth device identified by the MAC address
	 * passed by parameter.
	 * 
	 * @param mac
	 *            The MAC address of the Bluetooth device
	 * @param file
	 *            The file to send
	 * 
	 * @return The Job containing information of the demand
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Job sendFile(String mac, File file) throws IOException {
        List<IBaseType> attrs = new ArrayList<IBaseType>();
        attrs.add(new StringType("MAC", mac));
        attrs.add(new StringType("LocalId", file.getName()));
        attrs.add(new DataType("Input", Base64Utils.encodeFromFile(file.getAbsolutePath())));
        Job job = new Job(getInstanceName(), "sendFile", attrs);
        job.setAccess(getAccess());
        return job;
    }
}
