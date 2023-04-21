package org.databene.mad4db.cmd;

import org.databene.jdbacl.model.DBTrigger;
import org.databene.mad4db.ChangeSeverity;

/**
 * Represents the creation of a trigger.<br/><br/>
 * Created: 10.11.2011 13:47:01
 * @since 0.3
 * @author Volker Bergmann
 */
public class TriggerCreation extends Creation<DBTrigger> {

    public TriggerCreation(DBTrigger trigger) {
        super(trigger);
        this.severity = ChangeSeverity.REORGANIZATION;
    }
}
