package org.az.macaroni;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.az.macaroni.impl.ApplicationScope;
import org.az.macaroni.impl.ThreadLocalScope;
import org.az.macaroni.spring.PipeAware;
import org.az.macaroni.sync.SyncPipeFactory;

/**
 * @author Artem Zaborsky (compartia@gmail.com)
 */
public class MacaroniContext {

    private static Logger logger = Logger.getLogger(MacaroniContext.class);

    private static Map<String, PipeScope> scopes;

    static {
        scopes = new HashMap<String, PipeScope>();
        scopes.put(PipeScope.SCOPE_THREAD_LOCAL, ThreadLocalScope.instance());
        scopes.put(PipeScope.SCOPE_APPLICATION, ApplicationScope.instance());
    }

    private MacaroniContext() {
    }

    public static PipeScope getScope(String scopeName) {
        return scopes.get(scopeName);
    }

    public static PipeFactory getFactory(PipeScope scope) {
        return SyncPipeFactory.instance();
    }

    public static void wire(Object bean) {
        PipeAware a = bean.getClass().getAnnotation(PipeAware.class);
        if (a != null) {
            logger.info(bean.getClass().getName() + " is aware of pipes:" + toString(a.value()));
            PipeScope scope = getScope(a.scope());
            if (a.scope() == null) {
                throw new IllegalArgumentException(a.scope() + " is unknown scope");
            }
            PipeFactory factory = getFactory(scope);
            if (factory == null) {
                throw new IllegalStateException("no factory was defined for " + a.scope());
            }
            for (String pipeName : a.value()) {
                EventPipe pipe = factory.getPipe(pipeName, a.scope());
                pipe.addSubscriber(bean);
            }
        }
    }

    private static final String toString(String[] ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append("{");
            sb.append(s);
            sb.append("} ");
        }
        return sb.toString();
    }
}
