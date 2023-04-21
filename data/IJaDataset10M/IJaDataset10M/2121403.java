package org.enjavers.jethro.api.triggers;

/**
 * @author Alessandro Lombardi
 * @since 1.07
 */
public abstract class DeleteTriggerAdapter implements Trigger {

    public abstract void deletePerformed(TriggerEvent i_event);

    public abstract void deletePerforming(TriggerEvent i_event);

    public void insertPerformed(TriggerEvent i_event) throws TriggerException {
    }

    public void updatePerformed(TriggerEvent i_event) throws TriggerException {
    }

    public void findPerformed(TriggerEvent i_event) throws TriggerException {
    }

    public void insertPerforming(TriggerEvent i_event) throws TriggerException {
    }

    public void updatePerforming(TriggerEvent i_event) throws TriggerException {
    }

    public void findPerforming(TriggerEvent i_event) throws TriggerException {
    }

    public void transactionPerforming(TriggerEvent i_event) throws TriggerException {
    }

    public void transactionPerformed(TriggerEvent i_event) throws TriggerException {
    }
}
