package net.sourceforge.freejava.util.make;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.sourceforge.freejava.util.exception.NotImplementedException;

public abstract class AbstractMakeTarget implements IMakeTarget {

    private final Collection<? extends IMakeTarget> dependencies;

    public AbstractMakeTarget() {
        this.dependencies = Collections.emptyList();
    }

    public AbstractMakeTarget(IMakeTarget... dependencies) {
        if (dependencies == null) throw new NullPointerException("dependencies");
        List<IMakeTarget> list = new ArrayList<IMakeTarget>(dependencies.length);
        for (int i = 0; i < dependencies.length; i++) list.add(dependencies[i]);
        this.dependencies = list;
    }

    public AbstractMakeTarget(Collection<? extends IMakeTarget> dependencies) {
        if (dependencies == null) throw new NullPointerException("dependencies");
        this.dependencies = dependencies;
    }

    @Override
    public Collection<? extends IMakeTarget> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean isUpdated() {
        long version = getVersion();
        for (IMakeTarget dependency : getDependencies()) {
            if (!dependency.isUpdated()) return false;
            long dependencyVersion = dependency.getVersion();
            if (dependencyVersion > version) return false;
        }
        return true;
    }

    @Override
    public void update() throws Exception {
        if (!isUpdated()) make();
    }

    @Override
    public void make() throws Exception {
        throw new NotImplementedException("Don't know how to make this target: " + getName());
    }
}
