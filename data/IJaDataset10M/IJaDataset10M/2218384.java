package org.sourceforge.myjavaconf;

/**
 * @author arunmuralidharan This acts as a common interface for different types of configurators
 */
public interface Configurator {

    public void configure(PropertiesConfigurable propConfigurable);

    public void configure(PropertiesConfigurable propConfigurable, String relativePath);

    public void configure(JAXBConfigurable jaxbConfigurable);

    public void configure(JAXBConfigurable jaxbConfigurable, String relativePath);

    public void configure(FreeFormConfigurable ffConfigurable);

    public void configure(FreeFormConfigurable ffConfigurable, String relativePath);

    public void persist(PropertiesConfigurable propConfigurable);

    public void persist(PropertiesConfigurable propConfigurable, String relativePath);

    public void persist(FreeFormConfigurable ffConfigurable);

    public void persist(FreeFormConfigurable ffConfigurable, String relativePath);

    public void persist(JAXBConfigurable jaxbConfigurable);

    public void persist(JAXBConfigurable jaxbConfigurable, String relativePath);
}
