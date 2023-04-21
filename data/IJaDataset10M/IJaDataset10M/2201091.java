package com.desktopdeveloper.pendulum.samples.application;

import com.desktopdeveloper.pendulum.components.listeners.ActionFeedbackListener;
import com.desktopdeveloper.pendulum.components.panels.PPanel;
import com.desktopdeveloper.pendulum.core.binder.ComponentBinder;
import com.desktopdeveloper.pendulum.core.binder.ComponentBinderImpl;
import com.desktopdeveloper.pendulum.core.controller.Controller;
import com.desktopdeveloper.pendulum.core.factory.ObjectFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: stuart_e
 * Date: 27-Jul-2004
 * Time: 09:49:30
 */
public class ListPanel extends PPanel {

    private ComponentBinder binderImpl;

    public ListPanel(Controller controller, ObjectFactory factory) {
        super(controller, factory);
    }

    protected void buildPanel() {
        binderImpl = new ComponentBinderImpl();
        final JList list = (JList) factory.control(JList.class).add(binderImpl.newBinding("model", "model")).create();
        final JButton button = (JButton) factory.control(JButton.class).add(binderImpl.newBinding("text", "buttonText")).setProperty("text", "Hello I'm a button").create();
        button.addActionListener(new ActionFeedbackListener() {

            public void actionPerformed(ActionEvent e) {
                binderImpl.push(controller);
                controller.postData("testApplication.StuffAction", this);
            }

            public void receiveFeedback() {
                binderImpl.refresh(controller, null);
            }
        });
        setMinimumSize(new Dimension(0, 0));
        setPreferredSize(new Dimension(100, 400));
        add(list, BorderLayout.NORTH);
        add(button, BorderLayout.CENTER);
    }
}
