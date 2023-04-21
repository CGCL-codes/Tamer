package oxygen.forum.view;

import java.io.StringReader;
import java.util.Date;
import oxygen.forum.*;
import oxygen.forum.data.*;
import oxygen.util.NullWriter;
import oxygen.util.StringUtils;
import oxygen.web.GenericWebAction;
import oxygen.web.ViewContext;
import oxygen.web.WebInteractionContext;
import oxygen.web.WebLocal;

public class PostAction extends GenericWebAction {

    {
        setFlag(FLAG_WRITE_ACTION);
    }

    public int processAction() throws Exception {
        ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
        WebInteractionContext wctx = WebLocal.getWebInteractionContext();
        if (ForumUtil.isCaptchaEnabled()) {
            WebLocal.getWebUserSession().checkCaptchaChallenge();
        }
        Long forumid = null;
        Long topicid = null;
        Long postid = null;
        Long parentpostid = null;
        String s = null;
        if ((s = wctx.getParameter("forumid")) != null && !StringUtils.isBlank(s)) {
            forumid = new Long(s);
        }
        if ((s = wctx.getParameter("topicid")) != null && !StringUtils.isBlank(s)) {
            topicid = new Long(s);
        }
        if ((s = wctx.getParameter("postid")) != null && !StringUtils.isBlank(s)) {
            postid = new Long(s);
        }
        if ((s = wctx.getParameter("parentpostid")) != null && !StringUtils.isBlank(s)) {
            parentpostid = new Long(s);
        }
        String subject = wctx.getParameter("subject");
        String text = wctx.getParameter("text");
        boolean write = "true".equals(wctx.getParameter("write"));
        boolean read = "true".equals(wctx.getParameter("read"));
        boolean highvisibility = "true".equals(wctx.getParameter("highvisibility"));
        ForumLocal.getForumEngine().getForumRenderEngine().render(new NullWriter(), new StringReader(text));
        String remoteAddr = wctx.getRemoteAddress();
        User user = dao.getUser(wctx.getUserName());
        Forum f = null;
        Post p = null;
        Topic t = null;
        ForumEvent fevent = null;
        ForumEventManager fem = ForumLocal.getForumEngine().getForumEventManager();
        ForumStatisticsManager fsm = ForumLocal.getForumEngine().getForumStatisticsManager();
        if (postid != null) {
            p = (Post) dao.get(Post.class, postid, true);
            t = p.getTopic();
            f = t.getForum();
            checkAccess(p.getTopic());
            fevent = new ForumEvent(p, ForumEvent.POST, ForumEvent.UPDATE);
        } else {
            if (parentpostid != null) {
                p = new Post();
                p.setParentPost((Post) dao.get(Post.class, parentpostid, true));
                t = p.getParentPost().getTopic();
                f = t.getForum();
                p.setTopic(t);
                checkAccess(t);
                fevent = new ForumEvent(p, ForumEvent.POST, ForumEvent.UPDATE);
            } else if (topicid != null) {
                t = (Topic) dao.get(Topic.class, topicid, true);
                checkAccess(t);
                f = t.getForum();
                p = t;
                fevent = new ForumEvent(t, ForumEvent.TOPIC, ForumEvent.UPDATE);
            } else if (forumid != null) {
                f = (Forum) dao.get(Forum.class, forumid, true);
                checkAccess(f);
                t = new Topic();
                t.setForum(f);
                p = t;
                fevent = new ForumEvent(t, ForumEvent.TOPIC, ForumEvent.CREATE);
            } else {
                throw new Exception("All of postid, postparentid, forumid, topicid values cannot be null");
            }
            p.setDate(new Date());
            p.setIp(remoteAddr);
            p.setAuthor(user);
        }
        p.setTitle(subject);
        p.setDetails(text);
        p.setWrite(write);
        p.setRead(read);
        p.setHighvisibility(highvisibility);
        dao.saveOrUpdate(p);
        if (postid == null) {
            fsm.setLastPostIdForTopic(t.getId(), p.getId());
            fsm.incrNumPostsForUser(user.getId());
            fsm.incrNumRepliesForTopic(t.getId());
            fsm.setLastPostIdForForum(f.getId(), p.getId());
            fsm.incrNumTopicsForForum(f.getId());
            fsm.incrNumPostsForForum(f.getId());
        }
        fem.publish(fevent);
        ViewContext vctx = WebLocal.getViewContext();
        vctx.setAction("topic");
        vctx.setAttribute(ForumConstants.ID_KEY, p.getTopic().getId());
        vctx.setAttribute(ForumConstants.TOPICID_KEY, p.getTopic().getId());
        vctx.setAttribute(ForumConstants.FORUMID_KEY, p.getTopic().getForum().getId());
        return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
    }

    private void checkAccess(DataEntity t) throws Exception {
        ForumUtil.checkAccessConsideringLockedStatusAndRoles(t, true);
    }
}
