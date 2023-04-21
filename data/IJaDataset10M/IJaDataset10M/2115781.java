package org.openremote.modeler.client.listener;

import org.openremote.modeler.client.event.SubmitEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Listener for data submit.
 * 
 * Usage:
 * <pre>
    FormPanel form = new FormPanel();
    form.addListener(SubmitEvent.Submit, new SubmitListener() {
            public void afterSubmit(SubmitEvent be) {
              Object data = be.getData();
              // do something
            }
         });
 * </pre>
 */
public abstract class SubmitListener implements Listener<SubmitEvent> {

    /**
    * {@inheritDoc}
    * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
    */
    public void handleEvent(SubmitEvent be) {
        if (be.getType() == SubmitEvent.SUBMIT) {
            afterSubmit(be);
        }
    }

    /**
    * After submit.
    * 
    * @param be the SubmitEvent
    */
    public abstract void afterSubmit(SubmitEvent be);
}
