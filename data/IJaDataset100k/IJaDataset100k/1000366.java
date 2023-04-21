package net.eejits.ejbgen;

/**
 * The <code>Config</code> interface specifies the functionality that 
 * should be made available by classes that will be used to load the
 * configuration data required by EJBGen to generate EJB sources.
 * 
 * @author Nick Sharples
 */
public interface Config {

    /**
	 * Provides access to the <code>Entity</code> objects that model
	 * the beans that will be created.
	 * 
	 * @return an array of <code>Entity</code> objects
	 * @see net.eejits.ejbgen.Entity
	 */
    public Entity[] getEntities();
}
