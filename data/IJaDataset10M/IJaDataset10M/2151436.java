package br.com.arsmachina.colloquium.tapestry.pages.topic;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authorization.annotation.NeedsLoggedInUser;
import br.com.arsmachina.authorization.annotation.NeedsPermission;
import br.com.arsmachina.colloquium.Constants;
import br.com.arsmachina.colloquium.controller.MessageController;
import br.com.arsmachina.colloquium.entity.Forum;
import br.com.arsmachina.colloquium.entity.ForumUser;
import br.com.arsmachina.colloquium.entity.Message;
import br.com.arsmachina.colloquium.entity.Topic;
import br.com.arsmachina.colloquium.tapestry.pages.Index;
import br.com.arsmachina.colloquium.tapestry.pages.forum.ViewForum;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;

/**
 * {@link Topic} edition page.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@NeedsLoggedInUser
@NeedsPermission(Constants.FORUM_USER_PERMISSION)
public class EditTopic extends BaseEditPage<Topic, Integer> {

    @Mixin
    @SuppressWarnings("unused")
    private HibernateValidatorMixin hibernateValidatorMixin;

    @Retain
    private ActivationContextEncoder<Forum> forumACE = getActivationContextEncoder(Forum.class);

    @Property
    private Forum forum;

    @InjectPage
    private ViewForum viewForum;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private String content;

    @SessionState(create = false)
    private User user;

    @Inject
    private MessageController messageController;

    /**
	 * Sets the creator and the forum for this new topic.
	 */
    @Override
    protected void prepareObject() {
        final Topic topic = getObject();
        topic.setCreator(user.getRole(ForumUser.class));
        topic.setForum(forum);
    }

    /**
	 * Saves the topic and its first message.
	 */
    @Override
    protected Topic saveOrUpdate(Topic topic) {
        topic = super.saveOrUpdate(topic);
        Message message = new Message();
        message.setAuthor(topic.getCreator());
        message.setSubject(topic.getSubject());
        message.setTopic(topic);
        message.setContent(this.content);
        messageController.save(message);
        return topic;
    }

    @Override
    protected Object onActivate(EventContext context) {
        Object returnValue = null;
        checkStoreTypeAccess();
        if (context.getCount() > 0) {
            forum = forumACE.toObject(context);
        }
        if (forum == null || forum.isTopicless()) {
            returnValue = Index.class;
        }
        return returnValue;
    }

    /**
	 * @see br.com.arsmachina.tapestrycrud.base.BaseEditPage#returnFromSaveOrUpdate()
	 */
    @Override
    protected Object returnFromSaveOrUpdate() {
        viewForum.setObject(forum);
        return viewForum;
    }

    @Override
    public Object onPassivate() {
        return forumACE.toActivationContext(forum);
    }
}
