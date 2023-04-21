package org.jomc.ri.model;

import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import org.jomc.model.Dependencies;
import org.jomc.model.Dependency;
import static org.jomc.ri.model.RuntimeModelObjects.createMap;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
public class RuntimeDependencies extends Dependencies implements RuntimeModelObject {

    /** Cache map. */
    @XmlTransient
    private final transient Map<String, Dependency> dependenciesByNameCache = createMap();

    /**
     * Creates a new {@code RuntimeDependencies} instance by deeply copying a given {@code Dependencies} instance.
     *
     * @param dependencies The instance to copy.
     *
     * @throws NullPointerException if {@code dependencies} is {@code null}.
     */
    public RuntimeDependencies(final Dependencies dependencies) {
        super(dependencies);
        if (this.getAuthors() != null) {
            this.setAuthors(RuntimeModelObjects.getInstance().copyOf(this.getAuthors()));
        }
        if (this.getDocumentation() != null) {
            this.setDocumentation(RuntimeModelObjects.getInstance().copyOf(this.getDocumentation()));
        }
        this.copyDependencies();
    }

    /**
     * Gets a dependency for a given name from the list of dependencies.
     * <p>This method queries an internal cache for a result object to return for the given argument values. If no
     * cached result object is available, this method queries the super-class for a result object to return and caches
     * the outcome of that query for use on successive calls.</p>
     * <p><b>Note:</b><br/>Method {@code clear()} must be used to synchronize the state of the internal cache with the
     * state of the instance, should the state of the instance change.</p>
     *
     * @param name The name of the dependency to return.
     *
     * @return The first matching dependency or {@code null}, if no such dependency is found.
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     *
     * @see #getDependency()
     * @see Dependency#getName()
     * @see #clear()
     */
    @Override
    public Dependency getDependency(final String name) {
        if (name == null) {
            throw new NullPointerException("identifier");
        }
        synchronized (this.dependenciesByNameCache) {
            Dependency d = this.dependenciesByNameCache.get(name);
            if (d == null && !this.dependenciesByNameCache.containsKey(name)) {
                d = super.getDependency(name);
                this.dependenciesByNameCache.put(name, d);
            }
            return d;
        }
    }

    private void copyDependencies() {
        for (int i = 0, s0 = this.getDependency().size(); i < s0; i++) {
            final Dependency d = this.getDependency().get(i);
            this.getDependency().set(i, RuntimeModelObjects.getInstance().copyOf(d));
        }
    }

    public void gc() {
        this.gcOrClear(true, false);
    }

    public void clear() {
        synchronized (this.dependenciesByNameCache) {
            this.dependenciesByNameCache.clear();
        }
        this.gcOrClear(false, true);
    }

    private void gcOrClear(final boolean gc, final boolean clear) {
        if (this.getAuthors() instanceof RuntimeModelObject) {
            if (gc) {
                ((RuntimeModelObject) this.getAuthors()).gc();
            }
            if (clear) {
                ((RuntimeModelObject) this.getAuthors()).clear();
            }
        }
        if (this.getDocumentation() instanceof RuntimeModelObject) {
            if (gc) {
                ((RuntimeModelObject) this.getDocumentation()).gc();
            }
            if (clear) {
                ((RuntimeModelObject) this.getDocumentation()).clear();
            }
        }
        this.gcOrClearDependencies(gc, clear);
    }

    private void gcOrClearDependencies(final boolean gc, final boolean clear) {
        for (int i = 0, s0 = this.getDependency().size(); i < s0; i++) {
            final Dependency d = this.getDependency().get(i);
            if (d instanceof RuntimeModelObject) {
                if (gc) {
                    ((RuntimeModelObject) d).gc();
                }
                if (clear) {
                    ((RuntimeModelObject) d).clear();
                }
            }
        }
    }

    /** Creates a new {@code RuntimeDependencies} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 2.0-SNAPSHOT", comments = "See http://jomc.sourceforge.net/jomc/2.0/jomc-tools-2.0-SNAPSHOT")
    public RuntimeDependencies() {
        super();
    }
}
