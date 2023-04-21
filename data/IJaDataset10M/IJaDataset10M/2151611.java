package org.opoo.oqs.core;

import java.util.ArrayList;
import java.util.List;
import org.opoo.oqs.PreparedStatementBatcher;
import org.opoo.oqs.StatementBatcher;
import org.opoo.oqs.TypedValue;

/**
 *
 *
 * @author Alex Lin(alex@opoo.org)
 * @version 1.0
 */
public abstract class AbstractBatcher implements StatementBatcher, PreparedStatementBatcher {

    private final AbstractQueryFactory queryFactory;

    private String sql = null;

    private List list = null;

    private List sqls = null;

    public AbstractBatcher(AbstractQueryFactory queryFactory) {
        sqls = new ArrayList();
        this.queryFactory = queryFactory;
    }

    public AbstractBatcher(AbstractQueryFactory queryFactory, String sql) {
        this.sql = sql;
        this.queryFactory = queryFactory;
        list = new ArrayList();
    }

    public PreparedStatementBatcher addBatch(TypedValue[] typedValues) {
        list.add(typedValues);
        return this;
    }

    public StatementBatcher addBatch(String sql) {
        sqls.add(sql);
        return this;
    }

    protected String getSql() {
        if (queryFactory.debugLevel > 0) {
            System.out.println("[SQL]: " + sql);
        }
        return sql;
    }

    protected String[] getSqls() {
        String[] sqla = (String[]) sqls.toArray(new String[sqls.size()]);
        if (queryFactory.debugLevel > 0) {
            for (int i = 0; i < sqla.length; i++) {
                System.out.println("[SQL" + i + "]: " + sqla[i]);
            }
        }
        return sqla;
    }

    protected List getTypedValuesList() {
        return list;
    }
}
