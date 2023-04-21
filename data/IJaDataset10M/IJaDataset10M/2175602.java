package com.showdown.torrent.resolvers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.showdown.api.IEpisode;
import com.showdown.api.IShow;
import com.showdown.api.ITorrentInfo;
import com.showdown.api.IShow.SupportedShowProperties;
import com.showdown.api.impl.ShowDownManager;
import com.showdown.api.impl.TorrentInfo;
import com.showdown.api.rss.FeedManager;
import com.showdown.api.rss.IRssFeedInfo;
import com.showdown.api.rss.RssFeedElement;
import com.showdown.log.ShowDownLog;
import com.showdown.settings.IShowTorrentSettings;
import com.showdown.util.DateUtil;
import com.showdown.util.TorrentUtil;

/**
 * Class for resolving a list of {@link ITorrentInfo} instances for a given {@link IEpisode}
 * @author Mat DeLong
 */
public final class TorrentInfoResolver implements ITorrentInfoResolver {

    private static final File FEEDS_FILE = new File("data", "feeds.xml");

    private static final TorrentResolverFactory RESOLVER_FACTORY = new TorrentResolverFactory();

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM dd yyyy");

    /**
    * {@inheritDoc}
    */
    public ITorrentInfo getBestTorrent(IEpisode episode, boolean respectSettings) {
        ITorrentInfo info = null;
        List<ITorrentInfo> infos = getTorrentInfo(episode);
        Collections.sort(infos);
        IShowTorrentSettings settings = ShowDownManager.INSTANCE.getShowSettings(episode.getShow());
        if (respectSettings) {
            for (ITorrentInfo i : infos) {
                if (settings.isRespectedBy(i)) {
                    info = i;
                    break;
                }
            }
        } else if (!infos.isEmpty()) {
            info = infos.get(0);
        }
        return info;
    }

    /**
    * {@inheritDoc}
    */
    public List<URL> getTorrentSearchURLs(IEpisode episode) {
        List<URL> result = new ArrayList<URL>();
        for (IRssFeedInfo feedInfo : getRssFeedInfo()) {
            if (feedInfo.isEnabledForShow(episode.getShow())) {
                URL feed = resolveURL(feedInfo, episode);
                if (feed != null) {
                    result.add(feed);
                }
            }
        }
        return result;
    }

    /**
    * {@inheritDoc}
    */
    @SuppressWarnings("unchecked")
    public List<ITorrentInfo> getTorrentInfo(IEpisode episode) {
        List<ITorrentInfo> infos = new ArrayList<ITorrentInfo>();
        for (IRssFeedInfo feedInfo : getRssFeedInfo()) {
            if (feedInfo.isEnabledForShow(episode.getShow())) {
                URL feed = resolveURL(feedInfo, episode);
                Element channel = null;
                if (feed != null) {
                    channel = getChannelElement(feed);
                }
                if (channel != null) {
                    for (Iterator i = channel.elementIterator("item"); i.hasNext(); ) {
                        Element item = (Element) i.next();
                        ITorrentInfo info = getTorrentInfo(episode, item, feedInfo);
                        if (info != null) {
                            infos.add(info);
                        }
                    }
                }
            }
        }
        return infos;
    }

    private ITorrentInfo getTorrentInfo(IEpisode episode, Element item, IRssFeedInfo feedInfo) {
        ITorrentInfo info = null;
        String title = TorrentUtil.getValue(item, "title");
        String link = TorrentUtil.getValue(item, "link");
        String pubDate = TorrentUtil.getValue(item, "pubDate");
        if (episodeAirsBefore(episode, pubDate) && matchesEpisode(episode, title, false)) {
            ITorrentResolver resolver = RESOLVER_FACTORY.createResolver(link, item, feedInfo, episode);
            URL torrentUrl = resolver.getTorrentURL();
            if (torrentUrl != null) {
                info = new TorrentInfo(episode, TorrentUtil.getEpisodeIdentifyingName(episode), torrentUrl, feedInfo, resolver);
                resolver.getFileExtensions();
            }
        }
        return info;
    }

