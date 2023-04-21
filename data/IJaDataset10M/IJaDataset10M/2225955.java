package org.plc.inventaire.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.plc.inventaire.data.DBListener.Mode;

/**
 * @author pierreluc
 * @date 2010-10-20
 *
 */
abstract class AbstractDAO<T extends AbstractDBObject> implements DAO<T> {

    protected DBConnection connection;

    protected EventListenerList eventListenerList = new EventListenerList();

    /**
	 * @param connection
	 */
    protected AbstractDAO(DBConnection connection, int currentVersion) {
        this.connection = connection;
        createTable(currentVersion);
    }

    private void createTable(int currentVersion) {
    }

    protected abstract Logger getLogger();

    protected abstract List<DBColumn> getColumns();

    protected abstract String getTableName();

    protected abstract Class<T> getPrototypeClass();

    public void addDBListener(DBListener listener) {
        this.eventListenerList.add(DBListener.class, listener);
    }

    public void removeDBListener(DBListener listener) {
        this.eventListenerList.remove(DBListener.class, listener);
    }

    protected <S extends AbstractDBObject> S copy(AbstractDBObject original, S copy) {
        for (DBColumn f : getColumns()) {
            try {
                f.getField().set(copy, f.getField().get(original));
            } catch (IllegalArgumentException e) {
                getLogger().error("Shouldn't happen, it copies an object into another of the same class.", e);
            } catch (IllegalAccessException e) {
                getLogger().error("Shouldn't happen, it copies an object into another of the same class.", e);
            }
        }
        return copy;
    }

    protected void fillWithResultSet(T o, ResultSet rs) {
        for (DBColumn f : getColumns()) {
            Object value = null;
            boolean error = false;
            try {
                value = rs.getObject(f.getField().getName());
            } catch (SQLException e) {
                error = true;
            }
            if (!error) {
                try {
                    f.getField().set(o, value);
                } catch (IllegalArgumentException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                } catch (IllegalAccessException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                }
            }
        }
    }

    protected String toString(T o) {
        StringBuilder sb = new StringBuilder(o.getClass().getName()).append("{originalID=").append(o.getOriginalID()).append(',');
        for (DBColumn f : getColumns()) {
            try {
                sb.append(f.getName()).append('=').append(f.getField().get(o)).append(',');
            } catch (IllegalArgumentException e) {
                getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
            } catch (IllegalAccessException e) {
                getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }

    protected boolean add(T o) {
        PreparedStatement stat = null;
        try {
            StringBuilder cols = new StringBuilder(), vals = new StringBuilder();
            List<DBColumn> columns = getColumns();
            for (int i = 0; i < columns.size(); ++i) {
                if (i > 0) {
                    cols.append(',');
                    vals.append(",?");
                } else {
                    vals.append("?");
                }
                cols.append(columns.get(i).getName());
            }
            stat = this.connection.prepareStatement("INSERT INTO " + getTableName() + " (" + cols + ") VALUES (" + vals + ")");
            for (int i = 0; i < columns.size(); ++i) {
                try {
                    stat.setObject(i + 1, columns.get(i).getField().get(o));
                } catch (IllegalArgumentException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                } catch (IllegalAccessException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                }
            }
            stat.execute();
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
            return false;
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    getLogger().info("Ignorable database error.", e);
                }
            }
        }
        fireDBUpdated(Mode.ADDED, o);
        return true;
    }

    protected boolean cancelTransaction() {
        if (isInTransaction()) {
            try {
                this.connection.rollback();
                return true;
            } catch (SQLException e) {
                getLogger().error("Database access error.", e);
            }
        }
        return false;
    }

    @Deprecated
    protected void createTable(String tableName, List<Column> columns) throws SQLException {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for (Column c : columns) {
            sb.append(c.getColumnName()).append(" ").append(c.getColumnType()).append(',');
        }
        sb.setLength(sb.length() - 1);
        sb.append(')');
        this.connection.createStatement().executeUpdate(sb.toString());
    }

    protected boolean delete(T o) {
        if (o.getOriginalID() == null) {
            return false;
        }
        PreparedStatement stat = null;
        try {
            stat = this.connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE id=?");
            stat.setString(1, o.getOriginalID());
            stat.execute();
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
            return false;
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    getLogger().info("Ignorable database error.", e);
                }
            }
        }
        fireDBUpdated(Mode.DELETED, o);
        return true;
    }

    protected boolean fetch(T o) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = this.connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE id=?");
            stat.setString(1, o.getOriginalID());
            rs = stat.executeQuery();
            if (rs.next()) {
                fillWithResultSet(o, rs);
                fireDBUpdated(Mode.FETCHED, o);
                return true;
            }
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    getLogger().info("Ignorable database error.", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    getLogger().info("Ignorable database error.", e);
                }
            }
        }
        return false;
    }

    @Deprecated
    protected String getColumnName(String field) {
        for (DBColumn c : getColumns()) {
            if (c.getField().getName().equals(field)) {
                return c.getName();
            }
        }
        return null;
    }

    protected boolean isInTransaction() {
        try {
            return this.connection.isInTransaction();
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
        }
        return false;
    }

    protected boolean isModified(T o) {
        @SuppressWarnings("unchecked") T o2 = (T) o.copy();
        if (!fetch(o2)) {
            return true;
        }
        boolean modified = false;
        for (DBColumn c : getColumns()) {
            try {
                if (!ObjectUtils.equals(c.getField().get(o), c.getField().get(o2))) {
                    modified = true;
                    break;
                }
            } catch (IllegalArgumentException e) {
                getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
            } catch (IllegalAccessException e) {
                getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
            }
        }
        return modified;
    }

    protected boolean startTransaction() {
        try {
            this.connection.startTransaction();
            return true;
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
        }
        return false;
    }

    protected boolean stopTransaction() {
        if (isInTransaction()) {
            try {
                this.connection.stopTransaction();
                return true;
            } catch (SQLException e) {
                getLogger().error("Database access error.", e);
            }
        }
        return false;
    }

    protected boolean update(T o) {
        if (o.getOriginalID() == null) {
            return add(o);
        }
        PreparedStatement stat = null;
        try {
            StringBuilder sets = new StringBuilder();
            List<DBColumn> columns = getColumns();
            for (int i = 0; i < columns.size(); ++i) {
                if (i > 0) {
                    sets.append(',');
                }
                sets.append(columns.get(i).getName()).append(" = ?");
            }
            stat = this.connection.prepareStatement("UPDATE " + getTableName() + " SET " + sets + " WHERE id = ?");
            for (int i = 0; i < columns.size(); ++i) {
                try {
                    stat.setObject(i + 1, columns.get(i).getField().get(o));
                } catch (IllegalArgumentException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                } catch (IllegalAccessException e) {
                    getLogger().error("Shouldn't happen, the variable was discovered be reflection.", e);
                }
            }
            stat.setString(columns.size() + 1, o.getOriginalID());
            stat.execute();
        } catch (SQLException e) {
            getLogger().error("Database access error.", e);
            return false;
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    getLogger().info("Ignorable database error.", e);
                }
            }
        }
        fireDBUpdated(Mode.UPDATED, o);
        return true;
    }

    protected void fireDBUpdated(Mode mode, T obj) {
        fireDBUpdated(mode, Collections.nCopies(1, obj));
    }

    protected void fireDBUpdated(Mode mode, List<T> objs) {
        for (DBListener l : this.eventListenerList.getListeners(DBListener.class)) {
            l.dbUpdated(this, mode, objs);
        }
    }
}
