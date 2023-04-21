package net.sf.jimo.platform.event;

import java.util.concurrent.Callable;

public interface EventDispatcher {

    public <T> T syncDispatch(Callable<T> callable) throws Exception;

    public <T> void asyncDispatch(Callable<T> callable);
}
