package uk.ac.ebi.intact.core.persister.stats;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.intact.model.AnnotatedObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Gathers statistics during saveOrUpdate by the CorePersister
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: PersisterStatistics.java 13197 2009-06-01 13:54:39Z baranda $
 */
public class PersisterStatistics implements Serializable {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private Multimap<Class, StatsUnit> persistedMap;

    private Multimap<Class, StatsUnit> mergedMap;

    private Multimap<Class, StatsUnit> duplicatesMap;

    private Multimap<Class, StatsUnit> transientMap;

    public PersisterStatistics() {
        this.persistedMap = new HashMultimap<Class, StatsUnit>();
        this.mergedMap = new HashMultimap<Class, StatsUnit>();
        this.duplicatesMap = new HashMultimap<Class, StatsUnit>();
        this.transientMap = new HashMultimap<Class, StatsUnit>();
    }

    public void reset() {
        this.persistedMap.clear();
        this.mergedMap.clear();
        this.duplicatesMap.clear();
        this.transientMap.clear();
    }

    public void addPersisted(AnnotatedObject ao) {
        StatsUnit su = StatsUnitFactory.createStatsUnit(ao);
        persistedMap.put(su.getType(), su);
    }

    public Multimap<Class, StatsUnit> getPersistedMap() {
        return persistedMap;
    }

    public Collection<StatsUnit> getPersisted(Class type, boolean includeSubclasses) {
        return statsOfType(type, getPersistedMap(), includeSubclasses);
    }

    public int getPersistedCount(Class type, boolean includeSubclasses) {
        return getPersisted(type, includeSubclasses).size();
    }

    public void addMerged(AnnotatedObject ao) {
        StatsUnit su = StatsUnitFactory.createStatsUnit(ao);
        mergedMap.put(su.getType(), su);
    }

    public Multimap<Class, StatsUnit> getMergedMap() {
        return mergedMap;
    }

    public Collection<StatsUnit> getMerged(Class type, boolean includeSubclasses) {
        return statsOfType(type, getMergedMap(), includeSubclasses);
    }

    public int getMergedCount(Class type, boolean includeSubclasses) {
        return getMerged(type, includeSubclasses).size();
    }

    public void addDuplicate(AnnotatedObject ao) {
        StatsUnit su = StatsUnitFactory.createStatsUnit(ao);
        duplicatesMap.put(su.getType(), su);
    }

    public Multimap<Class, StatsUnit> getDuplicatesMap() {
        return duplicatesMap;
    }

    public Collection<StatsUnit> getDuplicates(Class type, boolean includeSubclasses) {
        return statsOfType(type, getDuplicatesMap(), includeSubclasses);
    }

    public int getDuplicatesCount(Class type, boolean includeSubclasses) {
        return getDuplicates(type, includeSubclasses).size();
    }

    public void addTransient(AnnotatedObject ao) {
        StatsUnit su = StatsUnitFactory.createStatsUnit(ao);
        transientMap.put(su.getType(), su);
    }

    public Multimap<Class, StatsUnit> getTransientMap() {
        return transientMap;
    }

    public Collection<StatsUnit> getTransient(Class type, boolean includeSubclasses) {
        return statsOfType(type, getTransientMap(), includeSubclasses);
    }

    public int getTransientCount(Class type, boolean includeSubclasses) {
        return getTransient(type, includeSubclasses).size();
    }

    protected static Collection<StatsUnit> statsOfType(Class type, Multimap<Class, StatsUnit> multimap, boolean includeSubclasses) {
        if (!includeSubclasses) {
            return multimap.get(type);
        }
        Collection<StatsUnit> stats = new ArrayList<StatsUnit>();
        for (Class key : multimap.keySet()) {
            if (type.isAssignableFrom(key)) {
                stats.addAll(multimap.get(key));
            }
        }
        return stats;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Persister Stats:").append(NEW_LINE);
        sb.append("----------------").append(NEW_LINE);
        sb.append("Persisted: ").append(multimapToString(getPersistedMap())).append(NEW_LINE);
        sb.append("Merged: ").append(multimapToString(getMergedMap())).append(NEW_LINE);
        sb.append("Duplicates: ").append(multimapToString(getDuplicatesMap())).append(NEW_LINE);
        sb.append("Transient: ").append(multimapToString(getTransientMap())).append(NEW_LINE);
        return sb.toString();
    }

    private String multimapToString(Multimap<Class, StatsUnit> multimap) {
        StringBuilder sb = new StringBuilder();
        sb.append(multimap.size());
        if (!multimap.isEmpty()) {
            sb.append(" { ");
            for (Iterator<Class> iterator = multimap.keySet().iterator(); iterator.hasNext(); ) {
                Class key = iterator.next();
                sb.append(key.getSimpleName() + " (" + multimap.get(key).size() + ")");
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(" }");
        }
        return sb.toString();
    }
}
