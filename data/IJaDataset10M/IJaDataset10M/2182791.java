package net.sourceforge.javautil.cdi.impl.standard.context;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import net.sourceforge.javautil.cdi.IBeanContainer;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class CreationalContextImpl<T> implements CreationalContext<T> {

    protected final IBeanContainer container;

    protected final Contextual<T> contextual;

    public CreationalContextImpl(IBeanContainer container, Contextual<T> contextual) {
        this.container = container;
        this.contextual = contextual;
    }

    public void push(T incompleteInstance) {
    }

    public void release() {
    }
}
