package org.iqual.chaplin.composite;

import org.iqual.chaplin.msg.MessageReceiver;
import org.iqual.chaplin.msg.Message;
import org.iqual.chaplin.msg.MessageReplies;
import org.iqual.chaplin.msg.MessageReceiversChain;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

/**
 * @author Zbynek Slajchrt
 * @since Jul 21, 2009 8:19:01 PM
 */
public class CompositeObjectProxy implements InvocationHandler, MessageReceiver {

    private Object composite;

    private CompositeContextHandler compositeContextHandler;

    public static <T> T createProxy(Class type, CompositeContextHandler composite) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { type, MessageReceiver.class }, new CompositeObjectProxy(composite));
    }

    private CompositeObjectProxy(CompositeContextHandler compositeContextHandler) {
        this.compositeContextHandler = compositeContextHandler;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("$$$$$$")) {
            if (args == null || args.length == 0) {
                return composite;
            } else if (args.length == 1) {
                composite = args[0];
                return null;
            }
        }
        if (method.getName().equals("onMessageReceived")) {
            return method.invoke(this, args);
        }
        return compositeContextHandler.invoke(proxy, method, args);
    }

    public void onMessageReceived(Message message, MessageReplies replies, MessageReceiversChain chain) throws Throwable {
        chain.process(message, replies);
    }
}
