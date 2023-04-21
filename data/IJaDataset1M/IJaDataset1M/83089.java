package com.hvilela.client.input.actions;

import com.hvilela.client.jmonkey.GameAgent;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

/**
 * @author Henrique
 */
public class DriftAction extends KeyInputAction {

    private GameAgent agent;

    public DriftAction(GameAgent vehicle) {
        this.agent = vehicle;
    }

    public void performAction(InputActionEvent evt) {
        agent.drift(evt.getTime());
    }
}
