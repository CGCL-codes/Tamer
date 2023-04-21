package org.ujorm.orm;

import org.ujorm.UjoProperty;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.implementation.quick.QuickUjoMid;
import org.ujorm.orm.metaModel.MetaProcedure;

/**
 * Abstract database procedure.
 * @author Ponec
 */
public abstract class DbProcedure<UJO extends DbProcedure> extends QuickUjoMid<UJO> {

    /** Meta - model */
    protected transient MetaProcedure metaProcedure = null;

    /** Clear all parameters */
    @SuppressWarnings("unchecked")
    public DbProcedure clear() {
        for (UjoProperty p : readProperties()) {
            p.setValue(this, null);
        }
        return this;
    }

    /** Call the procedure and return a type-safe value of the required Property */
    @SuppressWarnings("unchecked")
    public <T> T call(final Session session, final UjoProperty<UJO, T> result) {
        if (metaProcedure == null) {
            metaProcedure = session.getHandler().findProcedureModel(getClass());
        }
        session.call(this);
        return result.of((UJO) this);
    }

    /** Call the procedure and return a value of the first Property. <br>
     * WARNING: The result is NOT type-save value, use rather {@link #call(org.ujorm.orm.Session, org.ujorm.UjoProperty)}.
     * @see #call(org.ujorm.orm.Session, org.ujorm.UjoProperty)
     */
    @SuppressWarnings("unchecked")
    public <T> T call(final Session session) {
        return (T) call(session, readProperties().get(0));
    }

    /** Returns MetaModel of the procedure */
    public MetaProcedure metaProcedure() {
        return metaProcedure;
    }

    /** A PropertyIterator Factory creates an new property and assign a next index.
     * @hidden
     */
    protected static <UJO extends OrmTable, ITEM extends OrmTable> RelationToMany<UJO, ITEM> newRelation(String name, Class<ITEM> type) {
        return new RelationToMany<UJO, ITEM>(name, type, -1, false);
    }

    /** A PropertyIterator Factory creates an new property and assign a next index.
     * @hidden
     */
    protected static <UJO extends OrmTable, ITEM extends OrmTable> RelationToMany<UJO, ITEM> newRelation(Class<ITEM> type) {
        return newRelation(null, type);
    }
}
