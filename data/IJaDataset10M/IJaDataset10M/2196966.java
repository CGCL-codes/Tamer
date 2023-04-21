package net.sourceforge.jfunctions.patterns;

import static net.sourceforge.jfunctions.reflect.ReflectionToolkit.copy;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Map.Entry;
import net.sourceforge.jfunctions.lang.NullArgumentException;

public class MixedBase<Mixin> implements Mixed<Mixin> {

    private MixinMap<Mixin> features = null;

    public MixedBase() {
    }

    public MixedBase(MixedBase<Mixin> source) {
        mergeFeatures(source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Mixin> T as(Class<T> type) {
        if (type == null) throw new NullArgumentException("type");
        if (type.isInstance(this)) return (T) this;
        checkFeatures();
        return (T) features.get(type);
    }

    private void checkFeatures() {
        if (features == null) {
            features = new MixinMap<Mixin>();
        }
    }

    protected Class<?> getDecoratedClass() {
        return this.getClass();
    }

    protected <T> T createMixinInstance(Class<T> superType) throws InstantiationException, IllegalAccessException {
        try {
            Constructor<T> ctor = superType.getConstructor(getDecoratedClass());
            return ctor.newInstance(this);
        } catch (Exception e) {
        }
        return superType.newInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Mixin> T extend(Class<T> featureType) throws IllegalArgumentException {
        if (featureType == null) throw new NullArgumentException("featureType");
        if (featureType.isInstance(this)) return (T) this;
        try {
            T instance = createMixinInstance(featureType);
            checkFeatures();
            features.put(featureType, instance);
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T extends Mixin> T extend(Class<T> featureType, Class<? extends T> implType) throws IllegalArgumentException {
        if (featureType == null) throw new NullArgumentException("featureType");
        if (implType == null) throw new NullArgumentException("implType");
        if (featureType.isInstance(this)) {
            throw new IllegalArgumentException("This class is already implements " + featureType.getName());
        }
        try {
            T instance = createMixinInstance(implType);
            checkFeatures();
            features.put(featureType, instance);
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T extends Mixin> T extend(Class<T> type, T impl) {
        if (type == null) throw new NullArgumentException("type");
        if (impl == null) throw new NullArgumentException("impl");
        if (type.isInstance(this)) {
            throw new IllegalArgumentException("This class is already implements " + type.getName());
        }
        checkFeatures();
        features.put(type, impl);
        return impl;
    }

    protected void mergeFeatures(MixedBase<Mixin> base) {
        if ((base.features == null) || (base.features.size() == 0)) return;
        checkFeatures();
        for (Entry<Class<? extends Mixin>, Mixin> entry : base.features.entrySet()) {
            Class<? extends Mixin> extensionType = entry.getKey();
            if (features.get(extensionType) == null) {
                features.put(extensionType, copy(entry.getValue()));
            }
        }
    }

    @Override
    public boolean hasFeaturesOf(Class<? extends Mixin> type) {
        checkFeatures();
        return type.isInstance(this) || features.containsKey(type);
    }

    @Override
    public Map<Class<? extends Mixin>, Mixin> features() {
        checkFeatures();
        return features.map();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof MixedBase)) return false;
        MixedBase other = (MixedBase) obj;
        if (features == null) {
            if (other.features != null) return false;
        } else if (!features.equals(other.features)) return false;
        return true;
    }
}
