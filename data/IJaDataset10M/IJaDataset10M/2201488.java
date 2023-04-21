package com.cleangwt.client.application.view;

import java.util.Map;
import com.cleangwt.client.application.action.ActionEventBus;
import com.cleangwt.client.application.action.ActionView;
import com.cleangwt.client.validation.Validator;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Jess
 * @date 2011/11/9
 */
public class SimpleActionView extends ActionView {

    private SimplePanel shell = new SimplePanel();

    public SimpleActionView(ActionEventBus eventBus, Map<String, Validator> validators) {
        super(eventBus, validators);
        initWidget(shell);
    }

    public void setContent(Widget widget) {
        shell.setWidget(widget);
    }
}
