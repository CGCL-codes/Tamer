package com.mycila.event.spi;

import com.mycila.event.api.Event;
import com.mycila.event.api.Reachability;
import com.mycila.event.api.Referencable;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.TopicMatcher;
import com.mycila.event.api.annotation.Reference;
import net.sf.cglib.reflect.FastMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Subscriptions {

    private Subscriptions() {
    }

    static Subscriber createSubscriber(Object instance, Method method) {
        return new MethodSubscriber(instance, method);
    }

    static <E, S> Subscription create(final TopicMatcher matcher, final Class<E> eventType, final S subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        return new Subscription<E, S>() {

            final Reachability reachability = subscriber instanceof Referencable ? ((Referencable) subscriber).getReachability() : Reachability.of(subscriber.getClass());

            @Override
            public TopicMatcher getTopicMatcher() {
                return matcher;
            }

            @Override
            public Class<E> getEventType() {
                return eventType;
            }

            @Override
            public S getSubscriber() {
                return subscriber;
            }

            @Override
            public Reachability getReachability() {
                return reachability;
            }
        };
    }

    private static class ReferencableMethod implements Referencable {

        final Reachability reachability;

        final Object target;

        final Invokable invokable;

        ReferencableMethod(Object target, final Method method) {
            notNull(target, "Target object");
            notNull(method, "Method");
            this.target = target;
            this.reachability = method.isAnnotationPresent(Reference.class) ? method.getAnnotation(Reference.class).value() : Reachability.of(target.getClass());
            notNull(reachability, "Value of @Reference on method " + method);
            this.invokable = Modifier.isPrivate(method.getModifiers()) ? new Invokable() {

                {
                    if (!method.isAccessible()) method.setAccessible(true);
                }

                @Override
                public void invoke(Object target, Object... args) throws Exception {
                    try {
                        method.invoke(target, args);
                    } catch (InvocationTargetException e) {
                        Throwable t = e.getTargetException();
                        if (t instanceof Error) throw (Error) t;
                        if (t instanceof Exception) throw (Exception) t;
                        RuntimeException re = new RuntimeException(t.getMessage(), t.getCause());
                        re.setStackTrace(t.getStackTrace());
                        throw re;
                    }
                }
            } : new Invokable() {

                final FastMethod m = Proxy.fastMethod(method);

                @Override
                public void invoke(Object target, Object... args) throws Exception {
                    try {
                        m.invoke(target, args);
                    } catch (InvocationTargetException e) {
                        Throwable t = e.getTargetException();
                        if (t instanceof Error) throw (Error) t;
                        if (t instanceof Exception) throw (Exception) t;
                        RuntimeException re = new RuntimeException(t.getMessage(), t.getCause());
                        re.setStackTrace(t.getStackTrace());
                        throw re;
                    }
                }
            };
        }

        @Override
        public final Reachability getReachability() {
            return reachability;
        }

        protected final void invoke(Object... args) throws Exception {
            invokable.invoke(target, args);
        }
    }

    private static final class MethodSubscriber<E> extends ReferencableMethod implements Subscriber<E> {

        MethodSubscriber(Object target, Method method) {
            super(target, method);
            hasOneArg(Event.class, method);
        }

        @Override
        public void onEvent(Event<E> event) throws Exception {
            invoke(event);
        }
    }

    private static interface Invokable {

        void invoke(Object target, Object... args) throws Exception;
    }
}
