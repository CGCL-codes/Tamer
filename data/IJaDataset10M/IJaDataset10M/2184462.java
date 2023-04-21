package annone.engine.test;

import annone.engine.DefaultChannel;
import annone.engine.DefaultRootChannel;
import annone.engine.Request;
import annone.engine.Response;
import annone.engine.ui.Action;
import annone.engine.ui.ActionGroup;
import annone.engine.ui.ActionRequest;
import annone.engine.ui.BlockingLayer;
import annone.engine.ui.CancelRequest;
import annone.engine.ui.DisplayableResponse;
import annone.engine.ui.ErrorMessage;
import annone.engine.ui.GetDisplayableRequest;
import annone.engine.ui.InformationMessage;
import annone.engine.ui.Input;
import annone.engine.ui.OpenDisplayableResponse;
import annone.engine.ui.View;
import annone.util.Text;

public class TestChannel extends DefaultChannel {

    public TestChannel(DefaultRootChannel rootChannel) {
        super(rootChannel);
    }

    @Override
    public Response perform(Request request) {
        Response response = performImpl(request);
        response.setRequestId(request.getId());
        return response;
    }

    protected Response performImpl(Request request) {
        if (request instanceof GetDisplayableRequest) {
            if (((GetDisplayableRequest) request).getDisplayableId() == 1) {
                BlockingLayer layer = new BlockingLayer(1);
                layer.setCaption(Text.get("Authentication required."));
                Input user = new Input(2);
                user.setCaption(Text.get("User"));
                user.setType(String.class);
                layer.addChild(user);
                Input password = new Input(3);
                password.setCaption(Text.get("Password"));
                password.setType(String.class);
                password.setOpaque(true);
                layer.addChild(password);
                ActionGroup actionGroup = new ActionGroup(4);
                Action okAction = new Action(5);
                actionGroup.addAction(okAction);
                layer.setActionGroup(actionGroup);
                return new DisplayableResponse(layer);
            }
        } else if (request instanceof ActionRequest) {
            ActionRequest actionRequest = (ActionRequest) request;
            if (actionRequest.getActionId() == 5) {
                Object user = actionRequest.getDisplayableValue(2);
                Object password = actionRequest.getDisplayableValue(3);
                if ("hello".equals(user) && "world".equals(password)) {
                    InformationMessage message = new InformationMessage(10);
                    message.setCaption(Text.get("Welcome"));
                    View text = new View(11);
                    text.setValue(Text.get("Welcome mr. \"Hello\" \"World\"."));
                    ActionGroup actionGroup = new ActionGroup(12);
                    Action okAction = new Action(13);
                    actionGroup.addAction(okAction);
                    message.setActionGroup(actionGroup);
                    message.addChild(text);
                    return new DisplayableResponse(message);
                } else {
                    ErrorMessage message = new ErrorMessage(14);
                    message.setCaption(Text.get("Authentication failed"));
                    View text = new View(15);
                    text.setValue(Text.get("Invalid user/password."));
                    ActionGroup actionGroup = new ActionGroup(15);
                    Action okAction = new Action(16);
                    actionGroup.addAction(okAction);
                    message.setActionGroup(actionGroup);
                    message.addChild(text);
                    return new DisplayableResponse(message);
                }
            } else if (actionRequest.getActionId() == 9) return new NoopResponse(); else if (actionRequest.getActionId() == 13) return new NoopResponse(); else if (actionRequest.getActionId() == 16) return new OpenDisplayableResponse(1);
        } else if (request instanceof CancelRequest) return new NoopResponse();
        ErrorMessage message = new ErrorMessage(6);
        message.setCaption(Text.get("Unhandled request"));
        View text = new View(7);
        text.setValue(Text.get("Unable to recognize request format."));
        ActionGroup actionGroup = new ActionGroup(8);
        Action okAction = new Action(9);
        actionGroup.addAction(okAction);
        message.setActionGroup(actionGroup);
        message.addChild(text);
        return new DisplayableResponse(message);
    }
}
