package org.magicdroid.commons;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.magicdroid.commons.ReflectionIterator.ReflectionCallback;
import org.magicdroid.commons.Structures.Pair;

public class Injector {

    public interface Provider<T> {

        T provide();
    }

    public interface Scope {

        public abstract <T> T lookup(String type);

        public abstract <T> void store(String type, T instance);
    }

    public static class AppScope implements Scope {

        private static Map<String, Object> registry = new HashMap<String, Object>();

        @Override
        public <T> T lookup(String type) {
            return (T) this.registry.get(type);
        }

        public <T> void store(String type, T instance) {
            this.registry.put(type, instance);
        }
    }

    public static class DefaultScope implements Scope {

        @Override
        public <T> T lookup(String type) {
            return null;
        }

        public <T> void store(String type, T instance) {
        }

        ;
    }

    public interface Scopes {

        Scope DEFAULT = new DefaultScope();

        Scope APPLICATION = new AppScope();
    }

    private final class SpecForClass<T> implements Spec<T> {

        private final Class<? extends T> impl;

        private SpecForClass(Class<? extends T> impl) {
            this.impl = impl;
        }

        @Override
        public T create() {
            try {
                for (Constructor c : this.impl.getConstructors()) if (c.getAnnotation(Inject.class) != null) {
                    c.setAccessible(true);
                    return (T) c.newInstance(lookupParams(c.getParameterTypes(), c.getParameterAnnotations()));
                }
                return this.impl.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException("Error creating: " + this.impl + ". Forgot to use @Inject annotation in constructor?", e);
            }
        }
    }

    private interface Spec<T> {

        T create();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.CONSTRUCTOR })
    public @interface Inject {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PARAMETER })
    public @interface BoundAs {

        String value();
    }

    private final Map<String, Object> registryInstances = new HashMap<String, Object>();

    private final Map<String, Pair<Spec<?>, Scope>> registry = new HashMap<String, Pair<Spec<?>, Scope>>();

    public Injector() {
        this.bindInstance(Injector.class, this);
    }

    public <T> Injector bindInstance(Class<? super T> clazz, T object) {
        this.bindInstance(clazz.getName(), object);
        return this;
    }

    public <T> Injector bindInstance(String key, T object) {
        this.registryInstances.put(key, object);
        return this;
    }

    public <T> Injector bind(final Class<T> clazz, Class<? extends T> object) {
        return this.bind(clazz, object, Scopes.DEFAULT);
    }

    public <T> Injector bind(final Class<T> clazz, Class<? extends T> object, Scope scope) {
        return this.bind(clazz.getName(), object, scope);
    }

    public <T> Injector bind(final String key, Class<? extends T> object, Scope scope) {
        this.registry.put(key, new Pair(new SpecForClass<T>(object), scope));
        return this;
    }

    public <T> Injector bind(final Class<T> type, final Class<?>[] mixins) {
        return this.bind(type, mixins, Scopes.DEFAULT);
    }

    public <T> Injector bind(final Class<T> type, final Class<?>[] mixins, Scope scope) {
        return this.bind(type, type.getName(), mixins, scope);
    }

    public <T> Injector bind(final Class<T> type, final String key, final Class<?>[] mixins, Scope scope) {
        this.registry.put(key, new Pair(new Spec<T>() {

            @Override
            public T create() {
                return MagicObject.create(type, lookupParams(mixins, new Annotation[][] {}));
            }
        }, scope));
        return this;
    }

    public <T> T create(Class<T> type) {
        return (T) this.create(type.getName(), type);
    }

    private <T> T create(String key, Class<T> type) {
        if (this.registryInstances.containsKey(key)) return (T) this.registryInstances.get(key);
        if (this.registry.containsKey(key)) {
            Pair<Spec<?>, Scope> pair = this.registry.get(key);
            T result = pair.second.lookup(key);
            if (result != null) return result;
            result = (T) pair.first.create();
            pair.second.store(key, result);
            return result;
        }
        return (T) new SpecForClass(type).create();
    }

    private Object[] lookupParams(Class[] parameterTypes, Annotation[][] annotations) {
        List<Object> result = new ArrayList<Object>();
        int index = 0;
        for (Class type : parameterTypes) {
            result.add(this.<Object>create(extractKeyFromAnnotations(type, annotations, index), type));
            index++;
        }
        return result.toArray();
    }

    private String extractKeyFromAnnotations(Class type, Annotation[][] annotations, int index) {
        if (index < annotations.length) for (Annotation a : annotations[index]) if (a.annotationType() == BoundAs.class) return ((BoundAs) a).value();
        return type.getName();
    }

    public void inject(final Object target) {
        new ReflectionIterator<Field>(ReflectionIterator.FIELDS).iterateOn(target, new ReflectionCallback<Field>() {

            @Override
            public void process(Object object, Field member) {
                if (member.getAnnotation(Inject.class) == null) return;
                try {
                    member.setAccessible(true);
                    member.set(target, create(member.getType()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
