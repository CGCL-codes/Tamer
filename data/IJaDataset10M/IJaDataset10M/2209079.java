package org.enjavers.jethro.api.triggers;

import java.io.Serializable;

/**
 * A trigger exposes methods that are invoked in callback
 * by the DAOs. Be careful to preserve the parameterless
 * constructor to allow run-time instantiation.
 * Also consider a Trigger a stateless object.
 * 
 * @author Alessandro Lombardi
 * @since 1.07
 */
public interface Trigger extends Serializable {

    /**
	 * Invoked before an insert query.
	 */
    public void insertPerforming(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked before an update query.
	 */
    public void updatePerforming(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked before a delete query.
	 */
    public void deletePerforming(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked before a find query.
	 */
    public void findPerforming(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked after an insert query.
	 */
    public void insertPerformed(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked after an update query.
	 */
    public void updatePerformed(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked after a delete query.
	 */
    public void deletePerformed(TriggerEvent i_event) throws TriggerException;

    /**
	 * Invoked after a find query.
	 */
    public void findPerformed(TriggerEvent i_event) throws TriggerException;

    /**
	 * invoked before executeQueryGroup
	 * @param i_event
	 * @throws TriggerException
	 */
    public void queryGroupPerforming(TriggerEvent i_event) throws TriggerException;

    /**
	 * invoked after executeQueryGroup
	 * @param i_event
	 * @throws TriggerException
	 */
    public void queryGroupPerformed(TriggerEvent i_event) throws TriggerException;
}
