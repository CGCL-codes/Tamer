package org.liris.schemerger.ui.observablemodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.liris.schemerger.chronicle.ChrTimeConstraint;
import org.liris.schemerger.chronicle.cstdb.CstDB;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.IEpisode;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.core.pattern.TimeInterval;
import org.liris.schemerger.core.persistence.TypeAdapter;
import org.liris.schemerger.ui.editors.SimplifiedChronicle;
import org.liris.schemerger.utils.IndexEntry;

public class ObservableChronicle<T extends ITypeDec> implements IChronicle<T> {

    private IChronicle<T> baseChronicle;

    public IChronicle<T> makeUnionWith(Class<T> clazz, IChronicle<T> moreGeneralPattern) {
        return baseChronicle.makeUnionWith(clazz, moreGeneralPattern);
    }

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public String toString() {
        return baseChronicle.toString();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public static final String NEW_CONSTRAINT = "newConstraint";

    public static final String NEW_TYPEDEC = "newItemType";

    public static final String TYPEDEC_REMOVED = "itemTypeRemoved";

    public static final String CONSTRAINT_REMOVED = "constraintRemoved";

    public static final String CONSTRAINT_CHANGED = "constraintChanged";

    public void addConstraint(ChrTimeConstraint cst) {
        baseChronicle.addConstraint(cst);
        support.firePropertyChange(NEW_CONSTRAINT, null, cst);
    }

    public void addEventDec(T t) {
        baseChronicle.addEventDec(t);
        support.firePropertyChange(NEW_TYPEDEC, null, t);
    }

    public IChronicle<T> clone() {
        return baseChronicle.clone();
    }

    public int compareTo(IChronicle<? super T> o) {
        return baseChronicle.compareTo(o);
    }

    public int depth(CstDB<?> cstDB) {
        return baseChronicle.depth(cstDB);
    }

    public Set<IChronicle<T>> get_km1_subChronicles() {
        return baseChronicle.get_km1_subChronicles();
    }

    public TimeInterval getConstraint(IndexEntry ie) {
        return baseChronicle.getConstraint(ie);
    }

    public TimeInterval getConstraint(Integer index1, Integer index2) {
        return baseChronicle.getConstraint(index1, index2);
    }

    public Collection<ChrTimeConstraint> getConstraints() {
        return baseChronicle.getConstraints();
    }

    public List<IndexEntry> getCstIndexEntries() {
        return baseChronicle.getCstIndexEntries();
    }

    public IEpisode<T> getEpisode() {
        return baseChronicle.getEpisode();
    }

    public int getIndexOfDec(ITypeDec t) {
        return baseChronicle.getIndexOfDec(t);
    }

    public boolean isBuiltOn(CstDB<?> cstDb) {
        return baseChronicle.isBuiltOn(cstDb);
    }

    public boolean isConsistent() {
        return baseChronicle.isConsistent();
    }

    public boolean isConsistentWith(ChrTimeConstraint ctc) {
        return baseChronicle.isConsistentWith(ctc);
    }

    public boolean isSEMoreConstrainedThan(IChronicle<T> otherChronicle) {
        return baseChronicle.isSEMoreConstrainedThan(otherChronicle);
    }

    public boolean isStricterThan(IChronicle<T> otherChronicle) {
        return baseChronicle.isStricterThan(otherChronicle);
    }

    public boolean propagateConstraints() {
        return baseChronicle.propagateConstraints();
    }

    public void removeConstraint(IndexEntry o) {
        baseChronicle.removeConstraint(o);
        support.firePropertyChange(CONSTRAINT_REMOVED, null, o);
    }

    public void removeConstraint(int index1, int index2) {
        baseChronicle.removeConstraint(index1, index2);
        support.firePropertyChange(CONSTRAINT_REMOVED, null, new IndexEntry(index1, index2));
    }

    public void removeEventDec(int index) {
        baseChronicle.removeEventDec(index);
        support.firePropertyChange(TYPEDEC_REMOVED, null, index);
    }

    public void replaceConstraint(IndexEntry entry, ChrTimeConstraint ctc) {
        baseChronicle.replaceConstraint(entry, ctc);
        support.firePropertyChange(CONSTRAINT_CHANGED, null, entry);
    }

    public void setConstraint(IndexEntry key, TimeInterval timeInterval) {
        baseChronicle.setConstraint(key, timeInterval);
        support.firePropertyChange(CONSTRAINT_CHANGED, null, key);
    }

    public String to1LineString() {
        return baseChronicle.to1LineString();
    }

    public String to1LineString(TypeAdapter adapter) {
        return baseChronicle.to1LineString(adapter);
    }

    public String toString(TypeAdapter typeAdapter) {
        return baseChronicle.toString(typeAdapter);
    }

    private SimplifiedChronicle<T> simplifiedChronicle;

    public ObservableChronicle(IChronicle<T> baseChronicle) {
        super();
        this.baseChronicle = baseChronicle;
        this.simplifiedChronicle = new SimplifiedChronicle<T>(this);
    }

    public SimplifiedChronicle<T> getSimplifiedChronicle() {
        return simplifiedChronicle;
    }

    public IChronicle<T> copy() {
        return baseChronicle.copy();
    }
}
