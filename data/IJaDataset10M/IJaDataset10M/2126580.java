package de.webmines.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import de.webmines.client.multiplayer.MultiPlayerService;
import de.webmines.client.multiplayer.MultiPlayerServiceAsync;
import de.webmines.shared.model.MineFieldState;
import de.webmines.shared.view.ViewEvent;
import de.webmines.shared.view.ViewEventListener;
import de.webmines.shared.view.ViewEventListeners;

public class MultiPlayerGame extends Composite implements de.webmines.shared.view.MultiPlayerGame, ViewEventListener {

    private static MultiPlayerGameUiBinder uiBinder = GWT.create(MultiPlayerGameUiBinder.class);

    @UiField
    VerticalPanel panel;

    @UiField
    Label elapsedTime;

    @UiField
    Label minesToFlag;

    private String name;

    private Field field;

    private Timer timer;

    private ViewEventListeners viewEvent;

    private Boolean sent = true;

    /**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
    private final MultiPlayerServiceAsync multiPlayerService = GWT.create(MultiPlayerService.class);

    interface MultiPlayerGameUiBinder extends UiBinder<Widget, MultiPlayerGame> {
    }

    public MultiPlayerGame() {
        initWidget(uiBinder.createAndBindUi(this));
        viewEvent = new ViewEventListeners();
        field = new Field();
        field.setViewEvent(viewEvent);
        viewEvent.addViewEventListener(this);
        panel.add(field);
    }

    private void initTimer() {
        timer = new Timer() {

            public void run() {
                updateField();
                updateElapsedTime();
            }
        };
        timer.scheduleRepeating(500);
    }

    private void updateElapsedTime() {
        if (sent) {
            sent = false;
            multiPlayerService.getElapsedSeconds(name, new AsyncCallback<Integer>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("failure: " + caught.toString());
                    timer.cancel();
                    sent = true;
                }

                @Override
                public void onSuccess(Integer result) {
                    elapsedTime.setText(result.toString());
                    sent = true;
                }
            });
        }
    }

    private void updateField() {
        if (sent) {
            sent = false;
            multiPlayerService.getMinesField(name, new AsyncCallback<MineFieldState[][]>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("failure: " + caught.toString());
                    timer.cancel();
                    sent = true;
                }

                @Override
                public void onSuccess(MineFieldState[][] result) {
                    field.setField(result);
                    sent = true;
                }
            });
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void start() {
        initTimer();
    }

    @Override
    public void setViewEvent(ViewEventListeners viewEventListeners) {
    }

    @Override
    public void viewEvent(ViewEvent e) {
        switch(e.getType()) {
            case LEFTCLICKED:
                multiPlayerService.leftClick(name, e.getX(), e.getY(), new AsyncCallback() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("failure: " + caught.toString());
                        timer.cancel();
                    }

                    @Override
                    public void onSuccess(Object result) {
                        sent = true;
                    }
                });
                break;
            case RIGHTCLICKED:
                multiPlayerService.rightClick(name, e.getX(), e.getY(), new AsyncCallback() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("failure: " + caught.toString());
                        timer.cancel();
                    }

                    @Override
                    public void onSuccess(Object result) {
                        sent = true;
                    }
                });
                break;
            default:
                break;
        }
    }
}
