package net.sourceforge.javautil.dependency.impl.standard;

import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.javautil.common.logging.ILogger;
import net.sourceforge.javautil.common.logging.LoggingContext;
import net.sourceforge.javautil.dependency.IDependancyRelationship;
import net.sourceforge.javautil.dependency.IDependancyRelationship.DependentStartStrategy;
import net.sourceforge.javautil.dependency.DependencyException;
import net.sourceforge.javautil.dependency.IDependencyManager;
import net.sourceforge.javautil.dependency.IManaged;
import net.sourceforge.javautil.lifecycle.ILifecycle;
import net.sourceforge.javautil.lifecycle.ILifecycleListener;
import net.sourceforge.javautil.lifecycle.LifecycleEvent;

/**
 * Base for most dependency managers.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public abstract class StandardManagerAbstract<M extends IManaged<?>, R extends IDependancyRelationship<M>> implements IDependencyManager<M, R>, ILifecycleListener {

    protected final ILogger log = LoggingContext.getContextualLogger(getClass());

    protected final Map<M, R> relationships = new HashMap<M, R>();

    protected final Map<M, Set<R>> reverseIndex = new HashMap<M, Set<R>>();

    @Override
    public R getDependencies(M dependent) {
        return relationships.get(dependent);
    }

    @Override
    public Set<R> getDependents(M dependency) {
        if (!reverseIndex.containsKey(dependency)) return Collections.EMPTY_SET;
        return Collections.unmodifiableSet(reverseIndex.get(dependency));
    }

    @Override
    public void relate(M dependent, M dependency, DependentStartStrategy strategy) {
        R relationship = relationships.get(dependent);
        if (relationship == null) {
            relationships.put(dependent, relationship = this.createRelationship(dependent, strategy));
        }
        this.addDependency(relationship, dependency);
        Set<R> lookup = this.reverseIndex.get(dependency);
        if (lookup == null) this.reverseIndex.put(dependency, lookup = new LinkedHashSet<R>());
        lookup.add(relationship);
    }

    @Override
    public void before(LifecycleEvent event) {
        switch(event.getPhase()) {
            case PRESTART:
                R dependencies = this.getDependencies((M) event.getSource());
                for (M dependency : dependencies.getDependencies()) {
                    switch(dependency.getLifecycle().getCurrentPhase()) {
                        case CREATE:
                        case INITIALIZE:
                        case PRESTART:
                        case STOP:
                            dependency.getLifecycle().start();
                            break;
                        case POSTSTOP:
                        case DESTROY:
                            throw new DependencyException("Dependency Failure: has been permanently destroyed: " + dependency);
                        default:
                    }
                }
                break;
            case STOP:
                Set<R> dependents = this.getDependents((M) event.getSource());
                for (R dependent : dependents) {
                    switch(dependent.getDependant().getLifecycle().getCurrentPhase()) {
                        case START:
                            dependent.getDependant().getLifecycle().stop();
                        default:
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void after(LifecycleEvent event) {
        switch(event.getPhase()) {
            case START:
                Set<R> dependents = this.getDependents((M) event.getSource());
                for (R dependent : dependents) {
                    if (dependent.getDependentStartStrategy() == DependentStartStrategy.Manual) continue;
                    switch(dependent.getDependant().getLifecycle().getCurrentPhase()) {
                        case DESTROY:
                        case POSTSTOP:
                        case START:
                            break;
                        default:
                            dependent.getDependant().getLifecycle().start();
                    }
                }
        }
    }

    @Override
    public void failure(LifecycleEvent event) {
    }

    @Override
    public boolean listensFor(LifecycleEvent event) {
        return true;
    }

    @Override
    public void handle(EventObject event) {
        if (event instanceof LifecycleEvent) {
            switch(((LifecycleEvent) event).getTransitionType()) {
                case After:
                    this.after((LifecycleEvent) event);
                    break;
                case Before:
                    this.before((LifecycleEvent) event);
                    break;
                case Failure:
                    this.failure((LifecycleEvent) event);
                    break;
            }
        }
    }

    /**
	 * This must provide the relationship implementation used by this manager.
	 */
    protected abstract R createRelationship(M dependent, DependentStartStrategy strategy);

    /**
	 * @param relationship The relationship whose dependency list must be updated
	 * @param dependency The dependency that should be added to the relationship
	 */
    protected abstract void addDependency(R relationship, M dependency);
}
