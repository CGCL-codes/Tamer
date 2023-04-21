package org.santeplanning.state;

import org.santeplanning.state.EditServiceState.AssociateCycleProgrammeTravailAndDay;
import org.stateengine.storage.IDBConnection;

public class RowHeureDebut implements RowType {

    public static final String DESIGNATION = "Deb";

    public String getDesignation() {
        return DESIGNATION;
    }

    public String getValueFor(IDBConnection db, AssociateCycleProgrammeTravailAndDay associate) {
        try {
            if (associate.travail.getTypeHoraire().getHeureDebut().intValue() == 0 && associate.travail.getTypeHoraire().getMinuteDebut().intValue() == 0 && associate.travail.getTypeHoraire().getHeureFin().intValue() == 0 && associate.travail.getTypeHoraire().getMinuteFin().intValue() == 0) {
                return ".";
            }
            return associate.travail.getTypeHoraire().getHeureDebut() + "h" + associate.travail.getTypeHoraire().getMinuteDebut();
        } catch (Exception e) {
            return "";
        }
    }

    public RowCounter getCompteur() {
        return new EmptyRowCounter();
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
