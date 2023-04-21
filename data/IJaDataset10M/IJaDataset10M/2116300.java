package com.mycila.testing.plugin.atunit.container;

import atunit.core.Container;
import atunit.lib.com.google.common.collect.Iterables;
import atunit.lib.com.google.common.collect.Multimap;
import atunit.lib.com.google.common.collect.Multimaps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.mycila.testing.core.Mycila;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class GuiceContainer implements Container {

    public Object createTest(Class<?> testClass, Map<Field, Object> fieldValues) throws Exception {
        FieldModule fields = new FieldModule(fieldValues);
        Injector injector;
        if (Module.class.isAssignableFrom(testClass)) {
            injector = Guice.createInjector(fields, (Module) testClass.newInstance());
        } else {
            injector = Guice.createInjector(fields);
        }
        injector.injectMembers(Mycila.currentExecution().context().introspector().instance());
        return null;
    }

    protected class FieldModule extends AbstractModule {

        final Map<Field, Object> fields;

        public FieldModule(Map<Field, Object> fields) {
            this.fields = fields;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void configure() {
            Multimap<Type, Field> fieldsByType = Multimaps.newHashMultimap();
            for (Field field : fields.keySet()) {
                fieldsByType.put(field.getGenericType(), field);
            }
            for (Type type : fieldsByType.keySet()) {
                Collection<Field> fields = fieldsByType.get(type);
                if (fields.size() == 1) {
                    Field field = Iterables.getOnlyElement(fields);
                    TypeLiteral literal = TypeLiteral.get(type);
                    bind(literal).toInstance(this.fields.get(field));
                }
            }
        }
    }
}
