package com.nogoodatcoding.cip.idle.services;

import com.nogoodatcoding.cip.CiscoIPPhoneMenu;
import com.nogoodatcoding.cip.MenuItem;
import com.nogoodatcoding.cip.SoftKeyItem;
import com.nogoodatcoding.cip.idle.CIPPhone;
import com.nogoodatcoding.cip.idle.interfaces.IdleService;
import com.nogoodatcoding.cip.idle.utils.Utils;
import com.nogoodatcoding.cip.interfaces.CiscoIPPhoneXMLObject;
import com.nogoodatcoding.commons.FeedDateComparator;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * Service to display feeds on the phone
 * 
 * @author no.good.at.coding
 */
public class Feeder implements IdleService {

    private static Logger log_ = Logger.getLogger(Feeder.class);

    private static ResourceBundle messages_ = ResourceBundle.getBundle("com.nogoodatcoding.cip.idle.services.messages.Messages_Feeder");

    private Map<String, List<String>> initParameters;

    private Map<String, Long> defaultFeeds = null;

    private List<SoftKeyItem> defaultSoftKeyItems = null;

    private FeederCache cache = new FeederCache();

    private FeedDateComparator feedDateComparator = new FeedDateComparator();

    private int defaultMaxItems = 100;

    private long defaultDisplayRefreshTime = 5 * Utils.MULTIPLIER_FOR_MINUTES_TO_SECONDS;

    public String getIdleServiceAuthor() {
        return "no.good.at.coding";
    }

    public String getIdleServiceDescription() {
        return "Subscribe to feeds on your 'home' screen";
    }

    public String getIdleServiceName() {
        return "Feeder";
    }

    public void init(Map<String, List<String>> initParameters) {
        this.initParameters = initParameters;
        defaultSoftKeyItems = Utils.parseSoftKeyItems(initParameters);
        defaultFeeds = splitFeedStrings(initParameters.get("feed"));
        List<String> value = initParameters.get("max-items");
        if (value != null && value.size() > 0) {
            try {
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.parsingMaxItems") + value);
                defaultMaxItems = Integer.parseInt(value.get(0));
                if (defaultMaxItems > 100 || defaultMaxItems <= 0) {
                    Feeder.log_.warn(Feeder.messages_.getString("feeder.log.warn.invalidMaxItems"));
                    defaultMaxItems = 100;
                }
            } catch (NumberFormatException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.maxItems.numberFormatException"), e);
            }
        }
        value = initParameters.get("display-refresh-time");
        if (value != null && value.size() > 0) {
            try {
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.parsingDisplayRefreshTime") + value);
                defaultDisplayRefreshTime = Long.parseLong(value.get(0));
                if (defaultDisplayRefreshTime < 0) {
                    Feeder.log_.warn(Feeder.messages_.getString("feeder.log.warn.invalidDisplayRefreshTime"));
                    defaultDisplayRefreshTime = 5 * Utils.MULTIPLIER_FOR_MINUTES_TO_SECONDS;
                }
            } catch (NumberFormatException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.displayRefreshTime.numberFormatException"), e);
            }
        }
        Iterator it = defaultFeeds.keySet().iterator();
        while (it.hasNext()) {
            try {
                String feedURL = (String) it.next();
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.prefetchingFeed") + feedURL);
                cache.getFeed(feedURL, defaultFeeds.get(feedURL));
            } catch (FeedException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.feed.feedException"), e);
            } catch (IOException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.feed.ioException"), e);
            }
        }
    }

