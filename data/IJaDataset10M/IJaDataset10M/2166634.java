package br.com.wepa.webapps.orca.logica.persistencia.search;

import static br.com.wepa.webapps.orca.util.CheckValue.IF;
import br.com.wepa.webapps.orca.util.CheckValue.Condition;
import br.com.wepa.webapps.search.PagingBean;

/**
 * Representa uma Query de busca utilizando Hibernate Query Language
 * @author Fabr�cio Silva Epaminondas
 */
public class HqlQuerySearch<T> extends QuerySearch<T> {

    private Class resultClass = null;

    public HqlQuerySearch() {
        super();
        nonSqlStatement();
    }

    public HqlQuerySearch(PagingBean pagingBean) {
        this();
        super.configPaging(pagingBean);
    }

    public HqlQuerySearch(Class resultClass) {
        this();
        this.resultClass = resultClass;
    }

    public void and(String str) {
        if (!where.isEmpty()) {
            where.add(" and ");
        }
        where.add(str);
    }

    public void and(String str, Object... params) {
        and(str);
        addParams(params);
    }

    /**
	 * Adds the sentence if the parameter match the conditions
	 * 
	 * @param str
	 * @param param,
	 *            the parameter value
	 * @param conditions,
	 *            to test the parameter, for example: NOTNULL, NOTZERO
	 * @return
	 */
    public boolean andIf(String str, Object param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            and(str, param);
            return true;
        }
        return false;
    }

    public void or(String str) {
        if (!where.isEmpty()) {
            where.add(" or ");
        }
        where.add(str);
    }

    public void or(String str, Object... params) {
        or(str);
        addParams(params);
    }

    /**
	 * Adds the sentence if the parameter match the conditions
	 * 
	 * @param str
	 * @param param,
	 *            the parameter value
	 * @param conditions,
	 *            to test the parameter, for example: NOTNULL, NOTZERO
	 * @return
	 */
    public boolean orIf(String str, Object param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            or(str, param);
            super.buffer.get().capacity();
            return true;
        }
        return false;
    }

    @Override
    public String mount() {
        mountSelect();
        mountFrom();
        mountWhere();
        mountGroupBy();
        mountOrderBy();
        return super.mount();
    }

    protected void mountSelect() {
        if (!select.isEmpty()) {
            if (resultClass != null) {
                select.addStart("new " + resultClass.getName() + "( ");
                select.addEnd(") ");
            }
            select.addStart(" select ");
        }
    }

    protected void mountFrom() {
        from.addStart(" from ");
    }

    protected void mountWhere() {
        if (!where.isEmpty()) {
            where.addStart(" where ");
        }
    }

    protected void mountGroupBy() {
        if (!groupBy.isEmpty()) {
            groupBy.addStart(" group by ");
        }
    }

    protected void mountOrderBy() {
        if (!orderBy.isEmpty()) {
            orderBy.addStart(" order by ");
        }
    }

    public String likeStart(String value) {
        return "%" + value;
    }

    public String likeEnd(String value) {
        return value + "%";
    }

    public String likeAnywhere(String value) {
        return "%" + value + "%";
    }

    public boolean andLikeExactlyIf(String property, String param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            and(property + " like ? ", param);
            return true;
        }
        return false;
    }

    public boolean andLikeAnywhereIf(String property, String param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            and(property + " like ? ", likeAnywhere(param));
            return true;
        }
        return false;
    }

    public boolean andLikeStartIf(String property, String param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            and(property + " like ? ", likeStart(param));
            return true;
        }
        return false;
    }

    public boolean andLikeEndIf(String property, String param, final Condition... conditions) {
        if (IF.chk(param, conditions)) {
            and(property + " like ? ", likeEnd(param));
            return true;
        }
        return false;
    }
}
