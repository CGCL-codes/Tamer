package org.actioncenters.cometd.cache.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.actioncenters.core.contribution.data.IContribution;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * @author dougk
 *
 */
public class ChannelCacheEventListener implements CacheEventListener, Cloneable {

    /**
     * Map of contribution channels.
     */
    private static ConcurrentMap<String, List<String>> contributionChannels = new ConcurrentHashMap<String, List<String>>();

    /**
     * Full constructor.
     * @param contributionChannels
     *          the contribution channels
     */
    public ChannelCacheEventListener(ConcurrentMap<String, List<String>> contributionChannels) {
        setContributionChannels(contributionChannels);
    }

    /**
     * Default constructor.
     */
    public ChannelCacheEventListener() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        removeElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        removeElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        addElement(element);
    }

    /**
     * @param element
     *          the element
     */
    @SuppressWarnings("unchecked")
    private void addElement(Element element) {
        List<IContribution> contributions = (List<IContribution>) element.getValue();
        for (IContribution contribution : contributions) {
            addElement(contribution.getId(), (String) element.getKey());
        }
    }

    /**
     * @param contributionId
     *          the contribution id
     * @param channelName
     *          the channel name
     */
    private void addElement(String contributionId, String channelName) {
        List<String> channels = getContributionChannels().get(contributionId);
        if (channels == null) {
            channels = new ArrayList<String>();
            getContributionChannels().put(contributionId, channels);
        }
        channels.add(channelName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        removeElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRemoveAll(Ehcache cache) {
        getContributionChannels().clear();
    }

    /**
     * {@inheritDoc}
     */
    public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
        return new ChannelCacheEventListener(getContributionChannels());
    }

    /**
     * @return the conributionChannels
     */
    private static ConcurrentMap<String, List<String>> getContributionChannels() {
        return contributionChannels;
    }

    /**
     * @param contributionChannels the contributionChannels to set
     */
    private static void setContributionChannels(ConcurrentMap<String, List<String>> contributionChannels) {
        ChannelCacheEventListener.contributionChannels = contributionChannels;
    }

    /**
     * 
     * @param contributionId
     *          the contribution id
     * @return channels
     */
    public static List<String> getChannelsForContributionId(String contributionId) {
        List<String> returnValue = new ArrayList<String>();
        List<String> currentList = getContributionChannels().get(contributionId);
        if (currentList != null) {
            returnValue.addAll(currentList);
        }
        return returnValue;
    }

    /**
     * @param element
     *          the element
     */
    @SuppressWarnings("unchecked")
    private void removeElement(Element element) {
        List<IContribution> contributions = (List<IContribution>) element.getValue();
        if (contributions != null) {
            for (IContribution contribution : contributions) {
                remove(contribution.getId(), (String) element.getKey());
            }
        }
    }

    /**
     * @param contributionId
     *          the contribution id
     * @param channel
     *          the channel
     */
    private void remove(String contributionId, String channel) {
        while (getContributionChannels().get(contributionId).contains(channel)) {
            getContributionChannels().get(contributionId).remove(channel);
        }
    }
}
