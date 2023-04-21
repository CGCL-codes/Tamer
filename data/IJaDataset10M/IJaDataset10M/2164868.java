package com.avaje.ebean.server.persist.mapbean;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import com.avaje.ebean.MapBean;
import com.avaje.ebean.server.core.ConcurrencyMode;
import com.avaje.ebean.server.core.PersistRequest;
import com.avaje.ebean.server.deploy.BeanProperty;
import com.avaje.ebean.server.persist.Binder;

/**
 * Process Delete of a MapBean.
 */
public class DeleteMapBean extends BaseMapBean {

    MapBean oldBean;

    boolean firstColumn = true;

    @SuppressWarnings("unchecked")
    public DeleteMapBean(Binder binder, PersistRequest request) {
        super(binder, request);
        this.oldBean = (MapBean) request.getOldValues();
        if (oldBean == null) {
            oldBean = mapBean;
        }
        generate();
    }

    /**
	 * execute the delete and perform checkRowCount() and postExecute().
	 */
    protected void executeStmt(PreparedStatement pstmt) throws SQLException {
        int rc = pstmt.executeUpdate();
        request.checkRowCount(rc);
        request.postExecute();
    }

    protected void generate() {
        genSql.append("delete from ").append(desc.getBaseTable());
        genSql.append(" where ");
        bindLogAppend("Binding delete [");
        bindLogAppend(desc.getBaseTable());
        bindLogAppend("]  where[");
        whereIdColumns();
        int conMode = request.getConcurrencyMode();
        if (conMode == ConcurrencyMode.VERSION) {
            whereVersionColumns();
        } else if (conMode == ConcurrencyMode.ALL) {
            whereBaseColumns();
        }
    }

    protected void whereIdColumns() {
        BeanProperty[] uids = desc.propertiesId();
        if (uids.length == 1) {
            BeanProperty uid = uids[0];
            String propName = uid.getName();
            String dbColumn = uid.getDbColumn();
            int dbType = uid.getDbType();
            Object value = mapBean.get(propName);
            includeWhere(propName, dbColumn, value, dbType);
            request.setBoundId(value);
        } else {
            LinkedHashMap<String, Object> mapId = new LinkedHashMap<String, Object>();
            for (int i = 0; i < uids.length; i++) {
                String propName = uids[i].getName();
                String dbColumn = uids[i].getDbColumn();
                int dbType = uids[0].getDbType();
                Object value = mapBean.get(propName);
                includeWhere(propName, dbColumn, value, dbType);
                mapId.put(propName, value);
            }
            request.setBoundId(mapId);
        }
    }

    protected void whereVersionColumns() {
        BeanProperty[] props = desc.propertiesVersion();
        for (int i = 0; i < props.length; i++) {
            String propName = props[i].getName();
            if (mapBean.containsKey(propName)) {
                includeWhere(props[i], mapBean);
            } else {
            }
        }
    }

    /**
	 * For ALL concurrency mode bind using the OldValues.
	 */
    protected void whereBaseColumns() {
        BeanProperty[] props = desc.propertiesBaseScalar();
        for (int i = 0; i < props.length; i++) {
            String propName = props[i].getName();
            if (oldBean.containsKey(propName)) {
                includeWhere(props[i], oldBean);
            }
        }
    }

    private void includeWhere(BeanProperty prop, MapBean bean) {
        String propName = prop.getName();
        int dbType = prop.getDbType();
        if (!isLob(dbType)) {
            Object value = bean.get(propName);
            String dbColumn = prop.getDbColumn();
            includeWhere(propName, dbColumn, value, dbType);
        }
    }

    private boolean isLob(int type) {
        switch(type) {
            case Types.LONGVARCHAR:
                return true;
            case Types.CLOB:
                return true;
            case Types.LONGVARBINARY:
                return true;
            case Types.BLOB:
                return true;
            default:
                return false;
        }
    }

    private void includeWhere(String propName, String dbColumn, Object value, int dbType) {
        if (firstColumn) {
            firstColumn = false;
        } else {
            genSql.append(" and ");
        }
        genSql.append(dbColumn);
        if (value == null) {
            genSql.append(" is null");
            bindValues.addComment(", " + propName + "=NULL");
        } else {
            genSql.append("=?");
            bindValue(value, dbType, propName);
        }
    }
}
