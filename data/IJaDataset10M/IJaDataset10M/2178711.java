package net.spy.digg.parsers;

import java.io.Serializable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.spy.digg.Story;
import net.spy.digg.Thumbnail;
import net.spy.digg.Topic;
import net.spy.digg.TopicContainer;
import net.spy.digg.User;

/**
 * A Digg story.
 */
public class StoryImpl implements Story, Serializable {

    private final User user;

    private final TopicContainer container;

    private final Topic topic;

    private final Thumbnail thumbnail;

    private final int id;

    private final String link;

    private final String diggLink;

    private final String status;

    private final String media;

    private final long promoteDate;

    private final String title;

    private final String description;

    private final long submitDate;

    private final int diggs;

    private final int comments;

    StoryImpl(Node n) {
        super();
        id = Integer.parseInt(BaseParser.getAttr(n, "id"));
        link = BaseParser.getAttr(n, "link");
        submitDate = 1000 * Long.parseLong(BaseParser.getAttr(n, "submit_date"));
        diggs = Integer.parseInt(BaseParser.getAttr(n, "diggs"));
        comments = Integer.parseInt(BaseParser.getAttr(n, "comments"));
        diggLink = BaseParser.getAttr(n, "href");
        status = BaseParser.getAttr(n, "status");
        media = BaseParser.getAttr(n, "media");
        String pdStr = BaseParser.getAttr(n, "promote_date");
        promoteDate = pdStr == null ? -1 : 1000 * Long.parseLong(pdStr);
        String t = null;
        String d = null;
        User u = null;
        Topic top = null;
        TopicContainer tc = null;
        Thumbnail tn = null;
        final NodeList nl = n.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            final Node cn = nl.item(i);
            final String nm = cn.getNodeName();
            if (nm.equals("title")) {
                t = cn.getFirstChild().getNodeValue();
            } else if (nm.equals("description")) {
                Node node = cn.getFirstChild();
                if (node != null) {
                    d = node.getNodeValue();
                } else {
                    d = null;
                }
            } else if (nm.equals("user")) {
                u = new UserImpl(cn);
            } else if (nm.equals("topic")) {
                top = new TopicImpl(BaseParser.getAttr(cn, "name"), BaseParser.getAttr(cn, "short_name"));
            } else if (nm.equals("container")) {
                tc = new TopicContainerImpl(BaseParser.getAttr(cn, "name"), BaseParser.getAttr(cn, "short_name"));
            } else if (nm.equals("thumbnail")) {
                tn = new ThumbnailImpl(BaseParser.getAttr(cn, "src"), BaseParser.getAttr(cn, "contentType"), Integer.parseInt(BaseParser.getAttr(cn, "originalwidth")), Integer.parseInt(BaseParser.getAttr(cn, "originalheight")), Integer.parseInt(BaseParser.getAttr(cn, "width")), Integer.parseInt(BaseParser.getAttr(cn, "height")));
            } else if (cn.getNodeType() == Node.TEXT_NODE) {
            } else {
                assert false : "Unexpected node: " + cn;
            }
        }
        title = t;
        description = d;
        user = u;
        topic = top;
        container = tc;
        thumbnail = tn;
        container.add(topic);
        ((TopicImpl) topic).setContainerName(container.getShortName());
    }

    public int getComments() {
        return comments;
    }

    public TopicContainer getContainer() {
        return container;
    }

    public String getDescription() {
        return description;
    }

    public String getDiggLink() {
        return diggLink;
    }

    public int getDiggs() {
        return diggs;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }

    public long getSubmittedTimestamp() {
        return submitDate;
    }

    public String getTitle() {
        return title;
    }

    public Topic getTopic() {
        return topic;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "{Story id=" + id + " ``" + title + "''}";
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getMedia() {
        return media;
    }

    public long getPromoteTimestamp() {
        return promoteDate;
    }
}
