package com.expertria.glex.view;

import com.expertria.glex.event.Events.IEventListener;
import com.expertria.glex.state.IState;
import com.expertria.glex.state.StateHelper;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.css.ast.CssEval;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ImageBundle.Resource;

public abstract class StateView extends Composite implements IState {

    private final StateHelper stateHelper = new StateHelper() {

        @Override
        public void onStateChangeImpt(String oldState, String newState) {
            onStateChange(oldState, newState);
        }
    };

    public abstract void onStateChange(String oldState, String newState);

    @Override
    public void enterState(String state) {
        stateHelper.enterState(state);
    }

    @Override
    public String getCurrentState() {
        return stateHelper.getCurrentState();
    }

    @Override
    public void setCurrentState(String currentState) {
        this.stateHelper.setCurrentState(currentState);
    }

    public void addStateChangeListener(IEventListener e) {
        this.stateHelper.addStateChangeListener(e);
    }
}
