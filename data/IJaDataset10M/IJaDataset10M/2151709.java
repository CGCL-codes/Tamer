package com.spring.rssReader.jdbc;

import com.spring.rssReader.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import java.io.CharArrayReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * This class is the central controller for the channel and items.
 */
public class ChannelController implements IChannelController {

    public static final int STATUS_UNKNOWN = -1;

    public static final int STATUS_OK = 0;

    public static final int STATUS_NOT_FOUND = 1;

    public static final int STATUS_INVALID_CONTENT = 2;

    public static final int STATUS_CONNECTION_TIMEOUT = 3;

    public static final int STATUS_UNKNOWN_HOST = 4;

    public static final int STATUS_NO_ROUTE_TO_HOST = 5;

    public static final int STATUS_SOCKET_EXCEPTION = 6;

    /** date patterns to try to format the date as defined in the possbile date fields of the rss feeds */
    public static final SimpleDateFormat rssFormatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static final SimpleDateFormat rssFormatDateWithoutSeconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static final SimpleDateFormat rssFormatDateVersion2 = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss");

    public static final SimpleDateFormat httpFormatDateVersion2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

    private IChannelDAO channelDAO;

    private IItemDAO itemDAO;

    protected final Log logger = LogFactory.getLog(getClass());

    public ChannelController() {
    }

    public List getArticles() {
        return this.channelDAO.getChannels(IChannelDAO.ARTICLES);
    }

    public List getFavourites() {
        return this.channelDAO.getChannels(IChannelDAO.FAVOURITES);
    }

    public List getChannelsToRead() {
        return this.channelDAO.getChannels(IChannelDAO.CHANNELS_TO_READ);
    }

    public List getNoNewsChannels() {
        return this.channelDAO.getChannels(IChannelDAO.NO_NEWS_CHANNELS);
    }

    public List getEmptyChannels() {
        return this.channelDAO.getChannels(IChannelDAO.EMPTY_CHANNELS);
    }

    public List getChannels(String queryKey) {
        return channelDAO.getChannels(queryKey);
    }

    public List getCategorizedChannels(ICategory category) {
        return channelDAO.getCategorizedChannels(category);
    }

    public Channel getChannel(Long id) {
        return this.channelDAO.getChannel(id);
    }

    public void markAsRead(Long id) {
        this.channelDAO.markAsRead(id);
        this.itemDAO.markItemsAsRead(id);
        this.channelDAO.update(getChannel(id));
    }

    public void update(Channel channel) {
        if (channel.getId() == null) {
            this.channelDAO.insert(channel);
        } else {
            this.channelDAO.update(channel);
        }
    }

    public void remove(Channel channel) {
        if (channel.isHtml()) {
            this.channelDAO.delete(channel);
        } else {
            this.channelDAO.update(channel);
        }
    }

    public void update(Channel channel, Item item) {
        if (item.getId() == null) {
            channel.setNumberOfItems(channel.getNumberOfItems() + 1);
            channel.addItem(item);
            this.itemDAO.insert(item);
            this.update(channel);
        } else {
            this.itemDAO.update(item);
        }
    }

    public List getAllItems(Long channelID) {
        return this.itemDAO.getAllItems(channelID);
    }

    public List getNewItems(Long channelID) {
        return this.itemDAO.getNewItems(channelID);
    }

    public void removeChannel(Long id) {
        Channel channel = this.getChannel(id);
        channel.remove();
        this.remove(channel);
        this.itemDAO.deleteItemsFromChannel(channel);
    }

    /**
     * delete an item means that the description will be nulled and remove will be set to true, so that this item doesnt
     * take up space in the database, yet stays in the database so wont be imported again. After the item has deleted
     * the total number of items and number of items read must be updated.
     * @param itemId
     */
    public void deleteItem(Long itemId) {
        Item item = this.getItem(itemId);
        Channel channel = this.getChannel(item.getChannelID());
        channel.removeItem(item);
        this.update(channel);
        if (item.isFetched()) {
            this.itemDAO.deleteItem(item);
        } else {
            this.update(channel, item);
        }
    }

    public Item getItem(Long itemId) {
        return this.itemDAO.getItem(itemId);
    }

    /**
     * Delete a bunch of items in one go.
     * @param itemsToDelete
     */
    public void deleteItems(List itemsToDelete) {
        Channel channel = null;
        for (int i = 0; i < itemsToDelete.size(); i++) {
            Item item = (Item) itemsToDelete.get(i);
            if (channel == null) {
                channel = this.getChannel(item.getChannelID());
            }
            channel.removeItem(item);
            this.update(channel, item);
        }
        if (channel != null) {
            this.update(channel);
        }
    }

    public void dontPollAnymore(Long id) {
        Channel channel = this.getChannel(id);
        channel.setRemove(true);
        this.update(channel);
    }

    public List getChannelsByUrl(String url) {
        return this.channelDAO.findChannelsByUrl(url);
    }

    public List getChannelsLikeUrl(String url) {
        return this.channelDAO.findChannelsLikeUrl("%" + url.toLowerCase() + "%");
    }

    public List getChannelsLikeTitle(String title) {
        return this.channelDAO.findChannelsLikeTitle("%" + title.toLowerCase() + "%");
    }

