package org.tm4j.topicmap.tmdm;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import org.tm4j.topicmap.tmdm.util.ListenerDispatcher;

/**
	@author <a href="mailto:xuan--2007.05--org.tm4j.topicmap.tmdm--tm4j.org@public.software.baldauf.org">Xuân Baldauf</a>
*/
public class TopicMapEventListenerUtil {

    protected static InvocationHandler nullInvocationHandler = new InvocationHandler() {

        public Object invoke(Object proxy, Method method, Object[] args) {
            return null;
        }
    };

    protected static TopicMapEventListener nullTopicMapEventListener = (TopicMapEventListener) Proxy.newProxyInstance(TopicMapEventListenerUtil.class.getClassLoader(), new Class<?>[] { TopicMapEventListener.class }, nullInvocationHandler);

    @SuppressWarnings("unchecked")
    public static <TM extends ReadableTopicMap, T extends ReadableTopic, A extends ReadableAssociation, O extends ReadableOccurrence, TMC extends ReadableTopicMapConstruct, TN extends ReadableTopicName, V extends ReadableVariant, AR extends ReadableAssociationRole> TopicMapEventListener<TM, T, A, O, TMC, TN, V, AR> getNullTopicMapEventListener() {
        return (TopicMapEventListener<TM, T, A, O, TMC, TN, V, AR>) nullTopicMapEventListener;
    }

    public static ListenerDispatcher<TopicMapEventListener> createTopicMapEventListenerDispatcher(Collection<TopicMapEventListener> clientListeners) {
        return new ListenerDispatcher<TopicMapEventListener>(TopicMapEventListener.class, clientListeners);
    }
}
