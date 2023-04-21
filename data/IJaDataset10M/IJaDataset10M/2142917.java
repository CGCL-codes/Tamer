package br.com.arsmachina.colloquium.tapestry.pages.message;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SessionState;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authorization.annotation.NeedsLoggedInUser;
import br.com.arsmachina.authorization.annotation.NeedsPermission;
import br.com.arsmachina.colloquium.Constants;
import br.com.arsmachina.colloquium.entity.ForumUser;
import br.com.arsmachina.colloquium.entity.Message;
import br.com.arsmachina.colloquium.entity.Topic;
import br.com.arsmachina.colloquium.tapestry.pages.Index;
import br.com.arsmachina.colloquium.tapestry.pages.topic.ViewTopic;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;

/**
 * {@link Message} edition page.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@NeedsLoggedInUser
@NeedsPermission(Constants.FORUM_USER_PERMISSION)
public class EditMessage extends BaseEditPage<Message, Long> {

    @Mixin
    @SuppressWarnings("unused")
    private HibernateValidatorMixin hibernateValidatorMixin;

    @Retain
    private ActivationContextEncoder<Topic> topicACE = getActivationContextEncoder(Topic.class);

    @Property
    private Topic topic;

    @InjectPage
    private ViewTopic viewTopic;

    @SessionState(create = false)
    private User user;

    /**
	 * Sets the creator and the topic for this new message.
	 */
    @Override
    protected void prepareObject() {
        final Message message = getObject();
        message.setAuthor(user.getRole(ForumUser.class));
        message.setSubject(topic.getSubject());
        message.setTopic(topic);
    }

    @Override
    protected Object onActivate(EventContext context) {
        Object returnValue = null;
        checkStoreTypeAccess();
        if (context.getCount() > 0) {
            topic = topicACE.toObject(context);
        }
        if (topic == null) {
            returnValue = Index.class;
        }
        return returnValue;
    }

    /**
	 * Shows the topic viewing page after saving the message.
	 * @return a {@link ViewTopic} instance.
	 */
    @Override
    protected Object returnFromSaveOrUpdate() {
        viewTopic.setTopic(topic);
        viewTopic.setNewMessage(true);
        return viewTopic;
    }

    @Override
    public Object onPassivate() {
        return topicACE.toActivationContext(topic);
    }
}