    /**
     * Marks the item as read, and updates the number of read items in the channel.
     * @param item
     */
    public void markItemRead(Item item, boolean read) {
        Channel channel = this.getChannel(item.getChannelID());
        channel.markItemRead(item, read);
        this.update(channel, item);
        this.update(channel);
    }

    /**
     * This method will set the categories for this item. If the channel isnt set to this category yet, the category is
     * added to the channel as well.
     * @param item
     * @param choosenCategories
     */
    public void setCategories(Item item, List choosenCategories) {
        Channel channel = item.getChannel();
        List channelCategories = channel.getCategories();
        item.setCategories(choosenCategories);
        int nrOfPreviousCategories = channelCategories.size();
        for (int i = 0; i < choosenCategories.size(); i++) {
            Category category = (Category) choosenCategories.get(i);
            boolean foundChannel = false;
            for (int j = 0; j < nrOfPreviousCategories; j++) {
                Category savedCategory = (Category) channelCategories.get(j);
                if (savedCategory.getId().equals(category.getId())) {
                    foundChannel = true;
                    break;
                }
            }
            if (!foundChannel) {
                channelCategories.add(category);
            }
        }
        if (nrOfPreviousCategories != channelCategories.size()) {
            channel.setCategories(channelCategories);
        }
        this.update(channel, item);
    }

    /**
     * This method will try to establish whether the url of the GeneralItem is unique.
     * @param channel
     * @return
     */
    public boolean isUniqueUrl(IGeneralItem channel) {
        List channels = this.getChannelsByUrl(channel.getUrl());
        if (channels.size() == 0) {
            return true;
        }
        if (channels.size() == 1) {
            if (channel.getId() == null) {
                return false;
            }
            if (channel.getId().equals(((Channel) channels.get(0)).getId())) {
                return true;
            }
        }
        return false;
    }

    public synchronized void pollChannel(Channel channel) {
        System.out.println("Starting to poll channel: " + channel.getTitle() + " with id " + channel.getId());
        channel.setStatus(-2);
        channel.setPollsStarted(channel.getPollsStarted() + 1);
        this.update(channel);
        String bodyResponse = null;
        channel.setLastPolled(new java.util.Date().getTime());
        Map parsedChannel = null;
        try {
            if (channel.isHtml() && channel.getNumberOfItems() == 0 || !channel.isHtml()) {
                bodyResponse = channel.load();
                if (channel.getStatus() == HttpURLConnection.HTTP_OK && bodyResponse != null) {
                    if (!channel.isHtml()) {
                        ChannelPullHandler pullHandler = new ChannelPullHandler();
                        parsedChannel = pullHandler.parse(new CharArrayReader(bodyResponse.toCharArray()));
                        String title = (String) parsedChannel.get("title");
                        if (title == null) {
                            title = channel.getUrl();
                        }
                        channel.setTitle(title);
                        channel.setLastModified((String) parsedChannel.get("lastModified"));
                        channel.setLastETag((String) parsedChannel.get("lastETag"));
                        processParsedItems(channel, parsedChannel);
                    } else {
                        Item item = new Item();
                        item.setChannel(channel);
                        item.setUrl(channel.getUrl());
                        item.processHtml(bodyResponse);
                        this.update(channel, item);
                    }
                }
                if (channel.getStatus() == 304 || channel.getStatus() == 200) {
                    channel.setPollsStarted(0);
                    this.update(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (bodyResponse != null) {
                    String fileName = "error_" + channel.getId() + ".xml";
                    FileOutputStream fos = new FileOutputStream(fileName);
                    fos.write(bodyResponse.getBytes());
                    fos.close();
                }
                return;
            } catch (IOException e1) {
            }
        }
    }

    public List getChannelsToPoll() {
        return channelDAO.getChannels(IChannelDAO.CHANNELS_TO_POLL);
    }

    /**
     * process the items that were found in the rss feed.
     * @param channel
     * @param parsedChannel
     */
    private void processParsedItems(Channel channel, Map parsedChannel) {
        if (!parsedChannel.containsKey("items")) {
            return;
        }
        List items = (List) parsedChannel.get("items");
        int size = items.size();
        int addedItems = 0;
        for (int i = 0; i < size; i++) {
            Map parsedItem = (Map) items.get(i);
            String url = (String) parsedItem.get("url");
            String description = (String) parsedItem.get("description");
            if (description == null || description.equals("")) {
                continue;
            }
            Item foundItem = null;
            foundItem = itemDAO.findItemByUrl(url, channel.getId());
            if (foundItem == null) {
                foundItem = new Item();
            } else {
                if (foundItem.getDescription() != null || foundItem.isRemove()) {
                    continue;
                }
            }
            BeanWrapperImpl wrapper = new BeanWrapperImpl(foundItem);
            parsedItem.remove("id");
            wrapper.setPropertyValues(new MutablePropertyValues(parsedItem), true);
            foundItem.setChannel(channel);
            foundItem.processHtml(description);
            String stringDate = (String) parsedItem.get("date");
            foundItem.findDate(stringDate);
            update(channel, foundItem);
            addedItems++;
        }
        this.update(channel);
    }

    public IChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(IChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public IItemDAO getItemDAO() {
        return itemDAO;
    }

    public void setItemDAO(IItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }
}
