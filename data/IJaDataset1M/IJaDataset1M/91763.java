package org.santeplanning.state;

import org.santeplanning.state.EditServiceState.AssociateCycleProgrammeTravailAndDay;
import org.stateengine.storage.IDBConnection;

public class RowHeureNormal implements RowType {

    public static final String DESIGNATION = "H.n.";

    public String getDesignation() {
        return DESIGNATION;
    }

    public String getValueFor(IDBConnection db, AssociateCycleProgrammeTravailAndDay associate) {
        if (associate.travail != null) {
            return "" + associate.travail.getHeureNormales();
        }
        return "";
    }

    public RowCounter getCompteur() {
        return new HeureNormalCounter();
    }

    public String getPairKey() {
        return getDesignation();
    }

    public String getPairValue() {
        return getDesignation();
    }

    public String toString() {
        return getDesignation();
    }
}
