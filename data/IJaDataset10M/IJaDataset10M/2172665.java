package com.cleangwt.showcase.client;

import java.util.Map;
import com.cleangwt.client.annotation.Entry;
import com.cleangwt.client.application.action.ActionEventBus;
import com.cleangwt.client.application.presenter.ManagerEntryPresenter;
import com.cleangwt.client.serialization.JsonBean;
import com.cleangwt.client.validation.Validator;

/**
 * @author Jess
 * @date 2011/9/23
 */
@Entry(activity = "actionwidget")
public class DemoEntryPresenter extends ManagerEntryPresenter<DemoEntryView> {

    /**
	 * @param entry
	 */
    public DemoEntryPresenter(String entry, String activity) {
        super(entry, activity);
    }

    @Override
    protected DemoEntryView createView(ActionEventBus eventBus, Map<String, Validator> validators) {
        return new DemoEntryView(eventBus, validators);
    }

    @Override
    protected void onAction(String action) {
    }

    @Override
    protected void onResponse(String action, JsonBean bean) {
    }
}
