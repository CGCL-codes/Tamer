package com.alexmcchesney.poster.plugins.delicious;

import java.net.*;
import java.util.Date;
import com.alexmcchesney.operations.*;
import com.alexmcchesney.poster.*;
import com.alexmcchesney.poster.plugins.delicious.gui.GUIStringResources;
import com.alexmcchesney.poster.post.Post;
import com.alexmcchesney.poster.plugins.delicious.postcache.*;
import com.alexmcchesney.delicious.*;

/**
 * Asynchronous operation for deleting posts.
 * @author amcchesney
 *
 */
public class DeleteOperation extends DeliciousPluginOperation implements IAsynchronousOperation {

    /** The actual posts to delete */
    private Post[] m_posts = null;

    /** The account we're deleting from */
    private DeliciousAccount m_account = null;

    /**
	 * Constructor
	 * @param posts	Posts to delete
	 * @param account Account we're deleting from
	 */
    public DeleteOperation(Post[] posts, DeliciousAccount account) {
        m_posts = posts;
        m_account = account;
    }

    /**
	 * Actually performs the deletion
	 */
    public void doOperation() throws OperationCancelledException, InterruptedException, MalformedURLException, com.alexmcchesney.poster.operations.exceptions.AuthenticationException, com.alexmcchesney.poster.operations.exceptions.IOException, com.alexmcchesney.poster.operations.exceptions.PostException, NotDeliciousPostException, LoadConfigException, SaveConfigException, UserDirectoryNotFoundException, DeliciousException {
        cancellationCheck();
        PostCache cache = m_account.getPostCache();
        synchronized (cache) {
            Service deliciousService = m_account.getDeliciousService();
            int iTotalPosts = m_posts.length;
            for (int i = 0; i < iTotalPosts; i++) {
                cancellationCheck();
                m_status.appendToLog("DELETING_POSTS", new String[] { Integer.toString(i + 1), Integer.toString(iTotalPosts) });
                m_status.setCompletionPercentage((100 / iTotalPosts) * i);
                deliciousService.deletePost(m_posts[i].getURL());
                cache.removeFromCache((DeliciousPost) m_posts[i]);
                m_posts[i].onDelete();
            }
            cache.save();
            Date lastUpdate = deliciousService.getLastUpdate();
            cache.setLastUpdateTime(lastUpdate);
            cache.saveCacheProperties();
        }
    }

    /**
	 * Should return a short description of the operation, to appear
	 * in any progress dialog.  For example "Getting posts"
	 * @return	String containing a brief description of the operation.
	 */
    public String getOperationDescription() {
        return GUIStringResources.gui.getString("DELETING");
    }
}
