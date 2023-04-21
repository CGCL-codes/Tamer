package com.ail.core.configure;

import com.ail.core.factory.AbstractFactory;

/**
 * A "Builder" represents an instance of a class builder - i.e. a class factory. There may
 * be an arbitrary number of builders in any live system each capable of building class based
 * on different inputs. e.g. and BeanShellBuilder while builds class based on a bean shell script, 
 * or a CastorXMLBuilder whild builds classes based on the XML representation of a class instance.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/Builder.java,v $
 * @stereotype type
 */
public class Builder extends Group {

    static final long serialVersionUID = -2392094239125625998L;

    private String factory;

    private transient AbstractFactory instance = null;

    /**
     * Default constructor.
     */
    public Builder() {
    }

    /**
     * Get the name of the factory class (e.g. com.ail.core.factory.BeanShellFactory).
     * @return The name of the factory class.
     */
    public String getFactory() {
        return factory;
    }

    /**
     * Set the name of the factory class.
     * @param factory The name of the Factory class
     */
    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     * Fetch the cached instance of the factory.
     * @return An instance of the factory class.
     */
    public AbstractFactory getInstance() {
        return instance;
    }

    /**
     * Set the instance of the factory class.
     * @param instance Instance of the class.
     */
    public void setInstance(AbstractFactory instance) {
        this.instance = instance;
    }
}
