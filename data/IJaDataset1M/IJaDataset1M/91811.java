package ru.adv.db.handler.wrapper;

import ru.adv.db.handler.HandlerI;

/**
 * 
 * Factory for {@link GroovyHandlerWrapper},
 *  
 * @author vic
 *
 */
public class GroovyHandlerWrapperFactory extends HandlerWrapperFactory {

    @Override
    public HandlerWrapper getHandlerWrapper() throws Exception {
        return new GroovyHandlerWrapper(new LazyProxyHandler() {

            @Override
            public HandlerI createHandler() {
                return getRepository().getHandler(getDatabaseName());
            }

            @Override
            public void destroy() {
                if (this.handler != null) {
                    this.handler.destroy();
                }
            }
        }, getSecurityOptions());
    }
}
