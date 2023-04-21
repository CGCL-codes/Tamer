package org.openconcerto.sql.sqlobject;

import org.openconcerto.sql.Log;
import org.openconcerto.sql.model.DBRoot;
import org.openconcerto.sql.model.IResultSetHandler;
import org.openconcerto.sql.model.SQLDataSource;
import org.openconcerto.sql.model.SQLField;
import org.openconcerto.sql.model.SQLRowValues;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.request.SQLRowItemView;
import org.openconcerto.sql.sqlobject.itemview.RowItemViewComponent;
import org.openconcerto.ui.component.ComboLockedMode;
import org.openconcerto.ui.component.ITextComboCache;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An ITextCombo with the cache from COMPLETION.
 * 
 * @author Sylvain CUAZ
 */
public class SQLTextCombo extends org.openconcerto.ui.component.ITextCombo implements RowItemViewComponent {

    public SQLTextCombo() {
        super();
    }

    public SQLTextCombo(boolean locked) {
        super(locked);
    }

    public SQLTextCombo(ComboLockedMode mode) {
        super(mode);
    }

    public void init(SQLRowItemView v) {
        final ITextComboCacheSQL cache = new ITextComboCacheSQL(v.getField());
        if (cache.isValid()) this.initCache(cache);
    }

    public static class ITextComboCacheSQL implements ITextComboCache {

        private final String field;

        private final SQLTable t;

        private final List<String> cache;

        private boolean loadedOnce;

        public ITextComboCacheSQL(final SQLField f) {
            this(f.getDBRoot(), f.getFullName());
        }

        public ITextComboCacheSQL(final DBRoot r, final String id) {
            this.field = id;
            this.t = r.findTable("COMPLETION");
            if (!this.isValid()) Log.get().warning("no completion found for " + this.field);
            this.cache = new ArrayList<String>();
            this.loadedOnce = false;
        }

        public final boolean isValid() {
            return this.t != null;
        }

        private final SQLDataSource getDS() {
            return this.t.getDBSystemRoot().getDataSource();
        }

        public List<String> loadCache(final boolean dsCache) {
            final SQLSelect sel = new SQLSelect(this.t.getBase());
            sel.addSelect(this.t.getField("LABEL"));
            sel.setWhere(new Where(this.t.getField("CHAMP"), "=", this.field));
            @SuppressWarnings("unchecked") final List<String> items = (List<String>) this.getDS().execute(sel.asString(), new IResultSetHandler(SQLDataSource.COLUMN_LIST_HANDLER) {

                @Override
                public boolean readCache() {
                    return dsCache;
                }

                @Override
                public boolean writeCache() {
                    return true;
                }
            });
            this.cache.clear();
            this.cache.addAll(items);
            return this.cache;
        }

        public List<String> getCache() {
            if (!this.loadedOnce) {
                this.loadCache(true);
                this.loadedOnce = true;
            }
            return this.cache;
        }

        public void addToCache(String string) {
            if (!this.cache.contains(string)) {
                final Map<String, Object> m = new HashMap<String, Object>();
                m.put("CHAMP", this.field);
                m.put("LABEL", string);
                try {
                    new SQLRowValues(this.t, m).insert(true, false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.cache.add(string);
            }
        }

        public void deleteFromCache(String string) {
            final Where w = new Where(this.t.getField("CHAMP"), "=", this.field).and(new Where(this.t.getField("LABEL"), "=", string));
            this.getDS().executeScalar("DELETE FROM " + this.t.getSQLName().quote() + " WHERE " + w.getClause());
        }

        @Override
        public String toString() {
            return this.getClass().getName() + " on " + this.field;
        }
    }
}
