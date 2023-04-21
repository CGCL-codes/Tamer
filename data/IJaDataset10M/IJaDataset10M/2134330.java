package net.sourceforge.geeboss.model.midi.memory;

import static net.sourceforge.geeboss.model.XmlConstants.*;
import net.sourceforge.geeboss.util.ConfigurationUtil;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

/**
 * A reference to a memory object of a specified class
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class MemoryObjectVariable implements Reference {

    /** Name for the variable */
    private String mName;

    /** Name of the references memory class */
    private String mClass;

    /** Base address for this memory object */
    private long mBaseAddress;

    /**
     * Create a new MemoryObjectVariable given it's configuration
     * @param configuration the configuration
     */
    public MemoryObjectVariable(Configuration configuration) throws ConfigurationException {
        loadConfiguration(configuration);
    }

    /**
     * Create the referenced object
     * @param parent the parentn memory object
     * @param manager the MemoryManager
     * @return the referenced object
     */
    public MemoryObject createReference(MemoryObject parent, MemoryManager manager) {
        return manager.getMemoryClass(mClass).createInstance(mName, parent, mBaseAddress, manager);
    }

    /**
     * Load this MemoryObjectVariable's configuration
     * @param configuration the configuration object
     */
    private void loadConfiguration(Configuration configuration) throws ConfigurationException {
        ConfigurationUtil.checkElementName(configuration, MEMORY_OBJECT_ELEMENT);
        mClass = ConfigurationUtil.checkAndGetAttribute(configuration, MEMORY_OBJECT_CLASS_ATTRIBUTE);
        mName = ConfigurationUtil.checkAndGetElementValue(configuration, MEMORY_OBJECT_NAME_ELEMENT);
        mBaseAddress = ConfigurationUtil.checkAndGetHexElementValue(configuration, MEMORY_OBJECT_ADDRESS_ELEMENT);
    }
}
