package org.openremote.modeler.client.listener;

import org.openremote.modeler.client.event.ResponseJSONEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Listener for JSON response.
 * 
 * Usage:
 * <pre>
    FormPanel form = new FormPanel();
    form.addListener(ResponseJSONEvent.ResponseJSON, new ResponseJSONListener() {
            public void afterSubmit(ResponseJSONEvent rje) {
              Object data = rje.getData();
              // do something
            }
         });
 * </pre>
 */
public abstract class ResponseJSONListener implements Listener<ResponseJSONEvent> {

    /**
    * {@inheritDoc}
    * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
    */
    public void handleEvent(ResponseJSONEvent rje) {
        if (rje.getType() == ResponseJSONEvent.RESPONSEJSON) {
            afterSubmit(rje);
        }
    }

    /**
    * After submit.
    * 
    * @param rje the rje
    */
    public abstract void afterSubmit(ResponseJSONEvent rje);
}
