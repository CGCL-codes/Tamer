package org.simfony.database;

import org.simfony.Document;

/**
 * DatabaseListener is interface for listening Document changes in the
 * database.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public interface DatabaseListener {

    /**
    * This method is invoked when document has been added to the database.
    *
    * @param Document which has been added.
    */
    public void documentAdded(Database database, Document document);

    /**
    * This method is invoked when document has been removed from the database.
    *
    * @param Document which has been removed.
    */
    public void documentRemoved(Database database, Document document);

    /**
    * This method is invoked when document has been updated in the database.
    *
    * @param Document which has been updated.
    */
    public void documentUpdated(Database database, Document document);
}
