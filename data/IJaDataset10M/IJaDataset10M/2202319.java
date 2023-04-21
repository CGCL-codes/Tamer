package blogger;

import java.io.IOException;
import java.net.URL;
import java.util.TimeZone;
import wp.Blog;
import wp.Comment;
import wp.Post;
import wp.PostStatus;
import com.google.gdata.client.GoogleService;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.ServiceException;

public class BloggerClient {

    private static final String META_FEED_URL = "http://www.blogger.com/feeds/default/blogs";

    private static final String FEED_BASE = "http://www.blogger.com/feeds";

    private static final String POST_SUFFIX = "/posts/default";

    private static final String COMMENT_SUFFIX = "/comments/default";

    private GoogleService service;

    private Person author;

    private String blogId;

    private String feedUri;

    public void login(String userName, String password) throws IOException, ServiceException {
        this.service = new GoogleService("blogger", "forth-wp2blogger-1");
        this.service.setUserCredentials(userName, password);
        Feed feed = this.service.getFeed(new URL(META_FEED_URL), Feed.class);
        this.author = feed.getAuthors().get(0);
        if (feed.getEntries().size() > 0) {
            Entry entry = feed.getEntries().get(0);
            blogId = entry.getId().split("blog-")[1];
        } else {
            throw new IOException("User " + userName + " has no blog.");
        }
        this.feedUri = FEED_BASE + "/" + blogId;
    }

    public Entry createPost(Post post) throws IOException, ServiceException {
        Entry newPost = new Entry();
        newPost.getAuthors().add(author);
        newPost.setTitle(new PlainTextConstruct(post.getTitle()));
        newPost.setContent(new PlainTextConstruct(post.getContent()));
        newPost.setDraft(post.getStatus() == PostStatus.DRAFT ? true : false);
        newPost.setPublished(new DateTime(post.getCreatedAt(), TimeZone.getDefault()));
        Category category = new Category();
        category.setTerm(post.getCategory());
        newPost.getCategories().add(category);
        newPost = service.insert(new URL(feedUri + POST_SUFFIX), newPost);
        String[] tokens = newPost.getSelfLink().getHref().split("/");
        String postId = tokens[tokens.length - 1];
        for (Comment c : post.getComments()) {
            createComment(postId, c);
        }
        return newPost;
    }

    public Entry createComment(String postId, Comment comment) throws IOException, ServiceException {
        StringBuilder sb = new StringBuilder();
        if (comment.getAuthor() != null && comment.getAuthor().length() > 0) {
            sb.append("Author: " + comment.getAuthor()).append("\n");
        }
        if (comment.getEmail() != null && comment.getEmail().length() > 0) {
            sb.append("Email: " + comment.getEmail()).append("\n");
        }
        if (comment.getUrl() != null && comment.getUrl().length() > 0) {
            sb.append("URL: " + comment.getUrl()).append("\n");
        }
        sb.append(comment.getContent());
        Entry newComment = new Entry();
        newComment.setContent(new PlainTextConstruct(sb.toString()));
        newComment.setPublished(new DateTime(comment.getCreatedAt(), TimeZone.getDefault()));
        return service.insert(new URL(feedUri + "/" + postId + COMMENT_SUFFIX), newComment);
    }

    public void deleteAllPosts() throws IOException, ServiceException {
        Feed feed = this.service.getFeed(new URL(feedUri + POST_SUFFIX), Feed.class);
        for (Entry entry : feed.getEntries()) {
            this.service.delete(new URL(entry.getEditLink().getHref()));
        }
    }

    public void sync(Blog blog) throws IOException, ServiceException {
        int count = 1;
        int total = blog.getPosts().size();
        for (Post post : blog.getPosts()) {
            System.out.println(count++ + "/" + total + ": " + post.getTitle());
            createPost(post);
        }
    }
}
