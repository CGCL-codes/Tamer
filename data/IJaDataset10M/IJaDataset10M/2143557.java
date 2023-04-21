package net.sf.joafip.entity.rel400;

import net.sf.joafip.StorableClass;
import net.sf.joafip.store.service.proxy.IInstanceFactory;

/**
 * 
 * @author luc peuvrier
 * 
 */
@StorableClass
public class BobASWithTransientCaller {

    private final BobASWithTransient delegate;

    public BobASWithTransientCaller(final IInstanceFactory instanceFactory, final boolean delegateIsProxy) {
        super();
        if (delegateIsProxy) {
            delegate = BobASWithTransient.newInstance(instanceFactory);
        } else {
            delegate = new BobASWithTransient(instanceFactory);
        }
    }

    public static BobASWithTransientCaller newInstance(final IInstanceFactory instanceFactory, final boolean delegateIsProxy) {
        final BobASWithTransientCaller newInstance;
        if (instanceFactory == null) {
            newInstance = new BobASWithTransientCaller(instanceFactory, delegateIsProxy);
        } else {
            newInstance = (BobASWithTransientCaller) instanceFactory.newInstance(BobASWithTransientCaller.class, new Class[] { IInstanceFactory.class, boolean.class }, new Object[] { instanceFactory, delegateIsProxy });
        }
        return newInstance;
    }

    public void action() {
        delegate.action();
    }
}
