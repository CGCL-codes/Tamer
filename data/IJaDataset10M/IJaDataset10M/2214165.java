package de.rentoudu.chat.server.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.users.UserService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import de.rentoudu.chat.model.Message;
import de.rentoudu.chat.server.CurrentChannel;
import de.rentoudu.chat.server.JsonResponse;
import de.rentoudu.chat.server.ObjectifyManager;
import de.rentoudu.chat.server.guice.ChannelKey;

/**
 * The "heart" or interface of the application.
 * 
 * @author Florian Sauter
 */
public class MessageServlet extends HttpServlet {

    private static final long serialVersionUID = 6345781522312385253L;

    public static final String MODE_SAVE = "save";

    public static final String MODE_LIST = "list";

    private final ChannelService channelService;

    private final Gson gson;

    private final ObjectifyManager objectifyManager;

    private final String channelKey;

    private final UserService userService;

    private final CurrentChannel userInfo;

    /**
	 * Instantiated over injection.
	 */
    @Inject
    public MessageServlet(final ChannelService channelService, final ObjectifyManager objectifyManager, final Gson gson, @ChannelKey String channelKey, final UserService userService, final CurrentChannel userInfo) {
        this.channelService = channelService;
        this.objectifyManager = objectifyManager;
        this.gson = gson;
        this.channelKey = channelKey;
        this.userService = userService;
        this.userInfo = userInfo;
    }

    /**
	 * Processor for the different modes.
	 */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        JsonResponse response = JsonResponse.fromUnsuccessfulResult();
        response.setSuccess(false);
        String passedMode = request.getParameter("mode");
        if (MODE_LIST.equals(passedMode)) {
            response = processListMode(request);
        } else if (MODE_SAVE.equals(passedMode)) {
            response = processSaveMode(request);
        } else {
            throw new IllegalArgumentException("Unsupported mode in request: " + String.valueOf(passedMode));
        }
        String jsonResponse = gson.toJson(response);
        resp.setContentType("text/javascript");
        resp.getWriter().print(jsonResponse);
    }

    /**
	 * List all messages.
	 * 
	 * @param request
	 * @param response
	 */
    public JsonResponse processListMode(HttpServletRequest request) {
        JsonResponse response = JsonResponse.fromUnsuccessfulResult();
        List<Message> messages = objectifyManager.query(Message.class).filter("channel", userInfo.manufactureChannelKey()).list();
        for (Message message : messages) {
            response.addData(message);
        }
        response.setLength(response.getData().size());
        response.setSuccess(true);
        response.setMessage("Listed items.");
        return response;
    }

    /**
	 * Saves a message. Requires the "message" parameter.
	 */
    public JsonResponse processSaveMode(HttpServletRequest request) {
        JsonResponse response = JsonResponse.fromUnsuccessfulResult();
        String passedMessage = request.getParameter("message");
        if (passedMessage == null || passedMessage.isEmpty()) {
            response.setMessage("Sry, empty message...");
        } else {
            Message message = new Message();
            message.setMessage(passedMessage);
            message.setAuthor(userService.getCurrentUser().getNickname());
            message.setTimestamp(new Date());
            message.setChannel(userInfo.manufactureChannelKey());
            objectifyManager.put(message);
            response.addProperty("channel", userInfo.getActiveChannel().getId());
            response.addData(message);
            response.setSuccess(true);
            response.setMessage("New message revieved.");
            String jsonResponse = gson.toJson(response);
            channelService.sendMessage(new ChannelMessage(channelKey, jsonResponse));
        }
        return response;
    }

    /**
	 * Calls {@link #doGet(HttpServletRequest, HttpServletResponse)}.
	 */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
