package jmcf.impl;

import jmcf.core.ICompositeComponentProxy;
import jmcf.core.IContext;
import jmcf.impl.AbstractSimpleComponent;
import jmcf.impl.AbstractComponentContext;
import jmcf.impl.Framework;

/**
 *
 * @author Mauro Dragone
 */
public class AbstractCompositeComponent extends AbstractSimpleComponent implements ICompositeComponentProxy {

    AbstractComponentContext context;

    public AbstractCompositeComponent() {
        super();
        context = Framework.getFramework().createNewContext(this);
    }

    public IContext getInsideContext() {
        return context;
    }
}
