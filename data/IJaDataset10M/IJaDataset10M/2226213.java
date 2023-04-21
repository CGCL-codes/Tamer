package org.netxilia.api.impl.model;

import java.util.concurrent.Callable;
import org.netxilia.api.impl.user.ISpringUserService;
import org.netxilia.api.impl.user.NetxiliaSecurityContext;

/**
 * This class takes the current user in the local thread when the command was sent and resets it when the command is
 * executed.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class CallableWithUser<V> implements Callable<V> {

    private final NetxiliaSecurityContext securityContext;

    private final Callable<V> delegate;

    public CallableWithUser(ISpringUserService userService, Callable<V> delegate) {
        this.securityContext = new NetxiliaSecurityContext(userService);
        this.delegate = delegate;
    }

    @Override
    public V call() throws Exception {
        try {
            securityContext.set();
            return delegate.call();
        } finally {
            securityContext.restore();
        }
    }
}
