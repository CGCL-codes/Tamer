package org.bing.zion.core;

public class SimpleDispatcher implements MessageDispatcher {

    public void dispatch(Session session, Object msg) {
        MessageHandlerChain handlerChain = session.getHandlerChain();
        handlerChain.first().handle(handlerChain, session, msg);
    }
}
