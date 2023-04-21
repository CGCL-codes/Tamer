package org.castafiore.shoppingmall.contacts.ui;

import java.util.Date;
import org.castafiore.searchengine.EventDispatcher;
import org.castafiore.shoppingmall.user.ShoppingMallUser;
import org.castafiore.shoppingmall.user.ShoppingMallUserManager;
import org.castafiore.spring.SpringUtil;
import org.castafiore.ui.Container;
import org.castafiore.ui.events.Event;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.EXInput;
import org.castafiore.ui.ex.form.EXTextArea;
import org.castafiore.ui.scripting.EXXHTMLFragment;
import org.castafiore.wfs.Util;
import org.castafiore.wfs.types.Message;

public class EXInviteFriend extends EXXHTMLFragment implements EventDispatcher {

    public EXInviteFriend(String name) throws Exception {
        super(name, "templates/EXInviteFriend.xhtml");
        addChild(new EXContainer("from", "div")).addChild(new EXInput("to", "").addClass("span-12")).addChild(new EXInput("subject").addClass("span-12")).addChild(new EXTextArea("message").setAttribute("rows", "16").addClass("span-12")).addChild(new EXContainer("send", "button").setText("Send Invitation").addEvent(DISPATCHER, Event.CLICK)).addChild(new EXContainer("cancel", "button").setText("Cancel").addEvent(DISPATCHER, Event.CLICK));
    }

    public void init() throws Exception {
        ShoppingMallUser user = SpringUtil.getBeanOfType(ShoppingMallUserManager.class).getCurrentUser();
        getChild("from").setText(user.getUser().toString()).setAttribute("author", user.getUser().getUsername());
        ((EXInput) getChild("to")).setValue("");
        ((EXInput) getChild("subject")).setValue("Hi, I would like to add you in my contact like.");
        ((EXTextArea) getChild("message")).setValue("");
    }

    public void sendMessage() {
        Message message = new Message();
        message.setName("created on " + new Date().toString());
        String destination = ((EXInput) getChild("to")).getValue().toString();
        message.setDestination(destination);
        message.setAuthor(getChild("from").getAttribute("author"));
        message.setTitle(((EXInput) getChild("subject")).getValue().toString());
        message.setSummary(((EXTextArea) getChild("message")).getValue().toString());
        message.setOwner(Util.getRemoteUser());
        ShoppingMallUser user = SpringUtil.getBeanOfType(ShoppingMallUserManager.class).getCurrentUser();
        user.sendMessage(message);
        getAncestorOfType(EXContactPanel.class).showContacts(ShoppingMallUser.DEFAULT_CONTACT_CATEGORY);
    }

    public void cancel() {
        getAncestorOfType(EXContactPanel.class).showList();
    }

    @Override
    public void executeAction(Container source) {
        if (source.getName().equals("send")) {
            sendMessage();
        } else if (source.getName().equals("cancel")) {
            cancel();
        }
    }
}
