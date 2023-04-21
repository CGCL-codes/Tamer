package org.zeroexchange.web.page.messages;

import java.util.Collections;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.messaging.embedded.read.MessageReader;
import org.zeroexchange.messaging.embedded.write.MessageWriter;
import org.zeroexchange.model.message.Message;
import org.zeroexchange.model.message.UserMessage;
import org.zeroexchange.model.user.Role;
import org.zeroexchange.model.user.User;
import org.zeroexchange.security.AuthorizedUserService;
import org.zeroexchange.web.annotations.Bookmarkable;
import org.zeroexchange.web.annotations.MenuKey;
import org.zeroexchange.web.components.link.LinkPanel;
import org.zeroexchange.web.components.toolbar.ToolbarItem;
import org.zeroexchange.web.page.MenuKeys;
import org.zeroexchange.web.page.ToolbarPage;
import org.zeroexchange.web.page.user.UserMaintenance;

/**
 * Page shows the details (including body) of the user's message. 
 * 
 * @author black
 */
@Bookmarkable
@Secured(Role.ACEGITOKEN_USER)
@MenuKey(MenuKeys.PROFILE)
public class MessageDetails extends ToolbarPage {

    private static final long serialVersionUID = 1L;

    public static final String PARAM_MESSAGE_ID = "messageId";

    private static final String CKEY_FROM = "from";

    private static final String CKEY_SUBJECT = "subject";

    private static final String CKEY_BODY = "body";

    private static final String MKEY_SENDER_UNKNOWN = "sender.unknown";

    private static final String MKEY_SENDER_SYSTEM = "sender.system";

    private static final String MKEY_DELETE_MESSAGE = "message.delete";

    @SpringBean
    private MessageReader messageReader;

    @SpringBean
    private MessageWriter messageWriter;

    @SpringBean
    private AuthorizedUserService authorizedUserService;

    private Message message;

    /**
     * Constructor.
     */
    public MessageDetails(PageParameters pageParameters) {
        super(pageParameters);
    }

    /**
     * Builds the page's UI.
     */
    protected void initUI() {
        super.initUI();
        Message message = getMessage();
        IModel<String> senderName = null;
        if (message instanceof UserMessage) {
            User sender = ((UserMessage) message).getSender();
            senderName = sender != null ? new Model<String>(sender.getName()) : new ResourceModel(MKEY_SENDER_UNKNOWN);
        } else {
            senderName = new ResourceModel(MKEY_SENDER_SYSTEM);
        }
        add(new Label(CKEY_FROM, senderName));
        add(new Label(CKEY_SUBJECT, message.getSubject()));
        add(new Label(CKEY_BODY, message.getMessage()));
    }

    /**
     * Returns the current message.
     */
    protected Message getMessage() {
        if (message == null) {
            Integer messageId = getPageParameters().get(PARAM_MESSAGE_ID).toInteger();
            message = messageReader.getUserMessage(messageId, authorizedUserService.getCurrentUserId());
            if (message == null) {
                throw new BusinessLogicException("Cannot find message #" + messageId + " for the current user!");
            }
        }
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBeforeRender() {
        messageWriter.markRead(getMessage());
        super.onBeforeRender();
    }

    @Override
    protected List<ToolbarItem> getToolbarItems() {
        List<ToolbarItem> items = super.getToolbarItems();
        items.add(new ToolbarItem() {

            @Override
            public Component getComponent(String componentId) {
                return new LinkPanel(componentId, new ResourceModel(MKEY_DELETE_MESSAGE)) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onClick() {
                        messageWriter.deleteMessages(Collections.singleton(message.getId()));
                        setResponsePage(UserMaintenance.class, new PageParameters().add(UserMaintenance.PKEY_TAB, UserMaintenance.TABALIAS_USER_MESSAGES));
                    }
                };
            }
        });
        return items;
    }
}
