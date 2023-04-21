package jp.go.aist.six.util.castor;

import jp.go.aist.six.util.persist.AssociationEntry;

/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: AssociationEntryHelper.java 300 2011-03-02 05:45:46Z nakamura5akihito $
 */
public class AssociationEntryHelper<T extends AssociationEntry<?, ?, ?>> extends PersistenceHelper<T> {

    /**
     * Constructor.
     */
    public AssociationEntryHelper() {
    }

    @Override
    public boolean hasUnique() {
        return true;
    }

    @Override
    public Object getUnique(final T object) {
        return (new Object[] { object.getAntecendentPersistentID(), object.getDependentPersistentID() });
    }

    @Override
    public String getUniqueFilter() {
        return " WHERE o.antecendentPersistentID = $1 AND o.dependentPersistentID = $2";
    }
}
