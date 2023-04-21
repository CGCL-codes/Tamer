package org.ops4j.peaberry.activation.invocations.internal;

import static com.google.inject.matcher.Matchers.*;
import static org.ops4j.peaberry.Peaberry.*;
import java.lang.reflect.Method;
import org.ops4j.peaberry.activation.invocations.InvocationTracker;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;

/**
 * A module that can be mixed into an {@link com.google.inject.Injector} to
 * capture certain method invocations and store them in an {@link InvocationTracker}
 * service.
 * 
 * @author rinsvind@gmail.com (Todor Boev)
 */
public final class InvocationTrackerModule extends AbstractModule {

    private final Matcher<? super Class<?>> types;

    private final Matcher<? super Method> methods;

    public InvocationTrackerModule(Matcher<? super Class<?>> types) {
        this(types, any());
    }

    public InvocationTrackerModule(Matcher<? super Class<?>> types, Matcher<? super Method> methods) {
        this.types = types;
        this.methods = methods;
    }

    @Override
    protected void configure() {
        final LoggingInterceptor log = new LoggingInterceptor();
        bindInterceptor(types, methods, log);
        requestInjection(log);
        bind(InvocationTracker.class).toProvider(service(InvocationTracker.class).single());
    }
}
