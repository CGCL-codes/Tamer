package de.cinek.rssview.event;

/**
 * @todo broaden event to handle meta data through this
 * @author  saintedlama
 */
public interface ChannelListener {

    public void articlesAdded(ChannelEvent event);

    public void articleStateChanged(ChannelEvent event);

    public void articleRemoved(ChannelEvent event);
}
