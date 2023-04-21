package net.saim.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/** @author Konrad Höffner */
public abstract class UserInputPanel extends Composite {

    protected final GreetingServiceAsync service;

    public enum State {

        INACTIVE, EDITABLE, COMPLETE
    }

    protected SequencePanel sequencePanel = null;

    protected State state = State.INACTIVE;

    protected int step;

    protected final FlowPanel panel = new FlowPanel();

    protected final Image completeImage = new Image(ImageBundle.INSTANCE.yesIcon());

    protected final Image incompleteImage = new Image(ImageBundle.INSTANCE.noIcon());

    protected final FlowPanel inputPanel = new FlowPanel();

    protected final HorizontalPanel buttonPanel = new HorizontalPanel();

    protected final Button completeStepButton = new Button("OK");

    protected final Button backButton = new Button("go back one step");

    protected final UserInputClickHandler clickHandler = new UserInputClickHandler();

    protected void onCompletion() {
    }

    ;

    public void setBackButtonVisible(boolean visible) {
        backButton.setVisible(visible);
    }

    protected class UserInputClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent e) {
            if (e.getSource() == completeStepButton) {
                setState(State.COMPLETE);
                onCompletion();
                sequencePanel.forward();
                return;
            }
            if (e.getSource() == backButton) {
                setState(State.INACTIVE);
                sequencePanel.back();
                return;
            }
        }
    }

    public void setSequencePanel(SequencePanel sequencePanel) {
        this.sequencePanel = sequencePanel;
    }

    /**
	 * @param state transition into this state
	 * @return true if the transition was successful, false if not
	 */
    public boolean setState(State state) {
        switch(state) {
            case INACTIVE:
                {
                    this.state = State.INACTIVE;
                    inputPanel.setVisible(false);
                    buttonPanel.setVisible(false);
                    refresh();
                    return true;
                }
            case EDITABLE:
                {
                    this.state = State.EDITABLE;
                    inputPanel.setVisible(true);
                    buttonPanel.setVisible(true);
                    refresh();
                    return true;
                }
            case COMPLETE:
                {
                    if (this.state == State.INACTIVE) {
                        return false;
                    }
                    if (this.state != State.COMPLETE && this.step == sequencePanel.size()) {
                        return false;
                    }
                    this.state = State.COMPLETE;
                    inputPanel.setVisible(false);
                    buttonPanel.setVisible(false);
                    refresh();
                    return true;
                }
        }
        return false;
    }

    public static char getCircledCharacter(int step) {
        return (char) (9311 + step);
    }

    protected abstract String getCompleteDescription();

    public void refresh() {
    }

    public UserInputPanel(String title, GreetingServiceAsync service) {
        this.service = service;
        panel.add(inputPanel);
        panel.add(buttonPanel);
        buttonPanel.add(completeStepButton);
        buttonPanel.add(backButton);
        if (step == 1) {
            backButton.setVisible(false);
        }
        completeStepButton.addClickHandler(clickHandler);
        backButton.addClickHandler(clickHandler);
        refresh();
        initWidget(panel);
        this.setTitle(title);
    }
}
