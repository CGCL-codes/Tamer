package org.arasso.store;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.arasso.domain.Post;
import org.arasso.util.Aggregator;

/**
 *
 * @author fdiotalevi
 */
public class PostStore {

    private String name;

    private List<Post> posts = new ArrayList<Post>();

    private Aggregator aggregator;

    /** Creates a new instance of PostStore */
    public PostStore(String name, Aggregator aggregator) {
        this.name = name;
        this.aggregator = aggregator;
        loadPosts();
    }

    public PostStore() {
    }

    private void loadPosts() {
        try {
            posts.addAll(aggregator.aggregate());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }
}