    private boolean episodeAirsBefore(IEpisode episode, String pubDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.US);
        if (episode != null && pubDate != null) {
            try {
                Date postDate = sdf.parse(pubDate);
                return postDate.after(episode.getAirDate());
            } catch (Exception ex) {
            }
        }
        return true;
    }

    private String getShowTitleRegex(IShow show) {
        String[] words = show.getName().toLowerCase().split("\\W");
        StringBuffer sb = new StringBuffer();
        for (String w : words) {
            if (w.length() > 0) {
                sb.append("([\\W||[_]]*)");
                if (w.matches("(\\w+)")) {
                    sb.append("(");
                    sb.append(w);
                    sb.append(")");
                }
            }
        }
        sb.append("(\\W)*");
        return sb.toString();
    }

    private boolean matchesEpisode(IEpisode episode, String title, boolean checkLatest) {
        title = title.toLowerCase();
        String titleRegex = getShowTitleRegex(episode.getShow());
        Pattern titlePattern = Pattern.compile(titleRegex);
        Matcher titleMatcher = titlePattern.matcher(title);
        if (!titleMatcher.find()) {
            return false;
        }
        if (Boolean.toString(true).equalsIgnoreCase(episode.getShow().getProperty(SupportedShowProperties.DAILY.getKey()))) {
            return matchesDailyEpisode(episode, title, checkLatest);
        }
        return matchesNonDailyEpisode(episode, title, checkLatest);
    }

    private boolean matchesDailyEpisode(IEpisode episode, String title, boolean checkLatest) {
        String regex1 = "((\\d{4})(\\.)(\\d{2})(\\.)(\\d{2}))";
        String regex2 = "((\\d{2})(\\.)(\\d{2})(\\.)(\\d{4}))";
        Pattern p1 = Pattern.compile(regex1);
        Pattern p2 = Pattern.compile(regex2);
        Matcher m1 = p1.matcher(title);
        Matcher m2 = p2.matcher(title);
        Date date = null;
        try {
            if (m1.find()) {
                String value = m1.group(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                date = sdf.parse(value);
            } else if (m2.find()) {
                String value = m2.group(1);
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
                date = sdf.parse(value);
            }
        } catch (Exception ex) {
        }
        if (date != null) {
            return DateUtil.isSameDay(date, episode.getAirDate());
        }
        return false;
    }

    private boolean matchesNonDailyEpisode(IEpisode episode, String title, boolean checkLatest) {
        String sMatch = "(season|s)(\\W)*";
        String eMatch = "(episode|ep|e)(\\W)*";
        String xMatch = "x";
        String seasonPatternString = sMatch + "(\\d+)";
        String episodePatternstring = eMatch + "(\\d+)";
        Pattern pX = Pattern.compile((new StringBuilder("(\\d)+")).append(xMatch).append("(\\d)+").toString());
        Pattern pNum = Pattern.compile("(\\d){3,4}");
        Matcher mX = pX.matcher(title);
        Matcher mNum = pNum.matcher(title);
        int seasonNumber = -1;
        int episodeNumber = -1;
        Pattern fullPattern = Pattern.compile(seasonPatternString + "(\\W)*" + episodePatternstring + "(\\W)+");
        Matcher fullPatternMatcher = fullPattern.matcher(title);
        if (fullPatternMatcher.find()) {
            String seasonNum = fullPatternMatcher.group(3);
            String episodeNum = fullPatternMatcher.group(7);
            try {
                seasonNumber = Integer.parseInt(seasonNum);
                episodeNumber = Integer.parseInt(episodeNum);
            } catch (Exception e) {
                return false;
            }
        } else if (mX.find()) {
            String x = mX.group();
            String splitX[] = x.split(xMatch);
            try {
                seasonNumber = Integer.parseInt(splitX[0]);
                episodeNumber = Integer.parseInt(splitX[1]);
            } catch (Exception e) {
                return false;
            }
        } else if (mNum.find() && !checkLatest) {
            String x = mNum.group();
            if (x.length() == 3) {
                seasonNumber = Integer.parseInt(x.substring(0, 1));
                episodeNumber = Integer.parseInt(x.substring(1, 3));
            } else if (x.length() == 4) {
                seasonNumber = Integer.parseInt(x.substring(0, 2));
                episodeNumber = Integer.parseInt(x.substring(2, 4));
            }
        } else {
            return false;
        }
        return seasonNumber == episode.getSeason() && episodeNumber == episode.getEpisodeNumber();
    }

    @SuppressWarnings("unchecked")
    private static Element getChannelElement(URL url) {
        Document document = getDocument(url);
        if (document != null) {
            Element root = document.getRootElement();
            if (root != null) {
                for (Iterator i = root.elementIterator("channel"); i.hasNext(); ) {
                    Element item = (Element) i.next();
                    return item;
                }
            }
        }
        return null;
    }

    private static Document getDocument(URL url) {
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(url);
        } catch (Exception ex) {
        }
        return document;
    }

    /**
    * {@inheritDoc}
    */
    public List<IRssFeedInfo> getRssFeedInfo() {
        FeedManager fm = new FeedManager(FEEDS_FILE);
        return fm.getFeeds();
    }

    private String getPaddedInteger(int number, int minDigits) {
        if (minDigits < 2) return String.valueOf(number);
        return String.format("%0" + minDigits + "d", number);
    }

    private URL resolveURL(IRssFeedInfo info, IEpisode episode) {
        URL url = null;
        if (info != null && episode != null) {
            String urlString = info.getURL();
            urlString = urlString.replaceAll("##SHOW_NAME##", episode.getShow().getName());
            urlString = urlString.replaceAll("##SNUM##", Integer.toString(episode.getSeason()));
            urlString = urlString.replaceAll("##ENUM##", Integer.toString(episode.getEpisodeNumber()));
            urlString = urlString.replaceAll("##P_SNUM##", getPaddedInteger(episode.getSeason(), 2));
            urlString = urlString.replaceAll("##P_ENUM##", getPaddedInteger(episode.getEpisodeNumber(), 2));
            if (episode.getAirDate() != null) {
                urlString.replaceAll("##DATE##", SDF.format(episode.getAirDate()));
            }
            if (!urlString.startsWith("http://")) {
                urlString = "http://" + urlString;
            }
            try {
                url = new URL(urlString);
            } catch (Exception ex) {
            }
        }
        return url;
    }

    private void storeFeeds(List<IRssFeedInfo> feeds) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("feeds");
        for (IRssFeedInfo feed : feeds) {
            Element element = root.addElement("feed");
            element.addAttribute("name", feed.getName());
            Element child = element.addElement("url");
            child.setText(feed.getURL());
            if (feed.getTorrentURL() != null) {
                child = element.addElement("torrenturl");
                child.addAttribute("element", feed.getTorrentURL().getElement());
                child.addAttribute("url", feed.getTorrentURL().getUrl());
            }
            if (feed.getSeeds() != null) {
                child = element.addElement("seeds");
                child.addAttribute("element", feed.getSeeds().getElement());
                child.addAttribute("regex", feed.getSeeds().getRegex());
                child.addAttribute("group", Integer.toString(feed.getSeeds().getGroup()));
            }
            if (feed.getLeeches() != null) {
                child = element.addElement("leeches");
                child.addAttribute("element", feed.getLeeches().getElement());
                child.addAttribute("regex", feed.getLeeches().getRegex());
                child.addAttribute("group", Integer.toString(feed.getLeeches().getGroup()));
            }
            if (feed.getSize() != null) {
                child = element.addElement("size");
                child.addAttribute("element", feed.getSize().getElement());
                child.addAttribute("regex", feed.getSize().getRegex());
                child.addAttribute("group", Integer.toString(feed.getSize().getGroup()));
                child.addAttribute("units", feed.getSize().getUnits().getName());
            }
            for (RssFeedElement i : feed.getGoodComments()) {
                child = element.addElement("goodcomments");
                child.addAttribute("element", i.getElement());
                child.addAttribute("url", i.getUrl());
                child.addAttribute("regex", i.getRegex());
                child.addAttribute("group", Integer.toString(i.getGroup()));
            }
            for (RssFeedElement i : feed.getBadComments()) {
                child = element.addElement("badcomments");
                child.addAttribute("element", i.getElement());
                child.addAttribute("url", i.getUrl());
                child.addAttribute("regex", i.getRegex());
                child.addAttribute("group", Integer.toString(i.getGroup()));
            }
        }
        writeDocument(document, FEEDS_FILE);
    }

    private void writeDocument(Document document, File file) {
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();
        } catch (Exception ex) {
            ShowDownLog.getInstance().logError(ex.getLocalizedMessage(), ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    public void addFeed(IRssFeedInfo feed) {
        List<IRssFeedInfo> feeds = getRssFeedInfo();
        if (!feeds.contains(feed)) {
            feeds.add(feed);
        } else {
            int index = feeds.indexOf(feed);
            feeds.remove(index);
            feeds.add(index, feed);
        }
        storeFeeds(feeds);
    }

    /**
    * {@inheritDoc}
    */
    public void removeFeed(IRssFeedInfo feed) {
        List<IRssFeedInfo> feeds = getRssFeedInfo();
        if (feed != null && feeds.contains(feed) && !feed.getName().equalsIgnoreCase("ISO Hunt")) {
            feeds.remove(feed);
            storeFeeds(feeds);
        }
    }
}
