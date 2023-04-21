package net.butfly.bus.server.invoker;

/**
 * Invoker（具体工厂）接口
 * 
 */
public interface InvokerProvider {

    void init();

    Invoker createInvoker();
}
