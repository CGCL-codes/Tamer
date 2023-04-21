package org.boticelli.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.boticelli.plugin.chat.ChatMessage;
import org.boticelli.plugin.chat.ChatMessageHub;
import org.boticelli.util.JSONView;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ChatController extends AbstractBotController {

    private static final String CHAT_SECRET_ATTRIBUTE_NAME = "boticelli-chat-secret";

    protected static Logger log = Logger.getLogger(ChatController.class);

    private ChatMessageHub chatMessageHub;

    private String channelBotName;

    public ChatController() {
    }

    @Required
    public void setChannelBotName(String channelBotName) {
        this.channelBotName = channelBotName;
    }

    @Required
    public void setChatMessageHub(ChatMessageHub chatMessageHub) {
        this.chatMessageHub = chatMessageHub;
    }

    @RequestMapping("/chat/login")
    public ModelAndView login() {
        return new ModelAndView("chatLogin");
    }

    @RequestMapping("/chat/enter")
    public ModelAndView enter(HttpSession session, HttpServletResponse response, @RequestParam(value = "name", required = true) String name) throws IOException {
        try {
            String secret = chatMessageHub.enter(name, super.findBot(channelBotName));
            session.setAttribute(CHAT_SECRET_ATTRIBUTE_NAME, secret);
            ModelAndView mav = new ModelAndView("redirect:/app/chat/chat");
            return mav;
        } catch (Exception e) {
            log.error("", e);
            response.sendError(500, e.getMessage());
            return null;
        }
    }

    @RequestMapping("/chat/chat")
    public ModelAndView chat(HttpSession session, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("chat");
        String secret = (String) session.getAttribute(CHAT_SECRET_ATTRIBUTE_NAME);
        mav.addObject("secret", secret);
        return mav;
    }

    @RequestMapping("/chat/hub/receive")
    public ModelAndView receive(@RequestParam(value = "secret", required = true) String secret) {
        if (log.isDebugEnabled()) {
            log.debug("receiving");
        }
        List<ChatMessage> messages = chatMessageHub.getMessages(secret);
        if (log.isDebugEnabled()) {
            log.debug("received " + messages);
        }
        return JSONView.modelAndView(messages);
    }

    @RequestMapping("/chat/hub/send")
    public ModelAndView send(@RequestParam(value = "secret", required = true) String secret, @RequestParam(value = "msg", required = true) String message) {
        if (log.isDebugEnabled()) {
            log.debug("sending message " + message);
        }
        chatMessageHub.send(secret, message);
        if (log.isDebugEnabled()) {
            log.debug("sent");
        }
        return JSONView.modelAndView(Boolean.TRUE);
    }
}
