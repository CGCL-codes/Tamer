package org.xmldb.core.criteria.projections;

import org.xmldb.core.criteria.xpath.XPathSintax;

/**
 * 
 * @author Giacomo Stefano Gabriele
 *
 */
public abstract class Projection implements XPathSintax {

    protected String query;

    protected Class classe;

    public void setQuery(String query) {
        this.query = query;
    }

    public static Projection rowCount() {
        return new RowCount();
    }

    public static Projection projectionList(String property) {
        return new ProjectionList(property);
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }
}
