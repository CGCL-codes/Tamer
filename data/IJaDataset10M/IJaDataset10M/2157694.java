package harris.GiantBomb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

/**
 * This class parses the feed given to it, and returns a list of video objects
 * 
 */
public class NewsFeedParser implements api {

    private final URL feedUrl;

    private SimpleDateFormat dateFromFormater = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    private SimpleDateFormat dateToFormater = new SimpleDateFormat("MMM. d, yyyy");

    public NewsFeedParser(String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<News> parse() {
        final News currentNews = new News();
        RootElement root = new RootElement("rss");
        final List<News> news = new ArrayList<News>();
        Element channel = root.getChild("channel");
        Element item = channel.getChild("item");
        item.setEndElementListener(new EndElementListener() {

            public void end() {
                news.add(currentNews.copy());
            }
        });
        item.getChild("title").setEndTextElementListener(new EndTextElementListener() {

            public void end(String body) {
                body = StringEscapeUtils.unescapeHtml(body);
                currentNews.setTitle(body.trim());
            }
        });
        item.getChild("link").setEndTextElementListener(new EndTextElementListener() {

            public void end(String body) {
                currentNews.setLink(body);
            }
        });
        item.getChild("description").setEndTextElementListener(new EndTextElementListener() {

            public void end(String body) {
                body = StringEscapeUtils.unescapeHtml(body);
                currentNews.setContent(body);
            }
        });
        item.getChild("http://purl.org/dc/elements/1.1/", "creator").setEndTextElementListener(new EndTextElementListener() {

            public void end(String body) {
                currentNews.setAuthor(body);
            }
        });
        item.getChild("pubDate").setEndTextElementListener(new EndTextElementListener() {

            public void end(String body) {
                body = StringEscapeUtils.unescapeHtml(body);
                currentNews.setPubdate(parseDate(body));
            }
        });
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return news;
    }

    private InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseDate(String dateStr) {
        Date d = null;
        try {
            d = dateFromFormater.parse(dateStr);
            return dateToFormater.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToFormater.format(new Date());
    }
}
