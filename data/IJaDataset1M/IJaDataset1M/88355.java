package org.openconcerto.sql.model;

import org.openconcerto.sql.Configuration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLInjector {

    private final SQLTable tableSrc, tableDest;

    private final ArrayList<SQLField> from = new ArrayList<SQLField>();

    private final ArrayList<SQLField> to = new ArrayList<SQLField>();

    private Map<SQLField, Object> values = new HashMap<SQLField, Object>();

    private static Map<SQLTable, Map<SQLTable, SQLInjector>> injectors = new HashMap<SQLTable, Map<SQLTable, SQLInjector>>();

    public SQLInjector(final DBRoot r, final String src, final String dest) {
        this(r.findTable(src), r.findTable(dest));
    }

    public SQLInjector(SQLTable src, SQLTable dest) {
        this.tableDest = dest;
        this.tableSrc = src;
        Map<SQLTable, SQLInjector> srcs = injectors.get(src);
        if (srcs == null) {
            srcs = new HashMap<SQLTable, SQLInjector>();
            injectors.put(src, srcs);
        }
        srcs.put(dest, this);
    }

    public SQLRowValues createRowValuesFrom(int idSrc) {
        return createRowValuesFrom(getSource().getRow(idSrc));
    }

    private static final SQLSystem dbSystem = Configuration.getInstance().getBase().getServer().getSQLSystem();

    public SQLRowValues createRowValuesFrom(final SQLRowAccessor srcRow) {
        if (!srcRow.getTable().equals(getSource())) throw new IllegalArgumentException("Row not from source table : " + srcRow);
        SQLRowValues rowVals = new SQLRowValues(getDestination());
        for (SQLField field : this.values.keySet()) {
            rowVals.put(field.getName(), this.values.get(field));
        }
        for (int i = 0; i < getFrom().size(); i++) {
            final SQLField sqlFieldFrom = getFrom().get(i);
            final SQLField sqlFieldTo = getTo().get(i);
            final Object o = srcRow.getObject(sqlFieldFrom.getName());
            if (dbSystem == SQLSystem.H2 && sqlFieldFrom.getType().getJavaType() == Long.class && sqlFieldTo.getType().getJavaType() == Integer.class) {
                rowVals.put(sqlFieldTo.getName(), ((Long) o).intValue());
            } else {
                rowVals.put(sqlFieldTo.getName(), o);
            }
        }
        return rowVals;
    }

    public SQLRow insertFrom(final SQLRowAccessor srcRow) throws SQLException {
        return createRowValuesFrom(srcRow).insert();
    }

    /**
     * mettre une valeur par défaut pour un champ donné
     * 
     * @param fieldDest
     * @param defaultValue
     */
    protected final void mapDefaultValues(SQLField fieldDest, Object defaultValue) {
        if (fieldDest.getTable().getName().equalsIgnoreCase(this.tableDest.getName())) {
            this.values.put(fieldDest, defaultValue);
        } else {
            throw new IllegalArgumentException("SQLField " + fieldDest + " is not a field of table " + this.tableDest);
        }
    }

    protected final void map(SQLField from, SQLField to) throws IllegalArgumentException {
        if (!from.getTable().getName().equalsIgnoreCase(this.tableSrc.getName())) {
            throw new IllegalArgumentException("SQLField " + from + " is not a field of table " + this.tableSrc);
        } else {
            if (!to.getTable().getName().equalsIgnoreCase(this.tableDest.getName())) {
                throw new IllegalArgumentException("SQLField " + to + " is not a field of table " + this.tableDest);
            }
        }
        int index = this.from.indexOf(from);
        if (index > 0) {
            this.to.set(index, to);
        } else {
            this.from.add(from);
            this.to.add(to);
        }
    }

    protected final void remove(SQLField from, SQLField to) throws IllegalArgumentException {
        if (!from.getTable().getName().equalsIgnoreCase(this.tableSrc.getName())) {
            throw new IllegalArgumentException("SQLField " + from + " is not a field of table " + this.tableSrc);
        } else {
            if (!to.getTable().getName().equalsIgnoreCase(this.tableDest.getName())) {
                throw new IllegalArgumentException("SQLField " + to + " is not a field of table " + this.tableDest);
            }
        }
        int index = this.from.indexOf(from);
        if (this.to.get(index).getName().equalsIgnoreCase(to.getName())) {
            this.to.remove(to);
            this.from.remove(from);
        }
    }

    /**
     * Créer l'association entre les champs portant le nom dans les deux tables
     * 
     */
    public void createDefaultMap() {
        for (SQLField field : this.tableSrc.getContentFields()) {
            if (this.tableDest.contains(field.getName())) {
                map(field, this.tableDest.getField(field.getName()));
            }
        }
    }

    public ArrayList<SQLField> getFrom() {
        return this.from;
    }

    public ArrayList<SQLField> getTo() {
        return this.to;
    }

    public static SQLInjector getInjector(SQLTable src, SQLTable dest) {
        Map<SQLTable, SQLInjector> m = injectors.get(src);
        if (m != null) {
            return m.get(dest);
        } else {
            return null;
        }
    }

    /**
     * Creer un SQLInjector par défaut si aucun n'est déja défini
     * 
     * @param src
     * @param dest
     * @return un SQLInjector par défaut si aucun n'est déja défini
     */
    public static SQLInjector createDefaultInjector(SQLTable src, SQLTable dest) {
        if (getInjector(src, dest) == null) {
            System.err.println("No SQLInjector defined for " + src + " , " + dest + ". SQLInjector created automatically.");
            SQLInjector injector = new SQLInjector(src, dest);
            injector.createDefaultMap();
            return injector;
        } else {
            return getInjector(src, dest);
        }
    }

    public SQLTable getDestination() {
        return this.tableDest;
    }

    public SQLTable getSource() {
        return this.tableSrc;
    }
}
