package com.codebitches.spruce.module.bb.web.spring;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbForum;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbPost;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbTopic;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser;
import com.codebitches.spruce.module.bb.domain.local.SprucebbConfiguration;
import com.codebitches.spruce.module.bb.domain.logic.IBBActivityTracker;
import com.codebitches.spruce.module.bb.domain.logic.IBBCodeService;
import com.codebitches.spruce.module.bb.domain.logic.IBBService;
import com.codebitches.spruce.module.bb.exception.security.SprucebbLogonRequiredException;
import com.codebitches.spruce.module.bb.exception.security.SprucebbPermissionException;
import com.codebitches.spruce.module.bb.security.permission.SprucebbPermission;
import com.codebitches.spruce.module.bb.web.constants.SprucebbSessionConstants;

/**
 * @author Stuart Eccles
 */
public class ViewTopicController extends MultiActionController {

    private IBBService bbService;

    private IBBCodeService bbCodeService;

    private IBBActivityTracker bbActivityTracker;

    private SprucebbConfiguration sprucebbConfiguration;

    private List permissionChecks;

    private List postPermissionChecks;

    private List editPermissionChecks;

    private List replyPermissionChecks;

    private List deletePermissionChecks;

    /**
     * @param deletePermissionChecks The deletePermissionChecks to set.
     */
    public void setDeletePermissionChecks(List deletePermissionChecks) {
        this.deletePermissionChecks = deletePermissionChecks;
    }

    /**
     * @param editPermissionChecks The editPermissionChecks to set.
     */
    public void setEditPermissionChecks(List editPermissionChecks) {
        this.editPermissionChecks = editPermissionChecks;
    }

    /**
     * @param postPermissionChecks The postPermissionChecks to set.
     */
    public void setPostPermissionChecks(List postPermissionChecks) {
        this.postPermissionChecks = postPermissionChecks;
    }

    /**
     * @param replyPermissionChecks The replyPermissionChecks to set.
     */
    public void setReplyPermissionChecks(List replyPermissionChecks) {
        this.replyPermissionChecks = replyPermissionChecks;
    }

    /**
	 * @param permissionChecks The permissionChecks to set.
	 */
    public void setPermissionChecks(List permissionChecks) {
        this.permissionChecks = permissionChecks;
    }

    /**
	 * @param sprucebbConfiguration The sprucebbConfiguration to set.
	 */
    public void setSprucebbConfiguration(SprucebbConfiguration sprucebbConfiguration) {
        this.sprucebbConfiguration = sprucebbConfiguration;
    }

    /**
	 * @param bbCodeService The bbCodeService to set.
	 */
    public void setBbCodeService(IBBCodeService bbCodeService) {
        this.bbCodeService = bbCodeService;
    }

    /**
	 * @param bbActivityTracker The bbActivityTracker to set.
	 */
    public void setBbActivityTracker(IBBActivityTracker bbActivityTracker) {
        this.bbActivityTracker = bbActivityTracker;
    }

    /**
	 * @param bbService The bbService to set.
	 */
    public void setBbService(IBBService bbService) {
        this.bbService = bbService;
    }

    private Map retrieveTopicModel(long topicId, HttpServletRequest request) {
        SprucebbTopic topic = bbService.viewTopic(topicId);
        SprucebbUser user = (SprucebbUser) request.getSession().getAttribute(SprucebbSessionConstants.SESSION_ATTR_BB_USER);
        checkTopicPermissions(topic, user);
        if (topic != null && topic.getSprucebbPosts() != null) {
            Iterator it = topic.getSprucebbPosts().iterator();
            while (it.hasNext()) {
                SprucebbPost post = (SprucebbPost) it.next();
                post = bbCodeService.processPostForCode(post);
                post.setAllowEdit(SprucebbPermission.makePermissionChecks(post, user, editPermissionChecks));
                post.setAllowDelete(SprucebbPermission.makePermissionChecks(post, user, deletePermissionChecks));
            }
        }
        Map onlineUsers = bbActivityTracker.getOnlineUserActivity();
        Map model = new HashMap();
        model.put("topic", topic);
        model.put("onlineusers", onlineUsers);
        model.put("posts", topic.getSprucebbPosts());
        model.put("configuration", sprucebbConfiguration);
        try {
            model.put("allowPost", new Boolean(SprucebbPermission.makePermissionChecks(topic.getSprucebbForum(), user, postPermissionChecks)));
        } catch (SprucebbLogonRequiredException e) {
            model.put("allowPost", new Boolean(true));
        }
        try {
            model.put("allowReply", new Boolean(SprucebbPermission.makePermissionChecks(topic.getSprucebbForum(), user, replyPermissionChecks)));
        } catch (SprucebbLogonRequiredException e) {
            model.put("allowReply", new Boolean(true));
        }
        return model;
    }

