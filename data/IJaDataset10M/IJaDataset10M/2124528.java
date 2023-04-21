package org.arasso.store;

import junit.framework.*;
import org.arasso.domain.FeedList;
import org.arasso.domain.Post;
import org.arasso.util.Aggregator;

/**
 *
 * @author fdiotalevi
 */
public class TestPostStore extends TestCase {

    public TestPostStore(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testPostStore() {
        PostStore postStore = new PostStore("example", new Aggregator(new FeedList()));
        assertNotNull(postStore.getPosts());
        assert (postStore.getPosts().size() > 0);
        for (Post post : postStore.getPosts()) System.err.println(post);
    }
}
