package com.abiquo.api.resources;

import com.abiquo.framework.domain.ResourceInstance;

/**
 * This class represents a SquidPlusResource. It takes as the SquidResource as a base and extends it
 * on the fact of add it a new sensor for the humidity of the terrain.
 * 
 @verbatim
 <?xml version="1.0" encoding="utf-8"?>
 <Resource xmlns="http://http://cpal.abiquo.com/schema/Domain.xsd" LocalId="SquidPlusResource">
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
 * @see SquidResource
 */
public class SquidPlusResource extends SquidResource {

    /**
	 * String containing the identifier for this resource.
	 * @note To avoid misspellings when provision a resource, you should use this constant ID instead of a manually typed string.
	 */
    public static final String ID = "SquidPlus";

    /**
	 * Converts a generic ResourceInstance into a new one of type SquidPlusResource.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 * 
	 * @return The resultant SquidPlusResource
	 */
    public static SquidPlusResource cast(ResourceInstance resourceInstance) {
        return new SquidPlusResource(resourceInstance);
    }

    /**
	 * Creates a new SquidPlusResource based on a generic ResourceInstance.
	 * 
	 * @param resourceInstance
	 *            The generic ResourceInstance
	 */
    public SquidPlusResource(ResourceInstance resourceInstance) {
        super(resourceInstance);
    }
}
