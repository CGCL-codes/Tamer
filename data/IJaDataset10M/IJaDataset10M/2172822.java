package mwt.xml.xdbforms.dblayer.query.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mwt.xml.xdbforms.dblayer.query.Criterion;

/**
 * Progetto Master Web Technology
 * @author Gianfranco Murador, Cristian Castiglia, Matteo Ferri
 */
public class CriterionImpl implements Criterion {

    private List values;

    private String restriction = "";

    public CriterionImpl() {
        values = new ArrayList();
    }

    public String toSQLString() {
        return restriction;
    }

    public void setValue(Object val) {
        values.add(val);
    }

    public void setRestriction(String restrictionStmt) {
        restriction += restrictionStmt;
    }

    public Object[] getValues() {
        Object[] objects = new Object[values.size()];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = values.get(i);
        }
        return objects;
    }

    public void setValues(Object[] vals) {
        for (int i = 0; i < vals.length; i++) {
            Object object = vals[i];
            values.add(object);
        }
    }
}
