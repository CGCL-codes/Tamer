package com.moogleapps.hello1.client;

import com.google.gwt.accounts.client.AuthSubStatus;
import com.google.gwt.accounts.client.User;
import com.google.gwt.gdata.client.atom.Category;
import com.google.gwt.gdata.client.atom.Text;
import com.google.gwt.gdata.client.blogger.BlogEntry;
import com.google.gwt.gdata.client.blogger.BlogFeed;
import com.google.gwt.gdata.client.blogger.BlogFeedCallback;
import com.google.gwt.gdata.client.blogger.BlogPostFeed;
import com.google.gwt.gdata.client.blogger.BlogPostFeedCallback;
import com.google.gwt.gdata.client.blogger.BloggerService;
import com.google.gwt.gdata.client.blogger.PostEntry;
import com.google.gwt.gdata.client.blogger.PostEntryCallback;
import com.google.gwt.gdata.client.impl.CallErrorException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The following example demonstrates how to update a blog post.
 */
public class BloggerUpdateBlogPostDemo extends GDataDemo {

    /**
   * This method is used by the main sample app to obtain
   * information on this sample and a sample instance.
   * 
   * @return An instance of this demo.
   */
    public static GDataDemoInfo init() {
        return new GDataDemoInfo() {

            @Override
            public GDataDemo createInstance() {
                return new BloggerUpdateBlogPostDemo();
            }

            @Override
            public String getDescription() {
                return "<p>This sample updates the first blog post it finds that " + "begins with the prefix 'GWT-Blogger-Client'.</p>";
            }

            @Override
            public String getName() {
                return "Blogger - Updating a blog post";
            }
        };
    }

    private BloggerService service;

    private FlexTable mainPanel;

    private final String scope = "http://www.blogger.com/feeds/";

    /**
   * Setup the Blogger service and create the main content panel.
   * If the user is not logged on to Blogger display a message,
   * otherwise start the demo by retrieving the user's blogs.
   */
    public BloggerUpdateBlogPostDemo() {
        service = BloggerService.newInstance("HelloGData_Blogger_UpdateBlogPostDemo_v1.0");
        mainPanel = new FlexTable();
        initWidget(mainPanel);
        if (User.getStatus(scope) == AuthSubStatus.LOGGED_IN) {
            Button startButton = new Button("Update a blog post");
            startButton.addClickListener(new ClickListener() {

                public void onClick(Widget sender) {
                    getBlogs("http://www.blogger.com/feeds/default/blogs");
                }
            });
            mainPanel.setWidget(0, 0, startButton);
        } else {
            showStatus("You are not logged on to Blogger.", true);
        }
    }

    /**
   * Retrieve the Blogger blogs feed using the Blogger service and
   * the blogs feed uri. In GData all get, insert, update and delete methods
   * always receive a callback defining success and failure handlers.
   * Here, the failure handler displays an error message while the
   * success handler obtains the first Blog entry and
   * calls getPosts to retrieve the posts feed for that blog.
   * 
   * @param blogsFeedUri The uri of the blogs feed
   */
    private void getBlogs(String blogsFeedUri) {
        showStatus("Loading blog feed...", false);
        service.getBlogFeed(blogsFeedUri, new BlogFeedCallback() {

            public void onFailure(CallErrorException caught) {
                showStatus("An error occurred while retrieving the Blogger Blog " + "feed: " + caught.getMessage(), true);
            }

            public void onSuccess(BlogFeed result) {
                BlogEntry[] entries = result.getEntries();
                if (entries.length == 0) {
                    showStatus("You have no Blogger blogs.", false);
                } else {
                    BlogEntry targetBlog = entries[0];
                    String postsFeedUri = targetBlog.getEntryPostLink().getHref();
                    getPosts(postsFeedUri);
                }
            }
        });
    }

    /**
   * Retrieve the Blogger posts feed using the Blogger service and
   * the posts feed uri for a given blog.
   * On success, identify the first post entry with a title starting
   * with "GWT-Blogger-Client", this will be the post that will be updated.
   * If no post is found, display a message.
   * Otherwise call updatePost to update the post.
   * 
   * @param postsFeedUri The posts feed uri for a given blog
   */
    private void getPosts(String postsFeedUri) {
        showStatus("Loading posts feed...", false);
        service.getBlogPostFeed(postsFeedUri, new BlogPostFeedCallback() {

            public void onFailure(CallErrorException caught) {
                showStatus("An error occurred while retrieving the Blogger Posts " + "feed: " + caught.getMessage(), true);
            }

            public void onSuccess(BlogPostFeed result) {
                PostEntry targetPost = null;
                for (PostEntry entry : result.getEntries()) {
                    String title = entry.getTitle().getText();
                    if (title.startsWith("GWT-Blogger-Client")) {
                        targetPost = entry;
                        break;
                    }
                }
                if (targetPost == null) {
                    showStatus("Did not find a post entry whose title starts with " + "the prefix 'GWT-Blogger-Client'.", false);
                } else {
                    updatePost(targetPost);
                }
            }
        });
    }

    /**
   * Displays a status message to the user.
   * 
   * @param message The message to display.
   * @param isError Indicates whether the status is an error status.
   */
    private void showStatus(String message, boolean isError) {
        mainPanel.clear();
        mainPanel.insertRow(0);
        mainPanel.addCell(0);
        Label msg = new Label(message);
        if (isError) {
            msg.setStylePrimaryName("hm-error");
        }
        mainPanel.setWidget(0, 0, msg);
    }

    /**
   * Update a blog post by making use of the updateEntry
   * method of the Entry class.
   * Set the post's title and contents to an arbitrary string. Here
   * we prefix the title with 'GWT-Blogger-Client' so that
   * we can identify which posts were updated by this demo.
   * We also update the name of one of the post's categories.
   * On success and failure, display a status message.
   * 
   * @param postEntry The post entry which to update
   */
    private void updatePost(PostEntry postEntry) {
        showStatus("Updating post entry...", false);
        postEntry.getTitle().setText("GWT-Blogger-Client - updated post");
        postEntry.setContent(Text.newInstance());
        postEntry.getContent().setText("My updated post");
        Category[] categories = postEntry.getCategories();
        for (Category category : categories) {
            if (category.getTerm().equals("Label1")) {
                category.setTerm("Label1-updated");
            }
        }
        postEntry.updateEntry(new PostEntryCallback() {

            public void onFailure(CallErrorException caught) {
                showStatus("An error occurred while updating a blog post: " + caught.getMessage(), true);
            }

            public void onSuccess(PostEntry result) {
                showStatus("Updated a blog entry.", false);
            }
        });
    }
}
