package com.tysanclan.site.projectewok.beans;

import java.util.List;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupForum;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.User;

/**
 * A service that does various forum-related actions
 * 
 * @author Jeroen Steenbeeke
 */
public interface ForumService {

    /**
	 * Determine whether or not the given user can edit a post
	 * 
	 * @param user
	 *            The user to check
	 * @param post
	 *            The post to check for
	 * @return <code>true</code> if the user can edit the post,
	 *         <code>false</code> otherwise
	 */
    public boolean canEditPost(User user, ForumPost post);

    boolean canView(User user, Forum forum);

    /**
	 * Creates a new forumthread in the given forum
	 * 
	 * @param forum
	 *            The forum to post the thread in
	 * @param title
	 *            The title of the thread
	 * @param content
	 *            The content of the first post
	 * @param user
	 *            The user to make the post
	 * @return The created thread, or <code>null</code> if something went wrong.
	 */
    public ForumThread createForumThread(Forum forum, String title, String content, User user);

    /**
	 * Creates a new forumthread in the given forum
	 * 
	 * @param forum
	 *            The forum to post the thread in
	 * @param title
	 *            The title of the thread
	 * @param user
	 *            The user to make the post
	 * @return The created thread, or <code>null</code> if something went wrong.
	 */
    public ForumThread createEmptyForumThread(Forum forum, String title, User user);

    /**
	 * Deletes the given post
	 * 
	 * @param post
	 *            The post to delete
	 * @param user
	 *            The user doing the deletion
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean deletePost(ForumPost post, User user);

    /**
	 * Deletes a given thread, if allowed
	 * 
	 * @param thread
	 *            The thread to delete
	 * @param user
	 *            The user deleting the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean deleteThread(ForumThread thread, User user);

    /**
	 * Edit the given post, replacing the content with the indicated content
	 * 
	 * @param post
	 *            The post to edit
	 * @param editorContent
	 *            The content replacing the original content
	 * @param currentUser
	 *            The user doing the replacing
	 */
    public void editPost(ForumPost post, String editorContent, User currentUser);

    /**
	 * Get the forum used for posting news to
	 * 
	 * @return The forum used as news forum
	 */
    public Forum getNewsForum();

    /**
	 * Determines which forums are valid move destinations for the given forum
	 * and moderator
	 * 
	 * @param forum
	 *            The forum the thread is currently in
	 * @param u
	 *            The user performing the move
	 * @return A list of valid destination forums, empty if no such forum exists
	 */
    public List<Forum> getValidDestinationForums(Forum forum, User u);

    /**
	 * Determines if a given user is a moderator in the given forum
	 * 
	 * @param user
	 *            The user to check
	 * @param forum
	 *            The forum to check for
	 * @return <code>true</code> if the user is a moderator, <code>false</code>
	 *         otherwise
	 */
    public boolean isModerator(User user, Forum forum);

    /**
	 * Locks the given thread, if allowed
	 * 
	 * @param thread
	 *            The thread to lock
	 * @param user
	 *            The user locking the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean lockThread(ForumThread thread, User user);

    /**
	 * Moves the given thread to the target forum
	 * 
	 * @param thread
	 *            The thread to move
	 * @param forum
	 *            The forum to move the thread to
	 * @param user
	 *            The user moving the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean moveThread(ForumThread thread, Forum forum, User user);

    /**
	 * Creates a reply to the given thread
	 * 
	 * @param thread
	 *            The thread to respond to
	 * @param content
	 *            The content of the new post
	 * @param user
	 *            The user doing the post
	 * @return The new ForumPost, or <code>null</code> if a problem occured
	 */
    public ForumPost replyToThread(ForumThread thread, String content, User user);

    /**
	 * Splits a given thread, moving the indicated posts to a new thread with
	 * the given title
	 * 
	 * @param source
	 *            The thread to split the posts from
	 * @param splitPosts
	 *            The posts to move to the new thread
	 * @param splitTitle
	 *            The title of the new thread
	 * @param splitOpening
	 *            The opening text of the new thread
	 * @param user
	 *            The user doing the splitting
	 * @return The created thread if succesful, <code>null</code> otherwise
	 */
    public ForumThread splitThread(ForumThread source, List<ForumPost> splitPosts, String splitTitle, String splitOpening, User user);

    /**
	 * Stickies a given thread, if allowed
	 * 
	 * @param thread
	 *            The thread to sticky
	 * @param user
	 *            The user to sticky the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean stickyThread(ForumThread thread, User user);

    /**
	 * Unlocks a given thread, if allowed
	 * 
	 * @param thread
	 *            The thread to lock
	 * @param user
	 *            The user locking the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean unlockThread(ForumThread thread, User user);

    /**
	 * Unstickies a given thread, if allowed
	 * 
	 * @param thread
	 *            The thread to unsticky
	 * @param user
	 *            The user unstickying the thread
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
    public boolean unstickyThread(ForumThread thread, User user);

    public boolean makeMembersOnly(Forum forum, User user);

    public boolean unmakeMembersOnly(Forum forum, User user);

    public ForumCategory createCategory(User user, String name, boolean allowPublicGroups);

    public Forum createForum(String name, String description, boolean allowPublicAccess, ForumCategory category, User user);

    public NewsForum createNewsForum(String name, String description, boolean allowPublicAccess, ForumCategory category);

    public GroupForum createGroupForum(String name, String description, ForumCategory category, Group group);

    /**
	 * @return The first forum found where Events can be posted and user
	 *         applications can be found
	 */
    public Forum getInteractionForum();

    /**
	 * @param forum
	 *            The forum to make interactive
	 * @param interactive
	 *            The interactive setting
	 * @param user
	 *            The user that made the change
	 */
    public void setInteractive(Forum forum, boolean interactive, User user);

    public List<ForumPost> getUnreadPosts(User user);

    public boolean isGroupMember(User user, Group group);

    public void deleteCategory(User user, ForumCategory category);

    public void setForumName(Forum forum, String newName, User user);

    public void setForumDescription(Forum forum, String description, User user);

    public void setModeratorOnlyRestriction(User user, Forum forum, boolean publicAccess);

    public void setMembersOnly(User user, Forum forum, boolean membersOnly);

    public boolean deleteForum(User user, Forum forum);

    public void moveUp(Forum forum);

    public void moveDown(Forum forum);

    public void moveToCategory(User user, Forum forum, ForumCategory forumCategory);

    public Forum removeModerator(User remover, Forum forum, User moderator);

    public Forum addModerator(User adder, Forum forum, User moderator);

    public void setThreadTitle(ForumThread thread, String newTitle);

    public boolean isPostUnread(User user, ForumPost post);

    public int countUnread(User user);

    public void clearUnreadPosts(User user);

    public void clearUnreadPosts(User user, Forum forum);

    public void addUnreadPosts(User user);

    public void markForumPostRead(User user, ForumPost post);

    public int getForumThreadUnreadCount(User user, ForumThread thread);

    public int getForumUnreadCount(User user, Forum forum);

    List<Forum> filterForums(User user, List<Forum> in, boolean publicView);

    List<ForumPost> filterPosts(User user, boolean publicView, List<ForumPost> originalPosts);

    List<ForumThread> filterThreads(User user, Forum forum, boolean publicView);

    List<ForumCategory> filterCategories(User user, List<ForumCategory> in, boolean publicView);

    public ForumPost getFirstPost(ForumThread thread);

    public List<ForumThread> fiterAndSortThreads(User user, Forum forum, boolean publicView);

    public boolean canReply(User user, Forum forum);
}
