package uk.ac.roslin.ensembl.datasourceaware;

import uk.ac.roslin.ensembl.model.ExternalDB;
import uk.ac.roslin.ensembl.model.ObjectType;
import uk.ac.roslin.ensembl.config.EnsemblCoreObjectType;

/**
 *
 * @author tpaterso
 */
public class DAExternalDB extends DAObject implements ExternalDB {

    String dBName = null;

    String dBDisplayName = null;

    String dBRelease = null;

    String dBStatus = null;

    String dBType = null;

    public String getDBDisplayName() {
        return dBDisplayName;
    }

    public void setDBDisplayName(String dBDisplayName) {
        this.dBDisplayName = dBDisplayName;
    }

    public String getDBName() {
        return dBName;
    }

    public void setDBName(String dBName) {
        this.dBName = dBName;
    }

    public String getDBRelease() {
        return dBRelease;
    }

    public void setDBRelease(String dBRelease) {
        this.dBRelease = dBRelease;
    }

    public String getDBStatus() {
        return dBStatus;
    }

    public void setDBStatus(String dBStatus) {
        this.dBStatus = dBStatus;
    }

    public String getDBType() {
        return dBType;
    }

    public void setDBType(String dBType) {
        this.dBType = dBType;
    }

    public ObjectType getType() {
        return EnsemblCoreObjectType.externalDB;
    }
}