    private void checkTopicPermissions(SprucebbTopic topic, SprucebbUser user) {
        if (permissionChecks != null) {
            Iterator checks = permissionChecks.iterator();
            while (checks.hasNext()) {
                SprucebbPermission per = (SprucebbPermission) checks.next();
                if (!per.checkPermission(topic, user)) {
                    throw new SprucebbPermissionException("no permission to read this topic");
                }
            }
        }
    }

    /** default action
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String topicId = request.getParameter("topicId");
        Map model = retrieveTopicModel(Long.parseLong(topicId), request);
        return new ModelAndView("view_topic", model);
    }

    /** default action
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ModelAndView print(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String topicId = request.getParameter("topicId");
        Map model = retrieveTopicModel(Long.parseLong(topicId), request);
        return new ModelAndView("print_topic", model);
    }

    /**
	 * for action is next
	 * @author Stuart Eccles
	 */
    public ModelAndView next(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String topicId = request.getParameter("topicId");
        Map onlineUsers = bbActivityTracker.getOnlineUserActivity();
        SprucebbTopic topic = bbService.viewNextTopicToThis(Long.parseLong(topicId));
        Map model = new HashMap();
        model.put("topic", topic);
        model.put("onlineusers", onlineUsers);
        model.put("posts", topic.getSprucebbPosts());
        return new ModelAndView("view_topic", model);
    }

    /**
	 * for action is previous
	 * @author Stuart Eccles
	 */
    public ModelAndView previous(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String topicId = request.getParameter("topicId");
        SprucebbTopic topic = bbService.viewPreviousTopicToThis(Long.parseLong(topicId));
        Map onlineUsers = bbActivityTracker.getOnlineUserActivity();
        Map model = new HashMap();
        model.put("topic", topic);
        model.put("onlineusers", onlineUsers);
        model.put("posts", topic.getSprucebbPosts());
        return new ModelAndView("view_topic", model);
    }

    /**
	 * for action is delete
	 * @author Stuart Eccles
	 */
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String topicId = request.getParameter("topicId");
        String postId = request.getParameter("postId");
        Map model = retrieveTopicModel(Long.parseLong(topicId), request);
        model.put("postId", postId);
        return new ModelAndView("confirm_delete", model);
    }

    /**
	 * for action is deleteconfirm
	 * @author Stuart Eccles
	 */
    public ModelAndView deleteconfirm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionMessage = "";
        String topicId = request.getParameter("topicId");
        String postId = request.getParameter("postId");
        SprucebbTopic topic = bbService.deletePost(Long.parseLong(postId));
        Map model = new HashMap();
        if (topic != null) {
            return new ModelAndView(new RedirectView("viewTopic.htm?topicId=" + topicId));
        } else {
            actionMessage = "Topic has been deleted";
            model.put("actionMessage", actionMessage);
            return new ModelAndView("topic_deleted", model);
        }
    }

    /**
	 * for action is addWatch
	 * @author Stuart Eccles
	 */
    public ModelAndView addWatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionMessage = "";
        String topicId = request.getParameter("topicId");
        SprucebbUser user = (SprucebbUser) request.getSession().getAttribute(SprucebbSessionConstants.SESSION_ATTR_BB_USER);
        if (user != null) {
            bbService.addTopicWatch(user.getUserId().longValue(), Long.parseLong(topicId));
            actionMessage = "You are now watching this topic";
        } else {
            actionMessage = "Unable to watch topic";
        }
        Map model = retrieveTopicModel(Long.parseLong(topicId), request);
        model.put("actionMessage", actionMessage);
        return new ModelAndView("view_topic", model);
    }

    /**
	 * for action is removeWatch
	 * @author Stuart Eccles
	 */
    public ModelAndView removeWatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionMessage = "";
        String topicId = request.getParameter("topicId");
        SprucebbUser user = (SprucebbUser) request.getSession().getAttribute(SprucebbSessionConstants.SESSION_ATTR_BB_USER);
        if (user != null) {
            bbService.removeTopicWatch(user.getUserId().longValue(), Long.parseLong(topicId));
            actionMessage = "You are no longer watching this topic";
        } else {
            actionMessage = "Unable to unwatch topic";
        }
        Map model = retrieveTopicModel(Long.parseLong(topicId), request);
        model.put("actionMessage", actionMessage);
        return new ModelAndView("view_topic", model);
    }
}
