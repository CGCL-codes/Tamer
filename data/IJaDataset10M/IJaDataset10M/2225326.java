package com.mycila.jmx.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.jmx.export.JmxExporter;
import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaJmx {

    private MycilaJmx() {
    }

    public static ExporterBuilder exportJmxBeans(Binder binder) {
        return new ExporterBuilder(binder);
    }

    public static final class ExporterBuilder {

        Key<? extends JmxExporter> key = Key.get(JmxExporter.class);

        private ExporterBuilder(Binder binder) {
            binder.bindListener(Matchers.any(), new TypeListener() {

                @Override
                public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                    encounter.register(new InjectionListener<I>() {

                        @Override
                        public void afterInjection(I injectee) {
                        }
                    });
                }
            });
        }

        public void using(Class<? extends JmxExporter> type) {
            this.key = Key.get(type);
        }

        public void using(Class<? extends JmxExporter> type, Class<? extends Annotation> annotationType) {
            this.key = Key.get(type, annotationType);
        }

        public void using(Class<? extends JmxExporter> type, Annotation annotation) {
            this.key = Key.get(type, annotation);
        }
    }
}
