package net.sf.extcos.internal;

import java.lang.reflect.Modifier;
import net.sf.extcos.exception.ConcurrentInspectionException;
import net.sf.extcos.filter.ResourceMatcher;
import net.sf.extcos.resource.Resource;
import net.sf.extcos.util.Assert;

public class ExtendingResourceMatcher implements ResourceMatcher {

    private final Class<?> clazz;

    public ExtendingResourceMatcher(final Class<?> clazz) {
        Assert.notNull(clazz, IllegalArgumentException.class);
        Assert.isFalse(clazz.isInterface(), IllegalArgumentException.class);
        Assert.isFalse(Modifier.isFinal(clazz.getModifiers()), IllegalArgumentException.class);
        this.clazz = clazz;
    }

    @Override
    public boolean matches(final Resource resource) throws ConcurrentInspectionException {
        Assert.notNull(resource, IllegalArgumentException.class);
        return resource.isSubclassOf(clazz);
    }

    @Override
    public boolean isMatcherFor(final Object obj) {
        return clazz.equals(obj);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (clazz == null ? 0 : clazz.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExtendingResourceMatcher other = (ExtendingResourceMatcher) obj;
        if (clazz == null) {
            if (other.clazz != null) {
                return false;
            }
        } else if (!clazz.equals(other.clazz)) {
            return false;
        }
        return true;
    }
}
