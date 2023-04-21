package com.dotmarketing.tag.ajax;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import uk.ltd.getahead.dwr.WebContextFactory;
import com.dotmarketing.factories.UserProxyFactory;
import com.dotmarketing.portlets.usermanager.factories.UserManagerListBuilderFactory;
import com.dotmarketing.portlets.usermanager.struts.UserManagerListSearchForm;
import com.dotmarketing.tag.factories.TagFactory;
import com.dotmarketing.tag.model.Tag;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.WebKeys;

public class TagAjax {

    /**
	 * Tags an object, validates the existence of a tag(s), creates it if it doesn't exists
	 * and then tags the object 
	 * @param tagName tag(s) to create
	 * @param userId owner of the tag
	 * @param inode object to tag
	 * @return a list of all tags assigned to an object
	 */
    public static List addTag(String tagName, String userId, String inode) {
        return TagFactory.addTag(tagName, userId, inode);
    }

    /**
	 * Tags the users in selected in the User Manager, validates the existence of a tag(s), creates it if it doesn't exists
	 * and then tags the object 
	 * @param tagName tag(s) to create
	 * @param userId owner of the tag
	 * @return a list of all tags assigned to an object
	 */
    public static void addTagFullCommand(String tagName, String userId) {
        try {
            HttpSession session = WebContextFactory.get().getSession();
            UserManagerListSearchForm searchFormFullCommand = (UserManagerListSearchForm) session.getAttribute(WebKeys.USERMANAGERLISTPARAMETERS);
            searchFormFullCommand.setStartRow(0);
            searchFormFullCommand.setMaxRow(0);
            List matches = UserManagerListBuilderFactory.doSearch(searchFormFullCommand);
            Iterator it = matches.iterator();
            for (int i = 0; it.hasNext(); i++) {
                String userTagId = (String) ((Map) it.next()).get("userid");
                String inode = Long.toString(UserProxyFactory.getUserProxy(userTagId).getInode());
                TagFactory.addTag(tagName, userId, inode);
            }
        } catch (Exception ex) {
            String message = ex.toString();
            Logger.debug(TagAjax.class, message);
        }
    }

    /**
	 * Gets all the tag created by an user
	 * @param userId id of the user
	 * @return a list of all the tags created
	 */
    public List<Tag> getTagByUser(String userId) {
        return TagFactory.getTagByUser(userId);
    }

    /**
	 * Deletes a tag
	 * @param tagName name of the tag to be deleted
	 * @param userId id of the tag owner
	 * @return list of all the tags, with the owner information and the respective permission
	 */
    public List deleteTag(String tagName, String userId) {
        TagFactory.deleteTag(tagName, userId);
        return getAllTag(userId);
    }

    /**
	 * Gets all the tags created, with the respective owner and permission information
	 * @param userId id of the user that searches the tag
	 * @return a complete list of all the tags, with the owner information and the respective permission
	 * information
	 */
    public List getAllTag(String userId) {
        return TagFactory.getAllTag(userId);
    }

    /**
	 * Gets a tag with the owner information, searching by name
	 * @param name name of the tag
	 * @return the tag with the owner information 
	 */
    public List getTagInfoByName(String tagName) {
        return TagFactory.getTagInfoByName(tagName);
    }

    /**
	 * Gets all tags associated to an object
	 * @param inode inode of the object tagged
	 * @return list of all the TagInode where the tags are associated to the object
	 */
    public static List getTagInodeByInode(String inode) {
        return TagFactory.getTagInodeByInode(inode);
    }

    /**
	 * Deletes an object tag assignment(s)
	 * @param tagName name(s) of the tag(s)
	 * @param inode inode of the object tagged
	 * @return a list of all tags assigned to an object
	 */
    public List deleteTagInode(String tagName, String inode) {
        return TagFactory.deleteTagInode(tagName, inode);
    }

    public static void deleteTagFullCommand(String tagName) {
        try {
            HttpSession session = WebContextFactory.get().getSession();
            UserManagerListSearchForm searchFormFullCommand = (UserManagerListSearchForm) session.getAttribute(WebKeys.USERMANAGERLISTPARAMETERS);
            searchFormFullCommand.setStartRow(0);
            searchFormFullCommand.setMaxRow(0);
            List matches = UserManagerListBuilderFactory.doSearch(searchFormFullCommand);
            Iterator it = matches.iterator();
            for (int i = 0; it.hasNext(); i++) {
                String userTagId = (String) ((Map) it.next()).get("userid");
                String inode = Long.toString(UserProxyFactory.getUserProxy(userTagId).getInode());
                TagFactory.deleteTagInode(tagName, inode);
            }
        } catch (Exception ex) {
            String message = ex.toString();
            Logger.debug(TagAjax.class, message);
        }
    }

    /**
	 * Gets a suggested tag(s), by name
	 * @param name name of the tag searched
	 * @return list of suggested tags
	 */
    public List<Tag> getSuggestedTag(String tagName) {
        return TagFactory.getSuggestedTag(tagName);
    }

    /**
	 * Get a list of all the tags created
	 * @return list of all tags created
	 */
    public List<Tag> getAllTags() {
        return TagFactory.getAllTags();
    }
}
