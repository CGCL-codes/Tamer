package pl.bristleback.scrumtable.actions;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.bristleback.scrumtable.services.WidgetService;
import pl.bristleback.scrumtable.utils.SendMessageSupport;
import pl.bristleback.scrumtable.vo.Widget;
import pl.bristleback.server.bristle.actions.AnnotatedRemoteAction;
import pl.bristleback.server.bristle.actions.RemoteAction;
import pl.bristleback.server.bristle.binding.Bind;
import pl.bristleback.server.bristle.binding.ObjectBinder;
import pl.bristleback.server.bristle.binding.Property;
import pl.bristleback.server.bristle.token.Tokenize;
import pl.bristleback.server.bristle.token.Tokenizer;
import pl.bristleback.server.bristle.utils.BristleTokenFactory;

/**
 * //@todo class description
 * <p/>
 * Created on: 23.04.11 14:03 <br/>
 *
 * @author Paweł Machowski
 */
@Controller
@AnnotatedRemoteAction(actionName = "moveWidget")
public class MoveWidgetAction implements RemoteAction {

    private static Logger log = Logger.getLogger(MoveWidgetAction.class.getName());

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private SendMessageSupport sender;

    @Bind(properties = { @Property(name = "position.top"), @Property(name = "position.left"), @Property(name = "id") })
    private ObjectBinder<Widget> binder;

    @Tokenize(parameters = { "id", "position.left", "position.top" }, deepSearch = true)
    private Tokenizer tokenHelper;

    public void performAction(WebSocketConnector connector, Token token) {
        Widget widget = binder.bind(token);
        Token sendToken = BristleTokenFactory.createJsonToken("widgetMoved", new JSONObject());
        tokenHelper.tokenize(sendToken, widget.getId(), widget.getPosition().getLeft(), widget.getPosition().getTop());
        sender.broadcastNotLogged(sendToken);
        widgetService.moveWidget(widget);
    }
}
