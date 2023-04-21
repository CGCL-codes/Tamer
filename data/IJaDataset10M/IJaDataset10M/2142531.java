package org.hibernate.hql.ast.util;

import java.util.Map;
import org.hibernate.hql.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.ast.tree.FromElement;
import org.hibernate.hql.ast.tree.QueryNode;
import org.hibernate.hql.ast.tree.RestrictableStatement;
import org.hibernate.hql.ast.tree.SqlFragment;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.JoinFragment;
import org.hibernate.util.StringHelper;
import antlr.ASTFactory;
import antlr.collections.AST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates synthetic and nodes based on the where fragment part of a JoinSequence.
 *
 * @author josh
 */
public class SyntheticAndFactory implements HqlSqlTokenTypes {

    private static final Logger log = LoggerFactory.getLogger(SyntheticAndFactory.class);

    private ASTFactory astFactory;

    private AST thetaJoins;

    private AST filters;

    public SyntheticAndFactory(ASTFactory astFactory) {
        this.astFactory = astFactory;
    }

    public void addWhereFragment(JoinFragment joinFragment, String whereFragment, QueryNode query, FromElement fromElement) {
        if (whereFragment == null) {
            return;
        }
        whereFragment = whereFragment.trim();
        if (StringHelper.isEmpty(whereFragment)) {
            return;
        } else if (!fromElement.useWhereFragment() && !joinFragment.hasThetaJoins()) {
            return;
        }
        if (whereFragment.startsWith("and")) {
            whereFragment = whereFragment.substring(4);
        }
        if (log.isDebugEnabled()) log.debug("Using WHERE fragment [" + whereFragment + "]");
        SqlFragment fragment = (SqlFragment) ASTUtil.create(astFactory, SQL_TOKEN, whereFragment);
        fragment.setJoinFragment(joinFragment);
        fragment.setFromElement(fromElement);
        if (fragment.getFromElement().isFilter() || fragment.hasFilterCondition()) {
            if (filters == null) {
                AST where = query.getWhereClause();
                filters = astFactory.create(FILTERS, "{filter conditions}");
                ASTUtil.insertChild(where, filters);
            }
            filters.addChild(fragment);
        } else {
            if (thetaJoins == null) {
                AST where = query.getWhereClause();
                thetaJoins = astFactory.create(THETA_JOINS, "{theta joins}");
                if (filters == null) {
                    ASTUtil.insertChild(where, thetaJoins);
                } else {
                    ASTUtil.insertSibling(thetaJoins, filters);
                }
            }
            thetaJoins.addChild(fragment);
        }
    }

    public void addDiscriminatorWhereFragment(RestrictableStatement statement, Queryable persister, Map enabledFilters, String alias) {
        String whereFragment = persister.filterFragment(alias, enabledFilters).trim();
        if ("".equals(whereFragment)) {
            return;
        }
        if (whereFragment.startsWith("and")) {
            whereFragment = whereFragment.substring(4);
        }
        whereFragment = StringHelper.replace(whereFragment, persister.generateFilterConditionAlias(alias) + ".", "");
        AST discrimNode = astFactory.create(SQL_TOKEN, whereFragment);
        if (statement.getWhereClause().getNumberOfChildren() == 0) {
            statement.getWhereClause().setFirstChild(discrimNode);
        } else {
            AST and = astFactory.create(AND, "{and}");
            AST currentFirstChild = statement.getWhereClause().getFirstChild();
            and.setFirstChild(discrimNode);
            and.addChild(currentFirstChild);
            statement.getWhereClause().setFirstChild(and);
        }
    }
}
