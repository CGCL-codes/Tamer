package net.sf.joafip.entity.rel400;

import net.sf.joafip.AssertNotNull;
import net.sf.joafip.StorableClass;
import net.sf.joafip.store.service.proxy.IInstanceFactory;

/**
 * 
 * @author luc peuvrier
 * 
 */
@StorableClass
public class BobASDelegatingListenDelegate {

    @AssertNotNull
    private final BobAsDelegateNotifyDelegating delegate;

    private final boolean constructed;

    public BobASDelegatingListenDelegate(final IInstanceFactory instanceFactory) {
        super();
        delegate = BobAsDelegateNotifyDelegating.newInstance(instanceFactory, this);
        if (delegate == null) {
            throw new IllegalStateException("delegate must not be null");
        }
        initialize();
        constructed = true;
    }

    public boolean isConstructed() {
        return constructed;
    }

    public static BobASDelegatingListenDelegate newInstance(final IInstanceFactory instanceFactory) {
        final BobASDelegatingListenDelegate newInstance;
        if (instanceFactory == null) {
            newInstance = new BobASDelegatingListenDelegate(instanceFactory);
        } else {
            newInstance = (BobASDelegatingListenDelegate) instanceFactory.newInstance(BobASDelegatingListenDelegate.class, new Class[] { IInstanceFactory.class }, new Object[] { instanceFactory });
        }
        return newInstance;
    }

    public void initialize() {
    }

    public void action() {
        delegate.action();
    }

    public void delegateNotification() {
    }

    public BobAsDelegateNotifyDelegating getDelegate() {
        return delegate;
    }
}
