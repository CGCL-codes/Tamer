package uk.ac.ebi.intact.application.predict.business;

/**
 * This class implements Predict user for an Oracle database.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: PredictUserOra.java 2359 2003-12-10 14:20:13Z smudali $
 */
public class PredictUserOra extends PredictUser {

    protected String getSpeciesSQL() {
        return "SELECT DISTINCT species FROM ia_payg";
    }

    protected String getDbInfoSQL(String taxid) {
        return "SELECT nID FROM ia_payg WHERE ROWNUM<=50 AND really_used_as_bait='N' " + " AND species =\'" + taxid + "\' ORDER BY indegree DESC, qdegree DESC";
    }
}
