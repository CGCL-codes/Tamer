package com.synchrona.communication;

import com.google.gdata.client.Query;
import com.google.gdata.client.blogger.BloggerService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;

/**
 * Demonstrates how to use the Google Data API's Java client library to
 * interface with the Blogger service. There are examples for the following
 * operations:
 * 
 * <ol>
 * <li>Retrieving the list of all the user's blogs</li>
 * <li>Retrieving all posts on a single blog</li>
 * <li>Performing a date-range query for posts on a blog</li>
 * <li>Creating draft posts and publishing posts</li>
 * <li>Updating posts</li>
 * <li>Retrieving comments</li>
 * <li>Deleting posts</li>
 * </ol>
 * 
 * 
 */
public class Blogger {

    private static final String METAFEED_URL = "http://www.blogger.com/feeds/default/blogs";

    private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";

    private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";

    private static final String COMMENTS_FEED_URI_SUFFIX = "/comments/default";

    private static String feedUri;

    /**
	 * Utility classes should not have a public or default constructor.
	 */
    public Blogger() {
    }

    /**
	 * Parses the metafeed to get the blog ID for the authenticated user's
	 * default blog.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @return A String representation of the blog's ID.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    private static String getBlogId(BloggerService myService) throws ServiceException, IOException {
        final URL feedUrl = new URL(METAFEED_URL);
        Feed resultFeed = (Feed) myService.getFeed(feedUrl, Feed.class);
        if (resultFeed.getEntries().size() > 0) {
            Entry entry = (Entry) resultFeed.getEntries().get(0);
            return entry.getId().split("blog-")[1];
        }
        throw new IOException("User has no blogs!");
    }

    /**
	 * Prints a list of all the user's blogs.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static void printUserBlogs(BloggerService myService) throws ServiceException, IOException {
        final URL feedUrl = new URL(METAFEED_URL);
        Feed resultFeed = (Feed) myService.getFeed(feedUrl, Feed.class);
        System.out.println(resultFeed.getTitle().getPlainText());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            Entry entry = (Entry) resultFeed.getEntries().get(i);
            System.out.println("\t" + entry.getTitle().getPlainText());
        }
        System.out.println();
    }

    /**
	 * Creates a new post on a blog. The new post can be stored as a draft or
	 * published based on the value of the isDraft paramter. The method creates
	 * an Entry for the new post using the title, content, authorName and
	 * isDraft parameters. Then it uses the given GoogleService to insert the
	 * new post. If the insertion is successful, the added post will be
	 * returned.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param title
	 *            Text for the title of the post to create.
	 * @param content
	 *            Text for the content of the post to create.
	 * @param authorName
	 *            Display name of the author of the post.
	 * @param userName
	 *            username of the author of the post.
	 * @param b
	 *            True to save the post as a draft, False to publish the post.
	 * @return An Entry containing the newly-created post.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static Entry createPost(BloggerService myService, String title, String content, String authorName, String userName, Boolean b) throws ServiceException, IOException {
        Entry myEntry = new Entry();
        myEntry.setTitle(new PlainTextConstruct(title));
        myEntry.setContent(new PlainTextConstruct(content));
        Person author = new Person(authorName, null, userName);
        myEntry.getAuthors().add(author);
        myEntry.setDraft(b);
        URL postUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
        return (Entry) myService.insert(postUrl, myEntry);
    }

    /**
	 * Displays the titles of all the posts in a blog. First it requests the
	 * posts feed for the blogs and then is prints the results.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static void printAllPosts(BloggerService myService) throws ServiceException, IOException {
        URL feedUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
        Feed resultFeed = (Feed) myService.getFeed(feedUrl, Feed.class);
        System.out.println(resultFeed.getTitle().getPlainText());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            Entry entry = (Entry) resultFeed.getEntries().get(i);
            System.out.println("\t" + entry.getTitle().getPlainText());
        }
        System.out.println();
    }

    /**
	 * Displays the title and modification time for any posts that have been
	 * created or updated in the period between the startTime and endTime
	 * parameters. The method creates the query, submits it to the
	 * GoogleService, then displays the results.
	 * 
	 * Note that while the startTime is inclusive, the endTime is exclusive, so
	 * specifying an endTime of '2007-7-1' will include those posts up until
	 * 2007-6-30 11:59:59PM.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param startTime
	 *            DateTime object specifying the beginning of the search period
	 *            (inclusive).
	 * @param endTime
	 *            DateTime object specifying the end of the search period
	 *            (exclusive).
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static void printDateRangeQueryResults(BloggerService myService, DateTime startTime, DateTime endTime) throws ServiceException, IOException {
        URL feedUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
        Query myQuery = new Query(feedUrl);
        myQuery.setUpdatedMin(startTime);
        myQuery.setUpdatedMax(endTime);
        Feed resultFeed = (Feed) myService.query(myQuery, Feed.class);
        System.out.println(resultFeed.getTitle().getPlainText() + " posts between " + startTime + " and " + endTime);
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            Entry entry = (Entry) resultFeed.getEntries().get(i);
            System.out.println("\t" + entry.getTitle().getPlainText());
            System.out.println("\t" + entry.getUpdated().toStringRfc822());
        }
        System.out.println();
    }

    /**
	 * Updates the title of the given post. The Entry object is updated with the
	 * new title, then a request is sent to the GoogleService. If the insertion
	 * is successful, the updated post will be returned.
	 * 
	 * Note that other characteristics of the post can also be modified by
	 * updating the values of the entry object before submitting the request.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param entryToUpdate
	 *            An Entry containing the post to update.
	 * @param newTitle
	 *            Text to use for the post's new title.
	 * @return An Entry containing the newly-updated post.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static Entry updatePostTitle(BloggerService myService, Entry entryToUpdate, String newTitle) throws ServiceException, IOException {
        entryToUpdate.setTitle(new PlainTextConstruct(newTitle));
        URL editUrl = new URL(entryToUpdate.getEditLink().getHref());
        return (Entry) myService.update(editUrl, entryToUpdate);
    }

    /**
	 * Adds a comment to the specified post. First the comment feed's URI is
	 * built using the given post ID. Then an Entry is created for the comment
	 * and submitted to the GoogleService.
	 * 
	 * NOTE: This functionality is not officially supported yet.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param postId
	 *            The ID of the post to comment on.
	 * @param commentText
	 *            Text to store in the comment.
	 * @return An entry containing the newly-created comment.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
    public static Entry createComment(BloggerService myService, String postId, String commentText) throws ServiceException, IOException {
        String commentsFeedUri = feedUri + "/" + postId + COMMENTS_FEED_URI_SUFFIX;
        URL feedUrl = new URL(commentsFeedUri);
        Entry myEntry = new Entry();
        myEntry.setContent(new PlainTextConstruct(commentText));
        return (Entry) myService.insert(feedUrl, myEntry);
    }

    /**
	 * Displays all the comments for the given post. First the comment feed's
	 * URI is built using the given post ID. Then the method requests the
	 * comments feed and displays the results.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param postId
	 *            The ID of the post to view comments on.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If there is an error communicating with the server.
	 */
    public static void printAllComments(BloggerService myService, String postId) throws ServiceException, IOException {
        String commentsFeedUri = feedUri + "/" + postId + COMMENTS_FEED_URI_SUFFIX;
        URL feedUrl = new URL(commentsFeedUri);
        Feed resultFeed = (Feed) myService.getFeed(feedUrl, Feed.class);
        System.out.println(resultFeed.getTitle().getPlainText());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            Entry entry = (Entry) resultFeed.getEntries().get(i);
            System.out.println("\t" + ((TextContent) entry.getContent()).getContent().getPlainText());
            System.out.println("\t" + entry.getUpdated().toStringRfc822());
        }
        System.out.println();
    }

    /**
	 * Removes the comment specified by the given editLinkHref.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param editLinkHref
	 *            The URI given for editing the comment.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If there is an error communicating with the server.
	 */
    public static void deleteComment(BloggerService myService, String editLinkHref) throws ServiceException, IOException {
        URL deleteUrl = new URL(editLinkHref);
        myService.delete(deleteUrl);
    }

    /**
	 * Removes the post specified by the given editLinkHref.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param editLinkHref
	 *            The URI given for editing the post.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If there is an error communicating with the server.
	 */
    public static void deletePost(BloggerService myService, String editLinkHref) throws ServiceException, IOException {
        URL deleteUrl = new URL(editLinkHref);
        myService.delete(deleteUrl);
    }

    /**
	 * Runs through all the examples using the given GoogleService instance.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @param userName
	 *            username of user to authenticate (e.g. jdoe@gmail.com).
	 * @param userPassword
	 *            password to use for authentication.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If there is an error communicating with the server.
	 */
    public static String getLastPostId(BloggerService myService) throws ServiceException, IOException {
        URL feedUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
        Feed resultFeed = (Feed) myService.getFeed(feedUrl, Feed.class);
        String postID = null;
        Entry entry = (Entry) resultFeed.getEntries().get(0);
        postID = entry.getId();
        return postID;
    }

    public static void run(BloggerService myService, String userName, String userPassword, String message, String type) throws ServiceException, IOException {
        myService.setUserCredentials(userName, userPassword);
        Boolean FALSE = new Boolean(false);
        String blogId = getBlogId(myService);
        feedUri = FEED_URI_BASE + "/" + blogId;
        if (type.equals("POST")) {
            Entry publicPost = createPost(myService, "Blog Post From Synchrona", "<p>" + message + "<p>", "Post author", userName, FALSE);
            System.out.println("Successfully created public post: " + publicPost.getTitle().getPlainText());
        } else {
        }
    }

    public static void run1(BloggerService myService, String userName, String userPassword, String message, String type) throws ServiceException, IOException {
        Boolean TRUE = new Boolean(true);
        Boolean FALSE = new Boolean(false);
        myService.setUserCredentials(userName, userPassword);
        String blogId = getBlogId(myService);
        feedUri = FEED_URI_BASE + "/" + blogId;
        printUserBlogs(myService);
        Entry draftPost = createPost(myService, "Snorkling in Aruba", "<p>We had so much fun snorkling in Aruba<p>", "Post author", userName, TRUE);
        System.out.println("Successfully created draft post: " + draftPost.getTitle().getPlainText());
        Entry publicPost = createPost(myService, "Back from vacation", message, "Post author", userName, FALSE);
        System.out.println("Successfully created public post: " + publicPost.getTitle().getPlainText());
        printAllPosts(myService);
        printDateRangeQueryResults(myService, DateTime.parseDate("2007-04-04"), DateTime.parseDate("2007-04-06"));
        draftPost.setTitle(new PlainTextConstruct("Swimming with the fish"));
        draftPost.update();
        System.out.println("Post's new title is \"" + draftPost.getTitle().getPlainText() + "\".\n");
        publicPost = updatePostTitle(myService, publicPost, "The party's over");
        System.out.println("Post's new title is \"" + publicPost.getTitle().getPlainText() + "\".\n");
        System.out.println("Creating comment");
        String selfLinkHref = publicPost.getSelfLink().getHref();
        String[] tokens = selfLinkHref.split("/");
        String postId = tokens[tokens.length - 1];
        Entry comment = createComment(myService, postId, "Did you see any sharks?");
        printAllComments(myService, postId);
    }

    /**
	 * Prints the command line usage of this sample application.
	 */
    private static void usage() {
        System.out.println("Usage: BloggerClient --username <username>" + " --password <password>");
        System.out.println("\nA simple application that creates, queries,\n" + "updates and deletes posts and comments on the\n" + "specified blog using the provided username and\n" + "password for authentication.");
    }

    public void updateBlogger(String update, String sender) {
        String userPassword = null;
        String username = null;
        username = "synchrona.group@gmail.com";
        userPassword = "pass123456";
        BloggerService myService = new BloggerService("exampleCo-exampleApp-1");
        try {
            run1(myService, username, userPassword, update, sender);
        } catch (ServiceException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
