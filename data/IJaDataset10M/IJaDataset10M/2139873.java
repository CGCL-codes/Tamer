package cz.cvut.felk.cs.metamorphoses.mapping;

/**
 * <p>description of this class is missing!</p>
 * <p>
 * <b>History:</b><br/>
 * Created: 2.8.2004<br/>
 * Last change: 2.8.2004<br/>
 * </p>
 * @author Martin Svihla (martin@svihla.net)
 */
public class MappingCondition {

    private String name;

    private String whereSql;

    private String tableSql;

    private String comment;

    public MappingCondition(String name, String whereSql, String tableSql, String comment) {
        this.name = name;
        this.whereSql = whereSql;
        this.tableSql = tableSql;
        this.comment = comment;
    }

    /**
	 * @return
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return
	 */
    public String getTableSql() {
        return tableSql;
    }

    /**
	 * @return
	 */
    public String getWhereSql() {
        return whereSql;
    }
}
