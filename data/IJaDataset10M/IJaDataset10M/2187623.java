package com.avaje.ebeaninternal.server.query;

import java.sql.SQLException;
import java.util.List;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;

public final class SqlTreeNodeManyRoot extends SqlTreeNodeBean {

    public SqlTreeNodeManyRoot(String prefix, BeanPropertyAssocMany<?> prop, SqlTreeProperties props, List<SqlTreeNode> myList) {
        super(prefix, prop, prop.getTargetDescriptor(), props, myList, true);
    }

    @Override
    protected void postLoad(DbReadContext cquery, Object loadedBean, Object id) {
        cquery.setLoadedManyBean(loadedBean);
    }

    @Override
    public void load(DbReadContext cquery, Object parentBean) throws SQLException {
        super.load(cquery, null);
    }

    /**
     * Force outer join for everything after the many property.
     */
    @Override
    public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin) {
        super.appendFrom(ctx, true);
    }
}
