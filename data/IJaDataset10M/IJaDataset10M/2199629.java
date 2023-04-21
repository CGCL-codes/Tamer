package edu.psu.citeseerx.updates;

import edu.psu.citeseerx.domain.Document;

/**
 * Interface for all classes that should be notified when changes
 * to article metadata occur.  Implementing classes should be registered
 * with the UpdateManager.
 *
 * @author Isaac Councill
 * @version $Rev: 862 $ $Date: 2009-01-02 13:05:40 -0500 (Fri, 02 Jan 2009) $
 */
public interface UpdateListener {

    public void handleUpdate(Document doc);
}
