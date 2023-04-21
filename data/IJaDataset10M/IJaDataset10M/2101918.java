package org.snipsnap.semanticweb.rss;

import org.snipsnap.snip.Blog;
import org.snipsnap.snip.SnipSpaceFactory;
import org.snipsnap.snip.Snip;
import org.snipsnap.feeder.Feeder;
import java.util.List;

public class BlogFeeder implements Feeder {

    private Blog blog;

    public BlogFeeder() {
        blog = SnipSpaceFactory.getInstance().getBlog();
    }

    public BlogFeeder(String blogName) {
        blog = SnipSpaceFactory.getInstance().getBlog(blogName);
    }

    public String getName() {
        return "blog";
    }

    public List getFeed() {
        return blog.getFlatPosts();
    }

    ;

    public Snip getContextSnip() {
        return blog.getSnip();
    }
}
