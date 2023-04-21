package jEcoSim.Model;

import jEcoSim.Client.view.pnlPriceScale;
import jEcoSim.Server.Database.DatabaseAccessLayer;
import java.awt.Container;
import java.sql.SQLException;

public class PriceScale extends BusinessObject {

    /**
   * 
   */
    private static final long serialVersionUID = -2786656238573049105L;

    public Float _price = 0.f;

    public Integer _from = 0;

    public PriceScale(Cost c) {
        this.setParent(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PriceScale createInstance() {
        return new PriceScale((Cost) this.getParent());
    }

    @Override
    public boolean insert(DatabaseAccessLayer db) throws SQLException {
        db.insert(this);
        return true;
    }

    @Override
    public Container getPanel() {
        return new pnlPriceScale(this, false);
    }

    @Override
    public boolean update(DatabaseAccessLayer db) throws SQLException {
        return false;
    }

    @Override
    public boolean loadBusinessObject(DatabaseAccessLayer db) throws SQLException {
        return false;
    }
}
