package com.abiquo.api.resources;

import java.util.ArrayList;
import java.util.List;
import com.abiquo.framework.domain.InstanceName;
import com.abiquo.framework.domain.ResourceInstance;
import com.abiquo.framework.domain.types.IBaseType;
import com.abiquo.framework.domain.types.StringType;
import com.abiquo.framework.task.Job;

/**
 * This class represents a SquidResource. It gathers information from SquidBee devices
 * through a ZigBee gateway. 
 *
 @verbatim
 <?xml version="1.0" encoding="utf-8"?>
 <Resource xmlns="http://http://cpal.abiquo.com/schema/Domain.xsd" LocalId="SquidResource">
    <FunctionalityList >

      <Functionality LocalId="listen" ReturnType="void">
        <AttributeList>
          <Attribute Name="Session" Type="DataReference" />
        </AttributeList>
      </Functionality>

      <Functionality LocalId="getSample" ReturnType="string">
        <AttributeList>
          <Attribute Name="Sensor" Type="String" />
          <Attribute Name="MoteId" Type="String" />
        </AttributeList>
      </Functionality>        

      <Functionality LocalId="GetMoteList" ReturnType="string"/>
      
    </FunctionalityList>
 </Resource>
 @endverbatim

 * @see SquidFakeResource
 * @see SquidPlusResource
 */
public class SquidResource extends ResourceInstance {

    /**
	 * String containing the identifier for this resource.
	 * @note To avoid misspellings when provision a resource, you should use this constant ID instead of a manually typed string.
	 */
    public static String ID = "Squid";

    /**
	 * Converts a generic ResourceInstance into a new one of type SquidResource.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 * 
	 * @return The resultant SquidResource
	 */
    public static SquidResource cast(ResourceInstance resourceInstance) {
        return new SquidResource(resourceInstance);
    }

    /**
	 * Creates a new SquidResource based on a generic ResourceInstance.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 */
    public SquidResource(ResourceInstance resourceInstance) {
        super(resourceInstance);
    }

    /**
	 * Creates a new Job which once executed, returns the list of connected SquidBee devices.
	 * 
	 * @return The Job to perform the inquiry
	 */
    public Job getMoteList() {
        Job job = new Job(this.getInstanceName(), "getMoteList");
        job.setAccess(getAccess());
        return job;
    }

    /**
	 * Creates a new Job which once executed, returns the sample for the specified SquidBee device.
	 * 
	 * @param moteId 
	 * 		A string containing the mote identifier
	 * @param sensor 
	 * 		A sensor type (humidity, lightness, temperature, and so on)
	 * 
	 * @return The Job to perform the demand
	 */
    public Job getSample(String moteId, String sensor) {
        List<IBaseType> lstDefAtt = new ArrayList<IBaseType>();
        lstDefAtt.add(new StringType("MoteId", moteId));
        lstDefAtt.add(new StringType("Sensor", sensor));
        Job job = new Job(getInstanceName(), "getSample", lstDefAtt);
        job.setAccess(getAccess());
        return job;
    }

    /**
	 * Creates a new Job which once executed, provides a ZigBee feed for the resource.
	 * 
	 * @param resourceInstance 
	 * 			A valid ResourseInstance of a previously created ZigBee resource.
	 * 
	 * @return The Job to perform the demand
	 */
    public Job listen(InstanceName resourceInstance) {
        List<IBaseType> attributes = new ArrayList<IBaseType>();
        attributes.add(new StringType("Session", resourceInstance.getSession()));
        Job job = new Job(this.getInstanceName(), "listen", attributes);
        job.setAccess(getAccess());
        return job;
    }
}
