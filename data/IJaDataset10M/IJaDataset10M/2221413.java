package com.mycila.jmx.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.util.LinkedList;
import java.util.Queue;
import static com.google.inject.matcher.Matchers.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaEventGuiceModule implements Module {

    protected final Processor processor = new Processor() {

        private final Queue<Object> references = new LinkedList<Object>();

        private volatile boolean injectedCreated;

        public <I> void process(I instance) {
            if (injectedCreated) ; else references.offer(instance);
        }

        public void start() {
            injectedCreated = true;
            while (!references.isEmpty()) {
                Object o = references.poll();
                if (o != null) ;
            }
        }
    };

    public void afterCreation() {
        processor.start();
    }

    public void configure(Binder binder) {
        binder.bind(MycilaEventGuiceModule.class).toInstance(this);
        binder.bindListener(any(), new TypeListener() {

            public <I> void hear(TypeLiteral<I> type, final TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {

                    public void afterInjection(I injectee) {
                        processor.process(injectee);
                    }
                });
            }
        });
    }

    private static interface Processor {

        <I> void process(I instance);

        void start();
    }
}
