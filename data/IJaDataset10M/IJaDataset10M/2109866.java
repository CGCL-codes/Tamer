package com.triplea.dao.sql;

import com.triplea.dao.Consolidation;
import com.triplea.dao.DataException;
import com.triplea.dao.Element;
import java.sql.PreparedStatement;

public class ConsolidationImpl implements Consolidation {

    private ElementImpl parent;

    private ElementImpl child;

    private double weight;

    public ConsolidationImpl(ElementImpl parent, ElementImpl child, double weight) {
        this.parent = parent;
        this.child = child;
        this.weight = weight;
    }

    public Element getParent() {
        return parent;
    }

    public Element getChild() {
        return child;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) throws DataException {
        this.weight = weight;
        updateConsolidation();
    }

    public void insertConsolidation() throws DataException {
        try {
            PreparedStatement s = getStatementsCache().getPreparedStatement("insert into CONSOLIDATIONS (PARENT_ID, CHILD_ID, WEIGHT) values (?,?,?);");
            s.setInt(1, parent.getId());
            s.setInt(2, child.getId());
            s.setDouble(3, weight);
            s.execute();
            parent.dim.database.connect.commit();
            parent.dim.alterToken();
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    public void deleteConsolidation() throws DataException {
        try {
            PreparedStatement s = getStatementsCache().getPreparedStatement("delete from CONSOLIDATIONS where PARENT_ID=? and CHILD_ID=?;");
            s.setInt(1, parent.getId());
            s.setInt(2, child.getId());
            s.execute();
            parent.dim.database.connect.commit();
            parent.dim.alterToken();
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    public void updateConsolidation() throws DataException {
        try {
            PreparedStatement s = getStatementsCache().getPreparedStatement("update CONSOLIDATIONS set WEIGHT=? where PARENT_ID=? and CHILD_ID=?;");
            s.setDouble(1, weight);
            s.setInt(2, parent.getId());
            s.setInt(3, child.getId());
            s.execute();
            parent.dim.database.connect.commit();
            parent.dim.alterToken();
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    private StatementCache getStatementsCache() {
        return parent.getStatementsCache();
    }
}
