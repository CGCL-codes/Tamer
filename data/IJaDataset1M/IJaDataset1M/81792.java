package com.cleangwt.showcase.client.presenter;

import java.util.Map;
import com.cleangwt.client.action.ForwardAction;
import com.cleangwt.client.application.action.ActionEventBus;
import com.cleangwt.client.application.presenter.ModelPresenter;
import com.cleangwt.client.serialization.JsonBean;
import com.cleangwt.client.validation.Validator;
import com.cleangwt.showcase.client.view.UserEditView;
import com.cleangwt.web.action.WebAction;

/**
 * @author Jess
 * @date 2011/12/4
 */
public class UserEditPresenter extends ModelPresenter<UserEditView> {

    public UserEditPresenter(String clientId, ActionEventBus eventBus) {
        super(clientId, eventBus);
        setActionConfirmation(WebAction.DELETE, "Sure to delete?");
        setActionValidator(WebAction.SAVE, new Validator());
    }

    @Override
    protected void onAction(String action) {
        if (WebAction.SAVE.equals(action)) {
            request(action, view.getModifiedBean());
        } else if (WebAction.DELETE.equals(action)) {
            request(action, view.getModelBean());
        } else if (ForwardAction.RELOAD.equals(action)) {
            view.reload();
        } else if (ForwardAction.LIST.equals(action)) {
            parentEventBus.fireAction(action);
        }
    }

    @Override
    protected void onResponse(String action, JsonBean response) {
        if (WebAction.SAVE.equals(action)) {
            if (!view.isNewRecord()) {
                parentEventBus.fireAction(ForwardAction.LIST);
            }
        } else if (WebAction.DELETE.equals(action)) {
            parentEventBus.fireAction(ForwardAction.LIST);
        }
    }

    @Override
    protected UserEditView createView(ActionEventBus eventBus, Map<String, Validator> validators) {
        return new UserEditView(eventBus, validators);
    }
}
