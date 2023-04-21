package models;

import java.util.*;
import siena.*;

public class Link extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    public String url;

    public String title;

    public String description;

    public Date created;

    public Date modified;

    @Filter("link")
    public Query<LinkTag> linktags;

    @Index("user_index")
    public User user;

    public Link(User user, String url) {
        super();
        this.url = url;
        this.user = user;
    }

    public Link() {
        super();
    }

    static Query<Link> all() {
        return Model.all(Link.class);
    }

    public static Link findById(Long id) {
        return all().filter("id", id).get();
    }

    public static List<Link> findByUser(User user) {
        return all().filter("user", user).order("-created").fetch();
    }

    public static void addTag(Link link, Tag tag) {
        LinkTag linkTag = new LinkTag(link, tag);
        linkTag.insert();
    }

    public String toString() {
        return url;
    }

    public List<Tag> findTagsByLink() {
        List<Tag> tags = LinkTag.findByLink(this);
        return tags;
    }

    public static void addTagsFromCSV(Link link, String tagcsv, User user) {
        Collection<Tag> tags = null;
        if (null != tagcsv || !tagcsv.equalsIgnoreCase("")) {
            String[] tagArr = tagcsv.split(",");
            tags = new ArrayList<Tag>();
            for (String tagstr : tagArr) {
                tagstr = play.templates.JavaExtensions.slugify(tagstr.trim()).trim();
                if (null != tagstr && !tagstr.equals("")) {
                    Link.addTag(link, Tag.findOrCreateByName(tagstr, user));
                }
            }
        }
    }
}
