package com.gnizr.web.action.clustermap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.gnizr.core.exceptions.NoSuchUserException;
import com.gnizr.core.tag.TagManager;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.BookmarkTag;
import com.gnizr.db.dao.Tag;
import com.gnizr.web.action.AbstractLoggedInUserAction;

public class ClusterUserFolder extends AbstractLoggedInUserAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2955458844057527635L;

    private static final Logger logger = Logger.getLogger(ClusterUserFolder.class);

    private TagManager tagManager;

    private String folderName;

    private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

    private Map<String, List<Integer>> cluster = new HashMap<String, List<Integer>>();

    private List<Tag> tags = new ArrayList<Tag>();

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    protected String go() throws Exception {
        try {
            super.resolveUser();
        } catch (NoSuchUserException e) {
            logger.debug("no such user: " + getUser());
            addActionMessage(ClusterMapConstants.NO_SUCH_USER);
            return INPUT;
        }
        try {
            if (folderName != null && user != null) {
                List<BookmarkTag> bookmarkTags = tagManager.listBookmarkTagUserFolder(user, folderName);
                ClusterMapTool.clusterBookmarkTags(bookmarkTags, cluster, bookmarks, tags);
                return SUCCESS;
            }
        } catch (Exception e) {
            logger.debug(e);
            addActionMessage(ClusterMapConstants.NO_SUCH_FOLDER);
        }
        addActionMessage(ClusterMapConstants.NO_SUCH_USER);
        addActionMessage(ClusterMapConstants.NO_SUCH_FOLDER);
        return INPUT;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public Map<String, List<Integer>> getCluster() {
        return cluster;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public String doDefault() throws Exception {
        try {
            super.resolveUser();
        } catch (NoSuchUserException e) {
            logger.debug("no such user: " + getUser());
            return INPUT;
        }
        if (folderName == null || folderName.length() == 0) {
            return INPUT;
        }
        return SUCCESS;
    }

    @Override
    protected boolean isStrictLoggedInUserMode() {
        return false;
    }
}
