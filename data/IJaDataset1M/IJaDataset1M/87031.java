package org.nakedobjects.applib.fixtures;

import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;

public class FixtureFactoryAndRepository extends AbstractFactoryAndRepository {

    public <T> List<T> allInstances(Class<T> cls, boolean includeSubclasses) {
        return super.allInstances(cls, includeSubclasses);
    }

    public <T> List<T> allMatches(Class<T> cls, Filter filter, boolean includeSubclasses) {
        return super.allMatches(cls, filter, includeSubclasses);
    }

    public <T> List<T> allMatches(Class<T> cls, Object pattern, boolean includeSubclasses) {
        return super.allMatches(cls, pattern, includeSubclasses);
    }

    public <T> List<T> allMatches(Class<T> cls, String title, boolean includeSubclasses) {
        return super.allMatches(cls, title, includeSubclasses);
    }

    public <T> T firstMatch(Class<T> cls, Filter filter, boolean includeSubclasses) {
        return super.firstMatch(cls, filter, includeSubclasses);
    }

    public <T> T firstMatch(Class<T> cls, Object pattern, boolean includeSubclasses) {
        return super.firstMatch(cls, pattern, includeSubclasses);
    }

    public <T> T firstMatch(Class<T> cls, String title, boolean includeSubclasses) {
        return super.firstMatch(cls, title, includeSubclasses);
    }

    public void makePersistent(Object transientObject) {
        super.makePersistent(transientObject);
    }

    public <T> T newInstance(Class<T> ofClass, Object sameStateAs) {
        return super.newInstance(ofClass, sameStateAs);
    }

    public <T> T newPersistentInstance(Class<T> ofClass) {
        return super.newPersistentInstance(ofClass);
    }

    public <T> T newTransientInstance(Class<T> ofClass) {
        return super.newTransientInstance(ofClass);
    }

    public <T> T uniqueMatch(Class<T> cls, Filter filter, boolean includeSubclasses) {
        return super.uniqueMatch(cls, filter, includeSubclasses);
    }

    public <T> T uniqueMatch(Class<T> cls, String title, boolean includeSubclasses) {
        return super.uniqueMatch(cls, title, includeSubclasses);
    }
}
