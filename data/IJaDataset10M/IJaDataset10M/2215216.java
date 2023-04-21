package net.sourceforge.blogentis.plugins.base;

import net.sourceforge.blogentis.om.Post;
import net.sourceforge.blogentis.plugins.IBlogExtension;
import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.AbsoluteLinkURL;

/**
 * @author abas
 */
public interface IPostEditExtension extends IBlogExtension {

    public static final String LOCATION_END_OF_PAGE = "PostEdit.End";

    public static final String LOCATION_START_OF_PAGE = "PostEdit.Before";

    public static final String LOCATION_END_OF_OPTIONS = "PostEdit.AfterOptions";

    public static final String LOCATION_START_OF_OPTIONS = "PostEdit.BeforeOptions";

    public static final String LOCATION_BEFORE_FULL_TEXT = "PostEdit.MiddleContent";

    /**
     * An action that can be performed on a post.
     * 
     * @author abas
     */
    public interface IPostAction extends ILinkTo {

        /**
         * Modify given link to perform the action.
         * 
         * @param data
         * @param post
         */
        public AbsoluteLinkURL makeLinkURI(BlogRunData data, Post post, AbsoluteLinkURL link);
    }

    /**
     * This method will be called for the creation of a new post. The post is
     * complete and only lacks saving to disk, which will happen right after
     * this method.
     * 
     * Note that on a new post that will be published immediately,
     * postPublicationStatusChanged() will be called imediately afterwards.
     * 
     * @param data
     * @param post
     *            the new Post.
     */
    public void postNew(BlogRunData data, Post post);

    /**
     * When a post's text or content has been changed by the user, this method
     * will be called. The old text has been thrown away by this point, and the
     * new post is about to be saved to disk.
     * 
     * @param data
     *            the current rundata
     * @param post
     *            the post that has been modified.
     */
    public void postModified(BlogRunData data, Post post);

    /**
     * This method will be called whenever a publication change has been
     * requested by the user. The current publication changes are defined as:
     * <ul>
     * <li>draft -&gt; published: when a user has finished editing a post and
     * publishes the specific post</lI>
     * <li>published -&gt; draft: removed from publication</li>
     * </ul>
     * 
     * @param data
     *            the rundata of the request.
     * @param post
     *            the post that will have its publication type changed.
     * @param oldType
     *            the old post type, from the PostPeer constants.
     */
    public void postPublicationStatusChanged(BlogRunData data, Post post, int oldType);

    /**
     * The user is about to delete a post. This method will be called before the
     * actual deletion from the database.
     * 
     * @param data
     *            The current RunData object.
     * @param post
     *            The post that the user has requested to delete.
     */
    public void postDeleted(BlogRunData data, Post post);

    /**
     * Get extra HTML that should be inserted in the post edit screen.
     * 
     * @param data
     *            the RunData of the current request
     * @param post
     *            the current post, may be null if the post is new.
     * @param location TODO
     * @return an HTML fragment that should be inserted the final page.
     */
    public String buildOptionsHTML(BlogRunData data, Post post, String location);
}
