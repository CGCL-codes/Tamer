package com.cleangwt.showcase.client.presenter;

import java.util.Map;
import com.cleangwt.client.annotation.Activity;
import com.cleangwt.client.application.action.ActionEventBus;
import com.cleangwt.client.application.presenter.ActivityPresenter;
import com.cleangwt.client.serialization.JsonBean;
import com.cleangwt.client.validation.Validator;
import com.cleangwt.showcase.client.view.DynamicValidatorView;

/**
 * @author Jess
 * @date 2011/10/20
 */
@Activity("dynamicvalidator")
public class DynamicValidatorPresenter extends ActivityPresenter<DynamicValidatorView> {

    /**
	 * @param entry
	 * @param activity
	 */
    public DynamicValidatorPresenter(String entry, String activity) {
        super(entry, activity);
        setActionValidator(DynamicValidatorView.ADD_USER, new Validator());
        setActionValidator(DynamicValidatorView.SUBMIT, new Validator());
    }

    @Override
    protected void onAction(String action) {
        if (DynamicValidatorView.SUBMIT.equals(action)) {
            view.submit();
        } else if (DynamicValidatorView.ADD_USER.equals(action)) {
            view.addUser();
        }
    }

    @Override
    protected DynamicValidatorView createView(ActionEventBus eventBus, Map<String, Validator> validators) {
        return new DynamicValidatorView(eventBus, validators);
    }

    @Override
    protected void onResponse(String action, JsonBean bean) {
    }
}
