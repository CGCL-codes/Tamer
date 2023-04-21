package com.avaje.ebean.server.query;

import java.util.List;
import com.avaje.ebean.server.deploy.BeanDescriptor;
import com.avaje.ebean.server.deploy.DbReadContext;
import com.avaje.ebean.server.deploy.DbSqlContext;
import com.avaje.ebean.server.deploy.TableJoin;

/**
 * Represents the root node of the Sql Tree.
 */
public final class SqlTreeNodeRoot extends SqlTreeNodeBean {

    private final TableJoin includeJoin;

    /**
	 * Specify for SqlSelect to include an Id property or not.
	 */
    public SqlTreeNodeRoot(BeanDescriptor<?> desc, SqlTreeProperties props, List<SqlTreeNode> myList, boolean withId, TableJoin includeJoin) {
        super(null, null, desc, props, myList, withId);
        this.includeJoin = includeJoin;
    }

    @Override
    protected void postLoad(DbReadContext cquery, Object loadedBean, Object id) {
        cquery.setLoadedBean(loadedBean, id);
    }

    /**
	 * For the root node there is no join type or on clause etc.
	 */
    @Override
    public void appendFromBaseTable(DbSqlContext ctx, boolean forceOuterJoin) {
        ctx.append(desc.getBaseTable());
        ctx.append(" ").append(ctx.getTableAlias(null));
        if (includeJoin != null) {
            String a1 = ctx.getTableAlias(null);
            String a2 = "int_";
            includeJoin.addJoin(forceOuterJoin, a1, a2, ctx);
        }
    }
}
