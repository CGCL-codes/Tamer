package org.eaiframework.filters.routers;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eaiframework.Message;
import org.eaiframework.MessageException;
import org.eaiframework.MessageSender;
import org.eaiframework.filters.BaseFilter;

/**
 * 
 */
public class RecepientListRouter extends BaseFilter {

    private Log log = LogFactory.getLog(RecepientListRouter.class);

    private Collection<String> recepientList;

    public RecepientListRouter() {
        this.recepientList = new ArrayList<String>();
    }

    public void doFilter(Message message) {
        log.debug(getLogHead() + "routing message to channels: " + recepientList);
        MessageSender sender = filterContext.getMessageSender();
        for (String channelId : recepientList) {
            try {
                sender.sendMessage(message, channelId);
            } catch (MessageException e) {
                log.warn(getLogHead() + "message could not be sent to " + channelId + ": " + e.getMessage(), e);
            }
        }
    }

    public void addRecepientListItem(String item) {
        recepientList.add(item);
    }

    public void removeRecepientListItem(String item) {
        recepientList.remove(item);
    }

    /**
	 * 
	 * @param recepientList
	 */
    public void setRecepientList(Collection<String> recepientList) {
        log.debug(getLogHead() + "setting recepientList property: " + recepientList);
        this.recepientList = recepientList;
    }

    /**
	 * @return Returns the recepientList.
	 */
    public Collection<String> getRecepientList() {
        return recepientList;
    }
}