    public CiscoIPPhoneXMLObject processRequest(Map<String, String[]> requestParameters, Map<String, String> responseHeaders, Map<String, String> serverInfo, CIPPhone phone) {
        CiscoIPPhoneMenu cip = null;
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        List<MenuItem> menuItems = new ArrayList();
        String displayTitle = "";
        String[] requestFeedsArray = requestParameters.get("feed");
        String[] displayRefreshTimeArray = requestParameters.get("display-refresh-time");
        String[] maxItemsArray = requestParameters.get("max-items");
        Map<String, Long> requestFeeds = null;
        long displayRefreshTime = defaultDisplayRefreshTime;
        int maxItems = defaultMaxItems;
        if (maxItemsArray != null && maxItemsArray.length > 0 && maxItemsArray[0].length() > 0) {
            try {
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.parsingMaxItems") + maxItemsArray[0]);
                maxItems = Integer.parseInt(maxItemsArray[0]);
                if (maxItems > 100) maxItems = 100;
            } catch (NumberFormatException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.exception.numberFormatException.maxItems"), e);
            }
        }
        if (displayRefreshTimeArray != null && displayRefreshTimeArray.length > 0 && displayRefreshTimeArray[0].length() > 0) {
            try {
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.parsingdisplayRefreshTime") + displayRefreshTimeArray[0]);
                displayRefreshTime = Long.parseLong(displayRefreshTimeArray[0]);
            } catch (NumberFormatException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.displayRefreshTime.numberFormatException"), e);
            }
        }
        if (requestFeedsArray == null || requestFeedsArray.length == 0) {
            Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.noFeedsInRequest"));
            requestFeeds = defaultFeeds;
        } else {
            Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.feedsInRequest"));
            requestFeeds = splitFeedStrings(Arrays.asList(requestFeedsArray));
        }
        Iterator it = requestFeeds.keySet().iterator();
        while (it.hasNext()) {
            try {
                String feedURL = (String) it.next();
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.fetchingFeed") + feedURL);
                SyndFeed feed = cache.getFeed(feedURL, requestFeeds.get(feedURL));
                entries.addAll(feed.getEntries());
                displayTitle += feed.getTitle().trim() + "|";
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.displayTitle") + displayTitle);
            } catch (FeedException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.feed.feedException"), e);
            } catch (IOException e) {
                Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.feed.ioException"), e);
            }
        }
        Collections.sort(entries, feedDateComparator);
        Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.entriesSorted"));
        for (int i = 0; i < entries.size() && i < maxItems; i++) {
            SyndEntry currentEntry = entries.get(i);
            menuItems.add(new MenuItem(currentEntry.getTitle(), ""));
            Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.entryAdded") + i);
        }
        Feeder.log_.info(Feeder.messages_.getString("feeder.log.info.generatingCIP"));
        cip = new CiscoIPPhoneMenu(displayTitle, "", menuItems, defaultSoftKeyItems);
        setHeaders(responseHeaders, displayRefreshTime);
        return cip;
    }

    /**
     *
     * Sets the headers for the response
     *
     * @param headers The {@code Map} in which the headers are to be set
     *
     * @param refreshTime The refresh time to be set for the REFRESH header, in
     *                    seconds
     */
    private void setHeaders(Map<String, String> headers, long refreshTime) {
        Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.settingHeader") + Utils.HTTP_HEADER_CONTENT_TYPE + ":" + Utils.CONTENT_TYPE_XML);
        headers.put(Utils.HTTP_HEADER_CONTENT_TYPE, Utils.CONTENT_TYPE_XML);
        Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.settingHeader") + Utils.HTTP_HEADER_REFRESH + ":" + refreshTime);
        headers.put(Utils.HTTP_HEADER_REFRESH, refreshTime + "");
    }

    /**
     * Splits feed URL + refreshTime strings (in the format feedURL*refreshTime)
     *
     * @param feedURLs {@code String}s of the format {@code feedURL*refreshTime}
     *                 which are split on the *
     *
     * @return A {@code Map} of feed URLs (as {@code Strings}) and their refresh
     *         times in milliseconds
     */
    private Map<String, Long> splitFeedStrings(List<String> feedURLs) {
        Map<String, Long> splitFeeds = new HashMap<String, Long>();
        for (String feedParam : feedURLs) {
            Feeder.log_.debug(Feeder.messages_.getString("feeder.log.debug.splittingFeedParam") + feedParam);
            String[] feedParams = feedParam.split("\\*");
            if (feedParams.length == 2) {
                try {
                    splitFeeds.put(feedParams[0], new Long(feedParams[1]) * Utils.MULTIPLIER_FOR_MINUTES_TO_MILLISECONDS);
                } catch (NumberFormatException e) {
                    Feeder.log_.error(Feeder.messages_.getString("feeder.log.error.displayRefreshTime.numberFormatException"), e);
                }
            } else {
                Feeder.log_.debug(Feeder.messages_.getString("feeder.log.warn.noDisplayRefreshTime") + 30 * Utils.MULTIPLIER_FOR_MINUTES_TO_MILLISECONDS);
                splitFeeds.put(feedParams[0], (long) (30 * Utils.MULTIPLIER_FOR_MINUTES_TO_MILLISECONDS));
            }
        }
        return splitFeeds;
    }
}
