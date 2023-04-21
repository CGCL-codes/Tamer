package gov.sns.apps.jeri.sql;

import java.util.ArrayList;

/**
 * Represents a table join in a SQL statement. This class makes it easier to 
 * generate dynamic SQL statements by generating the part of the where clause 
 * that binds the two tables.
 * 
 * @author Chris Fowlkes
 */
public class TableJoin {

    /**
   * The name of one of the tables being joined.
   */
    private String mainTableName;

    /**
   * The name of one of the tables being joined.
   */
    private String joinTableName;

    /**
   * Holds the criteria for joining the tables.
   */
    private ArrayList criteria = new ArrayList();

    /**
   * Creates a new <CODE>TableJoin</CODE>.
   * 
   * @param mainTableName The name of one of the tables being joined.
   * @param joinTableName The name of one of the tables being joined.
   */
    public TableJoin(String mainTableName, String joinTableName) {
        this.mainTableName = mainTableName;
        this.joinTableName = joinTableName;
    }

    /**
   * Adds criteria to the join.
   * 
   * @param mainTableColumnName The name of the column that corresponds to the first table passed into the constructor.
   * @param joinTableColumnName The name of the column that corresponds to the second table passed into the constructor.
   */
    public void addCriteria(String mainTableColumnName, String joinTableColumnName) {
        criteria.add(new String[] { mainTableColumnName, joinTableColumnName });
    }

    /**
   * Gets the number of fields joined.
   * 
   * @return The number of fields by which the tables are joined.
   */
    public int getCriteriaCount() {
        return criteria.size();
    }

    /**
   * Generates the portion of the where clause needed to join the tables. All
   * criteria are included with ands between them, and the whole value returned
   * is wrapped in parenthesis.
   * 
   * @return The portion of the where clause that joins the two tables together.
   */
    @Override
    public String toString() {
        int criteriaCount = getCriteriaCount();
        if (criteriaCount <= 0) return "";
        StringBuffer join = new StringBuffer(" (");
        String mainTableName = getMainTableName();
        String joinTableName = getJoinTableName();
        for (int i = 0; i < criteriaCount; i++) {
            if (i > 0) join.append(" AND ");
            String[] columnNames = (String[]) criteria.get(i);
            join.append(mainTableName);
            join.append(".");
            join.append(columnNames[0]);
            join.append(" = ");
            join.append(joinTableName);
            join.append(".");
            join.append(columnNames[1]);
        }
        join.append(") ");
        return join.toString();
    }

    /**
   * Gets the main table name.
   * 
   * @return The name of the main table being joined.
   */
    public String getMainTableName() {
        return mainTableName;
    }

    /**
   * Gets the name of the table being joined.
   * 
   * @return The name of the second table being joined.
   */
    public String getJoinTableName() {
        return joinTableName;
    }
}
