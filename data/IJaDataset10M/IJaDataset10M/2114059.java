package org.avaje.ebean.server.query;

import java.util.List;
import org.avaje.ebean.bean.EntityBean;
import org.avaje.ebean.server.deploy.DbReadContext;
import org.avaje.ebean.server.deploy.DbSqlContext;
import org.avaje.ebean.server.deploy.jointree.JoinNode;

/**
 * Represents the root node of the Sql Tree.
 */
public final class SqlTreeNodeRoot extends SqlTreeNodeBean {

    /**
	 * Specify for SqlSelect to include an Id property or not.
	 */
    public SqlTreeNodeRoot(JoinNode node, SqlTreeProperties props, List<SqlTreeNode> myList, boolean withId) {
        super(node, props, myList, withId);
    }

    /**
	 * Normal constructor.
	 */
    public SqlTreeNodeRoot(JoinNode node, SqlTreeProperties props, List<SqlTreeNode> myList) {
        super(node, props, myList, true);
    }

    @Override
    protected void postLoad(DbReadContext cquery, EntityBean loadedBean, Object id) {
        cquery.setLoadedBean(loadedBean, id);
    }

    /**
	 * For the root node there is no join type or on clause etc.
	 */
    @Override
    public void appendFromBaseTable(DbSqlContext ctx, boolean forceOuterJoin) {
        ctx.append(desc.getBaseTable());
        if (desc.getBaseTableAlias() != null) {
            ctx.append(" ").append(desc.getBaseTableAlias());
        }
    }
}
