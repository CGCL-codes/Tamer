package wogwt.translatable.http;

import wogwt.translatable.WOGWTClientUtil;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.logging.shared.Log;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/** 
 * Intended mainly to turn a regular WOHyperlink with an ACTION binding into an
 * ajax link.
 * 
 * This listener will do an ajax request to the url obtained from the onClick sender's
 * "href" attribute and replace the updateContainer
 * with the response.
 *
 */
public class UpdateOnClickListener extends Updater implements ClickHandler {

    private Widget eventSender;

    public UpdateOnClickListener() {
        super();
    }

    /**
	 * Used with an existing WOHyperlink created by your WOComponent page.
	 * @param updateContainerID DOM container to update
	 */
    public UpdateOnClickListener(String updateContainerID) {
        super(updateContainerID);
    }

    /**
	 * Used with an existing WOHyperlink created by your WOComponent page.
	 * @param updateContainerID DOM container to update
	 * @param callback
	 */
    public UpdateOnClickListener(String updateContainerID, AfterDOMUpdateCallback callback) {
        super(updateContainerID, callback);
    }

    /**
	 * Used with a gwt-created Hyperlink or Anchor instead of a link that was
	 * created in your WOComponent page.
	 * 
	 * @param actionName component action to execute on click
	 * @param updateContainerID DOM container to update
	 * @param callback
	 */
    public UpdateOnClickListener(String actionName, String updateContainerID, AfterDOMUpdateCallback callback) {
        super(actionName, updateContainerID, callback);
    }

    public void onClick(ClickEvent event) {
        Log.finest("onClick: " + ((Widget) event.getSource()).getElement().getId());
        eventSender = (Widget) event.getSource();
        try {
            fireUpdate();
        } finally {
            eventSender = null;
            if (Event.getCurrentEvent() != null) Event.getCurrentEvent().preventDefault();
        }
    }

    @Override
    protected Object getSender() {
        return eventSender;
    }

    @Override
    protected String getUrl() {
        if (getActionName() == null) {
            String url = ((Widget) getSender()).getElement().getAttribute("href");
            return WOGWTClientUtil.componentUrlToAjaxUrl(url, getUpdateContainerID());
        } else {
            return super.getUrl();
        }
    }
}
