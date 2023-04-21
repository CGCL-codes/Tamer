package com.completex.objective.components.persistency;

/**
 * @author Gennady Krizhevsky
 */
public interface BasicQuery {

    OrderDirection ORDER_ASC = OrderDirection.ASC;

    OrderDirection ORDER_DESC = OrderDirection.DESC;

    /**
     * Query states:
     */
    int STATE_NEW = 0;

    int STATE_ACTIVE = 1;

    int STATE_CLOSED = 2;

    String UPDATE = "UPDATE ";

    String DELETE = "DELETE ";

    String SELECT = "SELECT ";

    String DISTINCT = " DISTINCT ";

    String COUNT = " count ";

    String COUNT_1 = " count(1) ";

    String EXISTS = " EXISTS ";

    String WHERE = " WHERE ";

    String FROM = " FROM ";

    String ORDER_BY = " ORDER BY ";

    String GROUP_BY = " GROUP BY ";

    String HAVING = " HAVING ";

    String AND = " AND ";

    String OR = " OR ";

    String ASC = " ASC ";

    String DESC = " DESC ";

    String STAR = " * ";

    String DOT_STAR = ".* ";

    String EQ = " = ";

    String NE = " <> ";

    String GT = " > ";

    String GE = " >= ";

    String LT = " < ";

    String LE = " <= ";

    String PH = "?";

    String PH1 = "? ";

    String EPH = "=?s";

    String EPH1 = "=? ";

    String MULTI_WILD = "%";

    String SQ = "'";

    String LIKE = " LIKE ";

    String BETWEEN = " BETWEEN ";

    String LOWER = " LOWER";

    String UPPER = " UPPER";

    /**
     * Returns SQL string that this query represents
     * 
     * @return SQL string that this query represents
     */
    String getSql();

    /**
     * Sets SQL string that this query represents
     * @param sql SQL string that this query represents
     * @return itself
     */
    BasicQuery setSql(String sql);

    /**
     * Set query name
     *
     * @param name
     */
    void setName(String name);

    /**
     * @return query name
     */
    String getName();

    BasicQuery setParameters(Parameter[] parameters);

    AbstractParameters getBaseParameters();

    /**
     * Sets page size in records
     * 
     * @param pageSize page size in records
     * @return itself
     */
    BasicQuery setPageSize(long pageSize);

    /**
     * Returns page size
     * 
     * @return page size
     */
    long getPageSize();

    /**
     * Set 0-based offset from which records to be retrieved
     *
     * @param offset
     */
    void setOffset(long offset);

    /**
     * Returns 0-based offset from which records to be retrieved
     *
     * @return 0-based offset from which records to be retrieved
     */
    long getOffset();

    /**
     * Sets page parameters in terms of <code>BasicQuery.Page<code/>
     *
     * @param page
     * @return itself
     */
    BasicQuery setPage(Page page);

    /**
     * Order direction type - ascending or descending.
     */
    static class OrderDirection {

        protected static final OrderDirection ASC = new OrderDirection("ACS");

        protected static final OrderDirection DESC = new OrderDirection("DESC");

        private String sqlString;

        private String name;

        private OrderDirection(String name) {
            this.name = name;
            this.sqlString = " " + name + " ";
        }

        public String toString() {
            return name;
        }

        public String getSqlString() {
            return sqlString;
        }
    }
}
