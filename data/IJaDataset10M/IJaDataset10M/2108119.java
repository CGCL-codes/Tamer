package be.roam.drest.xml.rss;

import java.util.ArrayList;

/**
 * Implementation of {@link Rss20Feed}.
 * 
 * @author Kevin Wetzels
 * @version 1.0
 */
public class Rss20FeedImpl extends Rss20Feed<Rss20ChannelImpl> {

    /**
     * @see be.roam.drest.xml.rss.Rss20Feed#addChannel(be.roam.drest.xml.rss.Rss20Channel)
     */
    @Override
    public void addChannel(Rss20ChannelImpl channel) {
        if (getChannelList() == null) {
            ArrayList<Rss20ChannelImpl> list = new ArrayList<Rss20ChannelImpl>();
            setChannelList(list);
        }
        getChannelList().add(channel);
    }
}
