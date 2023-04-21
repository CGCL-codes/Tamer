package adapter.commonData;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import serviceManagement.IInstanceManager;
import serviceManagement.InstanceManager;
import xmlProcessing.XMLFileReader;
import adapter.AdapterException;
import adapter.IAdapterManager;
import coordinator.wsaddressing.AttributedURI;
import coordinator.wsaddressing.EndpointReferenceType;

/**
 * Manages data that can be accessed by all parts of the adapter. It
 * contains the address of the adapter, and methods to manage adapter
 * manager instances.
 * 
 * @author Michael Sch�fer
 *
 */
public class AdapterData implements IAdapterData {

    private String adapterAddress;

    private EndpointReferenceType adapterReference;

    /**
	 * Constructs a new empty adapter data object.
	 * @param adapterDefinitionLocation The location of the adapter definition XML file.
	 * @throws AdapterException 
	 */
    public AdapterData(String adapterDefinitionLocation) throws AdapterException {
        this.adapterAddress = null;
        this.adapterReference = null;
        this.readAdapterConfiguration(adapterDefinitionLocation);
    }

    /**
	 * Returns the endpoint reference of the adapter service.
	 * @return The adapter service endpoint.
	 */
    public EndpointReferenceType getAdapterService() {
        return this.adapterReference;
    }

    /**
	 * Returns the adapter manager instance responsible for the given identifier.
	 * @param identifier The identifier which specifies the manager.
	 * @return The adapter manager, or null if none exists or the identifier is invalid.
	 * @throws AdapterException
	 */
    public IAdapterManager getManagerInstance(String identifier) throws AdapterException {
        if (identifier == null || identifier.equals("")) throw new AdapterException("The identifier is either null or empty.");
        IInstanceManager repository = InstanceManager.getInstanceManager();
        if (repository.hasInstance(this.adapterAddress, identifier)) return (IAdapterManager) repository.getInstance(this.adapterAddress, identifier); else throw new AdapterException("No manager instance was found for the specified identifier.");
    }

    /**
	 * Registers the given adapter manager instance with the given
	 * identifier.
	 * @param identifier The identifier which specifies the manager.
	 * @param manager The manager instance which is responsible for the given identifier.
	 * @throws AdapterException
	 */
    public void registerManagerInstance(String identifier, IAdapterManager manager) throws AdapterException {
        if (identifier == null || identifier.equals("") || manager == null) throw new AdapterException("The identifier is either null or empty, or the manager object is null.");
        IInstanceManager repository = InstanceManager.getInstanceManager();
        repository.addInstance(this.adapterAddress, identifier, manager);
    }

    /**
	 * Reads out the information from the adapter definition file,
	 * which can be found at the given location string.
	 * @param adapterDefinitionLocation The location of the adapter's definition file.
	 * @throws AdapterException
	 */
    private void readAdapterConfiguration(String adapterDefinitionLocation) throws AdapterException {
        File definitionFile = new File(adapterDefinitionLocation);
        if (!definitionFile.exists()) throw new AdapterException("The adapter definition file " + adapterDefinitionLocation + " does not exist.");
        try {
            Document document = XMLFileReader.readXMLFile(definitionFile);
            NodeList adapterElements = document.getElementsByTagNameNS(AdapterConstants.NAMESPACE_ADAPTERDEFINITION, AdapterConstants.ELEMENT_ADAPTER);
            if (adapterElements.getLength() < 1) throw new AdapterException("No adapter definition was found.");
            Element adapterElement = (Element) adapterElements.item(0);
            if (adapterElement.hasAttribute(AdapterConstants.ATTRIBUTE_ADAPTER_SERVICEADDRESS)) {
                this.adapterAddress = adapterElement.getAttribute(AdapterConstants.ATTRIBUTE_ADAPTER_SERVICEADDRESS);
                this.adapterReference = new EndpointReferenceType();
                AttributedURI address = new AttributedURI(this.adapterAddress);
                this.adapterReference.setAddress(address);
            } else {
                throw new AdapterException("The attribute 'serviceAddress' was not found.");
            }
        } catch (Exception e) {
            throw new AdapterException("The adapter definition file " + adapterDefinitionLocation + " could not be read successfully.");
        }
    }
}
