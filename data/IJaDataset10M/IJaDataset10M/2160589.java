package org.xebra.scp.db.sql.event;

/**
 * An abstract class for receiving database access events.  None of the methods
 * in this class have been implemented.  Rather, this class has been 
 * created so that the developer can choose to only implement those methods
 * which relate to their code.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.1.1.1 $
 */
public abstract class DatabaseAccessAdaptor implements DatabaseAccessListener {

    public void databaseReadAttempt(DatabaseAccessEvent event) {
    }

    public void databaseWriteAttempt(DatabaseAccessEvent event) {
    }

    public void persistErrorThrown(DatabaseAccessEvent event) {
    }

    public void insertAttempt(DatabaseAccessEvent event) {
    }

    public void updateAttempt(DatabaseAccessEvent event) {
    }

    public void deleteAttempt(DatabaseAccessEvent event) {
    }

    public void loadAllAttempt(DatabaseAccessEvent event) {
    }

    public void loadByUIDAttempt(DatabaseAccessEvent event) {
    }

    public void loadByParentUIDAttempt(DatabaseAccessEvent event) {
    }

    public void loadByChildUIDAttempt(DatabaseAccessEvent event) {
    }

    public void loadByQueryAttempt(DatabaseAccessEvent event) {
    }
}
