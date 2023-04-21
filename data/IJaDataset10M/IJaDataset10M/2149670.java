package com.showdown.util;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.dom4j.Element;
import com.showdown.api.IEpisode;
import com.showdown.api.IShow;
import com.showdown.api.IShow.SupportedShowProperties;

/**
 * Utility class for working with Torrent files
 * @author Mat DeLong
 */
public final class TorrentUtil {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd");

    private TorrentUtil() {
    }

    /**
    * Returns true if the given show is a daily show, false otherwise
    * @param show the show to check
    * @return true if the show is daily, false otherwise
    */
    public static boolean isDaily(IShow show) {
        final String val = show.getProperty(SupportedShowProperties.DAILY.getKey());
        return val != null && (val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("true"));
    }

    /**
    * Returns an identifying string for the episode
    * @return an identifying string for the episode
    */
    public static String getEpisodeIdentifyingName(IEpisode episode) {
        String name = episode.getShow().getName();
        name = name.replaceAll("[/:&*?|\"\\\\]", "");
        name = name.replaceAll(" ()", ".");
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(" - ");
        if (isDaily(episode.getShow()) && episode.getAirDate() != null) {
            sb.append(SDF.format(episode.getAirDate()));
        } else {
            sb.append("S");
            sb.append(episode.getSeason());
            sb.append("E");
            sb.append(episode.getEpisodeNumber());
        }
        return sb.toString();
    }

    /**
    * Helper method to get the text value of the first child element of the given name.
    * This is useful, for example, if you want to get the 'title' value for an RSS item.
    * @param the RSS element to get the child element text value for
    * @param childItem the name of the child element to get the text value for
    */
    @SuppressWarnings("unchecked")
    public static String getValue(Element element, String childItem) {
        for (Iterator i = element.elementIterator(childItem); i.hasNext(); ) {
            Element item = (Element) i.next();
            return item.getText();
        }
        return null;
    }
}
