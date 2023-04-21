package syndication.rss;

import java.util.Vector;
import syndication.ElementData;
import syndication.MalformedFeedElementException;

/**
 * Subordinate to the <rss> element is a single <channel> element, which
 * contains information about the channel (metadata) and its contents.
 * @author Omistaja
 */
public class Channel {

    private String title = null;

    private String link = null;

    private String description = null;

    private String language = null;

    private String copyright = null;

    private String managingEditor = null;

    private String webMaster = null;

    private String pubDate = null;

    private String lastBuildDate = null;

    private String category = null;

    private String generator = null;

    private String docs = null;

    private String cloud = null;

    private String ttl = null;

    private String image = null;

    private String rating = null;

    private String textInput = null;

    private String skipHours = null;

    private String skipDays = null;

    private Vector<Item> items = null;

    public Channel(ElementData element) throws MalformedFeedElementException {
        if (element == null) {
            throw new MalformedFeedElementException("Null element data passed to constructor.");
        }
        int index = findChildElement("title", element);
        if (index < 0) {
            throw new MalformedFeedElementException("Missing mandatory element.");
        } else {
            title = element.getChildAt(index).getContents();
        }
        index = findChildElement("link", element);
        if (index < 0) {
            throw new MalformedFeedElementException("Missing mandatory element.");
        } else {
            link = element.getChildAt(index).getContents();
        }
        index = findChildElement("description", element);
        if (index < 0) {
            throw new MalformedFeedElementException("Missing mandatory element.");
        } else {
            description = element.getChildAt(index).getContents();
        }
        index = findChildElement("language", element);
        if (index > -1) {
            language = element.getChildAt(index).getContents();
        }
        index = findChildElement("copyright", element);
        if (index > -1) {
            copyright = element.getChildAt(index).getContents();
        }
        index = findChildElement("managingEditor", element);
        if (index > -1) {
            managingEditor = element.getChildAt(index).getContents();
        }
        index = findChildElement("webMaster", element);
        if (index > -1) {
            webMaster = element.getChildAt(index).getContents();
        }
        index = findChildElement("pubDate", element);
        if (index > -1) {
            pubDate = element.getChildAt(index).getContents();
        }
        index = findChildElement("lastBuildDate", element);
        if (index > -1) {
            lastBuildDate = element.getChildAt(index).getContents();
        }
        index = findChildElement("category", element);
        if (index > -1) {
            category = element.getChildAt(index).getContents();
        }
        index = findChildElement("generator", element);
        if (index > -1) {
            generator = element.getChildAt(index).getContents();
        }
        index = findChildElement("docs", element);
        if (index > -1) {
            docs = element.getChildAt(index).getContents();
        }
        index = findChildElement("cloud", element);
        if (index > -1) {
            cloud = element.getChildAt(index).getContents();
        }
        index = findChildElement("ttl", element);
        if (index > -1) {
            ttl = element.getChildAt(index).getContents();
        }
        index = findChildElement("image", element);
        if (index > -1) {
            image = element.getChildAt(index).getContents();
        }
        index = findChildElement("rating", element);
        if (index > -1) {
            rating = element.getChildAt(index).getContents();
        }
        index = findChildElement("textInput", element);
        if (index > -1) {
            textInput = element.getChildAt(index).getContents();
        }
        index = findChildElement("skipHours", element);
        if (index > -1) {
            skipHours = element.getChildAt(index).getContents();
        }
        index = findChildElement("skipDays", element);
        if (index > -1) {
            skipDays = element.getChildAt(index).getContents();
        }
        if (findChildElement("item", element) > -1) {
            items = new Vector<Item>();
            appendItems(element);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public String getCategory() {
        return category;
    }

    public String getGenerator() {
        return generator;
    }

    public String getDocs() {
        return docs;
    }

    public String getCloud() {
        return cloud;
    }

    public String getTtl() {
        return ttl;
    }

    public String getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }

    public String getTextInput() {
        return textInput;
    }

    public String getSkipHours() {
        return skipHours;
    }

    public String getSkipDays() {
        return skipDays;
    }

    public Vector<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String s = "Channel title: " + title + '\n';
        s += "Channel link: " + link + '\n';
        s += "Channel description: " + description + '\n';
        int itemCount = items.size();
        if (itemCount > 0) {
            for (int i = 0; i < itemCount; i++) {
                s += "Item #" + (i + 1) + '\n';
                Item item = items.elementAt(i);
                s += item.toString();
            }
        }
        return s;
    }

    private int findChildElement(String localName, ElementData element) {
        if (localName == null || element == null) {
            return -1;
        }
        int childCount = element.getChildCount();
        if (childCount == 0) {
            return -1;
        }
        for (int i = 0; i < childCount; i++) {
            ElementData child = element.getChildAt(i);
            if (child.getLocalName().equalsIgnoreCase(localName) == true) {
                return i;
            }
        }
        return -1;
    }

    private void appendItems(ElementData element) throws MalformedFeedElementException {
        int childCount = element.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ElementData child = element.getChildAt(i);
            if (child.getLocalName().equalsIgnoreCase("item") == true) {
                Item item = new Item(child);
                items.add(item);
            }
        }
    }
}
